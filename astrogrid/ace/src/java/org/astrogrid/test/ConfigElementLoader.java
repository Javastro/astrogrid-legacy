/*
   $Id: ConfigElementLoader.java,v 1.1 2003/08/25 18:36:28 mch Exp $

   Date         Author      Changes
   17 Oct 2002  M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.*;
import org.astrogrid.xmlutils.*;

import org.astrogrid.tools.xml.EasyDomLoader;
import java.io.IOException;

import org.astrogrid.log.Log;

/**
 *  Configuration node loader - reads from an XML file, and returns the
 * node corresponding to the Config parameters.  Used as part of test harnesses
 * - normally Axis will pass this node already-loaded to the wrapper

 *  @author           : M Hill
 */
public class ConfigElementLoader
{

   /**
    * Loads and returns the 'configParams' DOM node (Element) from the given
    * XML file
    */
   public static Element loadElement(String filename) throws IOException
   {
      Element rootNode = EasyDomLoader.loadElement(filename);

      //quick fix for extra node depth
      //Element configNode = (Element) rootNode.getElementsByTagName("SExtractorData").item(0);
      Element configNode = rootNode;
      
      return configNode;
   }

}

