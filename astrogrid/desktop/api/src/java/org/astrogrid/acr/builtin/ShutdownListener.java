/*$Id: ShutdownListener.java,v 1.3 2007/01/24 14:04:46 nw Exp $
 * Created on 17-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.builtin;

import java.util.EventListener;

/** Listener interface for things that want to be notified of proposed or real shutdowns of the acr
 * @see org.astrogrid.acr.builtin.Shutdown
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Aug-2005
 *
 */
public interface ShutdownListener extends EventListener {
    /** called on listeners when acr is shutting down - too late to do anything about it */
    void halting();
    /** called on listeners when acr intends to shut down - gives listeners chance to object to this 
     * 
     * @return a string message why the acr should keep running. <tt>null</tt> means that this client hasn't got a problem with the acr shutting down.
     */
    String lastChance();
}


/* 
$Log: ShutdownListener.java,v $
Revision 1.3  2007/01/24 14:04:46  nw
updated my email address

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.40.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.1  2005/08/25 16:59:44  nw
1.1-beta-3
 
*/