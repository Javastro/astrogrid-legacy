package org.astrogrid.registry;

import de.gmd.ipsi.domutil.*;
import de.gmd.ipsi.pdom.*;
import org.w3c.dom.*;
import java.io.*;
import java.io.IOException;



/*
*
*
*
*/



public class RegAdminAction {


   /*
	*This Method delete any node contained in the variable "deleteNode" in the
	*file "xmlFile"
	*
   */

   public static void deleteNode(String xmlFile, String deleteNode) throws  IOException {

	 PDocument doc;
	 doc = new PDocument("mydoc.pdom");
	 try {
	   //Create a PDOM Document by parsing and XML input stream
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
	 catch (IOException e) {
	   e.printStackTrace();
	 }

	 // delete all the element deleteNode in doc PDOM document
	 deleteAllElements(doc,deleteNode);

	 // ... and do the garbage collection
	 PDOM.collectDOMFileGarbage( xmlFile );

   }


   /*
	*
	*
	*
   */

   public void addNode(String xmlFile,  Node parentNode, String newNode, String textNewNode) throws  IOException {
	 //PDOM document creation
	 PDocument doc;
	 doc = new PDocument("mydoc.pdom");
	 try {
	   //Create a PDOM Document by parsing and XML input stream
	   DOMUtil.parseXML(
		   new FileInputStream(xmlFile),
		   doc,
		   false, // Parse mode: true = validating, false = non-validating
		   DOMUtil.SKIP_IGNORABLE_WHITESPACE
		   );
	 }
	 catch (DOMParseException ex) {
	   ex.printStackTrace();
	 }
	 catch (FileNotFoundException ex) {
	   ex.printStackTrace();
	 }
	 catch (IOException ioe) {
	   ioe.printStackTrace();
	 }
	 //Variable definition

	 /*
	  find out how to convert a String into a node element
	  and insert here!!
	  */
	 Element e;
	 Text t;
	 Node n;

	 // Get the Node containing parentNode and add a newNode with text "textNewNode"
	 Node child = parentNode.getFirstChild();
	  while (child != null) {
			  if ( child instanceof Element && child.getNodeName() == textNewNode) {
					  e = doc.createElement(newNode);
					  t = doc.createTextNode(textNewNode);
					  parentNode = e.appendChild(t);
					  parentNode = child.appendChild(e);
			  }
		   child = child.getNextSibling();
	}

	// ... and do the garbage collection
	 PDOM.collectDOMFileGarbage( xmlFile );
   }


   /*
	*
	*
	*
   */


   public void update(String  xmlFile, String oldText, String newText ) throws  IOException {
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
	 catch (IOException ioe) {
	   ioe.printStackTrace();
	 }
	 // Get the TextNode containing "Text" and update it
	 Text t = (Text) doc.getFirstChild().getFirstChild();
	 t.setData(newText);

   }



  static void deleteAllElements( Node root, String deleteNode ) {

	 Node child = root.getFirstChild();
	 while(child != null) {
			 if (child instanceof Element && child.getNodeName() == deleteNode) {
			   child = root.removeChild( child );
			 }
			 else {
			   deleteAllElements(child);
			 }
			 child = child.getNextSibling();
	 }
   }
   static void deleteAllElements( Node root) {
	 Node child = root.getFirstChild();
	 while(child != null) {
			 if (child instanceof Element && child.getNodeName() == "keyword") {
			   child = root.removeChild( child );
			 }
			 else {
			   deleteAllElements( child);
			 }
			 child = child.getNextSibling();
	 }
   }


}
