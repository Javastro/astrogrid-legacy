/*$Id: MetadataTest.java,v 1.2 2004/09/08 15:03:02 mch Exp $
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
import java.io.FileOutputStream;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.sql.JdbcPlugin;
import org.astrogrid.datacenter.queriers.test.SampleStarsPlugin;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * tests the metadata generators, etc
 *
 */
public class MetadataTest extends TestCase {
   
   public void assertHasAuthorityResource(Document candidate) {
      
      //check for authority id
      Element id = (Element) candidate.getElementsByTagName("Identifier").item(0);
      String authorityId = DomHelper.getValue(id, "AuthorityID");
   }
   
   public void assertHasRdbmsResource(Document candidate) {
      
      //check for table data
      long numTables = candidate.getElementsByTagName("Table").getLength();
      assertEquals("Should be two tables in metadata", numTables, 2);
   }
   
   public void testServer() throws Exception {
      SampleStarsPlugin.initConfig();
      Document metadata = VoDescriptionServer.getVoDescription();
      //debug
      DomHelper.DocumentToStream(metadata, System.out);
      
      assertNotNull(metadata);
      assertHasAuthorityResource(metadata);
      assertHasRdbmsResource(metadata);
   }
   
   public void testJdbc() throws Exception {
     
      SimpleConfig.setProperty("datacenter.metadata.plugin", JdbcPlugin.class.getName());
      
      //generate metadata
      Document metaDoc = VoDescriptionServer.getVoDescription();
      
      //debug
      DomHelper.DocumentToStream(metaDoc, System.out);
      
      assertHasRdbmsResource(metaDoc);
   }

   public void testGetResouce() throws Exception {
      SimpleConfig.setProperty("datacenter.metadata.plugin", JdbcPlugin.class.getName());
      
      Element auth = VoDescriptionServer.getAuthorityResource();
      assertNotNull(auth);

      Element rdbms = VoDescriptionServer.getResource("RdbmsMetadata");
      assertNotNull(auth);
   }
   
   public void testMetatdataFileServer() throws Throwable{
      
      SimpleConfig.setProperty("datacenter.metadata.plugin", FileResourcePlugin.class.getName());
      //get non-existent file
      SimpleConfig.setProperty(FileResourcePlugin.METADATA_FILE_LOC_KEY, "doesntexist");
      VoDescriptionServer.clearCache(); //force reload
      try {
         Document metadata = VoDescriptionServer.getVoDescription();
         fail("Should have complained no metadata file");
      } catch (FileNotFoundException fnfe) {
         //ignore, supposed to happen
      }
      SimpleConfig.setProperty(FileResourcePlugin.METADATA_FILE_LOC_KEY, null);
      
      SimpleConfig.setProperty(FileResourcePlugin.METADATA_URL_LOC_KEY, this.getClass().getResource("metadata.xml").toString());
      Document metadata = VoDescriptionServer.getVoDescription();
      assertNotNull(metadata);
      
      SimpleConfig.setProperty(FileResourcePlugin.METADATA_FILE_LOC_KEY, "org/astrogrid/datacenter/metadata/metadata.xml");
      SimpleConfig.setProperty(FileResourcePlugin.METADATA_URL_LOC_KEY, null);
      FileResourcePlugin plugin = (FileResourcePlugin) VoDescriptionServer.createPlugin(FileResourcePlugin.class.getName());
      assertNotNull(plugin.getVoResource());
   }
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(MetadataTest.class);
   }
   
   
}


/*
 $Log: MetadataTest.java,v $
 Revision 1.2  2004/09/08 15:03:02  mch
 Added tests

 Revision 1.1  2004/09/06 20:23:00  mch
 Replaced metadata generators/servers with plugin mechanism. Added Authority plugin

 
 */
