package org.astrogrid.registry.xquery;


import de.gmd.ipsi.xql.*;
import de.gmd.ipsi.domutil.*;
import org.w3c.dom.*;
import java.net.URL;
import java.io.*;
import java.net.MalformedURLException;
import java.io.IOException;
import de.gmd.ipsi.domutil.DOMParseException;

public class RunXQueryIpsi {

  public static void main(String[] args) throws DOMParseException,MalformedURLException {

	String xquery = "http://143.117.59.17/~astrogrid/xmlfiles/registry.xquery";

	//Creation of a Dom document
	Document doc = DOMUtil.createDocument();

	try {

	  // URL variable stores the address of XML file to parse
	  URL xmlFileName = new URL("http://143.117.59.17/~astrogrid/xmlfiles/registry.xml");
	  InputStream xfl = xmlFileName.openStream();

	  DOMUtil.parseXML(
		  xfl,
		  doc,
		  false, // Parse mode: true = validating, false = non-validating
		  DOMUtil.SKIP_IGNORABLE_WHITESPACE
		  );
	}
	catch (MalformedURLException e) {}
	catch (DOMParseException e) {
	  e.printStackTrace();
	}
	catch (IOException ex) {}

	String xqueryString = Transform.URLFileToString(xquery);

	Document resultDoc = DOMUtil.createDocument();
	Element root = resultDoc.createElement("root");
	resultDoc.appendChild(root);
	XQL.execute(xqueryString, doc, root);

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