/*
 * @(#)NotFoundException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.job;


/**
 * The <code>JobNotFoundException</code> class represents 
 * the case where a select made against the Job database 
 * retrieved zero Job occurances.
 * <p>
 *
 *
 * @author  Jeff Lusted
 * @version 1.0 03-Jun-2003
 * @see     org.astrogrid.jes.job.JobException
 * @since   AstroGrid 1.2
 */
public class NotFoundException extends JobException {

	/**
	     * 
	     *
	     * 
	     */
	public NotFoundException( String  message ) {
		super(message);
	}


	/**
	     * 
	     * 
	     * 
	     */
	public NotFoundException( String message, Exception exception ) {
		super(message, exception);
	}


} // end of class JobNotFoundException
