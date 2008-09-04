/*$Id: TestApplicationDescriptionLibrary.java,v 1.5 2008/09/04 19:10:53 pah Exp $
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

import net.ivoa.resource.cea.CeaApplication;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.AppMetadataAdapter;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.security.SecurityGuard;

/**
 * Test / Mock implementation of an application description library. will contain a single resource.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 18 Mar 2008
 *
 */
public class TestApplicationDescriptionLibrary implements ApplicationDescriptionLibrary {
    private final String RES_NAME;
    private CeaApplication appMeta;
    private AbstractApplicationDescription appDesc;
    private String shortname;
    /**
     *  Construct a new TestApplicationDescriptionLibrary
     * @param RES_NAME name of the single resource to place in the library
     * @throws ParameterDescriptionNotFoundException 
     */
    public TestApplicationDescriptionLibrary(String RES_NAME, String shortName) throws ParameterDescriptionNotFoundException {        
        super();
        this.RES_NAME = RES_NAME;
        this.shortname = shortName;
        
        
        ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(null,null,null);
        appMeta = new CeaApplication();
	appMeta.setIdentifier(RES_NAME);
	appMeta.setShortName(shortName);
        InterfaceDefinition appIface = new InterfaceDefinition();
        appIface.setId("iface");
        BaseParameterDefinition foo = new BaseParameterDefinition();
        foo.setId("foo");
        foo.setType(ParameterTypes.TEXT);
        
        appMeta.addParameterDescription(foo);
        BaseParameterDefinition bar = new BaseParameterDefinition();
        bar.setId("bar");
        bar.setType(ParameterTypes.TEXT);        
        appMeta.addParameterDescription(bar);
        appIface.addInputParameter("foo");
        appIface.addOutputParameter("bar");
        appMeta.addInterface(appIface);
	appDesc = new AbstractApplicationDescription(new AppMetadataAdapter(appMeta) ) {
            public Application initializeApplication(String jobStepID, SecurityGuard secGuard, Tool tool )
                throws Exception {
                return null;
            }
        };

    }
    public ApplicationDescription getDescription(String name) throws ApplicationDescriptionNotFoundException {
        if (! name.equals(RES_NAME)) {
            throw new ApplicationDescriptionNotFoundException("no entry for " + name);
        }
         
        return appDesc;
    }
    public String[] getApplicationNames() {
        return new String[]{RES_NAME};
    }
    public ApplicationDescription getDescriptionByShortName(String name) throws ApplicationDescriptionNotFoundException{
    if (! name.equals(shortname)) {
        throw new ApplicationDescriptionNotFoundException("no entry for " + name);
    }
     
    return appDesc;
	
    }
}

/* 
$Log: TestApplicationDescriptionLibrary.java,v $
Revision 1.5  2008/09/04 19:10:53  pah
ASSIGNED - bug 2825: support VOSpace
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
Added the basic implementation to support VOSpace  - however essentially untested on real deployement

Revision 1.4  2008/09/03 14:18:52  pah
result of merge of pah_cea_1611 branch

Revision 1.3.182.9  2008/08/29 07:28:31  pah
moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration

Revision 1.3.182.8  2008/08/02 13:33:56  pah
safety checkin - on vacation

Revision 1.3.182.7  2008/05/17 16:45:01  pah
tidy tests to make sure more are passing

Revision 1.3.182.6  2008/05/13 15:57:32  pah
uws with full app running UI is working

Revision 1.3.182.5  2008/05/01 15:22:47  pah
updates to tool

Revision 1.3.182.4  2008/04/17 16:08:33  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.3.182.3  2008/04/01 13:50:07  pah
http service also passes unit tests with new jaxb metadata config

Revision 1.3.182.2  2008/03/26 17:15:39  pah
Unit tests pass

Revision 1.3.182.1  2008/03/19 23:10:55  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

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