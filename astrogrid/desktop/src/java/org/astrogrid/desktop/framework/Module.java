/*$Id: Module.java,v 1.2 2005/04/13 12:59:11 nw Exp $
 * Created on 10-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import org.picocontainer.PicoContainer;

import java.util.Iterator;

/** A containr for a set of services. Defined by a {@link ModuleDescriptor}
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public interface Module extends PicoContainer{


    public ModuleDescriptor getDescriptor();
    
    public Object getComponent(String componentName);
    
    //public ComponentDescriptor getComponentDescriptor(Object component);
    
    public Iterator componentIterator();

    /**
     * @return
     */
    public String getChain();
    
    
}


/* 
$Log: Module.java,v $
Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/01 19:03:10  nw
beta of job monitor

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/