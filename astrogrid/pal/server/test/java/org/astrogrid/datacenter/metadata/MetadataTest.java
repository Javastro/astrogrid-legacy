/*$Id: MetadataTest.java,v 1.5 2005/03/10 13:49:53 mch Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.metadata;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.FileResourcePlugin;
import org.astrogrid.dataservice.metadata.UrlResourcePlugin;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.tables.TableMetaDocInterpreter;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.dataservice.queriers.sql.RdbmsTableMetaDocGenerator;
import org.astrogrid.dataservice.queriers.test.SampleStarsPlugin;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.IOException;

/**
 * tests the metadata generators, etc
 *
 */
public class MetadataTest extends TestCase {
  
   public void setUp() {
      SampleStarsPlugin.initConfig();
      SimpleConfig.setProperty("datacenter.url", "http://localhost:8080/wossname");
   }
   
   /** Checks that the identifiers are there and valid and not duplicates */
   public void assertIdentifiersOK(Document candidate) {
      
      Hashtable ids = new Hashtable();
      
      Element[] resources = DomHelper.getChildrenByTagName(candidate.getDocumentElement(), "Resource");
      for (int r = 0; r < resources.length; r++) {
         String xsitype = resources[r].getAttribute("xsi:type");
         Element[] idNodes = DomHelper.getChildrenByTagName(resources[r], "Identifier");
         assertTrue("Should only be one identifier tag", idNodes.length==1);

         for (int i = 0; i < idNodes.length; i++) {
            Element id = idNodes[i];
            String authId = DomHelper.getValueOf(id, "AuthorityID");
            String idConfig = SimpleConfig.getSingleton().getString(VoResourceSupport.AUTHID_KEY);
            assertEquals("Authority ID incorrect in Resource type "+xsitype, idConfig, authId);

            String resKey = DomHelper.getValueOf(id, "ResourceKey");
            String resConfig = SimpleConfig.getSingleton().getString(VoResourceSupport.RESKEY_KEY);
            if (!xsitype.equals("AuthorityType")) {
               assertTrue("Resource Key is "+resKey+", should start with "+resConfig+" in Resource type "+xsitype, resKey.startsWith(resConfig));
            }
            
            //check for duplicates
            if (ids.get(resKey) != null) {
               fail("Duplicate ID "+resKey);
            }
            ids.put(resKey, resKey);
         }
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

   public void assertHasRdbmsResource(Document candidate) {
      
      Element[] resources = DomHelper.getChildrenByTagName(candidate.getDocumentElement(), "Resource");
      for (int r = 0; r < resources.length; r++) {
         String xsitype = resources[r].getAttribute("xsi:type");

         if (xsitype.equals("RdbmsMetadata")) {
            //check for table data
            long numTables = resources[r].getElementsByTagName("Table").getLength();
            assertEquals("Number of table elements in metadata, ", 3, numTables);
            return; //OK found it
         }
      }
      fail("No RdbmsMetadata resource");
   }
   
   //checks that the metadoc is valid and reads OK
   public void testMetadoc() throws IOException {
      TableMetaDocInterpreter reader = new TableMetaDocInterpreter();
   }
   
   
   public void testServer() throws Exception {
      Document metadata = VoDescriptionServer.getVoDescription();
      //debug
      DomHelper.DocumentToStream(metadata, System.out);
      
      assertNotNull(metadata);
      assertIdentifiersOK(metadata);
      assertHasRdbmsResource(metadata);
   }
   
   /** Tests the resource generator */
   public void testGenerator() throws Exception {
     
      RdbmsTableMetaDocGenerator generator = new RdbmsTableMetaDocGenerator();

      //generate metadata
      String metadoc = generator.getMetaDoc();

      //check it's valid
      Document metaDoc = DomHelper.newDocument(metadoc);
      
      //debug
      DomHelper.DocumentToStream(metaDoc, System.out);
    }

   public void testMetadataFileServer() throws Throwable{
      
      SimpleConfig.setProperty(VoDescriptionServer.RESOURCE_PLUGIN_KEY, FileResourcePlugin.class.getName());
      //get non-existent file
      SimpleConfig.setProperty(FileResourcePlugin.METADATA_FILE_LOC_KEY, "doesntexist");
      VoDescriptionServer.clearCache(); //force reload
      try {
         Document metadata = VoDescriptionServer.getVoDescription();
         fail("Should have complained no metadata file");
      } catch (FileNotFoundException fnfe) {
         //ignore, supposed to happen
      }
      
      SimpleConfig.setProperty(FileResourcePlugin.METADATA_FILE_LOC_KEY, "org/astrogrid/datacenter/metadata/metadata.xml");
      VoDescriptionServer.clearCache(); //force reload
      Document metadata = VoDescriptionServer.getVoDescription();
      assertNotNull(metadata);
      assertIdentifiersOK(metadata);
   }
   
   public void testMetatdataUrlServer() throws Throwable{
      SimpleConfig.setProperty(VoDescriptionServer.RESOURCE_PLUGIN_KEY, UrlResourcePlugin.class.getName());

      SimpleConfig.setProperty(FileResourcePlugin.METADATA_URL_LOC_KEY, this.getClass().getResource("metadata.xml").toString());
      Document metadata = VoDescriptionServer.getVoDescription();
      assertNotNull(metadata);
      assertIdentifiersOK(metadata);
   }

   public void testRegistryDates() {
      Calendar ukcal = new GregorianCalendar(Locale.UK);
      Calendar uscal = new GregorianCalendar(Locale.US);
   }
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(MetadataTest.class);
   }
   
   
}



