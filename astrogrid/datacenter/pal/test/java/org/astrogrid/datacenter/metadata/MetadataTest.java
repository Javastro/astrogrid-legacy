/*$Id: MetadataTest.java,v 1.5 2004/10/25 00:49:17 jdt Exp $
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
import java.util.Locale;
import junit.framework.TestCase;
import org.astrogrid.applications.component.CEAComponentManagerFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.DsaDomHelper;
import org.astrogrid.datacenter.queriers.sql.RdbmsResourcePlugin;
import org.astrogrid.datacenter.queriers.test.SampleStarsPlugin;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * tests the metadata generators, etc
 *
 */
public class MetadataTest extends TestCase {
  
   public void setUp() {
      SampleStarsPlugin.initConfig();
   }
   
   /** Checks that the identifiers are there and valid */
   public void assertIdentifiersOK(Document candidate) {
      
      Element[] resources = DsaDomHelper.getChildrenByTagName(candidate.getDocumentElement(), "Resource");
      for (int r = 0; r < resources.length; r++) {
         String xsitype = resources[r].getAttribute("xsi:type");
         Element[] ids = DsaDomHelper.getChildrenByTagName(resources[r], "Identifier");
         assertTrue("Should only be one identifier tag", ids.length==1);

         for (int i = 0; i < ids.length; i++) {
            Element id = ids[i];
            String authId = DomHelper.getValue(id, "AuthorityID");
            String idConfig = SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY);
            assertEquals("Authority ID incorrect in Resource type "+xsitype, idConfig, authId);

            String resKey = DomHelper.getValue(id, "ResourceKey");
            String resConfig = SimpleConfig.getSingleton().getString(VoDescriptionServer.RESKEY_KEY);
            if (!xsitype.equals("AuthorityType")) {
               assertTrue("Resource Key is "+resKey+", should start with "+resConfig+" in Resource type "+xsitype, resKey.startsWith(resConfig));
            }
         }
      }
   }
   
   public void assertHasRdbmsResource(Document candidate) {
      
      //check for table data
      long numTables = candidate.getElementsByTagName("Table").getLength();
      assertEquals("Should be two tables in metadata", numTables, 2);
   }
   
   public void testServer() throws Exception {
      Document metadata = VoDescriptionServer.getVoDescription();
      //debug
      DomHelper.DocumentToStream(metadata, System.out);
      
      assertNotNull(metadata);
      assertIdentifiersOK(metadata);
      assertHasRdbmsResource(metadata);
   }
   
   public void testJdbc() throws Exception {
     
      SimpleConfig.setProperty(VoResourcePlugin.RESOURCE_PLUGIN_KEY, RdbmsResourcePlugin.class.getName());
      
      //generate metadata
      Document metaDoc = VoDescriptionServer.getVoDescription();
      
      //debug
      DomHelper.DocumentToStream(metaDoc, System.out);
      
      assertHasRdbmsResource(metaDoc);
      assertIdentifiersOK(metaDoc);
    }

    //checks we can get individual resources from the metadata
   public void testGetResouce() throws Exception {
      SimpleConfig.setProperty(VoResourcePlugin.RESOURCE_PLUGIN_KEY, RdbmsResourcePlugin.class.getName());
      
//not required      Element auth = VoDescriptionServer.getAuthorityResource();
//      assertNotNull("No AuthorityType in VOdescription", auth);

      Element rdbms = VoDescriptionServer.getResource("RdbmsMetadata");
      assertNotNull("No RdbmsMetadata in VODescription", rdbms);
   }

   /** Tests that the SampleStars generate metadata correctly */
   public void testSampleGenerator() throws Throwable{
      SampleStarsPlugin.initConfig();
      
      VoResourcePlugin plugin = new RdbmsResourcePlugin();
      String[] resources = plugin.getVoResources();
      StringBuffer resource = new StringBuffer(VoDescriptionServer.VODESCRIPTION_ELEMENT+"\n");
      for (int i = 0; i < resources.length; i++) {
         resource.append(resources[i]);
      }
      resource.append("\n</VODescription>");
      Document resourceDoc = DomHelper.newDocument(resource.toString());
      assertHasRdbmsResource(resourceDoc);
   }

   //checks that the document being returned by the CEA Library is OK
   public void testCeaLibrary() throws Throwable {
      String ceaVoDescription = CEAComponentManagerFactory.getInstance().getMetadataService().returnRegistryEntry();
      Document ceaDoc = DomHelper.newDocument(ceaVoDescription);
//      assertIdentifiersOK(ceaDoc);
   }
   
   public void testCeaResource() throws Throwable {
      SimpleConfig.setProperty(VoResourcePlugin.RESOURCE_PLUGIN_KEY, CeaResourceServer.class.getName());
      //generate metadata
      Document metaDoc = VoDescriptionServer.getVoDescription();
      
      assertIdentifiersOK(metaDoc);
   }
      
   public void testMetadataFileServer() throws Throwable{
      
      SimpleConfig.setProperty(VoResourcePlugin.RESOURCE_PLUGIN_KEY, FileResourcePlugin.class.getName());
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
      SimpleConfig.setProperty(VoResourcePlugin.RESOURCE_PLUGIN_KEY, UrlResourcePlugin.class.getName());

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


/*
 $Log: MetadataTest.java,v $
 Revision 1.5  2004/10/25 00:49:17  jdt
 Merges from branch PAL_MCH

 Revision 1.4.6.5  2004/10/22 09:13:37  mch
 Started on Registry GMT dates

 Revision 1.4.6.4  2004/10/20 22:19:19  mch
 Fix for checking authority type resource key

 Revision 1.4.6.3  2004/10/20 18:12:45  mch
 CEA fixes, resource tests and fixes, minor navigation changes

 Revision 1.4.6.2  2004/10/19 17:26:27  mch
 Odd fixes

 Revision 1.4.6.1  2004/10/19 14:01:31  mch
 Merged other PAL_MCH branches together

 Revision 1.4.4.1  2004/10/19 11:45:02  mch
 Started fixes to CEA metadata from DSAs

 Revision 1.4  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.3.2.1  2004/10/15 19:59:06  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.3  2004/10/08 17:14:23  mch
 Clearer separation of metadata and querier plugins, and improvements to VoResource plugin mechanisms

 Revision 1.2  2004/10/05 20:26:43  mch
 Prepared for better resource metadata generators

 Revision 1.1  2004/09/28 15:11:33  mch
 Moved server test directory to pal

 Revision 1.5  2004/09/08 20:15:10  mch
 Some fixes and cea metadata test

 Revision 1.4  2004/09/08 19:18:47  mch
 Minor fixes and tidy up

 Revision 1.3  2004/09/08 17:51:49  mch
 Fixes to log and metadata views

 Revision 1.2  2004/09/08 15:03:02  mch
 Added tests

 Revision 1.1  2004/09/06 20:23:00  mch
 Replaced metadata generators/servers with plugin mechanism. Added Authority plugin

 
 */
