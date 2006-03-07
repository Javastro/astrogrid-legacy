/*
 * $Id: AbstractCmdLineAppTestCase.java,v 1.8 2006/03/07 21:45:26 clq2 Exp $
 * 
 * Created on 23-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;


import java.io.File;
import java.net.URI;
import java.net.URL;

import org.picocontainer.Parameter;
import org.picocontainer.PicoException;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;

import org.astrogrid.applications.commandline.CommandLineConfiguration;
import org.astrogrid.applications.commandline.digester.CommandLineApplicationDescriptionFactory;
import org.astrogrid.applications.commandline.digester.CommandLineDescriptionsLoader;
import org.astrogrid.applications.commandline.digester.CommandLineDescriptionsLoader.DescriptionURL;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.TestAppAuthorityIDResolver;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
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
public abstract class AbstractCmdLineAppTestCase extends
        DescriptionBaseTestCase {

 
    /**
     * @param arg0
     */
    public AbstractCmdLineAppTestCase(TestAppInfo info, String arg0) {
        super(info, arg0);
    }

    /**
     * @param arg0
     */
    public AbstractCmdLineAppTestCase(String arg0) {
        
      super(arg0);
    }

    protected void setUp() throws Exception {
      super.setUp();
        
      // Arrange the configuration parameters in a suitable adaptor.
      // This mimics the use, in operation, of an implementation
      // of CommandLineConfiguration that can get metadata for itself.
      BasicCommandLineConfiguration config = new BasicCommandLineConfiguration();
      config.setApplicationDescription(this.inputFile); // Defined in superclass.
      
      container = new DefaultPicoContainer();

      container.registerComponentInstance(CommandLineConfiguration.class, config);
      container.registerComponent(new ConstructorInjectionComponentAdapter(
                CommandLineApplicationDescription.class,
                CommandLineApplicationDescription.class,
                new Parameter[]{new ComponentParameter(ApplicationDescriptionEnvironment.class), new ConstantParameter(container)}
        ));
      container.registerComponent(new ConstructorInjectionComponentAdapter(
                CommandLineApplicationEnvironment.class,
                CommandLineApplicationEnvironment.class));
      container.registerComponentImplementation(InMemoryIdGen.class);
        
      DefaultProtocolLibrary lib = new DefaultProtocolLibrary();
      lib.addProtocol(new FileProtocol());
      container.registerComponentInstance(lib);
      container.registerComponentInstance(AppAuthorityIDResolver.class, 
                                          new TestAppAuthorityIDResolver("org.astrogrid.test"));
      container.registerComponentImplementation(ApplicationDescriptionEnvironment.class);
      CommandLineApplicationDescriptionFactory descFactory 
          = new CommandLineApplicationDescriptionFactory(container);
      
      try {
        container.verify();
      }
      catch (PicoException t) {
        t.printStackTrace();
        fail("Container misconfigured");
      }
      
      ApplicationDescriptionEnvironment env 
          = (ApplicationDescriptionEnvironment)container.getComponentInstanceOfType(ApplicationDescriptionEnvironment.class);
      CommandLineDescriptionsLoader dl 
          = new CommandLineDescriptionsLoader(config, descFactory, env);
      assertNotNull("cannot create the DescriptionLoader", dl);
      descs = dl;
        
      testAppDescr = (CommandLineApplicationDescription)descs.getDescription(TESTAPPNAME);
      assertNotNull(testAppDescr);
      
      // now fix the execution path for this app description.
      Toolbuilder.fixupExecutionPath( testAppDescr);
      assertTrue(testAppDescr instanceof CommandLineApplicationDescription);
      ExecutionHistory history = new InMemoryExecutionHistory();
        
      controller = new DefaultExecutionController(dl,history);
    }

    protected BaseApplicationDescriptionLibrary descs;
    protected CommandLineApplicationDescription testAppDescr;
    protected DefaultExecutionController controller;
    protected static final int WAIT_SECONDS = 300;
   protected DefaultPicoContainer container;
    
    /**
     * Create a tool instance to run.
     * @return
     */
    protected abstract Tool buildTool(String delay) throws Exception;

}
