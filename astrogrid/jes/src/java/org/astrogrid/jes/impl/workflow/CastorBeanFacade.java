/*$Id: CastorBeanFacade.java,v 1.5 2004/03/07 21:04:38 nw Exp $
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

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.component.ComponentDescriptor;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.SubmissionResponse;
import org.astrogrid.jes.types.v1.WorkflowList;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.exolab.castor.xml.CastorException;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

/** Bean facade that hides implementation of object model (castor in this case) from the service code.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 *
 */
public class CastorBeanFacade implements BeanFacade, ComponentDescriptor {
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
    public WorkflowList createListJobsErrorResponse(Account acc, String message) {
        WorkflowList l = new WorkflowList();
        l.setCommunity(acc.getCommunity());
        l.setUserId(acc.getName());
        l.setMessage(message);
        l.setWorkflow(new String[0]);
        return l;
    }
    /**
     * @see org.astrogrid.jes.job.BeanFacade#createListJobsSuccessResponse(java.lang.String, java.lang.String, java.util.Iterator)
     * @todo tidy up exception handling.
     */
    public WorkflowList createListJobsSuccessResponse(Account acc, Iterator iterator) {
        WorkflowList l = new WorkflowList();
        List wfList = new ArrayList();
        while (iterator.hasNext()) {
            Workflow j = (Workflow)iterator.next();
            StringWriter sw = new StringWriter();
            try {
            j.marshal(sw);
            sw.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }            
            wfList.add(sw.toString());
        }
        String[] example = new String[wfList.size()];
        l.setWorkflow((String[] )wfList.toArray(example));
        l.setCommunity(acc.getCommunity());
        l.setUserId(acc.getName());        
        return l;
    }
    

    /**
     * @see org.astrogrid.jes.job.BeanFacade#createSubmitJobSuccessResponse(org.astrogrid.jes.job.Job)
     */
    public SubmissionResponse createSubmitJobSuccessResponse(Workflow j) {
        SubmissionResponse sr= new SubmissionResponse();
        sr.setJobURN(new JobURN(j.getJobExecutionRecord().getJobId().getContent()));
        sr.setSubmissionSuccessful(true);
        return sr;
        
    }
    /**
     * @see org.astrogrid.jes.job.BeanFacade#createSubmitJobErrorResponse(org.astrogrid.jes.job.Job, org.astrogrid.i18n.AstroGridMessage)
     */
    public SubmissionResponse createSubmitJobErrorResponse(Workflow j, String msg) {
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
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "CastorBeanFacade";
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Standard Bean Facade";
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }

    
    
}


/* 
$Log: CastorBeanFacade.java,v $
Revision 1.5  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.4.4.1  2004/03/07 20:41:59  nw
altered to look in component manager factory for implementations

Revision 1.4  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

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