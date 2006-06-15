/*$Id: JobsSystemTest.java,v 1.3 2006/06/15 09:18:24 nw Exp $
 * Created on 09-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ACRTestSetup;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.axis.utils.XMLUtils;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathAPI;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** exercise the jobs interface.
 * some order-dependency between the last tests.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Aug-2005
 *
 */
public class JobsSystemTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        reg = getACR();
        jobs = (Jobs)reg.getService(Jobs.class);
        assertNotNull(jobs);
    }
    protected Jobs jobs;
    protected ACR reg;
    protected ACR getACR() throws Exception {
        return ACRTestSetup.acrFactory.getACR();
    }
    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(JobsSystemTest.class),true); // login.
    }    


    public void testCreateJob() throws Exception {
        Document doc = jobs.createJob();
        assertNotNull(doc);
        //@todo - aadd more verification here.
    }
    public void testList() throws Exception{
        URI[] a = jobs.list();
        assertNotNull(a);
        assertTrue(a.length > 0);
        for (int i =0 ; i < a.length; i++) {
            assertNotNull(a[i]);
        }
        
    }

    public void testListFully() throws Exception{
        ExecutionInformation[] a = jobs.listFully();
        URI[] u = jobs.list();
        assertNotNull(a);
        assertNotNull(u);
        assertEquals(a.length,u.length);
        for (int i = 0; i < a.length; i++) {
            assertEquals(u[i],a[i].getId());
            assertNotNull(a[i].getStartTime());
            assertNotNull(a[i].getStatus());      
            System.out.println(a[i]);
        }
    }

    public void testGetJobTranscript() throws Exception{
        URI[] u = jobs.list();
        for (int i =0; i < u.length; i++) {
            Document doc = jobs.getJobTranscript(u[i]);
            assertNotNull(doc);
            // check we can parse this as a workflow
            Workflow wf = (Workflow)Unmarshaller.unmarshal(Workflow.class,doc);
            assertNotNull(wf);
            // check it's the correct workflow.
            assertEquals(u[i].toString(),wf.getJobExecutionRecord().getJobId().getContent());
        }
        
    }

    public void testGetJobInformation() throws Exception{
        ExecutionInformation[] a = jobs.listFully();
        for (int i = 0; i < a.length; i++) {
            ExecutionInformation other = jobs.getJobInformation(a[i].getId());
            assertNotNull(other);
            assertEquals(a[i],other);
            assertEquals(a[i].getStatus(),other.getStatus());            
        }
    }

    public static final String TEST_WORKFLOW = "workflow.xml";
    public static URI id;
    public void testSubmitJob() throws Exception {
        InputStream is = getClass().getResourceAsStream(TEST_WORKFLOW);
        assertNotNull(is);
        Workflow wf = Workflow.unmarshalWorkflow(new InputStreamReader(is));
        assertNotNull(wf);
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(wf,doc);
        URI[] orig = jobs.list();
        id = jobs.submitJob(doc);
        assertFalse(Arrays.asList(orig).contains(id));
        
        URI[] now = jobs.list();
        assertEquals(orig.length + 1,now.length);
        assertTrue(Arrays.asList(now).contains(id));
    }

    public void testSubmitStoredJob() throws Exception {
        URL url = getClass().getResource(TEST_WORKFLOW);
        assertNotNull(url);
        assertEquals("file",url.getProtocol());
        URI[] orig = jobs.list();
        id = jobs.submitStoredJob(new URI(url.toString()));
        assertFalse(Arrays.asList(orig).contains(id));
        
        URI[] now = jobs.list();
        assertEquals(orig.length + 1,now.length);
        assertTrue(Arrays.asList(now).contains(id));        
    }
    
    public void testSubmitMyspaceStoredJob() throws Exception {
        // place test file in myspace.
        URL url = getClass().getResource(TEST_WORKFLOW);
        assertNotNull(url);
        Myspace ms = (Myspace)reg.getService(Myspace.class);
        URI testFile = new URI("#system-test/test-workflow.xml");
        if (!ms.exists(testFile)) {
            ms.createFile(testFile);
        }
        assertTrue(ms.exists(testFile));
        ms.copyURLToContent(url,testFile);
        
        // submit this test file.
        URI[] orig = jobs.list();
        id = jobs.submitStoredJob(testFile);
        assertFalse(Arrays.asList(orig).contains(id));
        
        URI[] now = jobs.list();
        assertEquals(orig.length + 1,now.length);
        assertTrue(Arrays.asList(now).contains(id));            
        
    }
    
    public void testCancelJob() throws Exception {
        jobs.cancelJob(id);
    }

    public void testDeleteJob() throws Exception {

        URI[] orig = jobs.list();
        assertTrue(Arrays.asList(orig).contains(id));
        
        jobs.deleteJob(id);
        URI[] now = jobs.list();
        assertEquals(orig.length ,now.length +1);
        assertFalse(Arrays.asList(now).contains(id));            
    }



}


/* 
$Log: JobsSystemTest.java,v $
Revision 1.3  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/09 17:33:07  nw
finished system tests for ag components.
 
*/