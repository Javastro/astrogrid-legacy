/*
 * @(#)name.java	1.2 
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.jes.job;

import org.astrogrid.jes.i18n.Message;

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
public class DuplicateJobFoundException extends JobException {

	/**
	     * 
	     * 
	     */
	public DuplicateJobFoundException(Message message) {
		super(message);
	}

	/**
	     * 
	     * 
	     */
	public DuplicateJobFoundException(Message message, Exception exception) {
		super(message, exception);
	}

}
