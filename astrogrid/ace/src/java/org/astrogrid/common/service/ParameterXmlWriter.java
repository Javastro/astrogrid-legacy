/*
   ParameterXmlWriter.java

   Date      Author      Changes
   $date$    M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.service;

import org.astrogrid.tools.xml.*;
import org.astrogrid.common.wrapper.ParameterBundle;
import org.astrogrid.common.wrapper.Parameter;
import java.io.*;

/**
 * ParameterXmlWriter takes a ParameterBundle and writes it out in XML form,
 * ready for use within SOAP messages.
 *
 * Used for testing, conversions, and for returning to the user, in XML form,
 * the parameters used as inputs.
 *
 * @see org.astrogrid.service.ParameterXmlWriter
 *
 * @version %I%
 * @author M Hill
 */
   
public class ParameterXmlWriter
{
   
   /** An example of writing out a bundle to a file
    */
   public static void writeXmlConfigFile(ParameterBundle rootBundle, String filename) throws IOException
   {
      FileOutputStream out = new FileOutputStream(filename);
      writeXmlConfigFile(rootBundle, out);
   }
   
   /** An example of writing out a bundle to a file
    */
   public static void writeXmlConfigFile(ParameterBundle rootBundle, OutputStream out) throws IOException
   {
      XmlOutputStream xmlOut = new XmlOutputStream(out);
      
      xmlOut.writeCommentTag("!DOCTYPE ExampleXmlConfiguration");
      xmlOut.writeLine("");

      XmlTag tag = xmlOut.newTag("Example");
      xmlOut.writeLine("");

      rootBundle.writeAsXml(xmlOut);
      
      xmlOut.close();
   }

   
}

