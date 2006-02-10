/*$Id: QueryRegistryClientADQLTest.java,v 1.9 2006/02/10 11:16:16 clq2 Exp $
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

import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;

import java.io.*;

import junit.framework.TestCase;

import java.net.URL;

/**
 * 
 * @author Kevin Benson
 * @author Noel Winstanley
 * @todo assert something in these tests.
 *
 */
public class QueryRegistryClientADQLTest extends AbstractTestForRegistry {

    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public QueryRegistryClientADQLTest(String arg0) {
        super(arg0);
    }
            
    public void testSearch() throws Exception {
       Document queryDoc = askQueryFromFile("QueryForIdentifier--adql-v0.7.4.xml");
       Document  result = rs.search(queryDoc);
       assertNotNull(result);
       DomHelper.DocumentToStream(result,System.out);
       assertVODescription(result);
    }
    
   protected Document askQueryFromFile(String queryFile) throws Exception {
      assertNotNull(queryFile);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      
      assertNotNull("Could not open query file :" + queryFile,is);
      Document queryDoc = DomHelper.newDocument(is);
      return queryDoc;
   }    

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

}