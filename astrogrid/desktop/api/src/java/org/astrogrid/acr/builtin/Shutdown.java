/*$Id: Shutdown.java,v 1.7 2008/09/25 16:02:04 nw Exp $
 * Created on 17-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.builtin;

/** AR Service: Halts the Astro Runtime
 * @service builtin.shutdown
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Mar-2005
 
 */
public interface Shutdown {
    
    /** Bring the AstroRuntime to a graceful halt. 
     * 
     * @note notifies all listeners, and if there's any objections displays a prompt for the user to acknowledge
     * then calls {@link #reallyHalt}
     * @see ShutdownListener#lastChance()
     * */
    public void halt();
    
    
    /** Bring the AstroRuntime to a graceful halt, ignoring any objections from clients.
     * still notifies all listeners that the system is shutting down
     * @warning Use this method with care - prefer {@link #halt} when there's a chance that the ACR has other clients
     *
     * @see ShutdownListener#halting()
     */
    public void reallyHalt();
    
    /** register a listener for shutdown events
     * @param l
     */
    public void addShutdownListener(ShutdownListener l) ;
    
    /** remove a previously registered listener.
     * @param l
     */
    public void removeShutdownListener(ShutdownListener l);

}


/* 
$Log: Shutdown.java,v $
Revision 1.7  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.6  2007/01/24 14:04:45  nw
updated my email address

Revision 1.5  2006/10/30 12:12:36  nw
documentation improvements.

Revision 1.4  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.3  2005/08/25 16:59:44  nw
1.1-beta-3

Revision 1.2  2005/08/12 08:45:16  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/