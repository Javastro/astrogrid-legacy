package org.astrogrid.registry.common;

import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * A Small helper class to validate XML from the Registry primarily updates to the Registry.
 * @author Kevin Benson
 *
 */
public class RegistryValidator {
    

    /**
     * Method: isValid
     * Description: Performs a validation on the XML from xerces to see if it is a valid XML for the Registry.
     * @param doc XML document to be validated
     * @param rootElement - rootElement of the XML
     * @return true if it is valid otherwise an Exception is thrown up.
     */
    public static boolean isValid(Document doc, String rootElement) {
        //System.out.println("calling assertSchemaValid with Resource String = " + DomHelper.DocumentToString(doc));
        NodeList nl = doc.getElementsByTagNameNS("*",rootElement);
        System.out.println("okay calling assertSchemaValid for rootelemnt = " + rootElement + " and nodelist size = " + nl.getLength());
        for(int i = 0;i < nl.getLength(); i++) { 
            AstrogridAssert.assertSchemaValid((Element)nl.item(i),rootElement,RegistrySchemaMap.ALL);
        }
        return true;
    }
    
    /**
     * Method: isValid
     * Description: Grabs the rootElement of the XML from the Local or Node Name of the given XML document then
     * validates the XML.
     * @param doc XML document to be validated.
     * @return true if it is valid otherwise an Exception is thrown up.
     */
    public static boolean isValid(Document doc) {
        //System.out.println("okay in isvalid now call it with root element = " + doc.getDocumentElement().getLocalName() + " and nodename = " + doc.getDocumentElement().getNodeName());
        String rootElement = doc.getDocumentElement().getLocalName();
        if(rootElement == null) {
            rootElement = doc.getDocumentElement().getNodeName();
        }
        AstrogridAssert.assertSchemaValid(doc,rootElement,RegistrySchemaMap.ALL);
        return true;
    }
}