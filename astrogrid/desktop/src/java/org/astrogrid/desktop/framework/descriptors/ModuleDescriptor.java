/*$Id: ModuleDescriptor.java,v 1.3 2005/04/27 13:42:41 clq2 Exp $
 * Created on 10-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework.descriptors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** Representation of the descriptor for a module.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class ModuleDescriptor  extends Descriptor {

    /** Construct a new ModuleDescriptor
     * 
     */
    public ModuleDescriptor() {
        super();
    }

    /** components in this module */
    protected final Map components = new HashMap();
  /** retreive a named component descriptor */
   public ComponentDescriptor getComponent(String name) {
       return (ComponentDescriptor)components.get(name);
   }
   
   public void addComponent(ComponentDescriptor desc) {
       components.put(desc.getName(),desc);
   }
   
   public Iterator componentIterator() {
       return components.values().iterator();
   }   
    

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ModuleDescriptor:");
        buffer.append(" components: ");
        buffer.append(components);
        buffer.append(super.toString());
        buffer.append("]");
        return buffer.toString();
    }

}


/* 
$Log: ModuleDescriptor.java,v $
Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:13  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/