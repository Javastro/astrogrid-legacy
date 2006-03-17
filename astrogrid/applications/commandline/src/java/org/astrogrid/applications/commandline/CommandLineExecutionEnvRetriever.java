/*
 * $Id: CommandLineExecutionEnvRetriever.java,v 1.7 2006/03/17 17:50:58 clq2 Exp $
 * 
 * Created on 03-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.ApplicationStillRunningException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.applications.commandline.CommandLineConfiguration;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;
import org.astrogrid.applications.manager.persist.PersistenceException;

/**
 * @author Paul Harrison (pharriso@eso.org) 03-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class CommandLineExecutionEnvRetriever implements
      ApplicationEnvironmentRetriver {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(CommandLineExecutionEnvRetriever.class);
   private final ExecutionHistory executionHistory;
   private final File workingDirectory;
   
   
   /**
    * 
    */
   public CommandLineExecutionEnvRetriever(ExecutionHistory eh, 
                                           CommandLineConfiguration config) {

      executionHistory = eh;
      this.workingDirectory = config.getWorkingDirectory();
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationEnvironmentRetriver#retrieveStdOut(java.lang.String)
    */
   public File retrieveStdOut(String executionId) throws ExecutionIDNotFoundException, PersistenceException, FileNotFoundException, ApplicationStillRunningException {
      return new File(locateExecutionDirectory(executionId),CommandLineApplicationEnvironment.CEA_OUTPUT_LOG);
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationEnvironmentRetriver#retrieveStdErr(java.lang.String)
    */
   public File retrieveStdErr(String executionId) throws ExecutionIDNotFoundException, PersistenceException, FileNotFoundException, ApplicationStillRunningException {
      return  new File(locateExecutionDirectory(executionId),CommandLineApplicationEnvironment.CEA_ERROR_LOG);
   }
   
   private File locateExecutionDirectory(String executionId) throws ExecutionIDNotFoundException, PersistenceException, FileNotFoundException, ApplicationStillRunningException {
      logger.debug("Getting execution directory for " +executionId);
      File retval = null;
      if (executionHistory.isApplicationInCurrentSet(executionId)) {
         Application app = executionHistory.getApplicationFromCurrentSet(executionId);
         if(isFinished(app.getStatus()))
         {
            retval = CommandLineApplicationEnvironment.createExecutionDirectoryName(executionId, workingDirectory);
         }
         else
         {
            throw new ApplicationStillRunningException("Application "+app.getID()+" executionId="+executionId+ " still running");
         }
         
      }
      else // look in the persistance store - just to check that the executionID exists
      {
         
         ExecutionSummaryType summary = executionHistory.getApplicationFromArchive(executionId);
         retval = CommandLineApplicationEnvironment.createExecutionDirectoryName(executionId, workingDirectory);
      }
      if(retval != null && !retval.exists())
      {
         throw new FileNotFoundException("could not find working directory ="+ retval.getAbsolutePath());
      }
      return retval;

  }

   /**
    * Return true if application is finished (or in error).
    * @param status
    * @return
    */
   private boolean isFinished(Status status) {
      if(status == Status.COMPLETED || status == Status.ERROR)
      {
         return true;
      }
      else
      {
         return false;
      }
  }

}


/*
 * $Log: CommandLineExecutionEnvRetriever.java,v $
 * Revision 1.7  2006/03/17 17:50:58  clq2
 * gtr_1489_cea correted version
 *
 * Revision 1.5  2006/03/07 21:45:25  clq2
 * gtr_1489_cea
 *
 * Revision 1.2.34.1  2005/12/19 18:12:30  gtr
 * Refactored: changes in support of the fix for 1492.
 *
 * Revision 1.2  2005/07/05 08:27:01  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/06/03 16:01:48  pah
 * first try at getting commandline execution log bz#1058
 *
 */
