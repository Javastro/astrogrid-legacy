/*
 * Created on 26-Aug-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry;
import de.gmd.ipsi.pdom.PDOM;
import de.gmd.ipsi.pdom.PDocument;
import de.gmd.ipsi.domutil.*;
import org.w3c.dom.*;

/**
 * @author Elizabeth Auden
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class XQuery {
	Document doc = DOMUtil.createDocument();
	public void addNode(String  newNode) {
	  //  insert Nodes

	  Element e = doc.createElement("root");
	  e.appendChild(doc.createTextNode(newNode));
	  doc.appendChild(e);

	  // Synchronize PDOM file and in-memory buffers
	  //doc.commit();

	  // loose object reference to be sure we really re-open the document.
	  doc = null;

	  //call garbage recollection method
	  garbageRecollection();

	 }


	 public void deleteNode(String deleteNode) {

	   // create a node and delete it

	   Element e = doc.createElement("garbage");

	   doc.getFirstChild().removeChild(e);

	   //call garbage recollection method
	   garbageRecollection();

	 }

	public void updateNode(String updateNode) {

	 // reopen the document

     try {
		 doc = new PDocument("newdoc.pdom");
     }
     catch (Exception e){
     	System.out.println(e);
     }
	 // Get the TextNode containing "Hello World" and update it
	 Text t = (Text) doc.getFirstChild().getFirstChild();
	 t.setData("This is theupdate node");

	 //call garbage recollection method
	  garbageRecollection();

	}

	public void garbageRecollection() {
		// loose object reference to be sure we really re-open
	 doc = null;
	 PDOM.clearCache();

	 // ... and do the garbage collection
	 PDOM.collectDOMFileGarbage("newdoc.pdom");

	}

}
