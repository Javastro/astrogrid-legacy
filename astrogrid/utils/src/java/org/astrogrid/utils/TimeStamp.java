/*$Id: TimeStamp.java,v 1.1 2005/03/22 13:00:41 mch Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.utils;


 /**
 * Timer - simple utility class for marking time passed
 * @author           : M Hill
 *
 * Creates a date instance (with time now) on creation, and user can then call
 * 'getSecs' to get seconds since the instantiation time. Add more methods as
 * required.
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

/*
$Log: TimeStamp.java,v $
Revision 1.1  2005/03/22 13:00:41  mch
Seperated utils from common

Revision 1.1  2004/10/01 17:52:18  mch
Added simple timestamp

Revision 1.1  2004/05/25 13:35:05  mch
Trivial timestamp utility

 */




