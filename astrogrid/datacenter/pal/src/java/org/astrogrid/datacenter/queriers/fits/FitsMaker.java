/*$Id: FitsMaker.java,v 1.5 2004/10/25 13:14:19 jdt Exp $
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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
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
import org.astrogrid.datacenter.query.Adql074Writer;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Creates XML from the query using transformation sheets
 */
public class FitsMaker {

   private static final Log log = LogFactory.getLog(FitsMaker.class);

      
   /**
    * Constructs an XQL statement for the given ADQL
    */
   public String fromAdql(Query query) throws QueryException, IOException {

      Element adql = null;
      //get the ADQL from the query
      try {
         adql = DomHelper.newDocument(Adql074Writer.makeAdql(query)).getDocumentElement();
      }
      catch (SAXException e) {
         throw new RuntimeException("Query2Adql074 procuced invalid XML from query "+query,e);
      }
      catch (ParserConfigurationException e) {
         throw new RuntimeException("Server configuration error:"+e,e);
      }
      
      //Create DOM
      String namespaceURI = adql.getNamespaceURI();
      if (namespaceURI == null) {
            // maybe not using namespace aware parser - see if we can find an xmlns attribute instead
            namespaceURI = adql.getAttribute("xmlns");
        }
        if (namespaceURI == null) {
            DomHelper.PrettyElementToStream(adql,System.out);
            throw new IllegalArgumentException("Query body has no namespace - cannot determine language");
        }
        
        String xql = useXslt(adql, namespaceURI);
        
        return xql;

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
         //xsltDoc = "ADQLToXQL-0_73.xsl";
        xsltDoc = "adql073-2-xql_fits.xsl";
      }
      else if (namespaceURI.equals("http://www.ivoa.net/xml/ADQL/v0.7.4")) {
         xsltDoc = "adql074-2-xql_fits.xsl";
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
         //InputStream xsltIn = new BufferedInputStream(FitsMaker.class.getResourceAsStream("./xslt/"+xsltDoc));
        ClassLoader loader = this.getClass().getClassLoader();
        InputStream xsltIn = loader.getResourceAsStream(xsltDoc);
      
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
Revision 1.5  2004/10/25 13:14:19  jdt
Merges from branch PAL_MCH - another attempt

Revision 1.2.8.1  2004/10/21 19:10:24  mch
Removed deprecated translators, moved SqlMaker back to server,

Revision 1.2  2004/10/06 21:12:17  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.5  2004/09/21 14:14:49  KevinBenson
added a xsl for fits on 0.7.4

Revision 1.4  2004/09/07 00:54:20  mch
Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

Revision 1.3  2004/08/10 12:07:03  KevinBenson
result of merge with kev_09_08_04_RT fixing the fits problem with xsl stylesheet

Revision 1.2.10.1  2004/08/10 12:01:34  KevinBenson
small change to reference another xsl stylesheet

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
