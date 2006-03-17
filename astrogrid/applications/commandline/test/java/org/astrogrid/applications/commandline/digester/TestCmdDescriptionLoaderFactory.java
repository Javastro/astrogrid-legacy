/*
 * $Id: TestCmdDescriptionLoaderFactory.java,v 1.7 2006/03/17 17:50:58 clq2 Exp $
 * 
 * Created on 02-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.digester;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.picocontainer.Parameter;
import org.picocontainer.PicoException;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;

import org.astrogrid.applications.commandline.CommandLineApplicationDescription;
import org.astrogrid.applications.commandline.BasicCommandLineConfiguration;
import org.astrogrid.applications.commandline.CommandLineConfiguration;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.TestAppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;

/**
 * @author Paul Harrison (pharriso@eso.org) 02-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class TestCmdDescriptionLoaderFactory {

   /**
    * @param inputFile
    * @return
    * @throws Exception
    */
   public static CommandLineDescriptionsLoader createDescriptionLoader(final URL inputFile) throws Exception {
    
    // Arrange the configuration parameters in a suitable adaptor.
    // This mimics the use, in operation, of an implementation
    // of CommandLineConfiguration that can get metadata for itself.
    BasicCommandLineConfiguration config = new BasicCommandLineConfiguration();
    config.setApplicationDescription(inputFile);
    
    DefaultPicoContainer container = new DefaultPicoContainer();
    
    container.registerComponentInstance(CommandLineConfiguration.class, 
                                        config);
    container.registerComponent(new ConstructorInjectionComponentAdapter(
            CommandLineApplicationDescription.class,
            CommandLineApplicationDescription.class,
            new Parameter[]{new ComponentParameter(ApplicationDescriptionEnvironment.class), new ConstantParameter(container)}                                
            ));
    container.registerComponentImplementation(InMemoryIdGen.class);
    container.registerComponentImplementation(DefaultProtocolLibrary.class);
    container.registerComponentInstance(AppAuthorityIDResolver.class, 
                                        new TestAppAuthorityIDResolver("org.astrogrid.test"));
    container
            .registerComponentImplementation(ApplicationDescriptionEnvironment.class);

  
     container.verify();
     CommandLineDescriptionsLoader dl 
         = new CommandLineDescriptionsLoader(config, 
                                             new CommandLineApplicationDescriptionFactory(container),
                                             (ApplicationDescriptionEnvironment)container.getComponentInstanceOfType(ApplicationDescriptionEnvironment.class));
     return dl;
   }

}


/*
 * $Log: TestCmdDescriptionLoaderFactory.java,v $
 * Revision 1.7  2006/03/17 17:50:58  clq2
 * gtr_1489_cea correted version
 *
 * Revision 1.5  2006/03/07 21:45:26  clq2
 * gtr_1489_cea
 *
 * Revision 1.2.34.2  2006/01/26 13:16:34  gtr
 * BasicCommandLineConfiguration has absorbed the functions of TestCommandLineConfiguration.
 *
 * Revision 1.2.34.1  2005/12/19 18:12:30  gtr
 * Refactored: changes in support of the fix for 1492.
 *
 * Revision 1.2  2005/07/05 08:27:01  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/06/02 14:57:28  pah
 * merge the ProvidesVODescription interface into the MetadataService interface
 *
 */
