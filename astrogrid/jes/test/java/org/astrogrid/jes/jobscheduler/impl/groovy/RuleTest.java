/*$Id: RuleTest.java,v 1.5 2004/08/09 17:33:02 nw Exp $
 * Created on 27-Jul-2004
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2004
 *
 */
public class RuleTest extends TestCase {

    protected void setUp() {
        rule = new Rule();
        rule.setTrigger("states.getStatus('testEnv') == UNSTARTED");
        rule.setBody("states.setStatus('anotherEnv',STARTED);");
        rule.setName("test rule");
        shell = new JesShell();
        shell.setJesInterface(new MockJes());
        store = new ActivityStatusStore();
        rules = new ArrayList();
        rules.add(rule);
    }
    protected Rule rule;
    protected JesShell shell;
    protected ActivityStatusStore store;
    protected List rules;
    
    public void testIsTriggered() throws CompilationFailedException, IOException {
        assertNull(rule.getCompiledTrigger());
        assertTrue(rule.isTriggered(shell,store));
        // check caching of compiled script has happened
        assertNotNull(rule.getCompiledTrigger());
    }

    public void testFire() throws CompilationFailedException, IOException {
        rule.fire(shell,store,rules); 
        assertEquals(Status.STARTED,store.getStatus("anotherEnv"));
        
    }

    public void testMatches() {
        assertTrue(rule.matches(Pattern.compile("[\"']testEnv[\"']")));
        assertFalse(rule.matches(Pattern.compile("[\"']unknownEnv[\"']")));
        assertFalse(rule.matches(Pattern.compile("[\"']testEnv1[\"']")));
        assertFalse(rule.matches(Pattern.compile("[\"']testE[\"']"))); // try sub strings, etc.
    }

    public void testRewriteAs() {
        Rule r1 = rule.rewriteAs(Pattern.compile("[\"'](testEnv)[\"']"),"'$1-1'");
        assertTrue(r1.matches(Pattern.compile("[\"']testEnv-1[\"']")));
        assertFalse(r1.matches(Pattern.compile("[\"']testEnv[\"']")));
        
    }
    public void testStandardMethods() {
        rule.toString();
        assertEquals(rule,rule);
        assertEquals(rule.hashCode(),rule.hashCode());
    }    

}


/* 
$Log: RuleTest.java,v $
Revision 1.5  2004/08/09 17:33:02  nw
optimized string handling - all done via regexps now.

Revision 1.4  2004/08/03 16:32:26  nw
remove unnecessary envId attrib from rules
implemented variable propagation into parameter values.

Revision 1.3  2004/08/03 14:27:38  nw
added set/unset/scope features.

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.3  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.2  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.1  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.
 
*/