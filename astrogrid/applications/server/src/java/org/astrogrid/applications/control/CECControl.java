/*
 * $Id: CECControl.java,v 1.3 2008/09/13 09:51:05 pah Exp $
 * 
 * Created on 19-Jul-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.control;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import junit.framework.Test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.manager.ControlService;
import org.astrogrid.applications.manager.ExecutionPolicy;
import org.astrogrid.applications.manager.Stopable;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;
import org.astrogrid.component.descriptor.ComponentDescriptor;

/**
 * Provides useful functions for managing the resources consumed by the
 * commandline runtime environment.
 * 
 * @author Paul Harrison (pharriso@eso.org) 19-Jul-2005
 * @version $Name:  $
 * @since initial Coding
 */

public class CECControl implements ControlService, ComponentDescriptor, Stopable {
   /**
    * Logger for this class
    */
   private static final Log logger 
       = LogFactory.getLog(CECControl.class);
   
   /**
    * The directory where files for jobs will be kept.
    */
   private File workingDirectory;

private final ExecutionHistory executionHistory;

private final ExecutionPolicy policy;
 
private final ScheduledExecutorService scheduler;

   /**
    * Constructs a new CommandLineCECControl with a given working directory.
    */
   public CECControl(Configuration config, ExecutionHistory eh, ExecutionPolicy policy) {
     this.workingDirectory = config.getTemporaryFilesDirectory();
     this.executionHistory = eh;
     this.policy = policy;
     this.scheduler = Executors.newScheduledThreadPool(1);
     startDestructionMonitor();
  }

   /**
    * Deletes any temporary files associated with execution of applications. 
    * The time before which files should be deleted is expressed as a delta time 
    * from the current time.
    * @param days The delta time in days before which the files should be deleted. Should be positive, e.g. a value of 2 means delete files which are nore than 3 days old.
    * @return
    */
   public String deleteOldRuntimeWorkFiles(int days) {
      StringBuffer result = new StringBuffer();
      int delcount = 0, failedcount = 0, bytesdeleted = 0;
      File baseDir = this.workingDirectory;
      Calendar cal = new GregorianCalendar();
      cal.add(Calendar.DATE, -days);
      long moddate = cal.getTimeInMillis();
      File[] files = baseDir.listFiles();
      for (int i = 0; i < files.length; i++) {
         if(files[i].lastModified() < moddate){
            int del = totalsize(files[i]);
            if (!deleteDirectory(files[i])) {
               logger.warn("directory "+files[i]+ " could not be deleted");
               failedcount++;
            }
            else {
               bytesdeleted += del;
               delcount++;
            }
         }
      }
      result.append("Deleted "+ delcount + " directories, "+ bytesdeleted+ " bytes");
      if (failedcount > 0) {
         result.append("\nFailed to delete "+failedcount+" directories");
      }
      return result.toString();
   }

   /**
    * Calculate the total size for files in a directory. This recursive function will find the total size of files in a directory if that is given as an argument.
    * @param file
    * @return
    */
   private int totalsize(File file) {
      
      int nbytes = 0;
      if (file.isDirectory()) {
         nbytes += file.length(); // TODO - this is actually unspecified for directories according to the java spec - see if it does the right thing for unix...
         File[] files = file.listFiles();
         for (int i = 0; i < files.length; i++) {
            nbytes += totalsize(files[i]);
         }
      }
      else {
         nbytes += file.length();
      }
      return nbytes;
  }

   /**
    * Recursively delete a directory and all of its contents.
    * 
    * @param dir
    * @return
    */
   private boolean deleteDirectory(File dir) {
      if (dir.isDirectory()) {
         String[] children = dir.list();
         for (int i = 0; i < children.length; i++) {
            boolean success = deleteDirectory(new File(dir, children[i]));
            if (!success) {
               return false;
            }
         }
      }

      // The directory is now empty (or is a plain file) so delete it
      return dir.delete();

   }

public boolean deleteJob(String jobId) throws ExecutionIDNotFoundException {
    boolean retval = false;
    
    if (executionHistory.isApplicationInCurrentSet(jobId)) {
	logger.warn("job is currently running - cannot delete"); // TODO perhaps should attempt to abort...
	return false;
    } else {
        retval = executionHistory.delete(jobId);
        // delete any temporary files
        File tmpDir = new File(this.workingDirectory,jobId);
        retval = retval & deleteDirectory(tmpDir);
    }
    
    return retval;
}

private void startDestructionMonitor()
{
	final Runnable jobKiller = new Runnable() {

	    public void run() {
		logger.debug("looking for jobs to destroy");
		List<String> jobIds = executionHistory.getExecutionIdDestructionBefore(new Date());
		for (String jobId : jobIds) {
		    logger.info("destroying records for job="+jobId);
		    try {
			CECControl.this.deleteJob(jobId);
		    } catch (ExecutionIDNotFoundException e) {
			logger.error("internal error - should not happen", e);
		    }
		}
		
	    }
	    
	};
	final ScheduledFuture<?> jobKillerTask = scheduler.scheduleAtFixedRate(jobKiller, policy.getDestroyPeriod(), policy.getDestroyPeriod(), TimeUnit.SECONDS);

}


public String getDescription() {
   return "Provides useful functions for managing the resources consumed by the  runtime environment.";
}

public Test getInstallationTest() {
   return null; //TODO think of a test
}

public String getName() {
    return "CecControl";
}

public void shutdown() {
    logger.info("shutting down job destroyer");
    scheduler.shutdownNow();
   
}

}

/*
 * $Log: CECControl.java,v $
 * Revision 1.3  2008/09/13 09:51:05  pah
 * code cleanup
 *
 * Revision 1.2  2008/09/03 14:18:52  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/05/08 22:40:54  pah
 * basic UWS working
 *
 * Revision 1.1.2.1  2008/04/04 15:46:08  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 * Revision 1.7  2006/03/17 17:50:58  clq2
 * gtr_1489_cea correted version
 *
 * Revision 1.5  2006/03/07 21:45:26  clq2
 * gtr_1489_cea
 *
 * Revision 1.2.20.1  2005/12/19 18:12:30  gtr
 * Refactored: changes in support of the fix for 1492.
 *
 * Revision 1.2  2005/08/10 14:45:37  clq2
 * cea_pah_1317
 *
 * Revision 1.1.2.1  2005/07/21 15:12:06  pah
 * added workfile deletion
 *
 */
