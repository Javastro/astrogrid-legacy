/*$Id: StdSqlMaker.java,v 1.5 2004/03/17 18:03:20 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.sql;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.spi.Translator;
import org.astrogrid.datacenter.queriers.sql.deprecated.SqlQuerierSPI;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;

/**
 * A 'standard' translator that creates 'standard' SQL
 */
public class StdSqlMaker  extends SqlMaker {

   private static final Log log = LogFactory.getLog(StdSqlMaker.class);

   /**
    * Constructs an SQL statement for the given cone query. Looks for the HTM
    * column first - if it finds it, uses that to do the cone search
    */
   public String fromCone(ConeQuery query) {
      if (SimpleConfig.getSingleton().getString(CONE_SEARCH_HTM_KEY, null) != null) {
         return getHtmSql(query);
      } else {
         return getRaDecSql(query);
      }
   }
   
   /** Returns the SQL suitable for doing a cone query on RA & DEC values */
   public String getRaDecSql(ConeQuery query) {

      String table = SimpleConfig.getSingleton().getString(CONE_SEARCH_TABLE_KEY);
      String alias = table.substring(0,1);
      
      //get which columns given RA & DEC for cone searches
      String raCol  = alias+"."+SimpleConfig.getSingleton().getString(CONE_SEARCH_RA_COL_KEY);
      String decCol = alias+"."+SimpleConfig.getSingleton().getString(CONE_SEARCH_DEC_COL_KEY);
      
      double ra  = query.getRa();
      double dec = query.getDec();
      double radius = query.getRadius();
      
      return "SELECT * FROM "+table+" as "+alias+
         " WHERE "+
         //square - for quicker searches
         "("+decCol+"<"+(dec+radius)+" AND "+decCol+">"+(dec-radius)+" AND"+
         " "+ raCol+"<"+(ra +radius)+" AND "+ raCol+">"+(ra -radius)+")"+
         " AND "+
         //circle
         "((2 * ASIN( SQRT("+
         "SIN(("+dec+"-"+decCol+")/2) * SIN(("+dec+"-"+decCol+")/2) +"+    //some sqls won't handle powers so multiply by self
         "COS("+dec+") * COS("+decCol+") * "+
         "SIN(("+ra+"-"+raCol+")/2) * SIN(("+ra+"-"+raCol+")/2)  "+ //some sqls won't handle powers so multiply by self
      "))) < "+radius+")";
   }
      
   /** Returns the SQL suitable for doing a cone query on HTM-indexed catalogue */
   public String getHtmSql(ConeQuery query) {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Constructs an SQL statement for the given ADQL
    */
   public String fromAdql(AdqlQuery query) throws QueryException {
      //should use appropriate xslt, but use deprecated stuff for now

      // find the translator
        Element queryBody = query.toDom().getDocumentElement();
        String namespaceURI = queryBody.getNamespaceURI();
        if (namespaceURI == null) {
            // maybe not using namespace aware parser - see if we can find an xmlns attribute instead
            namespaceURI = queryBody.getAttribute("xmlns");
        }
        if (namespaceURI == null) {
            DomHelper.PrettyElementToStream(queryBody,System.out);
            throw new IllegalArgumentException("Query body has no namespace - cannot determine language");
        }
        
        SqlQuerierSPI spi = new SqlQuerierSPI();
        Translator trans = spi.getTranslatorMap().lookup(namespaceURI);
        if (trans != null) {
           String sql = useSpi(queryBody, trans);
           log.info("Used SPI "+trans+") to translate ADQL ("+namespaceURI+") to '"+sql+"'");
           return sql;
        }
        
        return useXslt(queryBody, namespaceURI);

     }
     
     /** Uses the SPI plugin to do the translations */
     public String useSpi(Element queryBody, Translator trans) throws QueryException {
        // do the translation
        Object intermediateRep = null;
        Class expectedType = null;
        try { // don't trust it.
            intermediateRep = trans.translate(queryBody);
        } catch (Exception t) {
            throw new QueryException("Translation phase failed:" + t.getMessage(),t);
        }
        //check return type
        expectedType = trans.getResultType();
        if (! expectedType.isInstance(intermediateRep)) { // checks result is non-null and the right type.
            throw new QueryException("Translation result " + intermediateRep.getClass().getName() + " not of expected type " + expectedType.getName());
        }
        
        return (String) intermediateRep;

   }
   
     /** Uses Xslt to do the translations */
   public String useXslt(Element queryBody, String namespaceURI) throws QueryException {

      String xsltDoc = null;
      //work out which translator sheet to use - default
      if (namespaceURI.equals("http://adql.ivoa.net/v0.73")) {
         xsltDoc = "adql073-2-sql.xsl";
      }
      if (namespaceURI.equals("http://adql.ivoa.net/v0.8")) {
         xsltDoc = "adql08-2-sql.xsl";
      }

      if (namespaceURI.equals("http://astrogrid.org/sadql/v1.1")) {
         xsltDoc = "sadql1.1-2-sql.xsl";
      }
      //look up in config but using above softcoded as defaults
      String key = "datacenter.sqlmaker.xslt."+namespaceURI;
      xsltDoc = SimpleConfig.getSingleton().getString(key, xsltDoc);
      
      if (xsltDoc == null) {
         throw new RuntimeException("No XSLT sheet given for ADQL; set configuration key '" + key+"'");
      }

      URL xsltUrl = null;
      Transformer transformer = null;
      try {
         //find specified sheet on classpath/working directory
         xsltUrl = Config.resolveFilename(xsltDoc);
      
         log.debug("Transforming ADQL ["+namespaceURI+"] using Xslt doc at '"+xsltUrl+"'");
         TransformerFactory tFactory = TransformerFactory.newInstance();
         transformer = tFactory.newTransformer(new StreamSource(xsltUrl.openStream()));
         
         StringWriter sw = new StringWriter();
         transformer.transform(new DOMSource(queryBody), new StreamResult(sw));
         String sql = sw.toString();
        
         //tidy it up - remove new lines and double spaces
         sql = sql.replaceAll("\n","");
         sql = sql.replaceAll("\r","");
         while (sql.indexOf("  ")>-1) { sql = sql.replaceAll("  ", " "); }
         
         //botch botch botch - for some reason transformers sometimes add <?xml tag to beginning
         if (sql.startsWith("<?")) {
            sql = sql.substring(sql.indexOf("?>")+2);
         }
         //botch botch botch - something funny with ADQL 0.7.3 schema to do with comparisons
         sql = sql.replaceAll("&gt;", ">").replaceAll("&lt;", "<");
         
         log.info("Used "+xsltDoc+"(="+xsltUrl+" from "+key+") to translate ADQL ("+namespaceURI+") to '"+sql+"'");
         
         return sql;
      }
      catch (IOException ioe) {
         throw new QueryException("Could not find/create ADQL->SQL tansformer from "+xsltDoc+"(="+xsltUrl+", from key "+key+")",ioe);
      }
      catch (TransformerConfigurationException tce) {
         throw new QueryException("Server not setup correctly",tce);
      }
      catch (TransformerException te) {
         throw new QueryException("Error translating ADQL->SQL using "+xsltDoc+"(="+xsltUrl+", from key "+key+")",te);
      }

   }
}


/*
$Log: StdSqlMaker.java,v $
Revision 1.5  2004/03/17 18:03:20  mch
Added v0.8 ADQL

Revision 1.4  2004/03/17 01:47:26  mch
Added v05 Axis web interface

Revision 1.3  2004/03/14 16:55:48  mch
Added XSLT ADQL->SQL support

Revision 1.2  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 
*/
