/*
 * $Id: SqlQueryMaker.java,v 1.4 2004/10/12 22:46:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.slinger.TargetIndicator;
import org.xml.sax.SAXException;


/**
 * Makes queries from SQL-like strings using the SQL parser
 */

public class SqlQueryMaker  {
   
   
   public static Query makeQuery(String adql, TargetIndicator target, String format) throws QueryException, SAXException, IOException, ParserConfigurationException {
      Query query = makeQuery(adql);
      query.getResultsDef().setTarget(target);
      query.getResultsDef().setFormat(format);
      return query;
   }
   
   /** Static method for convenience */
   public static Query makeQuery(String sql) {
      return SqlParser.makeQuery(sql);
   }
   
   
   
}

/*
 $Log: SqlQueryMaker.java,v $
 Revision 1.4  2004/10/12 22:46:42  mch
 Introduced typed function arguments


 */



