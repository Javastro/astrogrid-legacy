/*
   $Id: XmlTag.java,v 1.1 2004/03/03 10:08:01 mch Exp $

   Date        Author      Changes
   8 Oct 2002  M Hill      Created

   (c) Copyright...
*/
package org.astrogrid.io.xml;

import java.io.*;
import org.astrogrid.log.Log;

/**
 * An abstract class defining output methods for XML output streams and tags. By
 * using this 'intermediary' class, we can define tags or streams as XmlOutput,
 * and pass them around as the same thing.  Therefore there is no effective
 * difference between writing a node in a bigger XML file, and writing the
 * root node.
 *
 */

public class XmlTag extends XmlOutput
{
   private XmlOutput parentOut = null;
   private String name = null;
   
   public XmlTag(String givenName, String attrs, XmlOutput givenParentOut) throws IOException
   {
      super();
      
      this.parentOut = givenParentOut;
      this.name = givenName;
      
      //write out tag here WITHOUT indent at this level.
      if ((attrs == null) || (attrs.length() == 0))
      {
         writeLine("<"+name+">");
      }
      else
      {
         writeLine("<"+name+" "+attrs+">");
      }
         
   }
   
   /**
    * WriteIndentedLine implementation - calls parent with incremented
    * indentation, so that the indent increases for each level of tag
    */
   protected void writeIndentedLine(int indent, String string) throws IOException
   {
      parentOut.writeIndentedLine(indent+1, string);
   }
   
   /**
    * writeString implementation - writes a string out to the parent output
    */
   public void writeString(String s) throws IOException
   {
      parentOut.writeString(s);
   }
   
   /**
    * Close tag
    */
   public void close() throws IOException
   {
      Log.affirm(parentOut.getChild() == this, "Closing XML tag but it's not the child of its parent");

      closeChild();

      writeLine("</"+name+">");
      parentOut.clearChild();
      super.close();
   }
   
   /** For debug and display purposes
    */
   public String toString()
   {
      return "<"+name+">";
   }
   
}


