/*$Id: HttpApplicationDescriptionLibraryTest.java,v 1.5 2008/09/03 14:18:33 pah Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 * Created on Aug 12, 2004
 */
package org.astrogrid.applications.http;

import java.io.IOException;

import javax.xml.bind.MarshalException;
import javax.xml.bind.ValidationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import junit.framework.TestCase;

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.http.test.TestRegistryQuerier;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;

/**
 * JUnit Test
 * @author jdt
 */
public class HttpApplicationDescriptionLibraryTest extends TestCase {

    private HttpApplicationDescriptionLibrary httpApplicationDescriptionLibrary;

    public final void testHttpApplicationDescriptionLibrary() {
    }

    public void setUp() throws IOException, MarshalException, ValidationException, MetadataException, XMLStreamException, FactoryConfigurationError {
      
      IdGen id = new InMemoryIdGen();
       DefaultProtocolLibrary lib = new DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
       AppAuthorityIDResolver resol = new TestAuthorityResolver();
      ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(id, lib, resol);
      Configuration conf = new MockNonSpringConfiguredConfig();
    httpApplicationDescriptionLibrary = new HttpApplicationDescriptionLibrary(new TestRegistryQuerier(null),  env, conf );  
    }
    /*
     * Class under test for String getDescription()
     */
    public final void testGetDescription() throws IOException {
//      just get the clover coverage up
        assertNotNull(httpApplicationDescriptionLibrary.getDescription());
    }

    /*
     * Class under test for String getName()
     */
    public final void testGetName() {
        assertEquals("HttpApplication Library", httpApplicationDescriptionLibrary.getName());
    }

    /*
     * Class under test for String toString()
     */
    public final void testToString() {
        //just get the clover coverage up
        assertNotNull(httpApplicationDescriptionLibrary.toString());
    }

}
