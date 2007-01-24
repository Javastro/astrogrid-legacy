/*$Id: Community.java,v 1.5 2007/01/24 14:04:44 nw Exp $
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

/** astogrid identity and authentication.
 * 
 * At the moment provides login ability. Later will provide access to permissioning and quota information for the current user.
 * <img src="doc-files/login.png"/>
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 18-Mar-2005
 * @service astrogrid.community
 */
public interface Community {

    /** login to astrogrid - identify yourself
     * 
     * @param username user name (e.g. <tt>fredbloggs</tt>)
     * @param password password for this user
     * @param community community the user is registered with (e.g. <tt>uk.ac.astogrid</tt> )
     * @throws SecurityException if login fails due to incorrect credentials
     * @throws ServiceException if error occurs while communicating with server.
     */
    public abstract void login(String username, String password, String community)
            throws SecurityException, ServiceException;

    
    /** access information about the currently logged in user.
     * 
     * <b>This method forces login if not already logged in.</b>
     * @return information about the current user.
     */
    public UserInformation getUserInformation();
    
    /** log current user out of astrogrid */
    public abstract void logout();

    /** verify user is currently logged in.
     * 
     * @return true if the user is logged in
     */
    public abstract boolean isLoggedIn();

    /** display the login dialogue to prompt the user for input, and then log in */
    public abstract void guiLogin();
    
    /** register a listener, that will be notified when the user logs in or out 
     * @xmlrpc method unavailable - callbacks not technically possible*/
    public void addUserLoginListener(UserLoginListener l) ;
    
    /** remove a previously registered listener
     * @xmlrpc method unavailable - callbacks not technically possible */
    public void removeUserLoginListener(UserLoginListener l);


}

/* 
 $Log: Community.java,v $
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