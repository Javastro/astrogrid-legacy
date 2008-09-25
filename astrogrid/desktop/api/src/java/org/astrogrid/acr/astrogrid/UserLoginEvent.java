/*$Id: UserLoginEvent.java,v 1.5 2008/09/25 16:02:04 nw Exp $
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

/** Event Object passed when the user logs in or out.
 * @see UserLoginListener
 * @see org.astrogrid.acr.astrogrid.Community#addUserLoginListener(UserLoginListener)
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Mar-2005
 *
 */
public class UserLoginEvent extends EventObject {

    /** Construct a new UserLoginEvent
     * @exclude
     * @param source
     */
    public UserLoginEvent(final boolean loggedIn,final Object source) {
        super(source);
        this.loggedIn = loggedIn;
    }
    
    static final long serialVersionUID = 1045060924801372793L;
    private final boolean loggedIn;
    
    /** reutrns true if this is a login event */
    public boolean isLoggedIn() {
        return loggedIn;
    }

}


/* 
$Log: UserLoginEvent.java,v $
Revision 1.5  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.4  2007/01/24 14:04:44  nw
updated my email address

Revision 1.3  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.2.40.1  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.2  2005/08/12 08:45:16  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

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