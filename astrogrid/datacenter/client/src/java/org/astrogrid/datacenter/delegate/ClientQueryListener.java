/*
 * $Id ConeSearcher.java $
 *
 */

package org.astrogrid.datacenter.delegate;

import java.io.IOException;
import java.io.InputStream;


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
Revision 1.1  2004/03/13 01:04:17  mch
It05 Refactor (Client)


*/
