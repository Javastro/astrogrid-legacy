/*$Id: RegExpConvertorTest.java,v 1.1 2003/11/18 11:48:15 nw Exp $
 * Created on 10-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java.response;


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2003
 *
 */
public class RegExpConvertorTest extends AbstractTestConvertor {

    /**
     * Constructor for RexExpConvertorTest.
     * @param arg0
     */
    public RegExpConvertorTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(RegExpConvertorTest.class);
    }

    /*
     * @see AbstractTestConvertor#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testInput = "input.xml";
        this.expectedOutput = " more here ";
    }

    /*
     * @see AbstractTestConvertor#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.http2soap.response.AbstractTestConvertor#createConvertor()
     */
    protected ResponseConvertor createConvertor() {
        RegExpConvertor conv = new RegExpConvertor();
        conv.setExp("<b>(.*)</b>");
        return conv;
    }

}


/* 
$Log: RegExpConvertorTest.java,v $
Revision 1.1  2003/11/18 11:48:15  nw
mavenized http2java

Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version
 
*/