/*
 * $Id: ConfManager.java,v 1.3 2004/03/12 23:16:56 jdt Exp $ Created on 19-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * Copyright (C) AstroGrid. All rights reserved. This software is published under the terms of the AstroGrid Software
 * License version 1.2, a copy of which has been included with this distribution in the LICENSE.txt file.
 */
package org.astrogrid.integrationtest.common;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author jdt Deal with properties common to all the tests
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
    private static final String MERLIN_ENDPOINT = "merlinDatacenterEndPoint";
    /**
     * name of key in properties file for the jobcontroller endpoint;
     */
    private static final String JOBCONTROLLER_ENDPOINT = "jobControlerEndPoint";
    /**
     * Singleton
     */
    private static final ConfManager instance = new ConfManager();
    /**
     * since we're loading up the props in a statically called ctor, can't throw an exception so gotta use a flag.
     */
    private boolean fileNotLoaded = true;
    /**
     * The class's raison d'etre
     */
    private Properties props;
    /**
     * ctor
     */
    private ConfManager() {
        try {
            props = new Properties();
            log.debug("Attempting to load " + ConfManager.WEBSERVICES_PROPS);
            InputStream inputStream =
                this.getClass().getResourceAsStream(
                    ConfManager.WEBSERVICES_PROPS);
            assert inputStream != null : "No file found";
            props.load(inputStream);
            fileNotLoaded = false;
        } catch (IOException ex) {
            fileNotLoaded = true;
            log.error("Problem loading test properties", ex);
        }
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
     * Gets the names property from the props file
     * 
     * @param propertyDescription description of the property (just used for logging)
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
        final String property = props.getProperty(propertyName);
        log.debug(propertyDescription + ": " + property);
        assert property != null && property.length() > 0;
        return property;
    }
    /**
     * get the endpoint of a datacenter server containing the merlin dataset
     * 
     * @return string containing url
     * @throws IOException if we couldn't load the props
     */
    public String getMerlinDatacenterEndPoint() throws IOException {
        return getProperty(
            ConfManager.MERLIN_ENDPOINT,
            "Merlin Datacenter endpoint");
    }
}
/*
 * $Log: ConfManager.java,v $
 * Revision 1.3  2004/03/12 23:16:56  jdt
 * got rid of nasty javadoc format
 * Revision 1.1 2004/03/04 19:06:04 jdt Package name changed to lower case to satisfy coding
 * standards. mea culpa - didn't read the Book. Tx Martin. Revision 1.4 2004/03/04 11:44:20 pah added jobcontroller
 * endpoint getter made the keys private, so that user is forced to go via the singleton Revision 1.3 2004/02/17
 * 23:59:17 jdt commented out lines killing the build, and made to conform to coding stds Revision 1.2 2004/02/17
 * 15:21:17 jdt formatted to remove tabs (cf coding standards) Revision 1.1 2004/02/17 02:03:03 jdt Moved some things
 * around, and added more mySpace tests. URL tests, and threaded tests not yet complete. Revision 1.3 2004/02/14
 * 16:03:25 jdt refactored Revision 1.2 2004/01/22 16:14:18 nw added conf entry for merlin datacenter Revision 1.1
 * 2004/01/19 17:11:06 jdt Refactored Revision 1.1 2004/01/19 16:44:38 jdt Refactored common properties file.
 */