/*$Id: AbstractTestConvertor.java,v 1.1 2003/11/11 14:43:33 nw Exp $
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import junit.framework.TestCase;


/** Abstract test case for testing a convertor
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2003
 *
 */
public abstract class AbstractTestConvertor extends TestCase {

    /**
     * Constructor for AbstractConvertorTest.
     * @param arg0
     */
    public AbstractTestConvertor(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AbstractTestConvertor.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

    }
    
    /** extenders to set this to resource-name of input file */
    protected String testInput;
    /** extenders to set this to expected output value*/
    protected String expectedOutput;
    
    protected abstract ResponseConvertor createConvertor() ;
    protected ReadableByteChannel input  = null;
    protected WritableByteChannel output = null;
    protected ByteArrayOutputStream buff = null;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    protected ReadableByteChannel getInput(String resource) throws Exception {
        InputStream is = this.getClass().getResourceAsStream(resource);
        assertNotNull(is);
        return Channels.newChannel(is);
    }
    
    protected WritableByteChannel createOutput() throws Exception {
        buff = new ByteArrayOutputStream();
        assertNotNull(buff);
        return Channels.newChannel(buff);
    }

    public void testConvertor() throws Exception{
        assertNotNull(testInput);
        assertNotNull(expectedOutput);
        ResponseConvertor conv = createConvertor();
        input = getInput(testInput);
        output = createOutput();
        assertNotNull(input);
        assertNotNull(output);
        conv.convertResponse(input,output);
        // result should be in output stream;
        String result = buff.toString();
        assertNotNull(result);
        System.out.println(result);
        assertEquals(expectedOutput,result);
    }

}


/* 
$Log: AbstractTestConvertor.java,v $
Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version
 
*/