/*
 * $Id: DefaultExecutionController.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 *
 * Created on 13 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.PersistenceException;
import org.astrogrid.community.User;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.workflow.beans.v1.Tool;

import java.util.Observable;
import java.util.Observer;

import junit.framework.Test;

/**
 * Default implementation of the execution controller.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */ 
public class DefaultExecutionController
   implements ExecutionController, Observer, ComponentDescriptor{

   static final protected org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(DefaultExecutionController.class);
   /**
    * The store for the descriptions of the applications that this application controller manages.
    */
   protected final ApplicationDescriptionLibrary applicationDescriptions;
   protected final ExecutionHistory executionHistory;
    /**
     *  Construct a new DefaultExecutionController
     * @param library collection of available appliation descriptions
     * @param executionHistory store of applications currently running, and archive of previous executions.
     */
   public DefaultExecutionController(final ApplicationDescriptionLibrary library, ExecutionHistory executionHistory ) {
      logger.info("initializing application controller");
      this.applicationDescriptions = library;

      this.executionHistory = executionHistory;
   }
public  boolean execute(String executionId) throws CeaException {
      boolean success = false;

      if (executionHistory.isApplicationInCurrentSet(executionId)) {
        Application app =
            (Application)executionHistory.getApplicationFromCurrentSet(executionId);
         success = app.execute();
      }

      return success;

   }
/** 
    * @see org.astrogrid.applications.manager.CommonExecutionController#execute(org.astrogrid.workflow.beans.v1.Tool, java.lang.String, java.lang.String)
    */
public String init(Tool tool, String jobstepID) throws CeaException {

      int idx;
      String toolname = tool.getName();
      /* now accept full authorityId-qualified tool names.
      if((idx = toolname.indexOf("/")) != -1){      
          toolname = toolname.substring(idx+1); 
      } 
      */     
      try {
          ApplicationDescription descr = applicationDescriptions.getDescription(toolname);
          User user = new User(); //TODO this needs to be obtained from the context
          Application app = descr.initializeApplication(jobstepID,user,tool);
          executionHistory.addApplicationToCurrentSet(app);          
          app.addObserver(this);
          return app.getID();
      } catch (CeaException e) {
          throw e;
      } catch (Exception e) {
          logger.error("Could not execute " + toolname,e);
          throw new CeaException("Could not execute " + toolname,e);
      }
   }

   public boolean abort(String executionId) {
       if(!   executionHistory.isApplicationInCurrentSet(executionId)) {
           return false; // its already dead.
       }
       try {
        Application app = executionHistory.getApplicationFromCurrentSet(executionId);
        return app.attemptAbort();
       } catch (PersistenceException e) {
           // oh well
           logger.error("Could not abort application, due to persistence error",e);
           return false;
       }
   }

 /** This component is itself an observer. it watches all applications, move them to the archive once they've completed.
  * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
  */
 public void update(Observable o, Object arg) {
     if (! (o instanceof Application && arg instanceof Status)) {
         return;
     }
     Status stat = (Status)arg;
     if (stat.equals(Status.COMPLETED) || stat.equals(Status.ERROR)) {
         Application app = (Application)o;
        try {
            executionHistory.moveApplicationFromCurrentSetToArchive(app.getID());
        } catch (PersistenceException e) { // oh well
              logger.error("Could not move application status to archive " + app.getID(),e);
        }
     }
   }
/**
 * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
 */
public String getName() {
    return "Default Common Execution Controller";
}
/**
 * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
 */
public String getDescription() {
    return "";
}
/**
 * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
 */
public Test getInstallationTest() {
    return null;
}








}
