/*$Id: ExampleVOTableParsingWorkflowTest.java,v 1.4 2004/08/12 22:05:43 nw Exp $
 * Created on 06-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration.itn6.solarevent;

import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

/** Example workflow that acquires a votable of urls, parses it, and then passes list of urls to parameters of next step.
 * extends the existing fitsworkflow test - as this provides a nice source for a votable.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Aug-2004
 *
 */
public class ExampleVOTableParsingWorkflowTest extends SimpleFitsWorkflowTest{

    /** Construct a new ExampleVOTableParsingWorkflowTest
     * @param applications
     * @param arg0
     */
    public ExampleVOTableParsingWorkflowTest(String arg0) {
        super(arg0); 
    }

    
    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws Exception {
        super.buildWorkflow(); 
        // now the script that mangles the results.
        // not very sophisticated parsing here. - pity votable isn't so easy to parse.
        Script sc = new Script();
        sc.setBody(
                "votable = source.Result; // access result of previous step\n" +
                "parser = new XmlParser(); //create new parser \n" +
                "nodes = parser.parseText(votable); //parse votable into node tree\n" +
                "urls = nodes.depthFirst().findAll{it.name() == 'STREAM'}.collect{it.value()}.flatten(); // filter node tree on 'STREAM', project value\n" +
                "print(urls); // show what we've found\n" /*+ for another test
                "sinkStep = jes.getSteps().find {it.getName() == 'sink-step'}; // find next step in workflow\n" +
                "inputs = sinkStep.getTool().getInput(); // get to set of input parameters" +
                "urls.each { p = jes.newParameter(); p.setName('url'); p.setValue(it); inputs.addParameter(p);} // add a new parameter for each url\n"
                */
        );
        wf.getSequence().addActivity(sc);
      
       /*
        // now the app that consumes the list of parameters. - a dummy for now.
        descr = reg.getDescriptionFor(URL_LIST_SINK);
        Tool sinkTool = descr.createToolFromDefaultInterface();
        // populate other parameters here.
        Step sink = new Step();
        sink.setName("sink-step");
        sink.setTool(sinkTool);
        wf.getSequence().addActivity(sink);
        */
                
    }
    
    public void checkExecutionResults(Workflow result) throws Exception {
        super.checkExecutionResults(result);

        // check script completed.
        Script interstep = (Script)result.getSequence().getActivity(1);
        assertScriptCompleted(interstep);
        
        String urlList = getStdoutOfScript(interstep).trim();
        // shame we haven't got groovy in scope - could evaluate this string to check its a list of url's. will need to parse by hand instead.
       assertEquals('[',urlList.charAt(0));
       assertEquals(']',urlList.charAt(urlList.length() - 1));
       urlList = urlList.substring(1,urlList.length());
       StringTokenizer tok = new StringTokenizer(urlList,", ");
       boolean seenSome = false;
       while(tok.hasMoreElements()) {
           seenSome = true;
           String s = tok.nextToken();
           System.out.println(s);
           assertNotNull(s);
           try {
               new URL(s); // will throw if not valid url.
           } catch (MalformedURLException e) {
               fail("Malformed url " + s);
           }
       }
       assertTrue(seenSome);                                       
        
    }

}


/* 
$Log: ExampleVOTableParsingWorkflowTest.java,v $
Revision 1.4  2004/08/12 22:05:43  nw
added checking of results of steps, and parsing of urls in interstep script

Revision 1.3  2004/08/12 21:30:07  nw
got it working. nice.

Revision 1.2  2004/08/12 15:15:55  nw
getting there

Revision 1.1  2004/08/12 13:33:34  nw
added framework of classes for testing the solar event science case.

Revision 1.2  2004/08/09 08:35:52  nw
no change

Revision 1.1  2004/08/06 10:28:23  nw
example workflow
 
*/