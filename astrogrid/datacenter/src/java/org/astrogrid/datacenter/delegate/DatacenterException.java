/*
 * $Id DatacenterException.java $
 *
 */

package org.astrogrid.datacenter.delegate;

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
Revision 1.2  2003/11/13 16:11:32  mch
minor import change testing CVS in Eclipse

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/
