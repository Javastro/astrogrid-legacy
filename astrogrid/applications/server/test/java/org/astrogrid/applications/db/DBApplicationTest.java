/*
 * $Id: DBApplicationTest.java,v 1.2 2011/09/02 21:55:54 pah Exp $
 * 
 * Created on 10 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.db;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.BaseApplicationDescriptionFactory;
import org.astrogrid.applications.description.ConfigFileReadingDescriptionLibrary;
import org.astrogrid.applications.description.StandardApplicationDescriptionFactory;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.test.MockMonitor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/DBspringTest.xml")
public class DBApplicationTest {

    private static MockMonitor monitor;
    private static MockNonSpringConfiguredConfig conf;
    @Autowired
    private  DataSource ds;
    private static BaseApplicationDescriptionFactory fac;
    private static Tool tool;
    private ConfigFileReadingDescriptionLibrary lib;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        IdGen idgen = new InMemoryIdGen();
        DefaultProtocolLibrary protocolLib = new DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
        monitor = new MockMonitor();
        AppAuthorityIDResolver aresolver = new TestAuthorityResolver();
        new InternalCeaComponentFactory(protocolLib, idgen, aresolver);
        conf = new MockNonSpringConfiguredConfig();
        fac = new StandardApplicationDescriptionFactory(conf);
        tool =  CEAJAXBUtils.unmarshall(DBApplicationTest.class.getResourceAsStream("/testDBTool"), Tool.class);
        assertNotNull(tool);
   }

    @Before
    public void setUp() throws Exception {
        lib = new ConfigFileReadingDescriptionLibrary(DBApplicationTest.class.getResource("/TAPConfig.xml"), fac );
        assertNotNull(lib);
    }

    @Test
    public void testRun() throws Exception {
        assertEquals("number of applications", 1 , lib.getApplicationNames().length);
        assertEquals("application name","ivo://org.testit/TAP", lib.getApplicationNames()[0]);
        ApplicationDescription desc = lib.getDescription("ivo://org.testit/TAP");
    }

}


/*
 * $Log: DBApplicationTest.java,v $
 * Revision 1.2  2011/09/02 21:55:54  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.2  2011/09/02 19:40:40  pah
 * change setup of dynamic description library
 *
 * Revision 1.1.2.1  2009/07/15 10:01:00  pah
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2907
 * NEW - bug 2851: generalized DAL applications
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2851
 * NEW - bug 2931: upgrades for 2009.2
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2931
 * NEW - bug 2920: upgrade to uws 1.0
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2920
 *
 */
