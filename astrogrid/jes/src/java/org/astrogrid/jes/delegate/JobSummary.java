/*$Id: JobSummary.java,v 1.2 2004/03/15 01:30:30 nw Exp $
 * Created on 08-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate;

import org.astrogrid.jes.types.v1.WorkflowSummary;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** Data class representing results of querying jes for user's jobs.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 *
 */
public class JobSummary {
    public JobSummary(WorkflowSummary item) {
        name = item.getWorkflowName();
        jobID = new JobURN();
        jobID.setContent(item.getJobUrn().toString());
    }
    public JobSummary(String name,JobURN jobID) {
        this.name = name;
        this.jobID = jobID;
    }
    
    protected final String name;
    protected final JobURN jobID;
    /** human-readable name of the job */
    public String getName() {
        return name;
    }
    /** unique identifier assigned to this job */
    public JobURN getJobURN() {
        return jobID;
    }
}


/* 
$Log: JobSummary.java,v $
Revision 1.2  2004/03/15 01:30:30  nw
jazzed up javadoc

Revision 1.1  2004/03/09 15:04:42  nw
renamed JobInfo to JobSummary

Revision 1.1  2004/03/09 14:23:12  nw
integrated new JobController wsdl interface
 
*/