/*$Id: DevelopmentJarPaths.java,v 1.1 2004/07/09 09:30:28 nw Exp $
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

import org.astrogrid.jes.jobscheduler.impl.scripting.WorkflowInterpreterFactory.JarPaths;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.StringTokenizer;


/** implementation of jar paths configuration for development - points to classes in buiuld tree */
public class DevelopmentJarPaths implements WorkflowInterpreterFactory.JarPaths {
    private static final Log log = LogFactory.getLog(DevelopmentJarPaths.class);
    private static final String SRCDIR  = "target" + File.separatorChar + "classes"; // maven-specific
    public DevelopmentJarPaths() {
        String jesSrcPath = null;         
        String classpath = System.getProperty("java.class.path");
        DevelopmentJarPaths.log.debug("Classpath is " + classpath);
        StringTokenizer tok = new StringTokenizer(classpath,File.pathSeparator);
        while (tok.hasMoreElements()) {
            String location = tok.nextToken();
            if (location.indexOf(JarPaths.LIBRARY_JAR) != -1) {
                libJarPath= location;
            }
            if (location.indexOf(JarPaths.JES_JAR) != -1) {
                jesJarPath = location;
            }
            if (location.indexOf(DevelopmentJarPaths.SRCDIR) != -1) {
                jesSrcPath = location;
            }
    }
    if (jesJarPath == null) {
        jesJarPath = jesSrcPath;
    }
    DevelopmentJarPaths.log.info("LIbrary jar set to " + libJarPath);
    DevelopmentJarPaths.log.info("Jes Jar set to" + jesJarPath);            
    }        
    protected String jesJarPath;
    protected String libJarPath;

    public String getLibraryJarPath() {
        return libJarPath;
    }

    public String getJesJarPath() {
        return jesJarPath;
    }

}

/* 
$Log: DevelopmentJarPaths.java,v $
Revision 1.1  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/