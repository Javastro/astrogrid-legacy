/*
 * $Id: TestApplicationControllerRunningSExtractor.java,v 1.5 2004/03/23 12:51:25 pah Exp $
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


import org.astrogrid.applications.ParameterValues;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class TestApplicationControllerRunningSExtractor extends BaseApplicationTestCase {

   /**
    * Constructor for CommandLineApplicationControllerTest.
    * @param arg0
    */
   public TestApplicationControllerRunningSExtractor(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(TestApplicationControllerRunningSExtractor.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      applicationid = "SExtractor";
   }
   
   final public void testAll()
   {
      initApp("b");
      initApp("v");
      initApp("i");
      initApp("z");
      
   }

   private void initApp(String band) {
      Output output = new Output();
      Input input = new Input();
      thisTool = new Tool();
      thisTool.setName(applicationid);
      thisTool.setInterface("simple");
      ParameterValue param = new ParameterValue();
      //FIXME this needs to be implemented
      String paramstr=
      "<tool><input><parameter name='DetectionImage'>/home/applications/data/GOODS/h_nz_sect23_v1.0_drz_img.fits</parameter>"
      +"<parameter name='PhotoImage'>/home/applications/data/GOODS/h_n"+band+"_sect23_v1.0_drz_img.fits</parameter>"
      +"<parameter name='config_file'>/home/applications/demo/h_goods_n"+band+"_r1.0z_phot_sex.txt</parameter><parameter name='PARAMETERS_NAME'>/home/applications/demo/std.param</parameter></input>"       +"<output><parameter name='CATALOG_NAME'>out1file_"+band+"</parameter></output></tool>";
   }
   
   

   /** 
    * @see org.astrogrid.applications.manager.BaseApplicationTestCase#setupTool()
    */
   protected void setupTool() {
      //REFACTORME - need to parameterize the tool setup
      initApp("b");
   }

}
