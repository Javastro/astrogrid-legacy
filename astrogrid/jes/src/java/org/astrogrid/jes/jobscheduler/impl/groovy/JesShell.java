/*$Id: JesShell.java,v 1.10 2004/08/13 09:10:30 nw Exp $
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

import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

/** class that encapuslates the execution and evaluation of grouvy code.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2004
 * @todo add in ag scripting object here..
 *
 */
public class JesShell {
    /**
     * Commons Logger for this class
     */
    private static final Logger logger = Logger.getLogger(JesShell.class);

    /** Construct a new JesShell
     * 
     */
    public JesShell() {
        super();
    }

    /** for efficiencies sake, only ever create a single, static interpreter.
     * this will be ok - as we parse-run all code we pass through it.
     * plus, we know it is being called in a single thread (the scheduler) only.
     */
    protected static final GroovyShell shell = new GroovyShell();
    protected JesInterface jes;
    
    public boolean evaluateTrigger(Rule r,ActivityStatusStore map) throws CompilationFailedException, IOException {
        Binding triggerBinding = createTriggerBinding(map);
        Script sc = r.getCompiledTrigger();
        if (sc == null) { // not there, compile it up..        
            sc =  shell.parse(r.getTrigger());
            r.setCompiledTrigger(sc);
        }
        sc.setBinding(triggerBinding);
        Boolean result = (Boolean)sc.run();
        return result.booleanValue();
    }
    
    public void executeBody(Rule r,ActivityStatusStore map, List store) throws CompilationFailedException, IOException {
        logger.info("firing " + r.getName());
        Binding bodyBinding = createBodyBinding(map,store);
        logger.debug(r);    
        Script sc = shell.parse(r.getBody());       
        sc.setBinding(bodyBinding);
        sc.run();

    }
    
    public void executeScript(String script,String id, ActivityStatusStore map,List rules, PrintStream errStream, PrintStream outStream) throws CompilationFailedException, IOException {
        logger.info("Running script for id " + id);
        logger.debug(script);
        Binding scriptBinding = createScriptBinding(map,rules);
        Vars vars= map.getEnv(id);
        vars.addToBinding(scriptBinding);

        Script sc = shell.parse(script);
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
    
    /** used to evaluate uer-supplied expressions - if tests, etc 
     * @throws IOException
     * @throws CompilationFailedException*/
public Object evaluateUserExpr(String expr,String id,ActivityStatusStore map, List rules) throws CompilationFailedException, IOException {
    logger.debug("exaluating " + expr);
    Binding scriptBinding = createScriptBinding(map,rules);
    Vars vars = map.getEnv(id);
    vars.addToBinding(scriptBinding);
    // wrap it in a here-document
    // want to return a string if it has more thatn just a single embedded ${..}, or is just a simple string.
    // otherwise want to return the object.
    String gExpr = "x = <<<GSTRING\n" + expr+ "\n" + "GSTRING\n " +
        "(x instanceof GString && x.getValueCount() == 1 && x.getStrings().find{it.size() > 0} == null) ? x.getValue(0) : x"; 
   
    Script sc = shell.parse(gExpr);
    sc.setBinding(scriptBinding);
     Object result = sc.run();
     logger.debug("result: '" + result + "' type: " + result.getClass().getName());
     return result;
    
}

    public boolean executeSet(Set set,String state,ActivityStatusStore map, List rules) throws CompilationFailedException, IOException {
        Vars vars = map.getEnv(state);
        if (set.getValue() != null) {
            Object result = evaluateUserExpr(set.getValue(),state,map,rules);
            vars.set(set.getVar(),result);
            return true;
        } else {
            // just a declaration, with no initialization.
            vars.set(set.getVar(),null);
            return true;
        }
    }
    
    // necessary to have these, rather than pass the string directly into evaluateUserExpr - 
   // otherwise the gstirng gets substituted into before it reaches us.
    public Object evaluateIfCondition(If ifObj,ActivityStatusStore map,List rules) throws CompilationFailedException, IOException {
        return evaluateUserExpr(ifObj.getTest(),ifObj.getId(),map,rules);
        
    }
    
    public Object evaluateWhileCondition(While whileObj,ActivityStatusStore map,List rules) throws CompilationFailedException, IOException {
        return evaluateUserExpr(whileObj.getTest(), whileObj.getId(),map,rules);
    }
    
    public Object evaluateForItems(For forObj,ActivityStatusStore map,List rules) throws CompilationFailedException, IOException {
        return  evaluateUserExpr(forObj.getItems(),forObj.getId(),map,rules);
   
    }
    public Object evaluateParforItems(Parfor forObj,ActivityStatusStore map,List rules) throws CompilationFailedException, IOException {
        return  evaluateUserExpr(forObj.getItems(),forObj.getId(),map,rules);
   
    }    
    
    public Tool evaluateTool(Tool original,String id,ActivityStatusStore map, List rules) throws CompilationFailedException, IOException {
        Tool copy = new Tool();
        copy.setInterface(original.getInterface());
        copy.setName(original.getName());
        Input input = new Input();
        copy.setInput(input);
        
        Vars vars = map.getEnv(id);
        Binding scriptBinding = createScriptBinding(map,rules);
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
        String expr = "<<<GSTRING\n" + originalP.getValue()  + "\nGSTRING\n";
        Script sc = shell.parse(expr);
        sc.setBinding(scriptBinding);
        Object result = sc.run();
        copyP.setValue( result.toString());
        return copyP;
    }

    //  binding creation functions - so that scripts can access the status store.
    private Binding createTriggerBinding(ActivityStatusStore map) {
        Binding b = new Binding();
        b.setVariable("states",map);
        for (Iterator i = Status.allStatus.iterator(); i.hasNext(); ) {
            Status stat = (Status)i.next();
            b.setVariable(stat.getName(),stat);
        }
        return b;
    }

    private  Binding createBodyBinding(ActivityStatusStore map,List rules){
        Binding b = createTriggerBinding(map);
        b.setVariable("rules",rules);
        b.setVariable("jes",jes);
        b.setVariable("log",jes.getLog());
        b.setVariable("shell",this);
        return b;
    }
    
    /** create environment for user scripts to run in - don't provide access to system objects.
     * @param state
     * @param map
     * @return
     * @todo we're using the static singleton provided by astrogrid object here. may be issues with scripts interfering with each other - i.e. corrupting service list, etc.
     * however, don't want to rebuild astrogird object each time. future solution - remove setters, etc - make immutable.
     */
    private Binding createScriptBinding(ActivityStatusStore map, List rules) {
        Binding b = new Binding();
        b.setVariable("log",jes.getLog());
        b.setVariable("jes",jes);
        Astrogrid ag = Astrogrid.getInstance();
        b.setVariable("astrogrid",ag);        

        Credentials creds = jes.getWorkflow().getCredentials();        
        b.setVariable("currentAccount",creds.getAccount());
        User u = new User(creds.getAccount().getName(),creds.getAccount().getCommunity(),creds.getGroup().getName(),creds.getSecurityToken());
        b.setVariable("currentUser",u);
        for (Iterator i = Status.allStatus.iterator(); i.hasNext(); ) {
            Status stat = (Status)i.next();
            b.setVariable(stat.getName(),stat);
        }
        // ah, what the hell, who knows - it might be useful.
        b.setVariable("__states",map);
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