/**
 * 
 */
package org.astrogrid.acr.builtin;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:07:24 PM
 */
public class ValueDescriptorTest extends DescriptorTestAbstract {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        vd = new ValueDescriptor();
        d = vd;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        vd= null;
    }
    
    ValueDescriptor vd;

    public void testType() {
        final Class c = Object.class;
        vd.setType(c);
        assertEquals(c,vd.getType());
    }
    
    public void testUiType() {
        final String uiType = "ui type";
        vd.setUitype(uiType);
        assertEquals(uiType,vd.getUitype());
    }
    
    
}
