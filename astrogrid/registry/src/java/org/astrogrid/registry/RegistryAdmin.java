/*
 * Created on 17-Jul-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.Reader;

/**
 * @author Elizabeth Auden
 *
 * The RegistryAdmin class receives an XML formatted admin request query
 * from the RegistryAdminService web service.  The class can add, edit, or
 * delete one resource service entry at a time. Once the string containing
 * the desired service entry has been changed accordingly, the old registry
 * file is deleted and a new registry file is written containing the added,
 * edited, or deleted service entry.
 * 
 * Elizabeth Auden, 24 October 2003
 */

public class RegistryAdmin {

	public static String requestAdmin(String adminQuery) {

        /**
         * The requestAdmin method takes an XML formatted admin query as 
         * input, formatted according to the schema located at 
         * http://wiki.astrogrid.org/pub/Astrogrid/AgCd06Schema/registryAdminQuery_v1_0.xsd.
         * A string corresponded to the added, edited, or deleted service
         * entry is produced.
         */
        
        /**
         * First, find the location of the service in the admin query.
         */
        
        int beginShortName = adminQuery.indexOf("<shortName>") + 11;  
        int endShortName = adminQuery.indexOf("</shortName>");  
        String shortName = adminQuery.substring(beginShortName, endShortName);  

		/**
		 * Locate the registry file.  The directory "user.dir" is assumed to
		 * be $TOMCAT_HOME/bin, and the registry file is assumed to be located
		 * in $TOMCAT_HOME/webapps/org/astrogrid.
		 */
		
        String filename = (System.getProperty("user.dir"));
        File f = new File(filename);
        String registryFileName = f.getAbsolutePath() + "/../webapps/org/astrogrid/registry/registry_v1_1.xml";
        File registryFile = new File(registryFileName);

		String adminResponse = "";	
        String registryContents = "";
 
        /**
         * Build a DOM tree out of the registry contents and write to a string.  
         * Throw an error if the file isn't found or if something else goes wrong.
         */
        try {
              Reader r = new FileReader(registryFile);
              BufferedReader br = new BufferedReader(r); 
              String line = null;
              while ((line = br.readLine()) != null) {
                    registryContents = registryContents + line;
              }

		      boolean goodQuery = true;	
		 
		      /**
		       * Make sure the admin query is formatted correctly before applying
       		   * the add, edit, or delete command to the registry file. Throw an
      		   * error if the <query> or <adminAction> elements are missing.  If 
      		   * the query is well formatted, call the appropriate add, edit, or
      		   * delete method.
	      	   */
		       if((goodQuery == true) && (adminQuery.indexOf("<query>")==-1)){
                    adminResponse =  "<queryResponse><recordKeyPair item='ERROR:' "
                                  + "value='Admin request must follow format "
                                  + "<registryAdminQuery><adminAction>"
                                  + "</adminAction><query></query><registryAdminQuery>.'/>"
                                  + "</queryResponse>";
                    goodQuery = false;
               }
               else if((goodQuery == true) && (adminQuery.indexOf("<adminAction>")==-1)){
                    adminResponse =  "<queryResponse><recordKeyPair item='ERROR:' "
                                  + "value='Admin request must follow format "
                                  + "<registryAdminQuery><certificate></certificate><adminAction>"
                                  + "</adminAction><query></query><registryAdminQuery>.'/>"
                                  + "</queryResponse>";
                    goodQuery = false;
               }
               else if (goodQuery == true){
	      	         String serviceNode = adminQuery.substring(adminQuery.indexOf("<query>")+7, adminQuery.indexOf("</query>"));
		             String action = adminQuery.substring(adminQuery.indexOf("<adminAction>")+13, adminQuery.indexOf("</adminAction>"));

                     /**
                      * Call the addNode method in class RegistryAdminAction.  The response
                      * will be either an admin error or a string containing the contents of the
                      * updated registry including the newly added service entry.
                      */
                     if (action.equals("add")){
                           String addedContents = RegistryAdminAction.addNode(serviceNode, shortName, registryContents);
                           if (addedContents.indexOf("ADMIN ERROR") > -1){
                                 adminResponse = adminResponse + "An error has occurred: " + shortName
                                               + " has not been added to the registry: " + addedContents;
                           }
                          else {
                                 adminResponse = adminResponse + updateRegFile(action, shortName, addedContents, registryFileName);
                          }                       
                     }
  				     /**
				      * Call the deleteNode method in class RegistryAdminAction.  The response
				      * will be either an admin error or a string containing the contents of the
				      * updated registry without a service entry for the deleted service.
				      */
                     if (action.equals("delete")){
                          String deletedContents = RegistryAdminAction.deleteNode(shortName, registryContents);
                          if (deletedContents.indexOf("ADMIN ERROR") > -1){
                                 adminResponse = adminResponse + "An error has occured: " + shortName
                                               + " has not been deleted from the registry.  " + deletedContents;
                          }      
                          else {
                                 adminResponse = adminResponse + updateRegFile(action, shortName, deletedContents, registryFileName);
                          }
                     }
				     /**
				      * Call the editNode method in class RegistryAdminAction.  The response
				      * will be either an admin error or a string containing the contents of the
				      * updated registry including the edited service entry.
				      */
                     if (action.equals("edit")){
                          String editedContents = RegistryAdminAction.editNode(serviceNode, shortName, registryContents);
                          if (editedContents.indexOf("ADMIN ERROR") > -1){
                                 adminResponse = adminResponse + "An error has occured: " + shortName
                                               + " has not been edited in the registry. " + editedContents;
                          }
                          else {
                                 adminResponse = adminResponse + updateRegFile(action, shortName, editedContents, registryFileName);
                          }
                     }
                     
                     /**
                      * Add formatting for admin query response.
                      */
    	             adminResponse = "<registryAdminQueryResponse>" + adminResponse + "</registryAdminQueryResponse>";
               }
	    } catch (FileNotFoundException e) {
                    adminResponse = "<registryAdminQueryResponse><recordKeyPair item='ERROR:' "
                        + "value='Registry file not found.'/>"
                        + "</registryAdminQueryResponse>";
		} catch (IOException e) {
                    adminResponse = "<registryAdminQueryResponse><recordKeyPair item='ERROR:' "
                        + "value='Could not read registry file.'/>"
                        + "</registryAdminQueryResponse>";
                }
		return adminResponse;
	}

