package org.astrogrid.registry;

import java.io.*;
import java.net.*;

import org.w3c.dom.*;
import de.gmd.ipsi.domutil.*;
import de.gmd.ipsi.pdom.*;

/**
 *
 * @author Pedro Contreras, Elizabeth Auden
 * 
 * The RegistryAdminAction class generates adds, edits, or deletes a service
 * entry in the string containing the registry contents.  The string containing
 * the updated registry content is returned to the RegistryAdmin class.
 * 
 * Elizabeth Auden, 24 October 2003
 *
 */


public class RegistryAdminAction {

  public static String deleteNode(String shortName, String contents) {
  	
  	/**
  	 * The deleteNode method locates the service entry that is to be deleted
  	 * inside the registry contents.  The service is identified by its 
  	 * shortName element, which is assumed to be unique.
  	 */
     String deletedContents = "";
     int snIndex = contents.indexOf("<shortName>" + shortName + "</shortName>");
     /**
      * Locate the position of the service entry inside the registry contents.
      * If the shortName does not exist within a <shortName> element, return an
      * error.  If the shortName does exist within a <shortName> element, create
      * one string containing the registry contents up to this service entry, and 
      * another string containing the registry contents after this service entry.
      * Concatenate the two strings.
      */
     if (contents.indexOf("<shortName>" + shortName + "</shortName>") == -1) {
        deletedContents = deletedContents + "ADMIN ERROR - Service entry with shortName " + shortName + " does not exist.";
     } else if (contents.indexOf("<shortName>" + shortName + "</shortName>") > -1) { 
        int beginServiceIndex = contents.lastIndexOf("<service>", snIndex);
        int endServiceIndex = contents.indexOf("</service>", snIndex) + 10;
        String firstHalf = contents.substring(0, beginServiceIndex);
        String secondHalf = contents.substring(endServiceIndex, contents.length());
        deletedContents = firstHalf + secondHalf;
     }
     return deletedContents;
  }

  public static String addNode(String serviceNode, String shortName, String contents) {
	/**
	 * The addNode method locates the last service entry so that a new service
	 * can be added directly after it. The service is identified by its 
	 * shortName element, which is assumed to be unique.
	 */
     String addedContents = "";
     if (contents.indexOf("<shortName>" + shortName + "</shortName>") > -1) {
        addedContents = addedContents + "ADMIN ERROR - Service entry with shortName " + shortName + " already exists.";
     } else if (contents.indexOf("<shortName>" + shortName + "</shortName>") == -1) { 
		/**
		 * Locate the position of the service entry inside the registry contents.
		 * If the shortName already exists within a <shortName> element, return an
		 * error.  If the shortName does not exist within a <shortName> element, 
		 * add the string containing the new service entry to the end of the registry 
		 * contents, just inside the "</registry>" element.
		 */
        int regEndIndex = contents.indexOf("</registry>");
        String firstHalf = contents.substring(0, regEndIndex);
        String secondHalf = contents.substring(regEndIndex, contents.length());
        addedContents = addedContents + firstHalf + serviceNode + secondHalf;
     }   
     return addedContents;
  }

  public static String editNode(String serviceNode, String shortName, String contents) {
	/**
	 * The editNode method locates the service entry that is to be edited
	 * inside the registry contents.  The service is identified by its 
	 * shortName element, which is assumed to be unique.  First the deleteNode
	 * method is applied to remove the old service entry from the registry; next,
	 * the addNode method is applied to insert the updated entry.
	 */
     String editedContents = "";
     String deletedContents = deleteNode(shortName, contents);
     if (deletedContents.indexOf("ADMIN ERROR") > -1) {
        editedContents = "ADMIN ERROR - Could not edit node; node does not exist.  Please not that this"
                         + "implementation of the Astrogrid does not allow the shortName element to be edited.";
     } else if (deletedContents.indexOf("ADMIN ERROR") ==  -1) {
        editedContents = addNode(serviceNode, shortName, deletedContents);
     }
     return editedContents;
  }
}
