/*$Id: VarsTest.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 28-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2004
 *
 */
public class VarsTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        vars = new Vars();
        vars.set("foo","fred");
        vars.set("bar",new Integer(1));
    }
    
    protected Vars vars;

    public void testGet() {
        assertNotNull(vars.get("foo"));
        assertEquals("fred",vars.get("foo"));
        assertNull(vars.get("unknown"));
    }


    public void testCloneVars() {
       Vars vars1 = vars.cloneVars();
       assertNotSame(vars,vars1);
       assertEquals(vars,vars1);
    }


    public void testRoundTripToBinding() throws CompilationFailedException, IOException {
        Binding b = new Binding();
        vars.addToBinding(b);
        GroovyShell shell = new GroovyShell();
        Script sc = shell.parse("foo= 'barney'; bar = bar + 1;vars.set('pling','bop');foo");
        sc.setBinding(b);
        Object result =sc.run();
        assertNotNull(result);
        assertEquals(String.class,result.getClass());
        assertEquals("barney",result);
       vars.readFromBinding(b);
        // check values read back.
       assertEquals(3,vars.e.size());
       assertNotNull(vars.get("foo"));
       assertNotNull(vars.get("pling"));
       assertNotNull(vars.get("bar"));
       assertEquals("barney",vars.get("foo"));
       assertEquals("bop",vars.get("pling"));
       assertEquals(new Integer(2),vars.get("bar"));
    }
    

    
    public void testStandardMethods() {
        vars.toString();
        assertEquals(vars,vars);
        assertEquals(vars.hashCode(),vars.hashCode());
    }    

}


/* 
$Log: VarsTest.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.3  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.2  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.1  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation
 
*/