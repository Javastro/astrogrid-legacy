/*$Id: CompositeFitsVotableParsingSolarMovieWorkflowTest.java,v 1.8 2004/11/24 19:49:22 clq2 Exp $
 * Created on 12-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.externaldep.itn6.solarevent;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;
import org.astrogrid.workflow.integration.itn6.solarevent.ExampleVOTableParsingWorkflowTest;
import org.astrogrid.workflow.integration.itn6.solarevent.SimpleConcatToolWorkflowTest;
import org.astrogrid.workflow.integration.itn6.solarevent.SimpleFitsWorkflowTest;
import org.astrogrid.workflow.integration.itn6.solarevent.SolarEventKeys;

import java.io.IOException;
import java.io.InputStream;

/**  The big one - performs a fits query, extracts urls from votable, and then passes all urls to the solar movie tool.
 *
 * 
 * @see ExampleVOTableParsingWorkflowTest
 * @see SimpleConcatToolWorkflowTest
 * @see SimpleSECWorkflowTest
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Aug-2004
 *
 */
public class CompositeFitsVotableParsingSolarMovieWorkflowTest extends SimpleFitsWorkflowTest  {

    /** Construct a new CompositeSecVotableParsingElizabethWorkflowTest
     * @param applications
     * @param arg0
     */
    public CompositeFitsVotableParsingSolarMovieWorkflowTest(String arg0) {
        super(new String[]{FITS_APP,MPEG_APP}, arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        target = createIVORN("/CompositeFitsVotableSolarMovieWorkflowTest.dat");
        client = new VoSpaceClient(user);
        // remove output file if there already.
        try {
            client.delete(target);
        } catch (Exception e) {
            // don't care. just make sure its gone.
        }          
    }
    protected VoSpaceClient client;
    protected Ivorn target;
    
    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws Exception {
        super.buildWorkflow(); // creates the fits step.
        // now the script that mangles the results.
        // not very sophisticated parsing here. - pity votable isn't so easy to parse.
        Script sc = new Script();
        sc.setBody(
                "votable = source.Result; // access result of previous step\n" +
                "parser = new XmlParser(); //create new parser \n" +
                "nodes = parser.parseText(votable); //parse votable into node tree\n" +
                "urls = nodes.depthFirst().findAll{it.name() == 'STREAM'}.collect{it.value()}.flatten(); // filter node tree on 'STREAM', project value\n" +
                "print(urls); // show what we've got\n" + 
                "concatStep = jes.getSteps().find {it.getName() == 'CallMakeMPEGFitsImages'}; // find next step in workflow\n" +
                "inputs = concatStep.getTool().getInput(); // get to set of input parameters\n" +
                "inputs.clearParameter(); // clear what's there already \n" +
                "urls.each { p = jes.newParameter(); p.setName('InputFiles'); p.setIndirect(true); p.setValue(it); inputs.addParameter(p);} // add a new parameter for each url\n"                
        );
        wf.getSequence().addActivity(sc);
      
        // What's the name of the Solar Movie app in terms of reg.getDescriptionFor?
        // Where do I find the names like CONCAT_APP, MPEG_APP, and FITS_APP?
        ApplicationDescription descr = reg.getDescriptionFor(MPEG_APP);
        Tool solarMovieTool = descr.createToolFromDefaultInterface();
        ParameterValue result = (ParameterValue)solarMovieTool.findXPathValue("output/parameter[name='OutputFile']");
        assertNotNull(result);
        result.setIndirect(true); // want to get results into myspace
        result.setValue(target.toString());
        Step sink = new Step();
        sink.setName("CallMakeMPEGFitsImages");
        sink.setTool(solarMovieTool);
        wf.getSequence().addActivity(sink);
                        
        
    }

    public void checkExecutionResults(Workflow result) throws Exception {
        super.checkExecutionResults(result);
        // check result file exists...
        try {            
            InputStream is = client.getStream(target);
            assertNotNull(is);
            //@todo add some more checking of data here.
        } catch (IOException e) {
            softFail("exception opening result stream" + e.getMessage());
        }        
    }
}


/* 
$Log: CompositeFitsVotableParsingSolarMovieWorkflowTest.java,v $
Revision 1.8  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.5.8.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.5  2004/11/02 15:30:12  nw
fixed name of input parameter that script is writing to.

Revision 1.4  2004/09/23 10:02:39  nw
fixed script to refer to previously-renamed movie-maker step.
removed call to return results back to workflow (going to be huge)

Revision 1.3  2004/09/20 15:03:53  pah
update the output parameter name

Revision 1.2  2004/09/07 12:57:23  nw
fixed little bug in embedded script - need to clear existing parameters first.

Revision 1.1  2004/08/26 21:50:43  eca
Integration test of CallMakeMPEGFitsImage based on Noel's concat test.

Revision 1.2  2004/08/22 01:49:08  nw
improved concurrent behaviour

Revision 1.1  2004/08/17 15:38:23  nw
set of integration tests that require external resources.

Revision 1.1  2004/08/12 13:33:34  nw
added framework of classes for testing the solar event science case.
 
*/