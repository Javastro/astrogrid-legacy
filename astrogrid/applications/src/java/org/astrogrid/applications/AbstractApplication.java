/*
 * $Id: AbstractApplication.java,v 1.6 2003/12/12 21:30:46 pah Exp $
 *
 * Created on 13 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

import org.astrogrid.applications.commandline.exceptions.ApplicationExecutionException;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import java.util.List;

public class AbstractApplication implements Application {
   /**
    *@link aggregation
    *@associates org.astrogrid.applications.Parameter
    */
   protected List parameters; 
   protected ApplicationDescription applicationDescription;
   
   public AbstractApplication()
   {
      parameters = new ArrayList();
   }
   
   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#completionStatus()
    */
   public int completionStatus() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("AbstractApplication.completionStatus() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#execute()
    */
   public boolean execute() throws ApplicationExecutionException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("AbstractApplication.execute() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#retrieveResult()
    */
   public Result[] retrieveResult() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("AbstractApplication.retrieveResult() not implemented");
   }

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
}
