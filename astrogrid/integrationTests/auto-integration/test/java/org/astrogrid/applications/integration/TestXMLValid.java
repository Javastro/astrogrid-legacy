/*
 * $Id: TestXMLValid.java,v 1.1 2004/10/13 08:14:16 pah Exp $
 * 
 * Created on 13-Oct-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.test.schema.SchemaMap;

import junit.framework.TestCase;

/**
 * Test an xml file for validity. This is a simple test that can be run manually to test for file validity.
 * @author Paul Harrison (pah@jb.man.ac.uk) 13-Oct-2004
 * @version $Name:  $
 * @since iteration6
 */
public class TestXMLValid extends TestCase {

   private static File xmlFile;
   public static void main(String[] args) {
      xmlFile = new File(args[0]);
      junit.textui.TestRunner.run(TestXMLValid.class);
   }
   
   public void testValid() throws FileNotFoundException
   {
      System.out.println(xmlFile.toString());
      InputStream entry = new FileInputStream(xmlFile);
      AstrogridAssert.assertSchemaValid(entry,"VODescription",SchemaMap.ALL);
  
   }
   

}