	static String updateRegFile(String action, String shortName, String newContents, String registryFileName){

                /**
                 * The updateRegFile method commits the updated registry contents
                 * to the registry file, whether a service entry has been added,
                 * edited, or deleted.
                 */
                String updateResult = "";

                File f = new File(registryFileName);
                //  NEW CODE:
                
                /**
                 * Test to see if the registry file exists.
                 */
                if (!(f.exists())) {
                   updateResult = updateResult + "Registry file does not exist and therefore cannot "
                                  + "be updated in administrative action";
                }
                
                /**
                 * Test to see if the registry filename refers to a directory.
                 */
                else if (!(f.isFile())) {
                   updateResult = updateResult + "Registry filename seems to be a directory and therefore cannot "
                                  + "be updated in administrative action";
                }
                
                /**
                 * Test to see if the registry file is readable.
                 */
                else if (!(f.canRead())) {
                   updateResult = updateResult + "Registry file cannot be read and therefore cannot "
                                  + "be updated in administrative action";
                }
                
                /** 
                 * Test to see if the registry file is writable.  If so,
                 * begin writing to it.
                 */
                else if (f.exists()) {
                   if (!(f.canWrite())) {
                      updateResult = updateResult + "Registry file cannot be overwritten and therefore cannot "
                                     + "be updated in administrative action";
                   }
                   else {
                   	  /**
                   	   * First, delete the old registry file. The directory also
                   	   * contains a file called registry_v1_1.xml.copy; this
                   	   * file should be updated periodically.
                   	   */
                      boolean success = f.delete();
                      if (success == false) {
                         updateResult = updateResult + "Registry file could not be deleted and therefore cannot "
                                        + "be updated in administrative action";
                      }
                      else if (success == true) {

                         try { 
                         	
                         	/**
                         	 * Write the contents of the updated registry to a new
                         	 * registry file, 4096 characters at a time.
                         	 */ 
                            FileWriter registry = new FileWriter(registryFileName);
                            PrintWriter fileOutput = new PrintWriter(registry);
                            int z = 0;
                            int y = 0;
                            while (z < newContents.length()){
                               if ((newContents.length() - z) < 4096){
                                  y = newContents.length();
                               } else y = z + 4096;  
                                  String subContents = newContents.substring(z, y);
                                  fileOutput.print(subContents);
                                  fileOutput.flush();
                                  z = z + 4096;
                               } 
   
                               /**
                                * Formulate appropriate add, edit, or delete message
                                * for user.
                                */
                               if (action.equals("add")){
      	   	                  updateResult = updateResult +  "Service '" + shortName + "' added.";
                               }
                               if (action.equals("edit")){
   	  	                  updateResult = updateResult +  "Service '" + shortName + "' edited.";
                               }
                               if (action.equals("delete")){
   		                  updateResult = updateResult +  "Service '" + shortName + "' deleted.";
                               }
                            } catch (IOException e) {
                               updateResult = "ERROR: Could not write to registry file.";
                            }  
                         }
                      }
                   }
                
        /**
         * Return the "service added", "service edited", or "service deleted"
         * message to user.
         */
        return updateResult;
        }
}
