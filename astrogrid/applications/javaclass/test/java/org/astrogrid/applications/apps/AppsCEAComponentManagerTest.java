/*$Id: AppsCEAComponentManagerTest.java,v 1.2 2007/02/19 16:20:24 gtr Exp $
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
public class AppsCEAComponentManagerTest 
    extends AbstractComponentManagerTestCase {

    /**
   * Construct a new AppsCEAComponentManagerTest
   */
    public AppsCEAComponentManagerTest() {
        super();
    }

    /**
   * Construct a new AppsCEAComponentManagerTest
   * 
   * @param arg0
   */
    public AppsCEAComponentManagerTest(String arg0) {
        super(arg0);
    }
    /**
     * @see org.astrogrid.applications.component.AbstractComponentManagerTestCase#createManager()
     */
    protected CEAComponentManager createManager() {
        return new AppsCEAComponentManager();
    }
    
    protected void configureManager() {
      // Nothing to do here.
    }

}


/* 
$Log: AppsCEAComponentManagerTest.java,v $
Revision 1.2  2007/02/19 16:20:24  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:29:18  gtr
no message

Revision 1.5  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.3  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.2.20.1  2006/01/31 21:39:07  gtr
Refactored. I have altered the configuration code slightly so that the JUnit tests can impose a Configuration instance to configure the tests. I have also fixed up almost all the bad tests for commandline and http.

Revision 1.2  2005/08/10 17:45:10  clq2
cea-server-nww-improve-tests

Revision 1.1.2.1  2005/07/21 18:12:38  nw
fixed up tests - got all passing, improved coverage a little.
still could do with testing the java apps.
 
*/