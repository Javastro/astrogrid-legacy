/*$Id: QueryOAIRegistryTest.java,v 1.7 2004/12/18 18:30:16 jdt Exp $
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
import java.util.Date;
import java.util.Calendar;

import java.util.HashMap;
import java.util.List;

import java.io.*;

import junit.framework.TestCase;

import java.net.URL;

/**
 * @author Kevin Benson
 * @author Noel Winstanley
 * @todo add assertions on document format.
 *
 */
public class QueryOAIRegistryTest extends AbstractTestForRegistry {
     
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public QueryOAIRegistryTest(String arg0) {
        super(arg0);
    }
            
    public void testIdentify() throws RegistryException
    {
       //HashMap auth = delegate.managedAuthorities();
       Document result = rs.identify();
       
       DomHelper.DocumentToStream(result,System.out);
    }
    
    public void testListRecords() throws RegistryException {
    	Document result = rs.listRecords();
      DomHelper.DocumentToStream(result,System.out);
    }
    
   public void testListRecordsWithDate() throws RegistryException {
        Calendar fromCal = Calendar.getInstance();
        fromCal.set(2001,5,9);
        Document result = rs.listRecords(fromCal.getTime());
        DomHelper.DocumentToStream(result,System.out);
   }
   
   public void testGetRecord() throws RegistryException {
       Document result = rs.getRecord("ivo://org.astrogrid.localhost/org.astrogrid.registry.RegistryService");
       DomHelper.DocumentToStream(result,System.out);
   }
   
   public void testListMetadataFormats() throws RegistryException {
       Document result = rs.listMetadataFormats("ivo://org.astrogrid.localhost/org.astrogrid.registry.RegistryService");
       DomHelper.DocumentToStream(result,System.out);
   }
   
   public void testListIdentifiers() throws RegistryException {
       Document result = rs.listIdentifiers();
       DomHelper.DocumentToStream(result,System.out);
   }
   
   public void testGetRegistries() throws RegistryException {
       Document result = rs.getRegistries();
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
   }

}


/* 
*/