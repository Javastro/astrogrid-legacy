package org.astrogrid.registry.xquery;

import java.io.*;

import org.w3c.dom.*;
import de.gmd.ipsi.domutil.*;
import de.gmd.ipsi.xql.*;

public class RunXQueryIpsi {
  //timer to measure performace
  static long startTime = System.currentTimeMillis();

  final static String xmlFileName = "D:\\astrogrid\\query.xml";
  final static String query = Transform.fileToString("D:\\astrogrid\\portalQuery.xql");

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
	catch ( DOMParseException e) {
	  e.printStackTrace();
	}
	catch (FileNotFoundException e) {
	  e.printStackTrace();
	}

	Document resultDoc = DOMUtil.createDocument();
	Element root = resultDoc.createElement("root");
	resultDoc.appendChild(root);
	XQL.execute(query, doc, root);

	try{
	  XMLWriter out = new XMLWriter(System.out);
	  out.formatOutput(true);
	  out.write(resultDoc);
	  out.writeln();
	  out.flush();
	}
	catch (IOException e) {
	  e.printStackTrace();
	}

	//timer calculation
	long stopTime = System.currentTimeMillis();
	long runTime = stopTime - startTime;
	System.out.println("Run time: " + runTime);
  }
}