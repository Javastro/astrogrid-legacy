/*
 * $Id: CommandLineApplicationEnvironment.java,v 1.6 2006/01/10 11:26:51 clq2 Exp $
 *
 * Created on 24 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline;

import org.astrogrid.applications.commandline.exceptions.CannotCreateWorkingDirectoryException;
import org.astrogrid.applications.manager.idgen.IdGen;

import java.io.File;
import java.net.URLEncoder;
/**
 * Encapsulates all that is needed to run a command line application in its own workspace.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CommandLineApplicationEnvironment {
    public static final String CEA_OUTPUT_LOG = "cea-output.log";
   public static final String CEA_ERROR_LOG = "cea-error.log";
   public interface WorkingDir {
        File getDir();
    }
   /**
    * Trivial class to create temporary files for use by the environment. Does this by creating files with numbers as their names.
    * @author Paul Harrison (pah@jb.man.ac.uk)
    * @version $Name:  $
    * @since iteration4
    * */
    
   private class TempFileFactory {
      private int filenum = 0;

      
      public File createFile(String name) {
         StringBuffer filename = new StringBuffer(name);
         filename.append("_");
         filename.append(filenum++);
         filename.append(".tmpgen");
         return new File(executionDirectory, filename.toString());
      }

   }
   
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(
         CommandLineApplicationEnvironment.class);
   private final File errorLog;
   private final String executionId;
   private final File outputLog;
   private final File executionDirectory;
   private final TempFileFactory tempFileFactory;


   public CommandLineApplicationEnvironment(final IdGen idgen, final WorkingDir workingDirectory)
      throws CannotCreateWorkingDirectoryException {

      
      executionId = idgen.getNewID();
      logger.info("new execution id="+executionId);
      executionDirectory = createExecutionDirectoryName(executionId, workingDirectory);
         
      if (!executionDirectory.exists()) {

         if (!executionDirectory.mkdir()) {
            logger.error(
               " cannot create working directory "
                  + executionDirectory.getAbsolutePath());
            throw new CannotCreateWorkingDirectoryException(executionDirectory);
         }
      }
      else {
         logger.warn(
            "working directory "
               + executionDirectory.getAbsolutePath()
               + " already exists");
         //TODO need to decide if this is a failure condition...
      }
      tempFileFactory = new TempFileFactory();
      // lets give these meaningful names.
      errorLog = new File(executionDirectory,CEA_ERROR_LOG); //tempFileFactory.createFile();
      outputLog = new File(executionDirectory,CEA_OUTPUT_LOG); //tempFileFactory.createFile();
   }

   /**
    * @return
    */
   public File getErrorLog() {
      return errorLog;
   }

   /**
    * @return
    */
   public File getExecutionDirectory() {
      return executionDirectory;
   }
   
   public File getTempFile(String name) {
       return tempFileFactory.createFile(name); 
   }

   /**
    * @return
    */
   public String getExecutionId() {
      return executionId;
   }

   /**
    * @return
    */
   public File getOutputLog() {
      return outputLog;
   }
   /**
    * Create the unique execution directory name from the executionId and working directory.
    * @param exid
    * @param wd
    * @return the execution directory name - note that it is not created.
    */
   public static File createExecutionDirectoryName(String exid, WorkingDir wd){
      return new File(wd.getDir(),URLEncoder.encode(exid));
   }


}
