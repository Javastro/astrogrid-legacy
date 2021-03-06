/*
 * $Id: ConfigFileReadingDescriptionLibraryTest.java,v 1.7 2011/09/02 21:55:53 pah Exp $
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
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
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
        ProtocolLibrary protocolLib = new DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
	@SuppressWarnings("unused") // this is set up on purpose with test fixtures.
	InternalCeaComponentFactory internal = new InternalCeaComponentFactory(protocolLib, new InMemoryIdGen(), new TestAuthorityResolver());
	
   }

    @Before
    public void setUp() throws Exception {
     }

    @Test
    public void testGetApplicationNames() {
        CEAConfiguration conf = new MockNonSpringConfiguredConfig();
        lib = new ConfigFileReadingDescriptionLibrary(conf.getApplicationDescriptionUrl(), new StandardApplicationDescriptionFactory(conf));
 	String[] names = lib.getApplicationNames();
	assertNotNull(names);
	assertTrue("there should be at least one application defined", names.length > 0);
    }
    
    @Test
    public void testLoadJustApp() throws ApplicationDescriptionNotFoundException {
        CEAConfiguration conf = new MockNonSpringConfiguredConfig();
        lib = new ConfigFileReadingDescriptionLibrary(conf.getApplicationDescriptionUrl(), new StandardApplicationDescriptionFactory(conf));
 	lib.loadFileOrDirectory(ConfigFileReadingDescriptionLibraryTest.class.getResource("/TestSingleApplicationConfig.xml"));
	ApplicationDefinition desc = lib.getDescription("ivo://org.testit/testappsingle");
	assertNotNull("Should have found the singly defined application", desc);
    }
    
    @Test
    public void testIncorrectApp() {
        CEAConfiguration conf = new MockNonSpringConfiguredConfig();
        lib = new ConfigFileReadingDescriptionLibrary(conf.getApplicationDescriptionUrl(), new StandardApplicationDescriptionFactory(conf));
 	boolean retval = lib.loadFileOrDirectory(ConfigFileReadingDescriptionLibraryTest.class.getResource("/TestIncorrectApplicationDefn.xml"));
	assertTrue("application loading should have failed for incorrect application definition",!retval);
	String error = lib.getErrorMessage();
	assertNotNull(error);
        System.err.println(error);
    }
    
    @Test
    public void testLoadTAPApp() throws ApplicationDescriptionNotFoundException {
        CEAConfiguration conf = new MockNonSpringConfiguredConfig();
        lib = new ConfigFileReadingDescriptionLibrary(ConfigFileReadingDescriptionLibraryTest.class.getResource("/TAPConfig.xml"), new StandardApplicationDescriptionFactory(conf));
       
        ApplicationDefinition desc = lib.getDescription("ivo://org.testit/TAP");
        assertNotNull("Should have found the singly defined application", desc);
    }
    
   
    @Test
    public void testReadDirectory(){
        CEAConfiguration conf = new MockNonSpringConfiguredConfig();
        lib = new ConfigFileReadingDescriptionLibrary(ConfigFileReadingDescriptionLibraryTest.class.getResource("/app/conf"), new StandardApplicationDescriptionFactory(conf));
        String[] names = lib.getApplicationNames();
        assertNotNull(names);
        
        assertEquals("applications defined in directory", 2, names.length);
        
    }

}


/*
 * $Log: ConfigFileReadingDescriptionLibraryTest.java,v $
 * Revision 1.7  2011/09/02 21:55:53  pah
 * result of merging the 2931 branch
 *
 * Revision 1.6.2.2  2011/09/02 19:40:45  pah
 * change setup of dynamic description library
 *
 * Revision 1.6.2.1  2009/07/15 10:01:00  pah
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2907
 * NEW - bug 2851: generalized DAL applications
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2851
 * NEW - bug 2931: upgrades for 2009.2
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2931
 * NEW - bug 2920: upgrade to uws 1.0
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2920
 *
 * Revision 1.6  2009/02/26 12:47:04  pah
 * separate more out into cea-common for both client and server
 *
 * Revision 1.5  2008/10/09 11:48:18  pah
 * add dynamic app description library & refactor more funtionality to base class
 *
 * Revision 1.4  2008/09/18 09:13:39  pah
 * improved javadoc
 *
 * Revision 1.3  2008/09/10 23:27:17  pah
 * moved all of http CEC and most of javaclass CEC code here into common library
 *
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
