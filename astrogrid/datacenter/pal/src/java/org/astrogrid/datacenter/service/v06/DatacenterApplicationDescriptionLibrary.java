/*$Id: DatacenterApplicationDescriptionLibrary.java,v 1.1 2004/09/28 15:02:13 mch Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.service.v06;

import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.datacenter.service.DataServer;

import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;

/** Application description library for datacenters - initialized with a single instance of a {@link DatacenterApplicationDescription}
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationDescriptionLibrary extends BaseApplicationDescriptionLibrary {
    /** configuration interface describing the configurable metadata for a datacenter
     * at moment just the name the datacenter application will take.
     * possibly more to add later.
     * @author Noel Winstanley nw@jb.man.ac.uk 16-Jul-2004
     *
     */
    public interface DatacenterMetadata {
        /** determines the name of the single application in this library */
        public String getName();
    }
    /** Construct a new DatacenterApplicationDescriptionLibrary
     * 
     */       
    public DatacenterApplicationDescriptionLibrary(DatacenterMetadata md,DataServer ds,ApplicationDescriptionEnvironment env,QueuedExecutor qe) {
        super();       
        addApplicationDescription(new DatacenterApplicationDescription(md.getName(), ds,env,qe));
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Datacenter Application Description Library";
    }

}


/* 
$Log: DatacenterApplicationDescriptionLibrary.java,v $
Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.3  2004/09/17 01:27:21  nw
added thread management.

Revision 1.2  2004/07/20 02:14:48  nw
final implementaiton of itn06 Datacenter CEA interface

Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/