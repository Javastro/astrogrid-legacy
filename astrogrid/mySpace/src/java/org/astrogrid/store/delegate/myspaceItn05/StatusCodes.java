package org.astrogrid.myspace.delegate;

/**
 * <p>
 * The <code>StatusCodes</code> class defines the values for the codes
 * used in the <code>StatusMessage</code> and <code>StatusResults</code>
 * classes.  In practice the variables correspond to the various values
 * for the severity level.
 * </p>
 * 
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */

public class StatusCodes
{

//
//Public constants defining the permitted codes for the severity level.

   public static final int INFO  = 1;  // Information (ie. nothing amiss).
   public static final int WARN  = 2;  // Warning.
   public static final int ERROR = 3;  // Error.
}
