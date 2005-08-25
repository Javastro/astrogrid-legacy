/*$Id: Shutdown.java,v 1.3 2005/08/25 16:59:44 nw Exp $
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

/** Interface to a service that will shut down the ACR server
 * @service builtin.shutdown
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Mar-2005
 
 */
public interface Shutdown {
    
    /** bring the whole ACR system to a graceful halt 
     * <p>
     * notifies all listeners, and if there's any objections displays a prompt for the user to acknowledge
     * then calls {@link #reallyHalt}
     * @see ShutdownListener#lastChance()
     * */
    public void halt();
    
    
    /** bring the ACR to a graceful halt, not giving clients chance to object.
     * Use this method with care - prefer {@link #halt} when theres a chance that the ACR has other clients (i.e. always).
     * <p>
     * still notifies all listeners that the system is shutting down
     * @see ShutdownListener#halting()
     */
    public void reallyHalt();
    
    /** register a listerner for shutdown events
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