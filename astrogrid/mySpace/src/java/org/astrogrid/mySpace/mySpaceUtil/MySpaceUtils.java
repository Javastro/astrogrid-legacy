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
import java.util.Calendar;
import java.io.PrintWriter;
import java.io.FileOutputStream;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;

import org.apache.log4j.Logger;

import org.apache.axis.AxisProperties;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceMessage;
import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;
import org.astrogrid.mySpace.mySpaceStatus.*;

/**
 * @author C L QIN
 * @version Iteration 2.
 */

public class MySpaceUtils {
    private static Logger logger = Logger.getLogger(MySpaceUtils.class);
    private static boolean DEBUG = true;
	private static String catalinaHome = AxisProperties.getProperty("catalina.home");
	private static String requestResponseTemplate = catalinaHome+"/conf/astrogrid/mySpace/" +"MSManagerRequestResponse.properties";
	//private static String responseTemplate = catalinaHome+"/conf/astrogrid/mySpace/" +"MySpaceManagerResponse.properties";
	//private static String responseTemplate_header = catalinaHome+"/conf/astrogrid/mySpace/" +"MSResponseHeader.properties";
	//private static String responseTemplate_element = catalinaHome+"/conf/astrogrid/mySpace/" +"MSResponseElements.properties";
	//private static String responseTemplate_foot = catalinaHome+"/conf/astrogrid/mySpace/" +"MSResponseFooter.properties";
	//private static String requestTemplate = "catalinaHome"+"/conf/astrogrid/mySpace/"+"MySpaceManagerRequest.properties";
    private static Properties conProperties = new Properties();
    private static final String RESPONSE = "MYSPACEMANAGER_RESPONSE";
    private static final String D_RESPONSE_HEAD ="MYSPACEMANAGER_D_RESPONSE_HEAD";
    private static final String D_RESPONSE_ELEMENT ="MYSPACEMANAGER_D_ELEMENT";
    private static final String D_RESPONSE_FOOT ="MYSPACEMANAGER_D_FOOT";
	private static MySpaceStatus msstatus = new MySpaceStatus();
	private static String response = " ";
	private static final String SUCCESS = "SUCCESS";
	private static final String FAULT = "FAULT";
	
