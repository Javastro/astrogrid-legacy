package org.astrogrid.registry;

import java.io.*;
import java.net.*;

import org.w3c.dom.*;
import de.gmd.ipsi.domutil.*;
import de.gmd.ipsi.pdom.*;

/**
 *
 * @author Pedro Contreras <mailto:p.contreras@qub.ac.uk><p>
 * @see School of Computer Science   <br>
 * The Queen's University of Belfast <br>
 * {@link http://www.cs.qub.ac.uk}
 * @author Elizabeth Auden <mailto:eca@mssl.ucl.ac.uk><p>
 * Mullard Space Science lab <br>
 * {@link http://www.mssl.ucl.ac.uk}
* <p>
 * ASTROGRID Project {@link http://www.astrogrid.org}<br>
 * Registry Group
 *
 */


public class RegistryAdminAction {

  public static String deleteNode(String shortName, String contents) {
     String deletedContents = "";
     int snIndex = contents.indexOf("<shortName>" + shortName + "</shortName>");
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
     String addedContents = "";
     if (contents.indexOf("<shortName>" + shortName + "</shortName>") > -1) {
        addedContents = addedContents + "ADMIN ERROR - Service entry with shortName " + shortName + " already exists.";
     } else if (contents.indexOf("<shortName>" + shortName + "</shortName>") == -1) { 
        int regEndIndex = contents.indexOf("</registry>");
        String firstHalf = contents.substring(0, regEndIndex);
        String secondHalf = contents.substring(regEndIndex, contents.length());
        addedContents = addedContents + firstHalf + serviceNode + secondHalf;
     }   
     return addedContents;
  }

  public static String editNode(String serviceNode, String shortName, String contents) {
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
