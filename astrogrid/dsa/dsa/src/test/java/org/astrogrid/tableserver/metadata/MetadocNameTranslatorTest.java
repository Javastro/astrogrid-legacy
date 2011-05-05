package org.astrogrid.tableserver.metadata;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Guy Rixon
 */
public class MetadocNameTranslatorTest {

  @Before
  public void setUp() throws Exception {
    SimpleConfig.setProperty("datacenter.cache.directory", "target");
    Job.initialize();
    SampleStarsPlugin.initConfig();
    TableMetaDocInterpreter.clear();
    TableMetaDocInterpreter.initialize();
  }

  @Test
  public void testColNameRA() throws Exception {
    MetadocNameTranslator sut = new MetadocNameTranslator();
    String name = sut.getColumnRealname("CatName_SampleStarsCat", "TabName_SampleStars", "ColName_RA");
    assertEquals("RA columns has wrong ID", "RA", name);
  }

  @Test(expected = MetadataException.class)
  public void testColNameRaBadCatalogueName() throws Exception {
    MetadocNameTranslator sut = new MetadocNameTranslator();
    String name = sut.getColumnRealname("CatName_SampleStars", "TabName_SampleStars", "ColName_RA");
  }

  @Test(expected = MetadataException.class)
  public void testColNameRaBadTableName() throws Exception {
    MetadocNameTranslator sut = new MetadocNameTranslator();
    String name = sut.getColumnRealname("CatName_SampleStarsCat", "TableName_SampleStars", "ColName_RA");
  }

  @Test
  public void testCatalogueName() throws Exception {
    MetadocNameTranslator sut = new MetadocNameTranslator();
    String name = sut.getCatalogRealname("CatName_SampleStarsCat");
    assertEquals("Wrong ID for catalogue", "SampleStarsCat", name);
  }

  @Test(expected = MetadataException.class)
  public void testCatalogueNameBadName() throws Exception {
    MetadocNameTranslator sut = new MetadocNameTranslator();
    String name = sut.getCatalogRealname("WrongCatName_SampleStarsCat");
  }

  @Test
  public void testNumberOfCatalogues() throws Exception {
    MetadocNameTranslator sut = new MetadocNameTranslator();
    assertEquals("Wrong number of catalogues", 1, sut.getNumCatalogs());
  }

  @Test
  public void testTableName() throws Exception {
    MetadocNameTranslator sut = new MetadocNameTranslator();
    String name = sut.getTableRealname("CatName_SampleStarsCat", "TabName_SampleStars");
    assertEquals("Wrong table-ID", "SampleStars", name);
  }

  @Test(expected = MetadataException.class)
  public void testTableNameBadCatalogueName() throws Exception {
    MetadocNameTranslator sut = new MetadocNameTranslator();
    String name = sut.getTableRealname("WrongCatName_SampleStarsCat", "TabName_SampleStars");
  }

  @Test(expected = MetadataException.class)
  public void testTableNameBadTableName() throws Exception {
    MetadocNameTranslator sut = new MetadocNameTranslator();
    String name = sut.getTableRealname("CatName_SampleStarsCat", "WrongTabName_SampleStars");
  }

}
