/*$Id: Rule.java,v 1.2 2004/07/30 15:42:34 nw Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.Script;

import java.io.IOException;

/**   represents a rule that is fired - has a trigger, location of environment, and list of actions to execute
 Written as a bean, for easy serialization.
 @todo make strinig manipulation more efficient.
 @todo cache compiled triggers for reuse - as are called often.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
 *
 */
public class Rule {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Rule.class);

    /** Construct a new Rule
     * 
     */
    public Rule() {
        super();
    }
    protected String trigger;
    protected String name;
    protected String body;
    protected String envId;
    protected transient Script compiledTrigger;
    /** returns true if trigger of rule succeeds. 
     * @throws IOException
     * @throws CompilationFailedException*/
    public boolean isTriggered(JesShell shell,ActivityStatusStore map) throws CompilationFailedException, IOException {
            return shell.evaluateTrigger(this,map);
    }
    
    /** execute the body of a rule 
     * @throws IOException
     * @throws CompilationFailedExceptiont*/
    public void fire(JesShell shell,ActivityStatusStore map, RuleStore store) throws CompilationFailedException, IOException {
        shell.executeBody(this,map,store);
    }
    
    /** return true if any trigger, env or body action references this key */
    public boolean references(String key) {
        if (this.envId.equals(key)) {
            return true;
        }
        return referencesTest(this.trigger,key) || this.referencesTest(this.body,key);
    }
    
    private boolean referencesTest(String codeSnippet,String key) {
        return codeSnippet.indexOf("'" + key + "'") != -1 
            || codeSnippet.indexOf('"' + key + '"') != -1 ;
    }
    
    /** create a copy of this rule, rewritten so that all references to old key reference new key*/
    public Rule rewriteAs(String oldKey, String newKey) {
        String newEnvId = this.envId == oldKey ? newKey : oldKey;
        Rule result = new Rule();
        result.setName(this.name = " branch " + newKey);
        result.setTrigger(replace(this.trigger,oldKey,newKey));
        result.setBody(replace(this.body,oldKey,newKey));
        result.setEnvId(newEnvId);
        return result;
    }

    private String replace(String code,String oldKey,String newKey) {
        return code
            .replaceAll("'" + oldKey + "'","'" + newKey + "'")
            .replaceAll('"' + oldKey + '"','"' + newKey + '"');
    }
    
    
    // getters and setters
    /**
     * @return Returns the body.
     */
    public String getBody() {
        return this.body;
    }
    /**
     * @param body The body to set.
     */
    public void setBody(String body) {
        this.body = body.trim();
    }
    /**
     * @return Returns the envId.
     */
    public String getEnvId() {
        return this.envId;
    }
    /**
     * @param envId The envId to set.
     */
    public void setEnvId(String envId) {
        this.envId = envId;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name.trim();
    }
    /**
     * @return Returns the trigger.
     */
    public String getTrigger() {
        return this.trigger;
    }
    /**
     * @param trigger The trigger to set.
     */
    public void setTrigger(String trigger) {
        this.trigger = trigger.trim();
        this.compiledTrigger = null;
    }
    
    public void setCompiledTrigger(Script script) {
        this.compiledTrigger = script;
    }
    
    public Script getCompiledTrigger() {
        return this.compiledTrigger;
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Rule:");
        buffer.append(" trigger: ");
        buffer.append(trigger);
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append(" body: ");
        buffer.append(body);
        buffer.append(" envId: ");
        buffer.append(envId);
        buffer.append("]");
        return buffer.toString();
    }
    /**
     * Returns <code>true</code> if this <code>Rule</code> is the same as the o argument.
     *
     * @return <code>true</code> if this <code>Rule</code> is the same as the o argument.
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        Rule castedObj = (Rule) o;
        return ((this.trigger == null
            ? castedObj.trigger == null
            : this.trigger.equals(castedObj.trigger))
            && (this.name == null ? castedObj.name == null : this.name
                .equals(castedObj.name))
            && (this.body == null ? castedObj.body == null : this.body
                .equals(castedObj.body)) && (this.envId == null
            ? castedObj.envId == null
            : this.envId.equals(castedObj.envId)));
    }
    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (trigger == null ? 0 : trigger.hashCode());
        hashCode = 31 * hashCode + (name == null ? 0 : name.hashCode());
        hashCode = 31 * hashCode + (body == null ? 0 : body.hashCode());
        hashCode = 31 * hashCode + (envId == null ? 0 : envId.hashCode());
        return hashCode;
    }
}


/* 
$Log: Rule.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.4  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.3  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.2  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.

Revision 1.1.2.1  2004/07/26 15:51:19  nw
first stab at a groovy scheduler.
transcribed all the classes in the python prototype, and took copies of the
classes in the 'scripting' package.
 
*/