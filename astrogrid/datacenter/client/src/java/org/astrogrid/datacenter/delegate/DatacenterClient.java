/*
 * $Id ConeSearcher.java $
 *
 */

package org.astrogrid.datacenter.delegate;

import java.io.IOException;
import java.io.InputStream;


/**
 * Defines the core activities that can be performed on any datacenter
 *
 * @author M Hill
 */

public interface DatacenterClient
{
   public void setTimeout(int newTimeout);
   
   public String getMetadata() throws IOException;
   
   public String getStatus(String id) throws IOException;
}

/*
$Log: DatacenterClient.java,v $
Revision 1.1  2004/03/12 20:00:11  mch
It05 Refactor (Client)


*/
