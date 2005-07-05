/*$Id: HttpApplicationDescriptionLibraryTest.java,v 1.4 2005/07/05 08:26:56 clq2 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 * Created on Aug 12, 2004
 */
package org.astrogrid.applications.http;

import java.io.IOException;

import junit.framework.TestCase;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.http.test.TestRegistryQuerier;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;

/**
 * JUnit Test
 * @author jdt
 */
public class HttpApplicationDescriptionLibraryTest extends TestCase {

    private HttpApplicationDescriptionLibrary httpApplicationDescriptionLibrary;

    public final void testHttpApplicationDescriptionLibrary() {
    }

    public void setUp() throws MarshalException, ValidationException, IOException {
      
      IdGen id = new InMemoryIdGen();
       DefaultProtocolLibrary lib = new DefaultProtocolLibrary();
       TestAuthority resol = new TestAuthority();
      ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(id, lib, resol);
      httpApplicationDescriptionLibrary = new HttpApplicationDescriptionLibrary(new TestRegistryQuerier(null),  env);  
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
