/*$Id: UserInformation.java,v 1.2 2006/02/02 14:19:48 nw Exp $
 * Created on 17-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.net.URI;

/** Information about the currently logged in user.
 * 
 * <ul>
 * <li><tt>id</tt></li> - will be the user ivorn
 * <li><tt>name</tt></li> - will be the user's name.
 * </ul>
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Aug-2005
 *@see org.astrogrid.acr.astrogrid.Community
 */
public class UserInformation extends AbstractInformation {

    /** Construct a new UserInformation
     * @param name
     * @param id
     */
    public UserInformation(URI id, String username, String password, String community) {
        super(username, id);
        this.password = password;
        this.community = community;
    }
    
    protected transient final String password;
    protected final String community;
    
    
    /** access the name of the community the logged in user belongs to */
    public String getCommunity() {
        return this.community;
    }
    
    /** access the password for the current user
     *  - just here temporarily
     * @return
     */
    public String getPassword() {
        return this.password;
    }

}


/* 
$Log: UserInformation.java,v $
Revision 1.2  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.1  2005/08/25 16:59:44  nw
1.1-beta-3
 
*/