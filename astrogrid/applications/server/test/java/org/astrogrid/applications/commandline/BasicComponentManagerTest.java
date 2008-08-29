/*
 * $Id: BasicComponentManagerTest.java,v 1.1 2008/08/29 07:28:28 pah Exp $
 * 
 * Created on 4 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import org.astrogrid.applications.test.AbstractComponentManagerTestCase;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests that the basic wiring and startup of the components is working ok. Just allows the tests in the base class to run.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 4 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"/ceaspringTest.xml"}) 
public class BasicComponentManagerTest extends AbstractComponentManagerTestCase {

 //no special tests of its own.

}


/*
 * $Log: BasicComponentManagerTest.java,v $
 * Revision 1.1  2008/08/29 07:28:28  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/04/04 15:34:52  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
