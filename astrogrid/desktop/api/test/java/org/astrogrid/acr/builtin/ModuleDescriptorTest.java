/**
 * 
 */
package org.astrogrid.acr.builtin;

import java.util.Iterator;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:07:00 PM
 */
public class ModuleDescriptorTest extends DescriptorTestAbstract {

    protected void setUp() throws Exception {
        super.setUp();
        md = new ModuleDescriptor();
        d = md;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        md = null;
    }
    ModuleDescriptor md;
    
    public void testComponentsEmpty() {
        assertFalse(md.componentIterator().hasNext());
        final ComponentDescriptor[] components = md.getComponents();
        assertNotNull(components);
        assertEquals(0,components.length);
        
    }
    
    public void testComponents() {
        final ComponentDescriptor c1 = new ComponentDescriptor();
        c1.setName("c1");
        final ComponentDescriptor c2 = new ComponentDescriptor();
        c2.setName("c2");
        
        md.addComponent(c1);
        md.addComponent(c2);

        final ComponentDescriptor[] components = md.getComponents();
        assertNotNull(components);
        assertEquals(2,components.length);
        
        assertTrue(c1.equals(components[0]) || c1.equals(components[1]));
        assertTrue(c2.equals(components[0]) || c2.equals(components[1]));
        
        final Iterator it = md.componentIterator();
        assertTrue(it.hasNext());
        Object o = it.next();
        assertTrue(o.equals(c1) || o.equals(c2));
        assertTrue(it.hasNext());
        o = it.next();
        assertTrue(o.equals(c1) || o.equals(c2));        
        assertFalse(it.hasNext());
        
    }
    

}
