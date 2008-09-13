/*$Id: RegistryUploaderTest.java,v 1.4 2008/09/13 09:51:02 pah Exp $
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

import org.astrogrid.applications.description.base.DummyVODescriptionProvider;
import org.astrogrid.applications.manager.MetadataService;
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
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MetadataService provider = new DummyVODescriptionProvider("test.org","testapp");
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
    public void testUpload() throws Exception {
       
        //null means that it will use the locator.   
        uploader.write(null);
        //the service should not attempt to automatically register itself - this should be a manual operation now 
        assertEquals(1,service.callCount);
    }
 
}

/* 
$Log: RegistryUploaderTest.java,v $
Revision 1.4  2008/09/13 09:51:02  pah
code cleanup

Revision 1.3  2005/07/05 08:27:00  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.2.172.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.2.158.2  2005/06/02 14:57:28  pah
merge the ProvidesVODescription interface into the MetadataService interface

Revision 1.2.158.1  2005/05/31 12:58:26  pah
moved to v10 schema interpretation - this means that the authorityID is read directly with the applicaion "name"

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/