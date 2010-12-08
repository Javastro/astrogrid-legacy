/*
 * (c) Copyright AstroGrid 2010.
 */
package org.astrogrid.dataservice.service.vosi;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.junit.Test;

/**
 * JUnit-4 tests for {@link org.astrogrid.dataservice.service.vosi.TableServlet}.
 * @author Guy Rixon
 */
public class TableServletTest {

  /**
   * Tests that the table-description method inside the servlet produces
   * an XML fragment that is internally well-formed. The fragment is wrapped
   * in an abitrary document-element to make a plausible document.
   */
  @Test
  public void testTableDescription() throws Exception {
    SampleStarsPlugin.initialise();
    TableServlet sut = new TableServlet();
    String xml = "<?xml version='1.0'?>" +
                 "<foo>" +
                 sut.getTableDescriptions(null) +
                 "</foo>"; // Should be well formed throughout.
    System.out.println(xml);

    // Parse it. This will throw if it's not well-formed.
    DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    b.parse(new ByteArrayInputStream(xml.getBytes()));
  }

  @Test
  public void testOutputAllCatalogues() throws Exception {
    SampleStarsPlugin.initialise();
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    TableServlet sut = new TableServlet();

    StringWriter sw = new StringWriter();
    sut.output(TableMetaDocInterpreter.getCatalogNames(),
               null,
               sw);
    System.out.println(sw);
    builder.parse(new ByteArrayInputStream(sw.toString().getBytes()));
  }

  @Test
  public void testOutputSelectedCatalogue() throws Exception {
    SampleStarsPlugin.initialise();
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    TableServlet sut = new TableServlet();

    StringWriter sw = new StringWriter();
    sut.output(TableMetaDocInterpreter.getCatalogNames(),
               "CatName_SampleStarsCat",
               sw);
    System.out.println(sw);
    builder.parse(new ByteArrayInputStream(sw.toString().getBytes()));
  }


}
