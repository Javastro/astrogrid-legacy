/*$Id: BootloaderTest.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import org.astrogrid.acr.builtin.Module;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Test the bootloader.
 * system test really - tests that a viable workbench can be created (i.e. no missing deps, etc.)
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public class BootloaderTest extends TestCase {

    
    
    public void testBootloader() throws Exception {
        DefaultModule system = (DefaultModule)ACRTestSetup.pico.getACR().getModule("system");        
        assertNotNull(system);      
        assertNotNull(system.getDescriptor());
        Iterator i = system.componentIterator(); 
        assertNotNull(i);
        assertTrue(i.hasNext());
        while (i.hasNext()) {
            assertNotNull(i.next());           
        }
        
    }
    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(BootloaderTest.class));
    }
    

    
}


/* 
$Log: BootloaderTest.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.4  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:17  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/03/29 16:10:59  nw
integration with the portal

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/