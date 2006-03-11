/*
 * $Id: TestCmdDescriptionLoaderFactory.java,v 1.6 2006/03/11 05:57:54 clq2 Exp $
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
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
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
      final File workingDir = File.createTempFile("DescriptionLoaderTest",
            null);
    workingDir.delete();
    workingDir.mkdir();
    if(!workingDir.exists())
    {
       //todo there is probably a better exception to throw...
       throw  new Exception("working dir does not exist");
    }
    workingDir.deleteOnExit();
    DefaultPicoContainer container = new DefaultPicoContainer();
    container.registerComponent(new ConstructorInjectionComponentAdapter(
            CommandLineApplicationDescription.class,
            CommandLineApplicationDescription.class,
            new Parameter[]{new ComponentParameter(ApplicationDescriptionEnvironment.class), new ConstantParameter(container)}                                
            ));
    container.registerComponentImplementation(InMemoryIdGen.class);
    container.registerComponentImplementation(DefaultProtocolLibrary.class);
    container.registerComponentInstance(BaseApplicationDescriptionLibrary.AppAuthorityIDResolver.class, new BaseApplicationDescriptionLibrary.AppAuthorityIDResolver(){/* (non-Javadoc)
   * @see org.astrogrid.applications.description.BaseApplicationDescriptionLibrary.AppAuthorityIDResolver#getAuthorityID()
   */
  public String getAuthorityID() {
    return "org.astrogrid.test";
  }});
    container
            .registerComponentImplementation(ApplicationDescriptionEnvironment.class);

  
     container.verify();
     CommandLineDescriptionsLoader dl = new CommandLineDescriptionsLoader(
            new CommandLineDescriptionsLoader.DescriptionURL() {

                public URL getURL() {
                    return inputFile;
                }
            }, new CommandLineApplicationDescriptionFactory(container),(ApplicationDescriptionEnvironment)container.getComponentInstanceOfType(ApplicationDescriptionEnvironment.class));
     return dl;
   }

}


/*
 * $Log: TestCmdDescriptionLoaderFactory.java,v $
 * Revision 1.6  2006/03/11 05:57:54  clq2
 * roll back to before merged apps_gtr_1489, tagged as rolback_gtr_1489
 *
 * Revision 1.4  2006/01/10 11:26:52  clq2
 * rolling back to before gtr_1489
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
