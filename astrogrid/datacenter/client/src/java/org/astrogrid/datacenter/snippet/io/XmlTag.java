/*
   $Id: XmlTag.java,v 1.1 2003/12/02 19:49:44 mch Exp $

   Date        Author      Changes
   8 Oct 2002  M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.datacenter.snippet.io;

import java.io.IOException;

/**
 * This represents a single element, with safegaurds against trying to add
 * child elements when it's been closed.
 *
 * @see XmlOutput for a full explanation of how this is used to help write XML
 * documents safely...
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
      assert parentOut.getChild() == this : "Closing XML tag but it's not the child of its parent";

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

