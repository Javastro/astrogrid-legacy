/*$Id: HttpApplicationDescriptionLibrary.java,v 1.3 2004/07/30 14:54:47 jdt Exp $
 * Created on Jul 24, 2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.http;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.beans.v1.WebHttpApplication;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.http.registry.RegistryQuerier;
import org.astrogrid.component.descriptor.ComponentDescriptor;

/** 
 * library of HttpApplication application descriptions.
 * @author JDT
 *
 */
public class HttpApplicationDescriptionLibrary extends BaseApplicationDescriptionLibrary implements  ComponentDescriptor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(HttpApplicationDescriptionLibrary.class);
	private RegistryQuerier querier;


    /** configuration interface - defines the name of the community the applications will be added to. */
    //@TODO find out about this....can we stick in Base and inherit it?
    public interface Community {
        String getCommunity();
    }

    /** Construct a new HttpApplicationDescriptionLibrary, based on methods of parameter class
     * @param querier does all the hard work of talking to the registry and getting the stuff we need to create HttpAppplicationDescriptions
     * @TODO see how all this relates to whatever we need in the registry
     * 
     */
    public HttpApplicationDescriptionLibrary(RegistryQuerier querier, Community community,ApplicationDescriptionEnvironment env) {
        super();
        this.querier = querier;
        this.community = community;
        this.env = env;
        populate();
    }
    
    //@TODO check with Noel about the non-privateness of all this
    protected final Community community;
    
    protected final ApplicationDescriptionEnvironment env;
    /** 
     * Populate the library
     */
    protected final void populate() {
        String communityName = community.getCommunity();
        List apps=null;
        try {
            apps = querier.getHttpApplications();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
            //@TODO make this robust...
        }
        Iterator it = apps.iterator();
        while (it.hasNext()) {
            WebHttpApplication app = (WebHttpApplication) it.next(); //@TODO consider renaming to avoid confusion
            super.addApplicationDescription(new HttpApplicationDescription(app,communityName,env)); 
        }
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "HttpApplication server serving applications found by RegistryQuerier" + querier + "\n" + super.getDescription();
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "HttpApplication Library";
    }

}

/* 
$Log: HttpApplicationDescriptionLibrary.java,v $
Revision 1.3  2004/07/30 14:54:47  jdt
merges in from case3 branch

Revision 1.1.4.4  2004/07/29 21:30:47  jdt
*** empty log message ***

Revision 1.1.4.3  2004/07/29 17:08:22  jdt
Think about how I'm going to get stuff out of the registry

Revision 1.1.4.2  2004/07/29 16:35:21  jdt
Safety checkin, while I think about what happens next.

Revision 1.1.4.1  2004/07/27 17:20:25  jdt
merged from applications_JDT_case3

Revision 1.1.2.1  2004/07/24 17:16:16  jdt
Borrowed from javaclass application....stealing is always quicker.

 
*/