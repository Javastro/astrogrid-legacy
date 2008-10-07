/**
 * 
 */
package org.astrogrid.acr.builtin;

import java.util.Iterator;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:06:14 PM
 */
public class ComponentDescriptorTest extends DescriptorTestAbstract {

    private ComponentDescriptor cd;

    protected void setUp() throws Exception {
        super.setUp();
        cd = new ComponentDescriptor();
        d = cd;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        cd = null;
    }
    
    public void testInterface() {
        final Class c = Object.class;
        cd.setInterfaceClass(c);
        assertEquals(c,cd.getInterfaceClass());
    }
    
    public void testMethodEmpty() {
        final MethodDescriptor[] methods = cd.getMethods();
        assertNotNull(methods);
        assertEquals(0,methods.length);
        
        assertFalse(cd.methodIterator().hasNext());        
    }
    
    public void testMethod() {
        final MethodDescriptor m1 = new MethodDescriptor();
        m1.setName("m1");
        final MethodDescriptor m2 = new MethodDescriptor();
        m2.setName("m2");
        
        cd.addMethod(m1);
        cd.addMethod(m2);
        
        assertEquals(m1,cd.getMethod(m1.getName()));
        assertEquals(m2,cd.getMethod(m2.getName()));
        
        final MethodDescriptor[] methods = cd.getMethods();
        assertNotNull(methods);
        assertEquals(2,methods.length);
        assertTrue(m1.equals(methods[0]) || m1.equals(methods[1]));
        assertTrue(m2.equals(methods[0]) || m2.equals(methods[1]));
        
        final Iterator it = cd.methodIterator();
        assertTrue(it.hasNext());
        Object o = it.next();
        assertTrue(o.equals(m1) || o.equals(m2));
        
        assertTrue(it.hasNext());
        o = it.next();
        assertTrue(o.equals(m1) || o.equals(m2));
        
        assertFalse(it.hasNext());
    }
    

}
