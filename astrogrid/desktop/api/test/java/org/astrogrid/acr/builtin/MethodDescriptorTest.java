/**
 * 
 */
package org.astrogrid.acr.builtin;

import java.util.Iterator;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:06:36 PM
 */
public class MethodDescriptorTest extends DescriptorTestAbstract {

    private MethodDescriptor md;

    protected void setUp() throws Exception {
        super.setUp();
        md = new MethodDescriptor();
        d = md;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        md = null;
    }
    
    public void testReturnValue() {
        final ValueDescriptor vd = new ValueDescriptor();
        md.setReturnValue(vd);
        assertEquals(vd,md.getReturnValue());
    }
    
    public void testParametersEmpty() {
        final ValueDescriptor[] parameters = md.getParameters();
        assertNotNull(parameters);
        assertEquals(0,parameters.length);
        assertFalse(md.parameterIterator().hasNext());
    }

    
    public void testParameters() {
        final ValueDescriptor v1 = new ValueDescriptor();
        v1.setName("v1");
        final ValueDescriptor v2 = new ValueDescriptor();
        v2.setName("v2");
        
        md.addParameter(v1);
        md.addParameter(v2);
        
        final ValueDescriptor[] parameters = md.getParameters();
        assertNotNull(parameters);
        assertEquals(2,parameters.length);
        assertTrue(v1.equals(parameters[0]) || v1.equals(parameters[1]));
        assertTrue(v2.equals(parameters[0]) || v2.equals(parameters[1]));

        final Iterator it = md.parameterIterator();
        assertTrue(it.hasNext());
        Object o = it.next();
        assertTrue(o.equals(v1) || o.equals(v2));

        assertTrue(it.hasNext());
        o = it.next();
        assertTrue(o.equals(v1) || o.equals(v2));      

        assertFalse(it.hasNext());
        
        
    }    
    
}
