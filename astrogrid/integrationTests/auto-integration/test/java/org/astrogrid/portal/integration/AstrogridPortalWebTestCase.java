/* $Id: AstrogridPortalWebTestCase.java,v 1.1 2004/06/07 14:39:36 jdt Exp $
 * Created on Jun 7, 2004 by jdt@roe.ac.uk
 * The auto-integration project
 * Copyright (c) Astrigrid 2004.  All rights reserved. 
 *
 */
package org.astrogrid.portal.integration;

import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

import net.sourceforge.jwebunit.WebTestCase;

/**
 * Factor out the common bits to all the test pages
 * 
 * @author jdt
 */
public abstract class AstrogridPortalWebTestCase extends WebTestCase {
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(
            AstrogridPortalWebTestCase.class);
    
    /**
     * Constructor
     * @param arg0 test name
     */
    public AstrogridPortalWebTestCase(String arg0) {
        super(arg0);
    }
    /**
     * Constructor
     * 
     */
    public AstrogridPortalWebTestCase() {
        super();
    }
    /**
     * Configuration holding endpoints of tests
     */
    private static Config conf = SimpleConfig.getSingleton();
    /**
     * Get the url of the website and 
     * set it for the remaining tests
     * @throws Exception most likely to throw a RunTime exception if it can't find the property which locates the website
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        final String url = conf.getString("org.astrogrid.portal.site");
        assert url!=null;
        log.debug("Setting portal URL to " + url);
        // Set up for jwebtest
        getTestContext().setBaseUrl(url);
    }
}


/*
 *  $Log: AstrogridPortalWebTestCase.java,v $
 *  Revision 1.1  2004/06/07 14:39:36  jdt
 *  Refactored out some common stuff
 *
 */