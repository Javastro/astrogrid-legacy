/*$Id: TypeStructureTransformerTest.java,v 1.3 2005/08/05 11:46:56 nw Exp $
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


import org.apache.commons.collections.Transformer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/** test for the xmltransformer code.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 *
 */
public class TypeStructureTransformerTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        trans = TypeStructureTransformer.getInstance();
        
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
    
    
    public void testString() {
        assertEquals("foo",trans.transform("foo"));
    }
    
    public void testStringArray() {
        Object result = trans.transform(new String[]{"foo","bar"});
        assertNotNull(result);
        System.out.println(result);
    }
    
    public void testCollection() throws Exception {
       Object result = trans.transform(l);
       assertNotNull(result);
       System.out.println(result);      
    }
    
    public void testMap() throws Exception {
        Object result = trans.transform(m);
        assertNotNull(result);
        System.out.println(result);
    }
    
    public void testBean() {
        Object result = trans.transform(a);
        assertNotNull(result);
        System.out.println(result);        
        
    }
    
    public void testAwkwardBean() {
        Object result = trans.transform(b);
        assertNotNull(result);
        System.out.println(result);           
    }
    
    public void testNestedBeans() {
        a.setAnotherBean(b);
        List list = new ArrayList();
        list.add(b);
        list.add(b);
        a.setChildren(list);
        Object result = trans.transform(a);
        assertNotNull(result);
        System.out.println(result);         
    }
    
    
    public void testListOfBeans() {
        List list = new ArrayList();
        list.add(a);
        list.add(b);
        Object result = trans.transform(list);
        assertNotNull(result);
        System.out.println(result);            
        
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

}


/* 
$Log: TypeStructureTransformerTest.java,v $
Revision 1.3  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/