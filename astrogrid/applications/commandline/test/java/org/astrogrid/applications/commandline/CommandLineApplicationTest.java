/*$Id: CommandLineApplicationTest.java,v 1.6 2004/09/10 21:29:00 pah Exp $
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
import java.net.URL;

import org.picocontainer.PicoException;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.MandatoryParameterNotPassedException;
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
import org.astrogrid.applications.manager.DefaultExecutionController;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.InMemoryExecutionHistory;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
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
public class CommandLineApplicationTest extends DescriptionBaseTestCase {
    private static final String TEST_DATA = "test input data";

    private static final String PAR4_DATA = "any old rubbish";

    /**
     * Constructor for CmdLineApplicationTest.
     * 
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
        final File workingDir = File.createTempFile(
                "CommandLineApplicationTest", null);
        workingDir.delete();
        workingDir.mkdir();
        assertTrue(workingDir.exists());
        workingDir.deleteOnExit();
        DefaultPicoContainer container = new DefaultPicoContainer();
        container.registerComponent(new ConstructorInjectionComponentAdapter(
                CommandLineApplicationDescription.class,
                CommandLineApplicationDescription.class));
        container.registerComponent(new ConstructorInjectionComponentAdapter(
                CommandLineApplicationEnvironment.class,
                CommandLineApplicationEnvironment.class));
        container.registerComponentImplementation(InMemoryIdGen.class);
        container
                .registerComponentInstance(new CommandLineApplicationEnvironment.WorkingDir() {

                    public File getDir() {
                        return workingDir;
                    }
                });
        DefaultProtocolLibrary lib = new DefaultProtocolLibrary();
        lib.addProtocol(new FileProtocol());
        container.registerComponentInstance(lib);
        container
                .registerComponentImplementation(ApplicationDescriptionEnvironment.class);
        CommandLineApplicationDescriptionFactory descFactory = new CommandLineApplicationDescriptionFactory(
                container);
        try {
            container.verify();
        }
        catch (PicoException t) {
            t.printStackTrace();
            fail("Container misconfigured");
        }
        CommandLineDescriptionsLoader dl = new CommandLineDescriptionsLoader(
                new CommandLineDescriptionsLoader.DescriptionURL() {

                    public URL getURL() {
                        return inputFile;
                    }
                }, descFactory);
        assertNotNull("cannot create the DescriptionLoader", dl);
        descs = dl;
        testAppDescr = descs.getDescription(TESTAPPNAME);
        assertNotNull(testAppDescr);
        // now fix the execution path for this app description.
        assertTrue(testAppDescr instanceof CommandLineApplicationDescription);
        CommandLineApplicationDescription cAppDescr = (CommandLineApplicationDescription)testAppDescr;
        // will only work with unjarred-classes - but this is always the case in
        // development.
        URI uri = new URI(this.getClass().getResource("/app/testapp.sh")
                .toString());
        File appPath = (new File(uri)).getParentFile();

        System.out.println("TESTAPPDIR := " + appPath.getAbsolutePath());
        assertTrue(appPath.exists());
        assertTrue(appPath.isDirectory());
        cAppDescr.setExecutionPath(cAppDescr.getExecutionPath().replaceAll(
                "@TOOLBASEDIR@", appPath.getAbsolutePath()));
        
        ExecutionHistory history = new InMemoryExecutionHistory();
        
        controller = new DefaultExecutionController(dl,history);
    }

    protected BaseApplicationDescriptionLibrary descs;

    protected ApplicationDescription testAppDescr;

    /** calls application, with direct parameter values */
    public void testCreateApplicationDirect() throws Exception {
        Tool t = buildTool();

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

        t.getInput().addParameter(inFile1);
        t.getInput().addParameter(inFile);
        t.getInput().addParameter(p4);
        t.getOutput().addParameter(out);
        t.getOutput().addParameter(echo);
        ResultListType results = execute(t);
        for (int i = 0; i < results.getResultCount(); i++) {
            ParameterValue result = results.getResult(i);
            assertNotNull(result);
            System.out.println("result par name = " + result.getName());

            if (result.getName().equals("P3")) {
                assertTrue("P3 does not contain expected string", result.getValue().indexOf(TEST_DATA) != -1);
            }
            else if (result.getName().equals("P2")) {
                assertTrue("P2 does not contain expected string", result.getValue().indexOf(PAR4_DATA) != -1);
                
            }
            else {
                fail("unknown result pararmeter  " + result.getName()
                        + " returned");
            }
        }
    }

    /** calls application, with indirect parameter values */
    public void testCreateApplicationIndirect() throws Exception {
        Tool t = buildTool();

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

        PrintWriter pw = new PrintWriter(new FileWriter(inputFile));
        pw.println(TEST_DATA);
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
        pw.println(PAR4_DATA);
        pw.close();
        ParameterValue p4 = new ParameterValue();
        p4.setName("P4");
        p4.setIndirect(true);
        p4.setValue(par4file.toURI().toString());

        t.getInput().addParameter(inFile1);
        t.getInput().addParameter(inFile);
        t.getOutput().addParameter(out);
        t.getInput().addParameter(p4);
        t.getOutput().addParameter(echo);
        ResultListType results = execute(t);
        for (int i = 0; i < results.getResultCount(); i++) {
            ParameterValue result = results.getResult(i);
            assertNotNull(result);
            System.out.println("result par name = " + result.getName());

            if (result.getName().equals("P3")) {
                assertEquals("results not in expected place",
                        result.getValue(), outputFile.toURI().toString());
                checkContent(outputFile, TEST_DATA);
            }
            else if (result.getName().equals("P2")) {
                assertEquals("results not in expected place",
                        result.getValue(), parameterEchoFile.toURI().toString());
                checkContent(parameterEchoFile, PAR4_DATA);

            }
            else {
                fail("unknown result pararmeter  " + result.getName()
                        + " returned");
            }
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

    protected Tool buildTool() throws Exception {
        // select the interface we're going to use.
        ApplicationInterface interf = testAppDescr.getInterface("I1");
        assertNotNull(interf);
        // from this 'meta data' populat a tool..
        Tool t = new Tool();
        t.setName(TESTAPPNAME);
        t.setInterface(interf.getName());
        Input input = new Input();
        ParameterValue time = new ParameterValue();
        time.setName("P1");
        time.setValue("0");//no delay
        input.addParameter(time);
        t.setInput(input);
        Output output = new Output();
        t.setOutput(output);
        return t;
    }

    public void testPickupMissingParameters() {
        Tool t = null;
        try {
            t = buildTool();
        }
        catch (Exception e) {
            // not really trying to test this
            e.printStackTrace();
        }
        try {
            execute(t);
            fail("this should have had a parameter not passed exception before now");
        }
        catch (Exception e1) {
            if (!(e1 instanceof MandatoryParameterNotPassedException)) {
                fail("unexpected exception " + e1);
            }
            else {
                System.out.println("expected exception caught");
            }
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
        app.addObserver(controller);
        app.addObserver(monitor);
        app.execute();
        monitor.waitFor(WAIT_SECONDS);

        // ok, either timed out, or the application finished..
        // check behaviour of monitor.
        assertTrue(monitor.sawExit);
        assertNotNull(monitor.sawApp);
        // check it completed, not in error, etc.
        assertEquals("application did not complete correctly",
                Status.COMPLETED, app.getStatus());
        // check results.
        ResultListType results = app.getResult();
        assertEquals(2, results.getResultCount());
        // NB cannot make assumption about result order.....
        System.out.println("results returned in order="
                + results.getResult(0).getName() + ","
                + results.getResult(1).getName());

        return results;
    }

    private static final int WAIT_SECONDS = 30;

    private DefaultExecutionController controller;
}

/*
 * $Log: CommandLineApplicationTest.java,v $
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