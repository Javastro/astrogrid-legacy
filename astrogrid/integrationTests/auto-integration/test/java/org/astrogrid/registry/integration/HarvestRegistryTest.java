/*$Id: HarvestRegistryTest.java,v 1.7 2005/08/04 09:40:10 clq2 Exp $
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
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Apr-2004
 * @author Paul Harrison pah@jb.man.ac.uk 07-May-2004
 *
 */
public class HarvestRegistryTest extends AbstractTestForRegistry {
     
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public HarvestRegistryTest(String arg0) {
        super(arg0);
    }
            
    public void testIdentify() throws RegistryException
    {
       //HashMap auth = delegate.managedAuthorities();
       Document result = rsOAI.identify();
       DomHelper.DocumentToStream(result,System.out);
    }
    
    public void testListRecords() throws RegistryException {
    	Document result = rsOAI.listRecords();
      DomHelper.DocumentToStream(result,System.out);
    }
    
   public void testListRecordsWithDate() throws RegistryException {
        Calendar fromCal = Calendar.getInstance();
        fromCal.set(2001,5,9);
        Document result = rsOAI.listRecords(fromCal.getTime());
        DomHelper.DocumentToStream(result,System.out);
   }
   
   public void testGetRecord() throws RegistryException {
       Document result = rsOAI.getRecord("ivo://org.astrogrid.localhost/org.astrogrid.registry.RegistryService");
       DomHelper.DocumentToStream(result,System.out);
   }
   
   public void testListMetadataFormats() throws RegistryException {
       Document result = rsOAI.listMetadataFormats("ivo://org.astrogrid.localhost/org.astrogrid.registry.RegistryService");
       DomHelper.DocumentToStream(result,System.out);
   }
   
   public void testListIdentifiers() throws RegistryException {
       Document result = rsOAI.listIdentifiers();
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