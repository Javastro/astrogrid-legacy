/*$Id: JarPathsFromConfig.java,v 1.1 2004/07/09 09:30:28 nw Exp $
 * Created on 08-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.scripting;

import org.astrogrid.config.Config;

/** implementation of jar-paths config for productions - queries config for settings.
 *
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jul-2004
 *
 */
public class JarPathsFromConfig implements WorkflowInterpreterFactory.JarPaths {
    /** key used to retreive file path to python library jar from config*/
    public static final String LIB_JAR_KEY = "jes.jarpaths.libjar";
    /** key used to retreive file path to jes jar from config */
    public static final String JES_JAR_KEY = "jes.jarpaths.jesjar";
    public JarPathsFromConfig(Config conf) {
        libJarPath = conf.getString(JarPathsFromConfig.LIB_JAR_KEY);
        jesJarPath = conf.getString(JarPathsFromConfig.JES_JAR_KEY);
    }
    protected final String jesJarPath;
    protected final String libJarPath;

    public String getLibraryJarPath() {
        return libJarPath;
    }

    public String getJesJarPath() {
        return jesJarPath;
    }
    
}

/* 
$Log: JarPathsFromConfig.java,v $
Revision 1.1  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/