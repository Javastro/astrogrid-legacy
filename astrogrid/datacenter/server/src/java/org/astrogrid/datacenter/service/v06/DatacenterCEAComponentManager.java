/*$Id: DatacenterCEAComponentManager.java,v 1.2 2004/07/20 02:14:48 nw Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.MutablePicoContainer;

/** Component manager implementation that assembles a CEA server which provides a single {@link DatacetnerApplicationDescription} for the 
 * datacenter application 
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterCEAComponentManager extends EmptyCEAComponentManager {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DatacenterCEAComponentManager.class);

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
        EmptyCEAComponentManager.registerDefaultVOProvider(pico,config);
        // the protocol lib
        EmptyCEAComponentManager.registerProtocolLibrary(pico);
        EmptyCEAComponentManager.registerStandardIndirectionProtocols(pico);
        EmptyCEAComponentManager.registerAstrogridIndirectionProtocols(pico);    
        registerDatacenterProvider(pico,config);    
    }
    
    /** key used to lookup in config the name of the datacenter application (optional, defaults to {@link #DS_APP_NAME_DEFAULT}*/
    public static final String DS_APP_NAME_KEY = "datacenter.cea.app.name";
    /** default value for {@link #DS_APP_NAME} if not set in config */
    public static final String DS_APP_NAME_DEFAULT = "org.astrogrid.localhost/testdsa";
    
    /** register the datacenter-specific components of this cea server */
    public static void registerDatacenterProvider(MutablePicoContainer pico, Config config) {
        logger.info("Registering Datacenter CEA Provider");
        final String name= config.getString(DS_APP_NAME_KEY,DS_APP_NAME_DEFAULT);
        logger.info(DS_APP_NAME_KEY + ":=" + name);
        pico.registerComponentInstance(new DatacenterApplicationDescriptionLibrary.DatacenterMetadata() {
            public String getName() {
                return name;
            }
        });
        pico.registerComponentImplementation(DatacenterApplicationDescriptionLibrary.class,DatacenterApplicationDescriptionLibrary.class);
        pico.registerComponentImplementation(DataServer.class,DataServer.class);
        
        
    }
}


/* 
$Log: DatacenterCEAComponentManager.java,v $
Revision 1.2  2004/07/20 02:14:48  nw
final implementaiton of itn06 Datacenter CEA interface

Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/