/*$Id: NewModuleEvent.java,v 1.2 2005/04/13 12:59:11 nw Exp $
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

import java.util.EventObject;

/** Event object that is passed when a new module is registeed.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public class NewModuleEvent extends EventObject {

    /** Construct a new NewModuleEvent
     * @param source
     */
    public NewModuleEvent(Object source, Module newModule) {
        super(source);
        this.newModule = newModule;
    }
    
    protected final Module newModule;
    
    public Module getModule() {
        return newModule;
    }

}


/* 
$Log: NewModuleEvent.java,v $
Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/