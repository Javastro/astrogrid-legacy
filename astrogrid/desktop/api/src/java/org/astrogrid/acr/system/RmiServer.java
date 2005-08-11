/*$Id: RmiServer.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.system;

/** Component that creates a java rmi service that exposes the modules in the acr 
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public interface RmiServer {
    /** information method - describes the port the rmi service is running on */
    int getPort();
}


/* 
$Log: RmiServer.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/