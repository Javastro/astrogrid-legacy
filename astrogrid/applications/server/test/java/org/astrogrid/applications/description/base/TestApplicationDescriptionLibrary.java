/*$Id: TestApplicationDescriptionLibrary.java,v 1.3 2004/11/27 13:20:02 pah Exp $
 * Created on 26-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description.base;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * Test / Mock implementation of an application description library. will contain a single resource.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 *
 */
public class TestApplicationDescriptionLibrary implements ApplicationDescriptionLibrary {
    private final String RES_NAME;
    /**
     *  Construct a new TestApplicationDescriptionLibrary
     * @param RES_NAME name of the single resource to place in the library
     */
    public TestApplicationDescriptionLibrary(String RES_NAME) {        
        super();
        this.RES_NAME = RES_NAME;
    }
    public ApplicationDescription getDescription(String name) throws ApplicationDescriptionNotFoundException {
        if (! name.equals(RES_NAME)) {
            throw new ApplicationDescriptionNotFoundException("no entry for " + name);
        }
        ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(null,null,null);
        AbstractApplicationDescription appDesc =   new AbstractApplicationDescription(env) {
            public Application initializeApplication(String jobStepID, User user, Tool tool )
                throws Exception {
                return null;
            }
        };     
        appDesc.setName(RES_NAME);
        BaseApplicationInterface appIface = new BaseApplicationInterface("iface",appDesc);
        BaseParameterDescription foo = new BaseParameterDescription();
        foo.setName("foo");
        foo.setType(ParameterTypes.TEXT);
        
        appDesc.addParameterDescription(foo);
        BaseParameterDescription bar = new BaseParameterDescription();
        bar.setName("bar");
        bar.setType(ParameterTypes.TEXT);        
        appDesc.addParameterDescription(bar);
        try {
        appIface.addInputParameter("foo");
        appIface.addOutputParameter("bar");
        } catch (ParameterDescriptionNotFoundException e) {
            e.printStackTrace();
            return null;
        }   
        appDesc.addInterface(appIface);
        
        return appDesc;
    }
    public String[] getApplicationNames() {
        return new String[]{RES_NAME};
    }
}

/* 
$Log: TestApplicationDescriptionLibrary.java,v $
Revision 1.3  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.2.90.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:11  nw
checked in early, broken version - but it builds and tests (fail)
 
*/