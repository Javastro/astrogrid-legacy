/*$Id: Status.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 26-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;


import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;

import java.util.ArrayList;
import java.util.List;

/** Enumeration type.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
 *
 */
public class Status {

    public Status() {
    };
    
    /** Construct a new Status
     * 
     */
    private Status(String status) {
        super();
        this.status = status;
    }
    
    
    public static  final Status UNSTARTED = new Status("UNSTARTED");

public static final Status START = new Status("START");

public static final Status STARTED = new Status("STARTED");

public static final Status FINISH = new Status("FINISH");

public static  final Status FINISHED = new Status("FINISHED");

public static final Status ERROR = new Status("ERROR");

public static final Status ERRED = new Status("ERRED");

public static  ExecutionPhase toPhase(Status status) {
    if (status.equals(Status.UNSTARTED)) {
        return ExecutionPhase.PENDING;
    }
    if (status.equals(Status.START)) {
        return ExecutionPhase.INITIALIZING;
    }
    if (status.equals(Status.STARTED)) {
        return ExecutionPhase.RUNNING;
    }
    if (status.equals(Status.FINISH)) {
        return ExecutionPhase.RUNNING;
    }
    if (status.equals(Status.FINISHED)) {
        return ExecutionPhase.COMPLETED;
    }
    if (status.equals(Status.ERROR)) {
        return ExecutionPhase.ERROR;
    }
    if (status.equals(Status.ERRED)) {
        return ExecutionPhase.ERROR;
    }
    return ExecutionPhase.UNKNOWN;
}

static  final List allStatus;
static {
    allStatus = new ArrayList();
    allStatus.add(UNSTARTED);
    allStatus.add(START);
    allStatus.add(STARTED);
    allStatus.add(FINISH);
    allStatus.add(FINISHED);
    allStatus.add(ERROR);
    allStatus.add(ERRED);
}

    private String status;
    
    public boolean equals(Object o) {
        Status other = (Status)o;
        return this.status.equals(other.status);
    }

    public String getName() {
        return status;
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Status:");
        buffer.append(status);
        buffer.append("]");
        return buffer.toString();
    }
}

/* 
$Log: Status.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.5  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.4  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.3  2004/07/27 23:50:09  nw
removed betwixt (duff). replaces with xstream.

Revision 1.1.2.2  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.

Revision 1.1.2.1  2004/07/26 15:51:19  nw
first stab at a groovy scheduler.
transcribed all the classes in the python prototype, and took copies of the
classes in the 'scripting' package.
 
*/