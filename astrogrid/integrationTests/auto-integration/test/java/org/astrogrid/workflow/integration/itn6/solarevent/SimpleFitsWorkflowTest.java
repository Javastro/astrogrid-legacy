/*$Id: SimpleFitsWorkflowTest.java,v 1.4 2004/08/12 15:54:22 nw Exp $
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

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.io.Piper;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.store.Ivorn;
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

/**
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
        InputStream is = this.getClass().getResourceAsStream("/org/astrogrid/datacenter/integration/SimpleFITSQuery-adql073.xml");
        assertNotNull(is);
        StringWriter out = new StringWriter();
        Piper.pipe(new InputStreamReader(is),out);
        query.setIndirect(false); 
        query.setValue(out.toString());
                       
        ParameterValue result = (ParameterValue)fitsTool.findXPathValue("output/parameter[name='Target']");
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
        
        // dump to screen for now.
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(result,doc);
        XMLUtils.PrettyDocumentToStream(doc,System.out);
    }
}


/* 
$Log: SimpleFitsWorkflowTest.java,v $
Revision 1.4  2004/08/12 15:54:22  nw
fixed faulty parameter name.

Revision 1.3  2004/08/12 15:15:55  nw
getting there

Revision 1.2  2004/08/12 14:30:03  nw
constructed workflows to call fits and sec. need to check the results next

Revision 1.1  2004/08/12 13:33:34  nw
added framework of classes for testing the solar event science case.
 
*/