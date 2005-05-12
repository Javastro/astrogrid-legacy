/*$Id: Module.java,v 1.3 2005/05/12 15:37:42 clq2 Exp $
 * Created on 10-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.builtin;

import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import org.picocontainer.PicoContainer;

import java.util.Iterator;

/** A container for a set of services. The result of instantiating a {@link org.astrogrid.desktop.framework.descriptors.ModuleDescriptor}
 * 
 * Module extends PicoContainer, which is a generic library for component containers.
 * @see DefaultModule
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public interface Module extends PicoContainer{

    /** return the module descriptor that was processed to create this module */
    public ModuleDescriptor getDescriptor();
    
    /** access a component by name  - will reutrn the component, or null */
    public Object getComponent(String componentName);
    
    //public ComponentDescriptor getComponentDescriptor(Object component);
    
    /** iterate through the components in the container */
    public Iterator componentIterator();

    /** @todo check whether this is used. think it just produces formatted debug info.
     * @return
     */
    public String getChain();
    
    
}


/* 
$Log: Module.java,v $
Revision 1.3  2005/05/12 15:37:42  clq2
nww 1111

Revision 1.2.8.1  2005/05/11 11:55:19  nw
javadoc

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/01 19:03:10  nw
beta of job monitor

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/