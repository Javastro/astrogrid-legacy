/*$Id: ReflectionHelperUnitTest.java,v 1.3 2008/04/23 11:22:37 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class ReflectionHelperUnitTest extends TestCase {

    public void testGetMethodByName() throws NoSuchMethodException {
       Method m =  ReflectionHelper.getMethodByName(Object.class,"equals");
       assertNotNull(m);
       Method m1 = ReflectionHelper.getMethodByName(Object.class," EqualS"); // check still works for poorly entered data.
       assertNotNull(m1);
       assertEquals(m,m1);
              
     
    }
    
    public void testGetMethodByNameFails() throws NoSuchMethodException {
        try {
        Method m =  ReflectionHelper.getMethodByName(Object.class,"equals1");
        fail("expected to throw");
        } catch (NoSuchMethodException e) {
            // expeced.
        }
    }
    
   
    public void testCallStatic() throws Exception {
        assertEquals(str,ReflectionHelper.callStatic(this.getClass(),"method1"));
    }

    public void testCallStaticParam() throws Exception {
        Object o = new Object();
        assertSame(o,ReflectionHelper.callStatic(this.getClass(),"method2",o));
    }
    public void testCall() throws Exception {
        Object o = new Object();
        assertEquals(o,ReflectionHelper.call(this,"meth3",o));
    }

    public void testCallMoreParam() throws Exception {
        Object o = new Object();
        Object o1 = new Object();
        assertSame(o1,ReflectionHelper.call(this,"meth4",o,o1,o));
    }
    
    public void testOverloadedFails() throws Exception {
        try {
        Object o = new Object();
        assertEquals(o,ReflectionHelper.call(this,"overloadedMethod",o));               
        Object o1 = new Object();
        assertSame(o1,ReflectionHelper.call(this,"overloadedMethod4",o,o1,o));
        fail("one of these method calls should have failed");
    } catch (Exception e) {
        // ok
    }
    }
    
    public final static String str = "str";
    
    public static final String method1() {
        return str;
    }
    public static final Object method2(Object o) {
        return o;
    }

    public  Object overloadedMethod(Object o) {
        return o;
    }

    public  Object overloadedMethod(Object o,Object o1,Object ignored) {
        return o1;
    }
    
    public  Object meth3(Object o) {
        return o;
    }

    public  Object meth4(Object o,Object o1,Object ignored) {
        return o1;
    }
}


/* 
$Log: ReflectionHelperUnitTest.java,v $
Revision 1.3  2008/04/23 11:22:37  nw
added more tests

Revision 1.2  2007/01/29 10:38:40  nw
documentation fixes.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/