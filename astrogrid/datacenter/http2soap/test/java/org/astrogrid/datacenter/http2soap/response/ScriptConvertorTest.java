/*$Id: ScriptConvertorTest.java,v 1.1 2003/11/11 14:43:33 nw Exp $
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;



/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2003
 *
 */
public class ScriptConvertorTest extends AbstractTestConvertor {

    /**
     * Constructor for ScriptConvertorTest.
     * @param arg0
     */
    public ScriptConvertorTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ScriptConvertorTest.class);
    }

    /*
     * @see AbstractTestConvertor#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.testInput = "input.xml";
        this.expectedOutput = "hi there";
        InputStream scriptStream = this.getClass().getResourceAsStream("script.js");
        assertNotNull(scriptStream);
        InputStreamReader r = new InputStreamReader(scriptStream);
        StringWriter sw = new StringWriter();
        int c = 0;
        while ( ( c = r.read()) != -1) {
            sw.write(c);
        }
        this.script  = sw.toString();
        assertNotNull(script);        
        
    }
    protected String script;
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
        ScriptConvertor conv = new ScriptConvertor();
        conv.setLanguage("javascript");
        conv.setScript(script);
        return conv;
    }

}


/* 
$Log: ScriptConvertorTest.java,v $
Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version
 
*/