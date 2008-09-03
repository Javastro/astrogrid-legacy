/*
 * $Id: IvornProtocolTest.java,v 1.2 2008/09/03 14:19:00 pah Exp $
 * 
 * Created on 2 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.parameter.protocol;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 2 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class IvornProtocolTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test method for {@link org.astrogrid.applications.parameter.protocol.IvornProtocol#IvornProtocol()}.
     */
    @Test
    public void testIvornProtocol() {
	IvornProtocol prot = new IvornProtocol();
    }

    /**
     * Test method for {@link org.astrogrid.applications.parameter.protocol.IvornProtocol#getProtocolName()}.
     */
    @Test
    public void testGetProtocolName() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.astrogrid.applications.parameter.protocol.IvornProtocol#createIndirectValue(java.net.URI)}.
     */
    @Test
    public void testCreateIndirectValue() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.astrogrid.applications.parameter.protocol.IvornProtocol#getName()}.
     */
    @Test
    public void testGetName() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.astrogrid.applications.parameter.protocol.IvornProtocol#getDescription()}.
     */
    @Test
    public void testGetDescription() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.astrogrid.applications.parameter.protocol.IvornProtocol#getInstallationTest()}.
     */
    @Test
    public void testGetInstallationTest() {
	fail("Not yet implemented");
    }

}


/*
 * $Log: IvornProtocolTest.java,v $
 * Revision 1.2  2008/09/03 14:19:00  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/04/04 15:46:08  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
