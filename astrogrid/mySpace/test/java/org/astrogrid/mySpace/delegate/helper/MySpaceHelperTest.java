package org.astrogrid.mySpace.delegate.helper;

import junit.framework.*;
import java.util.*;

import org.astrogrid.mySpace.delegate.helper.MySpaceHelper;

/**
 * Junit tests for the <code>MySpaceHelper</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 */

public class MySpaceHelperTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public MySpaceHelperTest (String name)
   {  super(name);
   }

// ------------------------------------------------------------------

/**
 * Test the <code>buildSave</code> method.
 */

   public void testBuildSave()
   {  String xmlFragment =
        "<request><userID>clq</userID><communityID>lei</communityID>" +
        "<credential>all</credential>" +
        "<fileContent>lonely as a cloud</fileContent><newDataHolderName>" +
        "/clq@lei/serv1/query/somequery" +
        "</newDataHolderName><action></action></request>";

      MySpaceHelper helper = new MySpaceHelper();
      String result = helper.buildSave("clq", "lei", "all",
        "/clq@lei/serv1/query/somequery", "lonely as a cloud",
        "query", "");
      System.out.println("buildSave:\n" + result);

      Assert.assertEquals(xmlFragment, result);
   }

/**
 * Test the <code>buildSaveURL</code> method.
 */

   public void testBuildSaveURL()
   {  String xmlFragment =
        "<request><userID>clq</userID><communityID>lei</communityID>" +
        "<credential>all</credential>" +
        "<importURI>http://www.roe.ac.uk/acdwww/index.html</importURI>" +
        "<newDataHolderName>" +
        "/clq@lei/serv1/query/somequery" +
        "</newDataHolderName><action></action></request>";

      MySpaceHelper helper = new MySpaceHelper();
      String result = helper.buildSaveURL("clq", "lei", "all",
        "/clq@lei/serv1/query/somequery",
        "http://www.roe.ac.uk/acdwww/index.html", "query", "");
//      System.out.println("buildSaveURL:\n" + result);

      Assert.assertEquals(xmlFragment, result);
   }

/**
 * Test the <code>buildDownload</code> method.
 */

   public void testBuildDownload()
   {  String xmlFragment =
        "<request><userID>clq</userID><communityID>lei</communityID>" +
        "<credential>all</credential><serverFileName>" +
        "/clq@lei/serv1/query/somequery</serverFileName></request>";

      MySpaceHelper helper = new MySpaceHelper();
      String result = helper.buildDownload("clq", "lei", "all",
        "/clq@lei/serv1/query/somequery");
//      System.out.println("buildDownload:\n" + result);

      Assert.assertEquals(xmlFragment, result);
   }

/**
 * Test the <code>buildListDataHoldings</code> method.
 */

   public void testBuildListDataHoldings()
   {  String xmlFragment =
        "<request><userID>clq</userID><communityID>lei</communityID>" +
        "<credential>all</credential><query>*</query></request>";

      MySpaceHelper helper = new MySpaceHelper();
      String result = helper.buildListDataHoldings("clq", "lei", "all",
        "*");
//      System.out.println("buildListDataHoldings:\n" + result);

      Assert.assertEquals(xmlFragment, result);
   }

/**
 * Test the <code>buildListDataHolding</code> method.
 */

   public void testBuildListDataHolding()
   {  String xmlFragment =
        "<request><userID>clq</userID><communityID>lei</communityID>" +
        "<credential>all</credential><serverFileName>" +
        "/clq@lei/serv1/query/somequery</serverFileName></request>";

      MySpaceHelper helper = new MySpaceHelper();
      String result = helper.buildListDataHolding("clq", "lei", "all",
        "/clq@lei/serv1/query/somequery");
//      System.out.println("buildListDataHolding:\n" + result);

      Assert.assertEquals(xmlFragment, result);
   }

/**
 * Test the <code>buildCopy</code> method.
 */

   public void testBuildCopy()
   {  String xmlFragment =
        "<request><userID>clq</userID><communityID>lei</communityID>" +
        "<credential>all</credential><serverFileName>" +
        "/clq@lei/serv1/query/somequery</serverFileName>" +
        "<newDataItemName>/clq@lei/serv1/query/anotherquery" +
        "</newDataItemName></request>";

      MySpaceHelper helper = new MySpaceHelper();
      String result = helper.buildCopy("clq", "lei", "all",
        "/clq@lei/serv1/query/somequery",
        "/clq@lei/serv1/query/anotherquery");
//      System.out.println("buildCopy:\n" + result);

      Assert.assertEquals(xmlFragment, result);
   }

