/*$Id: SQLUtils.java,v 1.5 2004/03/07 21:13:15 mch Exp $
 * Created on 07-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.sql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;


/** Collection of helper methods when working with SQL queries
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Jan-2004
 *
 */
public class SQLUtils {

   /** namespace used within astrogrid to identify the sql query language */
   public static final String SQL_XMLNS = "urn:sql";

   private SQLUtils() {
   }
   /**
    * convert a sql string object to an Element that can be used as the query body in a
     * {@link org.astrogrid.datacenter.delegate.FullSearcher}
    * @param sql
    * @return
    */
   public static Element toQueryBody(String sql) throws IOException {
      try {
         Document doc = DomHelper.newDocument();
         Element root = doc.createElementNS(SQL_XMLNS,"sql:sql");
         doc.appendChild(root);
         Text text = doc.createTextNode(sql);
         root.appendChild(text);
         return root;
      } catch (ParserConfigurationException pe) {
         throw new IOException("Parser Configuration failed:" + pe.getMessage());
      }
   }
   
   /** @deprecated - less robust, doesn't escape entities in text. use {@link #toQueryBody} instead */
   public static Element toQueryBodyOld(String sql) throws IOException {
      String docString = "<sql xmlns='" + SQL_XMLNS + "'>"
         + sql
         + "</sql>";
      InputStream is = new ByteArrayInputStream(docString.getBytes());
      try {
      Document doc = DomHelper.newDocument(is);
      return doc.getDocumentElement();
      } catch (ParserConfigurationException pe) {
         throw new IOException("Parser Configuration failed: " + pe.getMessage());
      } catch (SAXException se) {
         throw new IOException("Parse Failure: " + se.getMessage());
      }
      
   }

}


/*
$Log: SQLUtils.java,v $
Revision 1.5  2004/03/07 21:13:15  mch
Changed apache XMLUtils to implementation-independent DomHelper

Revision 1.4  2004/01/14 12:29:52  nw
improved documentation

Revision 1.3  2004/01/14 11:15:12  nw
changed constant to final.

Revision 1.2  2004/01/13 00:32:47  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.1.2.1  2004/01/08 09:10:20  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)
 
*/