	public static String readFromFile( File file ) {
		FileReader fileReader = null;
		try{
			if (file == null || !file.exists()) {
				//throw new IOException("File does not exist");
				MySpaceMessage msMessage = new MySpaceMessage("FILE_NOT_EXIST");
				msstatus.addCode(MySpaceStatusCode.FILE_NOT_EXIST,MySpaceStatusCode.ERROR);
				response = FAULT+MySpaceStatusCode.FILE_NOT_EXIST;
				return response;
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
			msstatus.addCode(MySpaceStatusCode.ERROR_READING_FILE,MySpaceStatusCode.ERROR);
			response = FAULT+MySpaceStatusCode.ERROR_READING_FILE;
			return response;
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
	
	public static boolean writeToFile( File file, String theString ){
			PrintWriter printWriter = null;    	
			try{
				//open file to write into
				printWriter = new PrintWriter(new FileOutputStream(file));    	       	    
				//write to file
				printWriter.println(theString);		    
				return true;				    
			}catch (Exception e) {
				return false;
			}finally{
				//close file
				try{
					if(printWriter != null) {
						printWriter.close();
					}
				}catch(Exception e){					
				}
			}
	   }
	
    public String buildMySpaceManagerResponse(DataItemRecord record, String status, String details, String dataHolderURI){
		//conProperties = new Properties();
		String response = "";
		try {
			FileInputStream istream = new FileInputStream( requestResponseTemplate );
			conProperties.load(istream);
			istream.close();
			if (DEBUG)  logger.debug( "buildMySpaceManagerResponse: " +conProperties.toString() ) ;
			String template = conProperties.getProperty( RESPONSE );
			if (DEBUG)  logger.debug("buildMySpaceManagerResponse = "+template);
			
			Object [] inserts = new Object[12] ;
			inserts[0] = status;
			inserts[1] = details;
			if(record != null){
				inserts[2] = Calendar.getInstance().getTime();
				inserts[3] = record.getDataItemName();				
				inserts[4] = new Integer(record.getDataItemID()).toString();
				inserts[5] = record.getOwnerID();
				inserts[6] = record.getCreationDate();
				inserts[7] = record.getExpiryDate();
				inserts[8] = new Integer(record.getSize()).toString();
				inserts[9] = new Integer(record.getType()).toString();
				inserts[10] = record.getPermissionsMask();				
			}
			if (!dataHolderURI.equals(null))  inserts[11] = dataHolderURI;
			if( DEBUG ) logger.debug("UTIL: DATAHOLDERURI ="+dataHolderURI);		
		 
			response = MessageFormat.format( template, inserts ) ;
			return response;
		}
		catch ( IOException ex ) {
			if (DEBUG)  logger.error("MYSPACEUTILS IO EXCEPTION :" +ex.getMessage());
			MySpaceMessage msMessage = new MySpaceMessage("ERR_IO_BUILD_RESPONS",ex.toString());
			msstatus.addCode(MySpaceStatusCode.ERR_IO_BUILD_RESPONS,MySpaceStatusCode.ERROR);
			response = FAULT+MySpaceStatusCode.ERR_IO_BUILD_RESPONS;
			return response;
		}
		
	}
	
	public String buildMySpaceManagerResponseFooter(){
		//conProperties = new Properties();
		String response = "";
		try {
			//MySpaceUtils.readFromFile(new File(responseTemplate_foot));
			FileInputStream istream = new FileInputStream( requestResponseTemplate );
			conProperties.load(istream);
			istream.close();
			if (DEBUG)  logger.debug( "buildMySpaceManagerResponse_Footer: " +conProperties.toString() ) ;
			response = conProperties.getProperty( D_RESPONSE_FOOT );
			if (DEBUG)  logger.debug("buildMySpaceManagerResponse_Footer = "+response);
	
			return response;
		}
		catch ( IOException ex ) {
			if (DEBUG)  logger.error("MYSPACEUTILS IO EXCEPTION :" +ex.getMessage());
			MySpaceMessage msMessage = new MySpaceMessage("ERR_IO_BUILD_RESPONS",ex.toString());
			msstatus.addCode(MySpaceStatusCode.ERR_IO_BUILD_RESPONS,MySpaceStatusCode.ERROR);
			response = FAULT+MySpaceStatusCode.ERR_IO_BUILD_RESPONS;
			return response;
		}
		
	}
	
	public String buildMySpaceManagerResponseHeader(String status, String details){
		//conProperties = new Properties();
		String response = "";
		try {
			FileInputStream istream = new FileInputStream( requestResponseTemplate );
			conProperties.load(istream);
			istream.close();
			if (DEBUG)  logger.debug( "buildMySpaceManagerResponse_Header: " +conProperties.toString() ) ;
			String template = conProperties.getProperty( D_RESPONSE_HEAD );
			if (DEBUG)  logger.debug("buildMySpaceManagerResponse_Header = "+template);
			
			Object [] inserts = new Object[3] ;
			inserts[0] = status;
			inserts[1] = details;
			inserts[2] = Calendar.getInstance().getTime();
					
			response = MessageFormat.format( template, inserts ) ;
			return response;
		}
		catch ( IOException ex ) {
			if (DEBUG)  logger.error("MYSPACEUTILS IO EXCEPTION :" +ex.getMessage());
			MySpaceMessage msMessage = new MySpaceMessage("ERR_IO_BUILD_RESPONS",ex.toString());
			msstatus.addCode(MySpaceStatusCode.ERR_IO_BUILD_RESPONS,MySpaceStatusCode.ERROR);
			response = FAULT+MySpaceStatusCode.ERR_IO_BUILD_RESPONS;
			return response;
		}
		
	}	
	
	public String buildMySpaceManagerResponseElement(DataItemRecord record, String status, String details){
		//conProperties = new Properties();
		String response = "";
		try {
			FileInputStream istream = new FileInputStream( requestResponseTemplate );
			conProperties.load(istream);
			istream.close();
			if (DEBUG)  logger.debug( "buildMySpaceManagerResponse_Element: " +conProperties.toString() ) ;
			String template = conProperties.getProperty( D_RESPONSE_ELEMENT );
			if (DEBUG)  logger.debug("buildMySpaceManagerResponse_Element = "+template);
			
			Object [] inserts = new Object[8] ;

			if(record != null){
				inserts[0] = record.getDataItemName();				
				inserts[1] = new Integer(record.getDataItemID()).toString();
				inserts[2] = record.getOwnerID();
				inserts[3] = record.getCreationDate();
				inserts[4] = record.getExpiryDate();
				inserts[5] = new Integer(record.getSize()).toString();
				inserts[6] = new Integer(record.getType()).toString();
				inserts[7] = record.getPermissionsMask();
			}
					
			response = MessageFormat.format( template, inserts ) ;
			return response;
		}
		catch ( IOException ex ) {
			if (DEBUG)  logger.error("MYSPACEUTILS IO EXCEPTION :" +ex.getMessage());
			MySpaceMessage msMessage = new MySpaceMessage("ERR_IO_BUILD_RESPONS",ex.toString());
			msstatus.addCode(MySpaceStatusCode.ERR_IO_BUILD_RESPONS,MySpaceStatusCode.ERROR);
			response = FAULT+MySpaceStatusCode.ERR_IO_BUILD_RESPONS;
			return response;
		}
		
	}		
	
	public HashMap getRequestAttributes( String xmlRequest ){
		HashMap request = new HashMap();
		try{		
			Document doc = parseRequest( xmlRequest );
			Node checker;
			checker = doc.getDocumentElement();
			boolean ascending = false;
			int level = 1;
			while (true) {
				logger.debug("Now trying to walk the dom tree..");
					
					if (checker!=null && (checker.hasChildNodes()) && (!ascending)) {
						checker = checker.getFirstChild();
						logger.debug("GOING DOWN"+checker.getNodeName() +"NODETYPE="+checker.getNodeType());
						if(checker.getNodeType()==1){
							String text = "";
							if(checker.getFirstChild()!=null){
								if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
									text = checker.getFirstChild().getNodeValue();
									logger.debug("BEFORE.PUT.DOWN" +checker.getNodeName()+" TEXT " +text);
									
									request.put(checker.getNodeName(), text);
									
									logger.debug("NODENAME: "+checker.getNodeName() +",  TEXTVALUE: "+text);
								}
							}
						}
						ascending = false;
						level++;
						}
						else if (checker!=null && checker.getNextSibling() != null) {
							checker= checker.getNextSibling();
							String text = "";
							logger.debug(	"SIBBLING...");
							if(checker.getFirstChild()!=null){
								if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
									text = checker.getFirstChild().getNodeValue();
									logger.debug("BEFORE.PUT.SIBLING");
									
									request.put(checker.getNodeName(), text);
									
									logger.debug("NODENAME: "+checker.getNodeName() +",  TEXTVALUE: "+text);
								}
							}
							ascending = false;
							logger.debug("GOING RIGHT");
							}
						else if (checker !=null && checker.getParentNode() != null) {
							checker= checker.getParentNode();
							String text = "";
							if(checker.getFirstChild()!=null){
								if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
									text = checker.getFirstChild().getNodeValue();
									logger.debug("BEFORE.PUT.UP");
									
									request.put(checker.getNodeName(), text);
									
									logger.debug("NODENAME: "+checker.getNodeName() +",  TEXTVALUE: "+text);
								}
							}
							ascending = true;
							level--;
							logger.debug("GOING UP");
							}
							else {
								logger.debug("BREAK!");
								break;
								}
			}
		}catch(Exception e){
			logger.debug("ERROR Walking DOM TREE: "+e.toString());
			//MySpaceMessage msMessage = new MySpaceMessage("FILE_NOT_EXIST");
			//status.addCode(MySpaceStatusCode.FILE_NOT_EXIST,MySpaceStatusCode.ERROR);
			//response = FAULT+MySpaceStatusCode.FILE_NOT_EXIST;
			//return response;
			
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