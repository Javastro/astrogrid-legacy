/*$Id: NewModuleListener.java,v 1.4 2005/08/05 11:46:55 nw Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import java.util.EventListener;

/** A listener interface - listeners are notified whenever a new module is added to the registry.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public interface NewModuleListener extends EventListener{
    
    public void newModuleRegistered(NewModuleEvent e);

}


/* 
$Log: NewModuleListener.java,v $
Revision 1.4  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/