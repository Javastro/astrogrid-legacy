/*
 * $Id: SoapSupport.java,v 1.1 2009/05/13 13:20:23 gtr Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.dataservice.api;

import javax.xml.soap.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * A set of routines to help with setting up SOAP messages and calls.  Of course,
 * there are a variety of existing ones, but I couldn't find one that did the
 * straightfoward (?) job of inserting elements as parameters which we do
 * for ADQL parameters.
 *
 * @author M Hill
 */

public class SoapSupport  {
   
   
   
   /**
    * Converts a DOM element to a SOAP element and adds it to the given SOAP element, recursively */
   public SOAPElement addSoapChildFromDom(SOAPEnvelope factory, SOAPElement parent, Element domElement) throws SOAPException {
      //      SOAPElement soapChild = parent.addChildElement(domElement.getLocalName(), "adql", "http://www.ivoa.net/xml/ADQL/v0.7.4");
      SOAPElement soapChild = parent.addChildElement(domElement.getLocalName());  //create with no namespace
      
      //copy attributes
      NamedNodeMap atts = domElement.getAttributes();
      for (int i = 0; i < atts.getLength(); i++) {
         Name attName = factory.createName(atts.item(i).getNodeName());
         soapChild.addAttribute(attName, atts.item(i).getNodeValue());
      }
      
      //copy children
      NodeList childs = domElement.getChildNodes();
      for (int i = 0; i < childs.getLength(); i++) {
         if (childs.item(i) instanceof Element) {
            addSoapChildFromDom(factory, soapChild, (Element) childs.item(i));
         }
         else if (childs.item(i).getNodeType() == Element.TEXT_NODE) {
            soapChild.addTextNode(childs.item(i).getNodeValue());
         }
         
      }
      return soapChild;
   }
   
   /** Returns the reply as a string */
   public String getReplyAsString(SOAPMessage reply) throws IOException, SOAPException {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      
      reply.writeTo(out);
      
      return out.toString();
   }

   /** Simple convenience routine to create the connection and make the call */
   public SOAPMessage makeSimpleCall(SOAPMessage message, URL endpoint) throws SOAPException {
      SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();//new HttpSOAPConnectionFactory(); //
      SOAPConnection connection = soapConnFactory.createConnection();
      SOAPMessage reply = connection.call(message, endpoint);
      connection.close();
      return reply;
   }
   /*
   public SOAPConnection makeMessage(String method, Hashtable parameters) throws SOAPException {
      MessageFactory messageFactory = MessageFactory.newInstance();
      SOAPMessage message = messageFactory.createMessage(); //new Message1_2Impl(); //
      
      //Create objects for the message parts
      SOAPPart soapPart =     message.getSOAPPart();
      SOAPEnvelope envelope = soapPart.getEnvelope();
      SOAPBody body =         envelope.getBody();
      
      //envelope.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");
      
      //Populate the body
      //Create the main element and namespace
      SOAPElement methodElement = body.addBodyElement(
         envelope.createName(method)
      );

      //add content
      Enumeration keys = parameters.keys();
      while (keys.hasMoreElements())  {
         Object key = keys.nextElement();
         Object parameter = parameters.get(key);
         
         if (parameter instanceof Element) {
            addSoapChildFromDom(envelope, methodElement, (Element) parameter);
         }
         else {
            methodElement.addChildElement(key.toString()).addTextNode(parameter.toString());
         }
      }
      
      //Save the message
      message.saveChanges();
      
      return message;
   }
    */
}

/*
 $Log: SoapSupport.java,v $
 Revision 1.1  2009/05/13 13:20:23  gtr
 *** empty log message ***

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.1  2004/12/05 19:38:37  mch
 changed skynode to 'raw' soap (from axis) and bug fixes



 */


