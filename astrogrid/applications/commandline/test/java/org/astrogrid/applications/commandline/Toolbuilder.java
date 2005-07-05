/*
 * $Id: Toolbuilder.java,v 1.2 2005/07/05 08:26:56 clq2 Exp $
 * 
 * Created on 03-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pharriso@eso.org) 03-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public abstract class Toolbuilder  {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory.getLog(Toolbuilder.class);

 
   protected static final String TEST_DATA = "test input data";
   protected static final String PAR4_DATA = "any old rubbish";
   protected static final String LOCALFILE_DATA = "some local test content";

   protected void setUp() throws Exception {
         
   
    }

   /**
    * @param t
    * @throws IndexOutOfBoundsException
    */
   public static void fillDirect(Tool t) throws IndexOutOfBoundsException {
      ParameterValue p4 = new ParameterValue();
        p4.setName("P4");
        p4.setValue(PAR4_DATA);
        p4.setIndirect(false);
   
        ParameterValue inFile1 = new ParameterValue();
        inFile1.setName("P9");
        inFile1.setValue("any file contents"); // we expect the first p9 to be ignored by the testapp
        inFile1.setIndirect(false);
        
        ParameterValue inFile = new ParameterValue();
        inFile.setName("P9");
        inFile.setValue(TEST_DATA);
        inFile.setIndirect(false);
   
        ParameterValue out = new ParameterValue();
        out.setName("P3");
        out.setIndirect(false);
   
        ParameterValue echo = new ParameterValue();
        echo.setName("P2");
        echo.setIndirect(false);
        ParameterValue localfileout = new ParameterValue();
        localfileout.setName("P14");
        localfileout.setIndirect(false);
   
        t.getInput().addParameter(inFile1);
        t.getInput().addParameter(inFile);
        t.getInput().addParameter(p4);
        t.getOutput().addParameter(out);
        t.getOutput().addParameter(echo);
        t.getOutput().addParameter(localfileout);
   }

   public static Tool buildTool(String delay, CommandLineApplicationDescription testAppDescr) throws Exception {
        // select the interface we're going to use.
        ApplicationInterface interf = testAppDescr.getInterface("I1");
        assert interf != null : "interface I1 not found for test application";
        // from this 'meta data' populat a tool..
        Tool t = new Tool();
        t.setName(TestAppConst.TESTAPP_NAME);
        t.setInterface(interf.getName());
        Input input = new Input();
        ParameterValue time = new ParameterValue();
        time.setName("P1");
        time.setValue(delay);//no delay
        input.addParameter(time);
         t.setInput(input);
        Output output = new Output();
        t.setOutput(output);
        return t;
    }

   /**
    * @param testAppDescr
    */
   public static void fixupExecutionPath( CommandLineApplicationDescription testAppDescr) {
      // will only work with unjarred-classes - but this is always the case in
      // development.
      try {
         URI uri = new URI(Toolbuilder.class.getResource("/app/testapp.sh")
                 .toString());
         File appPath = (new File(uri)).getParentFile();
 
         System.out.println("TESTAPPDIR := " + appPath.getAbsolutePath());
         assert appPath.exists() : "application directory does not exist";
         assert appPath.isDirectory() : "application path is not a directory";
         testAppDescr.setExecutionPath(testAppDescr.getExecutionPath().replaceAll(
                 "@TOOLBASEDIR@", appPath.getAbsolutePath()));
      } catch (URISyntaxException e) {
        
         logger.error("problem trying to set real execution path", e);
      }
    
   }

  
}


/*
 * $Log: Toolbuilder.java,v $
 * Revision 1.2  2005/07/05 08:26:56  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.2  2005/06/08 22:10:45  pah
 * make http applications v10 compliant
 *
 * Revision 1.1.2.1  2005/06/03 16:01:48  pah
 * first try at getting commandline execution log bz#1058
 *
 */
