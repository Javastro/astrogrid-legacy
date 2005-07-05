/*
 * $Id: AbstractTestForRegistry.java,v 1.6 2005/07/05 10:54:36 jdt Exp $
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

import org.astrogrid.common.bean.v1.Namespaces;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.query.OAIService;
import org.astrogrid.scripting.Astrogrid;
import org.astrogrid.scripting.Service;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.test.schema.SchemaMap;

import org.astrogrid.registry.common.RegistryValidator;
import org.astrogrid.registry.common.RegistrySchemaMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
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
   
   protected OAIService rsOAI = null;
   

   /**
    * @param arg0
    */
   public AbstractTestForRegistry(String arg0) {
      super(arg0);
      rs = factory.createQuery();
      ras = factory.createAdmin();
      rsOAI = factory.createOAI();
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
//      // this is how a client would validate the response from the registry.
//      NodeList nl = doc.getElementsByTagNameNS(Namespaces.REGINTERFACE,"VODescription");
//      assertTrue("did not find any VODescription in registry response",nl.getLength() >0);
//      Element el = (Element) nl.item(0);
//      AstrogridAssert.assertSchemaValid(el,"VODescription",SchemaMap.ALL);
      
      //this line is the registries own validator, which is really cheating needs to be validated independently as above (as a client would do).
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
