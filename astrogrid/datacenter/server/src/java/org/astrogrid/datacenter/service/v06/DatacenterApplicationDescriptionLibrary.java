/*$Id: DatacenterApplicationDescriptionLibrary.java,v 1.1 2004/07/13 17:11:09 nw Exp $
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

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationDescriptionLibrary extends BaseApplicationDescriptionLibrary {
    /** Construct a new DatacenterApplicationDescriptionLibrary
     * 
     */
    public DatacenterApplicationDescriptionLibrary(DataServer ds,ApplicationDescriptionEnvironment env) {
        super();       
        addApplicationDescription(new DatacenterApplicationDescription(ds,env));
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
Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/