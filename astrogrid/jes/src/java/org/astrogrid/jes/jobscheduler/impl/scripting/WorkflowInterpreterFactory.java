/*$Id: WorkflowInterpreterFactory.java,v 1.1 2004/07/09 09:30:28 nw Exp $
 * Created on 14-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.scripting;

import org.astrogrid.workflow.beans.v1.execution.Extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.python.core.PyException;
import org.python.core.PyFile;
import org.python.core.PyJavaInstance;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/** Factory object that takes care of creating fresh and unpickling existing workflow interpreters.
 * @author Noel Winstanley nw@jb.man.ac.uk 14-May-2004
 *
 */
public class WorkflowInterpreterFactory {
    private static final Log log = LogFactory.getLog(WorkflowInterpreterFactory.class);
    /** configuration interface - defines where to load library py files from
     *  - pity, but doesn't seem to work just referring to the classpath
     * @author Noel Winstanley nw@jb.man.ac.uk 06-Jul-2004
     *
     */
    public interface JarPaths {
     
        static final String LIBRARY_JAR = "jython-lib" ;
        static final String JES_JAR = "astrogrid-jes";        
        public String getLibraryJarPath();
        public String getJesJarPath();
    }
    
    /** Construct a new WorkflowInterpreterFactory
     * 
     */
    public WorkflowInterpreterFactory(JarPaths paths) {        
        this.paths = paths;
        this.initialize();
    }
    private final JarPaths paths;

    /** sets python path to classes - need to zip up jython lib to jar in classpath at some point.
     * necessary to call this before any workflow interpreters are created.
     * */
    public final void initialize() {
        log.debug("initializing");
        Properties props = new Properties();

        File cacheFile = null;
        try {
            cacheFile = File.createTempFile("python-cachedir",null);
        } catch (IOException  e) {
            cacheFile = new File("/tmp");
        }
        cacheFile.delete();
        cacheFile.mkdirs();
        cacheFile.deleteOnExit();
        log.debug("Cache dir set to " + cacheFile.getAbsolutePath());
        props.setProperty("python.cachedir",cacheFile.getAbsolutePath());
        PythonInterpreter.initialize(System.getProperties(),props, new String[]{});
    }

    
    public WorkflowInterpreter unpickleFrom(InterpreterEnvironment env) throws ScriptEngineException {        
        Extension pickleJar = (Extension)env.getWorkflow().findXPathValue(WorkflowInterpreter.EXTENSION_XPATH);
        PyFile file = new PyFile(new ByteArrayInputStream(pickleJar.getContent().getBytes()));
        try {
            WorkflowInterpreter result = newWorkflowInterpreter(env);
            result.pyInterpreter.set("_file",file);
            result.pyInterpreter.exec("import cPickle; interp = cPickle.load(_file)");        
            return result;
        } catch (PyException e) {
            throw new ScriptEngineException("Failed to unpickle interpreter",e);
        }
    }
    
    
    public WorkflowInterpreter newWorkflowInterpreter(String pythonScript,InterpreterEnvironment env) throws ScriptEngineException{
        WorkflowInterpreter interp = newWorkflowInterpreter(env);                  
        try {
            interp.pyInterpreter.exec(pythonScript);
            return interp;
        } catch (PyException e) {
            throw new ScriptEngineException("Could not create script engine",e);
        }
    }
    public WorkflowInterpreter newWorkflowInterpreter(InterpreterEnvironment env) throws ScriptEngineException {
        // splice env object into builtins, as '_jes
        PySystemState state = new PySystemState();
   //     if (jesJar != null) {
            state.path.append(new PyString(paths.getJesJarPath()));
    /*    } else {
            state.path.append
        }*/
        state.path.append(new PyString(paths.getLibraryJarPath() + "/Lib"));
        log.debug("Path set to " + state.path.toString());        
        state.builtins.__setitem__("_jes",new PyJavaInstance(env));
        try {
            return new WorkflowInterpreter(state);
        } catch (PyException t) {
            throw new ScriptEngineException("Could not create script engine",t);            
        }               
        
    } 
    
}
/*
 * 

    private static String libJar = null;
    private static String jesJar = null;    
    *

        // 
       // props.setProperty("python.path", System.getProperty("java.class.path")+ File.pathSeparator + "/usr/share/jython/Lib");
 */


/* 
$Log: WorkflowInterpreterFactory.java,v $
Revision 1.1  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/