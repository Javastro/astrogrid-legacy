/*
   ServiceClient.java

   Date         Author      Changes
   1 Nov 2002   M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.soap.SOAPFaultException;
import org.w3c.dom.Element;
import org.astrogrid.log.Log;

/**
 * a client that wraps the service level interface to SExtractor.  Useu to
 * wrap up an XML configuration document in a SOAP message, post it to
 * Axis, and extract the results
 */

public class ServiceClient
{
   //service location
  URL endpointURL = null;

   public ServiceClient(String serviceEndpointUrl) throws MalformedURLException
   {
      this.endpointURL = new URL(serviceEndpointUrl);
   }

   /** Takes the given node and posts it to the service.  Returns an element
    * corresponding to the results document created by the application
    * wrapper.  This routine also checks in that doc for errors.  All faults
    * are returned as IOExcptions so that other implementations can be
    * made
    */
   public Element httpPost(Element domElement) throws IOException
   {
      try
      {
         Log.trace("(Client) Constructing http post...");
         //construct the service based on whatever implementation is used locally
         //using the service factory
//         QName qname = new QName("");  //local namespace description
//         ServiceFactory sf = ServiceFactory.newInstance();
//         Service service = sf.createService(endpointURL, qname);
         Service service = new org.apache.axis.client.Service();
         
         // Set up the infrastructure to contact the service
         Call call = (Call) service.createCall();
         call.setTargetEndpointAddress(endpointURL.toString());

         // Make the call to the service with the domElement as the inputParameter[0]
         Log.trace("(Client) ...invoking call...");
         Vector results = (Vector) call.invoke(new Object[] { new org.apache.axis.message.SOAPBodyElement(domElement) });

         if ((results == null) || (results.size() == 0))
         {
            throw new IOException("No results returned from call to service");
         }
         if (results.get(0) == null)
         {
            throw new IOException("Empty results returned from call to service");
         }

         Element soapResults = ((org.apache.axis.message.SOAPBodyElement) results.get(0)).getAsDOM();
         
         // Get the response message, extract the return document
         // from the message body and return it
         Log.trace("(Client) ...returning results ");
         return soapResults;
      }
      catch (SOAPFaultException fault)
      {
         IOException ioe = new IOException("SOAP Fault, "
                                    +"actor '"+ fault.getFaultActor()+"' "
                                    +"fault [" + fault.getFaultCode()+"] "
                                    +fault.getFaultString());
         //ioe.setStackTrace(fault.getStackTrace());  //java v1.4+
         ioe.fillInStackTrace(); //java v1.3
         throw ioe;
      }
      catch (IOException ioe) //just rethrow - caught to avoid being trapped by
      {                       //catch Exception below
         throw ioe;
      }
      catch (Exception e)
      {
         IOException ioe = new IOException(""+e);
         //ioe.setStackTrace(e.getStackTrace());  //java v1.4+
         ioe.fillInStackTrace(); //java v1.3
         throw ioe;
      }
   }

   /**
    * Test harness
    */
   public static void main(String [] args) throws Exception
   {
         // Read in the data from the given file
//         FileInputStream fis = new FileInputStream(args[0]);
//      FileInputStream fis = new FileInputStream("test.xml");
      Element e = org.astrogrid.tools.xml.EasyDomLoader.loadElement("test.xml");

      ServiceClient aceClient = new ServiceClient("http://localhost:8080/axis/services/AceService");

      Element results = aceClient.httpPost(e);

      org.astrogrid.tools.xml.DomDumper.dumpNode(results, System.out);
   }

}

