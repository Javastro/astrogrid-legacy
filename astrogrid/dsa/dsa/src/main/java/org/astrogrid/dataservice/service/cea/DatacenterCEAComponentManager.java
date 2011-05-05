/*$Id: DatacenterCEAComponentManager.java,v 1.3 2011/05/05 14:49:37 gtr Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.dataservice.service.cea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.manager.BaseConfiguration;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.dataservice.service.DataServer;


/** Component manager implementation that assembles a CEA server which provides a single {@link DatacetnerApplicationDescription} for the
 * datacenter application
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterCEAComponentManager extends EmptyCEAComponentManager {
    /**
     * Commons Logger for this class
     */
    private static final Log LOG = LogFactory.getLog(DatacenterCEAComponentManager.class);

    /** 
     * Constructs a new {@code DatacenterCEAComponentManager}.
     * <p>
     * Execution-history and id-generator components are overridden with
     * DSA-specific implementations. A standard {@code BaseConfiguration} is
     * registered. A DSA-specific application-description-library is registered.
     */
    public DatacenterCEAComponentManager() {
        super();

        // "pico" is a Picocontainer instance declared and initialized in the
        // superclass.

        registerDefaultServices(pico);
        EmptyCEAComponentManager.registerDefaultVOProvider(pico);
        
        // Force the CEC to refer to the UWS job-records.
        pico.unregisterComponent(ExecutionHistory.class);
        pico.registerComponentImplementation(ExecutionHistory.class,DatacenterExecutionHistory.class);

        // Force the CEC to use DSA's flavour of job identifiers.
        pico.unregisterComponent(IdGen.class);
        pico.registerComponentImplementation(IdGen.class, DatacenterIdGenerator.class);
        
        pico.registerComponentImplementation(DatacenterApplicationDescriptionLibrary.class,DatacenterApplicationDescriptionLibrary.class);
        pico.registerComponentImplementation(DataServer.class,DataServer.class);
        pico.registerComponentImplementation(Configuration.class, BaseConfiguration.class);
        
        // This is the generic protocol for VOSpace. Eventually, the CEA core
        // will include this by default, at which point this line has to be
        // taken out of DSA. We're superimposing it on an old version of the CEA 
        // core.
        pico.registerComponentImplementation(VosProtocol.class);
    }
    
}