/*$Id: CommandLineApplicationTest.java,v 1.11 2005/07/05 08:26:56 clq2 Exp $
 * Created on 27-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.applications.commandline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.MockMonitor;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.community.User;
import org.astrogrid.io.Piper;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * end-to-end test really. creates a micro-cea server, and then runs a test
 * application, and inspects the results.
 * <p>The tests rely heavily on the testapp.sh script and its behaviour. The intention is to test out all the combinations of direct and indirect paramaeters with fileref and non-fileref command-line parameters.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 27-May-2004
 * @author pah
 *  
 */
public class CommandLineApplicationTest extends AbstractCmdLineAppTestCase {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(CommandLineApplicationTest.class);

   /**
     * Constructor for CmdLineApplicationTest.
     * 
     * @param arg0
     */
    public CommandLineApplicationTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();        

    }
    /** calls application, with direct parameter values */
    public void testCreateApplicationDirect() throws Exception {
      if (logger.isDebugEnabled()) {
         logger.debug("testCreateApplicationDirect() - start");
      }
       
        Tool t = buildTool("0");

        Toolbuilder.fillDirect(t);
        ResultListType results = execute(t);
        for (int i = 0; i < results.getResultCount(); i++) {
            ParameterValue result = results.getResult(i);
            assertNotNull(result);
            System.out.println("result par name = " + result.getName());

            if (result.getName().equals("P3")) {
                assertTrue("P3 does not contain expected string", result.getValue().indexOf(Toolbuilder.TEST_DATA) != -1);
            }
            else if (result.getName().equals("P2")) {
               assertTrue("P2 does not contain expected string", result.getValue().indexOf(Toolbuilder.PAR4_DATA) != -1);
               
           }
            else if (result.getName().equals("P14")) {
               assertTrue("P14 does not contain expected string", result.getValue().indexOf(Toolbuilder.LOCALFILE_DATA) != -1);
               
           }
            
            else {
                fail("unknown result pararmeter  " + result.getName()
                        + " returned");
            }
        }

      if (logger.isDebugEnabled()) {
         logger.debug("testCreateApplicationDirect() - end");
      }
    }

    /** calls application, with indirect parameter values */
    public void testCreateApplicationIndirect() throws Exception {
      if (logger.isDebugEnabled()) {
         logger.debug("testCreateApplicationIndirect() - start");
      }

        Tool t = buildTool("0");

        File inputFile1 = File.createTempFile(
                "CommandLineApplicationTest-Input-ignored", null);
        File inputFile = File.createTempFile(
                "CommandLineApplicationTest-Input", null);
        File outputFile = File.createTempFile(
                "CommandLineApplicationTest-Output", null);
        File parameterEchoFile = File.createTempFile(
                "CommandLineApplicationTest-paramecho", null);
        File par4file = File.createTempFile("CommandLineApplicationTest-p4",
              null);
      
        File par14file = File.createTempFile("CommandLineApplicationTest-p14",
              null);
      

        PrintWriter pw = new PrintWriter(new FileWriter(inputFile));
        pw.println(Toolbuilder.TEST_DATA);
        pw.close();

        pw = new PrintWriter(new FileWriter(inputFile1));
        pw.println("any file contents"); // will be ignored
        pw.close();

        ParameterValue inFile = new ParameterValue();
        inFile.setName("P9");
        inFile.setValue(inputFile.toURI().toString());
        inFile.setIndirect(true);
        ParameterValue inFile1 = new ParameterValue();
        inFile1.setName("P9");
        inFile1.setValue(inputFile1.toURI().toString());
        inFile1.setIndirect(true);

        ParameterValue out = new ParameterValue();
        out.setName("P3");
        out.setIndirect(true);
        out.setValue(outputFile.toURI().toString());

        ParameterValue echo = new ParameterValue();
        echo.setName("P2");
        echo.setIndirect(true);
        echo.setValue(parameterEchoFile.toURI().toString());

        pw = new PrintWriter(new FileWriter(par4file));
        pw.println(Toolbuilder.PAR4_DATA);
        pw.close();
        ParameterValue p4 = new ParameterValue();
        p4.setName("P4");
        p4.setIndirect(true);
        p4.setValue(par4file.toURI().toString());
        
        ParameterValue p14 = new ParameterValue();
        p14.setName("P14");
        p14.setIndirect(true);
        p14.setValue(par14file.toURI().toString());

        t.getInput().addParameter(inFile1);
        t.getInput().addParameter(inFile);
        t.getOutput().addParameter(out);
        t.getInput().addParameter(p4);
        t.getOutput().addParameter(echo);
        t.getOutput().addParameter(p14);
        ResultListType results = execute(t);
        for (int i = 0; i < results.getResultCount(); i++) {
            ParameterValue result = results.getResult(i);
            assertNotNull(result);
            System.out.println("result par name = " + result.getName());

            if (result.getName().equals("P3")) {
                assertEquals("P3 results not in expected place",
                        result.getValue(), outputFile.toURI().toString());
                checkContent(outputFile, Toolbuilder.TEST_DATA);
            }
            else if (result.getName().equals("P2")) {
               assertEquals("P2 results not in expected place",
                       result.getValue(), parameterEchoFile.toURI().toString());
               checkContent(parameterEchoFile, Toolbuilder.PAR4_DATA);
            }
               else if (result.getName().equals("P14")) {
                  assertEquals("P14 results not in expected place",
                          result.getValue(), par14file.toURI().toString());
                  checkContent(par14file, Toolbuilder.LOCALFILE_DATA);

            }
            else {
                fail("unknown result pararmeter  " + result.getName()
                        + " returned");
            }
        }

      if (logger.isDebugEnabled()) {
         logger.debug("testCreateApplicationIndirect() - end");
      }
    }

    /**
     * @param outputFile
     * @param testContent
     *            TODO
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void checkContent(File outputFile, String testContent)
            throws IOException, FileNotFoundException {
        StringWriter contents = new StringWriter();
        Piper.pipe(new FileReader(outputFile), contents);
        System.out.println(contents.toString());
        assertTrue("result dones't contain expected data", contents.toString()
                .indexOf(testContent) != -1);
    }

    public void testPickupMissingParameters() {
      if (logger.isDebugEnabled()) {
         logger.debug("testPickupMissingParameters() - start");
      }

        Tool t = null;
        try {
            t = buildTool("0");
        }
        catch (Exception e) {
         logger.error("testPickupMissingParameters()", e);

            // not really trying to test this
            e.printStackTrace();
        }
        try {
        Application app = testAppDescr.initializeApplication("testExecution",
                new User(), t);
        assertNotNull(app);
        assertTrue(app instanceof CommandLineApplication);
        // and now run it.
        MockMonitor monitor = new MockMonitor();
//        app.addObserver(controller);
        app.addObserver(monitor);
        app.execute();
        monitor.waitFor(WAIT_SECONDS);
        assertNotNull(monitor.sawApp);
        assertTrue(monitor.sawError);
        // check it completed, not in error, etc.
        assertEquals("application should have ended with error status",
                Status.ERROR, app.getStatus());

        // ok, either timed out, or the application finished..
        // check behaviour of monitor.
 
        
        
        
             
        }
        catch (Exception e1) {
         logger.error("testPickupMissingParameters()", e1);

                fail("unexpected exception " + e1);
            }
        
      if (logger.isDebugEnabled()) {
         logger.debug("testPickupMissingParameters() - end");
      }
    }

   /**
    * N.B. will see writeback errors for the output parameters in stderr when running this test - this is normal.
    */
   public void testSeeError() {
           Tool t = null;
           try {
               t = buildTool("-1");
               Toolbuilder.fillDirect(t);
           }
           catch (Exception e) {
               // not really trying to test this
               e.printStackTrace();
           }
           try {
           Application app = testAppDescr.initializeApplication("testExecution",
                   new User(), t);
           assertNotNull(app);
           assertTrue(app instanceof CommandLineApplication);
           // and now run it.
           MockMonitor monitor = new MockMonitor();
   //        app.addObserver(controller);
           app.addObserver(monitor);
           app.execute();
           monitor.waitFor(WAIT_SECONDS);
           assertNotNull(monitor.sawApp);
           assertTrue(monitor.sawError);
           // check it failed error, etc.
           assertEquals("should have seen 3 warnings before final error status",3, monitor.nwarn);
           assertEquals("application should have ended with error status",
                   Status.ERROR, app.getStatus());
   
           // ok, either timed out, or the application finished..
           // check behaviour of monitor.
    
           
           
           
                
           }
           catch (Exception e1) {
                   fail("unexpected exception " + e1);
               }
           
       }

    private ResultListType execute(Tool t) throws Exception, CeaException,
            InterruptedException {

        // now create an application based on these details
        Application app = testAppDescr.initializeApplication("testExecution",
                new User(), t);
        
        assertNotNull(app);
        assertTrue(app instanceof CommandLineApplication);
        // and now run it.
        MockMonitor monitor = new MockMonitor();
//        app.addObserver(controller);
        app.addObserver(monitor);
        app.execute();
        monitor.waitFor(WAIT_SECONDS);

        // ok, either timed out, or the application finished..
        // check behaviour of monitor.
        assertTrue("application did not finish",monitor.sawExit);
        assertNotNull(monitor.sawApp);
        // check it completed, not in error, etc.
        assertEquals("application did not complete correctly",
                Status.COMPLETED, app.getStatus());
        // check results.
        ResultListType results = app.getResult();
        assertEquals(3, results.getResultCount());
        // NB cannot make assumption about result order.....
        System.out.println("results returned in order="
                + results.getResult(0).getName() + ","
                + results.getResult(1).getName()+ results.getResult(2).getName());

        return results;
    }
    
    public void testGetEnvironment()
    {
       
    }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.AbstractCmdLineAppTestCase#buildTool(java.lang.String)
    */
   protected Tool buildTool(String delay) throws Exception {
      return Toolbuilder.buildTool(delay, testAppDescr);
      }
}

