/*
 * $Id: SqlQueryMaker.java,v 1.7 2004/11/09 17:42:22 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.slinger.targets.TargetIndicator;
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
      assert (sql != null);
      return SqlParser.makeQuery(sql);
   }
   
}

/*
 $Log: SqlQueryMaker.java,v $
 Revision 1.7  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.6  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.5.8.1  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.5  2004/10/12 23:09:53  mch
 Lots of changes to querying to get proxy querying to SSA, and registry stuff

 Revision 1.4  2004/10/12 22:46:42  mch
 Introduced typed function arguments


 */



