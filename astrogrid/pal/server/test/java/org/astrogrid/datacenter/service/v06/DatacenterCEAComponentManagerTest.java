/*$Id: DatacenterCEAComponentManagerTest.java,v 1.3 2005/03/08 18:05:57 mch Exp $
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

import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.applications.component.JavaClassCEAComponentManagerTest;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.dataservice.service.cea.DatacenterCEAComponentManager;

/** Test that validates the datacenter component manager - checks that all required components are registered, etc.
 * extends existing test - as requirements are the same for any implementation of the component manager.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterCEAComponentManagerTest extends JavaClassCEAComponentManagerTest {

   public void setUp() throws Exception {
      SimpleConfig.getSingleton().setProperty(VoResourceSupport.AUTHID_KEY, "org.astrogrid.test");
      SimpleConfig.getSingleton().setProperty(VoResourceSupport.RESKEY_KEY, "test_dsa");
      super.setUp();
   }
   /** Construct a new DatacenterCEAComponentManagerTest
     * @param arg0
     */
    public DatacenterCEAComponentManagerTest(String arg0) {
        super(arg0);
    }
    /**
     * @see org.astrogrid.applications.component.JavaClassCEAComponentManagerTest#createManager()
     */
    protected CEAComponentManager createManager() {
        return new DatacenterCEAComponentManager();
    }

}


/*
$Log: DatacenterCEAComponentManagerTest.java,v $
Revision 1.3  2005/03/08 18:05:57  mch
updating resources to v0.10

Revision 1.2  2005/02/28 18:47:05  mch
More compile fixes

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:25  mch
Initial checkin

Revision 1.3  2004/11/09 18:02:57  mch
DatacenterCEAComponentManagerTest.java

Revision 1.2  2004/11/09 17:42:22  mch
Fixes to tests after fixes for demos, incl adding closable to targetIndicators

Revision 1.1  2004/09/28 15:11:33  mch
Moved server test directory to pal

Revision 1.1  2004/07/13 17:11:32  nw
first draft of an itn06 CEA implementation for datacenter
 
*/
