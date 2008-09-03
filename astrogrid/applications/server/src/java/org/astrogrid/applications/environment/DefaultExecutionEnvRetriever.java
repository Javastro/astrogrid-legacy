/*
 * $Id: DefaultExecutionEnvRetriever.java,v 1.2 2008/09/03 14:18:55 pah Exp $
 * 
 * Created on 03-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.environment;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.ApplicationStillRunningException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.contracts.CEAConfiguration;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;
import org.astrogrid.applications.manager.persist.PersistenceException;

/**
 * @author Paul Harrison (pharriso@eso.org) 03-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class DefaultExecutionEnvRetriever implements
      ApplicationEnvironmentRetriver {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(DefaultExecutionEnvRetriever.class);
   private final ExecutionHistory executionHistory;
   private final File workingDirectory;
   
   
   /**
    * 
    */
   public DefaultExecutionEnvRetriever(ExecutionHistory eh, 
                                           CEAConfiguration config) {

      executionHistory = eh;
      this.workingDirectory = config.getTemporaryFilesDirectory();
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationEnvironmentRetriver#retrieveStdOut(java.lang.String)
    */
   public File retrieveStdOut(String executionId) throws ExecutionIDNotFoundException, PersistenceException, FileNotFoundException, ApplicationStillRunningException {
      return new File(locateExecutionDirectory(executionId),ApplicationEnvironment.CEA_OUTPUT_LOG);
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationEnvironmentRetriver#retrieveStdErr(java.lang.String)
    */
   public File retrieveStdErr(String executionId) throws ExecutionIDNotFoundException, PersistenceException, FileNotFoundException, ApplicationStillRunningException {
      return  new File(locateExecutionDirectory(executionId),ApplicationEnvironment.CEA_ERROR_LOG);
   }
   
   private File locateExecutionDirectory(String executionId) throws ExecutionIDNotFoundException, PersistenceException, FileNotFoundException, ApplicationStillRunningException {
      logger.debug("Getting execution directory for " +executionId);
      File retval = null;
      if (executionHistory.isApplicationInCurrentSet(executionId)) {
         Application app = executionHistory.getApplicationFromCurrentSet(executionId);
         if(isFinished(app.getStatus()))
         {
            retval = ApplicationEnvironment.createExecutionDirectoryName(executionId, workingDirectory);
         }
         else
         {
            throw new ApplicationStillRunningException("Application "+app.getId()+" executionId="+executionId+ " still running");
         }
         
      }
      else // look in the persistance store - just to check that the executionID exists
      {
         
         ExecutionSummaryType summary = executionHistory.getApplicationFromArchive(executionId);
         retval = ApplicationEnvironment.createExecutionDirectoryName(executionId, workingDirectory);
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
 * $Log: DefaultExecutionEnvRetriever.java,v $
 * Revision 1.2  2008/09/03 14:18:55  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/06/11 14:31:43  pah
 * merged the ids into the application execution environment
 *
 * Revision 1.7.56.3  2008/04/23 14:13:49  pah
 * ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749
 *
 * Revision 1.7.56.2  2008/04/17 16:16:55  pah
 * removed all castor marshalling - even in the web service layer - unit tests passing
 * some uws functionality present - just the bare bones.
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 * ASSIGNED - bug 2739: remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 *
 * Revision 1.7.56.1  2008/04/04 15:34:51  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
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
