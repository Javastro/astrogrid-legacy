/*$Id: XMLHelper.java,v 1.2 2004/03/14 23:11:32 nw Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting;

import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
//import org.astrogrid.datacenter.sql.SQLUtils;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

/** helper methods for working with xml.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class XMLHelper {
    /** Construct a new XMLHelper
     * 
     */
    public XMLHelper() {
        super();
    }

    // helper methods related to datacenter - maybe these are better moved into a child object?
    /** convert an adql query to an Element
     * @see org.astrogrid.datacenter.adql.ADQLUtils */
    public Element toQueryBody(Select s) throws ADQLException {
       return ADQLUtils.toQueryBody(s);
    }
    /*convert an sql string query to an Element
     * @see org.astrogrid.datacenter.sql.SQLUtils 
    public Element toQueryBody(String s) throws IOException {
       return SQLUtils.toQueryBody(s);
    }
*/


    // XML helper methods
    /** parse contents of input stream into document */
    public Document newDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException {
       return XMLUtils.newDocument(is);
    }
    /** parse contents of string into document */
    public Document newDocument(String s) throws ParserConfigurationException, SAXException, IOException {
       InputStream is = new ByteArrayInputStream(s.getBytes());
       return XMLUtils.newDocument(is);
    }
    /** create new DOM Document */
    public Document newDocument() throws ParserConfigurationException {
       return XMLUtils.newDocument();
    }
    /** convert document to string */
    public String documentToString(Document doc) {
       return XMLUtils.DocumentToString(doc);
    }
    /** convert element to string */
    public String elementToString(Element el) {
       return XMLUtils.ElementToString(el);
    }
    
}


/* 
$Log: XMLHelper.java,v $
Revision 1.2  2004/03/14 23:11:32  nw
commented out code that used methods that have dissapeared from datacenter and applications delegate jars

Revision 1.1  2004/03/12 13:50:23  nw
improved scripting object
 
*/