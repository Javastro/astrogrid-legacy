/*$Id: CommandLineApplicationTest.java,v 1.2 2004/07/01 11:07:59 nw Exp $
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

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.MockMonitor;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.commandline.digester.CommandLineApplicationDescriptionFactory;
import org.astrogrid.applications.commandline.digester.CommandLineDescriptionsLoader;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.indirect.DefaultIndirectionProtocolLibrary;
import org.astrogrid.applications.parameter.indirect.FileProtocol;
import org.astrogrid.community.User;
import org.astrogrid.io.Piper;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import org.picocontainer.PicoException;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;

/** end-to-end test really. creates a micro-cea server, and then runs a test application, and inspects the results
 * @author Noel Winstanley nw@jb.man.ac.uk 27-May-2004
 *
 */
public class CommandLineApplicationTest extends DescriptionBaseTestCase {
    private static final String TEST_DATA = "test input data";
    /**
     * Constructor for CmdLineApplicationTest.
     * @param arg0
     */
    public CommandLineApplicationTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        final File workingDir = File.createTempFile("CommandLineApplicationTest",null);
        workingDir.delete();
        workingDir.mkdir();
        assertTrue(workingDir.exists());
        workingDir.deleteOnExit();
        DefaultPicoContainer container = new DefaultPicoContainer();
        container.registerComponent(
          new ConstructorInjectionComponentAdapter(CommandLineApplicationDescription.class,CommandLineApplicationDescription.class)
        );
        container.registerComponent(
            new ConstructorInjectionComponentAdapter(CommandLineApplicationEnvironment.class,CommandLineApplicationEnvironment.class));
        container.registerComponentImplementation(InMemoryIdGen.class);
        container.registerComponentInstance(new CommandLineApplicationEnvironment.WorkingDir() {

            public File getDir() {
                return workingDir;
            }
        });
        DefaultIndirectionProtocolLibrary lib = new DefaultIndirectionProtocolLibrary();
        lib.addProtocol(new FileProtocol());
        container.registerComponentInstance(lib);
        container.registerComponentImplementation(ApplicationDescriptionEnvironment.class);
        CommandLineApplicationDescriptionFactory descFactory = new CommandLineApplicationDescriptionFactory(container);
        try {
            container.verify();
        } catch (PicoException t) {
            t.printStackTrace();
            fail("Container misconfigured");
        }
        CommandLineDescriptionsLoader dl = new CommandLineDescriptionsLoader(new CommandLineDescriptionsLoader.DescriptionURL() {
    
          public URL getURL() {
              return inputFile;
          }
        },descFactory);
        assertNotNull("cannot create the DescriptionLoader", dl); 
        descs = dl;
        testAppDescr = descs.getDescription("testap2");
        assertNotNull(testAppDescr);
        // now fix the execution path for this app description.
        assertTrue(testAppDescr instanceof CommandLineApplicationDescription);
        CommandLineApplicationDescription cAppDescr = (CommandLineApplicationDescription) testAppDescr;
        // will only work with unjarred-classes - but this is always the case in development.
        URI uri = new URI(this.getClass().getResource("/app/testapp.sh").toString());
        File appPath = (new File(uri)).getParentFile();

        System.out.println("TESTAPPDIR := " + appPath.getAbsolutePath());        
        assertTrue(appPath.exists());
        assertTrue(appPath.isDirectory());
        cAppDescr.setExecutionPath(cAppDescr.getExecutionPath().replaceAll("@TESTAPPDIR@",appPath.getAbsolutePath()));
        }
    

    protected BaseApplicationDescriptionLibrary descs;
    protected ApplicationDescription testAppDescr;
    
    /** calls application, with direct parameter values */
    public void testCreateApplicationDirect() throws Exception {
        Tool t = buildTool();
        
        ParameterValue inFile = new ParameterValue();
        inFile.setName("P9");
        inFile.setValue(TEST_DATA);
        inFile.setIndirect(false);
       
        ParameterValue out = new ParameterValue();
        out.setName("P3");
        out.setIndirect(false);
        
        t.getInput().addParameter(inFile);
        t.getOutput().addParameter(out);
        String result = execute(t);
        assertNotNull(result);   
        System.out.println(result);
        assertTrue("result dones't contain expected data",result.indexOf(TEST_DATA) != -1);          
    }


    
    /** calls application, with indirect parameter values */
    public void testCreateApplicationIndirect() throws Exception {
        Tool t = buildTool();
        
       File inputFile = File.createTempFile("CommandLineApplicationTest-Input",null);
       File outputFile = File.createTempFile("CommandLineApplicationTest-Output",null);
       PrintWriter pw = new PrintWriter(new FileWriter(inputFile));
       pw.println(TEST_DATA);
       pw.close();
       
       ParameterValue inFile = new ParameterValue();
       inFile.setName("P9");
       inFile.setValue(inputFile.toURI().toString());
       inFile.setIndirect(true);
       
       ParameterValue out = new ParameterValue();
       out.setName("P3");
       out.setIndirect(true);
       out.setValue(outputFile.toURI().toString());
       
       t.getInput().addParameter(inFile);
       t.getOutput().addParameter(out);
       String result = execute(t);
       assertNotNull(result);   
       System.out.println(result);
       assertEquals("results not in expected place",result,outputFile.toURI().toString());
       StringWriter contents = new StringWriter();
       Piper.pipe(new FileReader(outputFile),contents);
       System.out.println(contents.toString());
       assertTrue("result dones't contain expected data",contents.toString().indexOf(TEST_DATA) != -1);                     
    }
    protected Tool buildTool() throws Exception{
        // select the interface we're going to use.
        ApplicationInterface interf = testAppDescr.getInterface("I2");
        assertNotNull(interf);
        // from this 'meta data' populat a tool..
       Tool t = new Tool();
       t.setInterface(interf.getName());
       Input input = new Input();
       t.setInput(input);
       Output output= new Output();
       t.setOutput(output);
       return t;
    }    
    
    private String execute(Tool t) throws Exception, CeaException, InterruptedException {
        
           // now create an application based on these details        
            Application app = testAppDescr.initializeApplication("testExecution",new User(),t);
            assertNotNull(app);
            assertTrue(app instanceof CommandLineApplication);
            // and now run it.
           MockMonitor monitor = new MockMonitor();
           app.addObserver(monitor);
           app.execute();
           monitor.waitFor(WAIT_SECONDS);
        
           // ok, either timed out, or the application finished..
           // check behaviour of monitor.
           assertTrue(monitor.sawExit);
           assertNotNull(monitor.sawApp);
           // check it completed, not in error, etc.
           assertEquals("application did not complete correctly",Status.COMPLETED,app.getStatus());
           // check results.    
           ResultListType results = app.getResult();
           assertEquals(1,results.getResultCount());
           String result = results.getResult(0).getValue();
        return result;
    }
    
    private static final int WAIT_SECONDS = 30;
}


/* 
$Log: CommandLineApplicationTest.java,v $
Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:43:39  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:24:18  nw
intermediate version

Revision 1.1.2.1  2004/06/14 08:57:48  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.22.1  2004/05/28 10:23:11  nw
checked in early, broken version - but it builds and tests (fail)
 
*/