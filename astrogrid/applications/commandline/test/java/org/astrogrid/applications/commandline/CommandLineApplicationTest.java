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
 * <p>The tests rely heavily on the testapp.sh script and its behaviour. 
 * The intention is to test out all the combinations of direct and indirect 
 * paramaeters with fileref and non-fileref command-line parameters.
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
        this.execute(app);
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
    * Tests an application that always returns bad status.
    * The application has some output parameters. The monitor should see
    * the execution failure but should not see warnings about the output
    * parameters: see BZ2077.
    */
   public void testSeeError() throws Exception {
           Tool t = null;
           try {
               t = buildTool("-1");
               Toolbuilder.fillDirect(t);
           }
           catch (Exception e) {
               // not really trying to test this
               e.printStackTrace();
           }
           Application app = testAppDescr.initializeApplication("testExecution",
                   new User(), t);
           assertNotNull(app);
           assertTrue(app instanceof CommandLineApplication);
           
           app.setRunTimeLimit(20000);
           
           // and now run it.
           MockMonitor monitor = new MockMonitor();
           app.addObserver(monitor);
           this.execute(app);
           monitor.waitFor(WAIT_SECONDS);
           assertNotNull(monitor.sawApp);
           assertTrue(monitor.sawError);
           assertEquals("Saw no (parameter) warnings",0, monitor.nwarn);
           assertEquals("application should have ended with error status",
                        Status.ERROR, app.getStatus());
           ResultListType resultsList = app.getResult();
           ParameterValue[] results = resultsList.getResult();
           int nonNullValueCount = 0;
           boolean sawCeaErrorResult = false;
           for (int i = 0; i < results.length; i++) {
             System.out.println(results[i].getName() + ":" + results[i].getValue());
             if (results[i].getValue() != null) {
               nonNullValueCount++;
             }
             if (results[i].getName().equals("cea-error")) {
               sawCeaErrorResult = true;
             }
           }
           assertTrue("Exactly one result has a value", nonNullValueCount == 1);
           assertTrue("cea-error is present", sawCeaErrorResult);
       }

    private ResultListType execute(Tool t) throws Exception, CeaException,
            InterruptedException {

        // now create an application based on these details
        CommandLineApplication app = (CommandLineApplication) testAppDescr.initializeApplication("testExecution",
                new User(), t);
        
        assertNotNull(app);
        assertTrue(app instanceof CommandLineApplication);
        
        app.setRunTimeLimit(20000);
        
        // and now run it.
        MockMonitor monitor = new MockMonitor();
//        app.addObserver(controller);
        app.addObserver(monitor);
        this.execute(app);
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
   
   
  /**
   * Starts the execution of a given Application.
   * This is a convenience method to hide the thread handling.
   *
   * @param app The Application to be executed.
   * @throws CeaException if the Application fails to provide a Runnable task.
   */
  private void execute(Application app) throws CeaException {
    Runnable r = app.createExecutionTask();
    assertNotNull(r);
    Thread t = new Thread(r);
    t.start();
  }

}
