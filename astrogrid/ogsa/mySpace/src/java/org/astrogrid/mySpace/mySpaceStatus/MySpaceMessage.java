package org.astrogrid.mySpace.mySpaceStatus;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;

import java.io.FileInputStream;

//axis
import org.apache.axis.AxisProperties;

import org.apache.log4j.Logger;

/**
 * The <code>MySpaceMessage</code> class holds messages from the MySpace
 * system which are intended for eventual delivery to the User.
 * 
 * <p>
 * The <code>MySpaceMessage</code> class is intended for use with the
 * <code>MySpaceStatus</code> class, which stores and returns
 * <code>MySpaceMessage</code> objects.  Each <code>MySpaceMessage</code>
 * object comprises two components: a message and a type.  The message
 * is a human-readable string set by the MySpace system and ultimately
 * intended for delivery to the User.  The type indicates the type of
 * event to which the message refers, coded as follows:
 * </p>
 * <ul>
 *   <li><code>"i"</code> - information (that is, nothing is amiss),</li>
 *   <li><code>"w"</code> - warning,</li>
 *   <li><code>"e"</code> - error.</li>
 * </ul>
 * <p>
 * The class has a single constructor to which the message and type are
 * passed as arguments.  There are get methods for both the message and
 * type and a <code>toString</code> method to produce a reasonable
 * representation.
 * 
 * @author A C Davenhall (Edinburgh)
 * @edited C L QIN
 * @version Iteration 2.
 */

public class MySpaceMessage{
	
    private static Logger logger = Logger.getLogger(MySpaceMessage.class);
    private boolean DEBUG = true;
    private String key;     // Message key.
    //private String type;        // Type of message: "i", "w" or "e".
    private String errMessage;  // exception message passed in.
    private String message;     // Message.
    private String catalinaHome = AxisProperties.getProperty("catalina.home");
    private String messageFilePath = catalinaHome+"/conf/astrogrid/mySpace/" +"statuscodes.lis";
    private Properties p = new Properties();

/**
 *  Default constructor
 */
    public MySpaceMessage (String key){
    	this.key = key;
    	loadProperties();
    }

/**
 * For debugging purpers, this constructor pass error message
 */
    public MySpaceMessage ( String key, String errMessage ){
    	this.key = key;
    	//this.type = type;
    	this.errMessage = errMessage;
    	loadProperties();
    }

/**
 * Create a <code>MySpaceMessage</code>, setting the message and type.
 */
/*
    public MySpaceMessage (String key, String type){
    	this.key = key;
        this.type = type;
        loadProperties();
   }
   */
/**
 *  This method reads properties from local property file.
 */
    private void loadProperties(){
    	try{
    		FileInputStream istream = new FileInputStream( messageFilePath );
    		p.load(istream);
    		istream.close();
    	}catch( FileNotFoundException fne ){
    		if (DEBUG)  logger.error("Can't find property file for reading MySpaceMessage: "+fne.toString());
    	}
    	catch(IOException ioe){
    		if (DEBUG)  logger.error("Unhandled IOException MySpaceMessage.readFromFile: "+ioe.toString());
    	}
    }

//
// Get methods.
//
// The MySpaceMessage class has a get method for every member variable.

/**
 * Return the message string associated with the <code>MySpaceMessage</code>.
 */

    public String getMessage(){
    	if (DEBUG)  logger.debug("MySpaceMessage.getMessage, CATALINAHOME: " +catalinaHome);
    	try{
   	        message = p.getProperty(key);
    	}catch (Exception e){
    		if (DEBUG)  logger.error("Error Reading Properties File: " +e);
    		message = e.toString();
    		return message;
    	}
    	return message;
   }
   
   
/**
 * This method is called when there is an exception thrown.
 */
    public String getMessage(String errMessage){
    	if (DEBUG)  logger.debug("MySpaceMessage.getMessage(errMessage)");
    	try{
    		message = p.getProperty(key) + "Error!" +errMessage;
    	}catch (Exception e){
    		if (DEBUG)  logger.error("ERROR Reading Properties File: " +e);
    		message = errMessage + e.toString();
    		return message;
    	}
    	return message;
    }
    

/**
 * Return the type associated with the <code>MySpaceMessage</code>.
 */
    //public String getType(){
   // 	return type;
//    }

/**
 * Produce a reasonable string representation of a MySpaceMessage.
 */
/*
   public String toString(){
   	String returnString = "";

      if (type.equals("i"))
      {  returnString = "!Info: " + key;
      }
      else if (type.equals("w"))
      {  returnString = "!Warning: " + key;
      }
      else if (type.equals("e")) 
      {  returnString = "!Error: " + key;
      }
      else
      {  returnString = "!Unknown: " + key;
      }

      return returnString;
   }
   */
   /*
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

   }*/
}
