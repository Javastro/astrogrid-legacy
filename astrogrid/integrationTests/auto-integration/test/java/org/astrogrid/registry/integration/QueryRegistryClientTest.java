/*$Id: QueryRegistryClientTest.java,v 1.4 2004/08/05 12:13:58 KevinBenson Exp $
 * Created on 15-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.registry.integration;

import org.astrogrid.registry.RegistryException;

import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;

import java.io.*;

import junit.framework.TestCase;

import java.net.URL;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Apr-2004
 * @author Paul Harrison pah@jb.man.ac.uk 07-May-2004
 *
 */
public class QueryRegistryClientTest extends RegistryBaseTest {
     
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public QueryRegistryClientTest(String arg0) {
        super(arg0);
    }
            
    public void testLoadRegistry() throws RegistryException
    {
       //HashMap auth = delegate.managedAuthorities();
       Document result = rs.loadRegistry();
       assertNotNull(result);
       DomHelper.DocumentToStream(result,System.out);
    }
    
    public void testSubmitQuery() throws Exception {
       Document queryDoc = askQueryFromFile("GetLocalHostResources.xml");
       Document  result = rs.submitQuery(queryDoc);
       assertNotNull(result);
       DomHelper.DocumentToStream(result,System.out);       
    }
    
   public void testSubmitQuery2() throws Exception {
      Document queryDoc = askQueryFromFile("GetContainsResources.xml");
      Document  result = rs.submitQuery(queryDoc);
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);       
   }
   
   public void testGetResourceByIdentifier() throws Exception {
      Document result = rs.getResourceByIdentifier("org.astrogrid.localhost/noaa_trace");
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);      
   }
   
   public void testGetEndpointByIdentifier() throws Exception {
      //String result = qr.getEndPointByIdentifier("org.astrogrid.localhost/org.astrogrid.community.common.security.service.SecurityService");
      //assertNotNull(result);
      //System.out.println("the endpoint = " + result);
   }

   public void testGetEndpointByIdentifier2() throws Exception {
      String result = rs.getEndPointByIdentifier("org.astrogrid.localhost/sia");
      assertNotNull(result);
      System.out.println("the endpoint2 = " + result);
   }
    
    
   protected Document askQueryFromFile(String queryFile) throws Exception {
      assertNotNull(queryFile);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      
      assertNotNull("Could not open query file :" + queryFile,is);
      Document queryDoc = DomHelper.newDocument(is);
      //Document queryDoc = DomHelper.newDocument(new File(queryFile));
      return queryDoc;
   }    

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

}


/* 
*/