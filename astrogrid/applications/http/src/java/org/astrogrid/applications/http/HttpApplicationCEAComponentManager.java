/*$Id: HttpApplicationCEAComponentManager.java,v 1.1 2004/09/02 10:19:17 jdt Exp $
 * Created on Jul 24, 2004 or thereabouts
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.applications.http;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.http.HttpApplicationDescriptionLibrary;
import org.astrogrid.applications.http.registry.RegistryQuerier;
import org.astrogrid.applications.http.registry.RegistryQuerierImpl;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.picocontainer.MutablePicoContainer;

/**
 * Component manager that assembles a standalone HttpApplication CEA server.
 * 
 * @author jdt
 * @TODO Tidy up superfluous stuff
 */
public class HttpApplicationCEAComponentManager extends EmptyCEAComponentManager {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(HttpApplicationCEAComponentManager.class);

    /**
     * key to look in config under for the community to add provided
     * applications to (optional, defaults to 'org.astrogrid.localhost')
     * 
     * @see #registerHttpApplicationProvider(MutablePicoContainer, Config)
     */
    public final static String COMMUNITY_NAME = "cea.httpapplication.community.name";

    /**
     * Construct a new HttpApplicationCEAComponentManager, with all necessary
     * components registered
     * <p />
     * registers the HttpApplication provider, plus all the standard services
     * defined in {@link EmptyCEAComponentManager}
     */
    public HttpApplicationCEAComponentManager() {
    	super();
    	logger.info("HttpApplicationCEAComponentManager() - registering components");
       
        final Config config = SimpleConfig.getSingleton();
        // controller & queriers
        EmptyCEAComponentManager.registerDefaultServices(pico);
        // store
        EmptyCEAComponentManager.registerDefaultPersistence(pico, config);
        // metadata
        EmptyCEAComponentManager.registerDefaultVOProvider(pico, config);
        // the protocol lib
        EmptyCEAComponentManager.registerProtocolLibrary(pico);
        EmptyCEAComponentManager.registerStandardIndirectionProtocols(pico);
        EmptyCEAComponentManager.registerAstrogridIndirectionProtocols(pico);
        // now hook in our own implementation
        registerHttpApplicationProvider(pico, config);
        logger.info("HttpApplicationCEAComponentManager() - done");
    }

    /**
     * just register the components specific to the HttpApplications provider,
     * but none of the generic components
     * 
     * @see {@link #COMMUNITY_KEY}
     */
    public static final void registerHttpApplicationProvider(MutablePicoContainer pico, final Config config) {
		if (logger.isDebugEnabled()) {
			logger.debug("registerHttpApplicationProvider(MutablePicoContainer, Config) - start");
		}

        pico.registerComponentInstance(new BaseApplicationDescriptionLibrary.Community() {
            protected final String community = config.getString(COMMUNITY_NAME, "org.astrogrid.localhost");

            public String getCommunity() {
                return community;
            }
        });
        pico.registerComponentImplementation(HttpApplicationDescriptionLibrary.class,
                HttpApplicationDescriptionLibrary.class);
        pico.registerComponentImplementation(RegistryQuerier.class, RegistryQuerierImpl.class);

		if (logger.isDebugEnabled()) {
			logger.debug("registerHttpApplicationProvider(MutablePicoContainer, Config) - end");
		}
    }
}

/*
 * $Log: HttpApplicationCEAComponentManager.java,v $
 * Revision 1.1  2004/09/02 10:19:17  jdt
 * Moved to a more sensible package (from org.astrogrid.applications.component)
 *
 * Revision 1.4  2004/09/01 15:42:26  jdt
 * Merged in Case 3
 *  
 */