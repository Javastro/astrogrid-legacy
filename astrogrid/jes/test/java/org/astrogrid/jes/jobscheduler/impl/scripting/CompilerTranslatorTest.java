/*$Id: CompilerTranslatorTest.java,v 1.1 2004/07/09 09:32:12 nw Exp $
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/** test that the compiler and translator stylesheets do what they should.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-May-2004
 *
 */
public class CompilerTranslatorTest extends AbstractTestWorkflowInputs {
    /** Construct a new CompilerTranslatorTest
     * @param arg
     */
    public CompilerTranslatorTest(String arg) {
        super(arg);
    }
    /**
     * @see org.astrogrid.jes.AbstractTestWorkflowInputs#testIt(java.io.InputStream, int)
     */
    protected void testIt(InputStream is, int resourceNum) throws Exception {
        // first make it into a castor-style xml document - as this is what we're going to be consuming.
        Workflow wf = Workflow.unmarshalWorkflow(new InputStreamReader(is));
        StringWriter document = new StringWriter();
        wf.marshal(document);
        document.close();
               
        DefaultTransformers trans = new DefaultTransformers();
        Transformer compiler = trans.getCompiler();
        Source source = new StreamSource(new StringReader(document.toString()));
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        compiler.transform(source,result);
        sw.close();        
        // now feed it throught the translator
        Source source1 = new StreamSource(new StringReader(sw.toString()));
        StringWriter sw1 = new StringWriter();
        Result result1 = new StreamResult(sw1);
        Transformer translator = trans.getTranslator();
        translator.transform(source1,result1);
        sw1.close();
        
       // System.out.println(sw1.toString());
       // System.out.println("-------------------------------------------------------------------------------------------------------");
     
    }
}


/* 
$Log: CompilerTranslatorTest.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:42  nw
first checkin of prototype scrpting workflow interpreter
 
*/