/*
 * $Id: RegistryEntryBuilderTest.java,v 1.5 2004/04/02 11:55:36 pah Exp $
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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
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

   private File testfile;

   /**
    * Constructor for RegistryEntryBuilderTest.
    * @param arg0
    */
   public RegistryEntryBuilderTest(String arg0) {
      super(arg0);
      //TODO should make this an os independent path - want to look at the file contents at the moment though
      testfile = new File("/tmp/CeaRegEntry.xml");
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(RegistryEntryBuilderTest.class);
   }

   final public void testMakeEntry() throws MarshalException, ValidationException, IOException {
     VODescription entry = builder.makeEntry();
     assertNotNull(entry);
     FileWriter writer = new FileWriter(testfile);

     Marshaller mar = new Marshaller(writer);
     mar.setDebug(true);
     mar.setMarshalExtendedType(true);
     mar.setSuppressXSIType(false);
     mar.setLogWriter(new PrintWriter(System.out));
     mar.setMarshalAsDocument(true);
//   TODO write a castor wiki page about this....      
     mar.setNamespaceMapping("cea", "http://www.ivoa.net/xml/CEAService/v0.1");


     mar.marshal(entry);
     
     writer.close();
     Unmarshaller um = new Unmarshaller(VODescription.class);
     
     //TODO Castor bug -will not round trip....
     VODescription reentry = (VODescription)um.unmarshal(new FileReader(testfile));
     
     
     //TODO - should make more extensive tests....
   }

}
