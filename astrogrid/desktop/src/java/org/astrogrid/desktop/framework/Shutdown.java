/*$Id: Shutdown.java,v 1.2 2005/04/13 12:59:12 nw Exp $
 * Created on 17-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

/** Interface to a component that will shut down the system
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Mar-2005
 *
 */
public interface Shutdown {
    
    public void halt();

}


/* 
$Log: Shutdown.java,v $
Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/