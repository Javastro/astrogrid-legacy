/*$Id: FileJobFactoryImpl.java,v 1.3 2004/03/03 01:13:41 nw Exp $
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

import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.exolab.castor.xml.CastorException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/** Implementation of JobFactory that stores xml documents on a filesystem.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 *
 */
public class FileJobFactoryImpl extends AbstractJobFactoryImpl {
    private static final String WORKFLOW_SUFFIX = "-workflow.xml";
    /** Construct a new FileJobFactoryImpl
     *  Construct a new FileJobFactoryImpl
     * @param baseDir directory to store workflow documents in
     * @throws IOException if basedir inaccessible.
     */
    public FileJobFactoryImpl(File baseDir) throws IOException{
        super();
        log.info("File Store Job Factory");
        this.baseDir = baseDir;
        log.info("Base directory of file store:" + baseDir.getAbsolutePath());
        initStore();       
    }
    
    
    protected final File baseDir;
    /** initialize the store directory */
    protected void initStore() throws IOException {
        // possible dir doesn't exist first time round..
        if (! baseDir.exists()) {
            log.info("Initializing file store");
            baseDir.mkdirs();
        } 
        assert baseDir.isDirectory();
        assert baseDir.canRead();
        assert baseDir.canWrite();
    }

    protected File mkOutputFile(Job j) {
        return mkOutputFile(j.getId());
    }
    
    protected File mkOutputFile(JobURN jobURN) {
        return new File(baseDir,URLEncoder.encode(jobURN + WORKFLOW_SUFFIX));
    }

    /**
     * @see org.astrogrid.jes.job.JobFactory#createJob(org.astrogrid.jes.job.SubmitJobRequest)
     */
    public Job createJob(SubmitJobRequest req) throws JobException {
            JobImpl j = buildJob(req);
            try {
                File outFile = mkOutputFile(j);
                FileWriter fw = new FileWriter(outFile);
                j.getWorkflow().marshal(fw);
                fw.close();
            } catch (Exception e) {
                throw new JobException("Problem with store",e);
            }            
        return j;     
    }
    
    /**
     * @see org.astrogrid.jes.job.JobFactory#findJob(java.lang.String)
     */
    public Job findJob(JobURN jobURN) throws JobException {
        try {
        File f = mkOutputFile(jobURN);
        if (! f.exists()) {
            throw new NotFoundException("Couldn't find job " + jobURN);
        }
        Workflow w = Workflow.unmarshalWorkflow(new FileReader(f));
        return new JobImpl(w);
        } catch (CastorException e) {
            throw new JobException("Problem with creating object model",e);
        } catch (IOException e) {
            throw new JobException("Problem with reading xml from store",e);
        }
        
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#findUserJobs(java.lang.String, java.lang.String, java.lang.String)
     */
    public Iterator findUserJobs(final String userid, final String community, String jobListXML) throws JobException {
        File[] jobFiles = baseDir.listFiles(new FilenameFilter() {
            final String searchString = URLEncoder.encode("jes:" + userid.trim() +":" + community.trim() + ":");
            public boolean accept(File dir, String name) { 
                return name.startsWith(searchString);  
            }
        });
        List l = Arrays.asList(jobFiles);
        final Iterator i = l.iterator();
        return new Iterator() {

            public void remove() {
                throw new UnsupportedOperationException("don't remove");
            }

            public boolean hasNext() {
                return i.hasNext();
            }

            public Object next() {
                File f = (File)i.next();
                try {
                if (! f.exists()) {
                    return null;
                }
                Workflow w = Workflow.unmarshalWorkflow(new FileReader(f));
                return new JobImpl(w);
                } catch (Exception e) {
                    throw new RuntimeException("Problem with store",e);
                }
            }
        };
        
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#deleteJob(org.astrogrid.jes.job.Job)
     */
    public JobURN deleteJob(Job job) throws JobException {
        File f = mkOutputFile(job);
        if (f.exists()) {
            f.delete();
            return job.getId();
        } else {
            throw new NotFoundException("Job URN " + job.getId() + " not found");
        }
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#updateJob(org.astrogrid.jes.job.Job)
     */
    public void updateJob(Job job) throws JobException {
        JobImpl j = (JobImpl)job;
        try {
        File outFile = mkOutputFile(j);
        if (! outFile.exists()) {
            throw new NotFoundException("Job URN " + job.getId() + " not found");
        }
        FileWriter fw = new FileWriter(outFile);
        j.getWorkflow().marshal(fw);
        fw.close();
        } catch (Exception e) {
            throw new JobException("Problem with store",e);
        }
    }
}


/* 
$Log: FileJobFactoryImpl.java,v $
Revision 1.3  2004/03/03 01:13:41  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.6  2004/02/19 13:40:09  nw
updated to fit new interfaces

Revision 1.1.2.5  2004/02/17 14:04:38  nw
little fix to match new job urn format

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