/*$Id: MockMonitor.java,v 1.4 2004/09/01 15:42:26 jdt Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications;

import java.util.Observable;
import java.util.Observer;

/** Mock impl of a monitor.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class MockMonitor implements Observer{
    /** Construct a new MockMonitor
     * 
     */
    public MockMonitor() {
        super();
    }
 
    
    public boolean sawExit = false;
    public boolean sawError = false;
    public Application sawApp = null;
    /**
     * @see org.astrogrid.applications.manager.observer.ApplicationProgressObserver#notifyStateChange(org.astrogrid.applications.Application, org.astrogrid.applications.Status)
     */
    public void notifyStateChange(Application app, Status status) {
        if (status.equals(Status.COMPLETED)) {
            sawApp = app;
            sawExit = true;
        } else if (status.equals(Status.ERROR)) {
            sawApp = app;
            sawError = true;
        }
    }

    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        Application app = (Application)o;
        if (arg instanceof Status) {
            notifyStateChange(app,(Status)arg);
        }
    }

    public void waitFor(int seconds) throws InterruptedException {
           long startTime = System.currentTimeMillis();
            while (!sawExit && System.currentTimeMillis() < startTime + (seconds * 1000)) {
                Thread.sleep(1000);
            }
    }
}


/* 
$Log: MockMonitor.java,v $
Revision 1.4  2004/09/01 15:42:26  jdt
Merged in Case 3

Revision 1.3.2.1  2004/08/11 22:54:42  jdt
Monitor now monitors errors too.

Revision 1.3  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/