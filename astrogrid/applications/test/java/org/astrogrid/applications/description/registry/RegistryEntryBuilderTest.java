/*
 * $Id: RegistryEntryBuilderTest.java,v 1.1 2004/03/24 17:13:15 pah Exp $
 * 
 * Created on 24-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.description.registry;

import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.CommandLineExecutionControllerConfig;
import org.astrogrid.applications.description.DescriptionBaseTestCase;
import org.astrogrid.registry.beans.resource.VODescription;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 24-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryEntryBuilderTest extends DescriptionBaseTestCase {

   private CommandLineExecutionControllerConfig clconf;
   private VODescription template;

   private RegistryEntryBuilder builder;

   /**
    * Constructor for RegistryEntryBuilderTest.
    * @param arg0
    */
   public RegistryEntryBuilderTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(RegistryEntryBuilderTest.class);
   }

   /*
    * @see BaseDBTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();

      InputSource saxis;
      // get the configuration         
      Unmarshaller um = new Unmarshaller(CommandLineExecutionControllerConfig.class);
      assertNotNull(um);

      saxis = new InputSource(inputFile.openStream());
      assertNotNull("problem with input stream ", saxis);
      clconf = (CommandLineExecutionControllerConfig)um.unmarshal(saxis);
      assertNotNull("problem unmarshalling the config", clconf);
      ApplicationList applist = RegistryEntryBuilder.makeApplist(clconf);
      assertNotNull(applist);
      //get the template

      // create the builder....
      Unmarshaller um2 = new Unmarshaller(VODescription.class);
      assertNotNull(um2);
      saxis = new InputSource(config.getRegistryTemplateURL().openStream());
      template = (VODescription)um2.unmarshal(saxis);
      assertNotNull(template);
      builder = new RegistryEntryBuilder(applist, template);
   }

   final public void testMakeEntry() {
     VODescription entry = builder.makeEntry();
     assertNotNull(entry);
   }

}
