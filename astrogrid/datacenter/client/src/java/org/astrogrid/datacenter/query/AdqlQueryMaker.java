/*
 * $Id: AdqlQueryMaker.java,v 1.5 2004/10/13 01:25:46 mch Exp $
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
 * Makes an Query from an ADQL document or model
 * <p>
 * @todo At the moment this is a horrible botch where the string is translated to SQL
    * using the StdSqlMaker, then parsed in again, whereas it should (one day) be
    * read directly from DOM or some generated model
 *
 *
 * @author M Hill
 */


public class AdqlQueryMaker  {

   /** Constructs a Query from the given ADQL (0.7.4) OM which is generated from the
    * SkyNode (0.7.4) WSDL
    */
   public static Query makeQuery(net.ivoa.www.xml.ADQL.v0_7_4.SelectType adql) {
      
      Adql074Parser maker = new Adql074Parser();
      return maker.makeQuery(adql);
   }

   
   /** Constructs a Query from the given ADQL DOM.  Nasty horrible thing converts
    * to SQL and parses it... */
   public static Query makeQuery(Element adql) throws QueryException {
      //yeuch
      StdSqlMaker maker = new StdSqlMaker();
      
      String sql = maker.getAdqlSql(adql);
      
      return SqlQueryMaker.makeQuery(sql);
   }

   /** Convenience routine - creates a Query (object model) from the given ADQL string.
    */
   public static Query makeQuery(String adql) throws QueryException, SAXException, IOException, ParserConfigurationException {
      return makeQuery(DomHelper.newDocument(adql).getDocumentElement());
   }

   /** Convenience routine - Constructs query from given inputstream
    */
   public static Query makeQuery(InputStream in) throws QueryException, IOException, SAXException, ParserConfigurationException {
      return makeQuery(DomHelper.newDocument(in).getDocumentElement());
   }

   public static Query makeQuery(String adql, TargetIndicator target, String format) throws QueryException, SAXException, IOException, ParserConfigurationException {
      Query query = makeQuery(adql);
      query.getResultsDef().setTarget(target);
      query.getResultsDef().setFormat(format);
      return query;
   }

   /** Constructs query from given inputstream
    */
   public static Query makeQuery(InputStream in, TargetIndicator target, String format) throws QueryException, IOException, SAXException, ParserConfigurationException {
      Query query = makeQuery(in);
      query.getResultsDef().setTarget(target);
      query.getResultsDef().setFormat(format);
      return query;
   }
  
   public static Query makeQuery(Element adql, TargetIndicator target, String format) throws QueryException {
      Query query = makeQuery(adql);
      query.getResultsDef().setTarget(target);
      query.getResultsDef().setFormat(format);
      return query;
   }
   
   
}
/*
 $Log: AdqlQueryMaker.java,v $
 Revision 1.5  2004/10/13 01:25:46  mch
 makes ADQL/sql rather than std sql (leave in CIRCLE)

 Revision 1.4  2004/10/12 22:46:42  mch
 Introduced typed function arguments

 Revision 1.3  2004/10/08 09:40:52  mch
 Started proper ADQL parsing

 Revision 1.2  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger


 */



