/* $Id: ConfFileLocatedTest.java,v 1.1 2003/12/28 12:59:04 jdt Exp $
 * Created on 28-Dec-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.mySpace.webapp;

import org.astrogrid.AstroGridException;
import org.astrogrid.mySpace.mySpaceManager.MMC;
import org.astrogrid.mySpace.mySpaceServer.MSC;

import junit.framework.TestCase;

/**
 * This test ensures that MySpace can find its configuration file
 * @author john taylor
 * @TODO do we require tests to located the template and .properties files?
 *
 */
public class ConfFileLocatedTest extends TestCase {
    /**
     * Constructor for ConfFileLocatedTest.
     * @param arg0 test name
     */
    public ConfFileLocatedTest(String arg0) {
        super(arg0);
    }
    /**
     * Fire up the text ui.
     * @param args ignored
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ConfFileLocatedTest.class);
    }
    
    /** 
     * There's one file for MySpaceManager
     */
    public void testGotMySpaceManagerConfig() throws AstroGridException {
			MMC.getInstance().checkPropertiesLoaded();
    }
    
	/** 
	 * There's one file for MySpaceServer
	 */
	public void testGotMySpaceServerConfig() throws AstroGridException {
			MSC.getInstance().checkPropertiesLoaded();
	}
}
