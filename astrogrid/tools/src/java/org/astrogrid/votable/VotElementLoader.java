/*
   ConfigElementLoader.java

   Date         Author      Changes
   17 Oct 2002  M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.tools.votable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.*;

/**
 *  Configuration node loader - reads from an XML file, and returns the
 * node corresponding to the Config parameters.  Used as part of test harnesses
 * - normally Axis will pass this node already-loaded to the wrapper

 *  @author           : M Hill
 */
public class VotElementLoader
{
   /**
    * Loads and returns the 'configParams' DOM node (Element) from the given
    * XML file
    */
   public static Element loadElement(String filename)
   {
      try
      {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         //dbf.setValidating(true);
         dbf.setIgnoringElementContentWhitespace(true);
         dbf.setNamespaceAware(true);
         DocumentBuilder builder = dbf.newDocumentBuilder();
         Document doc = builder.parse(new java.io.File(filename));
         
         Element rootNode =
            (Element) (doc.getElementsByTagName(VotTag.VOT_ROOT)).item(0);   //naughty, should use name-space version
         //            (doc.getElementsByTagNameNS(nameSpace,ROOT_TAG)).item(0);
         
         return rootNode;
         
      }
      catch (Exception e)
      {
         org.astrogrid.log.Log.logError("Failed to load parameters from '"+filename+"'",e);
         return null;
      }
   }
   
}

