/*$Id$
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.query.xql;

import java.io.BufferedInputStream;
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
import org.astrogrid.query.adql.Adql074Writer;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Creates XQL (XQuery?) from the query using transformation sheets
 */
public class XqlMaker {

   private static final Log log = LogFactory.getLog(XqlMaker.class);

      
   /**
    * Constructs an XQL statement for the given ADQL
    */
   public String getXql(Query query) throws QueryException, IOException {

      Element adql = null;
      //get the ADQL from the query
      try {
         adql = DomHelper.newDocument(Adql074Writer.makeAdql(query)).getDocumentElement();
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
      } else if (namespaceURI.equals("http://www.ivoa.net/xml/ADQL/v0.7.4")) {
         xsltDoc = "adql074-2-xql.xsl";
      }
      else if (namespaceURI.equals("http://astrogrid.org/sadql/v1.1")) {
         xsltDoc = "sadql1.1-2-sql.xsl";
      }
      
      //look up in config but using above softcoded as defaults
      String key = "datacenter.xqlmaker.xslt."+namespaceURI.replaceAll(":","_");
      xsltDoc = ConfigFactory.getCommonConfig().getString(key, xsltDoc);
      String declaredNS = ConfigFactory.getCommonConfig().getString("datacenter.xqlmaker.namespace.declare", "");
      
      if (xsltDoc == null) {
         throw new RuntimeException("No XSLT sheet given for ADQL (namespace '"+namespaceURI+"'); set configuration key '" + key+"'");
      }

      Transformer transformer = null;
      try {
         //look for transformation sheet as resource of this class (for unit tests, etc)
          InputStream xsltIn = null;
          ClassLoader loader = this.getClass().getClassLoader();
          xsltIn = loader.getResourceAsStream("xsl/" + xsltDoc);
          if (xsltIn == null) {
              throw new QueryException("Could not find/create ADQL->XQL transformer doc "+xsltDoc);
          }
         
         //log.debug("Transforming ADQL ["+namespaceURI+"] using Xslt doc at './xslt/"+xsltDoc+"'");
         log.debug("Transforming ADQL ["+namespaceURI+"] using Xsl doc at 'xslt/"+xsltDoc+"'");
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
         transformer.setParameter("declare_elems", declaredNS);         
         transformer.transform(new DOMSource(queryBody), new StreamResult(sw));
         String xql = sw.toString();
        
         //tidy it up - remove new lines and double spaces
         xql = xql.replaceAll("\n","");
         xql = xql.replaceAll("\r","");
         //while (xql.indexOf("  ")>-1) { xql = xql.replaceAll("  ", " "); }
         
         //botch botch botch - for some reason transformers sometimes add <?xml tag to beginning
         if (xql.startsWith("<?")) {
            xql = xql.substring(xql.indexOf("?>")+2);
         }
         //botch botch botch - something funny with ADQL 0.7.3 schema to do with comparisons
         xql = xql.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;","&");
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
$Log$
Revision 1.9  2006/06/15 16:50:10  clq2
PAL_KEA_1612

Revision 1.8.2.2  2006/04/24 13:49:59  clq2
merged with Pal_KMB_XMLDB

Revision 1.7.22.3  2006/02/27 16:45:08  KevinBenson
get rid of some println statements

Revision 1.7.22.2  2006/02/27 16:41:08  KevinBenson
some changes on how the xql is generated

Revision 1.7.22.1  2006/02/09 13:30:25  KevinBenson
adding xmldb type war/service for the dsa

Revision 1.7  2005/11/21 12:54:18  clq2
DSA_KEA_1451

Revision 1.6.58.1  2005/11/14 16:40:32  kea
Fix to allow unit tests to find xslt stylesheet (included stylesheet
in pal-query.jar).  Made search for xslt a little more flexible too.

Revision 1.6  2005/03/21 18:31:51  mch
Included dates; made function types more explicit

Revision 1.5  2005/03/11 15:01:42  mch
DomHelper now traps ParserConfigException

Revision 1.4  2005/03/11 14:38:47  KevinBenson
changed it so it would compile

Revision 1.3  2005/03/10 16:42:55  mch
Split fits, sql and xdb

Revision 1.2  2005/03/10 13:57:32  KevinBenson
added its ability to get the xsl stylesheet

Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.2  2005/02/16 21:19:00  mch
started incoporating into maven

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.2.24.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.2  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.1.2.1  2004/10/15 19:59:06  mch
Lots of changes during trip to CDS to improve int test pass rate


 
*/
