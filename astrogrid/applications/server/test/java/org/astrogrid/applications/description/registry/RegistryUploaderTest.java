/*$Id: RegistryUploaderTest.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 02-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description.registry;

import org.astrogrid.applications.component.ProvidesVODescription;
import org.astrogrid.applications.description.base.DummyVODescriptionProvider;
import org.astrogrid.registry.client.admin.RegistryAdminService;

import junit.framework.TestCase;

/** Test the registry uploader (with mocks).
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Jun-2004
 *
 */
public class RegistryUploaderTest extends TestCase {
    /**
     * Constructor for RegistryUploaderTest.
     * @param arg0
     */
    public RegistryUploaderTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ProvidesVODescription provider = new DummyVODescriptionProvider("test.org","testapp");
        service =  new MockRegistryAdminService();
        RegistryAdminLocator locator = new RegistryAdminLocator() {
            public RegistryAdminService getClient() throws Exception {
                return service;
            }
        };
        uploader = new RegistryUploader(provider,locator);
    }
    protected RegistryUploader uploader;
    protected MockRegistryAdminService service;
    public void testStart() {
        uploader.start();
        assertEquals(1,service.callCount);
    }
    public void testStop() {
        uploader.stop();
    }
}


/* 
$Log: RegistryUploaderTest.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/