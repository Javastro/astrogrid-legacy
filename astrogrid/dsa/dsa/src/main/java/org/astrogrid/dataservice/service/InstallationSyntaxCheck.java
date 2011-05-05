/*$Id: InstallationSyntaxCheck.java,v 1.2 2011/05/05 14:49:36 gtr Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service;

import java.io.StringWriter;
import java.security.Principal;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.DsaConfigurationException;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.tableserver.jdbc.AdqlSqlMaker;
import org.astrogrid.cfg.PropertyNotFoundException;
import org.astrogrid.dataservice.Configuration;
import org.astrogrid.tableserver.test.SampleStarsPlugin;




/** Unit test for checking an installation - runs a number of sample queries
 * to check that they produce SYNTACTICALLY valid SQL for the installation 
 * DBMS.
 *
 * @author K Andrews
 */
public class InstallationSyntaxCheck {

  /**
   * ADQL/X queries from which the ADQL/S is derived.
   */
   public static final String QUERYDIR_PREFIX = "adql/testqueries/";
   public static final String[] testSuiteQueries = {
      "selectAllAllow.xml",
      "selectAllLimit.xml",        
      "selectAggregateFuncs.xml",        
      "selectBinaries.xml",
      "selectDistinct.xml",        
      "selectExpr1.xml",           
      "selectExpr2.xml",          
      "selectExprMixed1.xml",     
      "selectExprMultiAlias.xml",  
      "selectAliasExpr.xml",  
      "selectExprUnary.xml",
      "selectGroupBy.xml",
      "selectLogPowMathsFuncs.xml",
      "selectSomeGroup.xml",
      "selectOrderByCol.xml",
      "selectOrderByComplex.xml",
      "selectSome.xml",
      "selectTrigFuncsDeg.xml",
      "selectTrigFuncsRad.xml",
      "selectUnaries.xml",
      "selectValueTweakMathsFuncs.xml",
      "selectComparisonOps.xml",
      "selectBetweenOps.xml",
      "selectBoolOps.xml",
      "selectAllNoAlias.xml",
      "selectSomeNoAlias.xml",
      "selectColourCutter.xml",
      "selectFromInnerJoin.xml",
      "selectFromOuterJoin.xml",
      "selectFromRightOuterJoin.xml"
      // This one will definitely fail if the same table is used 
      // for both tables in the join for testing purposes
      //"selectFromOuterJoinNoAlias.xml"
   };

