package org.astrogrid.adql;

import junit.framework.TestCase;
import org.astrogrid.query.ConeConverter;

/**
 * JUnit-3 tests for {@link ConeConverter}.
 *
 * @author Guy Rixon
 */
public class ConeConverterTest extends TestCase {

  public void testGetConeTemplate() throws Exception {
    assertNotNull(ConeConverter.getConeTemplate("adql/templates/cone_haversine_rad_deg.sql"));
  }

}
