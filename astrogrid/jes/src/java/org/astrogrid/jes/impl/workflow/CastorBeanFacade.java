/*$Id: CastorBeanFacade.java,v 1.3 2004/03/03 01:13:41 nw Exp $
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

import org.astrogrid.jes.JesException;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.SubmissionResponse;
import org.astrogrid.jes.types.v1.WorkflowList;

import org.exolab.castor.xml.CastorException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Bean facade that hides implementation of object model (castor in this case) from the service code.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 *
 */
public class CastorBeanFacade implements BeanFacade {
    public CastorBeanFacade(AbstractJobFactoryImpl jobFactory) {
        this.jobFactory = jobFactory;
    }
    protected final AbstractJobFactoryImpl jobFactory;
    
    /** default constructor - provides a InMemoryJobFactory */
    public CastorBeanFacade() {
        this(new InMemoryJobFactoryImpl());
    }
    
    public JobFactory getJobFactory() {
        return jobFactory;
    }

    /**
     * @see org.astrogrid.jes.job.BeanFacade#createSubmitJobRequest(java.lang.String)     
     */
    public SubmitJobRequest createSubmitJobRequest(String xmlDoc) throws JesException {
        try {
            return new SubmitJobRequestImpl(xmlDoc);
        } catch (CastorException e) {
            throw new JesException("Could not deserialize xml",e);
        }
    }

    /**
     * @see org.astrogrid.jes.job.BeanFacade#createListJobsErrorResponse(java.lang.String, java.lang.String, org.astrogrid.i18n.AstroGridMessage)
     */
    public WorkflowList createListJobsErrorResponse(String userid, String community, String message) {
        WorkflowList l = new WorkflowList();
        l.setCommunity(community);
        l.setUserId(userid);
        l.setMessage(message);
        l.setWorkflow(new String[0]);
        return l;
    }
    /**
     * @see org.astrogrid.jes.job.BeanFacade#createListJobsSuccessResponse(java.lang.String, java.lang.String, java.util.Iterator)
     */
    public WorkflowList createListJobsSuccessResponse(String userid, String community, Iterator iterator) {
        WorkflowList l = new WorkflowList();
        List wfList = new ArrayList();
        while (iterator.hasNext()) {
            Job j = (Job)iterator.next();            
            wfList.add(j.getDocumentXML());
        }
        String[] example = new String[wfList.size()];
        l.setWorkflow((String[] )wfList.toArray(example));
        l.setCommunity(community);
        l.setUserId(userid);        
        return l;
    }
    

    /**
     * @see org.astrogrid.jes.job.BeanFacade#createSubmitJobSuccessResponse(org.astrogrid.jes.job.Job)
     */
    public SubmissionResponse createSubmitJobSuccessResponse(Job j) {
        SubmissionResponse sr= new SubmissionResponse();
        sr.setJobURN(new JobURN(j.getId().getContent()));
        sr.setSubmissionSuccessful(true);
        return sr;
        
    }
    /**
     * @see org.astrogrid.jes.job.BeanFacade#createSubmitJobErrorResponse(org.astrogrid.jes.job.Job, org.astrogrid.i18n.AstroGridMessage)
     */
    public SubmissionResponse createSubmitJobErrorResponse(Job j, String msg) {
        SubmissionResponse sr = new SubmissionResponse();
        /* shouldn't return job back, as its all duff..
        if (j.getId() != null) {
            sr.setJobURN(new JobURN(j.getId()));
        }
        */
        sr.setJobURN(null);
        sr.setSubmissionSuccessful(false);
        sr.setMessage(msg);
        return sr;
    }

    /**
     * @see org.astrogrid.jes.job.BeanFacade#axis2castor(org.astrogrid.jes.types.v1.JobURN)
     */
    public org.astrogrid.workflow.beans.v1.execution.JobURN axis2castor(JobURN jobURN) {
        org.astrogrid.workflow.beans.v1.execution.JobURN result = new org.astrogrid.workflow.beans.v1.execution.JobURN();
        result.setContent(jobURN.toString());
        return result;
    }

    /**
     * @see org.astrogrid.jes.job.BeanFacade#castor2axis(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public JobURN castor2axis(org.astrogrid.workflow.beans.v1.execution.JobURN jobURN) {
        return new JobURN(jobURN.getContent());
    }
    
    
}


/* 
$Log: CastorBeanFacade.java,v $
Revision 1.3  2004/03/03 01:13:41  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.6  2004/02/19 13:40:09  nw
updated to fit new interfaces

Revision 1.1.2.5  2004/02/17 15:56:17  nw
updated to fill in correct parts of return object on error

Revision 1.1.2.4  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.3  2004/02/17 10:58:38  nw
altered to implement cut down facade interface, matched with types
generated by wsdl2java

Revision 1.1.2.2  2004/02/12 12:54:47  nw
worked in inversion of control pattern - basically means that
components have to be assembled, rather than self-configuring
from properties in config files. so easier to test each component in isolation

Revision 1.1.2.1  2004/02/12 01:14:01  nw
castor implementation of jes object model
 
*/