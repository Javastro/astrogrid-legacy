/*$Id: JesShell.java,v 1.18 2005/03/15 16:18:19 clq2 Exp $
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
import org.astrogrid.scripting.Toolbox;
import org.astrogrid.store.Ivorn;
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
import org.codehaus.groovy.GroovyException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.messages.WarningMessage;

import EDU.oswego.cs.dl.util.concurrent.Callable;
import EDU.oswego.cs.dl.util.concurrent.TimedCallable;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.SoftReference;
import java.net.URISyntaxException;
import java.util.Map;

import javax.imageio.IIOException;

/** class that encapuslates the execution and evaluation of grouvy code.
 * <p>
 * Uses a <a href="http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/TimedCallable.html">Timed Callable</a>
 * to abort execution after a certain time.
 * <p>
 * Added in codebases - to differentiate between user and system script.
 * @todo find out how to add in security managers to this too.
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
    
    private static final long EVAL_LIMIT = 60 * 1000; // 1 minute
    private static final long EXEC_LIMIT = 10 * 60 * 1000; // 10 minutes

    /** for efficiencies sake, only ever create a single, static interpreter.
     * this will be ok - as we parse-run all code we pass through it.
     * plus, we know it is being called in a single thread (the scheduler) only.
     * however, don't know for sure that this isn't memory leaking. so will be more cautious, and create a new one for each new shell
     * (i.e. for each workflow processed). but this seems really slow. will implement a kind of pool instead - reuse each object 500 times, then get shot of it.
     * @modified - made this an soft reference too - so can get reclaimed if things get tight. Done the same with the cached scripts - incase they hold references back to the shell that created them
     */
    private static SoftReference shell = new SoftReference(new GroovyShell());
    private static int useCount = 0;
    private static final int USE_LIMIT = 5000; // as shell gets accessed a lot.
    private static final CompilerConfiguration config = new CompilerConfiguration();
    static {
        config.setVerbose(true);
        config.setWarningLevel(WarningMessage.PARANOIA);
    }
    protected static GroovyShell getGroovyShell() {
        // logic a little tricky here.
        GroovyShell s = (GroovyShell)shell.get();
        if (useCount++ > USE_LIMIT || s == null) {
            logger.info("Recreating groovy shell");
            useCount = 0;
            shell.clear();            
            s = new GroovyShell(config);           
            shell = new SoftReference(s);
        }
        return s;
    }
    
    //same pattern for the scripting toolbox - as don't know yet how expensive it is to create.
    private static SoftReference toolbox = new SoftReference(new Toolbox());
    protected static Toolbox getToolbox() {
        Toolbox t = (Toolbox)toolbox.get();
        if (t == null) {
            logger.info("Recreating toolbox");
            t = new Toolbox();
            toolbox = new SoftReference(t);
        }
        return t;
    }
    
    protected JesInterface jes;
    

    
    public String evaluateIndex(Script indexScript,ActivityStatusStore statusStore) {
        Binding triggerBinding = createTriggerBinding(statusStore);
        indexScript.setBinding(triggerBinding);
        return (String)indexScript.run();
    }
    /** 
     * compile a rule script into the rule codebase.
     * @modified - specifies a code source while compiling.
     * 
     * commented out for now - needs further tuning and investigation
     * */
    public Script compileRuleScript (String script) throws CompilationFailedException, IOException {
       /*
        GroovyCodeSource cs = new GroovyCodeSource( script,"RuleScript","/jes/rule");
        return getGroovyShell().parse(cs);
        */
        return getGroovyShell().parse(script);
    }
    
    /**compile a user script into the user codebase. 
     * @modified - specifies a code source while compiling.
     * commented out for now - needs further tuning and investigation
     * */
    public Script compileUserScript (String script) throws CompilationFailedException, IOException {
        /*
        GroovyCodeSource cs = new GroovyCodeSource( script,"UserScript","/jes/user");
        return getGroovyShell().parse(cs);
        */
        return getGroovyShell().parse(script);
    }
    
    /*
     * for this to be effective, it seems like I need to instal my own policy- which means
     * Policy.getPolicy(), wrapping it in a custom subclass, and then Policy.setPolicy()
     * 
     * 'course, this relies on the tomcat security policy allowing me to change the policy. gahh.
     * of course, where the policy is finely configured already, just let the admin edit the policy to give the 
     * jes script the correct permissions. Need to have this policy-zapper as a separate component, which is allowed to 
     * fail as needed.
     */
    
    /** execute the body (concequence) of a rule */
    public void executeBody(Rule r,ActivityStatusStore statusStore, Map rules) throws CompilationFailedException, IOException {
        logger.info("firing " + r.getName());
        Binding bodyBinding = createBodyBinding(statusStore,rules);
        logger.debug(r);    
        Script sc = r.getCompiledBody();        
        if (sc == null) { // not there, compile it up..
            sc = compileRuleScript( r.getBody());
            r.setCompiledBody(sc);
        }
        sc.setBinding(bodyBinding);
        sc.run();
    }
    /** execute a script activity 
     * @throws InterruptedException
     * @throws IOException
     * @throws InterruptedException
     * @throws GroovyException*/
    public void executeScript(String script,String id, ActivityStatusStore statusStore,Map rules, PrintStream errStream, PrintStream outStream) throws GroovyException, IOException, InterruptedException {
        logger.info("Running script for id " + id);
        logger.debug(script);
        Binding scriptBinding = createScriptBinding(statusStore,rules);
        Vars vars= statusStore.getEnv(id);
        vars.addToBinding(scriptBinding);
        Script sc = compileUserScript(script);
        sc.setBinding(scriptBinding);
        PrintStream originalErr = System.err;
        PrintStream originalOut = System.out;
        try {
            System.setErr(errStream);
            System.setOut(outStream);
            runWithTimeLimit(sc,EXEC_LIMIT);
        } finally {
            System.setErr(originalErr);
            System.setOut(originalOut);
        }
        logger.debug("Completed Script execution");
        vars.readFromBinding(scriptBinding);
    }
    
    /** used to evaluate user-supplied expressions - if tests, etc 
     * @param expr
     * @param id
     * @param statusStore
     * @param rules
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws GroovyException*/
public Object evaluateUserExpr(String expr,String id,ActivityStatusStore statusStore, Map rules) throws GroovyException, IOException, InterruptedException {
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
   
    Script sc = compileUserScript(gExpr.toString());
    sc.setBinding(scriptBinding);    
    Object result =runWithTimeLimit(sc,EVAL_LIMIT);

     if (logger.isDebugEnabled()) {
         logger.debug("result: '" + result + "' type: " + result.getClass().getName());
     }
     return result;
    
}
/** wrap a script in a timed callable - will throws an interrupted if script overruns the time 
 * 
 * @param sc script to execute
 * @param timeLimit limit in milliseconds to execute the script for,
 * @return the result
 * @throws GroovyException if therer's a problem compiling or executing the script
 * @throws IOException
 * @throws InterruptedException if the script execution times out.
 */
