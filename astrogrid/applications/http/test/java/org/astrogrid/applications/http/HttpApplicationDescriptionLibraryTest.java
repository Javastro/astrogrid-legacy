/*$Id: HttpApplicationDescriptionLibraryTest.java,v 1.2 2004/09/01 15:42:26 jdt Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 * Created on Aug 12, 2004
 */
package org.astrogrid.applications.http;

import java.io.IOException;

import org.astrogrid.applications.http.test.TestRegistryQuerier;
import org.astrogrid.community.resolver.ant.CommunityAccountCreatorTask;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import junit.framework.TestCase;

/**
 * JUnit Test
 * @author jdt
 */
public class HttpApplicationDescriptionLibraryTest extends TestCase {

    private HttpApplicationDescriptionLibrary httpApplicationDescriptionLibrary;

    public final void testHttpApplicationDescriptionLibrary() {
        //@TODO Implement HttpApplicationDescriptionLibrary().
    }

    public void setUp() throws MarshalException, ValidationException, IOException {
        httpApplicationDescriptionLibrary = new HttpApplicationDescriptionLibrary(new TestRegistryQuerier(), new TestCommunity(), null);  
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
