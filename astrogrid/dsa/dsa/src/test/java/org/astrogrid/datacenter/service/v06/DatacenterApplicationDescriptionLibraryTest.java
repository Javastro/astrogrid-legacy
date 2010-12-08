/*$Id: DatacenterApplicationDescriptionLibraryTest.java,v 1.4 2010/12/08 12:46:35 gtr Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.service.v06;

import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.datacenter.DsaUnitTest;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.cea.CeaQueuedExecutor;
import org.astrogrid.dataservice.service.cea.DatacenterApplicationDescriptionLibrary;

/** Test the application description library - not much to do here..
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationDescriptionLibraryTest extends DsaUnitTest {
    
    /*
     * @see TestCase#setUp()
     */
  @Override
    public void setUp() throws Exception {

        super.setUp();

        dataServer = new DataServer();
        env = new ApplicationDescriptionEnvironment(
                  new InMemoryIdGen(),
                  new DefaultProtocolLibrary(),
                  new AppAuthorityIDResolver() {
                           public String getAuthorityID() {
                              return "org.astrogrid.test";
                           }
                     });
          
        /*
        DatacenterApplicationDescriptionLibrary.DatacenterMetadata md = new DatacenterApplicationDescriptionLibrary.DatacenterMetadata() {
            public String getName() {
                return "testdsa";
            }
        };
        */
        lib = new DatacenterApplicationDescriptionLibrary(/*md,*/dataServer,env,new CeaQueuedExecutor());
    }
    protected ApplicationDescriptionLibrary lib;
    protected DataServer dataServer;
    protected ApplicationDescriptionEnvironment env;


    public void testContents() throws ApplicationDescriptionNotFoundException {
        String[] names = lib.getApplicationNames();
        for (int i = 0; i < names.length; i++) {
          System.out.println(names[i]);
        }
        // We expect astrogrid.org/test-dsa-catalog/ceaApplication,
        // org.astrogrid.unregistered/default
        // astrogrid.org/test-dsa-catalog/CatName_SampleStarsCat/ceaApplication
        assertEquals(3,names.length);
        ApplicationDescription appDesc = lib.getDescription(names[0]);
        assertNotNull(appDesc);
    }
}