  /**
   * ADQL/S queries executed in the tests.
   */
  public static final String[] adqlsQueries = {
    "SELECT TOP 10 * FROM TABLE-NAME",
    "SELECT TOP 10 * FROM TABLE-NAME AS t",
    "SELECT TOP 10 t.COLUMN-NAME-1 FROM TABLE-NAME AS t WHERE t.COLUMN-NAME-1 BETWEEN 0 AND 0.5 AND t.COLUMN-NAME-2 BETWEEN -5 AND 5",
    "SELECT TOP 10 (COLUMN-NAME-1 + COLUMN-NAME-2), (COLUMN-NAME-1 - COLUMN-NAME-2), (COLUMN-NAME-1 * COLUMN-NAME-2), (COLUMN-NAME-1 / COLUMN-NAME-2) FROM TABLE-NAME WHERE COLUMN-NAME-2 <> 0.0",
    "SELECT TOP 10 COLUMN-NAME-1, COLUMN-NAME-2 FROM TABLE-NAME WHERE (NOT (COLUMN-NAME-1=COLUMN-NAME-2) OR COLUMN-NAME-1=COLUMN-NAME-2) AND COLUMN-NAME-1>COLUMN-NAME-2",
    "SELECT TOP 10 COLUMN-NAME-1, COLUMN-NAME-2 FROM TABLE-NAME WHERE ( ( ( ( (COLUMN-NAME-1>56.0)  And  (COLUMN-NAME-1<57.0) ) )  Or  ( (COLUMN-NAME-1>360.0)  Or  (COLUMN-NAME-1<0.0) ) ) )  AND  ( (COLUMN-NAME-2>22.0)  And  ( (COLUMN-NAME-2<23.0)  And  ( (COLUMN-NAME-1>0.0)  And  ( (COLUMN-NAME-2<0.5)  And  ( ( (COLUMN-NAME-1 - COLUMN-NAME-2)  >1.0) ) ) ) ) )",
    "SELECT TOP 10 COLUMN-NAME-1, COLUMN-NAME-2 FROM TABLE-NAME WHERE (COLUMN-NAME-1=COLUMN-NAME-2)  And  ( (COLUMN-NAME-1<>COLUMN-NAME-2)  And  ( (COLUMN-NAME-1>COLUMN-NAME-2)  And  ( (COLUMN-NAME-1>=COLUMN-NAME-2)  And  ( (COLUMN-NAME-1<COLUMN-NAME-2)  And  (COLUMN-NAME-1<=COLUMN-NAME-2) ) ) ) )",
    "SELECT DISTINCT TOP 10 t.COLUMN-NAME-1, t.COLUMN-NAME-2 FROM TABLE-NAME AS t",
    "SELECT TOP 10 SIN(COLUMN-NAME-1) FROM TABLE-NAME",
    "SELECT TOP 10 (t.COLUMN-NAME-1/t.COLUMN-NAME-2) FROM TABLE-NAME AS t WHERE COLUMN-NAME-2 <> 0",
    "SELECT TOP 10  ( (SIN(t.COLUMN-NAME-1)+ COS(t.COLUMN-NAME-2)) / (SQRT(ABS(t.COLUMN-NAME-1)) - LOG(ABS(t.COLUMN-NAME-2))) )   FROM TABLE-NAME AS t WHERE  (t.COLUMN-NAME-1>0.0)  AND  (t.COLUMN-NAME-2>0.0)",
    "SELECT TOP 10 ( ( (SIN(a.COLUMN-NAME-1)  + COS(b.COLUMN-NAME-2) )  )   /  ( (SQRT(ABS(c.COLUMN-NAME-1) )  - LOG(ABS(d.COLUMN-NAME-2) ) )  )  ) FROM TABLE-NAME AS a, TABLE-NAME AS b, TABLE-NAME AS c, TABLE-NAME AS d WHERE  ( (a.COLUMN-NAME-1>0.0)  AND  (b.COLUMN-NAME-2>0.0) )  AND  ( (c.COLUMN-NAME-1>0.0)  And  (d.COLUMN-NAME-2>0.0) )",
    "SELECT TOP 10 ( ( (SIN(a.COLUMN-NAME-1)  + COS(a.COLUMN-NAME-2) )  )   /  ( (SQRT(ABS(a.COLUMN-NAME-1) )  - LOG(ABS(a.COLUMN-NAME-1) ) )  )  )   AS exprAlias FROM TABLE-NAME AS a WHERE a.COLUMN-NAME-1>0.0",
    "SELECT TOP 10 a.COLUMN-NAME-1, a.COLUMN-NAME-2 FROM TABLE-NAME AS a GROUP BY a.COLUMN-NAME-1, a.COLUMN-NAME-2",
    "SELECT TOP 10 EXP( ( (ABS(a.COLUMN-NAME-1)  + 1.0)  )  ) , LOG(ABS(a.COLUMN-NAME-1) ) , SQRT( ( (a.COLUMN-NAME-1 * a.COLUMN-NAME-1)  )  ) , ((a.COLUMN-NAME-1)*(a.COLUMN-NAME-1)) , LOG10( ( (ABS(a.COLUMN-NAME-1)  + 1.0)  )  )  FROM TABLE-NAME as a WHERE a.COLUMN-NAME-1>0.0",
    "SELECT TOP 10 a.COLUMN-NAME-1, a.COLUMN-NAME-2 FROM TABLE-NAME AS a GROUP BY a.COLUMN-NAME-1, a.COLUMN-NAME-2",
    "SELECT TOP 10 * FROM TABLE-NAME AS b ORDER BY b.COLUMN-NAME-2",
    "SELECT TOP 10 * FROM TABLE-NAME AS a ORDER BY a.COLUMN-NAME-1 ASC, ABS(a.COLUMN-NAME-2)  DESC,  (a.COLUMN-NAME-1 + a.COLUMN-NAME-2)   ASC",
    "SELECT TOP 10 a.COLUMN-NAME-1, a.COLUMN-NAME-2 FROM TABLE-NAME AS a",
    "SELECT TOP 10 SIN(DEGREES(RADIANS(a.COLUMN-NAME-1) ) ) , COS(DEGREES(RADIANS(a.COLUMN-NAME-1) ) ) , TAN(DEGREES(RADIANS(a.COLUMN-NAME-1) ) ) , COT(DEGREES(RADIANS(a.COLUMN-NAME-1) ) ) , ASIN(DEGREES(RADIANS(a.COLUMN-NAME-1) ) ) , ACOS(DEGREES(RADIANS(a.COLUMN-NAME-1) ) ) , ATAN(DEGREES(RADIANS(a.COLUMN-NAME-1) ) ) , ATAN2(DEGREES(RADIANS(a.COLUMN-NAME-1) ) , DEGREES(RADIANS(a.COLUMN-NAME-2) ) )  FROM TABLE-NAME AS a WHERE  (a.COLUMN-NAME-1>0.0)  AND  (a.COLUMN-NAME-1<1.0)",
    "SELECT TOP 10 SIN(RADIANS(a.COLUMN-NAME-1) ) , COS(RADIANS(a.COLUMN-NAME-1) ) , TAN(RADIANS(a.COLUMN-NAME-1) ) , COT(RADIANS(a.COLUMN-NAME-1) ) , ASIN(RADIANS(a.COLUMN-NAME-1) ) , ACOS(RADIANS(a.COLUMN-NAME-1) ) , ATAN(RADIANS(a.COLUMN-NAME-1) ) , ATAN2(RADIANS(a.COLUMN-NAME-1) , RADIANS(a.COLUMN-NAME-2) )  FROM TABLE-NAME AS a WHERE  (a.COLUMN-NAME-1>0.0)  And  (a.COLUMN-NAME-1<1.0)",
    "SELECT TOP 10 TABLE-NAME.COLUMN-NAME-1, TABLE-NAME.COLUMN-NAME-2, SIN(TABLE-NAME.COLUMN-NAME-1) FROM TABLE-NAME AS TABLE-NAME",
    "SELECT TOP 10 * FROM TABLE-NAME AS a INNER JOIN TABLE-NAME AS b ON a.COLUMN-NAME-1=b.COLUMN-NAME-2",
    "SELECT TOP 10 a.COLUMN-NAME-1, b.COLUMN-NAME-2 FROM TABLE-NAME AS a LEFT OUTER JOIN TABLE-NAME AS b ON a.COLUMN-NAME-1=b.COLUMN-NAME-2",
    "SELECT TOP 10 a.COLUMN-NAME-1, b.COLUMN-NAME-2 FROM TABLE-NAME AS a RIGHT OUTER JOIN TABLE-NAME AS b ON a.COLUMN-NAME-1=b.COLUMN-NAME-2"
  };

