/*
 * $Id: RegistryFunctionTest.java,v 1.1 2004/05/07 15:32:36 pah Exp $
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

import java.net.URISyntaxException;
import java.net.URL;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.store.Ivorn;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 07-May-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryFunctionTest extends RegistryBaseTestCase {

   private URL entryurl;

   /**
    * Constructor for RegistryFunctionTest.
    * @param arg0
    */
   public RegistryFunctionTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(RegistryFunctionTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      entryurl = this.getClass().getResource("dummyEntry.xml");
   }
   
   public void testAddnew() throws RegistryException, URISyntaxException
   {
      adminDelegate.updateFromURL(entryurl);
      Ivorn ivorn = new Ivorn("ivo://"+AUTHORITY_ID+"/"+RESOURCE_KEY);
      System.out.println(ivorn.toString()+" "+ivorn.toRegistryString());
      Document dom = delegate.getResourceByIdentifierDOM(ivorn);
      assertNotNull("did not return the resource as DOM", dom);
      Element doc = dom.getDocumentElement();
      XMLUtils.PrettyElementToStream(doc, System.out);
      assertTrue("did not find the resource  - returned a null document - should have an error message before now",doc.hasChildNodes());
      VODescription val = delegate.getResourceByIdentifier(ivorn);
      assertNotNull("did not return the resource as Castor Object",val);
      
      

   }
   //TODO need tests for the other types of

}
