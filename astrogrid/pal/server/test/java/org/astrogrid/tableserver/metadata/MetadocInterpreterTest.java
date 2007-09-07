/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 **/
package org.astrogrid.tableserver.metadata;

import java.io.IOException;
import java.net.URL;
import junit.framework.TestCase;

import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.ConfigReader;

import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;


/**
 * Tests the tabular metadoc loader.
 *
 * @author M Hill
 * @author K Andrews
 */
public class MetadocInterpreterTest extends TestCase {
  
   protected void setUp() {
      // Just to stop the TableMetaDocInterpreter complaining
      ConfigFactory.getCommonConfig().setProperty(
            "datacenter.querier.plugin", 
            "org.astrogrid.tableserver.test.SampleStarsPlugin");
   }
   private void setMetadocUrl(String metadocFilename) throws IOException
   {
      ConfigFactory.getCommonConfig().setProperty(
            TableMetaDocInterpreter.TABLE_METADOC_URL_KEY, 
            MetadocInterpreterTest.class.getResource(
               metadocFilename).toString());
   }

   /** Tests for basic metadoc validity */
   public void testValidMetadoc() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      // Force metadoc (re-)initialization
      TableMetaDocInterpreter.initialize(true);
      assertTrue(TableMetaDocInterpreter.isValid());
   }
   public void testBadMetadoc_DupCatID() throws IOException
   {
      setMetadocUrl("metadocs/bad_DupCatID.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate catalog IDs should not be permitted");
   }
   public void testBadMetadoc_DupCatName() throws IOException
   {
      setMetadocUrl("metadocs/bad_DupCatName.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate catalog Names should not be permitted");
   }
   public void testBadMetadoc_DupTableID() throws IOException
   {
      setMetadocUrl("metadocs/bad_DupTableID.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate Table IDs in the same Catalog should not be permitted");
   }
   public void testBadMetadoc_DupTableName() throws IOException
   {
      setMetadocUrl("metadocs/bad_DupTableName.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate Table Names in the same Catalog should not be permitted");
   }
   public void testBadMetadoc_DupColumnID() throws IOException
   {
      setMetadocUrl("metadocs/bad_DupColumnID.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate Column IDs in the same Table should not be permitted");
   }
   public void testBadMetadoc_DupColumnName() throws IOException
   {
      setMetadocUrl("metadocs/bad_DupColumnName.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate Column Names in the same Table should not be permitted");
   }
   public void testBadMetadoc_MissingCatalog() throws IOException
   {
      setMetadocUrl("metadocs/bad_MissingCatalog.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with no Catalogs should not be permitted");
   }
   public void testBadMetadoc_MissingTable() throws IOException
   {
      setMetadocUrl("metadocs/bad_MissingTable.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with Catalog with no Tables should not be permitted");
   }
   public void testBadMetadoc_MissingColumn() throws IOException
   {
      setMetadocUrl("metadocs/bad_MissingColumn.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with Table with no Columns should not be permitted");
   }
   public void testBadMetadoc_ConeBadRA() throws IOException
   {
      setMetadocUrl("metadocs/bad_ConeBadRA.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with misnamed conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeBadDec() throws IOException
   {
      setMetadocUrl("metadocs/bad_ConeBadDec.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with misnamed conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeEmptyRA() throws IOException
   {
      setMetadocUrl("metadocs/bad_ConeEmptyRA.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with blank conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeEmptyDec() throws IOException
   {
      setMetadocUrl("metadocs/bad_ConeEmptyDec.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with blank conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeNoRA() throws IOException
   {
      setMetadocUrl("metadocs/bad_ConeNoRA.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with missing conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeNoDec() throws IOException
   {
      setMetadocUrl("metadocs/bad_ConeNoDec.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with missing conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeBadRAUnits() throws IOException
   {
      setMetadocUrl("metadocs/bad_ConeBadRAUnits.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with bad conesearch RA units should not be permitted");
   }
   public void testBadMetadoc_ConeBadDecUnits() throws IOException
   {
      setMetadocUrl("metadocs/bad_ConeBadDecUnits.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with bad conesearch Dec units should not be permitted");
   }
   public void testBadMetadoc_ConeNoRAUnits() throws IOException
   {
      setMetadocUrl("metadocs/bad_ConeNoRAUnits.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with no conesearch RA units should not be permitted");
   }
   public void testBadMetadoc_ConeNoDecUnits() throws IOException
   {
      setMetadocUrl("metadocs/bad_ConeNoDecUnits.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with no conesearch Dec units should not be permitted");
   }
   public void testBadMetadoc_ConeMismatchedUnits() throws IOException
   {
      setMetadocUrl("metadocs/bad_ConeMismatchedUnits.xml");
      try {
         TableMetaDocInterpreter.initialize(true);
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with mismatched conesearch units should not be permitted");
   }

   /** Tests for getCatalogXXX methods */
   public void testGetCatalogIDs() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      String[] IDs = TableMetaDocInterpreter.getCatalogIDs();
      assertTrue(IDs.length == 3);
      assertTrue(IDs[0].equals("FIRST"));
      assertTrue(IDs[1].equals("SECOND"));
      assertTrue(IDs[2].equals("THIRD"));
   }
   public void testGetCatalogNames() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      String[] names = TableMetaDocInterpreter.getCatalogNames();
      assertTrue(names.length == 3);
      assertTrue(names[0].equals("CatName_FIRST"));
      assertTrue(names[1].equals("CatName_SECOND"));
      assertTrue(names[2].equals("CatName_THIRD"));
   }
   public void testGetCatalogDescriptions() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      String[] descs = TableMetaDocInterpreter.getCatalogDescriptions();
      assertTrue(descs.length == 3);
   }

   /** Tests for conesearchable table methods */
   public void testGetConesearchableTables1() throws MetadataException, IOException {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      TableInfo[] tableInfos = 
         TableMetaDocInterpreter.getConesearchableTables();
      assertTrue(tableInfos != null);
      assertTrue(tableInfos.length == 3);

      // Sanitycheck the first one
      assertTrue(tableInfos[0].getCatalogName().equals("CatName_FIRST"));
      assertTrue(tableInfos[0].getCatalogID().equals("FIRST"));
      assertTrue(tableInfos[0].getName().equals("TabName1_catalogue1"));
      assertTrue(tableInfos[0].getId().equals("catalogue1"));
      assertTrue(tableInfos[0].getConesearchable() == true);
      assertTrue(tableInfos[0].getConeRAColName().equals("ColName1_POS_EQ_RA"));
      assertTrue(tableInfos[0].getConeDecColName().equals("ColName_POS_EQ_DEC"));
   } 
   public void testGetConesearchableTables2() throws MetadataException, IOException {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      TableInfo[] tableInfos = 
         TableMetaDocInterpreter.getConesearchableTables("FIRST");
      assertTrue(tableInfos != null);
      assertTrue(tableInfos.length == 2);

      // Sanitycheck the first one
      assertTrue(tableInfos[0].getCatalogName().equals("CatName_FIRST"));
      assertTrue(tableInfos[0].getCatalogID().equals("FIRST"));
      assertTrue(tableInfos[0].getName().equals("TabName1_catalogue1"));
      assertTrue(tableInfos[0].getId().equals("catalogue1"));
      assertTrue(tableInfos[0].getConesearchable() == true);
      assertTrue(tableInfos[0].getConeRAColName().equals("ColName1_POS_EQ_RA"));
      assertTrue(tableInfos[0].getConeDecColName().equals("ColName_POS_EQ_DEC"));

      tableInfos = TableMetaDocInterpreter.getConesearchableTables("SECOND");
      assertTrue(tableInfos != null);
      assertTrue(tableInfos.length == 1);

      tableInfos = TableMetaDocInterpreter.getConesearchableTables("THIRD");
      assertTrue(tableInfos != null);
      assertTrue(tableInfos.length == 0);
   } 

   /** Tests for getTableInfoByID() */
   public void testGetTableInfoByID() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      TableInfo info = TableMetaDocInterpreter.getTableInfoByID(
            "FIRST","catalogue1");
      assertTrue(info != null);
      assertTrue(info.getCatalogName().equals("CatName_FIRST"));
      assertTrue(info.getCatalogID().equals("FIRST"));
      assertTrue(info.getName().equals("TabName1_catalogue1"));
      assertTrue(info.getId().equals("catalogue1"));
   }
   public void testGetTableInfoByID_BadCat() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         TableInfo info = TableMetaDocInterpreter.getTableInfoByID(
               "FIRST_FOO","catalogue1");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad catalog ID should cause failure");
   }
   public void testGetTableInfoByID_BadTable() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         TableInfo info = TableMetaDocInterpreter.getTableInfoByID(
               "FIRST","catalogue1_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad table ID should cause failure");
   }


   /** Tests for getTableInfoByName() */
   public void testGetTableInfoByName() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      TableInfo info = TableMetaDocInterpreter.getTableInfoByName(
            "CatName_FIRST","TabName1_catalogue1");
      assertTrue(info != null);
      assertTrue(info.getCatalogName().equals("CatName_FIRST"));
      assertTrue(info.getCatalogID().equals("FIRST"));
      assertTrue(info.getName().equals("TabName1_catalogue1"));
      assertTrue(info.getId().equals("catalogue1"));
   }
   public void testGetTableInfoByName_BadCat() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         TableInfo info = TableMetaDocInterpreter.getTableInfoByName(
            "CatName_FIRST_FOO","TabName1_catalogue1");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad catalog ID should cause failure");
   }
   public void testGetTableInfoByName_BadTable() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         TableInfo info = TableMetaDocInterpreter.getTableInfoByName(
            "CatName_FIRST","TabName1_catalogue1_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad table ID should cause failure");
   }


   /** Tests for getTableNameForID() */
   public void testGetTableNameForID() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      String name = TableMetaDocInterpreter.getTableNameForID(
            "FIRST","catalogue1");
      assertTrue("TabName1_catalogue1".equals(name));
   }
   public void testGetTableNameForID_BadCat() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String name = TableMetaDocInterpreter.getTableNameForID(
               "FIRST_FOO","catalogue1");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad catalog ID should cause failure");
   }
   public void testGetTableNameForID_BadTable() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String name = TableMetaDocInterpreter.getTableNameForID(
               "FIRST","catalogue1_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad table ID should cause failure");
   }


   /** Tests for getTableIDForName() */
   public void testGetTableIDforName() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      String ID = TableMetaDocInterpreter.getTableIDForName(
            "CatName_FIRST","TabName1_catalogue1");
      assertTrue("catalogue1".equals(ID));
   }
   public void testGetTableIDforName_BadCat() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String ID = TableMetaDocInterpreter.getTableIDForName(
               "CatName_FIRST_FOO","TabName1_catalogue1");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad catalog Name should cause failure");
   }
   public void testGetTableIDforName_BadTable() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String ID = TableMetaDocInterpreter.getTableIDForName(
               "CatName_FIRST","TabName1_catalogue1_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad table Name should cause failure");
   }


   /** Tests for guessTableIDForName() */
   public void testGuessTableIDforName() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      String ID = TableMetaDocInterpreter.guessTableIDForName(
            "TabName1_catalogue1");
      assertTrue("catalogue1".equals(ID));
   }
   public void testGuessTableIDforName_Bad() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String ID = TableMetaDocInterpreter.guessTableIDForName(
               "TabName_catalogue");   //There are two of these
      }
      catch (MetadataException me) {
         return;
      }
      fail("Guessing multiply-occurring table Name should cause failure");
   }


   /** Tests for getTablesInfo functions() */
   public void testGetTablesInfoByID() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      TableInfo[] tables = TableMetaDocInterpreter.getTablesInfoByID("FIRST");
      assertTrue(tables.length == 2);
      // Some further checks
      assertTrue(tables[0].getCatalogName().equals("CatName_FIRST"));
      assertTrue(tables[0].getCatalogID().equals("FIRST"));
      assertTrue(tables[0].getName().equals("TabName1_catalogue1"));
      assertTrue(tables[0].getId().equals("catalogue1"));
      assertTrue(tables[1].getCatalogName().equals("CatName_FIRST"));
      assertTrue(tables[1].getCatalogID().equals("FIRST"));
      assertTrue(tables[1].getName().equals("TabName1_catalogue2"));
      assertTrue(tables[1].getId().equals("catalogue2"));
   }
   public void testGetTablesInfoByID_Bad() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         TableInfo[] tables = TableMetaDocInterpreter.getTablesInfoByID(
               "FIRST_FOO"); 
      }
      catch (MetadataException me) {
         return;
      }
      fail("Using non-existent catalog ID should cause failure");
   }
   public void testGetTablesInfoByName() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      TableInfo[] tables = 
         TableMetaDocInterpreter.getTablesInfoByName("CatName_FIRST");
      assertTrue(tables.length == 2);
      // Some further checks
      assertTrue(tables[0].getCatalogName().equals("CatName_FIRST"));
      assertTrue(tables[0].getCatalogID().equals("FIRST"));
      assertTrue(tables[0].getName().equals("TabName1_catalogue1"));
      assertTrue(tables[0].getId().equals("catalogue1"));
      assertTrue(tables[1].getCatalogName().equals("CatName_FIRST"));
      assertTrue(tables[1].getCatalogID().equals("FIRST"));
      assertTrue(tables[1].getName().equals("TabName1_catalogue2"));
      assertTrue(tables[1].getId().equals("catalogue2"));
   }
   public void testGetTablesInfoByName_Bad() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         TableInfo[] tables = TableMetaDocInterpreter.getTablesInfoByName(
               "CatName_FIRST_FOO"); 
      }
      catch (MetadataException me) {
         return;
      }
      fail("Using non-existent catalog Name should cause failure");
   }


   /** Tests for getColumnInfoByID() */
   /* TOFIX maybe add some more field tests here? */
   public void testGetColumnInfoByID() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      ColumnInfo info = TableMetaDocInterpreter.getColumnInfoByID(
            "FIRST","catalogue1", "POS_EQ_DEC");
      assertTrue(info != null);
      assertTrue(info.getGroupName().equals("TabName1_catalogue1"));
      assertTrue(info.getGroupID().equals("catalogue1"));
      assertTrue(info.getName().equals("ColName_POS_EQ_DEC"));
      assertTrue(info.getId().equals("POS_EQ_DEC"));
   }
   public void testGetColumnInfoByID_BadCat() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         ColumnInfo info = TableMetaDocInterpreter.getColumnInfoByID(
            "FIRST_FOO","catalogue1", "POS_EQ_DEC");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad catalog ID should cause failure");
   }
   public void testGetColumnInfoByID_BadTable() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         ColumnInfo info = TableMetaDocInterpreter.getColumnInfoByID(
            "FIRST","catalogue1_FOO", "POS_EQ_DEC");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad table ID should cause failure");
   }
   public void testGetColumnInfoByID_BadCol() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         ColumnInfo info = TableMetaDocInterpreter.getColumnInfoByID(
            "FIRST","catalogue1", "POS_EQ_DEC_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad column ID should cause failure");
   }

   /** Tests for getColumnInfoByName() */
   /* TOFIX maybe add some more field tests here? */
   public void testGetColumnInfoByName() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      ColumnInfo info = TableMetaDocInterpreter.getColumnInfoByName(
            "CatName_FIRST","TabName1_catalogue1", "ColName_POS_EQ_DEC");
      assertTrue(info != null);
      assertTrue(info.getGroupName().equals("TabName1_catalogue1"));
      assertTrue(info.getGroupID().equals("catalogue1"));
      assertTrue(info.getName().equals("ColName_POS_EQ_DEC"));
      assertTrue(info.getId().equals("POS_EQ_DEC"));
   }
   public void testGetColumnInfoByName_BadCat() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         ColumnInfo info = TableMetaDocInterpreter.getColumnInfoByName(
            "CatName_FIRST_FOO","TabName1_catalogue1", "ColName_POS_EQ_DEC");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad catalog Name should cause failure");
   }
   public void testGetColumnInfoByName_BadTable() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         ColumnInfo info = TableMetaDocInterpreter.getColumnInfoByName(
            "CatName_FIRST","TabName1_catalogue1_FOO", "ColName_POS_EQ_DEC");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad table Name should cause failure");
   }
   public void testGetColumnInfoByName_BadCol() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         ColumnInfo info = TableMetaDocInterpreter.getColumnInfoByID(
            "FIRST","catalogue1", "POS_EQ_DEC_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad column Name should cause failure");
   }


   /** Tests for getColumnIDForName() */
   public void testGetColumnIDForName() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      String ID = TableMetaDocInterpreter.getColumnIDForName(
            "CatName_FIRST","TabName1_catalogue1","ColName_POS_EQ_DEC");
      assertTrue("POS_EQ_DEC".equals(ID));
   }
   public void testGetColumnIDForName_BadCat() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String ID = TableMetaDocInterpreter.getColumnIDForName(
               "CatName_FIRST_FOO","TabName1_catalogue1","ColName_POS_EQ_DEC");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad catalog Name should cause failure");
   }
   public void testGetColumnIDForName_BadTable() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String ID = TableMetaDocInterpreter.getColumnIDForName(
               "CatName_FIRST","TabName1_catalogue1_FOO","ColName_POS_EQ_DEC");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad table Name should cause failure");
   }
   public void testGetColumnIDForName_BadColumn() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String ID = TableMetaDocInterpreter.getColumnIDForName(
               "CatName_FIRST","TabName1_catalogue1","ColName_POS_EQ_DEC_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad column Name should cause failure");
   }



   /** Tests for getColumnNameForID() */
   public void testGetColumnNameForID() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      String name = TableMetaDocInterpreter.getColumnNameForID(
            "FIRST","catalogue1","POS_EQ_DEC");
      assertTrue("ColName_POS_EQ_DEC".equals(name));
   }
   public void testGetColumnNameForID_BadCat() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String name = TableMetaDocInterpreter.getColumnNameForID(
               "FIRST_FOO","catalogue1","POS_EQ_DEC");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad catalog ID should cause failure");
   }
   public void testGetColumnNameForID_BadTable() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String name = TableMetaDocInterpreter.getColumnNameForID(
               "FIRST","catalogue1_FOO","POS_EQ_DEC");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad table ID should cause failure");
   }
   public void testGetColumnNameForID_BadColumn() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String name = TableMetaDocInterpreter.getColumnNameForID(
               "FIRST","catalogue1","POS_EQ_DEC_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Bad column ID should cause failure");
   }

   /** Tests for conesearch-related stuff  */
   public void testGetConeRAColumnByName() throws MetadataException, IOException 
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);
      String name = TableMetaDocInterpreter.getConeRAColumnByName(
            "CatName_FIRST","TabName1_catalogue1");
      assertTrue("ColName1_POS_EQ_RA".equals(name));
   }
   public void testGetConeDecColumnByName() throws MetadataException, IOException 
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);
      String name = TableMetaDocInterpreter.getConeDecColumnByName(
            "CatName_FIRST","TabName1_catalogue1");
      assertTrue("ColName_POS_EQ_DEC".equals(name));
   }
   public void testGetConeUnitsByName() throws MetadataException, IOException 
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);
      String units = TableMetaDocInterpreter.getConeUnitsByName(
            "CatName_FIRST","TabName1_catalogue1");
      assertTrue("deg".equals(units));
   }
   public void testGetConeRAColumnByName_Bad() throws MetadataException, IOException 
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);
      try {
         String name = TableMetaDocInterpreter.getConeRAColumnByName(
            "CatName_THIRD","TabName_catalogue");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Using non-cone table should cause failure");
   }
   public void testGetConeDecColumnByName_Bad() throws MetadataException, IOException 
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);
      try {
         String name = TableMetaDocInterpreter.getConeDecColumnByName(
            "CatName_THIRD","TabName_catalogue");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Using non-cone table should cause failure");
   }
   public void testGetConeUnitsByName_Bad() throws MetadataException, IOException 
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);
      try {
         String name = TableMetaDocInterpreter.getConeUnitsByName(
            "CatName_THIRD","TabName_catalogue");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Using non-cone table should cause failure");
   }

   /** Tests for getColumnsInfo methods */
   public void testGetColumnsInfoByID() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      ColumnInfo[] columns = TableMetaDocInterpreter.getColumnsInfoByID(
            "FIRST", "catalogue1");
      assertTrue(columns.length == 13);
      // Some further checks
      assertTrue(columns[1].getGroupName().equals("TabName1_catalogue1"));
      assertTrue(columns[1].getGroupID().equals("catalogue1"));
      assertTrue(columns[1].getName().equals("ColName_POS_EQ_DEC"));
      assertTrue(columns[1].getId().equals("POS_EQ_DEC"));
   }
   public void testGetColumnsInfoByID_BadCat() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         ColumnInfo[] columns = TableMetaDocInterpreter.getColumnsInfoByID(
            "FIRST_FOO", "catalogue1");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Using non-existent catalog ID should cause failure");
   }
   public void testGetColumnsInfoByID_BadTable() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         ColumnInfo[] columns = TableMetaDocInterpreter.getColumnsInfoByID(
            "FIRST", "catalogue1_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Using non-existent table ID should cause failure");
   }
   public void testGetColumnsInfoByName() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      ColumnInfo[] columns = TableMetaDocInterpreter.getColumnsInfoByName(
            "CatName_FIRST", "TabName1_catalogue1");
      assertTrue(columns.length == 13);
      // Some further checks
      assertTrue(columns[1].getGroupName().equals("TabName1_catalogue1"));
      assertTrue(columns[1].getGroupID().equals("catalogue1"));
      assertTrue(columns[1].getName().equals("ColName_POS_EQ_DEC"));
      assertTrue(columns[1].getId().equals("POS_EQ_DEC"));
   }
   public void testGetColumnsInfoByName_BadCat() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         ColumnInfo[] columns = TableMetaDocInterpreter.getColumnsInfoByName(
            "CatName_FIRST_FOO", "TabName1_catalogue1");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Using non-existent catalog ID should cause failure");
   }
   public void testGetColumnsInfoByName_BadTable() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         ColumnInfo[] columns = TableMetaDocInterpreter.getColumnsInfoByName(
            "CatName_FIRST", "TabName1_catalogue1_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Using non-existent table ID should cause failure");
   }

   /** Tests for guessColumn() */
   public void testGuessColumnNoTableUnique() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      String[] tables = {};
      ColumnInfo colInfo = TableMetaDocInterpreter.guessColumn(
            tables, "PHOT_FLUX_PEAK_UNIQUE");

      assertTrue(colInfo.getGroupName().equals("TabName1_catalogue1"));
      assertTrue(colInfo.getGroupID().equals("catalogue1"));
      assertTrue(colInfo.getName().equals("ColName_PHOT_FLUX_PEAK_UNIQUE"));
      assertTrue(colInfo.getId().equals("PHOT_FLUX_PEAK_UNIQUE"));
   }
   public void testGuessColumnNullTableUnique() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      ColumnInfo colInfo = TableMetaDocInterpreter.guessColumn(
            null, "PHOT_FLUX_PEAK_UNIQUE");

      assertTrue(colInfo.getGroupName().equals("TabName1_catalogue1"));
      assertTrue(colInfo.getGroupID().equals("catalogue1"));
      assertTrue(colInfo.getName().equals("ColName_PHOT_FLUX_PEAK_UNIQUE"));
      assertTrue(colInfo.getId().equals("PHOT_FLUX_PEAK_UNIQUE"));
   }
   public void testGuessColumnNoTableNotUnique() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String[] tables = {};
         ColumnInfo colInfo = TableMetaDocInterpreter.guessColumn(
               tables, "PHOT_FLUX_INT");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Guessing multiply-occurring column ID should cause failure");
   }
   public void testGuessColumnNoTableMissing() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String[] tables = {};
         ColumnInfo colInfo = TableMetaDocInterpreter.guessColumn(
               tables, "PHOT_FLUX_INT_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Guessing missing column ID should cause failure");
   }
   public void testGuessColumnWithTableUnique() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      String[] tables = { "TabName1_catalogue1", "TabName1_catalogue2" };
      ColumnInfo colInfo = TableMetaDocInterpreter.guessColumn(
            tables, "PHOT_FLUX_PEAK_UNIQUE");

      assertTrue(colInfo.getGroupName().equals("TabName1_catalogue1"));
      assertTrue(colInfo.getGroupID().equals("catalogue1"));
      assertTrue(colInfo.getName().equals("ColName_PHOT_FLUX_PEAK_UNIQUE"));
      assertTrue(colInfo.getId().equals("PHOT_FLUX_PEAK_UNIQUE"));
   }
   public void testGuessColumnWithTableNotUnique() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         // This example uses tables from different catalogs 
         String[] tables = { "TabName_catalogue", "TabName1_catalogue2" };
         ColumnInfo colInfo = TableMetaDocInterpreter.guessColumn(
               tables, "PHOT_FLUX_PEAK");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Guessing multiply-occurring column ID should cause failure");
   }
   public void testGuessColumnWithTableMissing() throws MetadataException, IOException
   {
      setMetadocUrl("metadocs/good_metadoc.xml");
      TableMetaDocInterpreter.initialize(true);

      try {
         String[] tables = { "TabName1_catalogue1", "TabName1_catalogue2" };
         ColumnInfo colInfo = TableMetaDocInterpreter.guessColumn(
               tables, "PHOT_FLUX_INT_FOO");
      }
      catch (MetadataException me) {
         return;
      }
      fail("Guessing missing column ID should cause failure");
   }


   public static void main(String[] args) {
      junit.textui.TestRunner.run(MetadataTest.class);
   }
   
   
}



