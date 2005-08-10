/*$Id: AppsCEAComponentManagerTest.java,v 1.2 2005/08/10 17:45:10 clq2 Exp $
 * Created on 21-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.apps;

import org.astrogrid.applications.apps.AppsCEAComponentManager;
import org.astrogrid.applications.component.AbstractComponentManagerTestCase;
import org.astrogrid.applications.component.CEAComponentManager;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Jul-2005
 *@todo @TODO add tests specific to Apps CEA component manager - at the moment is 
 *just relying on tests in base class.
 */
public class AppsCEAComponentManagerTest extends AbstractComponentManagerTestCase {

    /** Construct a new AppsCEAComponentManagerTest
     * 
     */
    public AppsCEAComponentManagerTest() {
        super();
    }

    /** Construct a new AppsCEAComponentManagerTest
     * @param arg0
     */
    public AppsCEAComponentManagerTest(String arg0) {
        super(arg0);
    }

    /**
     * @see org.astrogrid.applications.component.AbstractComponentManagerTestCase#setupConfigComponentManager()
     */
    protected void setupConfigComponentManager() {
        basicConfig();
    }

    /**
     * @see org.astrogrid.applications.component.AbstractComponentManagerTestCase#createManager()
     */
    protected CEAComponentManager createManager() {
        return new AppsCEAComponentManager();
    }

}


/* 
$Log: AppsCEAComponentManagerTest.java,v $
Revision 1.2  2005/08/10 17:45:10  clq2
cea-server-nww-improve-tests

Revision 1.1.2.1  2005/07/21 18:12:38  nw
fixed up tests - got all passing, improved coverage a little.
still could do with testing the java apps.
 
*/