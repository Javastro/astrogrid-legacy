/*$Id: AnnotatorTest.java,v 1.1 2004/07/09 09:32:12 nw Exp $
 * Created on 11-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.scripting;

import org.astrogrid.jes.AbstractTestWorkflowInputs;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.Extension;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/** test annotator stylesheet does what it should.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-May-2004
 *
 */
public class AnnotatorTest extends AbstractTestWorkflowInputs {
    /** Construct a new AnnotatorTest
     * @param arg
     */
    public AnnotatorTest(String arg) {
        super(arg);
    }
       
    /**
     * @see org.astrogrid.jes.AbstractTestWorkflowInputs#testIt(java.io.InputStream, int)
     */
    protected void testIt(InputStream is, int resourceNum) throws Exception {
        // first make it into a castor-style xml document - as this is what we're going to be consuming.
        Workflow wf0 = Workflow.unmarshalWorkflow(new InputStreamReader(is));
        JobExecutionRecord rec = new JobExecutionRecord();
        JobURN urn = new JobURN();
        urn.setContent("fred");
        rec.setJobId(urn);
        Extension e = new Extension();
        e.setKey("barney");
        rec.addExtension(e);
        wf0.setJobExecutionRecord(rec);
        StringWriter document = new StringWriter();
        wf0.marshal(document);
        document.close();
                       
        DefaultTransformers trans = new DefaultTransformers();
        Transformer annotator = trans.getWorkflowAnnotator();
        Source source = new StreamSource(new StringReader(document.toString()));
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        annotator.transform(source,result);
        sw.close();
        assertNotNull(sw.toString());
        // check we can load transformed doc as workflow still.
        Workflow wf = Workflow.unmarshalWorkflow(new StringReader(sw.toString()));
        assertNotNull(wf);
        // now check we've got id's where we expect. - only checking on the root for now..
        assertNotNull("no id seen on root sequence",wf.getSequence().getId());
        System.out.println(wf.getId());
        // check that execution record has been preserved
        assertNotNull(wf.getJobExecutionRecord());
        assertNotNull(wf.getJobExecutionRecord().getJobId());
        assertEquals("fred",wf.getJobExecutionRecord().getJobId().getContent());
        assertEquals(1,wf.getJobExecutionRecord().getExtensionCount());
    }
}


/* 
$Log: AnnotatorTest.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:42  nw
first checkin of prototype scrpting workflow interpreter
 
*/