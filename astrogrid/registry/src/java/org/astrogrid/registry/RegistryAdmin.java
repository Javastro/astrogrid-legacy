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
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RegistryAdmin {

	/**
	 * @param adminQuery
	 * @return
	 */
	public static String requestAdmin(String adminQuery) {
                int beginShortName = adminQuery.indexOf("<shortName>") + 11;  
                int endShortName = adminQuery.indexOf("</shortName>");  
                String shortName = adminQuery.substring(beginShortName, endShortName);  

                String filename = (System.getProperty("user.dir"));

                File f = new File(filename);
                String registryFileName = f.getAbsolutePath() + "/../webapps/org/astrogrid/registry/registry_v1_1.xml";
                File registryFile = new File(registryFileName);

		String adminResponse = "";	
                String registryContents = "";
 
                try {
                   Reader r = new FileReader(registryFile);
                   BufferedReader br = new BufferedReader(r); 
                   String line = null;
                   while ((line = br.readLine()) != null) {
                       registryContents = registryContents + line;
                   }

		  boolean goodQuery = true;	
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

                String updateResult = "";

                File f = new File(registryFileName);
                //  NEW CODE:
                if (!(f.exists())) {
                   updateResult = updateResult + "Registry file does not exist and therefore cannot "
                                  + "be updated in administrative action";
                }
                else if (!(f.isFile())) {
                   updateResult = updateResult + "Registry filename seems to be a directory and therefore cannot "
                                  + "be updated in administrative action";
                }
                else if (!(f.canRead())) {
                   updateResult = updateResult + "Registry file cannot be read and therefore cannot "
                                  + "be updated in administrative action";
                }
                else if (f.exists()) {
                   if (!(f.canWrite())) {
                      updateResult = updateResult + "Registry file cannot be overwritten and therefore cannot "
                                     + "be updated in administrative action";
                   }
                   else {
                      boolean success = f.delete();
                      if (success == false) {
                         updateResult = updateResult + "Registry file could not be deleted and therefore cannot "
                                        + "be updated in administrative action";
                      }
                      else if (success == true) {

                         try {  
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
                
        return updateResult;
        }
}
