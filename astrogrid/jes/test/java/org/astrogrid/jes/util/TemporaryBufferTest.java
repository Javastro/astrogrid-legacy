/*$Id: TemporaryBufferTest.java,v 1.2 2004/11/05 16:52:42 jdt Exp $
 * Created on 03-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.util;

import org.astrogrid.io.Piper;

import java.io.*;
import java.util.Arrays;

import junit.framework.TestCase;

/** tests for the temporary buffer class.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Nov-2004
 *
 */
public class TemporaryBufferTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        b = new TemporaryBuffer();
    }
    
    protected TemporaryBuffer b;

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /** tests behaviour of java spec - verify that things work the way I think they do. */
    public void testVariableAssignmentInNestedClasses() {
        ExampleNestedClasses e = new ExampleNestedClasses();
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        
        assertEquals(one,e.getI());
        assertEquals(one, e.a.getI());
        assertEquals(one,e.b.getI());
        
        e.b.setI(two);
        
        // am relying that a zap of a variable of the containing class in a nested class is visible in other existing nested classes - 
        // depends how the impl references these vars
        
        assertEquals(two, e.getI());
        assertEquals(two,e.a.getI());
        assertEquals(two,e.b.getI());
        
    }
    
    public void testEmpty() {
        b.readMode();
        assertEquals("",b.getContents());
    }
    
    public void testToString() {
        System.out.println(b.toString());
    }
    
    public void testStateLogic() {
        b.readMode();
        assertNotNull(b.getInputStream());
        assertNotNull(b.getReader());
        try {
            b.getOutputStream();
            fail("expected this to fail");
        } catch (IllegalStateException e) {
            // ok.
        }
        try {
            b.getWriter();
            fail("expected this to fail");
        } catch (IllegalStateException e) {
            // ok.
        }        
        // now the other way round.
        b.writeMode();
        assertNotNull(b.getOutputStream());
        assertNotNull(b.getWriter());
        try {
            b.getInputStream();
            fail("expected this to fail");
        } catch (IllegalStateException e) {
            // ok.
        }
        try {
            b.getReader();
            fail("expected this to fail");
        } catch (IllegalStateException e) {
            // ok.
        }            
    }
    
    public void testStreams() throws IOException {
        InputStream source = this.getClass().getResourceAsStream("img.jpg");
        assertNotNull(source);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        Piper.pipe(source, bs);
        source.close();
        bs.close();
        
        // the test                
        b.writeMode();
        OutputStream o = b.getOutputStream();
        InputStream input = new ByteArrayInputStream(bs.toByteArray()); 
        Piper.pipe(input,o);
        input.close();
        o.close();
        
        b.readMode();
        InputStream is = b.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Piper.pipe(is,output);
        is.close();
        output.close();
        // check results
        assertTrue(b.buff.capacity() > b.START_CAPACITY); // assert buffer has grown.
        assertTrue(Arrays.equals(bs.toByteArray(),output.toByteArray()));        
    }
    
    public void testReaders() throws IOException {
        // first of all load in a text.
        InputStream source = this.getClass().getResourceAsStream("largetext.txt");
        assertNotNull(source);
        StringWriter sw = new StringWriter();
        Piper.pipe(new InputStreamReader(source),sw);
        source.close();
        sw.close();
        
        //now do the test.
        b.writeMode();
        Writer w = b.getWriter();
        StringReader input = new StringReader(sw.toString());
        Piper.pipe(input,w);
        input.close();
        w.close();
        
        b.readMode();
        Reader r = b.getReader();
        StringWriter output =new StringWriter();
        Piper.pipe(r,output);
        r.close();
        output.close();
        // check results.
        System.out.println(b);
        assertTrue(b.buff.capacity() > b.START_CAPACITY); // assert buffer has grown.
        assertEquals(sw.toString(),output.toString());        
        
        
    }
 
    public void testRepeated() throws IOException {
        testStreams();
        testStreams();
        testReaders();
        testReaders();
    }
    
    public void testGetContents() throws IOException {
        // trying to recreate a but I saw in the code.
        b.writeMode();
        PrintWriter pw = new PrintWriter(b.getWriter());
        pw.print("hello");
        pw.close();
        b.readMode();
        assertEquals("hello",b.getContents());
    }
    

    public void testEncodings() throws IOException {
        b.writeMode();       
        PrintStream pw = new PrintStream( b.getOutputStream(),true,"UTF-16");       
        pw.print("hello");
        pw.close();
        b.readMode();
        assertEquals("hello",b.getContents("UTF-16"));
    }

}


/* 
$Log: TemporaryBufferTest.java,v $
Revision 1.2  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.1.2.1  2004/11/05 15:46:41  nw
tests for static buffer, and resources
 
*/