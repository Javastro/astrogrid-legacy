/*$Id: BuilderTest.java,v 1.1 2003/11/18 11:48:15 nw Exp $
 * Created on 10-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java.builder;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;

import junit.framework.TestCase;

import org.w3c.dom.Element;


/** Test class for the various builder routines
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2003
 *
 */
public class BuilderTest extends TestCase {

    /**
     * Constructor for BuilderTest.
     * @param arg0
     */
    public BuilderTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(BuilderTest.class);
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
    


    protected void testBuilder(ResultBuilder rb, String input,Class expectedType,Object expectedValue) 
        throws Exception{
            ReadableByteChannel in = mkChannel(input);
       Object o = rb.build(in);
       assertNotNull(o);
       assertEquals(expectedType,o.getClass());
       assertEquals(expectedValue,o);
           
    }

    private ReadableByteChannel mkChannel(String str)
        throws UnsupportedEncodingException {
        ReadableByteChannel in = Channels.newChannel(new ByteArrayInputStream(str.getBytes("UTF-8")));        
        return in;
    }
    public void testStringBuilder() throws Exception{
        ResultBuilder rb = new StringBuilder();
        testBuilder(rb,"test string",String.class,"test string");
    }

    public void testBooleanBuilder() throws Exception {
        ResultBuilder rb = new BooleanBuilder();
        testBuilder(rb,"true",Boolean.class,Boolean.TRUE);
        testBuilder(rb,"false",Boolean.class,Boolean.FALSE);
    }
    
    

    
    public void testElementBuilder() throws Exception {
        ResultBuilder rb = new ElementBuilder();
        String xml = "<?xml version='1.0' encoding='UTF-8'?><test><xml-doc /></test>";
        Object result = rb.build(mkChannel(xml));
        assertNotNull(result);
        assertTrue(result instanceof Element);
        assertEquals("test", ( (Element)result).getLocalName());
    }
    
    public void testFloatBuilder() throws Exception {
        ResultBuilder rb = new FloatBuilder();
        testBuilder(rb,"5",Float.class,new Float("5"));
        testBuilder(rb,"0.003",Float.class,new Float("0.003"));
    }
    
    public void testIntBuilder() throws Exception {
        ResultBuilder rb = new IntBuilder();
        testBuilder(rb,"0",Integer.class,new Integer("0"));
        testBuilder(rb,"12345",Integer.class,new Integer("12345"));
    }
    
    public void testVoidBuilder() throws Exception {
        ResultBuilder rb = new VoidBuilder();
        ReadableByteChannel in = mkChannel("nothing");
        Object o = rb.build(in);
        assertNull(o);
    }

    public void testBytesBuilder() throws Exception {
        ResultBuilder rb = new BytesBuilder();
        byte[] bytes = "test bytes".getBytes();
        ReadableByteChannel is = Channels.newChannel(new ByteArrayInputStream(bytes));
        Object result = rb.build(is);
        assertNotNull(result);
        assertTrue(Arrays.equals((byte[])result,bytes));
    }
    

}


/* 
$Log: BuilderTest.java,v $
Revision 1.1  2003/11/18 11:48:15  nw
mavenized http2java

Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version
 
*/