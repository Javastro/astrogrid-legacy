/*$Id: ACRInternal.java,v 1.3 2006/06/02 00:16:15 nw Exp $
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

import java.util.Iterator;
import java.util.Map;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.ModuleDescriptor;


/** (internal interface) Extension to the {@link ACR} interface, that allows new modules to be added to the registry.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public interface ACRInternal extends ACR {

    /** get a map of  descriptors for all modules 
     * 
     * @return a map of <tt>module-name</tt> -- <tt>module descriptor</tt>
     * @see ModuleDescriptor
     */
    public Map getDescriptors();
    /** get the named module
     * @param name
     * @return
     */
    public Module getModule(String name);
    /** iterate through all available modules. 
     * @return
     */
    public Iterator moduleIterator();
}


/* 
$Log: ACRInternal.java,v $
Revision 1.3  2006/06/02 00:16:15  nw
Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.

Revision 1.2  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.2.60.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

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