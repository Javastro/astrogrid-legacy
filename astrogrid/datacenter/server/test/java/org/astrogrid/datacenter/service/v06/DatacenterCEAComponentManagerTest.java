/*$Id: DatacenterCEAComponentManagerTest.java,v 1.1 2004/07/13 17:11:32 nw Exp $
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

/** Test that validates the datacenter component manager - checks that all required components are registered, etc.
 * extends existing test - as requirements are the same for any implementation of the component manager.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterCEAComponentManagerTest extends JavaClassCEAComponentManagerTest {
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
Revision 1.1  2004/07/13 17:11:32  nw
first draft of an itn06 CEA implementation for datacenter
 
*/