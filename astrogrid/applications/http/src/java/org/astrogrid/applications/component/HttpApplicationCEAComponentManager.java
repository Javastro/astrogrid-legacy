/*$Id: HttpApplicationCEAComponentManager.java,v 1.2 2004/07/30 14:54:47 jdt Exp $
 * Created on Jul 24, 2004 or thereabouts
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.applications.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.http.HttpApplicationDescriptionLibrary;
import org.astrogrid.applications.http.registry.RegistryQuerier;
import org.astrogrid.applications.http.registry.RegistryQuerierDummy;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.picocontainer.MutablePicoContainer;

/**
 * Simple component manager that defines a standalone HttpApplication CEA server
 * 
 * @author jdt
 * @TODO Tidy up superfluous stuff
 */
public class HttpApplicationCEAComponentManager extends EmptyCEAComponentManager {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(HttpApplicationCEAComponentManager.class);

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
        // now hook in out own implementation
        registerHttpApplicationProvider(pico, config);
    }

    /**
     * just register the components specific to the HttpApplications provider,
     * but none of the generic components
     * 
     * @see {@link #COMMUNITY_KEY}
     */
    public static final void registerHttpApplicationProvider(MutablePicoContainer pico, final Config config) {
        pico.registerComponentInstance(new HttpApplicationDescriptionLibrary.Community() {
            protected final String community = config.getString(COMMUNITY_NAME, "org.astrogrid.localhost");

            public String getCommunity() {
                return community;
            }
        });
        pico.registerComponentImplementation(HttpApplicationDescriptionLibrary.class,
                HttpApplicationDescriptionLibrary.class);
        //@TODO consider whether this needs to be got from config. Hardwire
        // dummy for now
        pico.registerComponentImplementation(RegistryQuerier.class, RegistryQuerierDummy.class);
    }
}

/*
 * $Log: HttpApplicationCEAComponentManager.java,v $
 * Revision 1.2  2004/07/30 14:54:47  jdt
 * merges in from case3 branch
 *
 * Revision 1.1.2.5  2004/07/30 13:11:44  jdt
 * reformatted
 * Revision 1.1.2.4 2004/07/30
 * 11:02:30 jdt Added unit tests, refactored the RegistryQuerier anf finished
 * off HttpApplicationCEAComponentManager.
 * 
 * Revision 1.1.2.3 2004/07/29 21:30:47 jdt ** empty log message ***
 * 
 * Revision 1.1.2.2 2004/07/29 17:08:22 jdt Think about how I'm going to get
 * stuff out of the registry
 * 
 * Revision 1.1.2.1 2004/07/29 16:35:05 jdt Safety checkin, while I think about
 * what happens next.
 *  
 */