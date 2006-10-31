/*$Id: CommunityImpl.java,v 1.11 2006/10/31 12:55:40 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserInformation;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.security.SecurityGuard;

/** Community Service implementation
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 */
public class CommunityImpl implements CommunityInternal {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommunityImpl.class);

    /** Construct a new Community
     * 
     */
    public CommunityImpl(UIInternal ui,LoginDialogue loginDialogue, SnitchInternal snitch, String trustedCertificates) {       
        this.ui = ui;
        this.loginDialogue = loginDialogue;
        this.snitch = snitch;
        ui.setStatusMessage("Not Logged In");
        logger.info("Trusted certificates directory set to: '" + trustedCertificates + "'");
        LoginFactory.declareTrustedCertificates(trustedCertificates);
    }
    protected final UIInternal ui;
    protected final SnitchInternal snitch;
    protected final LoginDialogue loginDialogue;
    protected UserInformation userInformation;
    protected SecurityGuard guard;

    public void login(String username,String password, String community) throws SecurityException, ServiceException {
        loginDialogue.setUser(username);
        loginDialogue.setPassword(password);
        loginDialogue.setCommunity(community);
        authenticate();
    }

    public void logout() {
        userInformation = null;
        loginDialogue.setPassword("");
        notifyListeners(false);
        ui.setStatusMessage("Not Logged In");
        ui.setLoggedIn(false);
    }

    
    public UserInformation getUserInformation()  {
       guiLogin();
       if (! isLoggedIn()) {
           throw new RuntimeException("Cannot proceed - failed to login");
       }
       return userInformation;
    }    
    
    public boolean isLoggedIn() {
        return userInformation != null;
    }
    /** @todo strictly speaking this should call swing worker to put this on the event queue thread. I think  
     * 
     * however, it is just a dialogue. try putting the method under synchronization - see what this does..
     * - seems to make it better. means that if  2 threads are trying to get the user to log in, only one will be able to - 
     * second thread will wait.
     * */
    
    public synchronized void guiLogin() {
        if (isLoggedIn()) { // we're already logged in - no need to do anything.
            return;
        }
        while(!isLoggedIn()) {
            if (! (loginDialogue.showDialog())) { // cancel was hit.
                break;
            }
            try {
            //@todo move this to debug, once the novelty wares off :)
            	logger.info("About to authenticate at the community...");
                authenticate();
                logger.info("Authenticated");
            } catch (Exception e) {
            	logger.info("Authentication failed.");
                UIComponentImpl.showError(null,"Failed to login",e);
            }
        }                    
    }
     
    /** uses fields in loginDialogue to autenticate against the server 
     * @throws RegistryException
     * @throws CommunityIdentifierException
     * @throws CommunitySecurityException
     * @throws CommunityServiceException
     * @throws CommunityResolverException*/
    private boolean authenticate() throws SecurityException, ServiceException{
        logger.info("In authenticate");
        try {
            ui.setStatusMessage("Logging in..");

            logger.info("Logging in " + 
                        loginDialogue.getUser() +
                        "@" +
                        loginDialogue.getCommunity());
            ScriptEnvironment env = LoginFactory.login(loginDialogue.getUser(),loginDialogue.getCommunity(),loginDialogue.getPassword());
            this.guard = env.getSecurityGuard();
            userInformation = new UserInformation(
                new URI(env.getUserIvorn().toString())
                ,loginDialogue.getUser()
                ,loginDialogue.getPassword()
                ,loginDialogue.getCommunity()
               );
           ui.setStatusMessage("Logged in as " + env.getUserIvorn());
           ui.setLoggedIn(true);
           // snitch now they've successfully logged in.
           Map m = new HashMap();
           m.put("username",loginDialogue.getCommunity() + "/" + loginDialogue.getUser());
           snitch.snitch("LOGIN",m);           
           notifyListeners(true);
        } catch (CommunityResolverException e) {
            throw new ServiceException(e);
        } catch (CommunityServiceException e) {
            throw new ServiceException(e);
        } catch (CommunitySecurityException e) {
            throw new SecurityException(e);
        } catch (CommunityIdentifierException e) {
            throw new SecurityException(e);
        } catch (RegistryException e) {
            throw new ServiceException(e);
        } catch (URISyntaxException e) {
            throw new ServiceException(e);
        } finally {
            if (!isLoggedIn()) {
                ui.setStatusMessage("");
                ui.setLoggedIn(false);
            }
        } 
        return true;
    }

    /**
     * 
     */
    private void notifyListeners(boolean loggedIn) {
        UserLoginEvent e= new UserLoginEvent(loggedIn,this);
        //for (Iterator i = listeners.iterator(); i.hasNext(); ) {
        for (int i = 0; i < listeners.size(); i++) {    
            try {
                UserLoginListener l = (UserLoginListener)listeners.get(i);
                if (l != null) {
                    if (loggedIn == true) {
                        l.userLogin(e);
                    } else {
                        l.userLogout(e);
                    }
                }
            } catch (Throwable t) { 
                // oh well, something has gone wrong with this listener - probably disconnected.
                // lets remove if from the listener list
                listeners.remove(i);
            }
        }
    }
    protected List listeners = new ArrayList();
    
    /**
     * @see org.astrogrid.acr.astrogrid.Community#addUserLoginListener(org.astrogrid.desktop.modules.ag.UserLoginListener)
     */
    public void addUserLoginListener(UserLoginListener l) {
        listeners.add(l);
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Community#removeUserLoginListener(org.astrogrid.desktop.modules.ag.UserLoginListener)
     */
    public void removeUserLoginListener(UserLoginListener l) {
        listeners.remove(l);
    }

    /**
     * Reveals the credentials and principals that were cached when the user 
     * logged in. Changes to the external copy affect the cached copy. If the
     * user is not logged in, then the returned object will contain no
     * credentials or principals; it will never be null.
     *
     * @return - The credentials and principals.
     * @see org.astrogrid.acr.astrogrid.Community#getSecurityGuard()
     */
    public SecurityGuard getSecurityGuard() {
      return (this.guard == null)? new SecurityGuard() : this.guard;
    }

}


