//-------------------------------------------------------------------------
// FILE: AceService.java
// PACKAGE: org.astrogrid.ace.webservice.axis
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 11/11/02   KEA       Initial prototype
//
//
// NOTE: This class is developed for use with Apache Axis 1.0.
//-------------------------------------------------------------------------

package org.astrogrid.ace.webservice.axis;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Text;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;

import org.apache.axis.AxisFault;
import org.apache.axis.utils.XMLUtils;
import org.apache.axis.message.SOAPBodyElement;

import org.astrogrid.ace.web.Ace;
import org.astrogrid.ace.service.AceContext;
//import org.astrogrid.ace.AceParameterBundle;

//import org.astrogrid.service.ParameterExtractor;

import org.astrogrid.xmlutils.XmlValidatorIfc;
import org.astrogrid.xmlutils.XmlValidatorXercesImpl;

// FOR LOGGING
import org.astrogrid.log.Log;

// MISC
import java.io.StringReader;
import java.io.IOException;
import java.util.Vector;


// FOR PRINT AND DEBUG FUNC ONLY - REMOVE LATER
//import org.astrogrid.ace.webservice.axis.ParameterBundle;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * <p>???
 *
 * <p>???
 *
 * <p>See ???.java for a ???
 *
 * <p>TO DO:  <ul>
 * <li>Nicer exception-throwing and handling?</li>
 * </ul>
 *
 *
 * @see org.astrogrid.ace.webservice.axis.???
 * @see AceService_deploy.wsdd
 * @see AceService_undeploy.wsdd
 *
 * @author Kona Andrews,
 * <a href="mailto:kea@ast.cam.ac.uk">kea@ast.cam.ac.uk</a>
 * @version 1.0
*/


public class AceService
{
   /**
    * Location of logfile - each instance should have a separate logfile -
      fix this when policy is decided re instance working directories etc.
    */
   //static final String LOGFILE =
   //    "/data/cass123a/kea/AvoDemo/WORKING/serverlog.txt";

   // Dump log info unless needed for debugging - otherwise log files get huge
   static final String LOGFILE = "/dev/null";


   /**
    * Dummy constructor - does nothing.
    */
   public AceService()
   {
   }

   /**
    * This function is the Ace web-service's public interface.
    * It accepts a DOM Element representing the root of an XML
    * document n
    *
    * @param xmlIn  A (pre-initialised) reader for the input XML
    *
    * @return  A Document containing the DOM tree for the input XML.
    */

   public Element[] processParams(Element[] soapBodyElements) throws AxisFault
   {
      try
      {
         net.mchill.log.Log.addHandler(new net.mchill.log.Log2File(LOGFILE));

         //Element soapBody = (Element) soapBodyElements.get(0);
         Element soapBody = soapBodyElements[0];

         Log.trace("Webservice: Validating XML input against its schema");

         XmlValidatorXercesImpl validator = new XmlValidatorXercesImpl();
         StringReader reader =
               new StringReader(XMLUtils.ElementToString(soapBody));

         // Throws exception if XML doesn't validate against schema
         try
         {
            validator.validate(reader);
         }
         catch (Exception e)
         {
            AxisFault fault = AxisFault.makeFault(e);
            fault.setFaultCode("Client.InvalidXMLDocument");
            throw fault;
         }

         // INPUT XML OK - NO VALIDATION ERROR AGAINST SCHEMA

         // Make the SExtractor call
         //
         Log.trace("Webservice: Doing Ace wrapper call");
         Element result = doSExtractorCall(soapBody);
         Log.trace("Webservice: Done SExtractor call");

         return makeVOTableReturn(result);
         //return makeStatusReturn(result);

      }
      // CATCH ANY OTHER EXCEPTIONS AND MAKE A PROPER AXIS FAULT FROM THEM
      catch (Exception e)
      {
         throw AxisFault.makeFault(e);
      }
   }


   protected Element[] makeVOTableReturn(Element result) throws Exception
   {
         // Build output doc for return SOAP msg
         //
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setNamespaceAware(false);
         DocumentBuilder builder = factory.newDocumentBuilder();

         Document responseDoc = builder.newDocument();

         Element[] wsResult = new Element[1];
         wsResult[0] = result;
         return wsResult;
   }

   protected Element[] makeStatusReturn(Element result) throws Exception
   {
         // Build output doc for return SOAP msg
         //
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setNamespaceAware(true);
         DocumentBuilder builder = factory.newDocumentBuilder();

         Document responseDoc = builder.newDocument();

         Element resRoot = responseDoc.createElementNS(
               "http://www.astrogrid.org/namespace/SimpleReturnDoc",
               "ReturnString");

         String status = "";
         if (result != null)
         {
            status = "SExtractor run complete - results in sexResults.xml";
         }
         else
         {
            status = "SExtractor run FAILED - null results node received";
         }
         // PUT OUR OUTPUT INTO THE DOCUMENT
         Text eltText = responseDoc.createTextNode(status);
         resRoot.appendChild(eltText);

         Element[] wsResult = new Element[1];
         wsResult[0] = resRoot;
         return wsResult;
   }


   protected Element doSExtractorCall(Element soapBody) throws IOException
   {
      Ace ace = new Ace();
      return ace.runApplication(soapBody);
   }

/* DEBUG HARNESS ONLY - NOT NEEDED NOW
   protected String getBodyString(Element soapBody) throws Exception
   {
      //SExConfigBundle bundle = new SExConfigBundle();
      ParameterBundle bundle = new ParameterBundle();
      String outString = "\n";

      NodeList nodeList = soapBody.getChildNodes();

      for (int i = 0; i < nodeList.getLength(); i++) {
         if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {

            Element paramNode = (Element)(nodeList.item(i));

            // SEE IF THIS IS A MULTI-ARGUMENT PARAMETER
            NodeList args =
               paramNode.getElementsByTagName("arg");

            if (args.getLength() == 0) {
               throw new Exception("Error - missing <arg> for parameter "
                     + paramNode.getTagName());
            }

            String tagString =
               args.item(0).getFirstChild().getNodeValue();

            for (int j = 1; j < args.getLength(); j++) {
               tagString =
                  tagString + ", " +
                  args.item(j).getFirstChild().getNodeValue();
            }
            //bundle.setDictionary(paramNode.getTagName(),tagString);
            bundle.addParameter(paramNode.getTagName(),tagString);
         }
      }
      //outString = outString + bundle.getStringOutput();

      String[] parameters = bundle.getParameters();
      int maxLen = 0;
      for (int i=0;i<parameters.length;i++) {
         if (maxLen < parameters[i].length()) {
            maxLen = parameters[i].length();
         }
      }

      for (int i=0;i<parameters.length;i++) {
         int numSpaces = maxLen - parameters[i].length();
         outString = outString + parameters[i] + "   ";
         for (int j = 0; j < numSpaces; j++) {
            outString = outString + " ";
         }
         // BOOLEANS ARE A SPECIAL CASE
         String paramVal =
            (bundle.getParameter(parameters[i])).trim();
         if (paramVal.equalsIgnoreCase("true")) {
            outString = outString + "Y" + '\n';
         }
         else if (paramVal.equalsIgnoreCase("false")) {
            outString = outString + "N" + '\n';
         }
         else {
            outString = outString +
               bundle.getParameter(parameters[i]) + '\n';
         }
      }
      return outString;
   }
   */
}
//-------------------------------------------------------------------------
