/*$Id: JobSummary.java,v 1.3 2004/12/03 14:47:41 jdt Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.workflow.beans.v1.execution.Extension;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;

import java.util.Date;
import java.util.Enumeration;

/** Data class representing results of querying jes for user's jobs.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 *@deprecated. just kept for compatability - clients should move to using {@link org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType} instead
 */
public class JobSummary {
    public JobSummary(WorkflowSummaryType item) {
        this.item = item;
        /*
        name = item.getWorkflowName();
        jobID = new JobURN();
        jobID.setContent(item.getJobId().toString());
        */
    }
    
    protected final WorkflowSummaryType item;
    /** can't really support this one anymore 
    public JobSummary(String name,JobURN jobID) {
        this.name = name;
        this.jobID = jobID;
    }*/
    /* don't need these anymore
    protected final String name;
    protected final JobURN jobID;
    */

    public JobURN getJobURN() {
        return this.getJobId();
    }
    
    public String getName() {
        return this.getWorkflowName();
    }
        
  
    public Enumeration enumerateExtension() {
        return this.item.enumerateExtension();
    }
    public Enumeration enumerateMessage() {
        return this.item.enumerateMessage();
    }

    public String getDescription() {
        return this.item.getDescription();
    }
    public Extension[] getExtension() {
        return this.item.getExtension();
    }
    public Extension getExtension(int arg0) throws IndexOutOfBoundsException {
        return this.item.getExtension(arg0);
    }
    public int getExtensionCount() {
        return this.item.getExtensionCount();
    }
    public Date getFinishTime() {
        return this.item.getFinishTime();
    }
    public JobURN getJobId() {
        return this.item.getJobId();
    }
    public MessageType[] getMessage() {
        return this.item.getMessage();
    }
    public MessageType getMessage(int arg0) throws IndexOutOfBoundsException {
        return this.item.getMessage(arg0);
    }
    public int getMessageCount() {
        return this.item.getMessageCount();
    }
    public Date getStartTime() {
        return this.item.getStartTime();
    }
    public ExecutionPhase getStatus() {
        return this.item.getStatus();
    }
    public String getWorkflowName() {
        return this.item.getWorkflowName();
    }
  
}


/* 
$Log: JobSummary.java,v $
Revision 1.3  2004/12/03 14:47:41  jdt
Merges from workflow-nww-776

Revision 1.2.128.1  2004/12/01 21:48:20  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.2  2004/03/15 01:30:30  nw
jazzed up javadoc

Revision 1.1  2004/03/09 15:04:42  nw
renamed JobInfo to JobSummary

Revision 1.1  2004/03/09 14:23:12  nw
integrated new JobController wsdl interface
 
*/