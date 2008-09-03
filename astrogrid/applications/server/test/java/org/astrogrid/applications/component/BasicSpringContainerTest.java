/*
 * $Id: BasicSpringContainerTest.java,v 1.2 2008/09/03 14:18:44 pah Exp $
 * 
 * Created on 2 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.component;


import static org.junit.Assert.*;

import java.util.Map;

import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={ "/org/astrogrid/applications/component/ceaspring.xml","/ceaspringTest.xml"}) 
public class BasicSpringContainerTest extends AbstractJUnit4SpringContextTests{

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testSpring() throws Exception {
	Map list = applicationContext.getBeansOfType(ComponentDescriptor.class);
	for (Object name : list.keySet()) {
	    System.out.print((String)name+", ");
	}
	
	String[] names = applicationContext.getBeanDefinitionNames();
	for (int i = 0; i < names.length; i++) {
	    String string = names[i];
	    System.out.print(string+", ");
	    
	}
	System.out.println("that was names");
    }
    
    public void testManager(){
	CEAComponentContainer val = CEAComponentContainer.getInstance();
	assertNotNull(val);
    }
    
    public void testTestSuite(){
	CEAComponentContainer val = CEAComponentContainer.getInstance();
	assertNotNull(val);
	junit.framework.Test s = val.getSuite();
	assertNotNull(s);
    }
    
   
}


/*
 * $Log: BasicSpringContainerTest.java,v $
 * Revision 1.2  2008/09/03 14:18:44  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.3  2008/08/29 07:28:30  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.2  2008/04/08 14:45:10  pah
 * Completed move to using spring as container for webapp - replaced picocontainer
 *
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 *
 * Revision 1.1.2.1  2008/04/04 15:46:07  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
