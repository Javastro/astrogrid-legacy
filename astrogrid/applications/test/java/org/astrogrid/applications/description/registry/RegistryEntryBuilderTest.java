/*
 * $Id: RegistryEntryBuilderTest.java,v 1.2 2004/03/29 12:38:56 pah Exp $
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

import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
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
public class RegistryEntryBuilderTest extends RegEntryBaseTestCase {

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

   final public void testMakeEntry() throws MarshalException, ValidationException {
     VODescription entry = builder.makeEntry();
     assertNotNull(entry);
     entry.marshal(new PrintWriter(System.out));
     //TODO - should make more extensive tests....
   }

}
