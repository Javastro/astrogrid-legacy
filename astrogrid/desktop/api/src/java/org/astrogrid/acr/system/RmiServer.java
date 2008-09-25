/*$Id: RmiServer.java,v 1.5 2008/09/25 16:02:03 nw Exp $
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

/**AR System Service: Information about the RMI interface to the AstroRuntime.
 * 
 * No management functions - just query methods.
 * @service system.rmi
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
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
Revision 1.5  2008/09/25 16:02:03  nw
documentation overhaul

Revision 1.4  2007/01/24 14:04:44  nw
updated my email address

Revision 1.3  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/