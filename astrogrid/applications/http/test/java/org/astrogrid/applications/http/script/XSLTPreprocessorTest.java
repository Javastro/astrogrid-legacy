/* $Id: XSLTPreprocessorTest.java,v 1.2 2004/09/01 15:42:26 jdt Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 * Created on Aug 10, 2004
 *
 */
package org.astrogrid.applications.http.script;

import junit.framework.TestCase;

/**
 * JUnit test for xslt preprocessor
 * @author jdt
 */
public class XSLTPreprocessorTest extends TestCase {

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
     * Class under test for void XSLTPreprocessor(String)
     */
    public final void testXSLTPreprocessorString() {
        //@TODO Implement XSLTPreprocessor().
    }

    /*
     * Class under test for void XSLTPreprocessor()
     */
    public final void testXSLTPreprocessor() {
        Preprocessor preprocessor = new XSLTPreprocessor();
        preprocessor.process(null,null);
    }

    public final void testProcess() {
        //@TODO Implement process().
    }

}
