package org.astrogrid.mySpace.mySpaceUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.lang.StringBuffer;
import java.io.FileInputStream;
import java.text.MessageFormat;

import org.apache.log4j.Logger;

import org.astrogrid.mySpace.mySpaceManager.*;

/**
 * @author C L QIN
 * @version Iteration 2.
 */

public class MySpaceUtils {
	private static Logger logger = Logger.getLogger(MySpaceUtils.class);
	private boolean DEBUG = true;
	private static String catalinaHome = "/usr/local/astroGrid/jakarta-tomcat-4.1.24";
	private static String responseTemplate = catalinaHome +"/conf/astrogrid/mySpace/"+"MySpaceManagerResponse.properties";
	private static Properties conProperties = new Properties();
	
	private static final String RESPONSE = "MYSPACEMANAGER_RESPONSE";
	
	public static String readFromFile( File file )throws Exception {
   
	 FileReader fileReader = null;
	 try{
		 if (file == null || !file.exists()) {
			 throw new IOException("File does not exist");
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
		 throw e;
	 }finally{
		 try{
			 if(fileReader != null){
				 fileReader.close();
			 }
		 }catch(Exception e){
			 throw e;
		 }
	 }
	
	}
	
	public String buildMySpaceManagerResponse(){//(DataItemRecord record){
		conProperties = new Properties();
		String response = "";
		try {
			FileInputStream istream = new FileInputStream( responseTemplate );
			conProperties.load(istream);
			istream.close();
			logger.debug( "buildMySpaceManagerResponse: " +conProperties.toString() ) ;
			String template = conProperties.getProperty( RESPONSE );
			logger.debug("buildMySpaceManagerResponse = "+response);
			
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
		}
		return response;
	}
	
	public static void main( String [] args ) {
	   }
	
}