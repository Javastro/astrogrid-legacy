/* $Id: ConfManagerTest.java,v 1.1 2004/03/29 17:17:55 jdt Exp $
 * Created on Mar 29, 2004 by jdt
 * Created on 19-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * Copyright (C) AstroGrid. All rights reserved.
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.integrationtest.common;

import java.io.IOException;

import org.astrogrid.config.Config;

import junit.framework.TestCase;

/**
 * Junit test
 * @author jdt
 */
public final class ConfManagerTest extends TestCase {
    /** 
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(ConfManagerTest.class);
    /**
     * Fire up text ui
     * @param args ingored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ConfManagerTest.class);
    }
    /**
     * Do we get the right object and is it a singleton?
     *
     */
    public void testGetInstance() {
        Object obj1 = ConfManager.getInstance();
        Object obj2 = ConfManager.getInstance();
        assertTrue("getinstance returns a ConfManager", obj1 instanceof ConfManager);
        assertEquals("should be a singleton", obj1, obj2);
    }
    /**
     * The best way to use this class now is just for it to load the properties
     * file for you, then use the Config this method supplies.
     *
     */
    public void testGetConfig() {
        Config config = ConfManager.getConfig();
        String test = config.getString("test.property");
        assertEquals("Can we find a simple property?", "test",test);       
    }
    /**
     * Check that existing get methods
     * haven't been broken by refactoring to use Martin's config
     *
     */
    public void testGetMySpaceEndPoint() throws IOException {
        final String url = ConfManager.getInstance().getMySpaceEndPoint();
        log.debug("myspace endpoint " + url);
        assertNotNull(url);
    }
    /**
     * Check that existing get methods
     * haven't been broken by refactoring to use Martin's config
     *
     */
    public void testGetJobControllerEndPoint() throws IOException {
        final String url = ConfManager.getInstance().getJobControllerEndPoint();
        assertNotNull(url);
    }
    /**
     * Check that existing get methods
     * haven't been broken by refactoring to use Martin's config
     *
     */
    public void testGetPortalURL() throws IOException {
        final String url = ConfManager.getInstance().getPortalURL();
        assertNotNull(url);
    }
    /**
     * Check that existing get methods
     * haven't been broken by refactoring to use Martin's config
     *
     */
    public void testGetStdDatacenterEndPoint() throws IOException {
        final String url = ConfManager.getInstance().getStdDatacenterEndPoint();
        assertNotNull(url);
    }
}


/*
 *  $Log: ConfManagerTest.java,v $
 *  Revision 1.1  2004/03/29 17:17:55  jdt
 *  Refactored ConfManager to use the astrogrid config class
 *
 */