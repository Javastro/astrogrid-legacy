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
                 //                 (" (hello) (1 ,(2)) `(3 ,(4))  there \"how are) you?\" (\"1\") ,(\"2\") ,@(\"3\") (\"x)y\") \"q\\\"t\" 'ping #f (a)\"b\"hi'fi,ve(there)")
                 (" (hello) (1 ,(2)) `(3 ,(4))  there \"how are) you?\" (\"1\") ,(\"2\") ,@(\"3\") (\"x)y\") \"q\\\"t\" 'ping #f (a)\"b\"hi'fi,ve(there) #\\; (define x #\\;)")
);
        assertEquals("(hello)",           ss.readSexp());
        assertEquals("(1 ,(2))",          ss.readSexp());
        assertEquals("`(3 ,(4))",         ss.readSexp());
        assertEquals("there",             ss.readSexp());
        assertEquals("\"how are) you?\"", ss.readSexp());
        assertEquals("(\"1\")",           ss.readSexp());
        assertEquals(",(\"2\")",          ss.readSexp());
        assertEquals(",@(\"3\")",         ss.readSexp());
        assertEquals("(\"x)y\")",         ss.readSexp());
        assertEquals("\"q\\\"t\"",        ss.readSexp());
        assertEquals("'ping",             ss.readSexp());
        assertEquals("#f",                ss.readSexp());
        assertEquals("(a)",               ss.readSexp());
        assertEquals("\"b\"",             ss.readSexp());
        assertEquals("hi",                ss.readSexp());
        assertEquals("'fi",               ss.readSexp());
        assertEquals(",ve",               ss.readSexp());
        assertEquals("(there)",           ss.readSexp());
        assertEquals("#\\;",              ss.readSexp());
        assertEquals("(define x #\\;)",   ss.readSexp());
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
        String[] testcases = { ")",          // too many right parens
                               "(one (two)", // too few right parens
                               "\"hell",     // unterminated quote
        };
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
