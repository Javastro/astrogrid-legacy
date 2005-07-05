/*
 * $Id: AbstractWSAppTestCase.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 * 
 * Created on 23-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.ws;

import java.io.File;
import java.net.URI;
import java.net.URL;

import junit.framework.TestCase;

import org.picocontainer.PicoException;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;

import org.astrogrid.applications.commandline.CommandLineApplicationEnvironment.WorkingDir;
import org.astrogrid.applications.commandline.digester.WebServiceApplicationDescriptionFactory;
import org.astrogrid.applications.commandline.digester.CommandLineDescriptionsLoader;
import org.astrogrid.applications.commandline.digester.CommandLineDescriptionsLoader.DescriptionURL;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.DefaultExecutionController;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.InMemoryExecutionHistory;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public abstract class AbstractWSAppTestCase extends TestCase
        {

 
    /**
     * @param arg0
     */
    public AbstractWSAppTestCase(TestAppInfo info, String arg0) {
        super(info, arg0);
    }

    /**
     * @param arg0
     */
    public AbstractWSAppTestCase(String arg0) {
        
      super(arg0);
    }

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
                WebServiceApplicationDescription.class,
                WebServiceApplicationDescription.class));
        container.registerComponentImplementation(InMemoryIdGen.class);
        
        //standard 
        DefaultProtocolLibrary lib = new DefaultProtocolLibrary();
        lib.addProtocol(new FileProtocol());
        container.registerComponentInstance(lib);
        container.registerComponentInstance(BaseApplicationDescriptionLibrary.AppAuthorityIDResolver.class, new BaseApplicationDescriptionLibrary.AppAuthorityIDResolver(){/* (non-Javadoc)
            * @see org.astrogrid.applications.description.BaseApplicationDescriptionLibrary.AppAuthorityIDResolver#getAuthorityID()
            */
           public String getAuthorityID() {
             return "org.astrogrid.test";
           }});

        container
                .registerComponentImplementation(ApplicationDescriptionEnvironment.class);
        WebServiceApplicationDescriptionFactory descFactory = new WebServiceApplicationDescriptionFactory(
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
                }, descFactory, (ApplicationDescriptionEnvironment)container.getComponentInstanceOfType(ApplicationDescriptionEnvironment.class));
        assertNotNull("cannot create the DescriptionLoader", dl);
        descs = dl;
        testAppDescr = (WebServiceApplicationDescription)descs.getDescription(TESTAPPNAME);
        assertNotNull(testAppDescr);
        // now fix the execution path for this app description.
        assertTrue(testAppDescr instanceof WebServiceApplicationDescription);
        ExecutionHistory history = new InMemoryExecutionHistory();
        
        controller = new DefaultExecutionController(dl,history);
    }

    protected BaseApplicationDescriptionLibrary descs;
    protected WebServiceApplicationDescription testAppDescr;
    private DefaultExecutionController controller;
    protected static final int WAIT_SECONDS = 300;
    
    /**
     * Create a tool instance to run.
     * @return
     */
    protected abstract Tool buildTool(String delay) throws Exception;

}
