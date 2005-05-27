/*$Id: MetadataTest.java,v 1.2 2005/05/27 16:21:02 clq2 Exp $
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import junit.framework.TestCase;
import org.astrogrid.cfg.ConfigFactory;
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
         String configAuth = ConfigFactory.getCommonConfig().getString(VoResourceSupport.AUTHID_KEY);
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
      TableMetaDocInterpreter reader = new TableMetaDocInterpreter();
   }
   
   
   public void testServer() throws Exception {
      Document metadata = VoDescriptionServer.getVoDescription();
      //debug
      DomHelper.DocumentToStream(metadata, System.out);
      
      assertNotNull(metadata);
      assertIdentifiersOK(metadata);
      
      //make sure its well-formed - strange idea given it's already an object model, but there may be some namespace bugs
      String s = DomHelper.DocumentToString(metadata);
      
      DomHelper.newDocument(s);
   }
   
   /** Tests the resource generator */
   public void testGenerator() throws Exception {
     
      RdbmsTableMetaDocGenerator generator = new RdbmsTableMetaDocGenerator();

      //generate metadata
      String metadoc = generator.getMetaDoc();

      //check it's well formed
      Document metaDoc = DomHelper.newDocument(metadoc);
      
      //debug
      DomHelper.DocumentToStream(metaDoc, System.out);
    }

   public void testMetadataFileServer() throws Throwable{
      
      ConfigFactory.getCommonConfig().setProperty(VoDescriptionServer.RESOURCE_PLUGIN_KEY, FileResourcePlugin.class.getName());
      //get non-existent file
      ConfigFactory.getCommonConfig().setProperty(FileResourcePlugin.METADATA_FILE_LOC_KEY, "doesntexist");
      VoDescriptionServer.clearCache(); //force reload
      try {
         Document metadata = VoDescriptionServer.getVoDescription();
         fail("Should have complained no metadata file");
      } catch (FileNotFoundException fnfe) {
         //ignore, supposed to happen
      }
      
      ConfigFactory.getCommonConfig().setProperty(FileResourcePlugin.METADATA_FILE_LOC_KEY, "org/astrogrid/tableserver/metadata/sample-metadata.xml");
      VoDescriptionServer.clearCache(); //force reload
      Document metadata = VoDescriptionServer.getVoDescription();
      assertNotNull(metadata);
      assertIdentifiersOK(metadata);
   }
   
   public void testMetatdataUrlServer() throws Throwable{
      ConfigFactory.getCommonConfig().setProperty(VoDescriptionServer.RESOURCE_PLUGIN_KEY, UrlResourcePlugin.class.getName());

      ConfigFactory.getCommonConfig().setProperty(FileResourcePlugin.METADATA_URL_LOC_KEY, MetadataTest.class.getResource("sample-metadata.xml").toString());
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