   private Principal testPrincipal = new LoginAccount("SelfTest", "localhost");

  /**
   * Runs the ADQL/S tests, writing the verdict in HTML to the given PrintWriter.
   * The HTML is a fragment at block level and must be embedded in a page by
   * the caller.
   *
   * @param out The destination for the HTML.
   */
  public String runAllTests() {
    StringBuilder out = new StringBuilder();

    /* Initialise plugin if required, just in case the user runs this test first */
    String plugin = "";
    try {
      plugin = ConfigFactory.getCommonConfig().getString("datacenter.querier.plugin");
    }
    catch (PropertyNotFoundException e) {
      // Ignore this one in this context - just want to initialise samplestars if needed
    }
    if ("org.astrogrid.tableserver.test.SampleStarsPlugin".equals(plugin)) {
      SampleStarsPlugin.initConfig();
    }

    for (String rawAdqls : adqlsQueries) {
      String matchedAdqls = null;
      try {
        matchedAdqls = matchToSchema(rawAdqls);
        out.append("<p>Given ADQL/S is </p><pre>");
        out.append(makePrintable(matchedAdqls));
        out.append("</pre></p>\n");
      }
      catch (Exception e) {
        out.append("<p class='fail'>FAIL</p>\n<pre>\n");
        out.append(e.getMessage());
        out.append("<br/>\n");
        for (StackTraceElement s : e.getStackTrace()) {
          out.append(s);
          out.append("<br/>\n");
        }
        out.append("</pre>\n");
      }
      try {
        StringWriter sw = new StringWriter();
        Query query = 
            new Query(matchToSchema(matchedAdqls),
                      new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE));
        out.append("<p>Parsed ADQL/X is</p><pre>");
        out.append(query.getHtmlAdqlString());
        out.append("</pre>");
        String sql = new AdqlSqlMaker().makeSql(query);
        out.append("<p>Translated SQL is </p><pre>\n");
        out.append(makePrintable(sql));
        out.append("</pre></p>\n");
        DataServer server = new DataServer();
        server.askQuery(testPrincipal, query, this);
        out.append("<p class='pass'>PASS</p>\n");
      }
      catch (Throwable t) {
        out.append("<p class='fail'>FAIL</p>\n<pre>\n");
        out.append(t.getMessage());
        out.append("<br/>\n");
        for (StackTraceElement s : t.getStackTrace()) {
          out.append(s);
          out.append("<br/>\n");
        }
        out.append("</pre>\n");
      }
    }

    return out.toString();
  }

  /**
   * Sets the table and column names to match the database in use.
   *
   * @param adqls The raw query in ADQL/S.
   * @return The updated query.
   */
  private String matchToSchema(String raw) throws DsaConfigurationException {
    return raw.replace("TABLE-NAME", Configuration.getTestTable())
              .replace("COLUMN-NAME-1", Configuration.getTestColumn1())
              .replace("COLUMN-NAME-2", Configuration.getTestColumn2());
  }

  /**
   * Escapes the characters necessary to include a string in an XHTML fragment.
   *
   * @param s The string to be escaped.
   * @return The escaped string.
   */
  private String makePrintable(String s) {
    return s.replaceAll("&", "&amp;").replaceAll("<", "\\&lt;").replaceAll(">", "\\&gt;");
  }
   
  

}
