/*$Id: Community.java,v 1.5 2005/08/05 11:46:55 nw Exp $
 * Created on 18-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.astrogrid;

import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;

/**  Interface to community service of astrogird - login and authentication.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2005
 *
 */
public interface Community {

    /** login to astrogrid
     * 
     * @param username 
     * @param password
     * @param community
     * @throws SecurityException if login fails due to incorrect credentials
     * @throws ServiceException if error occurs while communicating with server.
     */
    public abstract void login(String username, String password, String community)
            throws SecurityException, ServiceException;

    /** log out of astrogrid */
    public abstract void logout();

    /** returns true if logged in */
    public abstract boolean isLoggedIn();

    /** show the login dialogue, prompt the user for input, and then log in */
    public abstract void guiLogin();
    
    /** add a listener, that will be notified when the user logs in or out */
    public void addUserLoginListener(UserLoginListener l) ;
    
    /** remove a previously registered listener */
    public void removeUserLoginListener(UserLoginListener l);


}

/* 
 $Log: Community.java,v $
 Revision 1.5  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.4  2005/05/12 15:59:08  clq2
 nww 1111 again

 Revision 1.2.8.1  2005/05/11 11:55:19  nw
 javadoc

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:11  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/22 12:04:03  nw
 working draft of system and ag components.

 Revision 1.1.2.1  2005/03/18 15:47:37  nw
 worked in swingworker.
 got community login working.
 
 */