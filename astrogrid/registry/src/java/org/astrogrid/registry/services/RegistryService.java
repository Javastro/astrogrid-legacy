package org.astrogrid.registry.services;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class RegistryService {

  public class RegistryHandler {

    /** The RegistryHandler class builds an xml document and then parses the document into a tree.  Different lists of         element nodes have been created for curation metadata, serviceType metadata, contact metadata, subjectList metadata,         and sibling metadata elements of the query element.  Metadata for services matching either a specific query value or         all query values is returned for five registry use cases:

    1. queryServices
    2. listAllServices
    3. listServiceMetadata
    4. listRegistryMetadata
    5. listServiceMetadataFormat
    **/  

  private int TabCounter = 0;
  String serviceMetadataFormat = "";
  String basicMetadata = "";
  String curationMetadata = "";

    public String generateResponse(RegistryHandler dom, String queryElement, String queryElementValue, String returnMetadata){

    Document doc = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    try {
      builder = factory.newDocumentBuilder();
      doc = builder.parse("org/astrogrid/registry/registry.xml.new");
    }
    catch (ParserConfigurationException e) {
      System.err.println(e);
    }
    catch (Exception e){
      System.out.println("oops!: " + e);
    }

      String response = "";
      String metadataFormatResponse = "";
      String basicMetadataResponse = "";
      String curationMetadataResponse = "";

      Element docElement = doc.getDocumentElement();
      NodeList nl = docElement.getElementsByTagName(queryElement);
      NodeList serviceNodes = docElement.getElementsByTagName("service");


      /** For all nodes in the queryElement nodeList, determine basic, curation, service metadata, and service metadataFormat           metadata. **/

      for( int i=1;i<nl.getLength();i=i+2){
        
        Element serviceNodesParent = (Element)serviceNodes.item(i).getParentNode();
        Node node =serviceNodesParent;

        if(node.hasChildNodes()) {

          // get the child nodes, if they exist
          NodeList children = node.getChildNodes();
          if (children != null) {


           for (int k=1; k< children.getLength(); k=k+2) {
             if(children.item(k).hasChildNodes()) {

               NodeList childrenOfchildren = children.item(k).getChildNodes();

               if (childrenOfchildren != null) {
 
                 for (int j=1; j< childrenOfchildren.getLength();j=j+2) {
                   String childNodeName = childrenOfchildren.item(j).getNodeName();
                   String childNodeValue = childrenOfchildren.item(j).getNodeValue();

                   if ((childNodeName.equals("serviceType")) && (childrenOfchildren.item(j).getChildNodes() !=null)){
                     metadataFormatResponse = getServiceTypeNodeDetails(childrenOfchildren.item(j));
                   }
  
                   else if ((childNodeName.equals("id")) || (childNodeName.equals("title"))){
                     basicMetadataResponse = getBasicMetadata(childrenOfchildren.item(j));
                   }

                   else if (!(childNodeName.equals("id")) && !(childNodeName.equals("title")) && !                     (childNodeName.equals("serviceType"))){

                     curationMetadataResponse = getCurationMetadata(childrenOfchildren.item(j));
                   }
                 }
                 if(returnMetadata.equals("basic")) {
                   response = response + "Basic metadata: \n" + basicMetadataResponse + "\n";
                 }
                 else if(returnMetadata.equals("curation")) {
                   response = response +  "Basic metadata: \n" +basicMetadataResponse + "\n Curation metadata: \n"                                 + curationMetadataResponse + "\n";
                 }
                 else if(returnMetadata.equals("metadataFormat")) {
                   response = response + "Basic metadata: \n" + basicMetadataResponse + "\n MetadataFormat: \n"                                    + metadataFormatResponse + "\n";
                 }

                 metadataFormatResponse = null;
                 basicMetadataResponse = null;
                 curationMetadataResponse = null;
               }
             }
           }
         }
       }
    }
    return response;
  }

  private String getServiceTypeNodeDetails (Node node) {

    String content = "";
    int type = node.getNodeType();

    // check if node is type element
    if (type == Node.ELEMENT_NODE) {
      serviceMetadataFormat = serviceMetadataFormat + "<" + node.getNodeName() + ">";

    } else if (type == Node.TEXT_NODE) {
      // check if text node and print value
      content = node.getNodeValue();
 
      if (!content.trim().equals("")){
        serviceMetadataFormat = serviceMetadataFormat + content;
      }
    } else if (type == Node.COMMENT_NODE) {
      // check if comment node and print value
      content = node.getNodeValue();
      if (!content.trim().equals("")){
        serviceMetadataFormat = serviceMetadataFormat + content;
      }
    }

    // check if current node has any children
    NodeList children = node.getChildNodes();
    if (children != null) {

      // if it does, iterate through the collection
      for (int m=0; m< children.getLength(); m++) {
        TabCounter++;
        // recursively call function to proceed to next level
        getServiceTypeNodeDetails(children.item(m));
        TabCounter--;
      }
    } 
    
    if (type == Node.ELEMENT_NODE) {
      serviceMetadataFormat = serviceMetadataFormat + "</" + node.getNodeName() + ">";
    }     
    return serviceMetadataFormat;
  }

private String getBasicMetadata (Node node) {

    String content = "";
    int type = node.getNodeType();

    // check if node is type element
    if (type == Node.ELEMENT_NODE) {
      basicMetadata = basicMetadata + "<" + node.getNodeName() + ">";

    } else if (type == Node.TEXT_NODE) {
      // check if text node and print value
      content = node.getNodeValue();
 
      if (!content.trim().equals("")){
        basicMetadata = basicMetadata + content;
      }
    } else if (type == Node.COMMENT_NODE) {
      // check if comment node and print value
      content = node.getNodeValue();
      if (!content.trim().equals("")){
        basicMetadata = basicMetadata + content;
      }
    }

    // check if current node has any children
    NodeList children = node.getChildNodes();
    if (children != null) {

      // if it does, iterate through the collection
      for (int m=0; m< children.getLength(); m++) {
        TabCounter++;
        // recursively call function to proceed to next level
        getBasicMetadata(children.item(m));
        TabCounter--;
      }
    } 
  
    if (type == Node.ELEMENT_NODE) {
      basicMetadata = basicMetadata + "</" + node.getNodeName() + ">";
    }     
    return basicMetadata;
  }
private String getCurationMetadata (Node node) {

    String content = "";
    int type = node.getNodeType();

    // check if node is type element
    if (type == Node.ELEMENT_NODE) {
      curationMetadata = curationMetadata + "<" + node.getNodeName() + ">";

    } else if (type == Node.TEXT_NODE) {
      // check if text node and print value
      content = node.getNodeValue();
 
      if (!content.trim().equals("")){
        curationMetadata = curationMetadata + content;
      }
    } else if (type == Node.COMMENT_NODE) {
      // check if comment node and print value
      content = node.getNodeValue();
      if (!content.trim().equals("")){
        curationMetadata = curationMetadata + content;
      }
    }

    // check if current node has any children
    NodeList children = node.getChildNodes();
    if (children != null) {

      // if it does, iterate through the collection
      for (int m=0; m< children.getLength(); m++) {
        TabCounter++;
        // recursively call function to proceed to next level
        getCurationMetadata(children.item(m));
        TabCounter--;
      }
    } 
  
    if (type == Node.ELEMENT_NODE) {
      curationMetadata = curationMetadata + "</" + node.getNodeName() + ">";
    }     
    return curationMetadata;
  }

}

  public static void main(String args[]) {

    if (args.length != 1){
      System.out.println("Usage: java org.astrogrid.registry.services.RegistryService <methodName>");
      System.exit(0);
    }

    String queryElement = "id";
    String queryElementValue = "all";
    String response = "";
    String methodName = args[0];

    RegistryService rs2 = new RegistryService();
    
    if (methodName.equals("queryServices")){
      response = rs2.queryServices(queryElement, queryElementValue);
    }
    else if (methodName.equals("listRegistryMetadata")){
      response = rs2.listRegistryMetadata();
    }
    else if (methodName.equals("listAllServices")){
      response = rs2.listAllServices();
    }
    else if (methodName.equals("listServiceMetadata")){
      response = rs2.listServiceMetadata(queryElement, queryElementValue);
    }
    else if (methodName.equals("listServiceMetadataFormat")){
      response = rs2.listServiceMetadataFormat(queryElement, queryElementValue);
    }
    else {
      response = "Usage: java org.astrogrid.registry.services.RegistryService <methodName>";
    }

    System.out.println("\n\n" + response);
  }
 

  /** Method queryServices finds all services with a registry entry element that matches a specific value.  The method       returns basic service metadata.  **/

  public String queryServices(String queryElement, String queryElementValue) {
    String returnMetadata = "basic";
    RegistryHandler dom = new RegistryHandler();
    String response = dom.generateResponse(dom, queryElement, queryElementValue, returnMetadata);
    return response;

  }

  /** Method listRegistryMetadata finds the service entry for this registry. Searches can be done for a registry whose             service entry contains an element that matches a specific value.  The method returns curation service metadata.  **/

  public String listRegistryMetadata() {
    String queryElement = "id";
    String queryElementValue = "all";
    String returnMetadata = "curation";
    RegistryHandler dom = new RegistryHandler();
    String response = dom.generateResponse(dom, queryElement, queryElementValue, returnMetadata);
    return response;
  }

  /** Method listAllServices finds all services with a registry entry.  The method returns basic service metadata.  **/

  public String listAllServices() {
    String queryElement = "id";
    String queryElementValue = "all";
    String returnMetadata = "basic";
    RegistryHandler dom = new RegistryHandler();
    String response = dom.generateResponse(dom, queryElement, queryElementValue, returnMetadata);
    return response; 
  }

  /** Method listServiceMetadata finds a service with a registry entry element that matches a specific value.  The method           returns curation service metadata.  **/

  public String listServiceMetadata(String queryElement, String queryElementValue) {
    String returnMetadata = "curation";
    RegistryHandler dom = new RegistryHandler();
    String response = dom.generateResponse(dom, queryElement, queryElementValue, returnMetadata);
    return response;    
  }

  /** Method listServiceMetadataFormat finds a service with a registry entry element that matches a specific value.  The            method returns metadata from the service, or "metadata format".  **/

  public String listServiceMetadataFormat(String queryElement, String queryElementValue) {
    String returnMetadata = "metadataFormat";
    RegistryHandler dom = new RegistryHandler();
    String response = dom.generateResponse(dom, queryElement, queryElementValue, returnMetadata);
    return response;
  }

}