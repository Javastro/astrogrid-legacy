/*
 * $Id: AbstractTestForRegistry.java,v 1.2 2004/12/18 18:30:16 jdt Exp $
 * 
 * Created on 07-May-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.registry.integration;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.scripting.Astrogrid;
import org.astrogrid.scripting.Service;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.test.schema.SchemaMap;

import org.astrogrid.registry.common.RegistryValidator;
import org.astrogrid.registry.common.RegistrySchemaMap;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import junit.framework.TestCase;
import org.astrogrid.util.DomHelper;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 07-May-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class AbstractTestForRegistry extends TestCase {

   protected RegistryService delegate;
   protected final static String AUTHORITY_ID = "org.astrogrid.localhost";
   protected final static String RESOURCE_KEY="dummy.entry";

   protected Service service;
   protected RegistryAdminService adminDelegate;

   /**
    * Our Registry delegate factory.
    *
    */
   private static RegistryDelegateFactory factory = new RegistryDelegateFactory() ;
   
   protected RegistryService rs = null;
   protected RegistryAdminService ras = null;
   

   /**
    * @param arg0
    */
   public AbstractTestForRegistry(String arg0) {
      super(arg0);
      rs = factory.createQuery();
      ras = factory.createAdmin();
   }

   protected void setUp() throws Exception {
      
      Astrogrid ag = Astrogrid.getInstance();
      List regList = ag.getRegistries();
      assertNotNull(regList);
      assertTrue("no registry query endpoints", regList.size() > 0);
      service = (Service)regList.get(0);
      delegate = (RegistryService)service.createDelegate();
      assertNotNull(delegate);

      regList = ag.getRegistryAdmins();
      assertNotNull(regList);
      assertTrue("no registry admin endpoints", regList.size() > 0);
      service = (Service)regList.get(0);
      adminDelegate = (RegistryAdminService)service.createDelegate();
      assertNotNull(adminDelegate);
   }
   
  // useful reg-specific assertions.
   /** assert a document conforms to the vodescription schema */
   public void assertVODescription(Document doc) {
       //AstrogridAssert.assertSchemaValid(doc,"VODescription",RegistrySchemaMap.ALL);
       RegistryValidator.isValid(doc,"VODescription");
   }
   

   /** assert a document conforms to the vodescription schema, and contains at least one resource 
 * @throws TransformerException*/
   public void assertNonEmptyVODescription(Document doc) throws TransformerException {
       assertVODescription(doc);
       AstrogridAssert.assertXpathEvaluatesTo("true","count(/*/*[local-name(.) = 'Resource'] &gt; 0)",doc);
   }
   /** assert a document conforms to the vodescription schema, and containis exactly one resource 
 * @throws TransformerException*/
   public void assertSingletonVODescription(Document doc) throws TransformerException {
       assertVODescription(doc);
       AstrogridAssert.assertXpathEvaluatesTo("true","count(/*/*[local-name(.) = 'Resource'] = 1)",doc);       
   }
   
   
   /* @todo more needed here - inspect contents of a result, handle 'searchResults' etc*/
   
}
