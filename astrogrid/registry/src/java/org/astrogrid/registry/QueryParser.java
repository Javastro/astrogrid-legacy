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
public class QueryParser
 {
 	static boolean xmlQuery = true;
 	static boolean dbQuery = false;

	public static String parseQuery (String query) {

	  Reader reader = new StringReader(query);
	  InputSource inputSource = new InputSource(reader);
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
		if (docElement.getFirstChild().getNodeName().equals("selectionSequence")){
			if (dbQuery == true){
				queryResponse = xmlToSQL(docElement.getFirstChild());
			}
			/*
			if (xmlQuery == true){
				queryResponse = xmlToXML(docElement.getFirstChild());
			}
			*/

			queryResponse = xmlToSQL(docElement.getFirstChild());
		}	  	  
	  }
	  catch (Exception e) {
		queryResponse = "";
	  }
	  String response = "<queryResponse><responseRecord><recordKeyPair item='title' value='Oke (1990) Spectrophotometric Standards'/><recordKeyPair item='publication date' value='1990'/><recordKeyPair item='tableRows' value='25'/><recordKeyPair item='keyword' value='optical'/><recordKeyPair item='Vmax' value='16.14'/><recordKeyPair item='Vmin' value='7.04'/></responseRecord><responseRecord><recordKeyPair item='title' value='HST Spectrophotometric Standards'/><recordKeyPair item='publication date' value='1996'/><recordKeyPair item='tableRows' value='26'/><recordKeyPair item='keyword' value='uv'/><recordKeyPair item='keyword' value='optical'/><recordKeyPair item='flux' value='*'/></responseRecord></queryResponse>";
	  //return sqlQuery+ queryResponse;
	  return response;
	}

	private static String xmlToSQL(Node node) {
		String response = "";
		response = response + "(\n";
		NodeList nlSS = node.getChildNodes();
		for (int z=0; z < nlSS.getLength(); z++){
			if (nlSS.item(z).getNodeName().equals("selection")){
				response = response + "(";
				response = response + ((Element) nlSS.item(z)).getAttribute("item");
				response = response + " "+((Element) nlSS.item(z)).getAttribute("itemOp")+ " ";
				response = response + ((Element) nlSS.item(z)).getAttribute("value");
				response = response + ")";
			}
			if (nlSS.item(z).getNodeName().equals("selectionOp")){
				response = response + "\n"+((Element) nlSS.item(z)).getAttribute("op")+ "\n";
			}
			if (nlSS.item(z).getNodeName().equals("selectionSequence")){
				response = response + xmlToSQL(nlSS.item(z));
			}			
		}
		response = response + "\n)";
		return response;
	}

   public static void main(String args[]){
    String inputQuery = "<query><selectionSequence><selection item='spectral type' itemOp='EQ' value='white dwarf stars'/><selectionOp op='AND'/><selection item='keyword' itemOp='EQ' value='spectrophotometric standards'/><selectionOp op='AND'/><selectionSequence><selectionSequence><selection item='V magnitude' itemOp='GE' value='8'/><selectionOp op='AND'/><selection item='V magnitude' itemOp='LE' value='12'/></selectionSequence><selectionOp op='OR'/><selection item='flux' itemOp='EQ' value='*'/></selectionSequence><selectionOp op='AND'/><selectionSequence><selection item='wavelength' itemOp='EQ' value='optical'/><selectionOp op='OR'/><selection item='wavelength' itemOp='EQ' value='uv'/></selectionSequence></selectionSequence></query>";
    String response = QueryParser.parseQuery(inputQuery);
    System.out.println(response);
   }
 }

