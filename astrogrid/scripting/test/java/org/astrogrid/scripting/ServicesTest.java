/*$Id: ServicesTest.java,v 1.3 2004/08/09 09:54:58 nw Exp $
 * Created on 28-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting;

import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jan-2004
 *
 */
public class ServicesTest extends TestCase {
   /**
    * Constructor for ServicesTest.
    * @param arg0
    */
   public ServicesTest(String arg0) {
      super(arg0);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(ServicesTest.class);
   }
   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
       URL u = this.getClass().getResource("services.xml");
      s = new Services(u);
   }
   protected Services s;
   /*
    * @see TestCase#tearDown()
    */
   protected void tearDown() throws Exception {
      super.tearDown();
   }
   public void testCreateServiceList() throws Exception {
      List l = s.getAllServices();
      System.out.println(l);
      assertNotNull(l);
      assertEquals(4,l.size());
      Object o = l.get(0);
      assertNotNull(o);
      assertTrue(o instanceof Service);
      Service serv = (Service)o;
      assertNotNull(serv.getType());
      assertNotNull(serv.getEndpoint());   
      
      // more detauled now.
      
      assertEquals(1,s.getApplications().size());
      assertEquals(3,s.getDatacenters().size());
   }
   
   public void testStandardMethods() {
       System.out.println(s);
   }
}


/* 
$Log: ServicesTest.java,v $
Revision 1.3  2004/08/09 09:54:58  nw
moved services.xml to test hierarchy.
altered unit test to specify a path to the service.xml

Revision 1.2  2004/08/09 09:12:17  nw
made testing a little more thorough

Revision 1.1  2004/01/28 17:19:58  nw
first check in of scripting project
 
*/