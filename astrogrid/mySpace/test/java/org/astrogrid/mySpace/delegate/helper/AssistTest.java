package org.astrogrid.mySpace.delegate.helper;

import junit.framework.*;
import java.util.*;

import org.astrogrid.mySpace.delegate.helper.Assist;

/**
 * Junit tests for the <code>Assist</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 4.
 * @since Iteration 4.
 */

public class AssistTest extends TestCase
{  private static final String xmlString = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<results>" +
        "    <status>" +
        "        <status>{0}</status>" +
        "        <details>{1}</details>" +
        "        <currentDate>{2}</currentDate>" +
        "    </status>" +
        "    <dataItemRecords>" +
        "      <dataItemRecord>" +
        "        <dataItemName>/acd/serv1/conta/myfile</dataItemName>" +
        "        <dataItemID>{4}</dataItemID>" +
        "        <ownerID>{5}</ownerID>" +
        "        <creationDate>{6}</creationDate>" +
        "        <expiryDate>{7}</expiryDate>" +
        "        <size>{8}</size>" +
        "        <type>{9}</type>" +
        "        <permissionsMask>{10}</permissionsMask>" +
        "        <dataHolderURI>{11}</dataHolderURI>" +
        "      </dataItemRecord>" +
        "    </dataItemRecords>" +
        "</results>";

/**
 * Standard constructor for JUnit test classes.
 */

   public AssistTest (String name)
   {  super(name);
   }

// ------------------------------------------------------------------

/**
 * Test the <code>formatTree</code> method.
 */

   public void testFormatTree()
   {  Assist assistant = new Assist();
      String displayString = assistant.formatTree(xmlString);

      System.out.println("Test formatTree...");
      System.out.println(displayString);

      Assert.assertTrue(displayString.length() > 0);
   }

/**
 * Test the <code>getDataItemDetails</code> method.
 */

   public void testGetDataItemDetails()
   {  String currentName = "";
      String currentOwner = "";
      String currentCreation = "";

      Assist assistant = new Assist();

      Vector namesList = assistant.getDataItemDetails(xmlString,
        "dataItemName");
      int numEntries = namesList.size();
      Assert.assertEquals(numEntries, 1);
      if (numEntries > 0)
      {  currentName = (String)namesList.elementAt(0);
         Assert.assertEquals(currentName,
           "/acd/serv1/conta/myfile");
      }

      Vector ownerList = assistant.getDataItemDetails(xmlString,
        "ownerID");
      numEntries = ownerList.size();
      Assert.assertEquals(numEntries, 1);
      if (numEntries > 0)
      {  currentOwner = (String)ownerList.elementAt(0);
         Assert.assertEquals(currentOwner, "{5}");
      }

      Vector creationList = assistant.getDataItemDetails(xmlString,
        "creationDate");
      numEntries = creationList.size();
      Assert.assertEquals(numEntries, 1);
      if (numEntries > 0)
      {  currentCreation = (String)creationList.elementAt(0);
         Assert.assertEquals(currentCreation, "{6}");
      }
   }

/**
 * Test the <code>getDataItemSummary</code> method.
 */

   public void testGetDataItemSummary()
   {  Assist assistant = new Assist();

      Vector summaryList = assistant.getDataItemSummary(xmlString);
      int numEntries = summaryList.size();
      Assert.assertEquals(numEntries, 1);
      if (numEntries > 0)
      {  String currentSummary = (String)summaryList.elementAt(0);
         Assert.assertEquals(currentSummary,
           "/acd/serv1/conta/myfile ({5}, {6}).");
      }
   }

// ------------------------------------------------------------------

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (AssistTest.class);
      junit.swingui.TestRunner.run (AssistTest.class);
   }
}