/*$Id: SimpleFitsWorkflowTest.java,v 1.10 2004/11/19 10:27:29 clq2 Exp $
 * Created on 12-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration.itn6.solarevent;

import net.sourceforge.groboutils.util.xml.v1.XMLUtil;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.io.Piper;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.store.Ivorn;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/** workflow that tests the fits service
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Aug-2004
 *
 */
public class SimpleFitsWorkflowTest extends AbstractTestForWorkflow implements SolarEventKeys{

    /** Construct a new SimpleFitsWorkflowTest
     * @param applications
     * @param arg0
     */
    public SimpleFitsWorkflowTest(String arg0) {
        super(new String[]{FITS_APP}, arg0);
    }
 
    /** Construct a new SimpleFitsWorkflowTest
     * @param strings
     * @param arg0
     */
    public SimpleFitsWorkflowTest(String[] strings, String arg0) {
        super(strings,arg0);
    }

    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws Exception {
        wf.setName(this.getClass().getName());
        ApplicationDescription desc = reg.getDescriptionFor(FITS_APP);
        Tool fitsTool = desc.createToolFromDefaultInterface();
        // populate tool.
        ParameterValue format = (ParameterValue)fitsTool.findXPathValue("input/parameter[name='Format']");
        assertNotNull(format);
        format.setIndirect(false);
        format.setValue("VOTABLE");
        
        ParameterValue query= (ParameterValue)fitsTool.findXPathValue("input/parameter[name='Query']");
        assertNotNull(query);
        InputStream is = this.getClass().getResourceAsStream("/org/astrogrid/datacenter/integration/clientside/SimpleFITSQuery-adql074.xml");
        assertNotNull(is);
        StringWriter out = new StringWriter();
        Piper.pipe(new InputStreamReader(is),out);
        query.setIndirect(false); 
        query.setValue(out.toString());
                       
        ParameterValue result = (ParameterValue)fitsTool.findXPathValue("output/parameter[name='Result']");
        assertNotNull(result);
        result.setIndirect(false); // want to get result straight back.
                
        
        // add to the workflow.
        Step s = new Step();
        s.setDescription("Fits query");
        s.setName(FITS_APP);
        s.setResultVar("source");
        s.setTool(fitsTool);
        wf.getSequence().addActivity(s);
    }

    public void checkExecutionResults(Workflow result) throws Exception {
        super.checkExecutionResults(result);
        
        Step s = (Step)result.getSequence().getActivity(0);
        assertStepCompleted(s);
        ResultListType r = getResultOfStep(s);
        softAssertEquals("only expected a single result",1,r.getResultCount());
        AstrogridAssert.assertVotable(r.getResult(0).getValue());
        
       
    }
}


/* 
$Log: SimpleFitsWorkflowTest.java,v $
Revision 1.10  2004/11/19 10:27:29  clq2
nww-itn07-659

Revision 1.9.14.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.9  2004/10/22 08:21:57  pah
input query file had moved

Revision 1.8  2004/08/27 13:18:08  nw
used AstrogridAssert to check results more thoroughly.

Revision 1.7  2004/08/17 13:35:58  nw
added constructor for subclassing with

Revision 1.6  2004/08/12 22:05:43  nw
added checking of results of steps, and parsing of urls in interstep script

Revision 1.5  2004/08/12 21:30:07  nw
got it working. nice.

Revision 1.4  2004/08/12 15:54:22  nw
fixed faulty parameter name.

Revision 1.3  2004/08/12 15:15:55  nw
getting there

Revision 1.2  2004/08/12 14:30:03  nw
constructed workflows to call fits and sec. need to check the results next

Revision 1.1  2004/08/12 13:33:34  nw
added framework of classes for testing the solar event science case.
 
*/