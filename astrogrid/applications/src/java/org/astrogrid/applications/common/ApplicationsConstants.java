/*
 * $Id: ApplicationsConstants.java,v 1.2 2003/11/26 22:07:24 pah Exp $
 * 
 * Created on 17-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.common;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public final class ApplicationsConstants {
   

/**
 * The key used to look up the location of the configuration file
 */
public final static String ConfigFileKey = "AstrogridApplicationsConfigURL";

/**
 * the JNDI key for the datasource that represents the applicationController datasource
 */
public final static String DataSourceName = "AstrogridApplicationControllerDatasource";

// the keys below are found in the configuration file.

/**
 * The value of the property pointed to by this key is the application configuration file
 */
public final static String ApplicationConfigKey="ApplicationConfigFile";


/**
 * The value of the property pointed to bey this key is the directory used as a base for running the applications.
 */
public final static String WorkingDirectory = "WorkingDirectory";
}
