/*
 * @(#)JobFactory.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.job;

import org.astrogrid.datacenter.FactoryProvider;
import org.astrogrid.datacenter.config.Configurable;
import org.w3c.dom.Document;

/**
 * <code>JobFactory</code> is the interface that all implementations
 * of JobFactory must support.
 * <p>
 * All persistence considerations concerning Job are hidden behind
 * this interface. That means inserting, updating, finding and 
 * deleting in, for example, a suitable RDBMS.
 * <p>
 *
 * @author  Jeff Lusted
 * @version 1.0 08-Jun-2003
 * @see     org.astrogrid.JobFactoryImpl
 * @since   AstroGrid 1.2
 */
public interface JobFactory  extends Configurable {
	
//
//	JBL Note: The following are still under consideration...
//	 Job create( String jobURN ) ;
//	 String delete( String jobURN ) ;
	
/**
   * <p>
   * Creates a new and unique job in the Job database.
   * <p>
   * At present a Job is created (inserted into the Job database) 
   * by passing the request document to this method.
   * All associated objects (e.g. @see org.astrogrid.JobStep) are
   * chained from Job creation, and therefore this step of
   * Job creation needs all the associated information to pass on.
   * 
   * @param jobDoc - the complete request document
   * @return  - newly created job instance.
   * @exception - org.astrogrid.JobException
   */
	Job create( Document jobDoc , FactoryProvider facMan) throws JobException ;


	/**
	   * <p>
	   * Updates a job instance in the Job database.
	   * <p>
	   * 
	   * @param job - the instance whose values are to be written
	   *        back to the database.
	   * @return  void
	   * @exception - org.astrogrid.JobException
	   */	
    void update( Job job ) throws JobException ;
   
   
	/**
	   * <p>
	   * Finds a job instance in the Job database.
	   * <p>
	   * 
	   * @param jobURN - the job id.
	   * @return  the job instance.
	   * @exception - org.astrogrid.JobException
	   */	 
    Job find( String jobURN ) throws JobException ;
  
    
	/**
	   * <p>
	   * Deletes a job instance from the Job database.
	   * <p>
	   * 
	   * @param job - the job instance.
	   * @return  the jobURN of the deleted job.
	   * @exception - org.astrogrid.JobException
	   */	     
	String delete( Job job ) throws JobException ;
    
    
} // end of interface JobFactory
