/*$Id: ModuleRegistry.java,v 1.2 2005/04/13 12:59:12 nw Exp $
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

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public interface ModuleRegistry {
    
    public Module getModule(String name);
    
    public Iterator moduleIterator();
    
    /** add a new module listener. 
     * twist here - on registering a new listener, any modules current in the registry are passed to the
     * listener - to enable it to 'get up to date' with what it's missed so far.
     * @param l
     */
    public void addNewModuleListener(NewModuleListener l);
    
    public void removeNewModuleListener(NewModuleListener l);
}

/* 
 $Log: ModuleRegistry.java,v $
 Revision 1.2  2005/04/13 12:59:12  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.1  2005/03/18 12:09:31  nw
 got framework, builtin and system levels working.
 
 */