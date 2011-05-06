package org.astrogrid.tableserver.jdbc;

import org.astrogrid.dataservice.Configuration;
import org.astrogrid.query.QueryException;
import org.junit.Test;

/**
 * JUnit-4 tests for {@link org.astrogrid.tableserver.jdbc.AdqlSqlMaker2}.
 *
 * @author Guy Rixon
 */
public class AdqlSqlMaker2Test {

  @Test (expected = QueryException.class)
  public void testMissingTransformation1() throws Exception {
    Configuration.setAdqlStylesheetName(null);
    AdqlSqlMaker2.clearTemplates();
    AdqlSqlMaker2 sut = new AdqlSqlMaker2();
  }

  @Test (expected = QueryException.class)
  public void testMissingTransformation2() throws Exception {
    Configuration.setAdqlStylesheetName("");
    AdqlSqlMaker2.clearTemplates();
    AdqlSqlMaker2 sut = new AdqlSqlMaker2();
  }

  @Test (expected = QueryException.class)
  public void testMissingTransformation3() throws Exception {
    Configuration.setAdqlStylesheetName("/not-there");
    AdqlSqlMaker2.clearTemplates();
    AdqlSqlMaker2 sut = new AdqlSqlMaker2();
  }

}
