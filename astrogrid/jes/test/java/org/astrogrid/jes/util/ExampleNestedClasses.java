/*$Id: ExampleNestedClasses.java,v 1.2 2004/11/05 16:52:42 jdt Exp $
 * Created on 03-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.util;

/** used to test some assumptions about behaviour of nested classes.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Nov-2004
 *
 */
public class ExampleNestedClasses {

    /** Construct a new ExampleNestedClasses
     * 
     */
    public ExampleNestedClasses() {
        super();
    }
    
    protected Integer i = new Integer(1); // am using objects, rather than primitives, in case there is some different of impl
    protected final  A a = new A();
    protected final B b = new B();
    
    public Integer getI() {
        return i;
    }
    
    protected void _setI(Integer x) {
        i = x;
    }
    
    protected class A {
        public Integer getI() {
            return i;
        }
    }
    
    protected class B {
        public Integer getI() {
            return i;
        }
        public void setI(Integer x) {
            _setI(x);            
        }
    }
    
}


/* 
$Log: ExampleNestedClasses.java,v $
Revision 1.2  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.1.2.1  2004/11/05 15:46:41  nw
tests for static buffer, and resources
 
*/