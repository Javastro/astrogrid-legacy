/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.adql;

import java.io.InputStream;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * JUnit-4 tests of {@link org.astrogrid.query.adql.Query}.
 * These test the utility functions; most of the ADQL translation testing
 * is done in the AdqlsTest.java class.
 */
public class QueryTest {

  @Test
  public void testGetLimit() throws Exception {
    String adql = "SELECT TOP 100 * FROM catalogue";
    Query query = new Query(adql);
    long limit = query.getLimit();
    assertEquals("Wrong row-limit", 100, limit);
  } 

  @Test(expected = QueryException.class)
  public void testSetSelectDocument1() throws Exception {
    Query query = new Query((String)null);
  }

  @Test(expected = QueryException.class)
  public void testSetSelectDocument2() throws Exception {
    Query query = new Query((InputStream)null);
  }

  @Test(expected = QueryException.class)
  public void testSetSelectDocument3() throws Exception {
    Query query = new Query("");
  }

  @Test
  public void testGetTables1() throws Exception {
    String adql = "SELECT a.POS_EQ_RA, a.POS_EQ_DEC FROM catalogue AS a";
    Query query = new Query(adql);
    String[] tables = query.getTableReferences();
    assertEquals("Wrong number of tables", 1, tables.length);
    assertEquals("Wrong table-name", "catalogue", tables[0]);
  }
   
  @Test
  public void testGetTables2() throws Exception {
    String adql = "SELECT a.POS_EQ_RA, a.POS_EQ_DEC, b.r_mag, b.i_mag " +
                  "FROM positions AS a, magnitudes AS b";
    Query query = new Query(adql);
    String[] tables = query.getTableReferences();
    assertEquals("Wrong number of tables", 2, tables.length);
    assertEquals("Wrong table-name", "positions", tables[0]);
    assertEquals("Wrong table-name", "magnitudes", tables[1]);
  }

  @Test
  public void testGetTables3() throws Exception {
    // This one uses only one table, with 4 distinct aliases
    String adql = "SELECT a.a, b.b, c.c, d.d FROM catalogue AS a, " +
                  "catalogue AS b, catalogue AS c, catalogue AS d";
    Query query = new Query(adql);
    String[] tables = query.getTableReferences();
    assertEquals("Wrong number of tables", 1, tables.length);
    assertEquals("Wrong table-name", "catalogue", tables[0]);
  }

  @Test
  public void testGetTables4() throws Exception {
    // This one uses only two tables, with 2 distinct aliases each
    String adql = "SELECT a.a, b.b, c.c, d.d FROM t1 AS a, " +
                  "t2 AS b, t1 AS c, t2 AS d";
    Query query = new Query(adql);
    String[] tables = query.getTableReferences();
    assertEquals("Wrong number of tables", 2, tables.length);
    assertEquals("Wrong table-name", "t1", tables[0]);
    assertEquals("Wrong table-name", "t2", tables[1]);
  }

  @Test
  public void testGetCols1() throws Exception {
    String adql = "SELECT a.POS_EQ_RA, a.POS_EQ_DEC FROM catalogue AS a";
    Query query = new Query(adql);
    String[] columns = query.getColumnReferences();
    assertEquals("Wrong number of columns", 2, columns.length);
    assertEquals("Wrong column-name", "POS_EQ_RA", columns[0]);
    assertEquals("Wrong column-name", "POS_EQ_DEC", columns[1]);
  }

  @Test
  public void testGetCols2() throws Exception {
    String adql = "SELECT * FROM catalogue AS a";
    Query query = new Query(adql);
    String[] columns = query.getColumnReferences();
    assertEquals("Wrong number of columns", 0, columns.length);
  }

  @Test
  public void testGetCols3() throws Exception {
    String adql = "SELECT a.POS_EQ_RA, a.POS_EQ_DEC, b.MAG_R FROM " +
                  "positions AS a, magnitudes AS b";
    Query query = new Query(adql);

    String[] columnsA = query.getColumnReferences("positions");
    assertEquals("Wrong number of columns", 2, columnsA.length);
    assertEquals("Wrong column-name", "POS_EQ_RA", columnsA[0]);
    assertEquals("Wrong column-name", "POS_EQ_DEC", columnsA[1]);

    String[] columnsB = query.getColumnReferences("magnitudes");
    assertEquals("Wrong number of columns", 1, columnsB.length);
    assertEquals("Wrong column-name", "MAG_R", columnsB[0]);

    String[] columnsC = query.getColumnReferences("nosuchtable");
    assertEquals("Wrong number of columns", 0, columnsC.length);
  }
   
  @Test
  public void testGetTableAlias1() throws Exception {
    String adql = "SELECT a.POS_EQ_RA, a.POS_EQ_DEC, b.r_mag, b.i_mag " +
                  "FROM positions AS a, magnitudes AS b";
    Query query = new Query(adql);
    assertEquals("Wrong table-alias", "a", query.getTableAlias("positions"));
    assertEquals("Wrong table-alias", "b", query.getTableAlias("magnitudes"));
    assertNull("Wrong table-alias", query.getTableAlias("nosuchtable"));
  }

  @Test
  public void testGetTableAlias2() throws Exception {
    // If the query uses no aliases, the actual table-names are retuned as aliases.
    String adql = "SELECT positions.POS_EQ_RA, positions.POS_EQ_DEC, " +
                  "magnitudes.r_mag, magnitudes.i_mag FROM positions, magnitudes";
    Query query = new Query(adql);
    assertEquals("Wrong table-alias", "positions", query.getTableAlias("positions"));
    assertEquals("Wrong table-alias", "magnitudes", query.getTableAlias("magnitudes"));
    assertNull("Wrong table-alias", query.getTableAlias("nosuchtable"));
  }

  @Test
  public void testNoAlias() throws Exception {
    String adql = "SELECT * FROM positions WHERE POS_EQ_DEC > 0.0 ";
    Query query = new Query(adql);
    assertEquals("Wrong table-alias", "positions", query.getTableAlias("positions"));
    assertEquals("Wrong table-name", "positions", query.getTableName("positions"));
  }
   
}
