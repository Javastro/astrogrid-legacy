/*$Id: CachingFileJobFactory.java,v 1.3 2006/01/04 09:52:31 clq2 Exp $
 * Created on 11-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.job.DuplicateFoundException;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.util.BaseDirectory;
import org.astrogrid.jes.util.Cache;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.transaction.file.ResourceManagerException;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map;

/** Decorator for another job factory that maintains a LRU cache of workflow documents - hopefully we can see some performance gain 
 * by reducing the amount of xml parsing and marshalling required.
 * 
 * idea is that cache never fails / disrupts the system. any update gets written back to store and cache. however, reads may stop at cache, if match is found.
 * assumes a single-thread write design - which is what we've got.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Apr-2005
 *
 */
public class CachingFileJobFactory extends FileJobFactoryImpl {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CachingFileJobFactory.class);

    /** Construct a new CachingJobFactoryDecorator
     * @throws IOException
     * @throws ResourceManagerException
     * @throws IllegalArgumentException
     * 
     */
    public CachingFileJobFactory(BaseDirectory bd) throws IllegalArgumentException, 
                                                          IOException, 
                                                          ResourceManagerException{
        super(bd);       
    }
    protected static final int WORKFLOW_CACHE_SIZE = 20;
    protected final Cache cache = new Cache(WORKFLOW_CACHE_SIZE);
     
    /**
     * @see org.astrogrid.jes.job.JobFactory#initializeJob(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public Workflow initializeJob(Workflow req) throws JobException {
        Workflow wf = super.initializeJob(req);
        cache.stuff(wf.getJobExecutionRecord().getJobId(),wf);
        return wf;
    }

    /**
     * @see org.astrogrid.jes.job.JobFactory#findJob(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public Workflow findJob(JobURN urn) throws JobException, NotFoundException,
            DuplicateFoundException {
        Workflow wf = (Workflow)cache.check(urn);
        if (wf != null) {
            return wf;
        }
        wf = super.findJob(urn);
        //cache.stuff(wf); not safe to stuff in the cache if it's a cache miss on read - as is being done from the read thread.
        return wf;
    }

 /*can't easily cache this one - pity, as it's the one used most by the client - needs a redesign.
    public Iterator findUserJobs(Account acc) throws JobException {
        
        return .findUserJobs(acc);
    }
*/
    /**
     * @see org.astrogrid.jes.job.JobFactory#deleteJob(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public void deleteJob(Workflow job) throws JobException {
        super.deleteJob(job);
        cache.delete(job.getJobExecutionRecord().getJobId());        
    }

    /**
     * @see org.astrogrid.jes.job.JobFactory#updateJob(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public void updateJob(Workflow job) throws JobException {
        super.updateJob(job);
        cache.stuff(job.getJobExecutionRecord().getJobId(),job);        
    }

}


/* 
$Log: CachingFileJobFactory.java,v $
Revision 1.3  2006/01/04 09:52:31  clq2
jes-gtr-1462

Revision 1.2.42.1  2005/12/09 23:11:55  gtr
I refactored the base-directory feature out of its inner class and interface in FileJobFactory and into org.aastrogrid.jes.util. This addresses part, but not all, of BZ1487.

Revision 1.2  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.1.2.2  2005/04/12 17:07:51  nw
added cache to filestore

Revision 1.1.2.1  2005/04/11 16:31:14  nw
updated version of xstream.
added caching to job factory

Revision 1.1.2.1  2005/04/11 13:55:56  nw
started using common-namegen
 
*/