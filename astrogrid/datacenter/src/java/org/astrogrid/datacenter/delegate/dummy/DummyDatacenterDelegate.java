/*
 * $Id: DummyDatacenterDelegate.java,v 1.5 2003/09/07 18:52:39 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.dummy;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.delegate.DatacenterStatusListener;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.servicestatus.ServiceStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
/**
 * A convenience class for java clients of datacenters.  They can create and
 * use this class to manage all the connections/calls/etc without having to
 * mess around with all the SOAP messages directly.
 *
 * An instance of one of these corresponds to one connection to one database
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class DummyDatacenterDelegate extends DatacenterDelegate
{
   /** used for generating random results... */
   protected static java.util.Random random = new java.util.Random();

   /** Don't use this directly - use the factory method DatacenterDelegate.makeDelegate()
    * so that it can make decisions on new sorts
    * of datacenter delegates in the future...
    */
   public DummyDatacenterDelegate()
   {
   }

   /**
    * Dummy call does nothing
    */
   public void setTimeout(int givenTimeout)
   {
      //nothing needs done
   }

   /**
    * Checks that the given ADQL is valid (makes a query from it), returns an
    * example VOTable in this package
    * @todo returning a VOTable is not quite right - need to return the DOM
    * representation of the returning message which might contain admin info also
    */
   public Element adqlQueryDatacenter(Element adql) throws RemoteException
   {
      fireStatusChanged(ServiceStatus.STARTING);

      fireStatusChanged(ServiceStatus.RUNNING_QUERY);

      //normally the given adql would be validated at the server, so we throw
      //a special runtimeexception here if it's wrong
      try
      {
         Query query = new Query(adql);
      }
      catch (QueryException e)
      {
         //rethrow as runtime exception - somethings gone wrong that shouldn't
         throw new RuntimeException("Query='"+adql+"',",e);
      }

      fireStatusChanged(ServiceStatus.RUNNING_RESULTS);

      try
      {
         //load example response votable
         URL url = getClass().getResource("ExampleVotable.xml");
         Document resultsDoc = XMLUtils.newDocument(url.openConnection().getInputStream());

         fireStatusChanged(ServiceStatus.FINISHED);

         return resultsDoc.getDocumentElement();

      }
      catch (SAXException se)
      {
         //rethrow as runtime exception - somethings gone wrong that shouldn't
         throw new RuntimeException(se);
      }
      catch (IOException ioe)
      {
         //rethrow as runtime exception - somethings gone wrong that shouldn't
         throw new RuntimeException(ioe);
      }
      catch (ParserConfigurationException pce)
      {
         //should never happen, so rethrow as runtime (is this naughty?)
         throw new RuntimeException(pce);
      }


   }

   /**
    * Returns a random number between 0 and 100...
    */
   public int adqlCountDatacenter(Element adql)
   {
      return random.nextInt(100);
   }

   /**
    * returns an example metadata file.
    * @todo not supported yet.  Need to make a metadata file &amp; return it
    */
   public Element getRegistryMetadata()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Returns unknown
    */
   public ServiceStatus getStatus()
   {
      return ServiceStatus.UNKNOWN;
   }

}

/*
$Log: DummyDatacenterDelegate.java,v $
Revision 1.5  2003/09/07 18:52:39  mch
Added typesafe ServiceStatus

Revision 1.4  2003/09/05 12:01:56  mch
Minor doc/error changes

Revision 1.3  2003/08/29 07:57:01  maven
- changed '&' to '&amp;'

Revision 1.2  2003/08/27 23:54:20  mch
test bug fixes

Revision 1.1  2003/08/27 23:30:10  mch
Introduced DummyDatacenterDelegate, selfcontained package for other workgroups to test with

Revision 1.3  2003/08/27 22:40:55  mch
removed unnecessary import (maven report...!)

Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:28  mch
initial checkin


*/


