/*$Id: Community.java,v 1.2 2005/04/13 12:59:11 nw Exp $
 * Created on 18-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.ui.script.ScriptEnvironment;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2005
 *
 */
public interface Community {

    public abstract boolean login(String username, String password, String community)
            throws CommunityResolverException, CommunityServiceException,
            CommunitySecurityException, CommunityIdentifierException, RegistryException;

    public abstract void logout();

    public abstract boolean isLoggedIn();

    public abstract void guiLogin();

    public abstract ScriptEnvironment getEnv();
    
    public void addUserLoginListener(UserLoginListener l) ;
    
    public void removeUserLoginListener(UserLoginListener l);
}

/* 
 $Log: Community.java,v $
 Revision 1.2  2005/04/13 12:59:11  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/22 12:04:03  nw
 working draft of system and ag components.

 Revision 1.1.2.1  2005/03/18 15:47:37  nw
 worked in swingworker.
 got community login working.
 
 */