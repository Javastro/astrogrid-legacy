/*
 * $Id: TestApplicationControllerRunningHyperZ.java,v 1.5 2004/03/23 12:51:25 pah Exp $
 * 
 * Created on 01-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;


import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.avodemo.AVODemoConstants;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class TestApplicationControllerRunningHyperZ extends BaseApplicationTestCase {

   private final String myspaceBaseRef="/"+AVODemoConstants.ACCOUNT+"/serv1/";

   /**
    * Constructor for CommandLineApplicationControllerTest.
    * @param arg0
    */
   public TestApplicationControllerRunningHyperZ(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(TestApplicationControllerRunningHyperZ.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      applicationid = "HyperZ";
   }
   /** 
    * @see org.astrogrid.applications.manager.BaseApplicationTestCase#setupTool()
    */
   protected void setupTool() {
      Output output = new Output();
      Input input = new Input();
      thisTool = new Tool();
      thisTool.setName(applicationid);
      thisTool.setInterface("simple");
      ParameterValue param = new ParameterValue();
      param.setName("config_file");
      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setValue("/home/applications/demo/hyperz/zphot.param");
      input.addParameter(param);
      
      param = new ParameterValue();
            param.setName("input_catalog");
            param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
            param.setValue(myspaceBaseRef+"merged");
            input.addParameter(param);
            
      param = new ParameterValue();
            param.setName("output_catalog");
            param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
            param.setValue(myspaceBaseRef+"hyperzout");
            output.addParameter(param);     

      thisTool.setInput(input);
      thisTool.setOutput(output);
   }
   

}
