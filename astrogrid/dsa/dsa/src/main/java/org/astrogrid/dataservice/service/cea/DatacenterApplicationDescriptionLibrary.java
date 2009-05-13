/*$Id: DatacenterApplicationDescriptionLibrary.java,v 1.1 2009/05/13 13:20:32 gtr Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.dataservice.service.cea;

import java.io.IOException;

import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.dataservice.service.DataServer;

import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;

import org.astrogrid.dataservice.metadata.VoResourceSupportBase;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.metadata.MetadataException;

/** Application description library for datacenters - initialized with a single instance of a {@link DatacenterApplicationDescription}
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationDescriptionLibrary extends BaseApplicationDescriptionLibrary {
    /** configuration interface describing the configurable metadata for 
     * a datacenter
     * Sets up mappings for the CEA application(s) for this DSA.
     * @author Noel Winstanley nw@jb.man.ac.uk 16-Jul-2004
     *
     */

    /** Construct a new DatacenterApplicationDescriptionLibrary
     *
     */
    public DatacenterApplicationDescriptionLibrary(
          DataServer ds, ApplicationDescriptionEnvironment env,
          QueuedExecutor qe) throws MetadataException, IOException {

        super(env);
        
        String[] catalogNames = new String[0];
        catalogNames = TableMetaDocInterpreter.getCatalogNames();
        // If no catalogues, there will be no registered apps - but
        // other parts of the DSA will complain about having no 
        // catalogs anyway
        // The TableMetaDocInterpreter pre-validates the catalog
        // names etc so no need to check them for null here
        for (int i = 0 ; i < catalogNames.length; i++) {
           String catalogName = catalogNames[i];
           //Insert cat-specific prefix
           String catNamePrefix = catalogName+"/";
           String appIvorn = 
                VoResourceSupportBase.makeIvorn(catNamePrefix+"ceaApplication");

           // Strip off any leading ivo://
           if (appIvorn.startsWith("ivo://")) {
              appIvorn = appIvorn.substring(6);
           }
           String appName= appIvorn;
           addApplicationDescription(
                 new DatacenterApplicationDescription(appName,ds,env,qe));
       }
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
Revision 1.1  2009/05/13 13:20:32  gtr
*** empty log message ***

Revision 1.3  2008/02/07 17:27:45  clq2
PAL_KEA_2518

Revision 1.2.14.1  2008/02/07 16:36:15  kea
Further fixes for 1.0 support, and also MBT's changes merged into my branch.

Revision 1.2  2007/09/07 09:30:51  clq2
PAL_KEA_2235

Revision 1.1.1.1.130.2  2007/09/04 12:27:03  kea
Tweaks

Revision 1.1.1.1.130.1  2007/09/04 08:41:37  kea
Fixing v1.0 registrations and multi-catalog CEA stuff.

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.1.30.1  2005/01/13 18:57:31  mch
Fixes to metadata mostly

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.3  2004/09/17 01:27:21  nw
added thread management.

Revision 1.2  2004/07/20 02:14:48  nw
final implementaiton of itn06 Datacenter CEA interface

Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/
