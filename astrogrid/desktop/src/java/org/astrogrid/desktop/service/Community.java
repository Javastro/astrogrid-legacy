/*$Id: Community.java,v 1.2 2005/02/22 01:10:31 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.desktop.ui.BrowserControl;
import org.astrogrid.desktop.ui.LoginDialogue;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.ui.script.LoginFactory;
import org.astrogrid.ui.script.ScriptEnvironment;
import org.astrogrid.desktop.service.conversion.*;
import org.astrogrid.desktop.service.annotation.*;

import java.util.Observable;

/** Community Service
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *@@ServiceDoc("community","user management and authentication")
 */
public class Community extends Observable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Community.class);

    /** Construct a new Community
     * 
     */
    public Community(BrowserControl browser) {       
        this.browser = browser;
        loginDialogue = new LoginDialogue();
    }
    protected final BrowserControl browser;
    protected final LoginDialogue loginDialogue;
    
    /**@throws RegistryException
     * @throws CommunityIdentifierException
     * @throws CommunitySecurityException
     * @throws CommunityServiceException
     * @throws CommunityResolverException
     * @@MethodDoc("login","login to astrogrid");
       @@.return ReturnDoc("Success code",rts=BooleanResultTransformerSet.getInstance())
       @@.username ParamDoc("username","user to log in as")
       @@.password ParamDoc("password","Password for this user")
       @@.community ParamDoc("community","Community the user belongs to")
     */
    public boolean login(String username,String password, String community) throws CommunityResolverException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException, RegistryException {
        loginDialogue.setUser(username);
        loginDialogue.setPassword(password);
        loginDialogue.setCommunity(community);
        return authenticate();
    }
    
    /** @@MethodDoc("logout","logout of astrogrid")
       @@.return ReturnDoc("Success code",rts=BooleanResultTransformerSet.getInstance())
     */
    public boolean logout() {
        env = null;
        loginDialogue.setPassword("");
        setChanged();
        notifyObservers();
        return true;
    }
    
    private ScriptEnvironment env;
    
    public boolean isLoggedIn() {
        return env != null;
    }
    
    
    
    /** called by other components - login to the system if needed */
    public void loginIfNecessary() {
        guiLogin();
        if (! isLoggedIn()) {
            throw new RuntimeException("Cannot proceed - failed to login");
        }
    }

    public void guiLogin() {
        while(!isLoggedIn()) {
            if (! (loginDialogue.showDialog(null))) { // cancel was hit.
                break;
            }
            try {
                authenticate();
            } catch (Exception e) {
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
    private boolean authenticate() throws CommunityResolverException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException, RegistryException {
        env = LoginFactory.login(loginDialogue.getUser(),loginDialogue.getCommunity(),loginDialogue.getPassword());
        setChanged();
        notifyObservers();
        return true;
    }

    public ScriptEnvironment getEnv() {
        return this.env;
    }
}


/* 
$Log: Community.java,v $
Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/