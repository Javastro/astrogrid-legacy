package org.astrogrid.portal.query;

/*
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.*;
*/
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
		return 	"<query><selectionSequence>" +				"<selection item='searchElements' itemOp='EQ' value='all'/>" +				"<selectionOp op='$and$'/>" +				"<selection item='shortName' itemOp='EQ' value='all'/>" +				"</selectionSequence></query>";
	}

	public static String getAllContentInformationFromRegistryForDataSet(String dsName) {
		return 	"<query><selectionSequence>" +				"<selection item='searchElements' itemOp='EQ' value='content'/>" +				"<selectionOp op='$and$'/>" +				"<selection item='shortName' itemOp='EQ' value='" + dsName + "'/>" +				"</selectionSequence></query>";
	}

	public static Object[] getDataSetItemsFromRegistryResponse(String response) {
		//okay lets cheat instead of examining the xml with a sax parser lets just substring this thing.
		ArrayList items = new ArrayList();
		int start = 0,end = 0;
		String temp = "";
		while( (end = response.indexOf("item='shortName'",end)) != -1) {
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
}