/* 
$Log: CommunityImpl.java,v $
Revision 1.11  2006/10/31 12:55:40  nw
added logging message

Revision 1.10  2006/08/31 21:28:59  nw
doc fix.

Revision 1.9  2006/07/20 12:29:54  nw
fixed to snitch only when login is successful.

Revision 1.8  2006/06/15 18:20:54  nw
merge of desktop-gtr-1537

Revision 1.7.10.2  2006/06/09 17:16:18  gtr
It has an extra method: getSecurityGuard(). The guard is copied out of the LoginSriptenvironment and cached during CommunityImpl.authenticate();

Revision 1.7.10.1  2006/06/09 11:11:58  gtr
It implements CommunityInternal instead of just Community. This provides access to credentials for the rest of the implementation without exposing them to AR clients.

Revision 1.7  2006/05/26 15:22:54  nw
implemented snitching.

Revision 1.6  2006/05/13 16:34:55  nw
merged in wb-gtr-1537

Revision 1.5  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.4  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.3.34.2  2006/04/14 02:45:01  nw
finished code.
extruded plastic hub.

Revision 1.3.34.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.3  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.7  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.6  2005/07/08 11:08:02  nw
bug fixes and polishing for the workshop

Revision 1.5  2005/05/12 15:59:12  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 14:25:24  nw
javadoc, improved result transformers for xml

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.5  2005/04/06 15:04:09  nw
added new front end - more modern, with lots if icons.

Revision 1.1.2.4  2005/04/04 16:43:48  nw
made frames remember their previous positions.
synchronized guiLogin, so only one login box ever comes up.
made refresh action in jobmonitor more robust

Revision 1.1.2.3  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.2  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.1  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/