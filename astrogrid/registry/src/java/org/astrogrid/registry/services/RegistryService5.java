package org.astrogrid.registry.services;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class RegistryService5 {

  public class RegistryHandler {

    /** The RegistryHandler class builds an xml document and then parses the document into a tree.  Different lists of         element nodes have been created for curation metadata, serviceType metadata, contact metadata, subjectList metadata,         and sibling metadata elements of the query element.  Metadata for services matching either a specific query value or         all query values is returned for five registry use cases:

    1. queryServices
    2. listAllServices
    3. listServiceMetadata
    4. listRegistryMetadata
    5. listServiceMetadataFormat
    **/  

    public String generateResponse(RegistryHandler dom, String queryElement, String queryElementValue, String returnMetadata){

    Document doc = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    try {
      builder = factory.newDocumentBuilder();
      doc = builder.parse("org/astrogrid/registry/services/registry.xml.new");
    }
    catch (ParserConfigurationException e) {
      System.err.println(e);
    }
    catch (Exception e){
      System.out.println("oops!: " + e);
    }

      String response = "";
      Element docElement = doc.getDocumentElement();
      NodeList nl = docElement.getElementsByTagName(queryElement);
      NodeList curationNodes = docElement.getElementsByTagName("id");
      NodeList serviceTypeNodes = docElement.getElementsByTagName("servicelocation");
      NodeList contactNodes = docElement.getElementsByTagName("name");
      NodeList subjectListNodes = docElement.getElementsByTagName("subject");

      /** For all nodes in the queryElement nodeList, determine basic, curation, service metadata, and service metadataFormat           metadata. **/

      for( int i=0;i<nl.getLength();i++){
        
        Element nlParent = (Element)nl.item(i).getParentNode();
        Element curationNodesParent = (Element)curationNodes.item(i).getParentNode();
        Element serviceTypeNodesParent = (Element)serviceTypeNodes.item(i).getParentNode();
        Element contactNodesParent = (Element)contactNodes.item(i).getParentNode();
        Element subjectListNodesParent = (Element)subjectListNodes.item(i).getParentNode();


        String matchElementValue = nlParent.getElementsByTagName(queryElement).item(0).getFirstChild().getNodeValue();


        if ((queryElementValue.equals("all")) || (queryElementValue.equals(matchElementValue))){	

          String id = curationNodesParent.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();

          String title = curationNodesParent.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();

          String serviceType = serviceTypeNodesParent.getParentNode().getNodeName();


          String publisher = curationNodesParent.getElementsByTagName("publisher").item(0).getFirstChild().getNodeValue();
          String creator = curationNodesParent.getElementsByTagName("creator").item(0).getFirstChild().getNodeValue();
          String subjectList = "";



          for (int j=0;j<subjectListNodes.getLength();j++){
            subjectList = subjectList + "  <subject>" +               subjectListNodesParent.getElementsByTagName("subject").item(j).getFirstChild().getNodeValue() + "</subject>\n";

          }



          String name = contactNodesParent.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

          String email = contactNodesParent.getElementsByTagName("email").item(0).getFirstChild().getNodeValue();


          //String contributor = curationNodesParent.getElementsByTagName("contributor").item(0).getFirstChild().getNodeValue();


          //String description =curationNodesParent.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();

          String date = curationNodesParent.getElementsByTagName("date").item(0).getFirstChild().getNodeValue();

          //String version = curationNodesParent.getElementsByTagName("version").item(0).getFirstChild().getNodeValue();

          //String serviceURL= curationNodesParent.getElementsByTagName("serviceURL").item(0).getFirstChild().getNodeValue();


          response = response + "<service>\n";
          response = response + "<id>"+id +"</id> \n";
          response = response + "<title>"+title +"</title> \n";          
          response = response + "<serviceType>"+ serviceType +"</serviceType> \n";          

          if (returnMetadata.equals("curation")) {
            response = response + "<publisher>"+publisher +"</publisher> \n";
            response = response + "<creator>"+creator +"</creator> \n";
           // response = response + "<description>"+description +"</description> \n";
           // response = response + "<contributor>"+contributor +"</contributor> \n";
            response = response + "<date>"+ date +"</date> \n";
            //response = response + "<version>"+ version +"</version> \n";
            //response = response + "<serviceURL>"+ serviceURL +"</serviceURL> \n";
            response = response + "<subjectList>\n"+ subjectList +"</subjectList> \n";
            response = response + "<contact>\n  <name>"+ name +"</name> \n";
            response = response + "  <email>"+ email +"</email>\n</contact> \n";
          }

          if (returnMetadata.equals("metadataFormat")) {
            //response = response + "<allowedMethods>"+ allowedMethods +"</allowedMethods> \n";
          }
          response = response + "</service>\n";

        }
      }
  
      return response;

    }
  }

  public static void main(String args[]) {

    if (args.length != 2){
      System.out.println("Usage: java org.astrogrid.registry.services.RegistryService5 <methodName> <i>");
      System.exit(0);
    }

    String queryElement;
    String queryElementValue;
    String response = "";
    String methodName = args[0];

    queryElement = args[1];
    queryElementValue = "all";
    RegistryService5 rs2 = new RegistryService5();
    
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
      response = "Usage: java org.astrogrid.registry.services.RegistryService5 <methodName>";
    }

    System.out.println("Response: \n" + response);
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