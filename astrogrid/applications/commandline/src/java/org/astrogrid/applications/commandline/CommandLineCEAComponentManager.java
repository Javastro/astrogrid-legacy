/*$Id: CommandLineCEAComponentManager.java,v 1.15 2006/03/17 17:50:58 clq2 Exp $
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
    
    logger.info("Components for the CL-CEC have been registered.");
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


/*
$Log: CommandLineCEAComponentManager.java,v $
Revision 1.15  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.13  2006/03/07 21:45:25  clq2
gtr_1489_cea

Revision 1.10.12.4  2006/01/31 21:39:06  gtr
Refactored. I have altered the configuration code slightly so that the JUnit tests can impose a Configuration instance to configure the tests. I have also fixed up almost all the bad tests for commandline and http.

Revision 1.10.12.3  2006/01/25 17:02:49  gtr
Refactored: the configuration is now a fixed structure based at the configurable location cea.base.dir.

Revision 1.10.12.2  2005/12/19 18:44:46  gtr
Fixed: the "command-line provider" is now registered (previously it had been left commented out).

Revision 1.10.12.1  2005/12/19 18:12:30  gtr
Refactored: changes in support of the fix for 1492.

Revision 1.10  2005/09/01 16:08:46  clq2
gtr_1230_2

Revision 1.9.2.2  2005/08/17 15:02:05  gtr
The wrapping of very long lines is improved.

Revision 1.9.2.1  2005/08/17 11:31:50  gtr
The configuration keys were shortened.

Revision 1.9  2005/08/10 14:45:37  clq2
cea_pah_1317

Revision 1.8  2005/08/10 12:22:08  clq2
roll back to before Guy's 1230 merge, need to re do it.

Revision 1.6  2005/07/05 08:27:01  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.5.42.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.5.28.2  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.5.28.1  2005/06/02 14:57:29  pah
merge the ProvidesVODescription interface into the MetadataService interface

Revision 1.5  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.4.24.1  2005/03/11 11:21:48  nw
adjusted to fit with pico 1.1

Revision 1.4  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.3.90.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.3  2004/07/09 11:00:41  nw
removed auto-registration component for now

Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:43:39  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:24:18  nw
intermediate version

Revision 1.1.2.1  2004/06/14 08:57:47  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.2  2004/05/28 10:23:11  nw
checked in early, broken version - but it builds and tests (fail)

Revision 1.1.2.1  2004/05/21 12:00:22  nw
merged in latest changes from HEAD. start of refactoring effort

*/
