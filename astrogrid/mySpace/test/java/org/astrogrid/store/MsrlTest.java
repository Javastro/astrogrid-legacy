/*
 * $Id: MsrlTest.java,v 1.1 2004/03/01 22:35:54 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store;

import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.store.Msrl;


/**
 * Tests MSRL references - see org.astrogrid.store.Msrl.java
 */

public class MsrlTest extends TestCase
{
   
   public final static String DELEGATE = "http://grendel12.roe.ac.uk:8080/astrogrid-mySpace";
   public final static String PATH = "test.astrogrid.org/avodemo/serv1/query/mch-6dF-query.xml";
   public final static String SERVER = "SomeServerId";
   
   /**
    * Basic tests
    */
   public void testMsrl()  {
      
      String validMsrl = "myspace:"+DELEGATE;
      assertTrue(Msrl.isMsrl(validMsrl));
      
      try {
         Msrl msrl = new Msrl(validMsrl);
         assertEquals(msrl.toString(), validMsrl);
         
         validMsrl = validMsrl +"#"+PATH;
         msrl = new Msrl(validMsrl);
         assertEquals(msrl.toString(), validMsrl);
         
         validMsrl = validMsrl +"@"+SERVER;
         msrl = new Msrl(validMsrl);
         assertEquals(msrl.toString(), validMsrl);
      }
      catch (MalformedURLException mue) {
         fail("Could not cope with valid msrl '"+validMsrl+"'");
      }

      try {
         Msrl msrl = new Msrl(new URL(DELEGATE), PATH, SERVER);
         assertEquals(msrl.toString(), validMsrl);
      }
      catch (MalformedURLException mue) {
         fail("Test error: URL in test code is invalid");
      }
      
   }


   /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
     * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(MsrlTest.class);
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
$Log: MsrlTest.java,v $
Revision 1.1  2004/03/01 22:35:54  mch
Tests for StoreClient

Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.1  2004/02/16 23:31:47  mch
IVO Resource Name representation

 */

