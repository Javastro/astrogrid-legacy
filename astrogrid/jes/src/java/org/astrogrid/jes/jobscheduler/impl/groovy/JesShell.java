/*$Id: JesShell.java,v 1.15 2004/11/05 16:52:42 jdt Exp $
 * Created on 29-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.community.User;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.scripting.Astrogrid;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.While;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.SoftReference;
import java.util.Map;

/** class that encapuslates the execution and evaluation of grouvy code.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2004
 *
 */
public class JesShell {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JesShell.class);


    /** Construct a new JesShell
     * 
     */
    public JesShell() {
        super();
    }

    /** for efficiencies sake, only ever create a single, static interpreter.
     * this will be ok - as we parse-run all code we pass through it.
     * plus, we know it is being called in a single thread (the scheduler) only.
     * however, don't know for sure that this isn't memory leaking. so will be more cautious, and create a new one for each new shell
     * (i.e. for each workflow processed). but this seems really slow. will implement a kind of pool instead - reuse each object 500 times, then get shot of it.
     * @modified - made this an soft reference too - so can get reclaimed if things get tight. Done the same with the cached scripts - incase they hold references back to the shell that created them
     */
    private static SoftReference shell = new SoftReference(new GroovyShell());
    private static int useCount = 0;
    private static final int USE_LIMIT = 500; // as shell gets accessed a lot.
    protected static GroovyShell getGroovyShell() {
        // logic a little tricky here.
        GroovyShell s = (GroovyShell)shell.get();
        if (useCount++ > USE_LIMIT || s == null) {
            logger.info("Recreating groovy shell");
            useCount = 0;
            shell.clear();
            s = new GroovyShell();
            shell = new SoftReference(s);
        }
        return s;
    }
    
    protected JesInterface jes;
    

    
    public String evaluateIndex(Script indexScript,ActivityStatusStore statusStore) {
        Binding triggerBinding = createTriggerBinding(statusStore);
        indexScript.setBinding(triggerBinding);
        return (String)indexScript.run();
    }
    
    public Script compile (String script) throws CompilationFailedException, IOException {
        return getGroovyShell().parse(script);
    }
    
    /** execute the body (concequence) of a rule */
    public void executeBody(Rule r,ActivityStatusStore statusStore, Map rules) throws CompilationFailedException, IOException {
        logger.info("firing " + r.getName());
        Binding bodyBinding = createBodyBinding(statusStore,rules);
        logger.debug(r);    
        Script sc = r.getCompiledBody();
        if (sc == null) { // not there, compile it up..
            sc = getGroovyShell().parse(r.getBody());
            r.setCompiledBody(sc);
        }
        sc.setBinding(bodyBinding);
        sc.run();
    }
    /** execute a script activity */
    public void executeScript(String script,String id, ActivityStatusStore statusStore,Map rules, PrintStream errStream, PrintStream outStream) throws CompilationFailedException, IOException {
        logger.info("Running script for id " + id);
        logger.debug(script);
        Binding scriptBinding = createScriptBinding(statusStore,rules);
        Vars vars= statusStore.getEnv(id);
        vars.addToBinding(scriptBinding);
        Script sc = getGroovyShell().parse(script);
        sc.setBinding(scriptBinding);
        PrintStream originalErr = System.err;
        PrintStream originalOut = System.out;
        try {
            System.setErr(errStream);
            System.setOut(outStream);
            sc.run();
        } finally {
            System.setErr(originalErr);
            System.setOut(originalOut);
        }
        logger.debug("Completed Script execution");
        vars.readFromBinding(scriptBinding);
    }
    
    /** used to evaluate user-supplied expressions - if tests, etc 
     * @throws IOException
     * @throws CompilationFailedException*/
