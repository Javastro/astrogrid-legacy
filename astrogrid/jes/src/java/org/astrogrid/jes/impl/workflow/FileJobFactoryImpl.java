/*$Id: FileJobFactoryImpl.java,v 1.14 2006/01/04 09:52:31 clq2 Exp $
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

import org.astrogrid.common.namegen.FileNameGen;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.util.BaseDirectory;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.file.ResourceManagerException;
import org.exolab.castor.xml.CastorException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Implementation of JobFactory that stores xml documents on a filesystem.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 * @modified nww - looked at ways to optimize this class.
 *
 */
public abstract class FileJobFactoryImpl extends AbstractJobFactoryImpl implements ComponentDescriptor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(FileJobFactoryImpl.class);

    private static final String WORKFLOW_SUFFIX = "-workflow.xml";
    /** Construct a new FileJobFactoryImpl
     *  Construct a new FileJobFactoryImpl
     * @param baseDir directory to store workflow documents in
     * @throws IOException if basedir inaccessible.
     * @throws ResourceManagerException
     * @throws IllegalArgumentException
     */
    public FileJobFactoryImpl(BaseDirectory bd) throws IOException, 
                                                       IllegalArgumentException, 
                                                       ResourceManagerException{
        super(new FileNameGen(bd.getDir(),"jes"));
        log.info("File Store Job Factory");
        this.baseDir = bd.getDir();
        assert baseDir != null;
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

    protected final File mkOutputFile(Workflow j) {
        return mkOutputFile(j.getJobExecutionRecord().getJobId());
    }
    
    protected final File mkOutputFile(JobURN jobURN) {
        return new File(baseDir,URLEncoder.encode(jobURN.getContent() + WORKFLOW_SUFFIX));
    }

    /**
     * @see org.astrogrid.jes.job.JobFactory#createJob(org.astrogrid.jes.job.SubmitJobRequest)
     */
    public Workflow initializeJob(Workflow req) throws JobException {
            Workflow j = buildJob(req);
            FileWriter fw = null;
            try {
                File outFile = mkOutputFile(j);
                fw = new FileWriter(outFile);
                j.marshal(fw);
            } catch (Exception e) {
                throw new JobException("Problem with store",e);
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException ioe) {
                        log.error("failed to close filestore");
                    }
                }
            }
        return j;     
    }
    
    /**
     * @see org.astrogrid.jes.job.JobFactory#findJob(java.lang.String)
     */
    public Workflow  findJob(JobURN jobURN) throws JobException {
        FileReader fr = null;
        try {
        File f = mkOutputFile(jobURN);
        if (! f.exists()) {
            throw new NotFoundException("Couldn't find job " + jobURN);
        }
        fr = new FileReader(f);
        return Workflow.unmarshalWorkflow(fr);
        } catch (CastorException e) {
            throw new JobException("Problem with creating object model",e);
        } catch (IOException e) {
            throw new JobException("Problem with reading xml from store",e);
        } finally {
            if (fr != null) {
                try { 
                    fr.close();
                } catch (IOException ioe) {
                    log.error("failed to close file");
                }
            }
        }
    }
    
    protected static final String SERVER_PREFIX = "jes:" + hostname + "/" ;
    
    protected final String mkPrefix(Account acc) {
        StringBuffer buff = new StringBuffer()
           .append(SERVER_PREFIX)
           .append(acc.getName())
           .append("@")
           .append(acc.getCommunity())
           .append("/");
        return buff.toString();
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#findUserJobs(java.lang.String, java.lang.String, java.lang.String)
     */
    public Iterator findUserJobs(final Account acc) throws JobException {

        File[] jobFiles = baseDir.listFiles(new FilenameFilter() {
            final String searchString = URLEncoder.encode(mkPrefix(acc));
            public boolean accept(File dir, String name) { 
                return name.startsWith(searchString);
            }
        });
        final Iterator i = IteratorUtils.arrayIterator(jobFiles);
        return new Iterator() {

            public void remove() {
                throw new UnsupportedOperationException("don't remove");
            }

            public boolean hasNext() {
                return i.hasNext();
            }
            /** need to make this more resiliant to changes on disk - it's possible that queued changes will be processed, and so the files
             * won't be there when they're come to being read. If this happens now, it skips onto the next item.
             * 'Course this means that we need to handle a null at the end of the list. which is a pity.
             * @see java.util.Iterator#next()
             */
            public Object next() {
                if (!i.hasNext()) { // we've reached the end - maybe can't be helped, if we've had to skip something.
                    logger.warn("Reached unexpected end of iterator");
                    return null;
                }
                File f = (File)i.next();
                try {
                if (! f.exists()) {
                    logger.info("Skipping non-existent file " + f);
                    return this.next(); // recursive call, in case of non-existent file.
                }
                return Workflow.unmarshalWorkflow(new FileReader(f));
                } catch (Exception e) {
                    logger.warn("Failed to unmarshal this file " + f + " skipping");
                    return this.next();
                }
            }
        };
        
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#deleteJob(org.astrogrid.jes.job.Job)
     */
    public void deleteJob(Workflow job) throws JobException {
        File f = mkOutputFile(job);
        if (f.exists()) {
            f.delete();
        } else {
            throw new NotFoundException("Job URN " + id(job) + " not found");
        }
    } 
    /**
     * @see org.astrogrid.jes.job.JobFactory#updateJob(org.astrogrid.jes.job.Job)
     */
    public void updateJob(Workflow j) throws JobException {
        FileWriter fw = null;
        try {
        File outFile = mkOutputFile(j);
        if (! outFile.exists()) {
            throw new NotFoundException("Job URN " + id(j) + " not found");
        }
        fw = new FileWriter(outFile);
        j.marshal(fw);
        } catch (Exception e) {
            throw new JobException("Problem with store",e);
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    log.error("Could not close file writer");
                }
            }
        }
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Filestore-backed job factory";
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Stores jobs on disk as workflow xml document files\n"
        + "store directory: " + baseDir.getAbsolutePath();
    }


    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {        
        TestSuite suite  = new TestSuite("Tests for File Job Factory Imp");
        suite.addTest(new InstallationTest("testBaseDirExists"));
        suite.addTest(new InstallationTest("testWriteFile"));
        suite.addTest(new InstallationTest("testReadFile"));
        return suite;    
    }


    protected class InstallationTest extends TestCase {


        public InstallationTest(String s) {
            super(s);
        }
        
        public void testBaseDirExists() {
            assertTrue("base dir does not exist",baseDir.exists());
            assertTrue("base dir is not a directory",baseDir.isDirectory());
            assertTrue("base dir is not readable and writable",baseDir.canRead() && baseDir.canWrite());
        }
        final File testFile = new File(baseDir,"test-file");
        private final static String CONTENTS = "test file contents";
        public void testWriteFile() throws IOException {
            PrintWriter pw = new PrintWriter( new FileWriter(testFile));
            pw.println(CONTENTS);
            pw.close();
            assertTrue(testFile.exists());
        }
        public void testReadFile() throws IOException {
            BufferedReader reader = new BufferedReader( new FileReader(testFile));
            String line = reader.readLine();
            assertEquals("does not match expected",CONTENTS,line);
        }
    }

}


