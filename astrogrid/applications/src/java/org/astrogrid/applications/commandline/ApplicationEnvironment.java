/*
 * $Id: ApplicationEnvironment.java,v 1.4 2003/12/07 01:09:48 pah Exp $
 *
 * Created on 24 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline;

import java.io.File;

import org.astrogrid.applications.ApplicationFactory;
import org.astrogrid.applications.commandline.exceptions.CannotCreateWorkingDirectoryException;
import org.astrogrid.applications.common.config.ApplicationControllerConfig;
import org.astrogrid.applications.description.ApplicationDescriptions;
import org.astrogrid.applications.manager.PersistenceEngine;
public class ApplicationEnvironment {
   /**
    * Trivial class to create temporary files for use by the environment. Does this by creating files with numbers as their names.
    * @author Paul Harrison (pah@jb.man.ac.uk)
    * @version $Name:  $
    * @since iteration4
    */
   private class TempFileFactory {
      private int filenum = 0;
      
      public File createFile()
      {
         StringBuffer filename = new StringBuffer();
         filename.append(filenum++);
         filename.append(".tmpgen");
         return new File(executionDirectory,filename.toString()); 
      }

   }
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(
         ApplicationEnvironment.class);
   private File errorLog;
   private int executionId;
   private File outputLog;
   private File executionDirectory;
   private ApplicationControllerConfig config;
   private TempFileFactory tempFileFactory;
   
   public ApplicationEnvironment() throws CannotCreateWorkingDirectoryException
   {
      
      // set up the working directory
      config = ApplicationControllerConfig.getInstance();
      
      executionId = PersistenceEngine.getInstance().getNewID();
      executionDirectory = new File(config.getWorkingDirectory(),Integer.toString(executionId)+"/");
      if(!executionDirectory.mkdir())
      {
         logger.error("cannot create working directory");
         throw new CannotCreateWorkingDirectoryException(executionDirectory);
      }
      tempFileFactory = new TempFileFactory();
      errorLog = tempFileFactory.createFile();
      outputLog = tempFileFactory.createFile();
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

   /**
    * @return
    */
   public int getExecutionId() {
      return executionId;
   }

   /**
    * @return
    */
   public File getOutputLog() {
      return outputLog;
   }

}
