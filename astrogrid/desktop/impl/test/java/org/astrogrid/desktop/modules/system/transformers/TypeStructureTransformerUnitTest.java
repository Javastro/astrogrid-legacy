/*$Id: TypeStructureTransformerUnitTest.java,v 1.6 2009/03/26 18:01:23 nw Exp $
 * Created on 21-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;


import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.commons.collections.Transformer;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.acr.builtin.ValueDescriptor;

/** test for the type structure transformer.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Feb-2005
 */
public class TypeStructureTransformerUnitTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Transformer lazy = new Transformer() {
        	public Object transform(Object arg0) {
        		return trans.transform(arg0);
        	}
        };
        trans = new TypeStructureTransformer(lazy);
        
        l = new ArrayList();
        l.add("foo");
        l.add(new Integer(23));
        l.add(new Date());
        l.add(new URL("http://wibble.com"));
        
        m = new HashMap();
        m.put("x",new Integer(4));
        m.put("foo","wibble");
        
        a = new ABean();
        a.setChildren(new ArrayList());
        a.setValues(l);
        a.setFoo("foo");
        a.setAnotherBean(null);
        
        b = new BBean();
        b.setI(10);
        b.setJ(3.4F);
        b.setM(m);
        
    }
    
    protected Transformer trans;
    protected ABean a;
    protected BBean b;
    protected List l;
    protected Map m;
    
    @Override
    protected void tearDown() throws Exception {
    	super.tearDown();
    	trans = null;
    	a = null;
    	b = null;
    	l = null;
    	m = null;
    }
    
    public void testString() {
        assertEquals("foo",trans.transform("foo"));
        
    }
    
    public void testStringArray() {
        Object result = trans.transform(new String[]{"foo","bar"});
        checkBasics(result);
        assertEquals(Vector.class, result.getClass());
        assertEquals(2,((Vector)result).size());
        
    }

	/**
	 * @param result
	 */
	private void checkBasics(Object result) {
		assertNotNull(result);
	}
    
    public void testCollection() throws Exception {
       Object result = trans.transform(l);
       checkBasics(result);
       assertEquals(Vector.class,result.getClass());
       assertEquals(4,((Vector)result).size());
    }
    
    public void testMap() throws Exception {
        Object result = trans.transform(m);
        checkBasics(result);
        assertEquals(Hashtable.class,result.getClass());
        assertEquals(2,((Hashtable)result).size());
    }
    
    public void testBean() {
        Object result = trans.transform(a);
        checkBasics(result);
        assertEquals(Hashtable.class,result.getClass());
        //assertTrue(
        final Hashtable ht = (Hashtable)result;
        assertTrue(ht.containsKey("values"));
        assertTrue(ht.containsKey("children"));
        assertTrue(ht.containsKey("foo"));
        assertTrue(ht.containsKey("__interfaces"));
        assertEquals(4,ht.size());
        // verify the type informatio is correct
        Object o = ht.get("__interfaces");
        assertNotNull(o);
        assertTrue(o instanceof Vector);
        assertEquals(1,((Vector)o).size());
        assertEquals(ABean.class.getName(),((Vector)o).get(0));
        
    }
    
    public void testAwkwardBean() {
        Object result = trans.transform(b);
        checkBasics(result);   
        assertEquals(Hashtable.class,result.getClass());
        assertEquals(4,((Hashtable)result).size());         
    }
    
    public void testNestedBeans() {
        a.setAnotherBean(b);
        List list = new ArrayList();
        list.add(b);
        list.add(b);
        a.setChildren(list);
        Object result = trans.transform(a);
        checkBasics(result);
        assertEquals(Hashtable.class,result.getClass());
        assertEquals(5,((Hashtable)result).size());    
        Object oo = ((Hashtable)result).get("anotherBean");
        assertNotNull(oo);
        assertEquals(Hashtable.class,oo.getClass());
    }
    
    
    public void testListOfBeans() {
        List list = new ArrayList();
        list.add(a);
        list.add(b);
        Object result = trans.transform(list);
        checkBasics(result);
        assertEquals(Vector.class,result.getClass());
        assertEquals(2,((Vector)result).size());         
        
    }
    
    public void testModuleDescriptor() {
    	ModuleDescriptor md = new ModuleDescriptor();
    	md.setDescription("description");
    	md.setName("fred");
    	ComponentDescriptor c = new ComponentDescriptor();
    	c.setDescription("fred");
    	c.setName("foo");
    	c.setInterfaceClass(Runnable.class);
    	
    	MethodDescriptor m = new MethodDescriptor();
    	m.setDescription("foo");
    	m.setName("fred");
    	
    	ValueDescriptor vd = new ValueDescriptor();
    	vd.setDescription("fred");
    	vd.setName("foo");
    	vd.setType(Void.class);
    	vd.setUitype("void");
    	
    	m.setReturnValue(vd);
    	
    	c.addMethod(m);
    	
    	md.addComponent(c);
    	Object result = trans.transform(md);
    	checkBasics(result);
    	assertEquals(Hashtable.class,result.getClass());
    }
    
    public void testVoid() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    	// not possible - hard to get a void value
    }
    
    public void testNull() {
    	Object result = trans.transform(null);
    	assertEquals("null",result);
    }
    
    /** dunno if exceptions will ever be passed through this, but want to make sure that they don't beeak the system if this is the case */
    public void testTransformException() {
    	Exception e= new Exception("message");
    	Object result = trans.transform(e);
    	assertNotNull(result);
    	assertTrue(result instanceof Map);
    }
    
    public void testTransformByte() {
    	byte b = 3;
    	Byte input = new Byte(b);
    	Object result = trans.transform(input);
    	assertNotNull(result);
    	assertEquals(input,result);
    }
    
    public void testTransformByteArray() {
    	byte[] arr = "fred".getBytes();
    	Object result= trans.transform(arr);
    	assertNotNull(result);
    	assertEquals(arr,result);
    }
    
    public void testInterfaceInformation() {
        // check that the implementation class and the interfaces it impleemnts are reported.
        Object result = trans.transform(new CBean());
        assertNotNull(result);
        assertTrue(result instanceof Hashtable);
        Hashtable ht = (Hashtable)result;
        assertTrue(ht.containsKey("__interfaces"));
        Object ifaces = ht.get("__interfaces");
        assertNotNull(ifaces);
        assertTrue(ifaces instanceof Vector);
        Vector iVector = (Vector)ifaces;
        assertEquals(2,iVector.size());
        assertTrue(iVector.contains(CBean.class.getName()));
        assertTrue(iVector.contains(IInterface.class.getName()));
        
    }
    public void testInheritedInterfaceInformation() {
        // check that a subclass of this still reports the interfaces it's parent provides.
        Object result = trans.transform(new DBean());
        assertNotNull(result);
        assertTrue(result instanceof Hashtable);
        Hashtable ht = (Hashtable)result;
        assertTrue(ht.containsKey("__interfaces"));
        Object ifaces = ht.get("__interfaces");
        assertNotNull(ifaces);
        assertTrue(ifaces instanceof Vector);
        Vector iVector = (Vector)ifaces;
        assertEquals(3,iVector.size());
        assertTrue(iVector.contains(DBean.class.getName())); // implementation class.
        assertTrue(iVector.contains(JInterface.class.getName())); // interface implemented by this class
        assertTrue(iVector.contains(IInterface.class.getName())); // interface implemented by parent class
    }
    
    
    public static class ABean {
                public BBean getAnotherBean() {
            return this.anotherBean;
        }
        public void setAnotherBean(BBean anotherBean) {
            this.anotherBean = anotherBean;
        }
        public List getChildren() {
            return this.children;
        }
        public void setChildren(List children) {
            this.children = children;
        }
        public String getFoo() {
            return this.foo;
        }
        public void setFoo(String foo) {
            this.foo = foo;
        }
public List getValues() {
    return this.values;
}
public void setValues(List values) {
    this.values = values;
}
private List values;
        private String foo;
        private List children;
        private BBean anotherBean;
        
    }
    
    public static class BBean {
                public void setI(int i) {
    this.i = i;
}
        public void setJ(float j) {
            this.j = j;
        }
        public void setM(Map m) {
            this.m = m;
        }
public int getI() {
            return this.i;
        }
        public float getJ() {
            return this.j;
        }
        public Map getM() {
            return this.m;
        }
private int i;
        private float j;
        private Map m;
    }
    
    // classes juset used to verify type inheritance.
    public static interface IInterface {
    }
    
    public static class CBean implements IInterface {
    }
    
    public static interface JInterface {
    }
    
    public static class DBean extends CBean implements JInterface {
    }

}


/* 
$Log: TypeStructureTransformerUnitTest.java,v $
Revision 1.6  2009/03/26 18:01:23  nw
added override annotations

Revision 1.5  2007/10/07 10:41:17  nw
tested added serialization of classnames

Revision 1.4  2007/01/29 10:40:47  nw
documentation fixes.

Revision 1.3  2007/01/23 11:53:38  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.2  2007/01/09 16:12:20  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.2  2006/04/18 18:49:04  nw
version to merge back into head.

Revision 1.1.66.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.3  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/