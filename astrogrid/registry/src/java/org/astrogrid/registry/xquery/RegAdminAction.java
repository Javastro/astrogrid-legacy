package org.astrogrid.registry.xquery;

import de.gmd.ipsi.xql.*;
import de.gmd.ipsi.domutil.*;
import org.w3c.dom.*;
import java.io.*;

public class RegAdminAction {

   public void deleteNode(String xmlFile, String deleteNode) {

	 //Create a PDOM Document
	 Document doc = DOMUtil.createDocument();
	 try {
	   DOMUtil.parseXML(
		   new FileInputStream(xmlFile),
		   doc,
		   false, // Parse mode: true = validating, false = non-validating
		   DOMUtil.SKIP_IGNORABLE_WHITESPACE
		   );
	 }
	 catch (DOMParseException e) {
	   e.printStackTrace();
	 }
	 catch (FileNotFoundException e) {
	   e.printStackTrace();
	 }
	 Document resultDoc = DOMUtil.createDocument();
	 Element root = resultDoc.createElement("root");
	 resultDoc.appendChild(root);

	 // create a node and delete it
	 Element e = doc.getNodeName(deleteNode);

	 doc.getFirstChild().appendChild(e);
	 doc.getFirstChild().removeChild(e);

   }

   public void updateNode(String xmlFile, String oldNode, String newNode) {
	  //Create a PDOM Document
	 Document doc = DOMUtil.createDocument();
	 try {
	   DOMUtil.parseXML(
		   new FileInputStream(xmlFile),
		   doc,
		   false, // Parse mode: true = validating, false = non-validating
		   DOMUtil.SKIP_IGNORABLE_WHITESPACE
		   );
	 }
	 catch (DOMParseException e) {
	   e.printStackTrace();
	 }
	 catch (FileNotFoundException e) {
	   e.printStackTrace();
	 }
	 Document resultDoc = DOMUtil.createDocument();
	 Element root = resultDoc.createElement("root");
	 resultDoc.appendChild(root);



	 // Get the TextNode and update it
	 Text t = (Text) doc.getFirstChild().getFirstChild();
	 t.setData("This is theupdate node");



   }

   public void addNode(String  xmlFile, String newNode ) {
	  //Create a PDOM Document
	 Document doc = DOMUtil.createDocument();
	 try {
	   DOMUtil.parseXML(
		   new FileInputStream(xmlFile),
		   doc,
		   false, // Parse mode: true = validating, false = non-validating
		   DOMUtil.SKIP_IGNORABLE_WHITESPACE
		   );
	 }
	 catch (DOMParseException e) {
	   e.printStackTrace();
	 }
	 catch (FileNotFoundException e) {
	   e.printStackTrace();
	 }
	 Document resultDoc = DOMUtil.createDocument();
	 Element root = resultDoc.createElement("root");
	 resultDoc.appendChild(root);

	//  insert Nodes
	 Element e = doc.createElement("root");
	 e.appendChild(doc.createTextNode(newNode));
	 doc.appendChild(e);


	}


}