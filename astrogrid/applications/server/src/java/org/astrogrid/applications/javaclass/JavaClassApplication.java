/*$Id: JavaClassApplication.java,v 1.8 2006/06/13 20:33:13 clq2 Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.javaclass;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** An application that executes by calling a static java method
 * @see org.astrogrid.applications.javaclass.JavaClassApplicationDescription
 * @see java.lang.reflect.Method
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 */
public class JavaClassApplication extends AbstractApplication {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JavaClassApplication.class);

    /** Construct a new JavaClassApplication
     * @param ids
     * @param user
     * @param tool
     * @param description
     */
    public JavaClassApplication(IDs ids, Tool tool, ApplicationInterface interf, ProtocolLibrary lib) {
        super(ids, tool, interf,lib);
       // CeaUser.setUser(ids.getUser()); // Makes the User object available as ThreadLocal - but doesn't work.
    }
    
    /** Starts the application executing.
     * standard pattern - processes all input parameters, then starts a background thread to perform the execution itself.
     * @todo bug here - we assume our parameters are in the correct order to pass to the java method. should sort them into correct order first.
     * @see org.astrogrid.applications.Application#execute(org.astrogrid.applications.ApplicationExitMonitor)
     */
    public Runnable createExecutionTask() throws CeaException {
        createAdapters();

       JavaClassApplicationDescription jappDesc = (JavaClassApplicationDescription)getApplicationDescription();            
       Runnable task = new Worker(jappDesc.method);
       setStatus(Status.INITIALIZED);
       return task;
   
    }
     /** A Worker thread, that performs the computation after {@link JavaClassApplication#execute() } returns */
     protected class Worker implements Runnable {
         /** Construct a new Worker
         * @param args the arguments to the call 
         * @param m the method to call
         */
        public Worker(Method m) {
             this.method =m;
         }
         protected final Method method;
         /** 'executes' the application by calling {@link Method#invoke(java.lang.Object, java.lang.Object[])}*/
         public void run() {   
             try {
                 List args = new ArrayList();
                 for (Iterator i = inputParameterAdapters(); i.hasNext(); ) {
                     ParameterAdapter a = (ParameterAdapter)i.next();
                     args.add( a.process());
                 }             
                 setStatus(Status.RUNNING);
                 Object resultVal = null;

                resultVal = method.invoke(null,args.toArray());
                
                // we can do this, as we know there's only ever going to be one interface, and one output parameter.
                setStatus(Status.WRITINGBACK);
                ParameterAdapter result = (ParameterAdapter)outputParameterAdapters().next();
                result.writeBack(resultVal);
                setStatus(Status.COMPLETED);                
            } catch (IllegalArgumentException e) {
                reportError("Illegal Argument passed to  java 'application'",e);
            } catch (IllegalAccessException e) {
                reportError("Could not access java 'application'",e);
            } catch (InvocationTargetException e) {
                reportError("Invoked java 'application' raised an exception",e.getTargetException());
            } catch (CeaException e) {
                reportError("Failed to write back parameter values",e);
            } catch (Throwable t) {
                reportError("Something else gone wrong",t);
            }
         }
    }
    
    /** overridden to return a {@link JavaClassParameterAdapter}
     * @see org.astrogrid.applications.AbstractApplication#instantiateAdapter(org.astrogrid.applications.beans.v1.parameters.ParameterValue, org.astrogrid.applications.description.ParameterDescription, org.astrogrid.applications.parameter.indirect.IndirectParameterValue)
     */
    protected ParameterAdapter instantiateAdapter(ParameterValue pval,
            ParameterDescription descr, ExternalValue indirectVal) {
        return new JavaClassParameterAdapter(pval, descr, indirectVal);
    }

}


/* 
$Log: JavaClassApplication.java,v $
Revision 1.8  2006/06/13 20:33:13  clq2
pal_gtr_1671

Revision 1.7.162.1  2006/06/09 17:49:07  gtr
I added security features.

Revision 1.7  2004/09/17 01:21:20  nw
altered to work with new threadpool

Revision 1.6.40.1  2004/09/14 13:46:04  nw
upgraded to new threading practice.

Revision 1.6  2004/08/11 16:53:32  nw
added @todo for bug

Revision 1.5  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.4  2004/07/26 10:21:47  nw
javadoc

Revision 1.3  2004/07/22 16:32:54  nw
cleaned up application / parameter adapter interface.

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/