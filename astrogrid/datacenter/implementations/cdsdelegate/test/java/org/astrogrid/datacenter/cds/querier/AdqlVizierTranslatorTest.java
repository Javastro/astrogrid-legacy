/*$Id: AdqlVizierTranslatorTest.java,v 1.2 2004/03/16 01:32:34 mch Exp $
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
import org.astrogrid.datacenter.cdsdelegate.vizier.DecimalDegreesTarget;
import org.astrogrid.datacenter.cdsdelegate.vizier.NamedTarget;
import org.astrogrid.datacenter.cdsdelegate.vizier.Unit;
import org.astrogrid.datacenter.cdsdelegate.vizier.Wavelength;
import org.astrogrid.datacenter.queriers.spi.Translator;
import org.w3c.dom.Document;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Dec-2003
 *
 */
public class AdqlVizierTranslatorTest extends ServerTestCase {

    /**
     * Constructor for AdqlVizierTranslatorTest.
     * @param arg0
     */
    public AdqlVizierTranslatorTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AdqlVizierTranslatorTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testMetaAll() throws Exception {
        VizierQuery vc = buildCone("meta-all.xml");
        assertTrue(vc.metaData);
        assertNull(vc.target);
        assertNull(vc.unit);
    }
    
    public void testNamedTarget() throws Exception {
        VizierQuery vc = buildCone("named-target.xml");
        assertFalse(vc.metaData);
        assertNotNull(vc.target);
        assertTrue(vc.target instanceof NamedTarget);
        assertNotNull(vc.unit);
        assertEquals(vc.unit,Unit.ARCSEC);
        assertTrue(vc.radius > 0.0);
    }
    
    public void testCircleTarget() throws Exception {
        VizierQuery vc = buildCone("circle-target.xml");
        assertTrue(vc.metaData);
        assertNotNull(vc.target);
        assertTrue(vc.target instanceof DecimalDegreesTarget);
        assertNotNull(vc.unit);
        assertEquals(vc.unit,Unit.ARCMIN);
        assertTrue(vc.radius > 5.0);
        assertNotNull(vc.wavelength);
        assertEquals(vc.wavelength,Wavelength.IR);
    }
    
    
    
    protected VizierQuery buildCone(String resource) throws Exception {
        InputStream is = this.getClass().getResourceAsStream(resource);
        assertNotNull(is);
        Document doc = XMLUtils.newDocument(is);
        assertNotNull(doc);
        Translator trans = new AdqlVizierTranslator();
        Object o = trans.translate(doc.getDocumentElement());
        assertTrue(o instanceof VizierQuery);
        return (VizierQuery)o;
        
    }

}


/*
$Log: AdqlVizierTranslatorTest.java,v $
Revision 1.2  2004/03/16 01:32:34  mch
Fixed for cahnges to code to work with new plugins

Revision 1.1  2003/12/01 16:51:04  nw
added tests for cds spi
 
*/
