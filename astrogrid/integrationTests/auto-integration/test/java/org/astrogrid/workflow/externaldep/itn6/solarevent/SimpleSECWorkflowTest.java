/*$Id: SimpleSECWorkflowTest.java,v 1.2 2004/08/22 01:50:47 nw Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.io.Piper;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;
import org.astrogrid.workflow.integration.itn6.solarevent.SolarEventKeys;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/** test single step workflow that calls the sec.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Aug-2004
 *
 */
public class SimpleSECWorkflowTest  extends AbstractTestForWorkflow implements SolarEventKeys{

    /** Construct a new SimpleSECWorkflowTest
     * @param applications
     * @param arg0
     */
    public SimpleSECWorkflowTest( String arg0) {
        super(new String[]{SEC_APP}, arg0);
    }

    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws Exception {
        wf.setName(this.getClass().getName());
        ApplicationDescription desc = reg.getDescriptionFor(SEC_APP);
        Tool secTool = desc.createToolFromDefaultInterface();
        // populate tool.
        ParameterValue format = (ParameterValue)secTool.findXPathValue("input/parameter[name='Format']");
        assertNotNull(format);
        format.setIndirect(false);
        format.setValue("VOTABLE");
        
        ParameterValue query= (ParameterValue)secTool.findXPathValue("input/parameter[name='Query']");
        assertNotNull(query);
        InputStream is = this.getClass().getResourceAsStream("/org/astrogrid/datacenter/integration/SimpleSECQuery-adql05.xml");
        assertNotNull(is);
        StringWriter out = new StringWriter();
        Piper.pipe(new InputStreamReader(is),out);
        query.setIndirect(false); 
        query.setValue(out.toString());
                       
        ParameterValue result = (ParameterValue)secTool.findXPathValue("output/parameter[name='Result']");
        assertNotNull(result);
        result.setIndirect(false); // want to get result straight back.
                
        
        // add to the workflow.
        Step s = new Step();
        s.setDescription("SEC query");
        s.setName(SEC_APP);
        s.setTool(secTool);
        s.setResultVar("results"); // improves concurrent behaviour for testing
        wf.getSequence().addActivity(s);
    }

    public void checkExecutionResults(Workflow result) throws Exception {
        super.checkExecutionResults(result);
        
        Step s = (Step)result.getSequence().getActivity(0);
        assertStepCompleted(s);
        ResultListType r = getResultOfStep(s);
        softAssertEquals("only expected a single result",1,r.getResultCount());
        // now need to find helper to check that result is a votable.
        System.out.println(r.getResult(0).getValue());
    }
}




/* 
$Log: SimpleSECWorkflowTest.java,v $
Revision 1.2  2004/08/22 01:50:47  nw
improved concurrent behaviour

Revision 1.1  2004/08/17 15:38:23  nw
set of integration tests that require external resources.

Revision 1.4  2004/08/12 22:05:43  nw
added checking of results of steps, and parsing of urls in interstep script

Revision 1.3  2004/08/12 21:30:07  nw
got it working. nice.

Revision 1.2  2004/08/12 14:30:03  nw
constructed workflows to call fits and sec. need to check the results next

Revision 1.1  2004/08/12 13:33:34  nw
added framework of classes for testing the solar event science case.
 
*/