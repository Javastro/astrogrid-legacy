/*$Id: QueryRegistryClientTest.java,v 1.11 2004/10/14 13:19:40 KevinBenson Exp $
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
import org.astrogrid.registry.common.FileStoreInterfaceType;
import org.astrogrid.registry.common.MyspaceInterfaceType;

import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;

import java.io.*;

import junit.framework.TestCase;

import java.net.URL;

/**
 * @author Kevin Benson
 * @author Noel Winstanley
 * @author Paul Harrison pah@jb.man.ac.uk 07-May-2004
 * @author Roy Platon rtp@rl.ac.uk 16-Aug-2004
 * 
 *
 */
public class QueryRegistryClientTest extends AbstractTestForRegistry {
     
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
       assertVODescription(result);
    }
    
    public void testLocalHostResources() throws Exception {
       Document queryDoc = askQueryFromFile("GetLocalHostResources.xml");
       Document  result = rs.submitQuery(queryDoc);
       assertNotNull(result);
       DomHelper.DocumentToStream(result,System.out);
       assertNonEmptyVODescription(result);
    }
    
   public void testContainsResources() throws Exception {
      Document queryDoc = askQueryFromFile("GetContainsResources.xml");
      Document  result = rs.submitQuery(queryDoc);
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);
      assertNonEmptyVODescription(result);      
   }

   public void testFindFileStoreInterfaceType() throws Exception {
       Document  result = rs.getResourcesByInterfaceType(new FileStoreInterfaceType());
       assertNotNull(result);
       DomHelper.DocumentToStream(result,System.out);
       assertNonEmptyVODescription(result);      
   }

   public void testFindMyspaceInterfaceType() throws Exception {
       Document  result = rs.getResourcesByInterfaceType(new MyspaceInterfaceType());
       assertNotNull(result);
       DomHelper.DocumentToStream(result,System.out);
       assertNonEmptyVODescription(result);      
   }

/*
 *  Some Typical Queries
 */   
   public void testBasicCatalogueQuery() throws Exception {
      Document queryDoc = askQueryFromFile("BasicCatalogueQuery.xml");
      Document  result = rs.submitQuery(queryDoc);
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);
      assertNonEmptyVODescription(result);
   }
   
   public void testBasicToolQuery() throws Exception {
      Document queryDoc = askQueryFromFile("BasicToolQuery.xml");
      Document  result = rs.submitQuery(queryDoc);
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);
      assertSingletonVODescription(result);      
   }
   
   public void testColumnQuery() throws Exception {
      Document queryDoc = askQueryFromFile("ColumnQuery.xml");
      Document  result = rs.submitQuery(queryDoc);
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);
      assertNonEmptyVODescription(result);      
   }
   
   public void testTabularSkyServiceQuery() throws Exception {
      Document queryDoc = askQueryFromFile("TabularSkyServiceQuery.xml");
      Document  result = rs.submitQuery(queryDoc);
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);
      assertNonEmptyVODescription(result);      
   }
   
   public void testUCDQuery() throws Exception {
      Document queryDoc = askQueryFromFile("UCDQuery.xml");
      Document  result = rs.submitQuery(queryDoc);
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);
      assertNonEmptyVODescription(result);      
   }
   
   public void testResourceByIdentifier() throws Exception {
      Document result =
         rs.getResourceByIdentifier("org.astrogrid.localhost/noaa_trace");
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);
      assertSingletonVODescription(result);      
   }
   
   public void testEndpointByIdentifier() throws Exception {
      //String result =
      //  qr.getEndPointByIdentifier( "org.astrogrid.localhost/" +
      //  "org.astrogrid.community.common.security.service.SecurityService");
      //assertNotNull(result);
      //System.out.println("the endpoint = " + result);
   }

   public void testEndpointByIdentifier2() throws Exception {
      String result =
         rs.getEndPointByIdentifier("org.astrogrid.localhost/sia");
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
