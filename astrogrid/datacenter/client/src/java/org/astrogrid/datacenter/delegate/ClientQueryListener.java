/*
 * $Id ConeSearcher.java $
 *
 */

package org.astrogrid.datacenter.delegate;

import java.io.IOException;


/**
 * Defines a client listener (as opposed to a web based listener) that is
 * fed changes through the Client
 *
 * @author M Hill
 */

public interface ClientQueryListener
{
   public void queryStatusChanged(String newStatus) throws IOException;
}

/*
$Log: ClientQueryListener.java,v $
Revision 1.2  2004/11/10 22:01:50  mch
skynode starts and some fixes

Revision 1.1  2004/03/13 01:04:17  mch
It05 Refactor (Client)


*/
