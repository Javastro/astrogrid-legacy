package org.astrogrid.registry.services;
import java.io.*;
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
  String nodeDetails = "";


  boolean queryElementMatch = false;
  boolean queryMatch = false;

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
      String queryResponse = "";
      String metadataFormatResponse = "";
      String basicMetadataResponse = "";
      String curationMetadataResponse = "";

      Element docElement = doc.getDocumentElement();
      NodeList nl = docElement.getElementsByTagName(queryElement);
      NodeList serviceNodes = docElement.getElementsByTagName("service");


      /** For all nodes in the queryElement nodeList, determine basic, curation, service metadata, and service metadataFormat           metadata. **/

      for( int i=1;i<nl.getLength();i=i+2){
        
        Element serviceNodesParent = (Element)serviceNodes.item(i).getParentNode();
        Node node = serviceNodesParent;

        String matchElementValue = docElement.getElementsByTagName(queryElement).item(0).getFirstChild().getNodeValue();

         if(node.hasChildNodes()) {

            // get the child nodes, if they exist
            NodeList children = node.getChildNodes();
            if (children != null) {
 
              boolean match = false;
              for (int k=1; k< children.getLength(); k=k+2) {

                if(children.item(k).hasChildNodes()) {

                  NodeList childrenOfchildren = children.item(k).getChildNodes();

                  if (childrenOfchildren != null) {
 
                    for (int j=1; j< childrenOfchildren.getLength();j=j+2) {

                      // int queryInt = 0;
                      String childNodeName = childrenOfchildren.item(j).getNodeName();
                      String childNodeValue = childrenOfchildren.item(j).getNodeValue();

                      if ((childNodeName.equals("serviceType")) && (childrenOfchildren.item(j).getChildNodes() !=null)){
                        nodeDetails = "";
                        metadataFormatResponse = metadataFormatResponse 
                          + getNodeDetails(childrenOfchildren.item(j), queryElement, queryElementValue, matchElementValue);
                        nodeDetails = null;
                      }

  
                      else if ((childNodeName.equals("id")) || (childNodeName.equals("title"))){
                        nodeDetails = "";
                        basicMetadataResponse = basicMetadataResponse
                          + getNodeDetails(childrenOfchildren.item(j), queryElement, queryElementValue, matchElementValue);
                        nodeDetails = null;
                      }

                      else if (!(childNodeName.equals("id")) && !(childNodeName.equals("title")) && !                                                 (childNodeName.equals("serviceType"))){
                        nodeDetails = "";
                        curationMetadataResponse = curationMetadataResponse
                          + getNodeDetails(childrenOfchildren.item(j), queryElement, queryElementValue, matchElementValue);
                        nodeDetails = null;
                      }
                    }


                    if(returnMetadata.equals("basic")) {
                      response = response + "Basic metadata: \n" + basicMetadataResponse + "\n";
                    }
                    else if(returnMetadata.equals("curation")) {
                      response = response +  "Basic metadata: \n" +basicMetadataResponse + "\nCuration metadata: \n"                                 + curationMetadataResponse + "\n";
                    }
                    else if(returnMetadata.equals("metadataFormat")) {
                      response = response + "Basic metadata: \n" + basicMetadataResponse + "\nMetadataFormat: \n"                                    + metadataFormatResponse + "\n";
                    }

                    if (queryMatch == false) {
                      response = "";
                    }

                    queryResponse = queryResponse + response; 
                    response = "";       
                    queryElementMatch = false;
                    queryMatch = false;

                    metadataFormatResponse = "";
                    basicMetadataResponse = "";
                    curationMetadataResponse = "";
                  }
                }
              }
            }
          }
      }
      return queryResponse;
    }

    private String getNodeDetails (Node node, String queryElement, String queryElementValue, String matchElementValue) {

      String content = "";
      int type = node.getNodeType();

      // check if node is type element
      if (type == Node.ELEMENT_NODE) {
        if ((node.getNodeName()).equals(queryElement)) {
          queryElementMatch = true;
        } 
        nodeDetails = nodeDetails + "<" + node.getNodeName() + ">";

      } else if (type == Node.TEXT_NODE) {
        // check if text node and print value
        content = node.getNodeValue();

        if (!content.trim().equals("")){
          if ((queryElementMatch == true) && ((queryElementValue.equals("all")) || (content.equals(queryElementValue)))) {
            queryMatch = true;
          } 

          nodeDetails = nodeDetails + content;
        }
      } else if (type == Node.COMMENT_NODE) {
        // check if comment node and print value
        content = node.getNodeValue();
        if (!content.trim().equals("")){
          nodeDetails = nodeDetails + content;
        }
      }

      // check if current node has any children
      NodeList children = node.getChildNodes();
      if (children != null) {

        // if it does, iterate through the collection
        for (int m=0; m< children.getLength(); m++) {
          TabCounter++;
          // recursively call function to proceed to next level
          getNodeDetails(children.item(m), queryElement, queryElementValue, matchElementValue);
          TabCounter--;
        }
      } 
    
      if (type == Node.ELEMENT_NODE) {
        nodeDetails = nodeDetails + "</" + node.getNodeName() + ">";
      }     
      return nodeDetails;
    }
   
  }

  public String serviceMethod(String query) {


    System.out.println("Got to service Method!  \n\n");

  //public static void main(String args[]) {

/*
    if (args.length != 3){
      System.out.println("Usage: java org.astrogrid.registry.services.RegistryService <methodName> <queryElement> "
        + "<queryElementValue>");
      System.exit(0);
    }
*/
/*
    String methodName = args[0];
    String queryElement = args[1];
    String queryElementValue = args[2];
    String response = "";
*/

    String methodName = query.substring(query.indexOf("*"), query.indexOf("|"));
    String queryElement = query.substring(query.indexOf("|"), query.indexOf("$"));
    String queryElementValue = query.substring(query.indexOf("$"), query.indexOf("!"));
    String response = "";

    System.out.println("methodName: "+methodName + "\n\n");
    System.out.println("queryElement: "+queryElement + "\n\n");
    System.out.println("queryValue: "+queryElementValue + "\n\n");


    RegistryService rs = new RegistryService();
    
    if (methodName.equals("queryServices")){
      response = rs.queryServices(queryElement, queryElementValue);
    }
    else if (methodName.equals("listRegistryMetadata")){
      response = rs.listRegistryMetadata(queryElement, queryElementValue);
    }
    else if (methodName.equals("listAllServices")){
      response = rs.listAllServices(queryElement, queryElementValue);
    }
    else if (methodName.equals("listServiceMetadata")){
      response = rs.listServiceMetadata(queryElement, queryElementValue);
    }
    else if (methodName.equals("listServiceMetadataFormat")){
      response = rs.listServiceMetadataFormat(queryElement, queryElementValue);
    }
    else {
      response = "Usage: java org.astrogrid.registry.services.RegistryService <methodName>";
    }

    // System.out.println(response);
    return response;
  }
 

  /** Method queryServices finds all services with a registry entry element that matches a specific value.  The method       returns basic service metadata.  **/

  public String queryServices(String queryElement, String queryElementValue) {
    String returnMetadata = "basic";
    RegistryHandler dom = new RegistryHandler();
    String response = dom.generateResponse(dom, queryElement, queryElementValue, returnMetadata);
    return response;

  }

  /** Method listRegistryMetadata finds the service entry for this registry. Searches can be done for a registry whose             service entry contains an element that matches a specific value.  The method returns curation service metadata.  **/

  public String listRegistryMetadata(String queryElement, String queryElementValue) {
    String returnMetadata = "curation";
    RegistryHandler dom = new RegistryHandler();
    String response = dom.generateResponse(dom, queryElement, queryElementValue, returnMetadata);
    return response;
  }

  /** Method listAllServices finds all services with a registry entry.  The method returns basic service metadata.  **/

  public String listAllServices(String queryElement, String queryElementValue) {
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
