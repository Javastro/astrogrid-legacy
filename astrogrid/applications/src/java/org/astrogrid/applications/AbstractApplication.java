/*
 * $Id: AbstractApplication.java,v 1.7 2003/12/31 00:56:17 pah Exp $
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

import org.astrogrid.applications.commandline.exceptions.ApplicationExecutionException;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.applications.manager.ApplicationController;
import org.astrogrid.community.User;

import java.util.List;

public abstract class AbstractApplication implements Application {
   /**
    *@link aggregation
    *@associates org.astrogrid.applications.Parameter
    */
   protected List parameters; 
   protected AbstractApplicationController controller;
   protected ApplicationDescription applicationDescription;
   protected User user;
   
   public AbstractApplication(AbstractApplicationController controller, User user)
   {
      this.controller = controller;
      this.user = user;
      parameters = new ArrayList();
   }
   
   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#completionStatus()
    */
   public abstract int completionStatus();
   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#execute()
    */
   public abstract boolean execute() throws ApplicationExecutionException;

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

   private ApplicationInterface applicationInterface;

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

}

