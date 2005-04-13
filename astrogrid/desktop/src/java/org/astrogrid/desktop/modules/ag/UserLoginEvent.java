/*$Id: UserLoginEvent.java,v 1.2 2005/04/13 12:59:11 nw Exp $
 * Created on 21-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

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
    
    public boolean isLoggedIn() {
        return loggedIn;
    }

}


/* 
$Log: UserLoginEvent.java,v $
Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.
 
*/