/*
 * $Id: MockNonSpringConfiguredConfig.java,v 1.4 2009/07/01 13:33:20 pah Exp $
 * 
 * Created on 3 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.contracts;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.javaclass.JavaClassConfiguration;

/**
 * A configuration that can be used in unit tests that are not using the full
 * spring setup
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 3 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class MockNonSpringConfiguredConfig extends CEAConfiguration implements JavaClassConfiguration {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
	    .getLog(MockNonSpringConfiguredConfig.class);

    public MockNonSpringConfiguredConfig() {

	try {
	    baseDirectory = makeTemporaryDirectory();
	    this.configurationDirectory = new File(this.baseDirectory, "config");
	    this.configurationDirectory.mkdir();
	    this.recordsDirectory = new File(this.baseDirectory, "records");
	    this.recordsDirectory.mkdir();
	    this.temporaryFilesDirectory = new File(this.baseDirectory, "temp");
	    this.temporaryFilesDirectory.mkdir();
	    this.applicationDescriptionUrl = this.getClass().getResource("/TestApplicationConfig.xml");
	    this.serviceEndpoint = new URL(
		    "http://localhost:8888/cec/services/CommonExecutionConnectorService");

	} catch (IOException e) {
	    logger.fatal("error setting up cofiguration", e);
	}

    }

    public Class getApplicationClass() throws ClassNotFoundException {
       return Class.forName("org.astrogrid.applications.javaclass.SampleJavaClassApplications");
    }

}
/*
 * $Log: MockNonSpringConfiguredConfig.java,v $
 * Revision 1.4  2009/07/01 13:33:20  pah
 * registration template directly argument of builder object - removed from config
 *
 * Revision 1.3  2008/09/24 13:40:50  pah
 * package naming changes
 *
 * Revision 1.2  2008/09/03 14:19:05  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.3  2008/09/03 12:22:54  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1.2.2  2008/08/29 07:28:29  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/04/04 15:46:08  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
