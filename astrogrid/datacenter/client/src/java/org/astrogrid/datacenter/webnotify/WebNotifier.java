/*
 * $Id WebNotifier.java $
 *
 */

package org.astrogrid.datacenter.webnotify;


/**
 *
 * @author M Hill
 */

public class WebNotifier
{
   private String endPoint = null;
   
   public WebNotifier(String givenEndPoint)
   {
      this.endPoint = givenEndPoint;
   }
   
   public void notifyServer()
   {
   }
   
}

/*
$Log: WebNotifier.java,v $
Revision 1.1  2003/11/17 20:47:57  mch
Adding Adql-like access to Nvo cone searches

*/
