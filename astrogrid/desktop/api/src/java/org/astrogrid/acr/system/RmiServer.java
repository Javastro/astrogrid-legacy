/*$Id: RmiServer.java,v 1.2 2005/08/12 08:45:15 nw Exp $
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

/** Service Interface to the  JavaRMI interface to the ACR
 * <p>
 * No management functions - just query methods.
 * @service system.rmi
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public interface RmiServer {
    
    /** Query which port the RMI server is running on
     * @return the number of the network port
     */
    int getPort();
}


/* 
$Log: RmiServer.java,v $
Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/