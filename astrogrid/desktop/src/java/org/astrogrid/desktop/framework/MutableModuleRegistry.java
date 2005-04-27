/*$Id: MutableModuleRegistry.java,v 1.3 2005/04/27 13:42:41 clq2 Exp $
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

import org.astrogrid.acr.builtin.ModuleRegistry;
import org.astrogrid.acr.builtin.NewModuleEvent;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

/** Extension to the {@link ModuleRegistry} interface, that allows new modules to be added to the registry.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public interface MutableModuleRegistry extends ModuleRegistry {
    
    /** register (and instantiate) a new module from a descirptor
     * causes a {@link NewModuleEvent} to be fired to any listeners. */
    public void register(ModuleDescriptor description);

}


/* 
$Log: MutableModuleRegistry.java,v $
Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/