/*$Id: VizierQuerierTest.java,v 1.2 2004/03/16 01:32:35 mch Exp $
 * Created on 01-Dec-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.cds.querier;

import java.io.InputStream;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.spi.QuerierSPI;
import org.astrogrid.datacenter.queriers.spi.Translator;
import org.astrogrid.datacenter.queriers.spi.TranslatorMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Dec-2003
 *
 */
public class VizierQuerierTest extends ServerTestCase {

    /**
     * Constructor for VizierQuerierTest.
     * @param arg0
     */
    public VizierQuerierTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(VizierQuerierTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
//        spi = new VizierQuerierPlugin();
        // should set configuration and workspace here -- however, not needed by this plugin implementaiton.
    }
    protected QuerierSPI spi;

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        spi.close();
    }

    public void testName() {
        assertNotNull(spi.getPluginInfo());
    }
    
    /** chec;w there is at least one translator in the map */
    public void testTranslatorMap() {
        TranslatorMap map = spi.getTranslatorMap();
        assertTrue(map.list().length > 0);
    }
    
    /** do simplest query possible */
    public void testDoQuery() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("meta-all.xml");
        assertNotNull(is);
        Document doc = XMLUtils.newDocument(is);
        assertNotNull(doc);
        Element adql = doc.getDocumentElement();
        Translator trans = new AdqlVizierTranslator();
        QueryResults results = spi.doQuery(trans.translate(adql),trans.getResultType());
        assertNotNull(results);
//        Document votable = results.toVotable();
//        assertNotNull(votable);
        //assertIsVotable(votable); - don't work - as returns document conforming to votable _schema_ - assertion uses votable DTD.
    }

}


/*
$Log: VizierQuerierTest.java,v $
Revision 1.2  2004/03/16 01:32:35  mch
Fixed for cahnges to code to work with new plugins

Revision 1.1  2003/12/01 16:51:04  nw
added tests for cds spi
 
*/
