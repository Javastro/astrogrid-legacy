/*$Id: CommandLineCEAComponentManager.java,v 1.16 2007/09/28 18:03:35 clq2 Exp $
 * Created on 04-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.applications.commandline;

import org.astrogrid.applications.commandline.control.CommandLineCECControl;
import org.astrogrid.applications.commandline.digester.CommandLineApplicationDescriptionFactory;
import org.astrogrid.applications.commandline.digester.CommandLineDescriptionsLoader;
import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.DefaultApplicationEnvironmentRetriever;
import org.astrogrid.applications.manager.ExecutionController;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.BasicComponentParameter;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;

import java.io.File;
import java.net.URL;

/** Component manager that create a cea server that runs command-line applications
 * @author Noel Winstanley nw@jb.man.ac.uk 04-May-2004
 *
 */
public class CommandLineCEAComponentManager extends EmptyCEAComponentManager  {

    /**
     * construct a component manager configured with a working cea server with 
     * the commandline application provider.
     */
    public CommandLineCEAComponentManager() {
      super();
      
      registerDefaultServices(pico);
      EmptyCEAComponentManager.registerDefaultRegistryUploader(pico);
      
      // now the provider
      registerCommandLineProvider(pico);
    }
    

    /** register just the components for the commandline provider - none of the generic components */
    public static final void registerCommandLineProvider(MutablePicoContainer pico) {

      // The CEC configuration, in its type-safe form. The configuration
      // has two interfaces, one generic and one specific to the CL-CEC.
      logger.debug("Registering the CEC configuration.");
      pico.registerComponentImplementation(Configuration.class, 
                                           BasicCommandLineConfiguration.class);
      pico.registerComponentImplementation(CommandLineConfiguration.class, 
                                           BasicCommandLineConfiguration.class);
      
      // The carrier for the application environment. The "default services"
      // include one of these in order to be self consistent, but we need to
      // replace that with a specialized one.
      logger.debug("registering Commandline Environment Retriever");
      pico.unregisterComponent(ApplicationEnvironmentRetriver.class);
      pico.registerComponentImplementation(ApplicationEnvironmentRetriver.class,
                                           CommandLineExecutionEnvRetriever.class);
      
      // The command-line application server uses a specialized execution controllor.
      log.debug("registering the queuing execution-controller...");
      pico.unregisterComponent(ExecutionController.class);
      pico.registerComponentImplementation(ExecutionController.class, QueuedJobList.class);
      pico.unregisterComponent(JobList.class);
      pico.registerComponentImplementation(JobList.class, QueuedJobList.class);
      
      // stuff required for application descriptions
      log.debug("registering commandline description loader");
      pico.registerComponentImplementation(CommandLineDescriptionsLoader.class);

      // factory for appDescs - necessary to register parameter to this by hand -
      // as latest version of pico is a bit funny about references to the container itself.
      log.debug("registering the commandline application description factory");
      pico.registerComponentImplementation(CommandLineApplicationDescriptionFactory.class,
                                           CommandLineApplicationDescriptionFactory.class, 
                                           new Parameter[]{new ConstantParameter(pico)});

      // 'factory' for environments
      log.debug("registering the commandline application environment factory");
      pico.registerComponent( // create a new instance each time.
        new ConstructorInjectionComponentAdapter(CommandLineApplicationEnvironment.class,
                                                 CommandLineApplicationEnvironment.class));

     // again, create a new one at each call. again, need to pass in pico parameter separately.
     pico.registerComponent(
       new ConstructorInjectionComponentAdapter(CommandLineApplicationDescription.class,
                                                CommandLineApplicationDescription.class, 
                                                new Parameter[]{new ComponentParameter(ApplicationDescriptionEnvironment.class),
                                                new ConstantParameter(pico)}
             ));

    pico.registerComponentImplementation(CommandLineCECControl.class);
    
    logger.info("Components for the command-line-application server have been registered.");
  }
    
   /**
    * Sets a specific instance of the CEC-configuration object.
    * This is useful for testing with an environment set in the
    * the test-code. Calling this method circumvents the usual
    * process of finding the configuration from JNDI etc. and
    * is typically inappropriate for a CEC in actual service.
    */
   public void setCommandLineConfigurationInstance(CommandLineConfiguration c) {
     this.pico.unregisterComponent(CommandLineConfiguration.class);
     this.pico.registerComponentInstance(CommandLineConfiguration.class, c);
     this.pico.unregisterComponent(Configuration.class);
     this.pico.registerComponentInstance(Configuration.class, c);
   }  

 }