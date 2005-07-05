/*
 * $Id: RegistryEntryBuilderTest.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.registry.RegistryEntryBuilderTestBase;

/**
 * @author Paul Harrison (pharriso@eso.org) 02-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class RegistryEntryBuilderTest extends RegistryEntryBuilderTestBase {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(RegistryEntryBuilderTest.class);

   private final String configFileName = "/TestApplicationConfig.xml";

   public static void main(String[] args) {
      junit.textui.TestRunner.run(RegistryEntryBuilderTest.class);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.astrogrid.applications.description.registry.RegistryEntryBuilderTestBase#createDesciptionLibrary()
    */
   protected ApplicationDescriptionLibrary createDesciptionLibrary() throws Exception {
      URL inputFile = this.getClass().getResource(configFileName);
      CommandLineDescriptionsLoader lib = null;
      File file = File.createTempFile("test input file", "xml");
      FileWriter fw = new FileWriter(file);
      BufferedReader br =	 new BufferedReader(new InputStreamReader(inputFile.openStream()));
      String line = br.readLine();
      while (line != null)
      {
         line = line.replaceAll("@REGAUTHORITY@", "org.astrogrid.localhost");
         fw.write(line);
         line = br.readLine();
      }
      fw.close();
      inputFile = file.toURL();
    
         lib = TestCmdDescriptionLoaderFactory
               .createDescriptionLoader(inputFile);
         return lib;
   }
   
   

}

/*
 * $Log: RegistryEntryBuilderTest.java,v $
 * Revision 1.2  2005/07/05 08:27:01  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.2  2005/06/08 22:10:45  pah
 * make http applications v10 compliant
 *
 * Revision 1.1.2.1  2005/06/02 14:57:28  pah
 * merge the ProvidesVODescription interface into the MetadataService interface
 *
 */
