/*$Id: VarsTest.java,v 1.4 2004/08/09 17:34:10 nw Exp $
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


        public void testUnset() {
            assertNotNull(vars.get("foo"));
            vars.unset("foo");
            assertNull(vars.get("foo"));
        }
    
    public void testBranchVars() {
       Vars vars1 = vars.branchVars();
       assertNotSame(vars,vars1);
       assertEquals(vars,vars1);
       // check aliasing is happening.
       vars1.set("foo","barney");
       assertEquals(vars,vars1);
       vars1.newScope();
       assertTrue(! vars.equals(vars1));
    }
    
    public void testScopes() {
        vars.newScope();
        vars.set("wibble",new Integer(1));
        assertNotNull(vars.get("wibble"));
        vars.removeScope();
        assertNull(vars.get("wibble"));
    }
    
    public void testScopeShadowing() {
        vars.set("x",new Integer(1));
        vars.newScope();
        assertNotNull(vars.get("x"));
        assertEquals(new Integer(1),vars.get("x"));
        vars.set("x",new Integer(2));
        // design choice here - does the assignment shadow, or update original
        // think least astonishing is to update original.
        vars.removeScope();
        assertEquals(new Integer(2),vars.get("x"));
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
Revision 1.4  2004/08/09 17:34:10  nw
implemented parfor.
removed references to rulestore

Revision 1.3  2004/08/03 14:27:38  nw
added set/unset/scope features.

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