/*$Id: MetadataTest.java,v 1.2 2009/10/21 19:01:00 gtr Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.tableserver.metadata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import junit.framework.TestCase;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.InstallationIvorn;
import org.astrogrid.tableserver.jdbc.RdbmsTableMetaDocGenerator;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.astrogrid.slinger.ivo.IVORN;

/**
 * tests the metadata generators, etc
 *
 */
public class MetadataTest extends TestCase {
  
   public void setUp() {
      SampleStarsPlugin.initConfig();
      ConfigFactory.getCommonConfig().setProperty("datacenter.url", "http://localhost:8080/wossname");
   }
   
   /** Checks that the identifiers are there and valid and not duplicates */
   public static void assertIdentifiersOK(Document candidate) {
      
      Hashtable ids = new Hashtable();
      
      Element[] resources = DomHelper.getChildrenByTagName(candidate.getDocumentElement(), "Resource");
      for (int r = 0; r < resources.length; r++) {
         String xsitype = resources[r].getAttribute("xsi:type");
         Element[] idNodes = DomHelper.getChildrenByTagName(resources[r], "identifier");
         assertEquals("identifier tags", 1, idNodes.length);

         Element idNode = idNodes[0];

         String rawId = DomHelper.getValueOf(idNode);
         IVORN id = null;
         try {
            id = new IVORN(rawId);
         }
         catch (URISyntaxException use) {
            fail("id "+rawId+" is not a valid IVORN: "+use);
         }
         String configAuth = ConfigFactory.getCommonConfig().getString(InstallationIvorn.AUTHID_KEY);
         assertTrue("identity "+id.getAuthority()+" doesn't start with config'd authority '"+configAuth+"'", id.getAuthority().startsWith(configAuth));
            
         //check for duplicates
         if (ids.get(id) != null) {
            fail("Duplicate ID "+id);
         }
         ids.put(id, id);
      }
   }
   
   /** Checks that it includes the CEA resources */
   public void assertHasCeaResources(Document candidate) {

      boolean hasService = false;
      boolean hasApp = false;
      
      Element[] resources = DomHelper.getChildrenByTagName(candidate.getDocumentElement(), "Resource");
      for (int r = 0; r < resources.length; r++) {
         String xsitype = resources[r].getAttribute("xsi:type");

         if (xsitype.equals("cea:CeaService")) {
            if (hasService) {
               fail("Duplicate cea:CeaService elements in VoDescription");
            }
            hasService = true;
         }
         
         if (xsitype.equals("cea:CeaApplication")) {
            if (hasApp) {
               fail("Duplicate cea:CeaApplication elements in VoDescription");
            }
            hasApp = true;
         }
      }
      
      assertTrue("No cea:CeaService element", !hasService);
      assertTrue("No cea:CeaApplication element", !hasApp);
      
   }

   
   //checks that the metadoc is valid and reads OK
   public void testMetadoc() throws IOException {
      assertTrue(TableMetaDocInterpreter.isValid());
   }
   
   /** Tests the resource generator */
   public void testGenerator() throws Exception {
     
      RdbmsTableMetaDocGenerator generator = new RdbmsTableMetaDocGenerator();

      //generate metadata - use default catalog
      String metadoc = generator.getMetaDoc(null);

      //check it's well formed
      Document metaDoc = DomHelper.newDocument(metadoc);
      
      //debug
      DomHelper.DocumentToStream(metaDoc, System.out);
    }

   public void testRegistryDates() {
      Calendar ukcal = new GregorianCalendar(Locale.UK);
      Calendar uscal = new GregorianCalendar(Locale.US);
   }
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(MetadataTest.class);
   }
   
   
}



