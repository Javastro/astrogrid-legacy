/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import static org.astrogrid.desktop.modules.ui.scope.SsapRetrieval.BUST_MIME;
import junit.framework.TestCase;
/** Just some unit tests for parts of the parser - in particular the regexp.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 20, 200810:16:00 AM
 */
public class SsapRetrievalUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testSimpleMime() throws Exception {
        assertFalse("pattern shouldn't match a simple mime type",
                BUST_MIME.matcher("applications/fits").matches());
    }

    public void testBrokenMime() throws Exception {
        assertTrue("pattern should match a broken mime type",
                BUST_MIME.matcher("applications/fits;idiot").matches());
    }
    
    public void testParameterizedMime() throws Exception {
        assertFalse("pattern shouldn't match a mime type with parameters",
                BUST_MIME.matcher("applications/fits;format=wibble").matches());
    }
//    
//    public void testBrokenVotableMime() throws Exception {
//        assertFalse("pattern shouldn't match a broken mime type that mentions 'votable'",
//                BUST_MIME.matcher("applications/fits;votable").matches());
//        assertFalse("pattern shouldn't match a broken mime type that mentions 'votable'",
//                BUST_MIME.matcher("applications/fits;x-votable").matches());
//    }
    
}
