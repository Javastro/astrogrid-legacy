/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 **/
package org.astrogrid.tableserver.metadata;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import junit.framework.TestCase;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.MetadataException;


/**
 * Tests the tabular metadoc loader.
 *
 * @author M Hill
 * @author K Andrews
 */
public class MetadocInterpreterTest extends TestCase {

   @Override
   protected void setUp() throws MetadataException, IOException {
      // Just to stop the TableMetaDocInterpreter complaining
      ConfigFactory.getCommonConfig().setProperty(
            "datacenter.querier.plugin", 
            "org.astrogrid.tableserver.test.SampleStarsPlugin");
      TableMetaDocInterpreter.clear();
      TableMetaDocInterpreter.initialize(
            getMetadocUrlFromFilename(
            "metadocs/good_metadoc.xml"));
   }

   protected URL getMetadocUrlFromFilename(String metadocFilename) 
         throws MalformedURLException {
       return new URL(MetadocInterpreterTest.class.getResource(
               metadocFilename).toString());
   }

   /** Tests for basic metadoc validity */
   public void testValidMetadoc() throws MetadataException, IOException
   {
      TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
            "metadocs/good_metadoc.xml"));
   }
   public void testBadMetadoc_DupCatID() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_DupCatID.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate catalog IDs should not be permitted");
   }
   public void testBadMetadoc_DupCatName() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_DupCatName.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate catalog Names should not be permitted");
   }
   public void testBadMetadoc_DupTableID() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_DupTableID.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate Table IDs in the same Catalog should not be permitted");
   }
   public void testBadMetadoc_DupTableName() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_DupTableName.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate Table Names in the same Catalog should not be permitted");
   }
   public void testBadMetadoc_DupColumnID() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_DupColumnID.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate Column IDs in the same Table should not be permitted");
   }
   public void testBadMetadoc_DupColumnName() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_DupColumnName.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Duplicate Column Names in the same Table should not be permitted");
   }
   public void testBadMetadoc_MissingCatalog() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_MissingCatalog.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with no Catalogs should not be permitted");
   }
   public void testBadMetadoc_MissingTable() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_MissingTable.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with Catalog with no Tables should not be permitted");
   }
   public void testBadMetadoc_MissingColumn() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_MissingColumn.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with Table with no Columns should not be permitted");
   }
   public void testBadMetadoc_ConeBadRA() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_ConeBadRA.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with misnamed conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeBadDec() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_ConeBadDec.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with misnamed conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeEmptyRA() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_ConeEmptyRA.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with blank conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeEmptyDec() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_ConeEmptyDec.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with blank conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeNoRA() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_ConeNoRA.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with missing conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeNoDec() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_ConeNoDec.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with missing conesearch column should not be permitted");
   }
   public void testBadMetadoc_ConeBadRAUnits() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_ConeBadRAUnits.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with bad conesearch RA units should not be permitted");
   }
   public void testBadMetadoc_ConeBadDecUnits() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_ConeBadDecUnits.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with bad conesearch Dec units should not be permitted");
   }
   public void testBadMetadoc_ConeNoRAUnits() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_ConeNoRAUnits.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with no conesearch RA units should not be permitted");
   }
   public void testBadMetadoc_ConeNoDecUnits() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_ConeNoDecUnits.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with no conesearch Dec units should not be permitted");
   }
   public void testBadMetadoc_ConeMismatchedUnits() throws IOException
   {
      try {
         TableMetaDocInterpreter.loadAndValidateMetadoc(
            getMetadocUrlFromFilename(
               "metadocs/bad_ConeMismatchedUnits.xml"));
      }
      catch (MetadataException me) {
         return;
      }
      fail("Metadoc with mismatched conesearch units should not be permitted");
   }


   /** Tests for getCatalogXXX methods */
   public void testGetCatalogIDs() throws MetadataException, IOException
   {
      String[] IDs = TableMetaDocInterpreter.getCatalogIDs();
      assertTrue(IDs.length == 3);
      assertTrue(IDs[0].equals("FIRST"));
      assertTrue(IDs[1].equals("SECOND"));
      assertTrue(IDs[2].equals("THIRD"));
   }
   public void testGetCatalogNames() throws MetadataException, IOException
   {
      String[] names = TableMetaDocInterpreter.getCatalogNames();
      assertTrue(names.length == 3);
      assertTrue(names[0].equals("CatName_FIRST"));
      assertTrue(names[1].equals("CatName_SECOND"));
      assertTrue(names[2].equals("CatName_THIRD"));
   }
   public void testGetCatalogDescriptions() throws MetadataException, IOException
   {
      String[] descs = TableMetaDocInterpreter.getCatalogDescriptions();
      assertTrue(descs.length == 3);
   }

   /** Tests for conesearchable table methods */
   public void testGetConesearchableTables1() throws MetadataException, IOException {
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
      String name = TableMetaDocInterpreter.getTableNameForID(
            "FIRST","catalogue1");
      assertTrue("TabName1_catalogue1".equals(name));
   }
   public void testGetTableNameForID_BadCat() throws MetadataException, IOException
   {
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
      String ID = TableMetaDocInterpreter.getTableIDForName(
            "CatName_FIRST","TabName1_catalogue1");
      assertTrue("catalogue1".equals(ID));
   }
   public void testGetTableIDforName_BadCat() throws MetadataException, IOException
   {
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
      String ID = TableMetaDocInterpreter.guessTableIDForName(
            "TabName1_catalogue1");
      assertTrue("catalogue1".equals(ID));
   }
   public void testGuessTableIDforName_Bad() throws MetadataException, IOException
   {
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
      String ID = TableMetaDocInterpreter.getColumnIDForName(
            "CatName_FIRST","TabName1_catalogue1","ColName_POS_EQ_DEC");
      assertTrue("POS_EQ_DEC".equals(ID));
   }
   public void testGetColumnIDForName_BadCat() throws MetadataException, IOException
   {
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
      String name = TableMetaDocInterpreter.getColumnNameForID(
            "FIRST","catalogue1","POS_EQ_DEC");
      assertTrue("ColName_POS_EQ_DEC".equals(name));
   }
   public void testGetColumnNameForID_BadCat() throws MetadataException, IOException
   {
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
      String name = TableMetaDocInterpreter.getConeRAColumnByName(
            "CatName_FIRST","TabName1_catalogue1");
      assertTrue("ColName1_POS_EQ_RA".equals(name));
   }
   public void testGetConeDecColumnByName() throws MetadataException, IOException 
   {
      String name = TableMetaDocInterpreter.getConeDecColumnByName(
            "CatName_FIRST","TabName1_catalogue1");
      assertTrue("ColName_POS_EQ_DEC".equals(name));
   }
   public void testGetConeUnitsByName() throws MetadataException, IOException 
   {
      String units = TableMetaDocInterpreter.getConeUnitsByName(
            "CatName_FIRST","TabName1_catalogue1");
      assertTrue("deg".equals(units));
   }
   public void testGetConeRAColumnByName_Bad() throws MetadataException, IOException 
   {
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
      ColumnInfo colInfo = TableMetaDocInterpreter.guessColumn(
            null, "PHOT_FLUX_PEAK_UNIQUE");

      assertTrue(colInfo.getGroupName().equals("TabName1_catalogue1"));
      assertTrue(colInfo.getGroupID().equals("catalogue1"));
      assertTrue(colInfo.getName().equals("ColName_PHOT_FLUX_PEAK_UNIQUE"));
      assertTrue(colInfo.getId().equals("PHOT_FLUX_PEAK_UNIQUE"));
   }
   public void testGuessColumnNoTableNotUnique() throws MetadataException, IOException
   {
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



