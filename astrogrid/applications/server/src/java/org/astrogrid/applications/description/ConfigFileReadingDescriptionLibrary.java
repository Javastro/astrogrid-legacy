/*
 * $Id: ConfigFileReadingDescriptionLibrary.java,v 1.8 2009/02/26 12:45:54 pah Exp $
 * 
 * Created on 18 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.description;

import java.net.URL;


import junit.framework.Test;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.impl.CeaCmdLineApplicationDefinition;

/**
 * {@link ApplicationDefinition} library that can load and contain
 * {@link CeaCmdLineApplicationDefinition}s. Acts as a factory for applications
 * read from a file.
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 25 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class ConfigFileReadingDescriptionLibrary extends
        BaseApplicationDescriptionLibrary {
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.astrogrid.applications.description.BaseApplicationDescriptionLibrary
     * #getName()
     */
    @Override
    public String getName() {
        return "Configuration File Reading Library";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.astrogrid.applications.description.BaseApplicationDescriptionLibrary
     * #getInstallationTest()
     */
    @Override
    public Test getInstallationTest() {
        return new InstallationTest("testLoadApplications");
    }

    /**
     * The installation test case for
     * 
     * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
     * @version $Name:  $
     * @since VOTech Stage 7
     */
    public class InstallationTest extends TestCase {
        public InstallationTest(String name) {
            super(name);
        }

        public void testLoadApplications() {
            String error = getErrorMessage();
            if (error == null) {
                error = "ok";
            }
            assertTrue(error, loadok);
        }

    }

    /**
     * Logger for this class
     */
    static final Log logger = LogFactory
            .getLog(ConfigFileReadingDescriptionLibrary.class);
    public ConfigFileReadingDescriptionLibrary(Configuration conf) {
        this(conf, conf.getApplicationDescriptionUrl());
    }

    public ConfigFileReadingDescriptionLibrary(Configuration conf,
            URL applicationDescriptionUrl) {
        super(conf);
        loadok = loadApplications(applicationDescriptionUrl);

    }
}

/*
 * $Log: ConfigFileReadingDescriptionLibrary.java,v $
 * Revision 1.8  2009/02/26 12:45:54  pah
 * separate more out into cea-common for both client and server
 *
 * Revision 1.7  2008/10/09 11:47:27  pah
 * add dynamic app description library & refactor more funtionality to base class
 *
 * Revision 1.6  2008/09/24 13:40:49  pah
 * package naming changes
 *
 * Revision 1.5  2008/09/18 09:02:32  pah
 * add capability to read files from a directory
 * Revision 1.4 2008/09/13
 * 09:49:18 pah make installation test report error
 * 
 * Revision 1.3 2008/09/10 23:27:17 pah moved all of http CEC and most of
 * javaclass CEC code here into common library
 * 
 * Revision 1.2 2008/09/03 14:18:43 pah result of merge of pah_cea_1611 branch
 * 
 * Revision 1.1.2.2 2008/08/29 07:28:26 pah moved most of the commandline CEC
 * into the main server - also new schema for CEAImplementation in preparation
 * for DAL compatible service registration
 * 
 * Revision 1.1.2.1 2008/08/02 13:33:56 pah safety checkin - on vacation
 * 
 * Revision 1.1.2.5 2008/06/11 14:32:48 pah merged the ids into the application
 * execution environment
 * 
 * Revision 1.1.2.4 2008/05/13 16:02:47 pah uws with full app running UI is
 * working
 * 
 * Revision 1.1.2.3 2008/04/04 15:34:52 pah Have got bulk of code working with
 * spring - still need to remove all picocontainer refs ASSIGNED - bug 1611:
 * enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * 
 * Revision 1.1.2.2 2008/03/26 17:29:51 pah Unit tests pass
 * 
 * Revision 1.1.2.1 2008/03/19 23:15:43 pah First stage of refactoring done -
 * code compiles again - not all unit tests passed
 * 
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 */
