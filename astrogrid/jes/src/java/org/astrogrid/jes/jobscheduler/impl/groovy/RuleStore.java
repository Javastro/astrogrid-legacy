/*$Id: RuleStore.java,v 1.2 2004/07/30 15:42:34 nw Exp $
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**Maintains a set of rules.
 * @todo find more efficient data structure
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
 *
 */
public class RuleStore {

    /** Construct a new RuleStore
     * 
     */
    public RuleStore() {
        super();
    }
    
    protected List rules = new ArrayList();
    protected String name = "FRED";
    protected String rootState;
    /** add a rule to the store */
    public void addRule(Rule rule) {
        rules.add(rule);
    }
    
    
    public Rule findTriggeredRules(JesShell shell,ActivityStatusStore stats) throws CompilationFailedException, IOException {
        for (Iterator i = rules.iterator(); i.hasNext(); ) {
            Rule r = (Rule)i.next();
            if (r.isTriggered(shell,stats)) {
                return r; 
            }
        }
        return null;
    }
    /** find all rules referencing any states in statelist, create copies with references mangled
     * @todo check - only needed in parfor.
     * */
    public void addParallelBranch(int branchIndex,  List stateNames) {
        List newRules = new ArrayList();
        for (Iterator states = stateNames.iterator(); states.hasNext(); ) {
            String stateName = (String)states.next();
            for (Iterator rules = this.rules.iterator(); rules.hasNext(); ) {
                Rule rule = (Rule)rules.next();
                if (rule.references(stateName)) {
                    newRules.add(rule.rewriteAs(stateName,stateName+":" + branchIndex));
                }
            }
        }
    }
    /** Construct a parfor end rule from the info supplied, add it to the store 
     * @todo implement - only needed in parfor.*/
    public void addEndRule(String key, String function, int count) {
        List triggerList = null; //map(lambda x: "_getStatus('" + branchNameFn(x) + "')==FINISHED", range(count))
        String trigger = null; // reduce(lambda a, b: a + " and " + b, triggerList
        List envList = null; //map (branchNameFn,range(count))
        String body = "setStatus('" + key + "', FINISHED);"
            + "setEnv('" + key + "', mergeEnvs(" + envList +"))"; // need to convert envList to String
        Rule endRule = new Rule();
        endRule.setBody(body);
        endRule.setTrigger(trigger);
        /*
                triggerList = map(lambda x: "_getStatus('" + branchNameFn(x) + "')==FINISHED", range(count))
                body1 = "_setStatus('" + key + "',FINISHED)"
                envList = map (branchNameFn,range(count))
                body2 = "_setEnv('" + key + "', _mergeEnvs(" + str(envList) +"))"
                endRule = Rule(reduce(lambda a, b: a + " and " + b, triggerList),[body1,body2])
                self.add(endRule)
         * 
         */
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[RuleStore:");
        buffer.append(" rules: ");
        buffer.append(rules);
        buffer.append("]");
        return buffer.toString();
    }
    /**
     * Returns <code>true</code> if this <code>RuleStore</code> is the same as the o argument.
     *
     * @return <code>true</code> if this <code>RuleStore</code> is the same as the o argument.
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
        RuleStore castedObj = (RuleStore) o;
        return ((this.rules == null ? castedObj.rules == null : this.rules
            .equals(castedObj.rules)));
    }
    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (rules == null ? 0 : rules.hashCode());
        return hashCode;
    }
}


/* 
$Log: RuleStore.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.5  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.4  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.3  2004/07/27 23:50:09  nw
removed betwixt (duff). replaces with xstream.

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