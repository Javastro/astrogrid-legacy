/*$Id: CommunityImpl.java,v 1.1 2005/08/11 10:15:00 nw Exp $
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

import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.UI;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.ui.script.LoginFactory;
import org.astrogrid.ui.script.ScriptEnvironment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** Community Service implementation
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 */
public class CommunityImpl implements CommunityInternal  {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommunityImpl.class);

    /** Construct a new Community
     * 
     */
    public CommunityImpl(BrowserControl browser,UI ui) {       
        this.browser = browser;
        this.ui = ui;
        loginDialogue = new LoginDialogue();
        ui.setStatusMessage("Not Logged In");
    }
    protected final UI ui;
    protected final BrowserControl browser;
    protected final LoginDialogue loginDialogue;

    public void login(String username,String password, String community) throws SecurityException, ServiceException {
        loginDialogue.setUser(username);
        loginDialogue.setPassword(password);
        loginDialogue.setCommunity(community);
        authenticate();
    }

    public void logout() {
        env = null;
        loginDialogue.setPassword("");
        UserLoginEvent e= new UserLoginEvent(false,this);
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            ((UserLoginListener)i.next()).userLogout(e);
        }        
        ui.setStatusMessage("Not Logged In");
        ui.setLoggedIn(false);
    }
    
    private ScriptEnvironment env;

    public ScriptEnvironment getEnv() {
        guiLogin();
        if (! isLoggedIn()) {
            throw new RuntimeException("Cannot proceed - failed to login");
        }
        return this.env;
    }
    
    public boolean isLoggedIn() {
        return env != null;
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
            if (! (loginDialogue.showDialog(null))) { // cancel was hit.
                break;
            }
            try {
                authenticate();
            } catch (Exception e) {
                logger.info(e);
                e.printStackTrace();
                loginDialogue.showError(null,e.getMessage());
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
        env = LoginFactory.login(loginDialogue.getUser(),loginDialogue.getCommunity(),loginDialogue.getPassword());
        ui.setStatusMessage("Logged in as " + env.getUserIvorn());
        ui.setLoggedIn(true);
        UserLoginEvent e= new UserLoginEvent(true,this);
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            ((UserLoginListener)i.next()).userLogin(e);
        }
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
        } finally {
            if (!isLoggedIn()) {
                ui.setStatusMessage("");
                ui.setLoggedIn(false);
            }
        } 
        return true;
    }

    protected Set listeners = new HashSet();
    
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


}


/* 
$Log: CommunityImpl.java,v $
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