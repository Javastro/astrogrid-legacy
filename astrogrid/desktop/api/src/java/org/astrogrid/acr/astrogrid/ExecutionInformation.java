/*$Id: ExecutionInformation.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 04-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;

/**  Bean that summarizes the properties of a job
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Aug-2005
 *
 */
public class ExecutionInformation extends AbstractInformation {

    /** Construct a new JobInformation
     * 
     */
    public ExecutionInformation(URI id, String name, String description, String status, Date startTime,Date finishTime) {
        super(id);
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
    
    public static final String PENDING = "PENDING";
    public static final String INITIALIZING = "INITIALIZING";
    public static final String RUNNING = "RUNNING";
    public static final String COMPLETED = "COMPLETED";
    public static final String ERROR = "ERROR";
    public static final String UNKNOWN = "UNKNOWN";

    protected final String name;
    protected final String description;
    protected final String status;
    protected final Date startTime;
    protected final Date finishTime;
    //@todo later add messages.

    /** description of the workflow */
    public String getDescription() {
        return this.description;
    }
    /** time the workflow finished - may be null if still running */
    public Date getFinishTime() {
        return this.finishTime;
    }
    /** name of the workflow */
    public String getName() {
        return this.name;
    }
    /** time the workflow started - may be null if still pending */
    public Date getStartTime() {
        return this.startTime;
    }
    /** current status of this job - one of the constants in this class */
    public String getStatus() {
        return this.status;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ExecutionInformation:");
        buffer.append(" id: ");
        buffer.append(id);
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append(" description: ");
        buffer.append(description);
        buffer.append(" status: ");
        buffer.append(status);
        buffer.append(" startTime: ");
        buffer.append(startTime);
        buffer.append(" finishTime: ");
        buffer.append(finishTime);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: ExecutionInformation.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/