/*
 * $Log: CommandLineApplicationTest.java,v $
 * Revision 1.11  2005/07/05 08:26:56  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.10.66.2  2005/06/09 22:17:58  pah
 * tweaking the log getter
 *
 * Revision 1.10.66.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.10.52.3  2005/06/08 22:10:45  pah
 * make http applications v10 compliant
 *
 * Revision 1.10.52.2  2005/06/03 16:01:48  pah
 * first try at getting commandline execution log bz#1058
 *
 * Revision 1.10.52.1  2005/05/31 12:58:26  pah
 * moved to v10 schema interpretation - this means that the authorityID is read directly with the applicaion "name"
 *
 * Revision 1.10  2004/11/27 13:20:02  pah
 * result of merge of pah_cea_bz561 branch
 *
 * Revision 1.9.6.2  2004/11/15 16:53:56  pah
 * enable pickup of locally generated file name
 *
 * Revision 1.9.6.1  2004/10/27 16:04:05  pah
 * pulled up an AbstractParameterAdapter
 *
 * Revision 1.9  2004/09/30 15:10:00  pah
 * try to test for failure a bit better
 *
 * Revision 1.8  2004/09/23 22:44:23  pah
 * need new CommandLineParameterAdapter to separate out the switch adding part so that patricios merge tool can be accomodated
 *
 * Revision 1.7  2004/09/15 11:37:00  pah
 * make the commandline appliction do all the parameter fetching int he main execution thread
 *
 * Revision 1.6  2004/09/10 21:29:00  pah
 * add the controller as an observer
 *
 * Revision 1.5  2004/09/10 12:13:09  pah
 * allow for p9 to be repeatable
 *
 * Revision 1.4  2004/08/28 07:17:34  pah
 * commandline parameter passing - unit tests ok
 * Revision 1.3 2004/07/26 12:03:33 nw
 * updated to match name changes in cea server library
 * 
 * Revision 1.2 2004/07/01 11:07:59 nw merged in branch
 * nww-itn06-componentization
 * 
 * Revision 1.1.2.3 2004/07/01 01:43:39 nw final version, before merge
 * 
 * Revision 1.1.2.2 2004/06/17 09:24:18 nw intermediate version
 * 
 * Revision 1.1.2.1 2004/06/14 08:57:48 nw factored applications into
 * sub-projects, got packaging of wars to work again
 * 
 * Revision 1.1.22.1 2004/05/28 10:23:11 nw checked in early, broken version -
 * but it builds and tests (fail)
 *  
 */