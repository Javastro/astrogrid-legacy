/*$Id: SimpleMovieMakerWorkflowTest.java,v 1.2 2004/09/08 14:13:56 nw Exp $
 * Created on 17-Aug-2004
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
import org.astrogrid.io.Piper;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;
import org.astrogrid.workflow.integration.itn6.solarevent.SolarEventKeys;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/** integration test that builds a single-step workflow document, that exercises the moviemaker tool.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Aug-2004
 *
 */
public class SimpleMovieMakerWorkflowTest extends AbstractTestForWorkflow
        implements SolarEventKeys {

    /** Construct a new SimpleMovieMakerWorkflowTest
     * @param applications
     * @param arg0
     */
    public SimpleMovieMakerWorkflowTest(String arg0) {
        super(new String[]{MPEG_APP}, arg0);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        target = createIVORN("/SimpleMovieMakerWorkflowTest.mpg");
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
    protected static final String fits1 ="http://msslxy.mssl.ucl.ac.uk:8080/TraceFits/ObtainFITS?_file=trace4a/tri/week20020728/tri20020728.0500";
    protected static final String fits2 = "http://msslxy.mssl.ucl.ac.uk:8080/TraceFits/ObtainFITS?_file=trace4a/tri/week20020728/tri20020728.0600";


    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws Exception {
        wf.setName(this.getClass().getName());
        ApplicationDescription desc = reg.getDescriptionFor(MPEG_APP);
        Tool movieTool = desc.createToolFromDefaultInterface();

        // populate tool.
        ParameterValue movie = (ParameterValue)movieTool.findXPathValue("output/parameter[name='OutputFile']");
        assertNotNull(movie);
        movie.setIndirect(true); // sending movie to a location somewhere 
        movie.setValue(target.toString());
        
        ParameterValue image= (ParameterValue)movieTool.findXPathValue("input/parameter[name='InputFiles']");
        assertNotNull(image);
        image.setIndirect(true);
        image.setValue(fits1);
        
        ParameterValue image2 = copyParameter(image);
        image2.setValue(fits2);
        movieTool.getInput().addParameter(image2);
  
        
        // add to the workflow.
        Step s = new Step();
        s.setDescription("Movie tool");
        s.setName("movie tool");
        s.setTool(movieTool);
        wf.getSequence().addActivity(s);
    }

    public void checkExecutionResults(Workflow result) throws Exception {
        super.checkExecutionResults(result); // checks workflow completed.
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
$Log: SimpleMovieMakerWorkflowTest.java,v $
Revision 1.2  2004/09/08 14:13:56  nw
fixed invalid workflow problem

Revision 1.1  2004/08/17 15:38:23  nw
set of integration tests that require external resources.
 
*/