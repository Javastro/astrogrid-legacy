/*$Id: XsltConvertorTest.java,v 1.1 2003/11/11 14:43:33 nw Exp $
 * Created on 10-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap.response;


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2003
 *
 */
public class XsltConvertorTest extends AbstractTestConvertor {

    /**
     * Constructor for XsltConvertorTest.
     * @param arg0
     */
    public XsltConvertorTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(XsltConvertorTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testInput = "input.xml";
        this.expectedOutput = "value";
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.http2soap.response.AbstractConvertorTest#createConvertor()
     */
    protected ResponseConvertor createConvertor() {
        XsltConvertor conv = new XsltConvertor();
        conv.setXsltResource("transform.xsl");
        assertNotNull(conv.getStylesheetSource()); // check we've loaded the stylesheer
        return conv;
    }

}


/* 
$Log: XsltConvertorTest.java,v $
Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version
 
*/