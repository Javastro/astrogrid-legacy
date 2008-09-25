/*$Id: UserLoginListener.java,v 1.5 2008/09/25 16:02:04 nw Exp $
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

import java.util.EventListener;

/** Listener interface for classes that want to be notified of login events
 * @see Community#addUserLoginListener(UserLoginListener)
 * @see Community#removeUserLoginListener(UserLoginListener)
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Mar-2005
 *
 */
public interface UserLoginListener  extends EventListener{
    
    /** notification that the user has logged into the AR */
    public void userLogin(UserLoginEvent e);
    
    /** notification that the user has logged out of the AR*/
    public void userLogout(UserLoginEvent e);

}


/* 
$Log: UserLoginListener.java,v $
Revision 1.5  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.4  2007/01/24 14:04:44  nw
updated my email address

Revision 1.3  2005/08/25 16:59:44  nw
1.1-beta-3

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