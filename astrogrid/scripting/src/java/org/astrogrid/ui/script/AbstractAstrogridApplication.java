/*$Id: AbstractAstrogridApplication.java,v 1.2 2005/01/13 15:18:55 nw Exp $
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

    protected Toolbox astrogrid;
    protected User user;
    protected Account account;
    protected Ivorn homeIvorn;
    protected Ivorn userIvorn;

    /** Construct a new AbstractUIApplication
     * 
     */
    public AbstractAstrogridApplication() {
        super();
    }

    protected final void login(String username,String community,String password) throws CommunityResolverException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException, RegistryException {

          ScriptEnvironment env = LoginFactory.login(username,community,password);
          astrogrid = env.getAstrogrid();
          user = env.getUser();
          account = env.getAccount();
          homeIvorn = env.getHomeIvorn();
          userIvorn = env.getUserIvorn();

        }    
    
}


/* 
$Log: AbstractAstrogridApplication.java,v $
Revision 1.2  2005/01/13 15:18:55  nw
moved classes from scripting cdk - belong here.

Revision 1.1.2.1  2005/01/13 11:46:34  nw
saved work that I've not got time to do
 
*/