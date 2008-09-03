/*
 * $Id: ApplicationEnvironmentRetriver.java,v 1.3 2008/09/03 14:18:56 pah Exp $
 * 
 * Created on 02-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import java.io.File;
import java.io.FileNotFoundException;

import org.astrogrid.applications.ApplicationStillRunningException;
import org.astrogrid.applications.environment.ApplicationEnvironmentUnavailableException;
import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;
import org.astrogrid.applications.manager.persist.PersistenceException;


/**
 * @author Paul Harrison (pharriso@eso.org) 02-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public interface ApplicationEnvironmentRetriver {
   public static final class StdIOType
   {
      private final int val;
      private StdIOType(final int s)
      {
         val = s;
      }
      public static final StdIOType err = new StdIOType(0);
      public static final StdIOType out = new StdIOType(1);
   }

  public File retrieveStdOut(String executionId) throws ExecutionIDNotFoundException, PersistenceException, FileNotFoundException, ApplicationStillRunningException, ApplicationEnvironmentUnavailableException;
  public File retrieveStdErr(String executionId) throws ExecutionIDNotFoundException, PersistenceException, FileNotFoundException, ApplicationStillRunningException, ApplicationEnvironmentUnavailableException;
}


/*
 * $Log: ApplicationEnvironmentRetriver.java,v $
 * Revision 1.3  2008/09/03 14:18:56  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.2.102.1  2008/06/11 14:31:42  pah
 * merged the ids into the application execution environment
 *
 * Revision 1.2  2005/07/05 08:27:00  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.2  2005/06/09 22:17:58  pah
 * tweaking the log getter
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/06/03 16:01:48  pah
 * first try at getting commandline execution log bz#1058
 *
 */
