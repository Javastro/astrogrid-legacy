/*
 * $Id: ApplicationEnvironment.java,v 1.3 2003/12/05 22:52:16 pah Exp $
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

import com.sun.enterprise.deployment.Application;

import org.astrogrid.applications.ApplicationFactory;
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
   private ApplicationDescriptions applDescriptions;
   private File errorLog;
   private int executionId;
   private File outputLog;
   private File executionDirectory;
   private CmdLineApplication cmdLineApplication;
   private String[] args;
   
   public ApplicationEnvironment(String applicationId, ApplicationDescriptions applicationDescriptions)
   {
      // create the application object
      applDescriptions = applicationDescriptions;
      ApplicationFactory factory = CmdLineApplicationCreator.getInstance(applicationDescriptions);
      cmdLineApplication = (CmdLineApplication)factory.createApplication(applicationId);
      
      PersistenceEngine.getNewID();
   }
   
   
   
}