public Object evaluateUserExpr(String expr,String id,ActivityStatusStore statusStore, Map rules) throws CompilationFailedException, IOException {
    logger.debug("exaluating " + expr);
    Binding scriptBinding = createScriptBinding(statusStore,rules);
    Vars vars = statusStore.getEnv(id);
    vars.addToBinding(scriptBinding);
    // wrap it in a here-document
    // want to return a string if it has more thatn just a single embedded ${..}, or is just a simple string.
    // otherwise want to return the object.
    StringBuffer gExpr = (new StringBuffer("x = <<<GSTRING\n")) 
            .append(expr)
            .append("\nGSTRING\n ")
            .append("(x instanceof GString && x.getValueCount() == 1 && x.getStrings().find{it.size() > 0} == null) ? x.getValue(0) : x"); 
   
    Script sc = getGroovyShell().parse(gExpr.toString());
    sc.setBinding(scriptBinding);
     Object result = sc.run();
     if (logger.isDebugEnabled()) {
         logger.debug("result: '" + result + "' type: " + result.getClass().getName());
     }
     return result;
    
}
    /** executes a 'set' activity */
    public void executeSet(Set set,String state,ActivityStatusStore statusStore, Map rules) throws CompilationFailedException, IOException {
        Vars vars = statusStore.getEnv(state);
        if (set.getValue() != null) {
            Object result = evaluateUserExpr(set.getValue(),state,statusStore,rules);
            vars.set(set.getVar(),result);
        } else {
            // just a declaration, with no initialization.
            vars.set(set.getVar(),null);
        }
    }
    
    // necessary to have these, rather than pass the string directly into evaluateUserExpr - 
   // otherwise the gstirng gets substituted into before it reaches us.
    /** evaluates the test of  an 'if' activity */
    public Object evaluateIfCondition(If ifObj,ActivityStatusStore statusStore,Map rules) throws CompilationFailedException, IOException {
        return evaluateUserExpr(ifObj.getTest(),ifObj.getId(),statusStore,rules);
        
    }
    /** evaluates the test of a while activity */
    public Object evaluateWhileCondition(While whileObj,ActivityStatusStore statusStore,Map rules) throws CompilationFailedException, IOException {
        return evaluateUserExpr(whileObj.getTest(), whileObj.getId(),statusStore,rules);
    }
    /** evaluates the items of a for activity */
    public Object evaluateForItems(For forObj,ActivityStatusStore statusStore,Map rules) throws CompilationFailedException, IOException {
        return  evaluateUserExpr(forObj.getItems(),forObj.getId(),statusStore,rules);
   
    }
    /** evaluate the items of a parfor activity */
    public Object evaluateParforItems(Parfor forObj,ActivityStatusStore statusStore,Map rules) throws CompilationFailedException, IOException {
        return  evaluateUserExpr(forObj.getItems(),forObj.getId(),statusStore,rules);
   
    }    
    /** evaluate the parameter values of a tool */
    public Tool evaluateTool(Tool original,String id,ActivityStatusStore statusStore, Map rules) throws CompilationFailedException, IOException {
        Tool copy = new Tool();
        copy.setInterface(original.getInterface());
        copy.setName(original.getName());
        Input input = new Input();
        copy.setInput(input);
        
        Vars vars = statusStore.getEnv(id);
        Binding scriptBinding = createScriptBinding(statusStore,rules);
        vars.addToBinding(scriptBinding);
        if (original.getInput() != null) { // possible we have no inputs, I suppose
        for (int i = 0; i < original.getInput().getParameterCount(); i++) {
            ParameterValue originalP = original.getInput().getParameter(i);
            ParameterValue copyP = processParameter(originalP, scriptBinding);
            input.addParameter(copyP);
        }
        }
        
        if (original.getOutput() != null) {
        // do identical for ouputs.
        Output output = new Output();
        copy.setOutput(output);
        for (int i =0; i < original.getOutput().getParameterCount(); i++) {
            ParameterValue originalP = original.getOutput().getParameter(i);
            ParameterValue copyP = processParameter(originalP,scriptBinding);
            output.addParameter(copyP);
        }
        }
        return copy;
    }

