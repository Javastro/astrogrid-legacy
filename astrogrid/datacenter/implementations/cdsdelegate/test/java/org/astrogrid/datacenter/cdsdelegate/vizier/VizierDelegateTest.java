/*$Id: VizierDelegateTest.java,v 1.2 2003/11/20 15:47:18 nw Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/ 
package org.astrogrid.datacenter.cdsdelegate.vizier;

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
public class VizierDelegateTest extends TestCase {

    /**
     * Constructor for VizierDelegateTest.
     * @param arg0
     */
    public VizierDelegateTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(VizierDelegateTest.class);
    }
    
    protected void setUp() throws Exception {
        del = new VizierDelegate();
    }
    protected VizierDelegate del;

    public void testMetaAll() throws ParserConfigurationException, SAXException, IOException {
        Document doc = del.metaAll();
        assertNotNull(doc);
        // check its a votable
        assertEquals("VOTABLE",doc.getDocumentElement().getLocalName());
       // XMLUtils.PrettyDocumentToStream(doc,System.out);
    }
        
    public void testCatalogueMetaData() throws Exception {
        Target t = new NamedTarget("SN 1987A");
        Document doc = del.cataloguesMetaData(t,1.0,Unit.ARCSEC);
        assertNotNull(doc);
        assertEquals("VOTABLE",doc.getDocumentElement().getLocalName());
       // XMLUtils.PrettyDocumentToStream(doc,System.out);
    }
    
    public void testCatalogueData() throws Exception {
        Target t = new NamedTarget("SN 1987A");
        Document doc = del.cataloguesData(t,1.0,Unit.ARCSEC,Wavelength.IR);
        assertNotNull(doc);
        assertEquals("VOTABLE",doc.getDocumentElement().getLocalName());
        //XMLUtils.PrettyDocumentToStream(doc,System.out);
    }

}


/* 
$Log: VizierDelegateTest.java,v $
Revision 1.2  2003/11/20 15:47:18  nw
improved testing

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.2  2003/11/18 11:09:24  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/