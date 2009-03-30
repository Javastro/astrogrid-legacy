/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:18:11 PM
 */
public class ParamHttpInterfaceTest extends InterfaceTest {

    private ParamHttpInterface pi;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pi = new ParamHttpInterface();
        i = pi;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testQueryType() throws Exception {
        final String t= "a type";
        pi.setQueryType(t);
        assertEquals(t,pi.getQueryType());
    }
    
    public void testResultRType() throws Exception {
        final String t = "another tp";
        pi.setResultType(t);
        assertEquals(t,pi.getResultType());
    }
    
    public void testParams() throws Exception {
        final InputParam[] arr = new InputParam[] {
        };
        pi.setParams(arr);
        assertSame(arr,pi.getParams());
    }

}
