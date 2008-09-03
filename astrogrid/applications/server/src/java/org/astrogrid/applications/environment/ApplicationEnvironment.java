/*
 * $Id: ApplicationEnvironment.java,v 1.2 2008/09/03 14:18:55 pah Exp $
 * 
 * Created on 11 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.environment;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.community.User;

/**
 * The execution environment for a run of an application.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 11 Jun 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class ApplicationEnvironment {
    /**
     * Trivial class to create temporary files for use by the environment. Does this by creating files with numbers as their names.
     * @author Paul Harrison (pah@jb.man.ac.uk)
     * @version $Name:  $
     * @since iteration4
     * */
     
    class TempFileFactory {
       private int filenum = 0;

       
       public File createFile(String name) {
          StringBuffer filename = new StringBuffer(name);
          filename.append("_");
          filename.append(filenum++);
          filename.append(".tmpgen");
          return new File(executionDirectory, filename.toString());
       }

    }
    
    public static final String CEA_OUTPUT_LOG = "cea-output.log";
    public static final String CEA_ERROR_LOG = "cea-error.log";

    public interface WorkingDir {
        File getDir();
    }

    protected static org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(
             ApplicationEnvironment.class);
    protected final File errorLog;
    protected final String executionId;
    protected final String jobStepId;
    protected final File outputLog;
    protected final File executionDirectory;
    protected final TempFileFactory tempFileFactory;
    protected final Configuration config;
    protected final User user;

    public ApplicationEnvironment(String jobStepId, User user,IdGen idgen, Configuration config) throws CannotCreateWorkingDirectoryException, WorkingDirectoryAlreadyExists {
	      executionId = idgen.getNewID();
	      logger.info("new execution id="+executionId);
	      this.jobStepId = jobStepId;
              this.user = user;
	      executionDirectory = createExecutionDirectoryName(executionId, config.getTemporaryFilesDirectory());
	         
	      if (!executionDirectory.exists()) {

	         if (!executionDirectory.mkdir()) {
	            logger.error(
	               " cannot create working directory "
	                  + executionDirectory.getAbsolutePath());
	            throw new CannotCreateWorkingDirectoryException(executionDirectory);
	         }
	      }
	      else {
	         logger.error(
	            "working directory "
	               + executionDirectory.getAbsolutePath()
	               + " already exists");
	            throw new WorkingDirectoryAlreadyExists(executionDirectory.getAbsolutePath());
	      }
	      tempFileFactory = new TempFileFactory();
	      // lets give these meaningful names.
	      errorLog = new File(executionDirectory,CEA_ERROR_LOG); //tempFileFactory.createFile();
	      outputLog = new File(executionDirectory,CEA_OUTPUT_LOG); //tempFileFactory.createFile();
	      this.config = config;
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
    public static File createExecutionDirectoryName(String exid, File wd) {
          return new File(wd,URLEncoder.encode(exid));
       }

    public String getJobStepId() {
        return jobStepId;
    }

    public User getUser() {
        return user;
    }

}


/*
 * $Log: ApplicationEnvironment.java,v $
 * Revision 1.2  2008/09/03 14:18:55  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.4  2008/09/03 12:22:55  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1.2.3  2008/08/29 07:28:31  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.2  2008/08/02 13:33:56  pah
 * safety checkin - on vacation
 *
 * Revision 1.1.2.1  2008/06/11 14:31:43  pah
 * merged the ids into the application execution environment
 *
 */
