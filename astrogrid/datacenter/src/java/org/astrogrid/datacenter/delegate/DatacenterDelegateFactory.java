/*
 * $Id: DatacenterDelegateFactory.java,v 1.1 2003/10/06 18:55:21 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.rpc.ServiceException;
import org.astrogrid.datacenter.delegate.agss.SocketDelegate;
import org.astrogrid.datacenter.delegate.agws.WebDelegate;
import org.astrogrid.datacenter.delegate.dummy.DummyDelegate;
import org.astrogrid.datacenter.delegate.nvocone.NvoConeSearchDelegate;

/**
 * Constructs the right delegate for the given service
 *
 * @author M Hill
 */

public class DatacenterDelegateFactory
{
  
   /** Creates a ConeSearcher-implementing delegate given an endpoint
    * (a url to the service). If the endPoint
    * is null, creates a dummy delegate that can be used to test against, which
    * does not need access to any datacenter servers.
    * @todo make CatalogDelegate implement ConeSearcher and add here
    */
   public static ConeSearcher makeConeSearcher(String givenEndPoint)
         throws MalformedURLException, DatacenterException
   {
      //there are three server types -> three delegate implementations - dummies,
      // nvo cone-search servers, and astrogrid datacenter servers (agds).
      // The urls of nvo cone-search servers and adql servers may be similar.
      // only adql servers return metadata.
      
      if (givenEndPoint == null)
      {
         //easy one, return a dummy
         return new DummyDelegate();
      }

      //if the url includes ?CAT it's an nvo-server
      if (givenEndPoint.indexOf("?CAT=") >-1)
      {
         return new NvoConeSearchDelegate(givenEndPoint);
      }

      //pants.  Lets make a CatalogDelegate and if it fails (as it makes
      // the connection), assume we got it wrong and make a ConeSearch one...
      /* haven't implemented a ConeSearcher-compatible catalogdelegate yet
      try
      {
         return (ConeSearcher) makeCatalogDelegate(givenEndPoint);
      }
      catch (ServiceException se){ } //couldn't connect - never mind
      catch (ClassCastException se)
       {
      //delegate for that server type does not implement ConeSearcher
      throw new DatacenterException("Cannot provide cone search to service at "+givenEndPoint+".  Try another query type");
       }
       */
      
      //resort to a conesearch delegate
      return new NvoConeSearchDelegate(givenEndPoint);
   }


   /** Creates an adql-implementing delegate given an endpoint
    * (a url to the service). If the endPoint
    * is null, creates a dummy delegate that can be used to test against, which
    * does not need access to any datacenter servers.
    */
   public static AdqlQuerier makeAdqlQuerier(String givenEndPoint) throws ServiceException, MalformedURLException, IOException
   {
      if (givenEndPoint == null)
      {
         //easy one, return a dummy
         return new DummyDelegate();
      }

      //at some point, we ought to be able to make an AdqlQuerier-implementation
      //that runs on conesearches....
      /* but not yet
      if (givenEndPoint represents a cone search)
      {
         return new NvoConeSearchDelegate(givenEndPoint);
      }
       */

      if (givenEndPoint.startsWith("socket")) {
         return new SocketDelegate(givenEndPoint, null);
      }
      
      return new WebDelegate(new URL(givenEndPoint));
   }
   
}

/*
$Log: DatacenterDelegateFactory.java,v $
Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates



*/


