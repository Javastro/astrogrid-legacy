/*$Id: AppsCEAComponentManager.java,v 1.4 2004/08/27 10:56:38 nw Exp $
 * Created on 11-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.apps;

import org.astrogrid.applications.component.JavaClassCEAComponentManager;
import org.astrogrid.applications.description.ApplicationDescription;

/** Extended java class component manager, that also registers custom apps in this package.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Aug-2004
 *
 */
public class AppsCEAComponentManager extends JavaClassCEAComponentManager {

    /** Construct a new AppsCEAComponentManager
     * 
     */
    public AppsCEAComponentManager() {
        super();
        registerContainerApplicationDescriptionLibrary(pico);        
        pico.registerComponentImplementation(SendMailApplicationDescription.class);
        pico.registerComponentImplementation(CatApplicationDescription.class);
    }

}


/* 
$Log: AppsCEAComponentManager.java,v $
Revision 1.4  2004/08/27 10:56:38  nw
removed container-inspecting applicationDescriptionLibrary from default setup - cea-commandline doesn't like it.

Revision 1.3  2004/08/17 15:07:25  nw
added concat application

Revision 1.2  2004/08/16 11:03:46  nw
first stab at a cat application

Revision 1.1  2004/08/11 17:40:49  nw
implemented send mail application
 
*/