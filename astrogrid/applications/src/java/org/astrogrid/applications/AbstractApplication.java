/*
 * $Id: AbstractApplication.java,v 1.12 2004/03/23 12:51:25 pah Exp $
 *
 * Created on 13 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.community.User;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public abstract class AbstractApplication implements Application {
   /**
    *@link aggregation
    *@associates org.astrogrid.applications.Parameter
    */
   protected List parameters; 
   
   /**
    * The application controller that is running this application
    */
   protected AbstractApplicationController controller;
   /**
    * The description of this application
    */
   protected ApplicationDescription applicationDescription;
   /**
    * The user context that this application is running in
    */
   protected User user;
   /**
    * the application status
    */
   protected Status status = Status.NEW;
   //REFACTORME - probably the jobstep paramters are really a application controller properties.
   
   /**
    * the endpoint for the jobmonitor service to conctact to inform when this application has finished 
    */
   protected String jobMonitorURL;
   protected String jobStepID;
   
   public AbstractApplication(AbstractApplicationController controller, User user)
   {
      this();
      this.controller = controller;
      this.user = user;
   }
   /**
    * This constructor is only here to allow a public constructor for derived classes, so that they can use Class.newInstance()
    *
    */
   protected AbstractApplication()
   {
      parameters = new ArrayList();
      
   }
   
   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#completionStatus()
    */
   public abstract int completionStatus();
   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#execute()
    */
   public abstract boolean execute() throws CeaException;

   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#retrieveResult()
    */
   public abstract Result[] retrieveResult();
 
   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#setParameter()
    */
   public void addParameter(Parameter p) {
      parameters.add(p);
   }
   
   public Parameter[] getParameters()
   {
      return (Parameter[])parameters.toArray(new Parameter[0]);
   }

   protected ApplicationInterface applicationInterface;

   public ApplicationInterface getApplicationInterface(){ return applicationInterface; }

   public void setApplicationInterface(ApplicationInterface applicationInterface){ this.applicationInterface = applicationInterface; }

   /**
    * @return
    */
   public ApplicationDescription getApplicationDescription() {
      return applicationDescription;
   }

   /**
    * @param description
    */
   public void setApplicationDescription(ApplicationDescription description) {
      applicationDescription = description;
   }

   public String toString() {
      return applicationDescription.getName();
   }
   
   public abstract File createLocalTempFile();
   /**
    * @return
    */
   public AbstractApplicationController getController() {
      return controller;
   }

   /**
    * @return
    */
   public User getUser() {
      return user;
   }

   /**
    * @return
    */
   public String getJobMonitorURL() {
      return jobMonitorURL;
   }

   /**
    * @param string
    */
   public void setJobMonitorURL(String string) {
      jobMonitorURL = string;
   }

   /**
    * @return
    */
   public String getJobStepID() {
      return jobStepID;
   }

   /**
    * @param string
    */
   public void setJobStepID(String string) {
      jobStepID = string;
   }

   /**
    * @return
    */
   public Status getStatus() {
      return status;
   }

   /**
    * @param status
    */
   public void setStatus(Status status) {
      this.status = status;
   }


   protected Parameter findParameter(String name)
   {
      //REFACTORME should really do this with a map that is created when each of the parameters is added
      Parameter retval = null;
      for (Iterator iter = parameters.iterator(); iter.hasNext();) {
         Parameter element = (Parameter)iter.next();
         if(element.getName().equals(name))
         {
            retval = element;
            break;
         }
         
      }
      
      return retval;
   }
}

