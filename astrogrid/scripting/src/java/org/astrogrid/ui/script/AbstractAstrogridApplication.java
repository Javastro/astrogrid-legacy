/*$Id: AbstractAstrogridApplication.java,v 1.3 2005/01/14 00:42:58 nw Exp $
 * Created on 11-Jan-2005
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
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.scripting.Toolbox;
import org.astrogrid.store.Ivorn;

/** Abstract class for client applications of astrogrid
 * has methods to login, create astrogrid environment objects, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Jan-2005
 *
 */
public class AbstractAstrogridApplication {

    public Toolbox astrogrid;
    public User user;
    public Account account;
    public Ivorn homeIvorn;
    public Ivorn userIvorn;
    public String password;

    /** Construct a new AbstractUIApplication
     * 
     */
    public AbstractAstrogridApplication() {
        super();
    }

    public final void login(String username,String community,String password) throws CommunityResolverException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException, RegistryException {

          ScriptEnvironment env = LoginFactory.login(username,community,password);
          this.astrogrid = env.getAstrogrid();
          this.user = env.getUser();
          this.account = env.getAccount();
          this.homeIvorn = env.getHomeIvorn();
          this.userIvorn = env.getUserIvorn();
          this.password = env.getPassword();
        }    
    
}


/* 
$Log: AbstractAstrogridApplication.java,v $
Revision 1.3  2005/01/14 00:42:58  nw
worked on this a bit more. got it sorted.

Revision 1.2  2005/01/13 15:18:55  nw
moved classes from scripting cdk - belong here.

Revision 1.1.2.1  2005/01/13 11:46:34  nw
saved work that I've not got time to do
 
*/