/*$Id: CommandLineCEAComponentManagerTest.java,v 1.2 2004/07/01 11:07:59 nw Exp $
 * Created on 26-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.commandline;

import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.applications.component.JavaClassCEAComponentManagerTest;
import org.astrogrid.config.SimpleConfig;

import java.net.URL;

/** Test the component manager assembled for the commandline cea is valid - i.e. has all the necessary components registered.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 *
 */
public class CommandLineCEAComponentManagerTest extends JavaClassCEAComponentManagerTest {
    /**
     * Constructor for CommandLineCEAComponentManagerTest.
     * @param arg0
     */
    public CommandLineCEAComponentManagerTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
         setupConfigForCommandLineComponentManager();
        super.setUp();   
    }
    
    protected CEAComponentManager createManager() {
        return new CommandLineCEAComponentManager();
    }
    public static void setupConfigForCommandLineComponentManager() {
            URL descriptionURL = CommandLineCEAComponentManagerTest.class.getClass().getResource("/applicationDefinitions.xml");
            assertNotNull(descriptionURL);
            SimpleConfig.getSingleton().setProperty(CommandLineCEAComponentManager.DESCRIPTION_URL,descriptionURL.toString());       
    }
  
}


/* 
$Log: CommandLineCEAComponentManagerTest.java,v $
Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/06/17 09:24:18  nw
intermediate version

Revision 1.1.2.1  2004/06/14 08:57:48  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/