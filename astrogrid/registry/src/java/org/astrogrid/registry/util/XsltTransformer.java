package org.astrogrid.registry.util;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Category;
import org.w3c.dom.Document;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class XsltTransformer {
  private static Category LOGGER = Category.getInstance(XsltTransformer.class);

  public static void main(String[] args) {
    try {
      //  create an instance of DocumentBuilder
      DocumentBuilderFactory theBuilderFactory = DocumentBuilderFactory.newInstance();
      theBuilderFactory.setNamespaceAware(true);
      
      DocumentBuilder theBuilder = theBuilderFactory.newDocumentBuilder();

      Document theInputDocument = theBuilder.parse(new File(args[0]));
      // args[0] is path of xml input file
      Document theXsltDocument = theBuilder.parse(new File(args[1]));
      // args[1] is path of xslt file

      String result =
        XsltTransformer.transform(theInputDocument, theXsltDocument);
        
      System.out.println("[main] output: \n" + result);
    }
    catch(Exception e) {
      System.err.println("[main] exception: " + e.getMessage());
      e.printStackTrace();
    }
  }

//  public static String transform(Element xmlInput, Document xslStylesheet) throws Exception {
//    LOGGER.debug("[transform] xml node: \n" + XMLUtils.ElementToString(xmlInput));
//    LOGGER.debug("[transform] xsl doc:  \n" + XMLUtils.DocumentToString(xslStylesheet));
//    
//    DOMSource xmlSource = new DOMSource(xmlInput);
//    DOMSource xslSource = new DOMSource(xslStylesheet);
//
//    return transform(xmlSource, xslSource);
//  }
  
  public static String transform(Document xmlInput, Document xslStylesheet) throws Exception {
    LOGGER.debug("[transform] xmlInput: " + XMLUtils.ElementToString(xmlInput.getDocumentElement()));
    LOGGER.debug("[transform] xslStylesheet: " + XMLUtils.ElementToString(xslStylesheet.getDocumentElement()));
    
    DOMSource xmlSource = new DOMSource(xmlInput);
    DOMSource xslSource = new DOMSource(xslStylesheet);

    return transform(xmlSource, xslSource);
  }

  public static String transform(DOMSource xmlSource, DOMSource xslSource) throws Exception {
    // could also use a DOMResult if we wanted output as a DOM document rather than a string.
    StreamResult theTransformationResult =
      new StreamResult(new ByteArrayOutputStream());

    Transformer theTransformer =
      TransformerFactory.newInstance().newTransformer(xslSource);

    // indent the output to make it more legible...
    theTransformer.setOutputProperty(
      "{http://xml.apache.org/xslt}indent-amount",
      "2");
    theTransformer.transform(xmlSource, theTransformationResult);

    // return the transformed document as a string
    return theTransformationResult.getOutputStream().toString();
  }

}