/**
 * Test the <code>buildDelete</code> method.
 */

   public void testBuildDelete()
   {  String xmlFragment =
        "<request><userID>clq</userID><communityID>lei</communityID>" +
        "<credential>all</credential><serverFileName>" +
        "/clq@lei/serv1/query/anotherquery</serverFileName></request>";

      MySpaceHelper helper = new MySpaceHelper();
      String result = helper.buildDelete("clq", "lei", "all",
        "/clq@lei/serv1/query/anotherquery");
//      System.out.println("buildDelete:\n" + result);

      Assert.assertEquals(xmlFragment, result);
   }

/**
 * Test the <code>buildRename</code> method.
 */

   public void testBuildRename()
   {  String xmlFragment =
        "<request><userID>clq</userID><communityID>lei</communityID>" +
        "<credential>all</credential><serverFileName>" +
        "/clq@lei/serv1/query/somequery</serverFileName>" +
        "<newDataItemName>/clq@lei/serv1/query/anotherquery" +
        "</newDataItemName></request>";

      MySpaceHelper helper = new MySpaceHelper();
      String result = helper.buildRename("clq", "lei", "all",
        "/clq@lei/serv1/query/somequery",
        "/clq@lei/serv1/query/anotherquery");
//      System.out.println("buildRename:\n" + result);

      Assert.assertEquals(xmlFragment, result);
   }

/**
 * Test the <code>buildExtendlease</code> method.
 */

   public void testBuildExtendlease()
   {  String xmlFragment =
        "<request><userID>clq</userID><communityID>lei</communityID>" +
        "<credential>all</credential><serverFileName>" +
        "/clq@lei/serv1/query/somequery</serverFileName>" +
        "<extentionPeriod>25</extentionPeriod></request>";

      MySpaceHelper helper = new MySpaceHelper();
      String result = helper.buildExtendlease("clq", "lei", "all",
        "/clq@lei/serv1/query/somequery", 25);
//      System.out.println("buildExtendlease:\n" + result);

      Assert.assertEquals(xmlFragment, result);
   }

/**
 * Test the <code>buildContainer</code> method.
 */

   public void testBuildContainer()
   {  String xmlFragment =
        "<request><userID>clq</userID><communityID>lei</communityID>" +
        "<credential>all</credential><newContainerName>" +
        "/clq@lei/serv1/subcontainer</newContainerName></request>";

      MySpaceHelper helper = new MySpaceHelper();
      String result = helper.buildContainer("clq", "lei", "all",
        "/clq@lei/serv1/subcontainer");
//      System.out.println("buildContainer:\n" + result);

      Assert.assertEquals(xmlFragment, result);
   }

/**
 * Test the <code>getList</code> method.
 */

   public void testGetList()
   {  String xmlRequest =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<results>\n" +
        "    <status>\n" +
        "        <status>{0}</status>\n" +
        "        <details>{1}</details>\n" +
        "        <currentDate>{2}</currentDate>\n" +
        "    </status>\n" +
        "    <dataItemRecords>\n" +
        "      <dataItemRecord>\n" +
        "        <dataItemName>/clq@lei/serv1/query/somequery" +
                    "</dataItemName>\n" +
        "        <dataItemID>{4}</dataItemID>\n" +
        "        <ownerID>clq</ownerID>\n" +
        "        <creationDate>{6}</creationDate>\n" +
        "        <expiryDate>{7}</expiryDate>\n" +
        "        <size>{8}</size>\n" +
        "        <type>{9}</type>\n" +
        "        <permissionsMask>{10}</permissionsMask>\n" +
        "        <dataHolderURI>flibble flobble</dataHolderURI>\n" +
        "      </dataItemRecord>\n" +
        "    </dataItemRecords>\n" +
        "</results>\n";

      MySpaceHelper helper = new MySpaceHelper();

      Vector results = helper.getList(xmlRequest, "dataItemName");
      Assert.assertTrue(results.size() > 0);
      if (results.size() > 0)
      {  Assert.assertEquals("somequery", (String)results.elementAt(0) );
//         System.out.println("getList:\n" + (String)results.elementAt(0) );
      }

      results = helper.getList(xmlRequest, "ownerID");
      Assert.assertTrue(results.size() > 0);
      if (results.size() > 0)
      {  Assert.assertEquals("clq", (String)results.elementAt(0) );
//         System.out.println("getList:\n" + (String)results.elementAt(0) );
         
      }
   }

// ------------------------------------------------------------------

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (MySpaceHelperTest.class);
      junit.swingui.TestRunner.run (MySpaceHelperTest.class);
   }
}