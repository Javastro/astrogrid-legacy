package org.astrogrid.portal.query;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.*;
import org.astrogrid.portal.generated.registry.client.*;
import java.util.ArrayList;


public class QueryRegistryInformation {

	private static final String QUERY_ELEMENT = "query";
	private static final String SELECTIONSEQ_ELEMENT = "selectionSequence";
	private static final String SELECTION_ELEMENT = "selection";
	private static final String ITEM_ATTR = "item";
	private static final String ITEMOP_ATTR = "itemop";
	private static final String VALUE_ATTR = "value";

	public static String getAllDataSetInformationFromRegistry() {
		//the thing about this string it is always going to be the same.
		/*
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			//dbf.setValidating(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.getDOMImplementation().createDocument(null,QUERY_ELEMENT,null);
			Element seq = doc.createElement(SELECTIONSEQ_ELEMENT);
			doc.getDocumentElement().appendChild(seq);
			Element sel = doc.createElement(SELECTION_ELEMENT);
			//sel.setAttribute(ITEM_ATTR,"resourceType");
			sel.setAttribute(ITEM_ATTR,"type");
			sel.setAttribute(ITEMOP_ATTR,"EQ");
			sel.setAttribute(VALUE_ATTR,"archive");
			//sel.setAttribute(ITEM_ATTR,"id");
			//sel.setAttribute(ITEMOP_ATTR,"EQ");
			//sel.setAttribute(VALUE_ATTR,"all");
			seq.appendChild(sel);
			//same goes for resourcetype=archive
			ByteArrayOutputStream sw = new ByteArrayOutputStream();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer output = tf.newTransformer();
			output.transform(new DOMSource(doc), new StreamResult(sw));
			//return "<query><selectionSequence><selection item='community' itemOp='EQ' value='MSSL'/><selectionOp op='AND'/><selectionSequence><selectionSequence><selection item='publisher' itemOp='NE' value='E. Auden'/> <selectionOp op='OR'/> <selection item='name' itemOp='EQ' value='Carl Foley'/></selectionSequence> </selectionSequence> <selectionOp op='OR'/> <selectionSequence> <selection item='id' itemOp='EQ' value='LEDAS'/></selectionSequence></selectionSequence></query>";
			//return sw.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		*/
		return 	"<query><selectionSequence>" +				"<selection item='searchElements' itemOp='EQ' value='all'/>" +				"<selectionOp op='$and$'/>" +				"<selection item='ticker' itemOp='EQ' value='all'/>" +				"</selectionSequence></query>";
	}

	public static String getAllContentInformationFromRegistryForDataSet(String dsName) {
		/*
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			//dbf.setValidating(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.getDOMImplementation().createDocument(null,QUERY_ELEMENT,null);
			Element seq = doc.createElement(SELECTIONSEQ_ELEMENT);
			doc.getDocumentElement().appendChild(seq);
			Element sel = doc.createElement(SELECTION_ELEMENT);
			sel.setAttribute(ITEM_ATTR,"id");
			sel.setAttribute(ITEMOP_ATTR,"EQ");
			sel.setAttribute(VALUE_ATTR,dsName);
			//sel.setAttribute(ITEM_ATTR,"id");
			//sel.setAttribute(ITEMOP_ATTR,"EQ");
			//sel.setAttribute(VALUE_ATTR,"all");
			seq.appendChild(sel);
			//same goes for resourcetype=archive
			ByteArrayOutputStream sw = new ByteArrayOutputStream();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer output = tf.newTransformer();
			output.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		*/
		return 	"<query><selectionSequence>" +				"<selection item='searchElements' itemOp='EQ' value='content'/>" +				"<selectionOp op='$and$'/>" +				"<selection item='ticker' itemOp='EQ' value='" + dsName + "'/>" +				"</selectionSequence></query>"; 
	}

	public static Object[] getDataSetItemsFromRegistryResponse(String response) {
		//okay lets cheat instead of examining the xml with a sax parser lets just substring this thing.
		ArrayList items = new ArrayList();
		int start = 0,end = 0;
		String temp = "";
		while( (end = response.indexOf("item='ticker'",end)) != -1) {
			start = response.indexOf("value=",end) + 7;
			end = response.indexOf("'",start+1);
			if(end <= 0) { end = response.indexOf("\"",start+1); }
			items.add( (temp = response.substring(start,end)));
			end++;
		}
		return items.toArray();
	}


	public static ArrayList getItemsFromRegistryResponse(String response) {
		//okay lets cheat instead of examining the xml with a sax parser lets just substring this thing.
		ArrayList items = new ArrayList();
		int start = 0,end = 0;
		String temp = "";
		temp = "<recordKeyPair item='metadataType' value='content'/>";
		end = response.indexOf(temp) + temp.length(); 
		DataSetColumn dsColumn = null;
		while(response.indexOf("item=",end) != -1) {
			start = response.indexOf("item=",end) + 6;
			end = response.indexOf("\"",start+1);
			if(end <= 0) { end = response.indexOf("'",start+1); }
			temp = response.substring(start,end);
			if(temp.equals("ucd")) {
				start = response.indexOf("value=",end) + 7;
				end = response.indexOf("\"",start+1);
				if(end <= 0) { end = response.indexOf("'",start+1); }
				temp = response.substring(start,end);
				dsColumn = new DataSetColumn(temp,"UCD");
				if(items.indexOf(dsColumn) == -1) {
					items.add(dsColumn);
				}
			}else {
				dsColumn = new DataSetColumn(temp,"COLUMN");
				if(items.indexOf(dsColumn) == -1) {
					items.add(dsColumn);
				}
			}
			end++;
		}
		return items;
	}

	public static String sendRegistryQuery(String req) {
		RegistryInterface_Port binding;
		String xmlBuildResult = null;
		try {
			CreateRequest cr = new CreateRequest();
			  System.out.println("The registry XmL going to the webservice is = " + req);
			  binding = new RegistryInterfaceLocator().getRegistryInterfacePort();
			  //binding.setTimeout(30000);
			  String response = binding.submitQuery(req);
			  System.out.println("the response from the call to the webservice = " + response);
			  return response;
		  }catch (Exception e) {
			  e.printStackTrace();
		  }
		  return null;
	}
}