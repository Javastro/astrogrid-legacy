package org.astrogrid.tableserver.metadata;

import java.net.URL;
import java.util.HashMap;
import org.astrogrid.contracts.SchemaMap;

/**
 * A mapping of schema namespace-URIs to schema location-URIs.
 * The map contain mappings for all schemata standardized by the AstroGrid
 * contracts project, plus an experimental version of the schema for the DSA
 * "metadoc".
 *
 * @author Guy Rixon
 */
public class AugmentedSchemaMap extends HashMap<String,URL> {

  public AugmentedSchemaMap() {
    putAll(SchemaMap.ALL);
    put("urn:astrogrid:schema:dsa:TableMetaDoc:v1.2",
        this.getClass().getResource("TableMetaDoc-1.2.xsd"));
  }

}
