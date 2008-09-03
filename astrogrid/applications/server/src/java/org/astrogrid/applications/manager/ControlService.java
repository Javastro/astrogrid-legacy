/*
 * $Id: ControlService.java,v 1.3 2008/09/03 14:18:56 pah Exp $
 * 
 * Created on 20-Jul-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;

/**
 * Provides functions to control the functioning of the Common Exection Controller.
 * @author Paul Harrison (pharriso@eso.org) 20-Jul-2005
 * @version $Name:  $
 * @since initial Coding
 */
public interface ControlService {
   /**
    * Deletes any temporary files associated with execution of applications. The time before which files should be deleted is 
    * expressed as a delta time from the current time.
    * @TODO this currently returns a string with a summary of what has been deleted - it probably should return a bean with this information - string was chosen for ease of implementation in jsp etc.
    * @param days The delta time in days before which the files should be deleted. Should be positive, e.g. a value of 2 means delete files which are nore than 3 days old.
    * @return summary of what has been deleted.
    * @deprecated should really delete the jobs in entirety {@link #deleteJob(String)} not just the temporary files
    */
   public String deleteOldRuntimeWorkFiles(int days);
   
   public boolean deleteJob(String jobId) throws ExecutionIDNotFoundException;
  
   
   

}


/*
 * $Log: ControlService.java,v $
 * Revision 1.3  2008/09/03 14:18:56  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.2.84.1  2008/05/08 22:40:53  pah
 * basic UWS working
 *
 * Revision 1.2  2005/08/10 14:45:37  clq2
 * cea_pah_1317
 *
 * Revision 1.1.2.1  2005/07/21 15:09:19  pah
 * new control component - provided methods for controlling the CEC
 *
 */
