/*
 * $Id ConeSearcher.java $
 *
 */

package org.astrogrid.datacenter.delegate;

import java.io.IOException;


/**
 * Defines the core activities that can be performed on any datacenter
 *
 * @author M Hill
 */

public interface DatacenterClient
{
   /** Sets the timeout after which the client throws an exception, in milliseconds */
   public void setTimeout(int millis);
   
   public String getMetadata() throws IOException;
   
   public String getStatus(String id) throws IOException;
}

/*
$Log: DatacenterClient.java,v $
Revision 1.3  2004/11/10 22:01:50  mch
skynode starts and some fixes

Revision 1.2  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.1.120.1  2004/10/29 17:55:51  mch
Added comment

Revision 1.1  2004/03/12 20:00:11  mch
It05 Refactor (Client)


*/
