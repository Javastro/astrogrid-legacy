/*$Id: HttpApplicationCEAComponentManager.java,v 1.5 2006/01/09 17:52:36 clq2 Exp $
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
import org.astrogrid.applications.description.registry.RegistryAdminLocator;
import org.astrogrid.applications.description.registry.RegistryQueryLocator;
import org.astrogrid.applications.http.HttpApplicationDescriptionLibrary;
import org.astrogrid.applications.http.registry.RegistryQuerier;
import org.astrogrid.applications.http.registry.RegistryQuerierImpl;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.query.RegistryService;

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
        registerDefaultServices(pico);
        
        // metadata - note that his is not the default provider
        EmptyCEAComponentManager.registerVOProvider(pico,
                                                    HttpMetadataService.class);
        
        //registry uploader
        EmptyCEAComponentManager.registerDefaultRegistryUploader(pico);

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

        pico.registerComponentImplementation(HttpApplicationDescriptionLibrary.class,
                HttpApplicationDescriptionLibrary.class);
        pico.registerComponentImplementation(RegistryQuerier.class, RegistryQuerierImpl.class);
        pico.registerComponentInstance(RegistryQueryLocator.class,new RegistryQueryLocator() {

           public RegistryService getClient() {                
               return RegistryDelegateFactory.createQuery();
           }
       });        

		if (logger.isDebugEnabled()) {
			logger.debug("registerHttpApplicationProvider(MutablePicoContainer, Config) - end");
		}
    }
}

/*
 * $Log: HttpApplicationCEAComponentManager.java,v $
 * Revision 1.5  2006/01/09 17:52:36  clq2
 * gtr_1489_apps
 *
 * Revision 1.4.20.1  2005/12/22 13:56:03  gtr
 * Refactored to match the other kinds of CEC.
 *
 * Revision 1.4  2005/08/10 14:45:37  clq2
 * cea_pah_1317
 *
 * Revision 1.3.4.1  2005/07/21 15:10:39  pah
 * changes to acommodate contol component, and starting to change some of the static methods to dynamic
 *
 * Revision 1.3  2005/07/05 08:27:01  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.2.68.2  2005/06/14 09:49:32  pah
 * make the http cec only register itself as a ceaservice - do not try to reregister any cea applications that it finds
 *
 * Revision 1.2.68.1  2005/06/09 08:47:33  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.2.54.2  2005/06/08 22:10:46  pah
 * make http applications v10 compliant
 *
 * Revision 1.2.54.1  2005/06/02 14:57:29  pah
 * merge the ProvidesVODescription interface into the MetadataService interface
 *
 * Revision 1.2  2004/11/27 13:20:03  pah
 * result of merge of pah_cea_bz561 branch
 *
 * Revision 1.1.34.1  2004/11/09 09:21:16  pah
 * initial attempt to rationalise authorityID use & self registering
 *
 * Revision 1.1  2004/09/02 10:19:17  jdt
 * Moved to a more sensible package (from org.astrogrid.applications.component)
 *
 * Revision 1.4  2004/09/01 15:42:26  jdt
 * Merged in Case 3
 *  
 */