package org.astrogrid.mySpace.mySpaceUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.lang.StringBuffer;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.io.StringReader ;
import java.util.HashMap;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;

import org.apache.log4j.Logger;

import org.apache.axis.AxisProperties;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceMessage;
import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;

/**
 * @author C L QIN
 * @version Iteration 2.
 */

public class MySpaceUtils {
    private static Logger logger = Logger.getLogger(MySpaceUtils.class);
    private static boolean DEBUG = true;
	private static String catalinaHome = AxisProperties.getProperty("catalina.home");
	private static String responseTemplate = catalinaHome+"/conf/astrogrid/mySpace/" +"MySpaceManagerResponse.properties";
	private static String requestTemplate = "catalinaHome"+"/conf/astrogrid/mySpace/"+"MySpaceManagerRequest.properties";
    private static Properties conProperties = new Properties();
    private static final String RESPONSE = "MYSPACEMANAGER_RESPONSE";
	
	public static String readFromFile( File file ) {
		FileReader fileReader = null;
		try{
			if (file == null || !file.exists()) {
				//throw new IOException("File does not exist");
				return "";
			}
			//open file to read from
			fileReader = new FileReader(file);
			
			StringBuffer strbufFileContent = new StringBuffer(8192);
			char charFileText[] = new char[2048];
			
			int iTotalCharactersRead = 0;
			int iCharactersRead = 0;
			
			while((iCharactersRead = fileReader.read(charFileText,0,2048)) != -1){
				//add number of characters read to the total read
				iTotalCharactersRead += iCharactersRead;
				
				//add the characters read to the buffer
				strbufFileContent.append(charFileText);
			}
			
			//size buffer to fit data
			strbufFileContent.setLength(iTotalCharactersRead);
			
			//return the data as a String
			return strbufFileContent.toString();
		}catch (Exception e) {
			MySpaceMessage msMessage = new MySpaceMessage("ERROR_READING_FILE",e.toString());
			return "";
		}finally{
			try{
				if(fileReader != null){
					fileReader.close();
				}
			}catch(Exception e){
				return "";
			}
		}
	}
	
    public String buildMySpaceManagerResponse(DataItemRecord record){
		//conProperties = new Properties();
		String response = "";
		try {
			FileInputStream istream = new FileInputStream( responseTemplate );
			conProperties.load(istream);
			istream.close();
			if (DEBUG)  logger.debug( "buildMySpaceManagerResponse: " +conProperties.toString() ) ;
			String template = conProperties.getProperty( RESPONSE );
			if (DEBUG)  logger.debug("buildMySpaceManagerResponse = "+response);
			
			Object [] inserts = new Object[11] ;
			inserts[0] = "a"; //status
			inserts[1] = "b"; //details
			inserts[2] = "c"; //dataitemrecord.getDataItemName
			inserts[3] = "d"; //dataitemrecord.getDataItemID
			inserts[4] = "e"; //dataitemrecord.getDataItemFile
			inserts[5] = "f"; //dataitemrecord.getOwnerID
			inserts[6] = "g"; //dataitemrecord.getCreationDate
			inserts[7] = "h"; //dataitemrecord.getExpiryDate
			inserts[8] = "i"; //dataitemrecord.getSize
			inserts[9] = "j"; //dataitemrecord.getType
			inserts[10] = "k";// dataitemrecord.getPermissionsMask
					
			response = MessageFormat.format( template, inserts ) ;
		}
		catch ( IOException ex ) {
			if (DEBUG)  logger.error("MYSPACEUTILS IO EXCEPTION :" +ex.getMessage());
			
		}
		return response;
	}
	
	public HashMap getRequestAttributes( String xmlRequest ){
		HashMap request = new HashMap();
		Document doc = parseRequest( xmlRequest );
		Node checker;
		checker = doc.getDocumentElement();
		boolean ascending = false;
		int level = 1;
		while (true) {
			logger.debug("Now trying to walk the dom tree..");
			if (!ascending) {
				//indentToLevel(level);
				//printNodeInfo(checker);
				}
				if ((checker.hasChildNodes()) && (!ascending)) {
					checker = checker.getFirstChild();
					logger.debug("GOING DOWN"+checker.getNodeName() +"NODETYPE="+checker.getNodeType());
					if(checker.getNodeType()==1){
						String text = "";
						if(checker.getFirstChild()!=null){
							if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
								text = checker.getFirstChild().getNodeValue();
							}
						}
						logger.debug("NODENAME: "+checker.getNodeName() +"NODEVALUE: "+checker.getNodeValue() +"TEXTVALUE: "+text);
						request.put(checker.getNodeName(),checker.getNodeValue());
					//printAttributes((Node)checker);
					}
					ascending = false;
					level++;
					}
					else if (checker.getNextSibling() != null) {
						checker= checker.getNextSibling();
						String text = "";
						if(checker.getFirstChild()!=null){
							if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
								text = checker.getFirstChild().getNodeValue();
							}
						}
						logger.debug("NODENAME: "+checker.getNodeName() +"NODEVALUE: "+checker.getNodeValue() +"TEXTVALUE: "+text);
						ascending = false;
						logger.debug("GOING RIGHT");
						}
					else if (checker.getParentNode() != null) {
						checker= checker.getParentNode();
						String text = "";
						if(checker.getFirstChild()!=null){
							if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
								text = checker.getFirstChild().getNodeValue();
							}
						}
						logger.debug("NODENAME: "+checker.getNodeName() +"NODEVALUE: "+checker.getNodeValue() +"TEXTVALUE: "+text);
						ascending = true;
						level--;
						logger.debug("GOING UP");
						}
						else {
							break;
							}
		}
		return request;
	}
	
	
	
	private Document parseRequest ( String xmlRequest){
		if( DEBUG ) logger.debug( "Tring to parse MySpace xmlRequest.." +xmlRequest) ;
		
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
	       
		try {
		   builder = factory.newDocumentBuilder();
		   InputSource requestSource = new InputSource( new StringReader( xmlRequest ) );
		   doc = builder.parse( requestSource );
		}
		catch ( Exception ex ) {
			MySpaceMessage message = new MySpaceMessage( "PARSE_REQUEST_ERROR", ex.toString()) ; 
			logger.error( message.toString(), ex ) ;
		} 		
		return doc ;	
	}
	
	
	public static void main( String [] args ) {
	   }
	
}