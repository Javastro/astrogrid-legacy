/*$Id: DatacenterApplicationDescriptionLibraryTest.java,v 1.1 2009/05/13 13:20:59 gtr Exp $
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

import junit.framework.TestCase;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.cea.CeaQueuedExecutor;
import org.astrogrid.dataservice.service.cea.DatacenterApplicationDescriptionLibrary;
import org.astrogrid.tableserver.test.SampleStarsPlugin;

/** Test the application description library - not much to do here..
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationDescriptionLibraryTest extends TestCase {
    /**
     * Constructor for DatacenterApplicationDescriptionLibraryTest.
     * @param arg0
     */
    public DatacenterApplicationDescriptionLibraryTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {

        super.setUp();

        SampleStarsPlugin.initConfig();

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
        assertEquals(2,names.length);
        ApplicationDescription appDesc = lib.getDescription(names[0]);
        assertNotNull(appDesc);
    }
}
/*
$Log: DatacenterApplicationDescriptionLibraryTest.java,v $
Revision 1.1  2009/05/13 13:20:59  gtr
*** empty log message ***

Revision 1.7  2007/09/07 09:30:50  clq2
PAL_KEA_2235

Revision 1.6.6.1  2007/09/04 08:41:37  kea
Fixing v1.0 registrations and multi-catalog CEA stuff.

Revision 1.6  2007/03/02 14:50:50  kea
Updated to match change in CEA.

Revision 1.5  2006/03/17 17:56:58  clq2
gtr_1489_cea correted version

Revision 1.3  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.2.86.1  2006/01/30 11:39:22  gtr
I corrected the use of the AppAuthorityResolver interface to match the new CEA code.

Revision 1.2  2005/02/28 18:47:05  mch
More compile fixes

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:25  mch
Initial checkin

Revision 1.1.30.1  2005/01/13 18:57:31  mch
Fixes to metadata mostly

Revision 1.1  2004/09/28 15:11:33  mch
Moved server test directory to pal

Revision 1.4  2004/09/17 01:27:06  nw
added thread management.

Revision 1.3  2004/07/27 13:48:33  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package

Revision 1.2  2004/07/20 02:15:05  nw
final implementaiton of itn06 Datacenter CEA interface

Revision 1.1  2004/07/13 17:11:32  nw
first draft of an itn06 CEA implementation for datacenter
 
*/
