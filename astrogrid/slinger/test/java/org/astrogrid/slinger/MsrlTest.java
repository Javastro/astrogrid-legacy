/*
 * $Id: MsrlTest.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.slinger;

import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.slinger.myspace.MSRL;


/**
 * Tests MSRL references - see org.astrogrid.store.Msrl.java
 */

public class MsrlTest extends TestCase
{
   
   public final static String DELEGATE = "http://grendel12.roe.ac.uk:8080/astrogrid-mySpace";
   public final static String PATH = "test.astrogrid.org/avodemo/serv1/query/mch-6dF-query.xml";
   public final static String SERVER = "SomeServerId";
   
   public final static String NAME = "mch-6dF-query.xml";
   
   /**
    * Basic tests
    */
   public void testStringMake()  {
      
      String validMsrl = "myspace:"+DELEGATE;
      assertTrue(MSRL.isMsrl(validMsrl));
      
      try {
         MSRL msrl = new MSRL(validMsrl);
         assertEquals(msrl.toString(), validMsrl);
         
         validMsrl = validMsrl +"#"+PATH;
         msrl = new MSRL(validMsrl);
         assertEquals(msrl.toString(), validMsrl);
         assertEquals(NAME, msrl.getName());
         
         validMsrl = validMsrl +"!"+SERVER;
         msrl = new MSRL(validMsrl);
         assertEquals(NAME, msrl.getFilename());
         assertEquals(MSRL.SCHEME+":"+DELEGATE, msrl.getManagerMsrl().toString());
      }
      catch (MalformedURLException mue) {
         fail("Could not cope with valid msrl '"+validMsrl+"'");
      }
   }
   
   public void testUrlMake() {
      String validMsrl = "myspace:"+DELEGATE+"#"+PATH+"!"+SERVER;
      try {
         MSRL msrl = new MSRL(new URL(DELEGATE), PATH, SERVER);
         assertEquals(msrl.toString(), validMsrl);
         assertEquals(NAME, msrl.getName());
         assertEquals(NAME, msrl.getFilename());
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
Revision 1.2  2004/12/07 01:33:36  jdt
Merge from PAL_Itn07

Revision 1.1.2.2  2004/12/03 11:50:19  mch
renamed Msrl etc to separate from storeclient ones.  Prepared for SRB

Revision 1.1.2.1  2004/11/22 00:46:28  mch
New Slinger Package

Revision 1.2  2004/06/14 23:08:52  jdt
Merge from branches

ClientServerSplit_JDT

and

MySpaceClientServerSplit_JDT



MySpace now split into a client/delegate jar

astrogrid-myspace-<version>.jar

and a server/manager war

astrogrid-myspace-server-<version>.war

Revision 1.1.2.1  2004/06/14 22:33:21  jdt
Split into delegate jar and server war.  
Delegate: astrogrid-myspace-SNAPSHOT.jar
Server/Manager: astrogrid-myspace-server-SNAPSHOT.war

Package names unchanged.
If you regenerate the axis java/wsdd/wsdl files etc you'll need
to move some files around to ensure they end up in the client
or the server as appropriate.
As of this check-in the tests/errors/failures is 162/1/22 which
matches that before the split.

Revision 1.4  2004/04/22 08:58:35  mch
Fixes to tests etc

Revision 1.3  2004/03/14 13:44:51  mch
Increased coverage

Revision 1.2  2004/03/10 00:23:33  mch
Changed @ to !

Revision 1.1  2004/03/01 22:35:54  mch
Tests for StoreClient

Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.1  2004/02/16 23:31:47  mch
IVO Resource Name representation

 */

