/*$Id: MetadataTest.java,v 1.7 2005/03/10 20:19:21 mch Exp $
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
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.FileResourcePlugin;
import org.astrogrid.dataservice.metadata.UrlResourcePlugin;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.queryable.ConeConfigQueryableResource;
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.tableserver.jdbc.RdbmsTableMetaDocGenerator;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
         Element[] idNodes = DomHelper.getChildrenByTagName(resources[r], "identifier");
         assertEquals("identifier tags", 1, idNodes.length);

         Element idNode = idNodes[0];
         String id = DomHelper.getValueOf(idNode);
         String configAuth = SimpleConfig.getSingleton().getString(VoResourceSupport.AUTHID_KEY);
         assertTrue("identity doesn't start with config'd authority '"+configAuth+"'", id.startsWith(configAuth));
            
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
   
   public void testSpatial() throws IOException {
      ConeConfigQueryableResource conequeryable = new ConeConfigQueryableResource();
      SearchGroup[] groups = conequeryable.getSpatialGroups();
      for (int i = 0; i < groups.length; i++) {
         conequeryable.getSpatialFields(groups[i]);
      }
   }
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(MetadataTest.class);
   }
   
   
}



