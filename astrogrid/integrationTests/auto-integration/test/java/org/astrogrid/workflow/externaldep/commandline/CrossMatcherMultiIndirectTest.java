/*
 * $Id: CrossMatcherMultiIndirectTest.java,v 1.1 2004/10/06 13:33:31 pah Exp $
 * 
 * Created on 01-Oct-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.workflow.externaldep.commandline;

import org.astrogrid.applications.avodemo.AVODemoConstants;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 01-Oct-2004
 * @version $Name:  $
 * @since iteration6
 */
public class CrossMatcherMultiIndirectTest extends
      AbstractMultiSourceIndirectParameterWorkflow {
   
   static final String APP=AUTHORITYID+"/"+CommandLineInfo.DFT;

   /**
    * 
    */
   public CrossMatcherMultiIndirectTest(String arg0) {

     super(new String[]{APP}, arg0);
   }
   /**
    * @param applications
    * @param arg0
    */
   public CrossMatcherMultiIndirectTest(String[] applications, String arg0) {
      super(applications, arg0);
      // TODO Auto-generated constructor stub
   }

   /* (non-Javadoc)
    * @see org.astrogrid.workflow.externaldep.commandline.AbstractMultiSourceIndirectParameterWorkflow#buildStep(java.lang.String)
    */
   protected Step buildStep(String location, String resultLocation) {
      Step step = new Step();
      Output output = new Output();
      Input input = new Input();
      String applicationid = APP;

      Tool tool = new Tool();
      tool.setName(applicationid);
      tool.setInput(input);
      tool.setOutput(output);
      tool.setInterface("simple");
      ParameterValue param = new ParameterValue();
      input.addParameter(param);
      param.setName("targets");

      param.setValue(location + "sexout_z.vot");
      param.setIndirect(true);

      param = new ParameterValue();
      param.setName("matches");
      input.addParameter(param);
      param.setValue(location + "sexout_b.vot");
      param.setIndirect(true);

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("matches");

      param.setIndirect(true);
      param.setValue(location + "sexout_v.vot");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("matches");

      param.setIndirect(true);
      param.setValue(location + "sexout_i.vot");

      param = new ParameterValue();
      param.setName("merged_output");
      output.addParameter(param);
      param.setIndirect(true);
      param.setValue(resultLocation); 
      step.setTool(tool);
      step.setName("dft");
      return step;
  }

}
