/*
 * $Id: AbstractApplication.java,v 1.2 2003/11/13 22:44:14 pah Exp $
 *
 * Created on 13 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import java.util.Vector;
public class AbstractApplication implements Application {
   /**
    *@link aggregation
    *@associates org.astrogrid.applications.Parameter
    */
   protected Vector parameters;
   
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
   public void execute() {
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
   public void setParameter() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("AbstractApplication.setParameter() not implemented");
   }

}
