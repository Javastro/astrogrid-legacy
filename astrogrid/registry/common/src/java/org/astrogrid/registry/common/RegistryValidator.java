package org.astrogrid.registry.common;

import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class RegistryValidator {
    

    /**
     * @param doc
     * @param rootElement
     * @return
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