/** 
     * @param original
     * @param scriptBinding
     * @param i
     * @return
     * @throws CompilationFailedException
     * @throws IOException
     */
    private ParameterValue processParameter(ParameterValue originalP, Binding scriptBinding) throws CompilationFailedException, IOException {
        ParameterValue copyP = new ParameterValue();
        copyP.setName(originalP.getName());
        copyP.setIndirect(originalP.getIndirect());
        copyP.setEncoding(originalP.getEncoding());
        // evaluate value.. -- always to string.
        StringBuffer expr = (new StringBuffer("<<<GSTRING\n")).append(originalP.getValue()).append("\nGSTRING\n");
        Script sc = getGroovyShell().parse(expr.toString());
        sc.setBinding(scriptBinding);
        Object result = sc.run();
        copyP.setValue( result.toString());
        return copyP;
    }

    //  binding creation functions - so that scripts can access the status store.
    private Binding createTriggerBinding(ActivityStatusStore statusStore) {
        Binding b = new Binding();
        b.setVariable("states",statusStore);
        for (int i = 0;  i < Status.allStatus.size() ; i++ ) {
            Status stat = (Status)Status.allStatus.get(i);
            b.setVariable(stat.getName(),stat);
        }
        return b;
    }

    private  Binding createBodyBinding(ActivityStatusStore statusStore,Map rules){
        Binding b = createTriggerBinding(statusStore);
        b.setVariable("rules",rules);
        b.setVariable("jes",jes);
        b.setVariable("shell",this);
        return b;
    }
    
    /** create environment for user scripts to run in - don't provide access to system objects.
     * @param state
     * @param statusStore
     * @return
     * @todo we're using the static singleton provided by astrogrid object here. may be issues with scripts interfering with each other - i.e. corrupting service list, etc.
     * however, don't want to rebuild astrogird object each time. future solution - remove setters, etc - make immutable.
     */
    private Binding createScriptBinding(ActivityStatusStore statusStore, Map rules) {
        Binding b = new Binding();
        b.setVariable("jes",jes);
        Astrogrid ag = Astrogrid.getInstance();
        b.setVariable("astrogrid",ag);        

        Credentials creds = jes.getWorkflow().getCredentials();        
        b.setVariable("currentAccount",creds.getAccount());
        User u = new User(creds.getAccount().getName(),creds.getAccount().getCommunity(),creds.getGroup().getName(),creds.getSecurityToken());
        b.setVariable("currentUser",u);
        for (int i = 0;  i < Status.allStatus.size() ; i++ ) {
            Status stat = (Status)Status.allStatus.get(i);
            b.setVariable(stat.getName(),stat);
        }
        // ah, what the hell, who knows - it might be useful.
        b.setVariable("__states",statusStore);
        b.setVariable("__rules",rules);
        b.setVariable("__shell",this);
        
        
        return b;
    }    
    
    /**
     * @param jesInterface
     */
    public void setJesInterface(JesInterface jesInterface) {
        this.jes = jesInterface;
    }


    
    
}


/* 
$Log: JesShell.java,v $
Revision 1.15  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.14.18.1  2004/11/05 16:06:57  nw
optimized: cached GroovyShell in softreference
optimized: replaced + with stringBuffers
removed unused execute-trigger methods
added methods to compile / evaluate index code

Revision 1.14  2004/09/16 21:43:47  nw
made 3rd-party objects only persist for so many calls. - in case they're space leaking.

Revision 1.13  2004/09/06 16:47:04  nw
javadoc

Revision 1.12  2004/09/06 16:30:25  nw
javadoc

Revision 1.11  2004/08/18 21:50:15  nw
improved error propagation and reporting.
messages are now logged to workflow document

Revision 1.10  2004/08/13 09:10:30  nw
tidied imports

Revision 1.9  2004/08/09 17:34:10  nw
implemented parfor.
removed references to rulestore

Revision 1.8  2004/08/05 14:38:15  nw
implemented sequential for construct.

Revision 1.7  2004/08/05 10:56:23  nw
implemented while loop construct

Revision 1.6  2004/08/05 09:59:58  nw
implemented if statement

Revision 1.5  2004/08/05 07:36:14  nw
made shell static for efficiency.

Revision 1.4  2004/08/03 16:32:26  nw
remove unnecessary envId attrib from rules
implemented variable propagation into parameter values.

Revision 1.3  2004/08/03 14:27:38  nw
added set/unset/scope features.

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.2  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.1  2004/07/30 14:00:10  nw
first working draft
 
*/