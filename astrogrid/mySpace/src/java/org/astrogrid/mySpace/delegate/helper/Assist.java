package org.astrogrid.mySpace.delegate.helper;

import java.io.*;
import java.util.*;

import java.io.StringReader ;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;

/**
 * <code>Assist</code> is a class of auxiliary methods which assist
 * in interpreting the results returned by the MySpace delegate.
 * It primarily consists of methods for manipulating the XML strings
 * returned by some of the delegate methods.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 4.
 * @version Iteration 4.
 */

public class Assist
{

//
// ------------------------------------------------------------------------
//
// Constructor.

  public  Assist()
  {
  }

//
// ------------------------------------------------------------------------
//
// Other methods.

/**
 * Given an XML string, create an equivalent string containing a
 * hierarchical representation of the XML in a format suitable for
 * display.
 *
 * @param xmlString String containing XML.
 * @return A string containing a hierarchical, indented representation
 *    of the XML in a format suitable for display.
 */

   public String formatTree (String  xmlString)
   {  String displayString = "";

      Document doc = null;
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = null;
               
      try
      {  builder = factory.newDocumentBuilder();
         InputSource requestSource = new InputSource(
           new StringReader( xmlString ) );
           doc = builder.parse( requestSource );

         Element root = doc.getDocumentElement();
//         System.out.println("root = " + root.toString() );
//         System.out.println("root name = " + root.getNodeName() );

         int level = 1;
         boolean more = true;
         String nodeValue = "";

         Node current = (Node)root;

         while (more)
         {  String nodeName = current.getNodeName();
            int intHash = nodeName.indexOf('#');

            if (intHash == -1)
            {  displayString = displayString + level;
               for (int loop = 0; loop < level; loop++)
               {  displayString = displayString + "  ";
               }

               displayString = displayString + nodeName;

               nodeValue = "";

               if (current.hasChildNodes() )
               {  NodeList children = current.getChildNodes();
                  int numChildren = children.getLength();

                  for (int currentChild = 0; currentChild<numChildren;
                    currentChild++)
                  {  Node thisChild = children.item(currentChild);
                     String childName = thisChild.getNodeName();
                     if (childName.equals("#text") )
                     {  nodeValue = nodeValue + thisChild.getNodeValue();
                     }
                  }
               }

               nodeValue = nodeValue.trim();
               if (!nodeValue.equals(""))
               {  displayString = displayString + ": " + nodeValue;
               }

               displayString = displayString + "\n";
            }

            if (current.hasChildNodes() )
            {  level = level + 1;
               current = current.getFirstChild();
            }
            else if (current.getNextSibling() != null)
            {  current = current.getNextSibling();
            }
            else
            {  boolean ascend = true;

               while (ascend)
               {  level = level - 1;

                  if (!current.equals((Node)root) )
                  {  current = current.getParentNode();
                     if (current.getNextSibling() != null)
                     {  current = current.getNextSibling();
                        ascend = false;
                     }
                     else
                     {  if (current.equals((Node)root) )
                        {  more = false;
                           ascend = false;
                        }
                     }
                  }
                  else
                  {  more = false;
                     ascend = false;
                  }
               }
            }

//            if (current.equals((Node)root) )
//            {  more = false;
//            }
         }
      }
      catch ( Exception ex )
      {  System.out.println("Parse failed: " +ex.toString());
         ex.printStackTrace();
      }

      return displayString;
   }

/**
 * Given an XML string returned by the <code>listDataHoldingsGen</code>
 * method of the delegate, search it to assemble and return a Vector
 * comprising the value of one of the dataItemRecord details for all
 * the dataItemRecords in the XML string.
 *
 * <p>
 * The dataItemRecord details are: dataItemName,  dataItemID, ownerID, 
 * creationDate, expiryDate, size, type, permissionsMask and
 * dataHolderURI.
 *
 * @param xmlString XML string returned by the delegate method
 *    <code>listDataHoldingsGen</code>.
 * @param detail dataItemRecord detail whose values are to be returned.
 * @return A Vector of the values of the chosen detail for all the
 *    dataItemRecords found in the XML string.
 */
   public Vector getDataItemDetails (String  xmlString, String detail)
   {  Vector detailValuesVec = new Vector();

      Document doc = null;
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = null;
               
      try
      {  builder = factory.newDocumentBuilder();
         InputSource requestSource = new InputSource(
           new StringReader( xmlString ) );
           doc = builder.parse( requestSource );

//
//      Obtain the root node and proceed if ok.

         Node root = (Node)doc.getDocumentElement();

         if (root != null)
         {  Node current = root;
            int level = 1;
            boolean more = true;
            String nodeValue = "";

            while (more)
            {  String nodeName = current.getNodeName();
               Node parent = current.getParentNode();
               String parentName = parent.getNodeName();

               if (nodeName.equals(detail) &&
                   parentName.equals("dataItemRecord") )
               {  nodeValue = "";

                  if (current.hasChildNodes() )
                  {  NodeList children = current.getChildNodes();
                     int numChildren = children.getLength();

                     for (int currentChild = 0; currentChild<numChildren;
                       currentChild++)
                     {  Node thisChild = children.item(currentChild);
                        String childName = thisChild.getNodeName();
                        if (childName.equals("#text") )
                        {  nodeValue = nodeValue + thisChild.getNodeValue();
                        }
                     }

                     nodeValue = nodeValue.trim();
                     detailValuesVec.add(nodeValue);
                  }
               }

               if (current.hasChildNodes() )
               {  level = level + 1;
                  current = current.getFirstChild();
               }
               else if (current.getNextSibling() != null)
               {  current = current.getNextSibling();
               }
               else
               {  boolean ascend = true;

                  while (ascend)
                  {  level = level - 1;

                     if (!current.equals(root) )
                     {  current = current.getParentNode();
                        if (current.getNextSibling() != null)
                        {  current = current.getNextSibling();
                           ascend = false;
                        }
                        else
                        {  if (current.equals((Node)root) )
                           {  more = false;
                              ascend = false;
                           }
                        }
                     }
                     else
                     {  more = false;
                        ascend = false;
                     }
                  }
               }
            }
         }
      }
      catch ( Exception ex )
      {  System.out.println("Parse failed: " +ex.toString());
         ex.printStackTrace();
      }

      return detailValuesVec;
   }

/**
 * Given an XML string returned by the <code>listDataHoldingsGen</code>
 * method of the delegate, search it to assemble and return a Vector
 * each element of which is a String summarising each dataItemRecord
 * found in the XML string.
 *
 * @param xmlString XML string returned by the delegate method
 *    <code>listDataHoldingsGen</code>.
 * @return A Vector of Strings.  Each String summarises a dataItemRecord
 *    found in the XML string.
 */
   public Vector getDataItemSummary (String  xmlString)
   {  Vector detailSummaryVec = new Vector();

      try
      { Vector namesList = this.getDataItemDetails(xmlString,
           "dataItemName");
         Vector ownerList = this.getDataItemDetails(xmlString,
           "ownerID");
         Vector creationList = this.getDataItemDetails(xmlString,
           "creationDate");

         int numEntries = namesList.size();
 
         String currentName = "";
         String currentOwner = "";
         String currentCreation = "";
         String summary = "";

         for (int loop = 0; loop < numEntries; loop++)
         {  currentName = (String)namesList.elementAt(loop);
            currentOwner = (String)ownerList.elementAt(loop);
            currentCreation = (String)creationList.elementAt(loop);

            summary = currentName + " (" + currentOwner + ", " +
              currentCreation + ").";

            detailSummaryVec.add(summary);
         }
      }
      catch ( Exception ex )
      {  detailSummaryVec.add(ex.toString());
         System.out.println("Parse failed: " +ex.toString());
         ex.printStackTrace();
      }

      return detailSummaryVec;
   }
}



