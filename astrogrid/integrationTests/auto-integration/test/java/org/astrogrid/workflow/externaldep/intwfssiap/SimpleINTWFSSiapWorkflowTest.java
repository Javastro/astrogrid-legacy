/*$Id: SimpleINTWFSSiapWorkflowTest.java,v 1.2 2004/11/30 15:39:32 clq2 Exp $
 * Created on 24-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.externaldep.intwfssiap;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.io.Piper;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;
import org.astrogrid.workflow.integration.intwfssiap.INTWFSIAPKeys;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/** simple single-stage workflow that tests ability to retreive a votable from the SIAP service.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Nov-2004
 *
 */
public class SimpleINTWFSSiapWorkflowTest extends AbstractTestForWorkflow implements INTWFSIAPKeys {

    /** Construct a new SimpleINTWFSSiapWorkflowTest
     * @param applications
     * @param arg0
     */
    public SimpleINTWFSSiapWorkflowTest(String arg0) {
        super(new String[]{INTWFS_SIAP}, arg0);
    }
    public SimpleINTWFSSiapWorkflowTest(String[] apps,String arg0) {
        super(apps, arg0);
    }
    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws Exception {
        wf.setName(this.getClass().getName());
        Step s  = buildSiapTool(reg);
                        
        // add to the workflow.

        wf.getSequence().addActivity(s);
    }

    /**
     * @param applicationRegistry @todo
     * @return
     * @throws WorkflowInterfaceException
     */
    public static Step buildSiapTool(ApplicationRegistry applicationRegistry) throws WorkflowInterfaceException {
        ApplicationDescription desc = applicationRegistry.getDescriptionFor(INTWFS_SIAP);
        Tool siapTool = desc.createToolFromDefaultInterface();
        // populate tool.
        ParameterValue format = (ParameterValue)siapTool.findXPathValue("input/parameter[name='FORMAT']");
        assertNotNull(format);
        format.setIndirect(false);
        format.setValue("image/fits");

        ParameterValue pos = (ParameterValue)siapTool.findXPathValue("input/parameter[name='POS']");
        assertNotNull(pos);
        pos.setIndirect(false);
        pos.setValue("180,0");
        
        ParameterValue size = (ParameterValue)siapTool.findXPathValue("input/parameter[name='SIZE']");
        assertNotNull(size);
        size.setIndirect(false);
        size.setValue("0.1");        
        
        ParameterValue images = (ParameterValue)siapTool.findXPathValue("output/parameter[name='IMAGES']");
        assertNotNull(images);
        images.setIndirect(false);
        Step s = new Step();
        s.setDescription("SIAP query");
        s.setName(INTWFS_SIAP);
        s.setResultVar("siap");
        s.setTool(siapTool);
        return s;
    }
    public void checkExecutionResults(Workflow result) throws Exception {
        super.checkExecutionResults(result);
        
        Step s = (Step)result.getSequence().getActivity(0);
        assertStepCompleted(s);
        ResultListType r = getResultOfStep(s);
        softAssertEquals("only expected a single result",1,r.getResultCount());
        /** pity - can't do this - votable has xmlns noise on it, which means it's not DTD valid.
        AstrogridAssert.assertVotable(r.getResult(0).getValue());*/
        
       
    }

}


/* 
$Log: SimpleINTWFSSiapWorkflowTest.java,v $
Revision 1.2  2004/11/30 15:39:32  clq2
nww-itn07-684b

Revision 1.1.2.2  2004/11/26 15:46:01  nw
tests for the siap, siap-image-fetch tool, and equivalent script

Revision 1.1.2.1  2004/11/25 00:36:35  nw
tests for external siap tools.
 
*/