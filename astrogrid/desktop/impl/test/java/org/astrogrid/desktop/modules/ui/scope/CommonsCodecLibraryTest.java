/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.net.URLEncoder;

import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.net.QCodec;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.net.URLCodec;

import junit.framework.TestCase;

/** test behaviour of various codecs
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 8, 20075:46:46 PM
 */
public class CommonsCodecLibraryTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        encoder = new QCodec();
    }
    
    StringEncoder encoder;

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    public void testEncoder() throws Exception {
        System.out.println(encoder.encode("foo bar cho?oo"));
        System.out.println(URLEncoder.encode("foo bar choo"));
    }

}
