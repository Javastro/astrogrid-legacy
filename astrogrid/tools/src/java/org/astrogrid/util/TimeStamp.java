package org.astrogrid.tools.util;

 /**
 * Timer - simple utility class for marking time passed
 * @author           : M Hill
 *
 * Creates a date instance (with time now) on creation, and user can then call
 * 'getSecs' to get seconds since the instantiation time.
 */

import java.util.Date;

public class TimeStamp extends Date
{
   /**
    * Returns the number of seconds since the timestamp
    */
   public long getSecsSince()
   {
      Date nowTime = new Date();
      return (nowTime.getTime() - getTime()) / 1000;
   }
}

