/*$Id: BootloaderTest.java,v 1.3 2005/04/27 13:42:41 clq2 Exp $
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
import org.astrogrid.desktop.framework.descriptors.DescriptorParser;
import org.astrogrid.desktop.framework.descriptors.DigesterDescriptorParser;

import junit.framework.TestCase;

/** Test the bootloader.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public class BootloaderTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.reg = new DefaultModuleRegistry();
        this.parser = new DigesterDescriptorParser();
        this.bl = new Bootloader(reg,parser);
    }
    
    protected DefaultModuleRegistry reg;
    protected DescriptorParser parser;

    protected Bootloader bl;
    
    public void dontTestBootloader() throws Exception {
        bl.start();
        Module system = reg.getModule("system");
        assertNotNull(system);
        
        
    }
    
}


/* 
$Log: BootloaderTest.java,v $
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