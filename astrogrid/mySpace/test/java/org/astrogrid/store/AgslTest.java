/*
 * $Id: AgslTest.java,v 1.3 2004/03/14 13:47:26 mch Exp $
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

public class AgslTest extends TestCase
{

   /**
    * Basic tests
    */
   public void testMake()  {
      
      String validAgsl = "astrogrid:store:myspace:"+MsrlTest.DELEGATE+"#"+MsrlTest.PATH;

      assertTrue(Agsl.isAgsl(validAgsl));
      
      try {
         //check myspace forms
         Agsl agsl = new Agsl(validAgsl);
         assertEquals(agsl.toString(), validAgsl);
         
         //check urls
         validAgsl = "astrogrid:store:ftp://grendel12.roe.ac.uk/thing#path/path/file.txt";
         agsl = new Agsl(validAgsl);
         assertEquals(agsl.toString(), validAgsl);
      
         assertEquals(agsl.getEndpoint(), "ftp://grendel12.roe.ac.uk/thing");
         assertEquals(agsl.getPath(), "path/path/file.txt");
         assertEquals("file.txt", agsl.getFilename());
         
         //check file (a rather peculiar url...)
         validAgsl = "astrogrid:store:file://aname#path/path/file.txt";
         agsl = new Agsl(validAgsl);
         String s = agsl.toString();
         assertEquals(validAgsl, agsl.toString());
      
         assertEquals("file://aname", agsl.getEndpoint() );
         assertEquals("path/path/file.txt", agsl.getPath() );
         
      }
      catch (MalformedURLException mue) {
         fail("Could not cope with valid agsl '"+validAgsl+"'");
      }
   }

      
   
   /**
    * test deprecated 4.1 vospaceRl
    */
   public void testVospaceRL()  {
      
      String validVorl = "vospace://http.vm05.astrogrid.org:8080/astrogrid-mySpace#test.astrogrid.org/avodemo/serv1/votable/ProdderResults6dF.vot";

      try {
         Vorl vorl = new Vorl(validVorl);
         assertEquals(validVorl, vorl.toString());
         
         Agsl agsl = new Agsl(validVorl);
         String s = agsl.toString();
         assertEquals("astrogrid:store:myspace:http://vm05.astrogrid.org:8080/astrogrid-mySpace#avodemo@test.astrogrid.org/serv1/votable/ProdderResults6dF.vot",
                        agsl.toString());
      }
      catch (MalformedURLException mue) {
         fail("Could not cope with valid vorl '"+validVorl+"'");
      }
   }

   /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
     * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(AgslTest.class);
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
$Log: AgslTest.java,v $
Revision 1.3  2004/03/14 13:47:26  mch
Increased coverage

Revision 1.2  2004/03/09 23:18:09  mch
Added Vorl tests

Revision 1.1  2004/03/01 22:35:08  mch
Tests for StoreClient

Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.1  2004/02/16 23:31:47  mch
IVO Resource Name representation

 */

