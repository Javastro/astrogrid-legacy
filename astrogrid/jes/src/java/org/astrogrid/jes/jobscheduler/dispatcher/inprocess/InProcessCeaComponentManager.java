/*$Id: InProcessCeaComponentManager.java,v 1.4 2005/07/05 08:27:01 clq2 Exp $
 * Created on 07-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher.inprocess;

import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.CeaThreadPool;
import org.astrogrid.applications.manager.DefaultMetadataService;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.ThreadPoolExecutionController;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.config.Config;

import org.picocontainer.MutablePicoContainer;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import junit.framework.Test;

/** Set up a cea server for execution of in-process applications.
 * Difference between this and the usual vanilla server is that the QueryService in am {@link org.astrogrid.jes.jobscheduler.dispatcher.inprocess.InProcessQueryService}
 * which will callback notifications directly to the job monitor and results listener interfaces. 
 * @todo currently configured, but empty. needs to be populated with proxy applicaitons.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Feb-2005
 *
 */
public class InProcessCeaComponentManager extends EmptyCEAComponentManager implements ComponentDescriptor{

    
    /** Construct a new InProcessCeaServer
     * 
     */
    public InProcessCeaComponentManager(MutablePicoContainer parent, Config config) {
        super();   
        pico = parent.makeChildContainer();
       
        pico.registerComponentImplementation(ApplicationDescriptionEnvironment.class,ApplicationDescriptionEnvironment.class);
        pico.registerComponentImplementation(ExecutionController.class,ThreadPoolExecutionController.class);
        pico.registerComponentImplementation(PooledExecutor.class,CeaThreadPool.class);
        pico.registerComponentImplementation(QueryService.class,InProcessQueryService.class);   
       EmptyCEAComponentManager.registerCompositeApplicationDescriptionLibrary(pico);
        EmptyCEAComponentManager.registerContainerApplicationDescriptionLibrary(pico);    
        // store
        EmptyCEAComponentManager.registerDefaultPersistence(pico,config);
        // metadata
        EmptyCEAComponentManager.registerDefaultVOProvider(pico,config);
        // the protocol lib
        EmptyCEAComponentManager.registerProtocolLibrary(pico);
        EmptyCEAComponentManager.registerStandardIndirectionProtocols(pico);
        EmptyCEAComponentManager.registerAstrogridIndirectionProtocols(pico);
        // @todo now hook in out own implementations..
        //JavaApplicationCEAComponentManager.registerJavaClassProvider(pico,config);
        //HttpApplicationCEAComponentManager.registerHttpApplicationProvider(pico,config);
        // do this by registering the application instances directly.        
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "In-Process Cea Server";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Contains Components:\n----------------------------------------------\n" + super.information() + "\n-----------------------------------------------";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }        
   

}


/* 
$Log: InProcessCeaComponentManager.java,v $
Revision 1.4  2005/07/05 08:27:01  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.3.10.1  2005/06/09 10:03:20  pah
removed separate registration of metadata service

Revision 1.3  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.2.20.1  2005/04/11 13:56:30  nw
organized imports

Revision 1.2  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.1.2.1  2005/03/11 14:04:30  nw
in-process cea server
 
*/