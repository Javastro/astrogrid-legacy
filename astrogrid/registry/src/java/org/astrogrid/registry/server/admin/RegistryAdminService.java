package org.astrogrid.registry.server.admin;

import org.w3c.dom.Document;
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.axis.utils.XMLUtils;
 


/**
 * @author Elizabeth Auden
 * 
 * The RegistryAdminService is a web service that submits an XML formatted
 * administratio query to the RegistryAdmin class.  This web service allows
 * a user to add, edit, or delete a resource service entry inside the registry.
 * 
 * Elizabeth  Auden, 24 October 2003
 */

public class RegistryAdminService implements org.astrogrid.registry.RegistryAdminInterface {

  public Document adminQuery(Document adminQuery) {

    String regAdminQuery = XMLUtils.DocumentToString(adminQuery);     
    String adminResponse = RegistryAdmin.requestAdmin(regAdminQuery);
    Document registryDoc = null;
    try {
         
       Reader reader2 = new StringReader(adminResponse);
       InputSource inputSource = new InputSource(reader2);
         
       DocumentBuilder registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
       registryDoc = registryBuilder.parse(inputSource);
       System.out.println("the registryDoc = " + XMLUtils.DocumentToString(registryDoc));
         
    } catch (Exception e){
       registryDoc = null;
    }
    
    return registryDoc;
  }
}
