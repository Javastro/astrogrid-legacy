/*
 * $Id: SiapPhotoZTest.java,v 1.2 2004/11/09 14:11:18 pah Exp $
 * 
 * Created on 15-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.workflow.externaldep.avodemo;

import org.astrogrid.applications.avodemo.AVODemoConstants;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * A workflow test that does a photoZ flow on a set of images obtained by
 * 
 * @author Paul Harrison (pah@jb.man.ac.uk) 15-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class SiapPhotoZTest extends SimpleSiapWorkflowTest {

   private static final String SEXOUTFILEVARNAME = "sexoutname";
   protected final static String SExtractor_SERVICE = AUTHORITYID + "/"
         + AvoDemoTestConstants.SEXTRACTOR;

   /**
    * @param arg0
    */
   public SiapPhotoZTest(String arg0) {
      this(new String[] { SExtractor_SERVICE, SIAP_SERVICE }, arg0);
   }

   /**
    * @param applications
    * @param arg0
    */
   public SiapPhotoZTest(String[] applications, String arg0) {
      super(applications, arg0);
   }

   protected void buildWorkflow() throws Exception {
      super.buildWorkflow(); // use the siap step from there
//    add the variable set for the output file name at the top of the workflow.
      Set setvar = new Set();
      setvar.setVar(SEXOUTFILEVARNAME);
      Ivorn sexoutfilestub = createIVORN("/intwfsexout");
      setvar.setValue(sexoutfilestub.toString());
      wf.getSequence().addActivity(0, setvar); 
      //add a script to parse the urls out of the votable
      Script sc = new Script();
      sc
            .setBody("votable = source.Result; // access result of previous step\n"
                  + "parser = new XmlParser(); //create new parser \n"
                  + "nodes = parser.parseText(votable); //parse votable into node tree\n"
                  + "urls = nodes.depthFirst().findAll{it.name() == 'STREAM'}.collect{it.value()}.flatten(); // filter node tree on 'STREAM', project value\n"
                  + "print(urls); // show what we've got\n");
      wf.getSequence().addActivity(sc);
      For forstep = new For();
      forstep.setItems("${urls}");
      forstep.setVar("theURL");
      sc = new Script();
      sc.setBody("jes.info('calling sextractor for ${theURL}')\n" +
      		"n++");
      Sequence seq = new Sequence();
      seq.addActivity(sc);

      Step sextractorStep = CreateSextractorStep();
      seq.addActivity(sextractorStep);
      //
      forstep.setActivity(seq);
      wf.getSequence().addActivity(forstep);
      
   }

   protected void setUp() throws Exception {
      super.setUp();
   }

   private Step CreateSextractorStep() {
      Step step = new Step();
      Output output = new Output();
      Input input = new Input();
      String applicationid = SExtractor_SERVICE;
      Tool tool = new Tool();
      tool.setName(applicationid);
      tool.setInput(input);
      tool.setOutput(output);
      tool.setInterface("Simple");
      ParameterValue param = new ParameterValue();
      input.addParameter(param);
      param.setName("DetectionImage");

      param.setIndirect(true);
      param.setValue("${theURL}");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("config_file");

      param.setIndirect(true);
      param.setValue("file:///home/applications/demo/h_goods_sz_r1.0z_phot_sex.txt");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("PARAMETERS_NAME");

      param.setIndirect(true);
      param.setValue("file:///home/applications/demo/std.param");

      param = new ParameterValue();
      output.addParameter(param);
      param.setName("CATALOG_NAME");
      param.setValue("${"+SEXOUTFILEVARNAME+"}${n}");

      param.setIndirect(false);
      assertTrue("tool is not valid", tool.isValid());
      step.setTool(tool);
      step.setName("sextractor");
      assertTrue("sextractor step is not valid",step.isValid());

      return step;
   }
}

