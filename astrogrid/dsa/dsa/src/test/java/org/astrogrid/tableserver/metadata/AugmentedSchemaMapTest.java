package org.astrogrid.tableserver.metadata;

import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit-4 tests for {@link org.astrogrid.tableserver.metadata.AugmentedSchemaMap}.
 * @author Guy Rixon
 */
public class AugmentedSchemaMapTest {

  @Test
  public void testAll() throws Exception {
    assertNotNull("Can't see schema for metadoc 1.2", getClass().getResource("TableMetaDoc-1.2.xsd"));
    Map sut = new AugmentedSchemaMap();
    assertNotNull("No entry for metadoc 1.1", sut.get("urn:astrogrid:schema:dsa:TableMetaDoc:v1.1"));
    assertNotNull("No entry for metadoc 1.2", sut.get("urn:astrogrid:schema:dsa:TableMetaDoc:v1.2"));
  }
}
