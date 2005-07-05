/*
 * $Id: WebServiceCEAComponentManager.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 * 
 * Created on Feb 28, 2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.ws;

import org.picocontainer.MutablePicoContainer;

import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.component.ComponentManager;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

/**
 * Component manager that creates a CEA server that can run arbitrary web services.
 * @author Paul Harrison (pharriso@eso.org) Feb 28, 2005
 * @version $Name:  $
 * @since iteration9
 */
public class WebServiceCEAComponentManager extends EmptyCEAComponentManager
        implements ComponentManager {

    /**
     * 
     */
    public WebServiceCEAComponentManager() {
        super();
        final Config config =   SimpleConfig.getSingleton();        
 
        // base cea server also needs a provider for vodescription - registry entry builder does the job.        
        EmptyCEAComponentManager.registerDefaultVOProvider(pico,config);
        EmptyCEAComponentManager.registerDefaultRegistryUploader(pico);
        
        // now need the other side - the cec manager itself.
        EmptyCEAComponentManager.registerDefaultServices(pico);        
        EmptyCEAComponentManager.registerDefaultPersistence(pico,config);
        // indirection handlers
        EmptyCEAComponentManager.registerProtocolLibrary(pico);
        EmptyCEAComponentManager.registerAstrogridIndirectionProtocols(pico);
        EmptyCEAComponentManager.registerStandardIndirectionProtocols(pico);
        // now the provider
        registerWebServiceProvider(pico,config);
    }

    /**
     * @param pico
     * @param config
     */
    private void registerWebServiceProvider(MutablePicoContainer pico, Config config) {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("WebServiceCEAComponentManager.registerWebServiceProvider() not implemented");
    }

}
