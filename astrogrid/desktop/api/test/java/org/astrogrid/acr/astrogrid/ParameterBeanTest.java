/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import org.astrogrid.acr.ivoa.resource.BaseParamTest;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:01:00 PM
 */
public class ParameterBeanTest extends BaseParamTest {

    protected void setUp() throws Exception {
        super.setUp();
        pb = new ParameterBean();
        bp = pb;
    }
    
    ParameterBean pb;

    protected void tearDown() throws Exception {
        super.tearDown();
        pb = null;
    }
    
    public void testId() throws Exception {
       final String name= " name";
        pb.setId(name);
        assertEquals(name,pb.getId());
        assertEquals(name,pb.getName()); // legacy method
    }
    
    public void testOption() throws Exception {
        final String[] opts  = new String[] {
                "opts","opts1"
        };
        pb.setOptions(opts);
        assertSame(opts,pb.getOptions());
    }
    
    public void testType() throws Exception {
        final String t = "ttype";
        pb.setType(t);
        assertEquals(t,pb.getType());
    }
    /** 
     * overridden test for name - as behaviour differs.
     */
    @Override
    public void testName() throws Exception {
        final String uin = "uin ame";
        pb.setName(uin);
        assertEquals(uin,pb.getUiName());
    }
    
    public void testArraySize() throws Exception {
        final String as = "1x4x3";
        pb.setArraysize(as);
        assertEquals(as,pb.getArraysize());
    }
    
    public void testDefaultValues() throws Exception {
        final String[] vs = new String[] {
                "foo","nar"
        };
        pb.setDefaultValues(vs);
        assertSame(vs,pb.getDefaultValues());
    }
    
    public void testMimeType() throws Exception {
        final String mime = "mime";
        pb.setMimeType(mime);
        assertEquals(mime,pb.getMimeType());
        
    }
    
    public void testUType() throws Exception {
        final String ut = "utypes";
        pb.setUType(ut);
        assertEquals(ut,pb.getUType());
    }
}
