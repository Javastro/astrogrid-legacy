/*
   IllegalUcdException.java

   Date       Author      Changes
   1 Oct 2002 M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.ucd;


/**
 * IllegalUcdException is thrown when a string is not a valid UCD.  Needs
 * some expanding to expose notUcd.
 *
 * @version %I%
 * @author M Hill
 */
   
public class IllegalUcdException extends Exception
{
   private String notUcd = null;
   private String msg = null;
   
   /** Constructor */
   public IllegalUcdException(String givenUcd, String givenMsg)
   {
      this.notUcd = givenUcd;
      this.msg = givenMsg;
   }
   
}

