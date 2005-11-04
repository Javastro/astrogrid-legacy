/*
 * $Id: PiperTest.java,v 1.5 2005/11/04 17:31:05 clq2 Exp $
 */

package org.astrogrid.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests Piper
 */

public class PiperTest extends TestCase
{
   private static final String testContents = "A length of string to test streams against";

   /**
    * Streams
    */
   public void testStreamPipe() throws IOException
   {
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      Piper.bufferedPipe(new ByteArrayInputStream(testContents.getBytes()), out);

      assertEquals("Pipe has changed contents", testContents, out.toString());
   }

   /**
    * Readers
    */
   public void testReadPipe() throws IOException
   {
      StringWriter writer = new StringWriter();

      Piper.bufferedPipe(new StringReader(testContents), writer);

      assertEquals("Pipe has changed contents", testContents, writer.toString());
   }

   public void testBig() throws Exception
   {
       File f = File.createTempFile("foo", ".dat");
       FileOutputStream fs = new FileOutputStream(f);
       byte[] testin = new byte[1024*1024*31];
       Arrays.fill(testin, (byte)1);

       System.out.println("attempting big copy");
       Piper.pipe(new ByteArrayInputStream(testin), fs);
       System.out.println("finished big copy");

       f.delete();
   }


    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(PiperTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
}

/*
 $Log: PiperTest.java,v $
 Revision 1.5  2005/11/04 17:31:05  clq2
 axis_gtr_1046

 Revision 1.4.94.1  2005/10/10 15:33:47  gtr
 Fix for BZ1408: a temporary file, named by java.io.File, is used instead of /dev/null.

 Revision 1.4  2004/09/24 14:17:40  pah
 added excessive transfer size warning at 30M

 Revision 1.3  2004/03/02 11:55:28  mch
 Changed to more thorough buffered pipe tests

 Revision 1.2  2004/03/01 13:48:20  mch
 Fixed odd StringInputStream dependency changed to ByteArrayInputStream

 Revision 1.1  2004/02/17 14:56:25  mch
 Increased test coverage


 */
