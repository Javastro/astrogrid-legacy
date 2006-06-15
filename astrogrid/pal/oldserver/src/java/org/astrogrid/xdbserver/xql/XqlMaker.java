/*$Id: XqlMaker.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.xdbserver.xql;

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
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
//import org.astrogrid.query.adql.Adql074Writer;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Creates XML from the query using transformation sheets
 */
public class XqlMaker {

   private static final Log log = LogFactory.getLog(XqlMaker.class);

      
   /**
    * Constructs an XQL statement for the given ADQL
    */
   public String fromAdql(Query query) throws QueryException, IOException {

      Element adql = null;
      //get the ADQL from the query
      try {
         //adql = DomHelper.newDocument(Adql074Writer.makeAdql(query)).getDocumentElement();
         adql = DomHelper.newDocument(query.getAdqlString()).getDocumentElement();
      }
      catch (SAXException e) {
         throw new RuntimeException("Query2Adql074 procuced invalid XML from query "+query,e);
      }
      
      //Create DOM
      String namespaceURI = adql.getNamespaceURI();
      if (namespaceURI == null) {
            // maybe not using namespace aware parser - see if we can find an xmlns attribute instead
            namespaceURI = adql.getAttribute("xmlns");
        }
        if (namespaceURI == null) {
            //DomHelper.PrettyElementToStream(adql,System.out);
            DomHelper.ElementToStream(adql,System.out);
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
      xsltDoc = ConfigFactory.getCommonConfig().getString(key, xsltDoc);
      
      if (xsltDoc == null) {
         throw new RuntimeException("No XSLT sheet given for ADQL (namespace '"+namespaceURI+"'); set configuration key '" + key+"'");
      }

      Transformer transformer = null;
      try {
         //find specified sheet on classpath/working directory
         //InputStream xsltIn = new BufferedInputStream(FitsMaker.class.getResourceAsStream("./xslt/"+xsltDoc));
        ClassLoader loader = this.getClass().getClassLoader();
        InputStream xsltIn = null;
        xsltIn = loader.getResourceAsStream("xslt/" + xsltDoc);
        if (xsltIn == null) {
           xsltIn = loader.getResourceAsStream(xsltDoc);
         }
         if (xsltIn == null) {
            xsltIn = loader.getResourceAsStream("xsl/" + xsltDoc);
         }
      
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
$Log: XqlMaker.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.1  2006/04/20 15:23:08  kea
Checking old sources in in oldserver directory (rather than just
deleting them, might still be useful).

Revision 1.4.2.1  2006/04/19 13:57:32  kea
Interim checkin.  All source is now compiling, using the new Query model
where possible (some legacy classes are still using OldQuery).  Unit
tests are broken.  Next step is to move the legacy classes sideways out
of the active tree.

Revision 1.4  2006/03/22 15:10:13  clq2
KEA_PAL-1534

Revision 1.3.24.1  2006/02/16 17:13:05  kea
Various ADQL/XML parsing-related fixes, including:
 - adding xsi:type attributes to various tags
 - repairing/adding proper column alias support (aliases compulsory
    in adql 0.7.4)
 - started adding missing bits (like "Allow") - not finished yet
 - added some extra ADQL sample queries - more to come
 - added proper testing of ADQL round-trip conversions using xmlunit
   (existing test was not checking whole DOM tree, only topmost node)
 - tweaked test queries to include xsi:type attributes to help with
   unit-testing checks

Revision 1.3  2005/11/21 12:54:18  clq2
DSA_KEA_1451

Revision 1.2.58.1  2005/11/15 15:39:17  kea
Looks in additional places to find xslt stylesheet.

Revision 1.2  2005/03/21 18:45:55  mch
Naughty big lump of changes

Revision 1.1  2005/03/10 16:42:55  mch
Split fits, sql and xdb

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.6.12.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.6  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

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
