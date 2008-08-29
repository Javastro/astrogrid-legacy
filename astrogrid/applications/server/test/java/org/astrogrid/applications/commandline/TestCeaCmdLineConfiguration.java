/*
 * $Id: TestCeaCmdLineConfiguration.java,v 1.1 2008/08/29 07:28:28 pah Exp $
 * 
 * Created on 4 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.CEAConfiguration;

/**
 * A configuration that makes sure that the directories are created.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 4 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class TestCeaCmdLineConfiguration extends CEAConfiguration {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
	    .getLog(TestCeaCmdLineConfiguration.class);
    
    
    public void init() {
	logger.info("creating directories");
	recordsDirectory.mkdir();
	temporaryFilesDirectory.mkdir();
	configurationDirectory.mkdir();
    }
}


/*
 * $Log: TestCeaCmdLineConfiguration.java,v $
 * Revision 1.1  2008/08/29 07:28:28  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.2  2008/08/02 13:32:23  pah
 * safety checkin - on vacation
 *
 * Revision 1.1.2.1  2008/04/04 15:34:52  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
