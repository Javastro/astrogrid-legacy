/*$Id: LoginScriptEnvironment.java,v 1.2 2004/12/22 18:57:01 nw Exp $
 * Created on 20-Dec-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.ui.script;

import org.astrogrid.community.User;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.CommunityPasswordResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.scripting.Toolbox;
import org.astrogrid.store.Ivorn;

/** Implementation of ScriptEnvironment that enforces login -- a bit crap, but will have to do until we have proper security.
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Dec-2004
 *
 */
final class LoginScriptEnvironment implements ScriptEnvironment {

    /** Construct a new LoginScriptEnvironment
     * @throws RegistryException
     * @throws CommunityIdentifierException
     * @throws CommunitySecurityException
     * @throws CommunityServiceException
     * @throws CommunityResolverException
     * 
     */
    LoginScriptEnvironment(String name,String community,String password) throws CommunityResolverException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException, RegistryException {

        ag = new Toolbox();
        // handle nulls.
        if (name == null) {
            name = ag.getSystemConfig().getString("username");            
        }
        if (community == null) {
            community = ag.getSystemConfig().getString("org.astrogrid.community.ident");
        }
        if (password == null) {
            password = ag.getSystemConfig().getString("password");
        }
        this.password = password;        
        CommunityPasswordResolver security = new CommunityPasswordResolver();
        Account acc = new Account();
        Group group = new Group();
        group.setName("login-script-env-users");
        acc.setName(name);
        acc.setCommunity(community);
        group.setCommunity(community);
        creds = new Credentials();
        creds.setAccount(acc);
        creds.setGroup(group);
        userIvorn = new Ivorn(community,name,"");
        creds.setSecurityToken(security.checkPassword(userIvorn.toString(),password).getToken());
        // ok, we've passed authentication. create everything else now.
        homeIvorn = new Ivorn(community,name,name + "/");
        u = new User(name,community,creds.getGroup().getName(),creds.getSecurityToken());
    }

    private final Credentials creds;
    private final Toolbox ag;
    private final User u;
    private final Ivorn userIvorn;
    private final Ivorn homeIvorn;
    private final String password;

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getAstrogrid()
     */
    public Toolbox getAstrogrid() {
       return ag;
    }

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getUser()
     */
    public User getUser() {
        return u;
    }

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getAccount()
     */
    public Account getAccount() {
        return creds.getAccount();
    }

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getUserIvorn()
     */
    public Ivorn getUserIvorn() {
        return userIvorn;
    }

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getHomeIvorn()
     */
    public Ivorn getHomeIvorn() {
        return homeIvorn;
    }

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getPassword()
     */
    public String getPassword() {
        return password;
    }

}


/* 
$Log: LoginScriptEnvironment.java,v $
Revision 1.2  2004/12/22 18:57:01  nw
fixed bug in pasword storage.

Revision 1.1  2004/12/20 15:59:03  nw
added factory class to simplfy creation of scripting env. forces login.
 
*/