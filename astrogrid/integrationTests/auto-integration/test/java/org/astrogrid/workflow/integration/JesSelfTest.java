/*$Id: JesSelfTest.java,v 1.3 2004/08/27 13:25:40 nw Exp $
 * Created on 21-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;

import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

import net.sourceforge.jwebunit.WebTestCase;

/** Call the self-tests of the jes webapp - check that no errors occur there.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public class JesSelfTest extends WebTestCase {
    /** Construct a new JesSelfTest
     * 
     */
    public JesSelfTest() {
        super();
    }
    
    public final static String JES_BASE_URL = "org.astrogrid.jes.baseurl";
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        String url = SimpleConfig.getProperty(JES_BASE_URL);
        getTestContext().setBaseUrl(url);
    }
    
    public void testJesWar() throws Exception {
        beginAt("/");
        assertTextNotPresent("error");
        assertTextNotPresent("exception");
    }

    public void testJesSelfTests() throws Exception {
        beginAt("/TestServlet?suite=org.astrogrid.jes.component.ComponentManagerFactory");
        assertTextNotPresent("error");
        assertTextNotPresent("exception");
    }

}


/* 
$Log: JesSelfTest.java,v $
Revision 1.3  2004/08/27 13:25:40  nw
removed hardcoded endpoint.

Revision 1.2  2004/04/23 00:27:56  nw
reorganized end-to-end tests. added test to verify flows are executed in parallel

Revision 1.1  2004/04/21 10:57:46  nw
added jes installation test
 
*/