/*
 * $Id DatacenterException.java $
 *
 */

package org.astrogrid.dataservice;

import java.rmi.RemoteException;

/**
 * Exception thrown by datacenter delegates.  Extends RemoteException so that
 * we don't have to do extra catches/conversions for them...
 *
 * @author M Hill
 */


public class DatacenterException extends RemoteException
{
   public DatacenterException(String message)
   {
      super(message);
   }

   public DatacenterException(String message, Throwable cause)
   {
      super(message, cause);
   }
}

/*
$Log: DatacenterException.java,v $
Revision 1.1  2009/05/13 13:20:23  gtr
*** empty log message ***

Revision 1.2  2005/02/28 19:36:39  mch
Fixes to tests


*/
