/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import junit.framework.TestCase;

/** unit test for parts of HtmlServlet.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 18, 200811:53:10 AM
 */
public class HtmlServletUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testPassThru() throws Exception {
        final String ip = "foo";
        final String op = HtmlServlet.summarize(ip);
        assertEquals(ip,op);
    }
    
    public void testEmptyInput() throws Exception {
        final String ip = "";
        final String op = HtmlServlet.summarize(ip);
        assertEquals(ip,op);
    }    

    public void testStripLeadingWS() throws Exception {
        final String ip = "   foo";
        final String op = HtmlServlet.summarize(ip);
        assertEquals("foo",op);
    }
    
    public void testFullStop() throws Exception {
        final String ip = "foo. more <p>here\n too";
        final String op = HtmlServlet.summarize(ip);
        assertEquals("foo",op);
    }
    
    public void testNewline() throws Exception {
        final String ip = "foo\n bar";
        final String op = HtmlServlet.summarize(ip);
        assertEquals("foo",op);
    }
    
    public void testParaTag() throws Exception {
        final String ip = "<b>foo bar</b><p> some. \nmore";
        final String op = HtmlServlet.summarize(ip);
        assertEquals("<b>foo bar</b>",op);
    }
    
    public void testDlTag() throws Exception {
        final String ip = "<b>foo bar</b><dl> some <p> . \nmore";
        final String op = HtmlServlet.summarize(ip);
        assertEquals("<b>foo bar</b>",op);
    }

    
    public void testFullStopStripLead() throws Exception {
        final String ip = " AR system service : foo. more <p>here\n too";
        final String op = HtmlServlet.summarize(ip);
        assertEquals("foo",op);
    }
    
    public void testNewlineStripLead() throws Exception {
        final String ip = " ar service:foo\n bar";
        final String op = HtmlServlet.summarize(ip);
        assertEquals("foo",op);
    }
    
    public void testParaTagStripLead() throws Exception {
        final String ip = "AR System Service:<b>foo bar</b><p> some. \nmore";
        final String op = HtmlServlet.summarize(ip);
        assertEquals("<b>foo bar</b>",op);
    }
    
    public void testDlTagStripLead() throws Exception {
        final String ip = "  ar sERVICE  : <b>foo bar</b><dl> some <p> . \nmore";
        final String op = HtmlServlet.summarize(ip);
        assertEquals("<b>foo bar</b>",op);
    }
    
}
