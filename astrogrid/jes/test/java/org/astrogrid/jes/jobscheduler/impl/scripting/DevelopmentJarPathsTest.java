/*$Id: DevelopmentJarPathsTest.java,v 1.1 2004/07/09 09:32:12 nw Exp $
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

import java.io.File;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jul-2004
 *
 */
public class DevelopmentJarPathsTest extends TestCase {
    /**
     * Constructor for DevelopmentJarPathsTest.
     * @param arg0
     */
    public DevelopmentJarPathsTest(String arg0) {
        super(arg0);
    }
    
    public void  testPaths() {
        DevelopmentJarPaths paths = new DevelopmentJarPaths();
        assertNotNull(paths.getJesJarPath());
        assertTrue((new File(paths.getJesJarPath())).exists());
        
        assertNotNull(paths.getLibraryJarPath());
        assertTrue((new File(paths.getLibraryJarPath())).exists());
    }
}


/* 
$Log: DevelopmentJarPathsTest.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/