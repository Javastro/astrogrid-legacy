/*$Id: AppsCEAComponentManager.java,v 1.2 2007/02/19 16:20:22 gtr Exp $
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
        pico.registerComponentImplementation(SiapImageFetchDescription.class);
    }

}


/* 
$Log: AppsCEAComponentManager.java,v $
Revision 1.2  2007/02/19 16:20:22  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:29:17  gtr
no message

Revision 1.5  2004/11/29 20:00:56  clq2
nww-itn07-684

Revision 1.4.46.1  2004/11/22 14:06:13  nw
start on implementing these.

Revision 1.4  2004/08/27 10:56:38  nw
removed container-inspecting applicationDescriptionLibrary from default setup - cea-commandline doesn't like it.

Revision 1.3  2004/08/17 15:07:25  nw
added concat application

Revision 1.2  2004/08/16 11:03:46  nw
first stab at a cat application

Revision 1.1  2004/08/11 17:40:49  nw
implemented send mail application
 
*/