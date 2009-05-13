/*
 * $Id: ByteCounterTest.java,v 1.1 2009/05/13 13:21:00 gtr Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Tests Byte Counting input/output streams
 */

public class ByteCounterTest extends TestCase
{

   String sampleText = "Some Sample Text";
  
   /**
    * Tests input reads correctly and counts correclty
    */
   public void testInput() throws IOException {
      
      ByteCountingInputStream in = new ByteCountingInputStream(new ByteArrayInputStream(sampleText.getBytes()));
      
      byte[] b = new byte[100];
      
      int siz = in.read(b, 0, sampleText.length());
      
      String s = new String(b,0, siz);
      
      assertEquals("String read is not same as string supplied", sampleText, s);
      
      assertEquals("Number bytes read not right ", sampleText.length(), in.getBytesRead());
   }
   

   /**
    * Tests input reads correctly and counts correclty
    */
   public void testOutput() throws IOException {
      
      ByteArrayOutputStream collector = new ByteArrayOutputStream();
      
      ByteCountingOutputStream out = new ByteCountingOutputStream(collector);

      out.write(sampleText.getBytes());
      
      assertEquals("String collected is not same as string supplied", sampleText, collector.toString());
      
      assertEquals("Number bytes read not right ", out.getBytesWritten(), sampleText.length());
   }
   

   /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
     * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(ByteCounterTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
   
}

