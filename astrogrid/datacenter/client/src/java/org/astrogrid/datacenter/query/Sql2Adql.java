/*
 * $Id: Sql2Adql.java,v 1.1 2004/10/11 15:53:59 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.datacenter.queriers.sql.StdSqlMaker;
import org.astrogrid.slinger.TargetIndicator;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Convenience class for converting ADQL/SQL to ADQL/XML
 *
 * @author M Hill
 */


public class Sql2Adql {

   
   /** Constructs an ADQL 0.7.4 document from the given SQL */
   public static String translateToAdql074(String sql) throws QueryException, IOException {
      return Query2Adql074.makeAdql(SqlQueryMaker.makeQuery(sql));
   }

   
}
/*
 $Log: Sql2Adql.java,v $
 Revision 1.1  2004/10/11 15:53:59  mch
 New plugin key constant



 */



