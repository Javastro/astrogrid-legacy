/*$Id: ServiceListener.java,v 1.3 2003/09/05 13:21:48 nw Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.service;

/** interface for something that want to listen to the querying process
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Sep-2003
 *@see ProxyServiceListener
 */
public interface ServiceListener {
    /** Called by the service when it has a
    * status change
    */
    public abstract void serviceStatusChanged(String newStatus);
}

/* 
$Log: ServiceListener.java,v $
Revision 1.3  2003/09/05 13:21:48  nw
changed from an implementation to an interface.
previous implementation is in ProxyServiceListener.
 
*/