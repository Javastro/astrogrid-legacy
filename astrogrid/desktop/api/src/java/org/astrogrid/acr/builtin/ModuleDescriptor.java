/*$Id: ModuleDescriptor.java,v 1.6 2009/03/30 15:02:54 nw Exp $
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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.astrogrid.acr.system.ApiHelp;

/** Describes a module of the AstroRuntime.
 * <br />
 * Modules gather together related AR Services.
 * @see ApiHelp 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 

 *
 */
public class ModuleDescriptor  extends Descriptor {

    /** Construct a new ModuleDescriptor
     * @exclude
     */
    public ModuleDescriptor() {
        super();
    }

    /** components in this module */
    protected final Map components = new LinkedHashMap();
    	//new ListOrderedMap(); - don't think this is necessary anymore.
  /** Retrieve the description of a named component (service) */
   public ComponentDescriptor getComponent(final String serviceName) {
       return (ComponentDescriptor)components.get(serviceName);
   }
   /** list all components in this module */
   public ComponentDescriptor[] getComponents() {
	   return (ComponentDescriptor[])components.values().toArray(new ComponentDescriptor[]{});
   }
   
   /** used internally - not useful for clients 
    * @exclude */
   public void addComponent(final ComponentDescriptor desc) {
       components.put(desc.getName(),desc);
   }
   /** list all components in this module.
    * 
    * @return an iterator over {@link ComponentDescriptor} objects
    */
   public Iterator componentIterator() {
       return components.values().iterator();
   }   
    

   /** @exclude */
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
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
Revision 1.6  2009/03/30 15:02:54  nw
Added override annotations.

Revision 1.5  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.4  2007/11/12 13:36:27  pah
change parameter name to structure

Revision 1.3  2007/01/24 14:04:46  nw
updated my email address

Revision 1.2  2007/01/09 15:47:43  nw
change from HashMap to LinkedHashMap to preserve ordering.

Revision 1.1  2006/06/02 00:17:10  nw
Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.

Revision 1.3  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.2.60.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/05/12 15:59:07  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 22:48:23  nw
altered datastructure implementation, so order of insertion is preserved.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:13  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/