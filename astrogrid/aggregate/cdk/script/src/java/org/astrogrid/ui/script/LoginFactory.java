/*$Id: LoginFactory.java,v 1.1 2004/12/20 15:59:03 nw Exp $
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

import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;

/** Class that constructs a scripting environment, from results of logging in.
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Dec-2004
 *
 */
public class LoginFactory {

    /** Construct a new LoginFactory
     * 
     */
    private LoginFactory() {
        super();
    }
    
    /** create a script environment by logging into astrogird 
     * 
     * @param username user identity
     * @param community community the user belongs to
     * @param password password for this user
     * @return an initialized scripting environment
     * @throws RegistryException
     * @throws CommunityIdentifierException
     * @throws CommunitySecurityException
     * @throws CommunityServiceException
     * @throws CommunityResolverException
     */
    public static ScriptEnvironment login(String username,String community,String password) throws CommunityResolverException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException, RegistryException {
        return new LoginScriptEnvironment(username,community,password);
    }

}


/* 
$Log: LoginFactory.java,v $
Revision 1.1  2004/12/20 15:59:03  nw
added factory class to simplfy creation of scripting env. forces login.
 
*/