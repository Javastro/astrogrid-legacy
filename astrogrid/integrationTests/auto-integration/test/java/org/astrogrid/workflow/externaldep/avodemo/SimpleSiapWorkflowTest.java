/*
 * $Id: SimpleSiapWorkflowTest.java,v 1.1 2004/10/13 12:01:51 pah Exp $
 * 
 * Created on 13-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.workflow.externaldep.avodemo;


import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 13-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class SimpleSiapWorkflowTest extends AbstractTestForWorkflow {

    
    protected final static String SIAP_SERVICE = AUTHORITYID+"/"+AvoDemoTestConstants.CASU_SIAP;
    public static void main(String[] args) {
        junit.textui.TestRunner.run(SimpleSiapWorkflowTest.class);
    }
    
    /*
     * @see AbstractTestForWorkflow#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Constructor for SimpleSiapWorkflowTest.
     * @param arg0
     */
    public SimpleSiapWorkflowTest(String arg0) {
        super(new String[]{SIAP_SERVICE}, arg0);
    }

    /**
     * @param applications
     * @param arg0
     */
    public SimpleSiapWorkflowTest(String[] applications, String arg0) {
        super(applications, arg0);
    }
    protected void buildWorkflow() throws Exception {
      wf.setName(this.getClass().getName());
      org.astrogrid.portal.workflow.intf.ApplicationDescription desc = reg.getDescriptionFor(SIAP_SERVICE);
      Tool siapTool = desc.createToolFromDefaultInterface();
      
      ParameterValue ra = (ParameterValue)siapTool.findXPathValue("input/parameter[name='POS']");
      assertNotNull(ra);
      ra.setValue("244.5,54");
      ra.setIndirect(false);
      
      ParameterValue radius = (ParameterValue)siapTool.findXPathValue("input/parameter[name='SIZE']");
      assertNotNull(radius);
      radius.setValue("0.5,0.5");
      radius.setIndirect(false);
      
      ParameterValue format = (ParameterValue)siapTool.findXPathValue("input/parameter[name='FORMAT']");
      assertNotNull(format);
      format.setValue("image/fits");
      format.setIndirect(false);
      
      Ivorn outputIvorn = createIVORN("/simpleSiapTestoutput.vot");
      ParameterValue outvot = (ParameterValue)siapTool.findXPathValue("output/parameter[name='IMAGES']");
      assertNotNull(outvot);
      outvot.setValue(outputIvorn.toString());
      outvot.setIndirect(true);
      Step step = new Step();
      step.setTool(siapTool);
      step.setResultVar("source");
      wf.getSequence().addActivity(step);
      
    }
}
