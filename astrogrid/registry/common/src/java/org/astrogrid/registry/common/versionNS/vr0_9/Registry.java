package org.astrogrid.registry.common.versionNS.vr0_9;

import org.astrogrid.registry.common.versionNS.IRegistryInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 


/**
 * Small Interface used by the server side web service and the client delegate.  Used for
 * harvesting other registries.  A client may call this webservice, but typically it will 
 * automatically harvest from registries.
 * 
 * @author Kevin Benson
 *
 */
public class Registry extends
    org.astrogrid.registry.common.versionNS.Registry implements IRegistryInfo {
   
   private final double VERSION_NUMBER = 0.9;
    
}