/*
 * $Id: ApplicationDescription.java,v 1.8 2011/09/02 21:55:49 pah Exp $
 * 
 * Created on 23 Feb 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import org.astrogrid.applications.ApplicationFactory;

/**
 * Note that the old ApplicationDescription is now {@link ApplicationDefinition}.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 23 Feb 2009
 * @version $Name:  $
 * @since VOTech Stage 8
 */
public interface ApplicationDescription extends ApplicationDefinition, ApplicationFactory {

    /**
     * get the underlying description of the application.
     * @TODO should this really be exposed?
     * @return
     */
    public MetadataAdapter getMetadataAdapter();

}


/*
 * $Log: ApplicationDescription.java,v $
 * Revision 1.8  2011/09/02 21:55:49  pah
 * result of merging the 2931 branch
 *
 * Revision 1.7.2.1  2009/07/15 09:48:12  pah
 * redesign of parameterAdapters
 *
 * Revision 1.7  2009/02/26 12:45:54  pah
 * separate more out into cea-common for both client and server
 *
 */
