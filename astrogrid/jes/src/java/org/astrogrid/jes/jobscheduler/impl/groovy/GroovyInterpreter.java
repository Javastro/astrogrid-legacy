/*$Id: GroovyInterpreter.java,v 1.3 2004/08/04 16:51:46 nw Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/** top level class from python prototype.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
 *
 */
public class GroovyInterpreter  {

    /** Construct a new Interpreter
     * 
     */
    public GroovyInterpreter() {
        this(new RuleStore());
    }
    
    public GroovyInterpreter(RuleStore rs) {
        this.ruleStore = rs;    
        
        
    }
    private  static final Log logger = LogFactory.getLog(GroovyInterpreter.class);
    
    protected RuleStore ruleStore;
    protected ActivityStatusStore stateMap = new ActivityStatusStore();
    protected final transient JesShell shell = new JesShell();
    
    public void addRule(Rule r) {
        ruleStore.addRule(r);
    }
        
    /** find next candidate */
    public Rule findNext() throws ScriptEngineException {
        try {
            return ruleStore.findTriggeredRules(shell,stateMap);
        } catch (Exception e) {
            throw new ScriptEngineException("Failed to find next",e);
        }
    } 
    
    public void runNext() throws ScriptEngineException{
        try {
            Rule rule = findNext();
            logger.debug("Running " + rule.getName());
            rule.fire(shell,stateMap,ruleStore);
        } catch (Exception e) {
            throw new ScriptEngineException("Failed to run next",e);
        }
    }
    /** run the interpreter, until no more rules can be triggered */
    public void run() throws ScriptEngineException {
        try {
        for (Rule rule= findNext(); rule != null; rule = findNext()) {
            logger.debug("Running " + rule.getName());
            rule.fire(shell,stateMap,ruleStore);
        }
        }catch (Exception e) {
            throw new ScriptEngineException("Failed to run interpreter",e);
        }
    }
    
    public void updateStepStatus(Step step, ExecutionPhase phase)  {
        if (phase.equals(ExecutionPhase.COMPLETED)) {
            stateMap.setStatus(step.getId(),Status.FINISH);
        } else if (phase.equals(ExecutionPhase.ERROR)) {
            stateMap.setStatus(step.getId(),Status.ERROR);
        } else { // nothing we care about.
            return;
        }
        
    }
    /**
     * @param step
     * @param results
     */
    public void storeResults(Step step, ResultListType results) {
        String id = step.getId();
        // copy results into a more groovy-friendly datastructure (not to mention xstream)
        Map resultsMap = new HashMap();
        for (int i = 0; i < results.getResultCount(); i++) {
            ParameterValue pval = results.getResult(i);
            resultsMap.put(pval.getName(),pval.getValue());
        }
        
        stateMap.getEnv(step.getId()).set(step.getResultVar(),resultsMap);
        stateMap.setStatus(step.getId() + "-results",Status.FINISHED);
    }    
    
    /**
     * @param jesInterface The jesInterface to set.
     */
    public void setJesInterface(JesInterface jesInterface) {
        this.shell.setJesInterface(jesInterface);
    }
    /**
     * Returns <code>true</code> if this <code>GroovyInterpreter</code> is the same as the o argument.
     *
     * @return <code>true</code> if this <code>GroovyInterpreter</code> is the same as the o argument.
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
        GroovyInterpreter castedObj = (GroovyInterpreter) o;
        return ((this.ruleStore == null
            ? castedObj.ruleStore == null
            : this.ruleStore.equals(castedObj.ruleStore))
            && (this.stateMap == null
                ? castedObj.stateMap == null
                : this.stateMap.equals(castedObj.stateMap)) );
    }
    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (logger == null ? 0 : logger.hashCode());
        hashCode = 31
            * hashCode
            + (ruleStore == null ? 0 : ruleStore.hashCode());
        hashCode = 31 * hashCode + (stateMap == null ? 0 : stateMap.hashCode());
        return hashCode;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[GroovyInterpreter:");
        buffer.append(" ruleStore: ");
        buffer.append(ruleStore);
        buffer.append(" stateMap: ");
        buffer.append(stateMap);

        buffer.append("]");
        return buffer.toString();
    }


}


/* 
$Log: GroovyInterpreter.java,v $
Revision 1.3  2004/08/04 16:51:46  nw
added parameter propagation out of cea step call.

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