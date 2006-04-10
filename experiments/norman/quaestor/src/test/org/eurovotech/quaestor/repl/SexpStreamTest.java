// JUnit test cases for SexpStream

package org.eurovotech.quaestor.repl;

import java.io.StringReader;

import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.AssertionFailedError;

public class SexpStreamTest
        extends TestCase {

    public SexpStreamTest(String name) {
        super(name);
    }

    public void testRead()
            throws Exception {
        SexpStream ss = new SexpStream
                (new StringReader
                 (" (hello) (wibble (x))  there \"how are) you?\"  'ping #f (x)(y)hi(there)"));
        assertEquals("(hello)",           ss.readSexp());
        assertEquals("(wibble (x))",      ss.readSexp());
        assertEquals("there",             ss.readSexp());
        assertEquals("\"how are) you?\"", ss.readSexp());
        assertEquals("'ping",             ss.readSexp());
        assertEquals("#f",                ss.readSexp());
        assertEquals("(x)",               ss.readSexp());
        assertEquals("(y)",               ss.readSexp());
        assertEquals("hi",                ss.readSexp());
        assertEquals("(there)",           ss.readSexp());
        assertNull(ss.readSexp());
    }

    public void testComments()
            throws Exception {
        SexpStream ss = new SexpStream
                (new StringReader
                 ("(hello ; now\nthere) (how;x\ngoes) ; wibble"));
        assertEquals("(hello \nthere)", ss.readSexp());
        assertEquals("(how\ngoes)",    ss.readSexp());
        assertNull(ss.readSexp());
    }

    /**
     * Test that each object is successfully read when it's terminated
     * by EOF
     */
    public void testLast()
            throws Exception {
        String[] testcases = {
            "(hello)", "\"hello\"", "'hello", "hello", "#f" 
        };
        for (int i=0; i<testcases.length; i++) {
            SexpStream ss = new SexpStream(new StringReader(testcases[i]));
            assertEquals(testcases[i], ss.readSexp());
        }
    }

    /**
     * Test that an empty input returns EOF without error
     */
    public void testEmpty()
            throws Exception {
        SexpStream ss = new SexpStream(new StringReader("   "));
        assertNull(ss.readSexp());
    }

    public void testFailures()
            throws Exception {
        String[] testcases = { ")", "(one (two)", "\"hell" };
        for (int i=0; i<testcases.length; i++) {
            SexpStream ss = new SexpStream(new StringReader(testcases[i]));
            try {
                String s = ss.readSexp();
                throw new AssertionFailedError("Wrong: read <" + testcases[i]
                                               + "> as <" + s + ">");
            } catch (java.io.IOException e) {
                // OK
            }
        }
    }
}