protected Object runWithTimeLimit(final Script sc,long timeLimit) throws GroovyException, IOException, InterruptedException {
    Callable c = new Callable() {

        public Object call() throws Exception {
            return sc.run();
        }
    };
    TimedCallable tc = new TimedCallable(c,timeLimit);
    try {
        return tc.call();
    } catch (InterruptedException e) {// propagate upwards
        throw e;
    } catch (GroovyException e) {// propagate
        throw e;
    } catch (IIOException e) { // propoagate
        throw e;
    } catch (Exception e) {// doubt this will occur, but need to handle anyhow.
        GroovyException x =new GroovyException(e.getMessage());
        x.initCause(e);
        throw x;
    }
}

    /** executes a 'set' activity 
     * @throws IOException
     * @throws InterruptedException
     * @throws GroovyException*/
    public void executeSet(Set set,String state,ActivityStatusStore statusStore, Map rules) throws GroovyException, IOException, InterruptedException {
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
    /** evaluates the test of  an 'if' activity 
     * @throws IOException
     * @throws InterruptedException
     * @throws GroovyException*/
    public Object evaluateIfCondition(If ifObj,ActivityStatusStore statusStore,Map rules) throws GroovyException, IOException, InterruptedException {
        return evaluateUserExpr(ifObj.getTest(),ifObj.getId(),statusStore,rules);
        
    }
    /** evaluates the test of a while activity 
     * @throws IOException
     * @throws InterruptedException
     * @throws GroovyException*/
    public Object evaluateWhileCondition(While whileObj,ActivityStatusStore statusStore,Map rules) throws GroovyException, IOException, InterruptedException {
        return evaluateUserExpr(whileObj.getTest(), whileObj.getId(),statusStore,rules);
    }
    /** evaluates the items of a for activity 
     * @throws IOException
     * @throws InterruptedException
     * @throws GroovyException*/
    public Object evaluateForItems(For forObj,ActivityStatusStore statusStore,Map rules) throws GroovyException, IOException, InterruptedException {
        return  evaluateUserExpr(forObj.getItems(),forObj.getId(),statusStore,rules);
   
    }
    /** evaluate the items of a parfor activity 
     * @throws IOException
     * @throws InterruptedException
     * @throws GroovyException*/
    public Object evaluateParforItems(Parfor forObj,ActivityStatusStore statusStore,Map rules) throws GroovyException, IOException, InterruptedException {
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
        Script sc = compileUserScript(expr.toString());
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
     */
    private Binding createScriptBinding(ActivityStatusStore statusStore, Map rules) {
        Binding b = new Binding();
        b.setVariable("jes",jes);
        for (int i = 0;  i < Status.allStatus.size() ; i++ ) {
            Status stat = (Status)Status.allStatus.get(i);
            b.setVariable(stat.getName(),stat);
        }
        // basic stuff.
        JesShell.createBasicScriptBinding(b, getToolbox(), jes.getWorkflow().getCredentials());
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
    /**
     * @param b
     * @param toolbox2 @todo
     * @param credentials @todo
     */
    public static void createBasicScriptBinding(Binding b, Toolbox toolbox2, Credentials credentials) {
        b.setVariable("astrogrid",toolbox2);
    
        b.setVariable("account",credentials.getAccount());
        String name = credentials.getAccount().getName();
        String community = credentials.getAccount().getCommunity();
        User u = new User(name,community,credentials.getGroup().getName(),credentials.getSecurityToken());
        b.setVariable("user",u);        
    
        try {
            b.setVariable("userIvorn",new Ivorn("ivo://" + community + "/" +  name));
        } catch (URISyntaxException e) {
            logger.error("URISyntaxException when creating userIvorn.",e);
        }
        b.setVariable("homeIvorn",new Ivorn(community,name,name + "/"));
        
    }


    
    
}


/* 
$Log: JesShell.java,v $
Revision 1.18  2005/03/15 16:18:19  clq2
nww986

Revision 1.17.26.1  2005/03/14 18:44:55  nw
upped the amount of time a shell stays around.

Revision 1.17  2004/12/03 14:47:41  jdt
Merges from workflow-nww-776

Revision 1.16.2.1  2004/12/01 21:50:53  nw
tried to factor out the different parts of the JEScript environment

Revision 1.16  2004/11/29 20:00:24  clq2
jes-nww-714

Revision 1.15.12.5  2004/11/26 13:13:28  nw
fix

Revision 1.15.12.4  2004/11/26 12:51:30  nw
added more useful info into script namespace.

Revision 1.15.12.3  2004/11/26 01:31:18  nw
updated dependency on groovy to 1.0-beta7.
updated code and tests to fit.

Revision 1.15.12.2  2004/11/24 18:49:02  nw
sandboxing of script execution - first by a timeout,
later by java permissions system.

Revision 1.15.12.1  2004/11/24 00:23:20  nw
get script running in a separate thread, with a timeout (bz#665)
worked new scripting objects into environment (bz#715)

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