/*$Id: MockMonitor.java,v 1.2 2008/09/03 14:19:08 pah Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.test;

import java.util.Observable;
import java.util.Observer;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.description.execution.LogLevel;
import org.astrogrid.applications.description.execution.MessageType;

/** Mock impl of a monitor.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 * @author Paul Harrison (pah@jb.man.ac.uk)
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
    public int nwarn = 0;
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
        if(arg instanceof MessageType )
        {
            notifyStateChange(app,(MessageType)arg);
        }
    }

    /**
     * @param app
     * @param type
     */
    private void notifyStateChange(Application app, MessageType type) {
        if (type.getLevel() == LogLevel.ERROR || type.getLevel() == LogLevel.WARN) {
            System.err.println("saw  "+type.getLevel().toString()+"\n" + type.getContent());
        }
        if(type.getLevel() == LogLevel.WARN)
        {
           System.err.println("saw  "+type.getLevel().toString()+"\n" + type.getContent());
           nwarn++;
        }
        
    }

    public void waitFor(int seconds) throws InterruptedException {
           long startTime = System.currentTimeMillis();
            while (!sawExit && !sawError && System.currentTimeMillis() < startTime + (seconds * 1000)) {
                Thread.sleep(1000);
            }
    }
}


/* 
$Log: MockMonitor.java,v $
Revision 1.2  2008/09/03 14:19:08  pah
result of merge of pah_cea_1611 branch

Revision 1.1.2.1  2008/05/01 15:22:47  pah
updates to tool

Revision 1.1.2.1  2008/04/17 16:08:33  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.1  2008/02/12 12:10:55  pah
build with 1.0 registry and filemanager clients

Revision 1.8  2005/07/05 08:27:01  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.7.68.1  2005/06/09 22:17:58  pah
tweaking the log getter

Revision 1.7  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.6.6.1  2004/10/27 16:02:16  pah
counts warnings

Revision 1.6  2004/09/30 15:12:48  pah
try to test for failure a bit better

Revision 1.5  2004/09/15 11:37:43  pah
enhanced monitoring of error conditions

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