/*
 * $Id: UserCancelledException.java,v 1.1 2003/08/25 18:36:36 mch Exp $
 */

package org.astrogrid.ui;

/**
 * When you've got a ProgressMonitorBox running, the user could press 'Cancel' at
 * any time.  Rather than having lots and lots of nested "If" statements at every single
 * operation, to check if the user has cancelled, instead you can check for
 * if the progressbox iscanceleld and throw an exception, to be caught wherever
 * the progressbox would normally finish.
 * <P>
 * Still looks quite messy in the code, but avoids nesting.  Ideally the progress
 * box should have a way of interrupting the thread being run... or a method
 * that can be called that would throw an exception if it had been cacnelled...
 *
 * @author M Hill
 */

public class UserCancelledException extends RuntimeException
{
   public UserCancelledException()
   {
      super("User Cancelled Operation");
   }
}

