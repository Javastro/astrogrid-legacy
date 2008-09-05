/*
 * $Id: AbstractCEAComponentContainer.java,v 1.1 2008/09/05 07:55:45 pah Exp $
 * 
 * Created on 4 Sep 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.component;

import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.registry.RegistryUploader;
import org.astrogrid.applications.manager.ControlService;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.persist.ExecutionHistory;

/**
 * Contains common code for a component container to allow different component containers (with different wiring). The default component
 * container is the {@link CEAComponentContainer} that is wired with Spring {@link http://www.springframework.org/} - that class an the spring configuration
 * files should be examined to see how and which instances of the components are to be wired for particular CEA functionality.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 4 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public abstract class AbstractCEAComponentContainer implements CEAComponents {

    protected ControlService controlService;
    protected ExecutionController executionController;
    protected MetadataService metadataService;
    protected QueryService queryService;
    protected RegistryUploader registryUploaderService;
    protected ExecutionHistory executionHistoryService;
    protected ApplicationDescriptionLibrary applicationDescriptionLibrary;

    /**
     * @return the controlService
     */
    public ControlService getControlService() {
        return controlService;
    }

    /**
     * @return the executionController
     */
    public ExecutionController getExecutionController() {
        return executionController;
    }

    /**
     * @return the metadataService
     */
    public MetadataService getMetadataService() {
        return metadataService;
    }

    /**
     * @return the queryService
     */
    public QueryService getQueryService() {
        return queryService;
    }

    /**
     * @return the registryUploaderService
     */
    public RegistryUploader getRegistryUploaderService() {
        return registryUploaderService;
    }

    public ExecutionHistory getExecutionHistoryService() {
        return executionHistoryService;
    }

    public ApplicationDescriptionLibrary getApplicationDescriptionLibrary() {
        return applicationDescriptionLibrary;
    }

}


/*
 * $Log: AbstractCEAComponentContainer.java,v $
 * Revision 1.1  2008/09/05 07:55:45  pah
 * extract out a component container base class
 *
 */
