package org.astrogrid.registry.xquery;


import de.gmd.ipsi.xql.*;
import de.gmd.ipsi.domutil.*;
import org.w3c.dom.*;


import java.io.*;

public class RunXQueryIpsi {

  final static String xmlFileName = "http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/xquery/registry.xml";
  final static String xquery = Transform.fileToString("http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/xquery/registry.xquery");

  public static void main(String[] args) {
	   //Creation of a Dom document
	   Document doc = DOMUtil.createDocument();

	   try {
		 DOMUtil.parseXML(
			 new FileInputStream(xmlFileName),
			 doc,
			 false,
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
	   XQL.execute(xquery, doc, root);

	   try {
		 XMLWriter out = new XMLWriter(System.out);
		 out.formatOutput(true);
		 out.write(resultDoc);
		 out.writeln();
		 out.flush();
	   }
	   catch (IOException e) {
		 e.printStackTrace();
	   }
  }
}