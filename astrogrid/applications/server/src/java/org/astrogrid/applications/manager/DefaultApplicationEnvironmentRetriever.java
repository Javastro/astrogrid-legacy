/*
 * $Id: DefaultApplicationEnvironmentRetriever.java,v 1.2 2005/07/05 08:27:00 clq2 Exp $
 * 
 * Created on 03-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import java.io.File;

import org.astrogrid.applications.ApplicationEnvironmentUnavailableException;
import org.astrogrid.applications.manager.persist.ExecutionHistory;

/**
 * This is the default application environment retriever. It actually does nothing but throw exceptions that the environment cannot be retrieved.
 * @author Paul Harrison (pharriso@eso.org) 03-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class DefaultApplicationEnvironmentRetriever implements
      ApplicationEnvironmentRetriver {
   
   /**
    * 
    */
   public DefaultApplicationEnvironmentRetriever(ExecutionHistory history) {
  }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationEnvironmentRetriver#retrieveStdOut(java.lang.String)
    */
   public File retrieveStdOut(String executionId) throws ApplicationEnvironmentUnavailableException {
      throw new ApplicationEnvironmentUnavailableException("there is no environment available");
        
     }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationEnvironmentRetriver#retrieveStdErr(java.lang.String)
    */
   public File retrieveStdErr(String executionId) throws ApplicationEnvironmentUnavailableException {
      throw new ApplicationEnvironmentUnavailableException("there is no environment available");
   }

}


/*
 * $Log: DefaultApplicationEnvironmentRetriever.java,v $
 * Revision 1.2  2005/07/05 08:27:00  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/06/03 16:01:48  pah
 * first try at getting commandline execution log bz#1058
 *
 */
