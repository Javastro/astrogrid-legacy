/*$Id: JesSelfTest.java,v 1.1 2004/04/21 10:57:46 nw Exp $
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

import net.sourceforge.jwebunit.WebTestCase;

/**
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
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        getTestContext().setBaseUrl("http://localhost:8080/astrogrid-jes-SNAPSHOT/");
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
Revision 1.1  2004/04/21 10:57:46  nw
added jes installation test
 
*/