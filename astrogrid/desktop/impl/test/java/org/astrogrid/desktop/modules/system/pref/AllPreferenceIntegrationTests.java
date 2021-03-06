/*$Id: AllPreferenceIntegrationTests.java,v 1.1 2007/04/18 15:47:09 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.pref;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.modules.system.pref.PreferenceEditorServletIntegrationTest;
import org.astrogrid.desktop.modules.system.pref.PreferencesIntegrationTest;

/** Tests the system components.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class AllPreferenceIntegrationTests {

    
/** tests the system components */
    public static Test suite() {
        TestSuite suite = new TestSuite("Integration tests for System Preferences Module");

        suite.addTest(PreferencesIntegrationTest.suite());

        suite.addTest(PreferenceEditorServletIntegrationTest.suite());

        return new ARTestSetup(suite);
    }
}


/* 
$Log: AllPreferenceIntegrationTests.java,v $
Revision 1.1  2007/04/18 15:47:09  nw
tidied up voexplorer, removed front pane.

Revision 1.5  2007/01/29 10:42:48  nw
tidied.

Revision 1.4  2007/01/23 20:07:33  nw
fixes to use subclass of finder, and to work in a hub setting.

Revision 1.3  2007/01/23 11:53:37  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.2  2007/01/10 14:57:35  nw
tests for preferences.

Revision 1.1  2007/01/09 16:12:20  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/