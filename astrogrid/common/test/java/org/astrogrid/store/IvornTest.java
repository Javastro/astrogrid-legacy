/*
 * $Id: IvornTest.java,v 1.6 2004/07/07 10:55:09 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store;
import java.net.URISyntaxException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests IVORNs
 */

public class IvornTest extends TestCase
{

   String validirn = "ivo://test.astrogrid.org/avodemo#serv1/query/mch-6dF-query.xml";
      
   /**
    * Basic tests to/from string representations
    */
   public void testIvornStrings()  {
      
      try {
         Ivorn irn = new Ivorn(validirn);

         assertEquals(validirn, irn.toString());
         
         assertEquals("test.astrogrid.org/avodemo", irn.getPath());
         assertEquals("serv1/query/mch-6dF-query.xml", irn.getFragment());
      }
      catch (URISyntaxException use) {
         fail("Couldn't cope with valid irn "+validirn);
      }
   }
   
   public void testIvornFullConstructor() {
      //check that the three part constructor produces the same result.
      Ivorn irn = new Ivorn("test.astrogrid.org", "avodemo", "serv1/query/mch-6dF-query.xml");
      assertEquals(validirn, irn.toString());
   }
   
   public void testIvornOldConstructor() {
      //check that the two part constructor produces the same result.
      Ivorn irn = new Ivorn("test.astrogrid.org/avodemo", "serv1/query/mch-6dF-query.xml");
      assertEquals(validirn, irn.toString());
   }

   public void testIllegalConstructor() {
      //should fail
      try {
         Ivorn irn = new Ivorn("test.astrogrid.org", "serv1/query/mch-6dF-query.xml");
         fail("Should have failed with no key");
      }
      catch (IllegalArgumentException iae) {
         //ignore - supposed to happen
      }
   }

   /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
     * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(IvornTest.class);
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
$Log: IvornTest.java,v $
Revision 1.6  2004/07/07 10:55:09  mch
Added tests

Revision 1.5  2004/07/06 19:24:36  mch
Minor fix :-)

Revision 1.4  2004/07/06 19:19:53  mch
Switched assertEquals around so 'expected' works ok for failures

Revision 1.3  2004/06/17 17:34:08  jdt
Miscellaneous coding standards issues.

Revision 1.2  2004/03/12 15:11:33  dave
Removed extra import in IvornTest.
Fixed redundant '#' in Ivorn with no fragment.
Fixed missing new-line at end of file.

Revision 1.1  2004/03/12 13:14:01  mch
Moved to common

Revision 1.1  2004/03/01 22:35:54  mch
Tests for StoreClient

Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.1  2004/02/16 23:31:47  mch
IVO Resource Name representation

 */


