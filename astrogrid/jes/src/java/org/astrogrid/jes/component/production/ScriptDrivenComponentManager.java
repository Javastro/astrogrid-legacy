/*$Id: ScriptDrivenComponentManager.java,v 1.1 2004/07/09 09:30:28 nw Exp $
 * Created on 06-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component.production;

import org.astrogrid.component.ComponentManagerException;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.component.descriptor.SimpleComponentDescriptor;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.jes.component.EmptyJesComponentManager;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerDispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerIdDispatcher;
import org.astrogrid.jes.jobscheduler.impl.scripting.DefaultTransformers;
import org.astrogrid.jes.jobscheduler.impl.scripting.JarPathsFromConfig;
import org.astrogrid.jes.jobscheduler.impl.scripting.ScriptedSchedulerImpl;
import org.astrogrid.jes.jobscheduler.impl.scripting.WorkflowInterpreterFactory;

import org.picocontainer.MutablePicoContainer;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Jul-2004
 *
 */
public class ScriptDrivenComponentManager extends EmptyJesComponentManager {
    /** construct a component manager, that gets configuration from {@link SimpleConfig}*/
     public ScriptDrivenComponentManager() throws ComponentManagerException {
         this(SimpleConfig.getSingleton());
     }
     /** Construct a new ProductionComponentManager
      * @param conf confugration object to use to look up keys.
      */
     public ScriptDrivenComponentManager(Config conf) throws ComponentManagerException{
         super();
         try {
          pico.registerComponentInstance("jes-meta",JES_META);
          pico.registerComponentInstance(Config.class,conf); 

          registerScriptEngine(pico);   
         
         pico.registerComponentImplementation(Dispatcher.class,ApplicationControllerIdDispatcher.class);
         pico.registerComponentImplementation(ApplicationControllerDispatcher.Endpoints.class,EndpointsFromConfig.class);
         
         PolicyDrivenComponentManager.registerStandardComponents(pico);
         PolicyDrivenComponentManager.registerLocator(pico,conf);                    
         PolicyDrivenComponentManager.registerJobFactory(pico,conf);

         } catch (Exception e) {
             log.fatal("Could not create component manager",e);
             throw new ComponentManagerException(e);
        }
    }
    /**Description-only component - metadata for entire system. 
     * 
     * @todo add config tests for a production system in here */
    private static final ComponentDescriptor JES_META = new SimpleComponentDescriptor(
        "Script-Driven Job Execution System",
        "job execution system"
    );    

    public static void registerScriptEngine(MutablePicoContainer pico) {
             pico.registerComponentImplementation(SCHEDULER_ENGINE,ScriptedSchedulerImpl.class);
        
               pico.registerComponentImplementation(ScriptedSchedulerImpl.Transformers.class,DefaultTransformers.class);
        
               pico.registerComponentImplementation(WorkflowInterpreterFactory.class,WorkflowInterpreterFactory.class);
               pico.registerComponentImplementation(WorkflowInterpreterFactory.JarPaths.class,JarPathsFromConfig.class);
        
      }
        
}


/* 
$Log: ScriptDrivenComponentManager.java,v $
Revision 1.1  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/