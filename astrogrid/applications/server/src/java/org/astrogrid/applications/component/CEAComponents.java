/*
 * $Id: CEAComponents.java,v 1.2 2008/09/03 14:18:57 pah Exp $
 * 
 * Created on 2 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.component;

import junit.framework.Test;

import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.registry.RegistryUploader;
import org.astrogrid.applications.manager.ControlService;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.persist.ExecutionHistory;

/**
 * Interface to a container of components. This differs from the old CEAComponentManager in that all references to the underlying container implementation have been removed.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 2 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public interface CEAComponents {

    
    /** @return the controller component that does work */
    public ExecutionController getExecutionController();
    /** @return the meta data component that describes what this controller provides */
    public MetadataService getMetadataService();
    /** @return the query component that allows inspection of progress */
    public QueryService getQueryService();
    /** @return the component that will upload to the registry */
    public RegistryUploader getRegistryUploaderService();
    
    public ExecutionHistory getExecutionHistoryService();
    
    public ControlService getControlService();
    
    public ApplicationDescriptionLibrary getApplicationDescriptionLibrary();
    

    /** output human-readable description of contents of container as HTML
     *  <p>
     * used for debugging / output to JSP 
     * Uses descriptions from components that implement the {@link org.astrogrid.jes.component.descriptor.ComponentDescriptor} interface*/
    public abstract String informationHTML();
    /** output human-readable description of contents of container */
    public abstract String information();
    /** return a suite of installation tests for the components in the container 
     * <p>
     * this suite is the composition of all installation tests returned by objects in the container that implement the
     * {@link org.astrogrid.jes.component.descriptor.ComponentDescriptor} interface
     * */
    public abstract Test getSuite();

}


/*
 * $Log: CEAComponents.java,v $
 * Revision 1.2  2008/09/03 14:18:57  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.3  2008/05/13 15:57:31  pah
 * uws with full app running UI is working
 *
 * Revision 1.1.2.2  2008/05/08 22:40:53  pah
 * basic UWS working
 *
 * Revision 1.1.2.1  2008/04/04 15:46:07  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
