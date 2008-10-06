/*$Id: SiapImageFetchDescription.java,v 1.9 2008/10/06 12:16:14 pah Exp $
 * Created on 15-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.apps;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.InterfaceDefinition;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.description.cea.CeaApplication;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.intnl.InternallyConfiguredApplicationDescription;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.security.SecurityGuard;
import uk.ac.starlink.util.DataSource;

/** @todo implementation is a bit tatty - could do with refactoring a bunch of these applications,
 * improving the internal design, and extracting generally useful helper classes.
 * for astrogrid 2 - this code is ugly, but works for now.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Nov-2004
 *
 */
public class SiapImageFetchDescription extends InternallyConfiguredApplicationDescription
        implements ComponentDescriptor {
    static final String URLS = "urls";
    static final String IVORNS="ivorns";
    static final String BASEIVORN = "baseIvorn";
    static final String TABLE = "table";
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(SiapImageFetchDescription.class);
    private static final CeaApplication app = new CeaApplication();
    
    static {
	app.setIdentifier("ivo://org.astrogrid.util/siap-image-fetch");
	addParameter(app, TABLE, ParameterTypes.TABLE, "Image List", "VOTable containing URIs of images");
	addParameter(app, BASEIVORN, ParameterTypes.ANY_URI, "Save To", "Location of a directory in myspace in which to save fetched images");
	addParameter(app, URLS, ParameterTypes.TEXT, "URLs of files to fetch", "List of  the urls  of the fetched files");
	addParameter(app, IVORNS, ParameterTypes.TEXT, "Ivorns of fetched Files", "List of  the ivorns of the fetched files");
	InterfaceDefinition intf = addInterface(app, "default");
	intf.addInput(TABLE);
	intf.addInput(BASEIVORN);
	intf.addInput(URLS);
	intf.addOutput(IVORNS);
    }

    /** Construct a new SiapImageFetchDescription
     * @param conf 
     */
    public SiapImageFetchDescription( Configuration conf) {
        super(app , conf);
    }
    
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    @Override
    public String getDescription() {
        return "Application that parses results from a siap query and saves to myspace";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }

    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, SecurityGuard, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String callerAssignedID,
            SecurityGuard secGuard, Tool tool) throws Exception {
        ApplicationInterface iface = this.getInterface(tool.getInterface());
        ApplicationEnvironment env = new ApplicationEnvironment(callerAssignedID, secGuard, getInternalComponentFactory().getIdGenerator(), conf);
	return new SiapImageFetchApplication(tool,iface,env , getInternalComponentFactory().getProtocolLibrary());
    }
    
    public static class ParameterAdapterDataSource extends DataSource {
        public ParameterAdapterDataSource(CatApplicationDescription.StreamParameterAdapter p) {
            this.p = p;
        }
        CatApplicationDescription.StreamParameterAdapter p;
        @Override
	protected InputStream getRawInputStream() throws IOException {
            
            try {
                return (InputStream)p.process();
            } catch (CeaException e) {
             throw new IOException(e.getMessage());
            }
        }
    }
    

}


/* 
$Log: SiapImageFetchDescription.java,v $
Revision 1.9  2008/10/06 12:16:14  pah
factor out classes common to server and client

Revision 1.8  2008/09/24 13:40:49  pah
package naming changes

Revision 1.7  2008/09/13 09:51:02  pah
code cleanup

Revision 1.6  2008/09/10 23:27:16  pah
moved all of http CEC and most of javaclass CEC code here into common library

Revision 1.3  2008/09/03 14:18:33  pah
result of merge of pah_cea_1611 branch

Revision 1.2.10.5  2008/09/03 12:01:56  pah
should perhaps be moved out of javaclass

Revision 1.2.10.4  2008/08/02 13:33:41  pah
safety checkin - on vacation

Revision 1.2.10.3  2008/05/13 15:14:07  pah
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708

Revision 1.2.10.2  2008/03/27 13:37:24  pah
now producing correct registry documents

Revision 1.2.10.1  2008/03/19 23:28:58  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.2  2007/02/19 16:20:22  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:29:17  gtr
no message

Revision 1.4  2005/07/14 17:09:16  jdt
corrected the authorityId

Revision 1.3  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.2.26.1  2005/03/11 11:21:19  nw
replaced VoSpaceClient with FileManagerClient

Revision 1.2  2004/11/29 20:00:56  clq2
nww-itn07-684

Revision 1.1.2.2  2004/11/24 00:14:58  nw
factored the application class out of the descripiton - makes for
more manageable code.

Revision 1.1.2.1  2004/11/22 14:06:13  nw
start on implementing these.
 
*/