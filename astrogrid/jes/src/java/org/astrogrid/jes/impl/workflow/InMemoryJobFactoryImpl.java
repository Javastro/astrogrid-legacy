/*$Id: InMemoryJobFactoryImpl.java,v 1.9 2004/07/30 15:42:34 nw Exp $
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
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;

/** Noddy implementation of job factory that keeps all job records in memory only.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 *
 */
public class InMemoryJobFactoryImpl extends AbstractJobFactoryImpl implements ComponentDescriptor {
    /** Construct a new InMemoryJobFactoryImpl
     * 
     */
    public InMemoryJobFactoryImpl() {
        super();
        log.info("In Memory Job Factory");
    }
    protected Map m = new HashMap();
    
    /** method to access internal map - for testing only */
    public Map getInternalStore() {
        return m;
    }
    

    
    /**
     * @see org.astrogrid.jes.job.JobFactory#createJob(org.astrogrid.jes.job.SubmitJobRequest)
     */
    public Workflow initializeJob(Workflow req) throws JobException {
        Workflow j = super.buildJob(req);
        m.put(id(j),j);
        return j;
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#findJob(java.lang.String)
     */
    public Workflow findJob(JobURN jobURN) throws JobException {
        Workflow j = (Workflow)m.get(jobURN.getContent());
        if (j == null) {
            throw new NotFoundException("Job for urn " + jobURN.getContent() + " not found"); 
        }
        return j;
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#findUserJobs(java.lang.String, java.lang.String, java.lang.String)
     */
    public Iterator findUserJobs(Account acc) {
        Collection results = new ArrayList();
        for (Iterator i = m.values().iterator(); i.hasNext(); ) {
            Workflow j = (Workflow)i.next();
            Account candidate = j.getCredentials().getAccount();
            if (acc.getCommunity().equalsIgnoreCase(candidate.getCommunity()) && acc.getName().equalsIgnoreCase(candidate.getName())) {
                results.add(j);
            }
        }
        return results.iterator();        
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#deleteJob(org.astrogrid.jes.job.Job)
     */
    public void deleteJob(Workflow job) throws JobException {
        if (m.get(id(job)) == null) {
            throw new NotFoundException("no job for " + id(job));
        }
        m.remove(id(job));
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#updateJob(org.astrogrid.jes.job.Job)
     */
    public void updateJob(Workflow job) throws JobException {
        Object hashKey = id(job);
        if (m.get(hashKey) == null) {
            throw new NotFoundException("no job for" + hashKey);
        }
        m.put(id(job),job);
    }



    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Memory-only job store";
    }



    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Jobs stored only in volatile hashmap. no jobs will persist after life of component.\n testing only";
    }



    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: InMemoryJobFactoryImpl.java,v $
Revision 1.9  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.8.20.1  2004/07/30 14:00:10  nw
first working draft

Revision 1.8  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.7  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.6  2004/03/15 01:31:12  nw
jazzed up javadoc

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

Revision 1.1.2.5  2004/02/17 15:55:41  nw
updated to throw exceptions when jobs are not found

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