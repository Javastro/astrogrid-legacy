package org.astrogrid.mySpace.mySpaceStatus;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.lang.StringBuffer;

import org.apache.log4j.Logger;

/**
 * The <code>MySpaceMessage</code> class translates a MySpace status
 * code into a human-readable text string.
 * 
 * @author C L Qin (leicester)
 * @edited A C Davenhall (Edinburgh
 * @version Iteration 2.
 */

public class MySpaceMessage{

    private static Logger logger = Logger.getLogger(MySpaceMessage.class);
    private boolean DEBUG = true;
    private String key;     // Message key.

    private String message;     // Message.
    //This is returning null
    //private String catalinaHome = System.getProperty("CATALINA_HOME");
    //so hard code for now
    private String catalinaHome = "/usr/local/astroGrid/jakarta-tomcat-4.1.24";
    private String messageFilePath = catalinaHome+"/conf/astrogrid/mySpace/" +"mySpaceMessage.properties";
	private Properties p = new Properties();
    
   //logger.debug("XXX inside MySpaceMessage: ")+catalinaHome + "; mySpaceMessage = " +messageFilePath);
  
    

//
// Constructor.

/**
 * Create a <code>MySpaceMessage</code>, setting the message and type.
 */

   public MySpaceMessage (String key){
   	this.key = key;
   }

//
// Get methods.
//
// The MySpaceMessage class has a get method for every member variable.

/**
 * Return the message string associated with the <code>MySpaceMessage</code>.
 */

   public String getMessage(String key){
   	if (DEBUG)  logger.debug("Inside MySpaceMessage.getMessage: "+catalinaHome + "; mySpaceMessage = " +messageFilePath);
   	try{
   			File file = new File(messageFilePath);
   	        String s = readFromFile(file);
   	        p = buildProperties(s,"=", "|");
   	        message = p.getProperty(key);
   	}catch(Exception e){
   		if(DEBUG)  logger.error("Error Reading Properties File: " +e);
   		//Error message has to be defined, returning e.toString for now
   		message = e.toString();
   		return message;
   	}
   	return message;
   }

   private Properties buildProperties(String messageFile, String delimiter1, String delimiter2){
   	StringBuffer sb = new StringBuffer(messageFile);
   	String s1, s2 = "";
   	//while( !(sb.length()==0) ){
   	if( sb.indexOf("=")!=-1){
   		if (DEBUG) logger.debug("TRYING to delete sb..."+sb.toString());
   		s1 = sb.substring(0,sb.indexOf(delimiter1)).trim();
   		sb.delete(0,sb.indexOf(delimiter1)+1);
   		s2 = sb.substring(0,sb.indexOf(delimiter2)).trim();
   		sb.delete(0,sb.indexOf(delimiter2)+1);
   		if (DEBUG)  logger.debug("s1="+s1+", s2="+s2);
        p.put(s1, s2);
   	}
   	return p;
   }

//
// Other methods.

/**
 * Produce a reasonable string representation of a MySpaceMessage.
 */

   public String toString(){
      String returnString = "!status code: " + key;

      return returnString;
   }
   
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
}
