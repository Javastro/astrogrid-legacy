package org.astrogrid.mySpace.mySpaceUtil;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.text.MessageFormat;
import java.io.StringReader ;
import java.util.HashMap;
import java.util.Calendar;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;

//import org.apache.log4j.Logger;

import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;
import org.astrogrid.mySpace.mySpaceStatus.*;
import org.astrogrid.mySpace.mySpaceManager.MMC;
import org.astrogrid.Configurator;
import org.astrogrid.i18n.*;

/**
 * @author C L QIN
 * @version Iteration 2.
 */

public class MySpaceUtils {
    //private static Logger logger = Logger.getLogger(MySpaceUtils.class);
	private static Logger logger = new Logger (true, true, true,
		  "./myspace.log");
    private static boolean DEBUG = true;
	private static MySpaceStatus msstatus = new MySpaceStatus();
	private static String response = " ";

	public static String readFromFile( File file ) {
		FileReader fileReader = null;
		try{
			if (file == null || !file.exists()) {
				//throw new IOException("File does not exist");
				msstatus.addCode(MySpaceStatusCode.AGMSCE01047,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, new MySpaceUtils().getComponentName());
				response = MMC.FAULT+MySpaceStatusCode.AGMSCE01047;
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
			logger.appendMessage("Exception caught while reading from file MySpaceUtils.readFromFile: "+e.toString());
			msstatus.addCode(MySpaceStatusCode.AGMMCE00103,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, new MySpaceUtils().getComponentName());
			response = MMC.FAULT+MySpaceStatusCode.AGMMCE00103;
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
				//printWriter = new PrintWriter(new FileOutputStream(file));    
				printWriter = new PrintWriter(new BufferedWriter( new FileWriter (file, true)));	       	 
				if ( DEBUG )  logger.appendMessage("MySpaceUtil file is:"+file+";  CONTENTS :"+theString);
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

		String response = "";
		try {
			String requestTemplate = MMC.getProperty( MMC.RESPONSE, MMC.CATLOG) ;//this should get the xml template
			
			if ( DEBUG )  logger.appendMessage("testing using jConfig: "+requestTemplate);

			if ( DEBUG )  logger.appendMessage("buildMySpaceManagerResponse = "+requestTemplate);
			
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
			if( DEBUG )  logger.appendMessage("UTIL: DATAHOLDERURI ="+dataHolderURI);		
		 
			response = MessageFormat.format( requestTemplate, inserts ) ;
			return response;
		}
		catch ( Exception ex ) {
			if (DEBUG)  logger.appendMessage("MYSPACEUTILS ERROR_READING_FILE :" +ex.getMessage());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMSCE00032", this.getComponentName()) ;
			msstatus.addCode(MySpaceStatusCode.AGMMCE00103,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			response = MMC.FAULT+generalMessage.toString();
			return response;
		}
		
	}
	
	public String buildMySpaceManagerResponseFooter(){
		String response = "";
		try {
			response = MMC.getProperty( MMC.D_RESPONSE_FOOT, MMC.CATLOG) ;//this should get the xml template

			if ( DEBUG )  logger.appendMessage("buildMySpaceManagerResponse_Footer = "+response);
	
			return response;
		}
		catch ( Exception ex ) {
			if (DEBUG)  logger.appendMessage("MYSPACEUTILS ERROR_READING_FILE :" +ex.getMessage());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMSCE00032", this.getComponentName()) ;
			msstatus.addCode(MySpaceStatusCode.AGMMCE00103,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			response = MMC.FAULT+generalMessage.toString();
			return response;
		}
		
	}
	
	public String buildMySpaceManagerResponseHeader(String status, String details){
		String response = "";
		try {
			String requestTemplate = MMC.getProperty( MMC.D_RESPONSE_HEAD, MMC.CATLOG) ;//this should get the xml template

			if ( DEBUG )  logger.appendMessage("buildMySpaceManagerResponse_Header = "+requestTemplate);
			
			Object [] inserts = new Object[3] ;
			inserts[0] = status;
			inserts[1] = details;
			inserts[2] = Calendar.getInstance().getTime();
					
			response = MessageFormat.format( requestTemplate, inserts ) ;
			return response;
		}
		catch ( Exception ex ) {
			if (DEBUG)  logger.appendMessage("MYSPACEUTILS ERROR_READING_FILE :" +ex.getMessage());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMSCE00032", this.getComponentName()) ;
			msstatus.addCode(MySpaceStatusCode.AGMMCE00103,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			response = MMC.FAULT+generalMessage.toString();
			return response;
		}
		
	}	
	
	public String buildMySpaceManagerResponseElement(DataItemRecord record, String status, String details){

		try {
			String requestTemplate = MMC.getProperty( MMC.D_RESPONSE_ELEMENT, MMC.CATLOG) ;//this should get the xml template

			if (DEBUG)  logger.appendMessage("buildMySpaceManagerResponse_Element = "+requestTemplate);
			
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
					
			response = MessageFormat.format( requestTemplate, inserts ) ;
			return response;
		}
		catch ( Exception ex ) {
			if (DEBUG)  logger.appendMessage("MYSPACEUTILS ERROR_READING_FILE :" +ex.getMessage());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMSCE00032", this.getComponentName()) ;
			msstatus.addCode(MySpaceStatusCode.AGMMCE00103,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			response = MMC.FAULT+generalMessage.toString();
			return response;
		}
		
	}		

	/**
	 * Returns a <code>HashMap</code> with key/value filled in regarding
	 * to the request template.
	 * <p>
	 * @param xmlRequest: Xml Request template with filled parameters. 
	 */
	
	public HashMap getRequestAttributes( String xmlRequest ){
		HashMap request = new HashMap();
		try{		
			Document doc = parseRequest( xmlRequest );
			Node checker;
			checker = doc.getDocumentElement();
			boolean ascending = false;
			int level = 1;
			while (true) {
				if( DEBUG )  logger.appendMessage("Now trying to walk the dom tree..");
					
					if (checker!=null && (checker.hasChildNodes()) && (!ascending)) {
						checker = checker.getFirstChild();
						if( DEBUG )  logger.appendMessage("GOING DOWN"+checker.getNodeName() +"NODETYPE="+checker.getNodeType());
						if(checker.getNodeType()==1){
							String text = "";
							if(checker.getFirstChild()!=null){
								if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
									text = checker.getFirstChild().getNodeValue();
									if( DEBUG )  logger.appendMessage("BEFORE.PUT.DOWN" +checker.getNodeName()+" TEXT " +text);
									
									request.put(checker.getNodeName(), text);
									
									if( DEBUG )  logger.appendMessage("NODENAME: "+checker.getNodeName() +",  TEXTVALUE: "+text);
								}
							}
						}
						ascending = false;
						level++;
						}
						else if (checker!=null && checker.getNextSibling() != null) {
							checker= checker.getNextSibling();
							String text = "";
							if( DEBUG )  logger.appendMessage(	"SIBBLING...");
							if(checker.getFirstChild()!=null){
								if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
									text = checker.getFirstChild().getNodeValue();
									if(DEBUG)  logger.appendMessage("BEFORE.PUT.SIBLING");
									
									request.put(checker.getNodeName(), text);
									
									if(DEBUG)  logger.appendMessage("NODENAME: "+checker.getNodeName() +",  TEXTVALUE: "+text);
								}
							}
							ascending = false;
							if( DEBUG )  logger.appendMessage("GOING RIGHT");
							}
						else if (checker !=null && checker.getParentNode() != null) {
							checker= checker.getParentNode();
							String text = "";
							if(checker.getFirstChild()!=null){
								if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
									text = checker.getFirstChild().getNodeValue();
									if( DEBUG )  logger.appendMessage("BEFORE.PUT.UP");
									
									request.put(checker.getNodeName(), text);
									
									if( DEBUG )  logger.appendMessage("NODENAME: "+checker.getNodeName() +",  TEXTVALUE: "+text);
								}
							}
							ascending = true;
							level--;
							if( DEBUG )  logger.appendMessage("GOING UP");
							}
							else {
								if( DEBUG )  logger.appendMessage("BREAK!");
								break;
								}
			}
		}catch(Exception e){
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMSCE00046", this.getComponentName()) ;
			logger.appendMessage("ERROR Walking DOM TREE: "+generalMessage.toString()+e.toString());
		}
		return request;
	}
	
	
	
	private Document parseRequest ( String xmlRequest){
		if( DEBUG ) logger.appendMessage( "Tring to parse MySpace xmlRequest.." +xmlRequest) ;
		
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
	       
		try {
		   builder = factory.newDocumentBuilder();
		   InputSource requestSource = new InputSource( new StringReader( xmlRequest ) );
		   doc = builder.parse( requestSource );
		}
		catch ( Exception ex ) {
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMSCE00046", this.getComponentName()) ; 
			//logger.appendMessage( generalMessage.toString(), ex ) ;
		} 		
		return doc ;	
	}
	protected String getComponentName() { return Configurator.getClassName( MySpaceUtils.class) ; }
}