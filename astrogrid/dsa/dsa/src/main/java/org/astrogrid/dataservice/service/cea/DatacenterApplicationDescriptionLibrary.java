/*$Id: DatacenterApplicationDescriptionLibrary.java,v 1.2 2009/10/21 19:01:00 gtr Exp $
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
import org.astrogrid.dataservice.metadata.InstallationIvorn;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.metadata.MetadataException;

/** 
 * Application-description library for DSA services.
 * A DSA installation contains one or more catalogues and understands one
 * application for each individual catalogue plus one for all catalogues taken
 * together; the latter application implicitly supports joins between
 * catalogues while the others do not. The DSA installation need not register
 * all the applications it understands.
 *
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 */
public class DatacenterApplicationDescriptionLibrary extends BaseApplicationDescriptionLibrary {
    
  /**
   * Constructs a new DatacenterApplicationDescriptionLibrary
   *
   */
  public DatacenterApplicationDescriptionLibrary(DataServer                        ds,
                                                 ApplicationDescriptionEnvironment env,
                                                 QueuedExecutor                    qe) throws MetadataException,
                                                                                              IOException {

    super(env);
        
    String[] catalogNames = TableMetaDocInterpreter.getCatalogNames();
    // If no catalogues, there will be no registered apps - but
    // other parts of the DSA will complain about having no 
    // catalogs anyway
    // The TableMetaDocInterpreter pre-validates the catalog
    // names etc so no need to check them for null here
    for (int i = 0 ; i < catalogNames.length; i++) {
      String catalogName = catalogNames[i];
      String catNamePrefix = catalogName+"/";
      String appIvorn = InstallationIvorn.makeIvorn(catNamePrefix+"ceaApplication");
      String appName = (appIvorn.startsWith("ivo://"))?
           appIvorn.substring(6) :
           appIvorn;
      addApplicationDescription(new DatacenterApplicationDescription(appName,ds,env,qe));
    }
       
    String appIvorn = InstallationIvorn.makeIvorn("ceaApplication");
    String appName = (appIvorn.startsWith("ivo://"))?
        appIvorn.substring(6) :
        appIvorn;
    addApplicationDescription(new DatacenterApplicationDescription(appName,ds,env,qe));
  }

  /**
   * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
   */
  @Override
  public String getName() {
    return "Datacenter Application Description Library";
  }

}