/*
 * $Id: CommandLineCECControl.java,v 1.2 2005/08/10 14:45:37 clq2 Exp $
 * 
 * Created on 19-Jul-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.commandline.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.astrogrid.applications.commandline.CommandLineApplicationEnvironment;
import org.astrogrid.applications.commandline.CommandLineApplicationEnvironment.WorkingDir;
import org.astrogrid.applications.manager.ControlService;

/**
 * Provides useful functions for managing the resources consumed by the
 * commandline runtime environment.
 * 
 * @author Paul Harrison (pharriso@eso.org) 19-Jul-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class CommandLineCECControl implements ControlService {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(CommandLineCECControl.class);
   private WorkingDir wd;

 
   //FIXME just need the working directory for this....
   public CommandLineCECControl(CommandLineApplicationEnvironment.WorkingDir wd) {
      logger.info("Creating commandline control with working directory="+wd.getDir().getAbsolutePath());
      this.wd = wd;
   }

   /**
    * Deletes any temporary files associated with execution of applications. The time before which files should be deleted is 
    * expressed as a delta time from the current time.
    * @param days The delta time in days before which the files should be deleted. Should be positive, e.g. a value of 2 means delete files which are nore than 3 days old.
    * @return
    */
   public String deleteOldRuntimeWorkFiles(int days) {
      StringBuffer result = new StringBuffer();
      int delcount = 0, failedcount = 0, bytesdeleted = 0;
      File baseDir = wd.getDir();
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

}

/*
 * $Log: CommandLineCECControl.java,v $
 * Revision 1.2  2005/08/10 14:45:37  clq2
 * cea_pah_1317
 *
 * Revision 1.1.2.1  2005/07/21 15:12:06  pah
 * added workfile deletion
 *
 */
