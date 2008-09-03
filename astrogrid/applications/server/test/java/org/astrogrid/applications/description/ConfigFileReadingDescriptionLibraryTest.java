/*
 * $Id: ConfigFileReadingDescriptionLibraryTest.java,v 1.2 2008/09/03 14:19:02 pah Exp $
 * 
 * Created on 26 Aug 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import static org.junit.Assert.*;

import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.contracts.CEAConfiguration;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigFileReadingDescriptionLibraryTest {
    
    private static ConfigFileReadingDescriptionLibrary lib;

    @BeforeClass
    static public void beforeClass() throws Exception{
	CEAConfiguration conf = new MockNonSpringConfiguredConfig();
        ProtocolLibrary protocolLib = new DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
	@SuppressWarnings("unused") // this is set up on purpose with test fixtures.
	InternalCeaComponentFactory internal = new InternalCeaComponentFactory(protocolLib, new InMemoryIdGen(), new TestAuthorityResolver());
	
	lib = new ConfigFileReadingDescriptionLibrary(conf);
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetApplicationNames() {
	String[] names = lib.getApplicationNames();
	assertNotNull(names);
	assertTrue("there should be at least one application defined", names.length > 0);
    }

}


/*
 * $Log: ConfigFileReadingDescriptionLibraryTest.java,v $
 * Revision 1.2  2008/09/03 14:19:02  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/09/03 12:22:54  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1.2.1  2008/08/29 07:28:28  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 */
