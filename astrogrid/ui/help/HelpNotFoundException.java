/**
 * HelpNotFoundException.java
 *
 * @author M Hill
 */

package org.astrogrid.ui.help;

public class HelpNotFoundException extends Exception
{
   Exception cause;
   
   public HelpNotFoundException(String message, Exception aCause)
   {
      super(message);
      cause = aCause;
   }
   
   public Throwable getCause()
   {
      return cause;
   }
}

