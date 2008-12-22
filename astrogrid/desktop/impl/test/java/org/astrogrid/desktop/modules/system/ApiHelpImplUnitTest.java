/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import junit.framework.TestCase;

/** unit tests for api help.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 16, 20083:29:26 PM
 */
public class ApiHelpImplUnitTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testStripHTML1() throws Exception {
        assertEquals("",ApiHelpImpl.stripHTML("<B>"));
        
    }
    
    public void testStripHTML2() throws Exception {
        assertEquals("",ApiHelpImpl.stripHTML("<B><BR>"));        
        
    }

    public void testStripHTML3() throws Exception {
        assertEquals("a b c",ApiHelpImpl.stripHTML("a<B> b <BR>c"));  
        
    }

    public void testStripHTML5() throws Exception {
        assertEquals("a b c",ApiHelpImpl.stripHTML("a<B> b<B href='foo'> <p/><BR>c"));  
        
    }
        
    
    public void testStripHTML4() throws Exception {
        assertEquals("<type>foo</type>",ApiHelpImpl.stripHTML("<B><type>foo</type><BR>"));          
        
    }    
    
    public void testStripHTML6() throws Exception {
        assertEquals("<type>foo</type>",ApiHelpImpl.stripHTML("<B><type>foo</type><BR></B><i>"));          
        
    }        
    
}
