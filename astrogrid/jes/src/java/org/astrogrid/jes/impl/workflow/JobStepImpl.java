/*$Id: JobStepImpl.java,v 1.3 2004/03/03 01:13:41 nw Exp $
 * Created on 11-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.job.Tool;
import org.astrogrid.workflow.beans.v1.Step;

/** Implementation of JobStep as a thin wrapper around a {@link org.astrogrid.workflow.beans.v1.Step}
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 *
 */
public class JobStepImpl implements JobStep {
    /** Construct a new JobStepImpl
     * @param impl
     * @param choice
     */
    public JobStepImpl(JobImpl impl, Step step) {
        this.parent = impl;
        this.step = step;
    }
    protected JobImpl parent;
    protected Step step;
    public Step getStep() {
        return this.step;
    }
    /**
     * @see org.astrogrid.jes.job.JobStep#getParent()
     */
    public Job getParent() {
        return parent;
    }
    /**
     * @see org.astrogrid.jes.job.JobStep#getStepNumber()
     */
    public int getStepNumber() {
        return step.getStepNumber(); 
    }
    /**
     * @see org.astrogrid.jes.job.JobStep#setStatus(java.lang.String)
     * @todo check we have 0 origin
     */
    public void setStatus(ExecutionPhase status) {
        int last = this.step.getStepExecutionRecordCount() - 1;
        this.step.getStepExecutionRecord(last).setStatus(status);
    }
    /**
     * @see org.astrogrid.jes.job.JobStep#getStatus()
     */
    public ExecutionPhase getStatus() {
        int last = this.step.getStepExecutionRecordCount() - 1;
        return this.step.getStepExecutionRecord(last).getStatus();
    }
    /**
     * @see org.astrogrid.jes.job.JobStep#setComment(java.lang.String)
     * @todo implement
     */
    public void setComment(String comment) {
        int last = this.step.getStepExecutionRecordCount() - 1;
        //step.getStepExecutionRecord(last).addM
    }
    /**
     * @see org.astrogrid.jes.job.JobStep#getSequenceNumber()
     */
    public int getSequenceNumber() {        
        return step.getSequenceNumber(); 
    }
    /**
     * @see org.astrogrid.jes.job.JobStep#getJoinCondition()
     * @todo move this constant out into code.
     */
    public String getJoinCondition() {
        return step.getJoinCondition().toString();
    }
    /**
     * @see org.astrogrid.jes.job.JobStep#getTool()
     */
    public Tool getTool() {
        return new ToolImpl(step.getTool());
    }
    /**
     * @see org.astrogrid.jes.job.JobStep#getComment()
     * @todo implement
     */
    public String getComment() {
       // return step.getComment();
       return null;
    }
 
}


/* 
$Log: JobStepImpl.java,v $
Revision 1.3  2004/03/03 01:13:41  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.4  2004/02/19 13:40:09  nw
updated to fit new interfaces

Revision 1.1.2.3  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.2  2004/02/12 12:54:47  nw
worked in inversion of control pattern - basically means that
components have to be assembled, rather than self-configuring
from properties in config files. so easier to test each component in isolation

Revision 1.1.2.1  2004/02/12 01:14:01  nw
castor implementation of jes object model
 
*/