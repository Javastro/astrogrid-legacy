/*$Id: HttpApplicationDescriptionLibrary.java,v 1.7 2004/12/18 15:43:57 jdt Exp $
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
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.http.registry.RegistryQuerier;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.registry.beans.cea.CeaHttpApplicationType;

/** 
 * library of HttpApplication application descriptions.
 * @author JDT
 *
 */
public class HttpApplicationDescriptionLibrary extends BaseApplicationDescriptionLibrary implements  ComponentDescriptor{
    /**
     * Commons Logger for this class
     */
    private static final Log log = LogFactory.getLog(HttpApplicationDescriptionLibrary.class);
	private RegistryQuerier querier;

    /** Construct a new HttpApplicationDescriptionLibrary, based on methods of parameter class
     * @param querier does all the hard work of talking to the registry and getting the stuff we need to create HttpAppplicationDescriptions
     * @throws IOException if there was a problem in the querier.  Typically this will be a failure of the 
     * querier to contact the registry
     * 
     */
    public HttpApplicationDescriptionLibrary(RegistryQuerier querier, ApplicationDescriptionEnvironment env) throws IOException {
        super(env);
        if (log.isTraceEnabled()) {
            log.trace("HttpApplicationDescriptionLibrary(RegistryQuerier querier = " + querier
                    + ", ApplicationDescriptionEnvironment env = " + env
                    + ") - start");
        }

        this.querier = querier;
        populate();

        if (log.isTraceEnabled()) {
            log.trace("HttpApplicationDescriptionLibrary(RegistryQuerier, Community, ApplicationDescriptionEnvironment) - end");
        }
    }
    
    
    /** 
     * Populate the library
     * @throws IOException if there was a problem in the querier.  Typically this will be a failure of the 
     * querier to contact the registry
     */
    protected final void populate() throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("populate() - start");
        }

        final Collection apps = querier.getHttpApplications();
        assert apps!=null;

        final Iterator it = apps.iterator();
        while (it.hasNext()) {
            final CeaHttpApplicationType app = (CeaHttpApplicationType) it.next(); 
            super.addApplicationDescription(new HttpApplicationDescription(app, env)); 
        }

        if (log.isTraceEnabled()) {
            log.trace("populate() - end");
        }
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        String returnString = "HttpApplication server serving applications found by RegistryQuerier" + querier + "\n"
                + super.getDescription();
        return returnString;
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "HttpApplication Library";
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[HttpApplicationDescriptionLibrary:");
        buffer.append(" logger: ");
        buffer.append(log);
        buffer.append(" querier: ");
        buffer.append(querier);
        buffer.append(super.toString());
        buffer.append("]");
        String returnString = buffer.toString();
        return returnString;
    }
}

/* 
$Log: HttpApplicationDescriptionLibrary.java,v $
Revision 1.7  2004/12/18 15:43:57  jdt
merge from  cea_pah_561b

Revision 1.6.2.1  2004/12/07 07:32:26  pah
update to pass band information properly

Revision 1.6  2004/11/27 13:20:03  pah
result of merge of pah_cea_bz561 branch

Revision 1.5.36.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.5  2004/09/01 15:42:26  jdt
Merged in Case 3

Revision 1.1.4.8  2004/08/30 14:54:42  jdt
tidied some imports

Revision 1.1.4.7  2004/08/11 12:11:59  jdt
Some refactoring of the registry querier.  Made the test registry
querier easier to use.

Revision 1.1.4.6  2004/08/09 16:37:13  jdt
Brought into line following pah's suggested schema changes

Revision 1.1.4.5  2004/08/06 13:30:22  jdt
safety checkin

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