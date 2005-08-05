/*$Id: Module.java,v 1.5 2005/08/05 11:46:55 nw Exp $
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

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.desktop.framework.DefaultModule;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import java.util.Iterator;

/** A logical container for a set of related services.
 * 
 * @see DefaultModule
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public interface Module {

    
    /** access a component by name  - will reutrn the component, or null 
     * @throws ACRException */
    public Object getComponent(String componentName) throws ACRException, NotFoundException;
    
    
    /** iterate through the components in the container */
    public Iterator componentIterator();


    
    
}


/* 
$Log: Module.java,v $
Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/05/12 15:59:09  clq2
nww 1111 again

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