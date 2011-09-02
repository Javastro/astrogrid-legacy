/*
 * $Id: DefaultParameterAdapterTest.java,v 1.2 2011/09/02 21:55:50 pah Exp $
 * 
 * Created on 11 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.parameter;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;

import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.base.BaseParameterDefinition;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.security.SecurityGuard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultParameterAdapterTest {

    private static ApplicationEnvironment env;
    private static URL extTextURL;
    private static URL extBinURL;
    private static URL extTextOutURL;
    private static URL extBinOutURL;
    private static // this is set up on purpose with test fixtures.
    InternalCeaComponentFactory internal;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ProtocolLibrary protocolLib = new DefaultProtocolLibrary(
                new Protocol[] { new FileProtocol() });
        internal = new InternalCeaComponentFactory(
                protocolLib, new InMemoryIdGen(), new TestAuthorityResolver());
        env = new ApplicationEnvironment("testjob", new SecurityGuard(), internal.getIdGenerator(), new MockNonSpringConfiguredConfig());
        File file = File.createTempFile("partest", "txt");
        Writer w = new FileWriter(file);
        w.write("this is a test");
        w.close();
        extTextURL = file.toURL();
        file = File.createTempFile("partest", "dat");
        FileOutputStream os = new FileOutputStream(file);
        os.write(new byte[] { 1, 2, 3, 4, 5, 6 });
        os.close();
        extBinURL = file.toURL();
        
        file = File.createTempFile("partestout", "txt");
        extTextOutURL = file.toURL();
        file = File.createTempFile("partestout", "bin");
        extBinOutURL = file.toURL();
        

    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testProcessDirDir() throws Exception {


        BaseParameterDefinition desc = new BaseParameterDefinition();
        desc.setId("text");
        desc.setType(ParameterTypes.TEXT);

        ParameterValue val = new ParameterValue();
        val.setId("text");
        val.setIndirect(false);
        val.setValue("a small piece of text");

        ParameterAdapter ad = new DefaultParameterAdapter(val, desc, ParameterDirection.INPUT, env);

        assertNotNull(ad);
        ad.getInternalValue().setValue("yes");
        ad.writeBack();
        assertEquals("yes", val.getValue());

    }

    @Test
    public void testExtDir() throws Exception {
        BaseParameterDefinition desc = new BaseParameterDefinition();
        desc.setId("text");
        desc.setType(ParameterTypes.TEXT);

        ParameterValue val = new ParameterValue();
        val.setId("text");
        val.setIndirect(true);
        val.setValue(extTextURL.toString());

        ExternalValue ext = internal.getProtocolLibrary().getExternalValue(val, env.getSecGuard()) ;
        ParameterAdapter ad = new DefaultParameterAdapter(val, desc, ParameterDirection.INPUT, env);

        assertNotNull(ad);
        String theval =  ad.getInternalValue().asString();
        
        assertEquals("this is a test", theval);
        
        ad.writeBack();

    }

}

/*
 * $Log: DefaultParameterAdapterTest.java,v $
 * Revision 1.2  2011/09/02 21:55:50  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.2  2009/07/16 19:46:35  pah
 * ASSIGNED - bug 2950: rework parameterAdapter
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2950
 *
 * Revision 1.1.2.1  2009/07/15 09:58:05  pah
 * redesign of parameterAdapters
 *
 * - there was never a parameterAdapter test before!
 *
 */
