/*$Id: DefaultMetadataServiceTest.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 26-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager;

import org.astrogrid.applications.component.ProvidesVODescription;
import org.astrogrid.applications.description.base.DummyVODescriptionProvider;
import org.astrogrid.registry.beans.resource.VODescription;

import java.io.StringReader;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 *
 */
public class DefaultMetadataServiceTest extends TestCase {
    public static final String RES_KEY = "test-entry";
    public static final String AUTH_ID = "org.astrogrid.test";
    public static final String RES_NAME = AUTH_ID + "/" + RES_KEY;
    /**
     * Constructor for BaseCEAServerDescriptionTest.
     * @param arg0
     */
    public DefaultMetadataServiceTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        serverDesc = new DefaultMetadataService(provider);
    }
    protected DefaultMetadataService serverDesc;
    protected ProvidesVODescription provider = new DummyVODescriptionProvider(AUTH_ID, RES_KEY);
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testReturnRegistryEntry() throws Exception{
        String regString= serverDesc.returnRegistryEntry();
        assertNotNull(regString);
        // check we can parse it back into vodescription.
        StringReader reader = new StringReader(regString);
        VODescription desc = VODescription.unmarshalVODescription(reader);
        assertNotNull(desc);
    }
    


}


/* 
$Log: DefaultMetadataServiceTest.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:11  nw
checked in early, broken version - but it builds and tests (fail)
 
*/