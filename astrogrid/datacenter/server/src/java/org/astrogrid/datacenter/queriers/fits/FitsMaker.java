/*$Id: FitsMaker.java,v 1.2 2004/07/26 13:53:44 KevinBenson Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.fits;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.StringTokenizer;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.spi.Translator;
import org.astrogrid.datacenter.queriers.sql.deprecated.SqlQuerierSPI;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.sky.Angle;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;

/**
 * A 'standard' translator that creates 'standard' SQL
 */
public class FitsMaker {

   private static final Log log = LogFactory.getLog(FitsMaker.class);

      
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
        
        /*
        SqlQuerierSPI spi = new SqlQuerierSPI();
        Translator trans = spi.getTranslatorMap().lookup(namespaceURI);
        if (trans != null) {
           String sql = useSpi(queryBody, trans);
           log.debug("Used SPI "+trans+") to translate ADQL ("+namespaceURI+") to '"+sql+"'");
           return sql;
        }
        */
        String xql = useXslt(queryBody, namespaceURI);
        
        //sql = replaceRegion(sql);
        
        
        return xql;

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

      //work out which translator sheet to use
      
      //default
      if ((namespaceURI==null) || (namespaceURI.length()==0)) {
         throw new QueryException("No namespace specified in query document, so don't know what it is");
      }
      else if (namespaceURI.equals("http://tempuri.org/adql")) { //assume v0.5
         xsltDoc = "adql05-2-sql.xsl";
      }
      else if (namespaceURI.equals("http://adql.ivoa.net/v0.73")) {
         //xsltDoc = "adql073-2-sql.xsl";
         xsltDoc = "ADQLToXQL-0_73.xsl";
      }
      else if (namespaceURI.equals("http://www.ivoa.net/xml/ADQL/v0.7.4")) {
         xsltDoc = "adql074-2-sql.xsl";
      }
//      else if (namespaceURI.equals("http://adql.ivoa.net/v0.8")) {
//         xsltDoc = "adql08-2-sql.xsl";
//      }
      else if (namespaceURI.equals("http://astrogrid.org/sadql/v1.1")) {
         xsltDoc = "sadql1.1-2-sql.xsl";
      }
      
      //look up in config but using above softcoded as defaults
      String key = "datacenter.sqlmaker.xslt."+namespaceURI.replaceAll(":","_");
      xsltDoc = SimpleConfig.getSingleton().getString(key, xsltDoc);
      
      if (xsltDoc == null) {
         throw new RuntimeException("No XSLT sheet given for ADQL (namespace '"+namespaceURI+"'); set configuration key '" + key+"'");
      }

      Transformer transformer = null;
      try {
         //find specified sheet on classpath/working directory
         InputStream xsltIn = new BufferedInputStream(FitsMaker.class.getResourceAsStream("./xslt/"+xsltDoc));
      
         if (xsltIn == null) {
            throw new QueryException("Could not find/create ADQL->XQL transformer doc "+xsltDoc);
         }
         
         log.debug("Transforming ADQL ["+namespaceURI+"] using Xslt doc at './xslt/"+xsltDoc+"'");
         TransformerFactory tFactory = TransformerFactory.newInstance();
         transformer = tFactory.newTransformer(new StreamSource(xsltIn));
         try {
            tFactory.setAttribute("UseNamespaces", Boolean.FALSE);
         }
         catch (IllegalArgumentException iae) {
            //ignore - if UseNamepsaces is unsupported, it will chuck an exception, and
            //we don't want to use namespaces anyway so taht's fine
         }
         
         StringWriter sw = new StringWriter();
         transformer.transform(new DOMSource(queryBody), new StreamResult(sw));
         String xql = sw.toString();
        
         //tidy it up - remove new lines and double spaces
         xql = xql.replaceAll("\n","");
         xql = xql.replaceAll("\r","");
         while (xql.indexOf("  ")>-1) { xql = xql.replaceAll("  ", " "); }
         
         //botch botch botch - for some reason transformers sometimes add <?xml tag to beginning
         if (xql.startsWith("<?")) {
            xql = xql.substring(xql.indexOf("?>")+2);
         }
         //botch botch botch - something funny with ADQL 0.7.3 schema to do with comparisons
         xql = xql.replaceAll("&gt;", ">").replaceAll("&lt;", "<");
         
         log.debug("Used '"+xsltDoc+"' to translate ADQL ("+namespaceURI+") to '"+xql+"'");
         
         return xql;
      }
      catch (TransformerConfigurationException tce) {
         throw new QueryException(tce+" (using xslt sheet "+xsltDoc+")",tce);
      }
      catch (TransformerException te) {
         throw new QueryException(te+" translating ADQL->SQL using "+xsltDoc,te);
      }

   }
   
}


/*
$Log: FitsMaker.java,v $
Revision 1.2  2004/07/26 13:53:44  KevinBenson
Changes to Fits to do an xquery on an xml file dealing with fits data.
Small xsl style sheet to make the xql which will get the filename element

Revision 1.1.2.1  2004/07/26 08:53:40  KevinBenson
Still need to make a few more corrections, but wanted to check this in now.
It is the fits querier that now uses exist for doing adql->xquery

Revision 1.11  2004/07/12 23:26:51  mch
Fixed (somewhat) SQL for cone searches, added tests to Dummy DB

Revision 1.10  2004/07/12 14:12:04  mch
Fixed ADQL 0.7.4 xslt

Revision 1.9  2004/07/07 19:33:59  mch
Fixes to get Dummy db working and xslt sheets working both for unit tests and deployed

Revision 1.8  2004/07/06 18:48:34  mch
Series of unit test fixes

Revision 1.7  2004/07/01 23:07:14  mch
Introduced metadata generator

Revision 1.6  2004/03/17 21:03:20  mch
Added SQL transformation tests

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
