/*
 * @(#)DuplicateFoundException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.job;

import org.astrogrid.i18n.* ;

/**
 * The <code>DuplicateJobFoundException</code> class represents 
 * the case where an expected unique select was made against
 * the Job database and more than a singleton Job was retrieved.
 *
 * @author  Jeff Lusted
 * @version 1.0 03-Jun-2003
 * @see     org.astrogrid.jes.job.JobException
 * @since   AstroGrid 1.2
 */
public class DuplicateFoundException extends JobException {

	/**
	     * 
	     * 
	     */
	public DuplicateFoundException(AstroGridMessage message) {
		super(message);
	}

	/**
	     * 
	     * 
	     */
	public DuplicateFoundException(AstroGridMessage message, Exception exception) {
		super(message, exception);
	}

}
