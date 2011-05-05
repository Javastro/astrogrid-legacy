package org.astrogrid.query.adql;

import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.junit.Test;

/**
 * JUnit-4 test for query-parsing in {@link org.astrogrid.query.adql.Query}.
 * These tests supercede the ADQL/X tests in {@link org.astrogrid.query.adql.AdqlTest}.
 *
 * @author Guy Rixon
 */
public class AdqlsQueryTest {

  @Test (expected=QueryException.class)
  public void testEmptyFrom() throws Exception {
    Query sut = new Query("SELECT ALL FROM");
  }

  @Test (expected=QueryException.class)
  public void testEmptyWhere() throws Exception {
    Query sut = new Query("SELECT ALL FROM foo AS f WHERE");
  }

  @Test (expected=QueryException.class)
  public void testEmptyTableAlias() throws Exception {
    Query sut = new Query("SELECT ALL FROM foo AS ");
  }

  @Test (expected=QueryException.class)
  public void testMissingOrderBy() throws Exception {
    Query sut = new Query("SELECT ALL FROM foo AS f ASC");
  }
  
  @Test
  public void testSelectStar() throws Exception {
    Query sut = new Query("SELECT * FROM foo AS f");
  }

  @Test
  public void testSelectStarWithoutTableAlias() throws Exception {
    Query sut = new Query("SELECT * FROM foo");
  }

  @Test
  public void testSelectStarWithRowLimit() throws Exception {
    Query sut = new Query("SELECT TOP 1000 * FROM foo");
  }

  @Test public void testBetweenOperatorForRadec() throws Exception {
    Query sut = new Query("SELECT * FROM foo WHERE ra BETWEEN 10.0 AND 30.0 AND dec BETWEEN -5.0 AND 5.0");
  }

}
