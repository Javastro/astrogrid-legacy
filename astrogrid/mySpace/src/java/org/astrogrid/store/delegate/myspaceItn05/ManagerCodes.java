package org.astrogrid.myspace.delegate;

/**
 * ManagerCodes.java
 *
 * <p>
 * The <code>ManagerCodes</code> class specifies the permitted values
 * for flags occurring as arguments in the <code>Manager</code>
 * interface.
 * </p>
 */

public interface ManagerCodes
{
//
//If the putString, putBytes or putUri methods attempt to import a file
//using a name which already exists, the following flags indicate how 
//the existing entry is to be dispatched.

/**
 * Leave existing entry untouched and report error.
 */
   public static final int LEAVE = 1;

/**
 * Overwrite the existing entry with the new one.
 */
   public static final int OVERWRITE = 2;

/**
 * Append the new entry to the old one.
 */
   public static final int APPEND = 3;

}
