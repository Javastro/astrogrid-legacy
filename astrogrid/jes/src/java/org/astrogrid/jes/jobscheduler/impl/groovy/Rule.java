/*$Id: Rule.java,v 1.8 2004/11/05 16:52:42 jdt Exp $
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
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.regex.Pattern;

/**   represents a rule that is fired - has a trigger, name, and body of actions to execute
 Written as a bean, for easy serialization.
 <p>
 also caches compiled version of  body after first use - for faster execution the next time round.
 @modified - changed these cached scripts to be soft references - don't want the code blowing up unnecessarily.
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

    /** cache variable, not persisted */
    protected transient SoftReference compiledBody;

    /** execute the body of a rule 
     * @throws IOException
     * @throws CompilationFailedExceptiont*/
    public void fire(JesShell shell,ActivityStatusStore statusStore, Map rules) throws CompilationFailedException, IOException {
        shell.executeBody(this,statusStore,rules);
    }
    
    /** return true if any trigger, env or body action references this key */
    public boolean matches(Pattern p) {
        
        return p.matcher(this.trigger).find() || p.matcher(this.body).find();
    }
       
    /** create a copy of this rule, rewritten so that all references to old key reference new key*/
    public Rule rewriteAs(Pattern regexp, int branchId) {
        Rule result = new Rule();
        result.setName(this.name + "-" + branchId);
        String replacement = "'$1-" + branchId + "'";
        result.setTrigger(regexp.matcher(this.trigger).replaceAll(replacement));
        result.setBody(regexp.matcher(this.body).replaceAll(replacement));
        logger.debug("generated new rule " + result.toString());
        return result;
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
    }
    
    
    public void setCompiledBody(Script script) {
        this.compiledBody = new SoftReference(script);
    }
    
    public Script getCompiledBody() {
        if (compiledBody == null) {
            return null;
        }
        return (Script)this.compiledBody.get();
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
                .equals(castedObj.body))) ;
    }
    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (logger == null ? 0 : logger.hashCode());
        hashCode = 31 * hashCode + (trigger == null ? 0 : trigger.hashCode());
        hashCode = 31 * hashCode + (name == null ? 0 : name.hashCode());
        hashCode = 31 * hashCode + (body == null ? 0 : body.hashCode());
  
        return hashCode;
    }
}


/* 
$Log: Rule.java,v $
Revision 1.8  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.7.26.1  2004/11/05 16:04:56  nw
removed cached trigger
cached body in soft reference.
updated rewrite() to work with new rulestore (map-based)

Revision 1.7  2004/09/06 16:47:04  nw
javadoc

Revision 1.6  2004/08/18 21:50:15  nw
improved error propagation and reporting.
messages are now logged to workflow document

Revision 1.5  2004/08/13 09:10:30  nw
tidied imports

Revision 1.4  2004/08/09 17:32:53  nw
optimized string handling - all done via regexps now.

Revision 1.3  2004/08/03 16:32:26  nw
remove unnecessary envId attrib from rules
implemented variable propagation into parameter values.

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