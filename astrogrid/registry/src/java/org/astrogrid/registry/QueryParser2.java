/*
 * Created on 25-Apr-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import java.io.StringReader;
import javax.xml.parsers.*;
import java.io.Reader;

/**
 * @author Elizabeth Auden
 *
 */
public class QueryParser2 {

	public static String parseQuery (String query) {

	  Reader reader = new StringReader(query);
	  InputSource inputSource = new InputSource(reader);
	  //String queryType = "";
	  String queryResponse = "";
      String sqlQuery = "SELECT * FROM <registry> WHERE ";
      
	  Document doc = null;
	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  DocumentBuilder builder = null;
	  try {
		builder = factory.newDocumentBuilder();
		doc = builder.parse(inputSource);
	  }
	  catch (ParserConfigurationException e) {
		System.err.println(e);
	  }
	  catch (Exception e){
		System.out.println("oops!: " + e);
	  }

	  try{
		Element docElement = doc.getDocumentElement();
		NodeList nl = docElement.getElementsByTagName("query");
        sqlQuery = buildQuery(nl, sqlQuery);
	  	  
	  }
	  catch (Exception e) {
		queryResponse = "";
	  }
	  return queryResponse;
	}

    public static String buildQuery(NodeList nl, String sqlQuery){
      String queryResponse;
      String nodeDetails;
      
	  for (int i=1;i<nl.getLength();i=i+2){
        Node node = nl.item(i).getFirstChild();
	    if(node.hasChildNodes()) {

		  NodeList children = node.getChildNodes();
		  if (children != null) {

			for (int k=1; k< children.getLength(); k=k+2) {

			  if(children.item(k).hasChildNodes()) {

				NodeList childrenOfchildren = children.item(k).getChildNodes();

				if (childrenOfchildren != null) {
 
				  for (int j=1; j< childrenOfchildren.getLength();j=j+2) {

					// int queryInt = 0;
					String childNodeName = childrenOfchildren.item(j).getNodeName();
					String childNodeValue = childrenOfchildren.item(j).getNodeValue();
                    if ((childNodeName.equals("selectionSequence")) && (childrenOfchildren.item(j).getChildNodes() !=null)){
			 		  nodeDetails = "";
					  sqlQuery = sqlQuery + getSequenceSelectionDetails(childrenOfchildren.item(j), nodeDetails);
					  nodeDetails = null;
                    }
				  }
				}
			  }
			}
		  }
		}
	  }
	
	queryResponse = "<queryResponse>" + sqlQuery + "</queryResponse>";
	//System.out.println(queryResponse);
	return queryResponse;
    }
  


  //private String getNodeDetails (Node node, String queryElement, String queryElementValue, String matchElementValue) {
  public static String getSequenceSelectionDetails(Node node, String nodeDetails) {
  	
  	nodeDetails = nodeDetails + "(";
	String content = "";
	//int type = node.getNodeType();

    NodeList selectionSequenceChildren = node.getChildNodes();

	if (selectionSequenceChildren != null) {
	  // if it does, iterate through the collection
	  for (int m=0; m< selectionSequenceChildren.getLength(); m++) {
        int type = selectionSequenceChildren.item(m).getNodeType();

		if (type == Node.ELEMENT_NODE) {
		  if (node.getNodeName().equals("selection")){	
		  	 NodeList selectionChildren = node.getChildNodes();
  			 for (int n=0; n< selectionChildren.getLength(); n++) {
  			 	int selectionNodeType = selectionChildren.item(n).getNodeType();
  			 	if (type == Node.ELEMENT_NODE){
  			 		if (selectionChildren.item(n).getNodeName().equals("item")){
  			 		   nodeDetails = nodeDetails + selectionChildren.item(n+1).getNodeValue() + "="; 			 		   	
  			 		}
					if (selectionChildren.item(n).getNodeName().equals("value")){
					   nodeDetails = nodeDetails + "'" +selectionChildren.item(n+1).getNodeValue() + "'"; 			 		   	
					}
					if (selectionChildren.item(n).getNodeName().equals("selectionSequence")){
						// recursively call function to proceed to next level
						getSequenceSelectionDetails(selectionChildren.item(m), nodeDetails);
					}
  			 	}
  			 }
		  }
		  if (node.getNodeName().equals("selectionSequence")){	
			// recursively call function to proceed to next level
			getSequenceSelectionDetails(selectionSequenceChildren.item(m), nodeDetails);
		  }
		  if (node.getNodeName().equals("operator")){	
			// recursively call function to proceed to next level
			nodeDetails = nodeDetails + " " + selectionSequenceChildren.item(m+1).getNodeValue() + " "; 			 		   	
		  }
		}
	  }
	  nodeDetails = nodeDetails + ")";
	}
  
 
    return nodeDetails;
   }
 }

