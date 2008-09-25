/*$Id: UserInformation.java,v 1.6 2008/09/25 16:02:04 nw Exp $
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

/** Metadata about the currently logged in user.
 * <p/>
 * Behaviour of superclass methods:
 * <ul>
 * <li>{@link #getId()} will return the user's ivorn</li>
 * <li>{@link #getName()} will return be the user's name.</li>
 * </ul>
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Aug-2005
 *@see Community
 */
public class UserInformation extends AbstractInformation {

    /** Construct a new UserInformation
    @exclude
     */
    public UserInformation(final URI id, final String username, final String password, final String community) {
        super(username, id);
        this.password = password;
        this.community = community;
    }
    
    static final long serialVersionUID = -6197661977180232772L;
    protected transient final String password;
    protected final String community;
    
    
    /** the community the logged in user belongs to */
    public String getCommunity() {
        return this.community;
    }
    
    /** access the password for the current user
     */
    public String getPassword() {
        return this.password;
    }
    


}


/* 
$Log: UserInformation.java,v $
Revision 1.6  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.5  2007/10/23 07:48:57  nw
doc fix.

Revision 1.4  2007/01/24 14:04:44  nw
updated my email address

Revision 1.3  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.2.6.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.2.6.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.2  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.1  2005/08/25 16:59:44  nw
1.1-beta-3
 
*/