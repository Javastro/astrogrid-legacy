/*
 * $Id: BaseApplicationDescriptionFactory.java,v 1.2 2011/09/02 21:55:49 pah Exp $
 * 
 * Created on 9 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.base.ApplicationBase;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotLoadedException;

/**
 * factory for creating an {@link ApplicationDefinition} from a metadata description.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 Jul 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public interface BaseApplicationDescriptionFactory {
    
    
    public abstract ApplicationDescription addApp(ApplicationBase apptyp, MetadataAdapter ma) throws ApplicationDescriptionNotLoadedException;
}


/*
 * $Log: BaseApplicationDescriptionFactory.java,v $
 * Revision 1.2  2011/09/02 21:55:49  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/15 09:48:12  pah
 * redesign of parameterAdapters
 *
 */
