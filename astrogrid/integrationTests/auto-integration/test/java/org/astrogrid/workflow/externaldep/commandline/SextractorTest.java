/*
 * $Id: SextractorTest.java,v 1.1 2004/10/06 13:33:31 pah Exp $
 * 
 * Created on 29-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.workflow.externaldep.commandline;


import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 29-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class SextractorTest extends AbstractTestForWorkflow {

   private static final String APPNAME = COMMUNITY+"/SExtractor"; 
   /**
    *
    * @param arg0
    */
   public SextractorTest( String arg0) {
      super(new String[]{APPNAME}, arg0);
      
   }

   /* (non-Javadoc)
    * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
    */
   protected void buildWorkflow() throws Exception {
      wf.setName(this.getClass().getName());
      ApplicationDescription desc = reg.getDescriptionFor(APPNAME);
      Tool tool = new Tool();
      Output output = new Output();
      Input input = new Input();
      tool.setName(APPNAME);
      tool.setInput(input);
      tool.setOutput(output);
      tool.setInterface("Simple");
      ParameterValue param = new ParameterValue();
      input.addParameter(param);
      param.setName("DetectionImage");

      String hemi="s";
      String sector="sect23";
      String band="z";
      Ivorn outivorn = createIVORN("SextractorTest");
      
      param.setIndirect(true);
      param.setValue(
         "file:///home/applications/data/GOODS/h_"
            + hemi
            + "z_"
            + sector
            + "_v1.0_drz_img.fits");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("PhotoImage");

      param.setIndirect(true);
      param.setValue(
         "file:///home/applications/data/GOODS/h_"
            + hemi
            + band
            + "_"
            + sector
            + "_v1.0_drz_img.fits");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("config_file");

      param.setIndirect(true);
      param.setValue(
         "file:///home/applications/demo/h_goods_" + hemi + band + "_r1.0z_phot_sex.txt");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("PARAMETERS_NAME");

      param.setIndirect(true);
      param.setValue("file:///home/applications/demo/std.param");

      param = new ParameterValue();
      output.addParameter(param);
      param.setName("CATALOG_NAME");

      param.setIndirect(true);
      String myspaceBaseRef = outivorn.toString();
      param.setValue(myspaceBaseRef + "sexout_" + band);
      
      param = new ParameterValue();
      input.addParameter(param);
      param.setName("FILTER_NAME");
      param.setValue("file:///home/applications/demo/h_goods_r1.0z_detect_conv.txt");
      param.setIndirect(true);

      
      
     }

}
