/*$Id: UpdateRegistryTest.java,v 1.7 2005/08/04 09:40:11 clq2 Exp $
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
 *
 */
public class UpdateRegistryTest extends AbstractTestForRegistry {
     
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public UpdateRegistryTest(String arg0) {
        super(arg0);
    }
            
    public void testUpdate() throws RegistryException, Exception
    {
        Document doc = askQueryFromFile("AuthorityTest.xml");
        Document updateDoc = ras.update(doc);
        DomHelper.DocumentToStream(updateDoc,System.out);
        //System.out.println(DomHelper.DocumentToString(updateDoc));
        assertTrue("UpdateResponse".equals(updateDoc.getDocumentElement().getLocalName()));
    }
    
    public void testUpdate2() throws RegistryException, Exception
    {
        Document doc = askQueryFromFile("dummytest2.xml");
        Document updateDoc = ras.update(doc);
        DomHelper.DocumentToStream(updateDoc,System.out);
        //System.out.println(DomHelper.DocumentToString(updateDoc));
        assertTrue("UpdateResponse".equals(updateDoc.getDocumentElement().getLocalName()));
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