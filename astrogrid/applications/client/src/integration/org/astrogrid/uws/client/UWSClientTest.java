/*
 * $Id: UWSClientTest.java,v 1.1 2008/09/24 13:47:18 pah Exp $
 * 
 * Created on 22 Sep 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.uws.client;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import net.ivoa.uws.ExecutionPhase;
import net.ivoa.uws.JobSummary;
import net.ivoa.uws.ResultList;
import net.ivoa.uws.ResultReference;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 22 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class UWSClientTest {

    private UWSClient client;
    private String tool;
    private JobSummary job;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        StringBuffer toolbuf = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/org/astrogrid/uws/client/testTool.xml")));
        String line;
        while ((line= rd.readLine())!=null) {
            toolbuf.append(line);
        }
        tool = toolbuf.toString();
        client = new UWSClient(new URI("http://localhost:8888/astrogrid-cea-cec/uws/jobs"));
    }
    
    @After
    public void tearDown() throws Exception{
        if(job != null)
        {
            client.deleteJob(job.getJobId());
            job = null;
        }
    }

    /**
     * Test method for {@link org.astrogrid.uws.client.UWSClient#createJob(java.lang.String)}.
     * @throws UWSJobCreationException 
     */
    @Test
    public void testCreateJob() throws Exception {
        job = client.createJob(tool);
        assertNotNull(job);
    }

    /**
     * Test method for {@link org.astrogrid.uws.client.UWSClient#deleteJob(java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public void testDeleteJob() throws Exception {
        testCreateJob();
        client.deleteJob(job.getJobId());
        job=null;
    }

    /**
     * Test method for {@link org.astrogrid.uws.client.UWSClient#getPhase(java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public void testGetPhase() throws Exception {
        testCreateJob();
        ExecutionPhase phase = client.getPhase(job.getJobId());
        assertNotNull(phase);
        assertEquals("the phase should be", ExecutionPhase.PENDING, phase);
    }

    /**
     * Test method for {@link org.astrogrid.uws.client.UWSClient#getResults(java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public void testGetResults() throws Exception {
        testRunJob();
        ResultList resultList = client.getResults(job.getJobId());
        assertNotNull(resultList);
        List<ResultReference> results = resultList.getResult();
        assertEquals("number of results", 1, results.size());
        
    }

    /**
     * Test method for {@link org.astrogrid.uws.client.UWSClient#jobDetail(java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public void testJobDetail() throws Exception {
        testCreateJob();
        JobSummary myJob = client.jobDetail(job.getJobId());
        assertNotNull(myJob);
    }

    /**
     * Test method for {@link org.astrogrid.uws.client.UWSClient#listJobs()}.
     * @throws Exception 
     */
    @Test
    public void testListJobs() throws Exception {
       testCreateJob();
       JobSummary myjob = job;
       testCreateJob();
       List<String> jobs = client.listJobs();
       assertNotNull(jobs);
       assertTrue("should be at least two jobs in queue", jobs.size() > 1);
       client.deleteJob(myjob.getJobId());
       
    }

    /**
     * Test method for {@link org.astrogrid.uws.client.UWSClient#setDestruction(java.lang.String, org.joda.time.DateTime)}.
     * @throws Exception 
     */
    @Test
    public void testSetDestruction() throws Exception {
        testCreateJob();
        
        DateTime date = new DateTime();
        DateTime futuredate = date.plusMinutes(4);
        DateTime dest = client.setDestruction(job.getJobId(), futuredate );
        assertEquals("destruction dates",futuredate,dest);
    }

    /**
     * Test method for {@link org.astrogrid.uws.client.UWSClient#setPhase(java.lang.String, String)}.
     * @throws Exception 
     */
    @Test
    public void testSetPhase() throws Exception {
        testCreateJob();
        ExecutionPhase phase = client.setPhase(job.getJobId(), "ABORT");
        assertEquals("phase ", ExecutionPhase.ABORTED, phase);
        
    }

    /**
     * Test method for {@link org.astrogrid.uws.client.UWSClient#setTermination(java.lang.String, org.joda.time.Period)}.
     */
    @Test
    public void testSetTermination() {
        fail("Not yet implemented");

    }

    
    @Test
    public void testRunJob() throws Exception {
        testCreateJob();
        client.runJob(job.getJobId());
        Thread.sleep(5000); // wait 5 secs this should be enough
        ExecutionPhase phase = client.getPhase(job.getJobId());
        assertEquals("job should have finished",ExecutionPhase.COMPLETED, phase);
    }
}


/*
 * $Log: UWSClientTest.java,v $
 * Revision 1.1  2008/09/24 13:47:18  pah
 * added generic UWS client code
 *
 */
