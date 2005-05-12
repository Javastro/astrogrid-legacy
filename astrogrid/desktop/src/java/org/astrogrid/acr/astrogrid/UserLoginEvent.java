/*$Id: UserLoginEvent.java,v 1.4 2005/05/12 15:59:09 clq2 Exp $
 * Created on 21-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.util.EventObject;

/** event representing a user logging in or out.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2005
 *
 */
public class UserLoginEvent extends EventObject {

    /** Construct a new UserLoginEvent
     * @param source
     */
    public UserLoginEvent(boolean loggedIn,Object source) {
        super(source);
        this.loggedIn = loggedIn;
    }
    
    private final boolean loggedIn;
    
    /** reutrns true if this is a login event */
    public boolean isLoggedIn() {
        return loggedIn;
    }

}


/* 
$Log: UserLoginEvent.java,v $
Revision 1.4  2005/05/12 15:59:09  clq2
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

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.
 
*/