//-------------------------------------------------------------------------
// $Id: AxisClient.java,v 1.2 2004/01/12 18:43:50 mch Exp $
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 11/11/02   KEA       Initial prototype
// 29/11/02   MCH       Changed to instance based rather than static
//
//
// NOTE: This class is developed for use with Apache Axis 1.0 RC1.
//-------------------------------------------------------------------------


package org.astrogrid.ace.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class AxisClient
{
   //service location
   URL endpointURL = null;

   public AxisClient(String serviceEndpointUrl) throws MalformedURLException
   {
      this.endpointURL = new URL(serviceEndpointUrl);
   }

   public Element invokeService(String inputFilename) throws IOException
   {
      File paramFile = new File(inputFilename);
      FileInputStream fis = new FileInputStream(paramFile);
      return  invokeService(XMLUtils.newDocument(fis).getDocumentElement());
      
   }

   public Element invokeService(Element inputDom) throws IOException
   {
      Log.trace("Starting invokeService...");
      try
      {
         // Set up the infrastructure to contact the service
         Service service = new Service();
         Call call = (Call) service.createCall();
         call.setTargetEndpointAddress(endpointURL);

         // Set up the input message for the service
         // First, create an empty message body
         SOAPBodyElement[] requestSbe = new SOAPBodyElement[1];

         // Now get the data, read it into a document and put the document
         // into the message body
         //         File paramFile = new File(AceInputXmlFile);
         //         FileInputStream fis = new FileInputStream(paramFile);
         //         requestSBElts[0] = new SOAPBodyElement(XMLUtils.newDocument(fis).getDocumentElement());
         requestSbe[0] = new SOAPBodyElement(inputDom);

         // Make the call to the service
         System.out.println("Making actual call...");
         Vector resultSbe = (Vector) call.invoke(requestSbe);
         System.out.println("...finished actual call.");

         // Get the response message, extract the return document
         // from the message body and return it.
         //
         System.out.println("Returning response...");
         System.out.println("Number of response elts is " +
                               Integer.toString(resultSbe.size()));


         return (Element)resultSbe.get(0);
      }
      catch (AxisFault fault)
      {
         IOException ioe = new IOException("SOAP Fault, "
                                              +"actor '"+ fault.getFaultActor()+"' "
                                              +"fault [" + fault.getFaultCode()+"] "
                                              +fault.getFaultString());
         //ioe.setStackTrace(e.getStackTrace());  //java v1.4+
         ioe.fillInStackTrace(); //java v1.3
         throw ioe;
      }
      catch (ServiceException se)
      {
         IOException ioe = new IOException(""+se);
         //ioe.setStackTrace(e.getStackTrace());  //java v1.4+
         ioe.fillInStackTrace(); //java v1.3
         throw ioe;
      }
   }

}

