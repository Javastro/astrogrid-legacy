/*
 * $Id: HtmlDelegate.java,v 1.2 2003/09/10 13:04:17 nw Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.delegate.axisdataserver.*;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.common.ServiceStatus;
import org.w3c.dom.Element;

/**
 * A standard AstroGrid datacenter delegate implementation.
 *
 * @see DatacenterDelegate
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class HtmlDelegate extends DatacenterDelegate
{
   private URL endpoint = null;
   AxisDataServerSoapBindingStub binding;

   /** Don't use this directly - use the factory method
    * DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public HtmlDelegate(URL givenEndPoint) throws MalformedURLException, ServiceException
   {
      this.endpoint = givenEndPoint;

      binding =(AxisDataServerSoapBindingStub) new AxisDataServerServiceLocator().getAxisDataServer( endpoint );
   }

   /**
    * Sets the timeout for calling the service - ie how long after the initial call
    * is made before a timeout exception is thrown
    */
   public void setTimeout(int givenTimeout)
   {
      binding.setTimeout(givenTimeout);
   }


  /**
    * General purpose query database; pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * results part of the returned document, which may be VOTable or otherwise
    * depending on the results format specified in the ADQL
    * @todo - seems bad to have to hide these exceptions to fit in with interface - can we loosen interface instead.
    * or even get rid of it - what does it give us over the machine generated AxisDataServerSoapBindingStub ?
    */
   public Element adqlQuery(Element adql) throws RemoteException
   {
       try {
       return binding.doQuery(adql);
       } catch (QueryException e) {
           throw new RemoteException(e.getMessage());
       }

   }

   public Element getResults(String id) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public Element spawnAdqlQuery(Element adql) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }


   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public int adqlCountDatacenter(Element adql)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * returns metadata (an XML document describing the data the
    * center serves) in the form required by registries. See the VOResource
    * schema; I think that is what this should return...
    */
   public Element getRegistryMetadata() throws RemoteException
   {
     return binding.getVoRegistryMetadata();
   }

   /**
    * Polls the service and asks for the current status
    */
   public ServiceStatus getServiceStatus(String id)
   {
      return ServiceStatus.UNKNOWN;
   }

   /**
    * Register a remote listener with the server
    */
   public void registerWebListener(URL listenerUrl)
   {
      //need to send url to service
      throw new UnsupportedOperationException("Not implemented yet");
   }


}

/*
$Log: HtmlDelegate.java,v $
Revision 1.2  2003/09/10 13:04:17  nw
updated to work with new wsdl-generated classes
- changed imports, changed names of methods, etc.

Revision 1.1  2003/09/09 17:50:07  mch
Class renames, configuration key fixes, registry/metadata methods and spawning query methods

Revision 1.4  2003/09/08 16:34:04  mch
Added documentation

Revision 1.3  2003/09/07 18:51:12  mch
Added typesafe ServiceStatus

Revision 1.2  2003/08/31 15:23:08  mch
Removed unused listeners

Revision 1.1  2003/08/27 23:30:10  mch
Introduced DummyDatacenterDelegate, selfcontained package for other workgroups to test with

Revision 1.3  2003/08/27 22:40:55  mch
removed unnecessary import (maven report...!)

Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:28  mch
initial checkin


*/

