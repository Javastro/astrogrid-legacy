/*$Id: EgsoDelegateTest.java,v 1.1 2004/07/07 09:17:40 KevinBenson Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/ 
package org.astrogrid.datacenter.sec.secdelegate.egso;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * Exercises the methods of the vizier web service 
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Oct-2003
 * @todo test rest of methods - don't know what parameters to pass / good test queries.
 */
public class EgsoDelegateTest extends TestCase {

    /**
     * Constructor for VizierDelegateTest.
     * @param arg0
     */
    public EgsoDelegateTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(EgsoDelegateTest.class);
    }
    
    protected void setUp() throws Exception {
        del = new EgsoDelegate();
    }
    protected EgsoDelegate del;

    public void testDoQuery() throws ParserConfigurationException, SAXException, IOException {
        String testSQL = "SELECT * FROM sgas_event WHERE nar>9500 AND nar<9600";
        Document doc = del.doQuery(testSQL);        
        assertNotNull(doc);
        XMLUtils.PrettyDocumentToStream(doc,System.out);
        // check its a votable
        assertEquals("VOTABLE",doc.getDocumentElement().getLocalName());
    }
}


/* 
$Log: EgsoDelegateTest.java,v $
Revision 1.1  2004/07/07 09:17:40  KevinBenson
New SEC/EGSO proxy to query there web service on the Solar Event Catalog

Revision 1.2  2003/11/20 15:47:18  nw
improved testing

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.2  2003/11/18 11:09:24  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/