/*$Id: RuleStore.java,v 1.6 2005/04/25 12:13:54 clq2 Exp $
 * Created on 05-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.Script;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** Container for rules.
 * was previously storing rules in a hashmap. Need to add some extra methods on the side of this - but existing code expects to see a hashmap - hence
 * this class extends rather than contains a hashmap.
 * 
 * @modified optimized selection of next valid rule, by pre-compiling all triggers into a large script that evaluates to the key of the rule to fire next.
 * so we get to replace a linear traverse and evaluate each with a computation, followed by indexing into a map.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2004
 *
 */
public class RuleStore extends HashMap {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RuleStore.class);

    /** computed script that calculates id of next runnable rule */
    protected String indexScript;
    /**
     * cached compiled up version of this 
     */
    protected transient SoftReference cachedIndexScript = null;
        
    /** Construct a new RuleStore
     * 
     */
    public RuleStore() {
        super();
    }

    /** Construct a new RuleStore
     * @param m
     */
    public RuleStore(Map m) {
        super(m);
    }

    /** add a rule to the rulestore */
    public void add( Rule r) {
        invalidateCache();
        super.put(r.getName(),r);
    }


    
    /** called whenever an update occurs, to signify cache has been invalidated.
     * 
     */
    private void invalidateCache() {
        // invalidates index expression, so remove it.
        indexScript = null;
        if (cachedIndexScript != null) {
            cachedIndexScript.clear();
        }
        cachedIndexScript = null;
    }

    public void clear() {
        invalidateCache();
        super.clear();
    }
    public Object put(Object key, Object value) {
        invalidateCache();
        return super.put(key, value);
    }
    public void putAll(Map m) {
        invalidateCache();
        super.putAll(m);
    }
    public Object remove(Object key) {
        invalidateCache();
        return super.remove(key);
    }
    /** find a  triggered rule that can be validly executed
     * @param stateMap
     * 
     * @return a triggered rule, or null if no rules are currently triggered.
     * @throws ScriptEngineException if evaluati;on of triggers fails in some way.
     */
    public Rule findNext(JesShell shell, ActivityStatusStore stateMap) throws ScriptEngineException {
        if (indexScript == null) {
            logger.info("computing index");
            computeIndexScript();
        }
        Script s = null;
        if (cachedIndexScript != null) {
            s = (Script)cachedIndexScript.get();
        }
        if (s == null) { // no cached script, either because it's been GC'd, or because a rule has been added, invalidating the last one.
            logger.info("compiling up index");
            try {
                s = shell.compileRuleScript(this.indexScript);
            } catch (CompilationFailedException e) {
                logger.error("failed to compile index",e);
                throw new ScriptEngineException("failed to compile index",e);
            } catch (IOException e) {
                logger.error("failed to compile index",e);                
                throw new ScriptEngineException("failed to compile index",e);
            }            
            cachedIndexScript = new SoftReference(s);
        }
        String id = shell.evaluateIndex(s,stateMap);  
        if (id == null) {
            return null;
        }
        Rule r = (Rule)get(id);
        if (r == null) {
            throw new RuleNotFoundException(id);
        }
        return r;
    }
    
    /** generates a function with an if-clause based on each rule's trigger. */
    protected void computeIndexScript() {
        StringBuffer script = new StringBuffer();
        script.append("def index() {");
        for (Iterator i = values().iterator(); i.hasNext();) {
            Rule r = (Rule)i.next();
            script.append("if (\n");
            script.append(r.getTrigger());
            script.append("\n){\nreturn '");
            script.append(r.getName());
            script.append("';\n}\n");
        }
        // otherwise
        script.append("return null;");
        script.append("}\nindex()");
        this.indexScript = script.toString();
    }
    

}


/* 
$Log: RuleStore.java,v $
Revision 1.6  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.5.44.1  2005/04/11 13:56:48  nw
no change

Revision 1.5  2004/11/29 20:00:24  clq2
jes-nww-714

Revision 1.4.12.1  2004/11/24 18:49:02  nw
sandboxing of script execution - first by a timeout,
later by java permissions system.

Revision 1.4  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.3.2.1  2004/11/05 16:11:26  nw
subclassed map to create rulestore.
added methods to compute index of triggers,
optimization: compiled index is cached in soft reference
location of next rule uses index
 
*/