/*$Id: ModuleRegistry.java,v 1.2 2005/04/27 13:42:41 clq2 Exp $
 * Created on 15-Mar-2005
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

/** A registry of modules.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public interface ModuleRegistry {
    
    /** retreive a module by name. will return the module, or null */
    public Module getModule(String name);
    
    /** iterate over all modules in the registry */
    public Iterator moduleIterator();
    
    /** add a new module listener. 
     * twist here - on registering a new listener, any modules current in the registry are passed to the
     * listener - to enable it to 'get up to date' with what it's missed so far.
     * @param l
     */
    public void addNewModuleListener(NewModuleListener l);
    
    /** remove a module listener */
    public void removeNewModuleListener(NewModuleListener l);
}

/* 
 $Log: ModuleRegistry.java,v $
 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2.2.1  2005/04/22 15:59:26  nw
 made a star documenting desktop.

 Revision 1.2  2005/04/13 12:59:12  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.1  2005/03/18 12:09:31  nw
 got framework, builtin and system levels working.
 
 */