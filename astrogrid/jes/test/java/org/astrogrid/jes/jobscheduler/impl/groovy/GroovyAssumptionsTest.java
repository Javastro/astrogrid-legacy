/*$Id: GroovyAssumptionsTest.java,v 1.3 2004/11/29 20:00:24 clq2 Exp $
 * Created on 26-Jul-2004
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
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.messages.WarningMessage;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import groovy.lang.Script;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/** Unit test that verifies my assumptions about the behaviour of groovy - can be used to check that same behaviour occurs when upgrading
 * to new version of groovy lib, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
 *
 */
public class GroovyAssumptionsTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();      
        vars = new HashMap();
        vars.put("x",new Integer(12));
        vars.put("y",new Integer(14));
        vars.put("msg","hello world");
        binding = new Binding(vars);
        shell = new GroovyShell(binding);  
    }
    protected GroovyShell shell;    
    protected Binding binding;
    protected Map vars;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** most central to jes is the ability to be able to evaluate strings dynamically. 
     * @throws IOException
     * @throws CompilationFailedException*/
    public void testShellIntEvaluate() throws CompilationFailedException, IOException {
        Object result = shell.evaluate("1 + 1");
        assertNotNull(result);
        assertTrue(result instanceof Integer); // so true type is returned - not some kiind of wrapper object. nice.
        assertEquals(new Integer(2),result);
    }
    
    public void testShellObjectEvaluate() throws Exception {
        Object result = shell.evaluate("new java.util.Date()");
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }
    
    public void testShellReferenceBindings() throws Exception {
        Object result = shell.evaluate("x");
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(new Integer(12),result);
    }
    
    public void testShellReferenceBindingsInExpression() throws Exception {
        Object result = shell.evaluate("x + y");
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(new Integer(26),result);
    }
    /** @modified beta-7 - previouly returned null. now throws exception */
    public void testShellReferenceUnknownBinding() throws Exception {
        try {
        shell.evaluate("unknownVariable");
        fail("expected to barf");
        } catch (MissingPropertyException e) {
            //expected
        }
    }
    /** @modified beta-7 previously returned null, now throws an exception */
    public void testShellReferenceUnknownBindingInExpression() throws Exception {
        try {
        Object result = shell.evaluate("1 + unknownVariable");
        fail("expected to chuck");
        } catch (MissingPropertyException e) {
            //ecpected
        }
    }
    
    // ok that's the shell itself done. But I think that jes is going to be compiling fragments up into scriptlets, and then executing these scriptlets later against
    // differend binding sets. so we'd better test this too.
    
    public void testScriptIntEvaluate() throws Exception {
        Script sc = shell.parse("1 + 1");
        assertNotNull(sc);
        Object result = sc.run();
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(result, new Integer(2));
    }
    
    public void testScriptObjectEvaluate() throws Exception {
        Script sc = shell.parse("new java.util.Date()");
        assertNotNull(sc);
        Object result = sc.run();
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }
    
    public void testShellReferenceBinding() throws Exception {
        Script sc = shell.parse("x");
        assertNotNull(sc);
        Object result = sc.run();
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(new Integer(12),result);
    }
    /** test whether changes to bindings are available to compiled script. */
    public void testShellReferenceUpdatedBinding() throws Exception {
        Script sc = shell.parse("x");
        assertNotNull(sc);
        this.vars.put("x",new Integer(42));
        Object result = sc.run();
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(new Integer(42),result);
    }
    
    /** test behaviour when another binding set is provided */
    public void testShellReferenceOverriddenBinding() throws Exception {
        Script sc = shell.parse("x");
        assertNotNull(sc);
        Map m1 = new HashMap();
        m1.put("x",new Integer(66));
        sc.setBinding(new Binding(m1));
        Object result = sc.run();
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(new Integer(66),result);        
    }
    
    /** test behaviour when another binding set is provided, but var is not available there 
     * @modified - in beta-7, unbound properties throw an exception.*/
    public void testShellReferenceOverriddenUnknownBinding() throws Exception {
        Script sc = shell.parse("x");
        assertNotNull(sc);        
        Map m1 = new HashMap();
        sc.setBinding(new Binding(m1));
        try {
            assertNull(sc.run());
            fail("expected to barf");
        } catch (MissingPropertyException e) {
            // ok.
        }
    }
    
    // finally test statements that update the environment
    public void testShellUpdateBinding() throws Exception {
        Script sc = shell.parse("x = 42");
        assertNotNull(sc);
        Map m1 = new HashMap();
        m1.put("x",new Integer(32));
        sc.setBinding(new Binding(m1));
        Object result = sc.run();
        assertNotNull(result);
        assertEquals(new Integer(42),result); // result of assignment is the RHS.
        assertEquals(new Integer(42),m1.get("x"));
    }
    

}


/* 
$Log: GroovyAssumptionsTest.java,v $
Revision 1.3  2004/11/29 20:00:24  clq2
jes-nww-714

Revision 1.2.76.1  2004/11/26 01:31:18  nw
updated dependency on groovy to 1.0-beta7.
updated code and tests to fit.

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.2  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.1  2004/07/26 15:50:33  nw
load of tests to check behaviour of groovy interpreter
 
*/