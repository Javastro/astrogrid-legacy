/*$Id: JobImpl.java,v 1.3 2004/03/03 01:13:41 nw Exp $
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
import org.astrogrid.workflow.beans.v1.ActivityChoice;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/** Implementation of Job, as a wrapper around a {@link org.astrogrid.workflow.beans.v1.Workflow}
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 *
 */
public class JobImpl implements Job {
    /** Construct a new JobImpl
     * 
     */
    public JobImpl(SubmitJobRequestImpl impl) {
        super();
        this.workflow = impl.getWorkflow();
    }
    
    public JobImpl(Workflow w) {
        super();
        this.workflow = w;
    }
    
    protected final Workflow workflow;
    public Workflow getWorkflow() {
        return workflow;
    }
    /**
     * @see org.astrogrid.jes.job.Job#getId()
     */
    public JobURN getId() {
        return workflow.getJobExecutionRecord().getJobId();
    }
    /**
     * @see org.astrogrid.jes.job.Job#getName()
     */
    public String getName() {
        return workflow.getName();
    }

    /**
     * @see org.astrogrid.jes.job.Job#getDescription()
     */
    public String getDescription() {
        return workflow.getDescription();
    }

    /**
     * @see org.astrogrid.jes.job.Job#getDate()
     */
    public Date getDate() {
        return workflow.getJobExecutionRecord().getStartTime();
    }
    /**
     * @see org.astrogrid.jes.job.Job#setDate(java.util.Date)
     */
    public void setDate(Date date) {
        workflow.getJobExecutionRecord().setStartTime(date);
    }
    /**
     * @see org.astrogrid.jes.job.Job#getUserId()
     */
    public String getUserId() {
        return (String)workflow.findXPathValue("community/credentials/account");
    }

    /**
     * @see org.astrogrid.jes.job.Job#getCommunity()
     * @todo check what this signifies.
     */
    public String getCommunity() {
        return (String)workflow.findXPathValue("community/credentials/group");
    }

    /**
     * @see org.astrogrid.jes.job.Job#getGroup()
     */
    public String getGroup() {
        return (String)workflow.findXPathValue("community/credentials/group");
    }
    /**
     * @see org.astrogrid.jes.job.Job#setGroup(java.lang.String)
     * @todo implement
     */
    public void setGroup(String group) {
        //workflow.getCredentials().setGroup(group);
    }
    /**
     * @see org.astrogrid.jes.job.Job#getToken()
     */
    public String getToken() {
        return workflow.getCredentials().getSecurityToken();
    }

    /**
     * @see org.astrogrid.jes.job.Job#getJobSteps()
     */
    public Iterator getJobSteps() {
        final Iterator i = listAllJobSteps(workflow.getSequence().getActivitySequence().getActivityChoice()).iterator();
        return new Iterator() {

            public void remove() {
                throw new UnsupportedOperationException("don't support remove");
            }

            public boolean hasNext() {
                return i.hasNext();
            }

            public Object next() {
                return new JobStepImpl(JobImpl.this,(Step)i.next());
            }
        };
        
    }
    
    /** at moment this is a replication of the current duff jes behaviour - all job steps are stripped out, no matter the inner structure of the document
     *recursion can be quite inefficient, but don't care as this is only temporary behaviour. 
     *@todo update this so that workflow has meaning.
     *@return a list of Step objects
     */
    protected List listAllJobSteps(ActivityChoice[] seq) {
        List result = new ArrayList();
        for (int i = 0; i < seq.length; i++) {
            ActivityChoice ac = seq[i];
            if (ac.getStep() != null) {
                result.add(ac.getStep());
            } else if (ac.getFlow() != null) {
                List sublist = listAllJobSteps(ac.getFlow().getActivitySequence().getActivityChoice());
                result.addAll(sublist);
            } else if (ac.getSequence() != null) {
                List sublist = listAllJobSteps(ac.getSequence().getActivitySequence().getActivityChoice());
                result.addAll(sublist);
            }
        }
        return result;
    }
 
    /**
     * @see org.astrogrid.jes.job.Job#getDocumentXML()
     */
    public String getDocumentXML() {
        StringWriter sw = new StringWriter();
        try {
            workflow.marshal(sw);
            sw.close();
        } catch (Exception e) {
            throw new RuntimeException("Could Not marshall to xml, even though its come from there",e);
        }
        return sw.toString();
    }
    /**
     * @see org.astrogrid.jes.job.Job#setStatus(java.lang.String)
     */
    public void setStatus(ExecutionPhase status) {
        workflow.getJobExecutionRecord().setStatus(status);
    }
    /**
     * @see org.astrogrid.jes.job.Job#getStatus()
     */
    public ExecutionPhase getStatus() {
        return workflow.getJobExecutionRecord().getStatus();
        
    }
    /** equlity based only on getId() - the  jobURN*/
    public boolean equals(Object o) {
        Job other = (Job)o;
        return this.getId().equals(other.getId());
    }
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return this.getId().hashCode();
    }

}


/* 
$Log: JobImpl.java,v $
Revision 1.3  2004/03/03 01:13:41  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.7  2004/02/27 00:24:10  nw
added hashCode()

Revision 1.1.2.6  2004/02/19 13:40:09  nw
updated to fit new interfaces

Revision 1.1.2.5  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.4  2004/02/17 10:58:38  nw
altered to implement cut down facade interface, matched with types
generated by wsdl2java

Revision 1.1.2.3  2004/02/12 15:41:11  nw
improved wrapping of workflow - handles nulls gracefully

Revision 1.1.2.2  2004/02/12 12:54:47  nw
worked in inversion of control pattern - basically means that
components have to be assembled, rather than self-configuring
from properties in config files. so easier to test each component in isolation

Revision 1.1.2.1  2004/02/12 01:14:01  nw
castor implementation of jes object model
 
*/