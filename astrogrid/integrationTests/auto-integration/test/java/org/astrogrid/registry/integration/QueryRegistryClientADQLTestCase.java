/*$Id: QueryRegistryClientADQLTestCase.java,v 1.1 2004/07/26 14:07:59 KevinBenson Exp $
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
import org.astrogrid.registry.client.query.QueryRegistry;

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
public class QueryRegistryClientADQLTestCase extends TestCase {
    
   //public static final String SERVICE_ENDPOINT = "http://localhost:8080/astrogrid-registry-SNAPSHOT/services/RegistryQuery" ;    
   
   QueryRegistry qr = null; 
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public QueryRegistryClientADQLTestCase(String arg0) {
        super(arg0);
    }
            
    public void testSearch() throws Exception {
       Document queryDoc = askQueryFromFile("GetLocalHostResourcesADQL.xml");
       Document  result = qr.search(queryDoc);
       assertNotNull(result);
       DomHelper.DocumentToStream(result,System.out);       
    }
    
   public void testSearch2() throws Exception {
      Document queryDoc = askQueryFromFile("GetLocalHostResourcesADQL2.xml");
      Document  result = qr.search(queryDoc);
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);       
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
      qr = new QueryRegistry();
   }

}


/* 
*/