/*
 * $Id: ConfManager.java,v 1.7 2004/03/29 17:17:55 jdt Exp $
 * Created on 19-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * Copyright (C) AstroGrid. All rights reserved.
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 */
package org.astrogrid.integrationtest.common;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
/**
 * Deal with properties common to all the tests
 * @author jdt 
 */
public final class ConfManager {
    /**
     * Logger
     */
    private static final Log log = LogFactory.getLog(ConfManager.class);
    /**
     * Name of properties file
     */
    public static final String WEBSERVICES_PROPS = "/webservices.properties";
    /**
     * Name of key in property file for endpoint
     */
    private static final String MYSPACE_ENDPOINT = "mySpaceEndPoint";
    /**
     * name of key in properties file for merlin endpoint
     */
    private static final String STD_PAL_ENDPOINT = "stdDatacenterEndPoint";
    /**
     * name of key in properties file for the jobcontroller endpoint;
     */
    private static final String JOBCONTROLLER_ENDPOINT = "jobControlerEndPoint";
    /**
     * name of key in properties file for the portal we;
     */
    private static final String PORTAL_SITE = "portalWebSite";
    /**
     * Singleton
     */
    private static final ConfManager instance = new ConfManager();
    /**
     * since we're loading up the props in a statically called ctor, can't
     * throw an exception so gotta use a flag.
     */
    private boolean fileNotLoaded = true;
    /**
     * The class's raison d'etre
     */
    private Config config;
    /**
     * ctor
     */
    private ConfManager() {
        final URL url = this.getClass().getResource(WEBSERVICES_PROPS);
        assert url != null : "No properties file found";
        log.debug("Attempting to load " + ConfManager.WEBSERVICES_PROPS + " from "+url);
        try {
            SimpleConfig.load(url);
            fileNotLoaded = false;
        } catch (IOException ex) {
            log.error("Problem loading test properties", ex);
        }
        config = SimpleConfig.getSingleton();
    }
    /**
     * singleton pattern
     *
     * @return the oneandonlyinstance
     */
    public static ConfManager getInstance() {
        return instance;
    }
    
    /**
     * Static access to the singleton's the underlying config
     *
     * @return the config
     */
    public static Config getConfig() {
        return getInstance().config;
    }
    /**
     * Get the endpoint of our integration test myspace server
     *
     * @return string containing url
     * @throws IOException if we couldn't load the props
     */
    public String getMySpaceEndPoint() throws IOException {
        return getProperty(
            ConfManager.MYSPACE_ENDPOINT,
            "MySpace Web service end-point");
    }
    /**
     * Gets the jobcontroller endpoint in the integration environment.
     *
     * @return the endpoint url
     * @throws IOException if we couldn't load the props
     */
    public String getJobControllerEndPoint() throws IOException {
        return getProperty(JOBCONTROLLER_ENDPOINT, "job controller end point");
    }
    /**
     * Gets the portal website in the integration environment.
     *
     * @return the web site url
     * @throws IOException if we couldn't load the props
     */
    public String getPortalURL() throws IOException {
        return getProperty(PORTAL_SITE, "portal website");
    }
    /**
     * Gets the names property from the props file
     *
     * @param propertyDescription description of the property (just used for
     *            logging)
     * @param propertyName name of the property in the props file
     * @return string requested property
     * @throws IOException if we couldn't load the props
     */
    private String getProperty(
        final String propertyName,
        final String propertyDescription)
        throws IOException {
        if (fileNotLoaded) {
            throw new IOException("Configuration file not loaded");
        }
        final String property = config.getString(propertyName);
        log.debug(propertyDescription + ": " + property);
        assert property != null && property.length() > 0;
        return property;
        
        
         
    }
    /**
     * get the endpoint of a datacenter server containing the standard default
     * database (ie, a freshyly installed PAL)
     *
     * @return string containing url
     * @throws IOException if we couldn't load the props
     */
    public String getStdDatacenterEndPoint() throws IOException {
        return getProperty(
            ConfManager.STD_PAL_ENDPOINT,
            "Std Datacenter endpoint");
    }
}
/*
 * $Log: ConfManager.java,v $
 * Revision 1.7  2004/03/29 17:17:55  jdt
 * Refactored ConfManager to use the astrogrid config class
 *
 * Revision 1.6  2004/03/29 12:08:03  jdt
 * Added new prop for portal.  Think this class should be deprecated
 * in favour of Martin's config thing.  Let's face it, it's  pants.
 *
 * Revision 1.5  2004/03/22 16:31:08  mch
 * Changed merlin to std datacenter
 *
 * Revision 1.4  2004/03/12 23:53:26  jdt
 * my formatter's on the blink
 * Revision 1.3 2004/03/12 23:16:56 jdt got rid of
 * nasty javadoc format Revision 1.1 2004/03/04 19:06:04 jdt Package name
 * changed to lower case to satisfy coding standards. mea culpa - didn't read
 * the Book. Tx Martin. Revision 1.4 2004/03/04 11:44:20 pah added
 * jobcontroller endpoint getter made the keys private, so that user is forced
 * to go via the singleton Revision 1.3 2004/02/17 23:59:17 jdt commented out
 * lines killing the build, and made to conform to coding stds Revision 1.2
 * 2004/02/17 15:21:17 jdt formatted to remove tabs (cf coding standards)
 * Revision 1.1 2004/02/17 02:03:03 jdt Moved some things around, and added
 * more mySpace tests. URL tests, and threaded tests not yet complete. Revision
 * 1.3 2004/02/14 16:03:25 jdt refactored Revision 1.2 2004/01/22 16:14:18 nw
 * added conf entry for merlin datacenter Revision 1.1 2004/01/19 17:11:06 jdt
 * Refactored Revision 1.1 2004/01/19 16:44:38 jdt Refactored common properties
 * file.
 */