/* 
$Log: FileJobFactoryImpl.java,v $
Revision 1.14  2006/01/04 09:52:31  clq2
jes-gtr-1462

Revision 1.13.42.1  2005/12/09 23:11:55  gtr
I refactored the base-directory feature out of its inner class and interface in FileJobFactory and into org.aastrogrid.jes.util. This addresses part, but not all, of BZ1487.

Revision 1.13  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.12.42.3  2005/04/12 17:07:50  nw
added cache to filestore

Revision 1.12.42.2  2005/04/11 16:31:14  nw
updated version of xstream.
added caching to job factory

Revision 1.12.42.1  2005/04/11 13:55:54  nw
started using common-namegen

Revision 1.12  2004/12/03 14:47:41  jdt
Merges from workflow-nww-776

Revision 1.11.2.1  2004/12/01 21:48:20  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.11  2004/11/29 20:00:24  clq2
jes-nww-714

Revision 1.10.30.1  2004/11/24 00:19:38  nw
fixed to make reading list of workflows more robust
 - bz#673

Revision 1.10  2004/09/16 21:42:27  nw
made sure all streams are closed

Revision 1.9  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.8  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.7  2004/03/15 01:31:12  nw
jazzed up javadoc

Revision 1.6  2004/03/12 15:31:55  nw
added unit tests

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