/*$Id: DatacenterCEAComponentManager.java,v 1.1 2004/07/13 17:11:09 nw Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.service.v06;

import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.service.DataServer;

import org.picocontainer.MutablePicoContainer;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterCEAComponentManager extends EmptyCEAComponentManager {
    /** Construct a new DatacenterCEAComponentManager
     * 
     */
    public DatacenterCEAComponentManager() {
        super();
        final Config config = SimpleConfig.getSingleton();
        // controller & queriers        
        EmptyCEAComponentManager.registerDefaultServices(pico);
        // store
        EmptyCEAComponentManager.registerDefaultPersistence(pico,config);
        // metadata
        // @todo - set config to point to a differen template file.
        EmptyCEAComponentManager.registerDefaultVOProvider(pico,config);
        // the protocol lib
        EmptyCEAComponentManager.registerProtocolLibrary(pico);
        EmptyCEAComponentManager.registerStandardIndirectionProtocols(pico);
        EmptyCEAComponentManager.registerAstrogridIndirectionProtocols(pico);    
        registerDatacenterProvider(pico,config);    
    }
    
    public static void registerDatacenterProvider(MutablePicoContainer pico, Config config) {
        // register application description library.
        // may need to pass in more stuff here later..
        pico.registerComponentImplementation(DatacenterApplicationDescriptionLibrary.class,DatacenterApplicationDescriptionLibrary.class);
        pico.registerComponentImplementation(DataServer.class,DataServer.class);
        
        
    }
}


/* 
$Log: DatacenterCEAComponentManager.java,v $
Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/