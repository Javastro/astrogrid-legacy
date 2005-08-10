/*
 * $Id: TableConverterTest.java,v 1.3 2005/08/10 17:45:10 clq2 Exp $
 * 
 * Created on 11-Nov-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.apps.tables;

import java.io.IOException;
import java.io.InputStream;

import uk.ac.starlink.table.TableFormatException;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Nov-2004
 * @version $Name:  $
 * @since iteration6
 */
public class TableConverterTest extends TestCase {

   private InputStream inputStream;

   public static void main(String[] args) {
      junit.textui.TestRunner.run(TableConverterTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      inputStream = this.getClass().getResourceAsStream("input.csv");
      
   }
   
   public void testConvert() throws TableFormatException, IOException
   {      
      TableConverter tester = new TableConverter(inputStream, "csv");
      tester.write("VOTABLE");
   }

}
