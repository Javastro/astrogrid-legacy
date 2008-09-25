/*$Id: Community.java,v 1.6 2008/09/25 16:02:04 nw Exp $
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

/** AR Service: Single sign-on and authentication.
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 18-Mar-2005
 * @service astrogrid.community
 * @see <a href='http://www.ivoa.net/Documents/latest/SSOAuthMech.html'>IVOA Single-Sign On Specification</a>
 */
public interface Community {

    /** login to virtual observatory.
     * 
     * {@example login("EdwardWoodward","ewarwoowar","uk.ac.le.star") }
     * @param username user name (e.g. {@code fredbloggs})
     * @param password password for this user
     * @param community community the user is registered with (e.g. {@code uk.ac.astogrid} )
     * @throws SecurityException if login fails due to incorrect credentials
     * @throws ServiceException if error occurs while communicating with server.
     */
    public abstract void login(String username, String password, String community)
            throws SecurityException, ServiceException;

    
    /** Access information about who the AR is currently logged in as.
     * @note This method forces login if not already logged in
     * @return information about the current user.
     */
    public UserInformation getUserInformation();
    
    /** logout of the virtual observatory */
    public abstract void logout();

    /** check whether AR is currently logged in.
     * 
     * @return true if the user is logged in
     */
    public abstract boolean isLoggedIn();

    /** display the login dialogue to prompt the user for input, and then log in. */
    public abstract void guiLogin();
    
    /** register a listener, that will be notified when the user logs in or out 
     * @xmlrpc method unavailable - callbacks are not technically possible*/
    public void addUserLoginListener(UserLoginListener l) ;
    
    /** remove a previously registered listener
     * @xmlrpc method unavailable - callbacks are not technically possible */
    public void removeUserLoginListener(UserLoginListener l);


}

/* 
 $Log: Community.java,v $
 Revision 1.6  2008/09/25 16:02:04  nw
 documentation overhaul

 Revision 1.5  2007/01/24 14:04:44  nw
 updated my email address

 Revision 1.4  2006/02/02 14:19:48  nw
 fixed up documentation.

 Revision 1.3  2005/08/25 16:59:44  nw
 1.1-beta-3

 Revision 1.2  2005/08/12 08:45:16  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

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