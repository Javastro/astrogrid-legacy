/*
 * $Id: Toolbuilder.java,v 1.6 2009/02/26 12:47:04 pah Exp $
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.astrogrid.applications.description.AbstractApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.execution.ListOfParameterValues;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;

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
        p4.setId("P4");
        p4.setValue(PAR4_DATA);
        p4.setIndirect(false);
   
        ParameterValue inFile1 = new ParameterValue();
        inFile1.setId("P9");
        inFile1.setValue("any file contents"); // we expect the first p9 to be ignored by the testapp
        inFile1.setIndirect(false);
        
        ParameterValue inFile = new ParameterValue();
        inFile.setId("P9");
        inFile.setValue(TEST_DATA);
        inFile.setIndirect(false);
        ParameterValue p10 = new ParameterValue();
        p10.setId("P10");
        p10.setValue("on");
        p10.setIndirect(false);
   
        ParameterValue out = new ParameterValue();
        out.setId("P3");
        out.setIndirect(false);
   
        ParameterValue echo = new ParameterValue();
        echo.setId("P2");
        echo.setIndirect(false);
        ParameterValue localfileout = new ParameterValue();
        localfileout.setId("P14");
        localfileout.setIndirect(false);
   
        t.getInput().addParameter(inFile1);
        t.getInput().addParameter(inFile);
        t.getInput().addParameter(p4);
        t.getInput().addParameter(p10);
        t.getOutput().addParameter(out);
        t.getOutput().addParameter(echo);
        t.getOutput().addParameter(localfileout);
   }

   public static Tool buildTool(String delay, AbstractApplicationDescription testAppDescr) throws Exception {
        // select the interface we're going to use.
        ApplicationInterface interf = testAppDescr.getInterface("I1");
        assert interf != null : "interface I1 not found for test application";
        // from this 'meta data' populat a tool..
        Tool t = new Tool();
        t.setId(TestAppConst.TESTAPP_NAME);
        t.setInterface(interf.getId());
        ListOfParameterValues input = new ListOfParameterValues();
       
        ParameterValue time = new ParameterValue();
        time.setId("P1");
        time.setValue(delay);//no delay
	input.addParameter(time);
        t.setInput(input);
        
        ListOfParameterValues output = new ListOfParameterValues();
	t.setOutput(output);
        return t;
    }

   /**
    * @param testAppDescr
 * @throws URISyntaxException 
 * @throws IOException 
    */
   public static void fixupExecutionPath( CommandLineApplicationDescription testAppDescr) throws URISyntaxException, IOException {
      // will only work with unjarred-classes - but this is always the case in
      // development.
      //FIXME - make this work in jarred classes by copying the resource out of the jar....
         URI uri = new URI(Toolbuilder.class.getResource("/app/testapp.sh")
                 .toString());
         
         
         
         File appPath = (new File(uri)).getParentFile();
 
         System.out.println("TESTAPPDIR := " + appPath.getAbsolutePath());
         assert appPath.exists() : "application directory does not exist";
         assert appPath.isDirectory() : "application path is not a directory";
         
         testAppDescr.setExecutionPath(testAppDescr.getExecutionPath().replaceAll(
                 "@TOOLBASEDIR@", appPath.getAbsolutePath()));
         
         //also fix file permissions - maven removes executable bits - this will only work on unix
         
         ProcessBuilder pb = new ProcessBuilder("/bin/chmod","-f","+x", (new File(appPath,"testapp.sh")).getAbsolutePath());
         pb.redirectErrorStream(true);
         Process process = pb.start();
        
    
   }

  
}


/*
 * $Log: Toolbuilder.java,v $
 * Revision 1.6  2009/02/26 12:47:04  pah
 * separate more out into cea-common for both client and server
 *
 * Revision 1.5  2008/09/15 19:27:22  pah
 * comment on need to make this work in jar
 *
 * Revision 1.4  2008/09/13 09:51:04  pah
 * code cleanup
 *
 * Revision 1.3  2008/09/10 23:27:18  pah
 * moved all of http CEC and most of javaclass CEC code here into common library
 *
 * Revision 1.2  2008/09/03 12:22:54  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1  2008/08/29 07:28:27  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.2.100.5  2008/06/10 20:10:49  pah
 * moved ParameterValue and friends to CEATypes.xsd
 *
 * Revision 1.2.100.4  2008/05/01 15:35:55  pah
 * incorporated chiba xforms
 *
 * Revision 1.2.100.3  2008/04/17 16:16:55  pah
 * removed all castor marshalling - even in the web service layer - unit tests passing
 * some uws functionality present - just the bare bones.
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 * ASSIGNED - bug 2739: remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 *
 * Revision 1.2.100.2  2008/03/28 16:44:35  pah
 * RESOLVED - bug 2683: treatment of boolean values
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2683
 *
 * Revision 1.2.100.1  2008/03/19 23:15:42  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
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
