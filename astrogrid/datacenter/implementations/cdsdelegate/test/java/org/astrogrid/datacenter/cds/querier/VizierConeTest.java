/*$Id: VizierConeTest.java,v 1.2 2004/03/16 01:32:35 mch Exp $
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

import org.astrogrid.datacenter.cdsdelegate.vizier.VizierDelegate;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Dec-2003
 *
 */
public class VizierConeTest extends TestCase {

    /**
     * Constructor for VizierQueryTest.
     * @param arg0
     */
    public VizierConeTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(VizierConeTest.class);
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

    /*
    public void testCantQueryEmptyCone() throws Exception{
        VizierQuery c = new VizierQuery();
        try {
            c.doDelegateQuery(new VizierDelegate());
            fail("expected to barf");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }
     */
}


/*
$Log: VizierConeTest.java,v $
Revision 1.2  2004/03/16 01:32:35  mch
Fixed for cahnges to code to work with new plugins

Revision 1.1  2003/12/01 16:51:04  nw
added tests for cds spi
 
*/
