/*
 * @(#)name.java	1.2 
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.jes.job;

import org.astrogrid.jes.i18n.Message;

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
	public NotFoundException( Message message ) {
		super(message);
	}


	/**
	     * 
	     * 
	     * 
	     */
	public NotFoundException( Message message, Exception exception ) {
		super(message, exception);
	}


} // end of class JobNotFoundException
