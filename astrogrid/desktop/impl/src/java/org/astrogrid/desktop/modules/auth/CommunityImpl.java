/*$Id: CommunityImpl.java,v 1.11 2008/03/27 10:06:17 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.UserInformation;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.system.UI;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContextImpl;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.security.SecurityGuard;

/** Community Service implementation
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 01-Feb-2005
 */
public class CommunityImpl implements CommunityInternal {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommunityImpl.class);

    /** Construct a new Community
     * 
     */
    public CommunityImpl(UIContext ui,LoginDialogue loginDialogue, SnitchInternal snitch, String trustedCertificates) {       
        this.ui = ui;
        this.loginDialogue = loginDialogue;
        this.snitch = snitch;
        ui.setStatusMessage("Not Logged In");
        logger.info("Trusted certificates directory set to: '" + trustedCertificates + "'");
        LoginFactory.declareTrustedCertificates(trustedCertificates);
    }
    protected final UIContext ui;
    protected final SnitchInternal snitch;
    protected final LoginDialogue loginDialogue;
    private volatile UserInformation userInformation;
    protected volatile SecurityGuard guard;

    public void login(String username,String password, String community) throws SecurityException, ServiceException {
    	// Fix suggested by JDT.
    	if (isLoggedIn()) { //already logged in.
    		return;
    	}
        try {
            authenticate(LoginFactory.mkUserInfo(community,username,password));
        } catch (URISyntaxException x) {
            throw new ServiceException("Failed to create user ivorn - invalid syntax",x);
        }
    }

    public void logout() {
        userInformation = null;
        notifyListeners(false);
        ui.setStatusMessage("Not Logged In");
        ui.getLoggedInModel().setActionCommand("Not logged in");        
        ui.setLoggedIn(false);
    }
    


    
    public UserInformation getUserInformation()  {
    	return userInformation;
    }    
    
    public boolean isLoggedIn() {
        return userInformation != null;
    }

    
    public void guiLogin() {
        loginDialogue.login();
    }

    private boolean authenticate(UserInformation proposed) throws SecurityException, ServiceException{
        logger.info("Logging in " + 
                proposed.getName() +
                "@" +
                proposed.getCommunity());
        try {
            ui.setStatusMessage("Logging in..");
            ScriptEnvironment env = LoginFactory.login(proposed);
            // if we've gotten this far, we've successfullly logged in.
            this.guard = env.getSecurityGuard();
            this.userInformation = proposed;
           ui.findMainWindow().showTransientMessage("Logged in", "as " + userInformation.getId());
           //want to produce a tooltip from the authentication information when logged in
           // however, donn't want to build this for all views - prefer to build it once in the model.
           // however, there's no place to pass this info back to the views - so using a hack by stuffing it in 'actionCommand'
           // of the login model.
           ui.getLoggedInModel().setActionCommand("<html>Logged in as" 
                       + "<br>User: " +userInformation.getName()
                       + "<br>Community: " + userInformation.getCommunity()
                       +  "<br>DN: " + 
                           (guard != null && guard.getX500Principal()!= null
                           ? guard.getX500Principal().getName(X500Principal.CANONICAL)
                           : "not available")
                       );
           // snitch now they've successfully logged in.
           ui.setLoggedIn(true);
           Map m = new HashMap();
           m.put("username",userInformation.getCommunity() + "/" + userInformation.getName());
           snitch.snitch("LOGIN",m);           
           notifyListeners(true);
        } catch (CommunityResolverException e) {
            throw new ServiceException(e);
        } catch (CommunityServiceException e) {
            throw new ServiceException(e);
        } catch (CommunitySecurityException e) {
            throw new SecurityException(e.getMessage());
        } catch (CommunityIdentifierException e) {
            throw new SecurityException(e.getMessage());
        } catch (RegistryException e) {
            throw new ServiceException(e);
        } finally {
            if (!isLoggedIn()) {
                ui.setStatusMessage("");
                ui.getLoggedInModel().setActionCommand("Not logged in");
                ui.setLoggedIn(false);
            }
        } 

        logger.info("Authenticated");        
        return true;
    }

    /**
     * 
     */
    private void notifyListeners(boolean loggedIn) {
        UserLoginEvent e= new UserLoginEvent(loggedIn,this);
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
        	UserLoginListener l = null;
            try {
                 l = (UserLoginListener)i.next();
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
            	if (l != null) {
            		listeners.remove(l);
            	}
            }
        }
    }
    protected Set listeners = new HashSet();
    
  
    public void addUserLoginListener(UserLoginListener l) {
        listeners.add(l);
    }

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
     */
    public SecurityGuard getSecurityGuard() {
      return (this.guard == null)? new SecurityGuard() : this.guard;
    }

}


/* 
$Log: CommunityImpl.java,v $
Revision 1.11  2008/03/27 10:06:17  nw
 add display of login certificate

Revision 1.10  2008/03/26 13:21:40  nw
 add display of login certificate

Revision 1.9  2008/03/12 11:37:36  nw
Complete - task 270: improve error message when login fails.

Revision 1.8  2007/10/22 07:24:19  nw
altered login dialogue to be a full UIComponent.

Revision 1.7  2007/10/12 10:57:52  nw
refactored exception formatting code.

Revision 1.6  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.5  2007/09/11 12:07:27  nw
potential concurrency fixes.

Revision 1.4  2007/09/04 13:38:37  nw
added debugging for EDT, and adjusted UI to not violate EDT rules.

Revision 1.3  2007/06/18 16:31:19  nw
check if we're logged in before logging in.

Revision 1.2  2007/04/18 15:47:08  nw
tidied up voexplorer, removed front pane.

Revision 1.1  2007/03/22 19:01:18  nw
added support for sessions and multi-user ar.

Revision 1.13  2007/01/29 16:45:08  nw
cleaned up imports.

Revision 1.12  2007/01/29 11:11:35  nw
updated contact details.

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