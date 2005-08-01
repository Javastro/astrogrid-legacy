/*$Id: TimeStamp.java,v 1.2 2005/08/01 08:15:52 clq2 Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.datacenter.integration;


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

/*
$Log: TimeStamp.java,v $
Revision 1.2  2005/08/01 08:15:52  clq2
Kmb 1293/1279/intTest1 FS/FM/Jes/Portal/IntTests

Revision 1.1.2.1  2005/07/12 11:21:06  KevinBenson
old datacenter moved out of the test area

Revision 1.1  2004/05/25 13:35:05  mch
Trivial timestamp utility

 */




