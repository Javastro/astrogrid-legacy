/*
   $Id: XmlTagPrinter.java,v 1.1 2004/07/02 16:52:20 mch Exp $

   (c) Copyright...
*/
package org.astrogrid.io.xml;

import java.io.*;
import org.astrogrid.log.Log;

/**
 * Class used to write to an Xml Element.  The element is always created within
 * the context of another element (the root element being the file).
 *
 */

public class XmlTagPrinter
{
   private XmlTagPrinter parent = null;
   private XmlTagPrinter child = null;  //can only have one child at a time
   private String name = null;

   
   public XmlTagPrinter(String givenName, String attrs, XmlTagPrinter parentWriter) throws IOException
   {
      super();
      
      this.parent = parentWriter;
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
    * Writes out the given string as an indented line
    * at this level plus one (the indentation is dependent on the number of
    * children this tag/stream has)
    */
   public void writeIndentedLine(String string) throws IOException
   {
      Log.affirm(!hasChild(), "Cannot write to this tag ["+this+"], it has an open child "+getChild());
      writeIndentedLine(1, string);
   }

   /**
    * Writes out the given string as an indented line
    * at this level (ie the indentation is dependent on the number of
    * children this tag/stream has)
    */
   public void writeLine(String string) throws IOException
   {
      Log.affirm(!hasChild(), "Cannot write to this tag ["+this+"], it has an open child "+getChild());
      writeIndentedLine(0, string);
   }

   /**
    * WriteIndentedLine implementation - calls parent with incremented
    * indentation, so that the indent increases for each level of tag
    */
   protected void writeIndentedLine(int indent, String string) throws IOException
   {
      parent.writeIndentedLine(indent+1, string);
   }
   
   /**
    * writeString implementation - writes the given string within the opening
    * & closing tags of this element. Really you should use writeTag or writeComment...
    */
   public void writeString(String s) throws IOException
   {
      parent.writeString(s);
   }

   /**
    * Convenience routine to write a child tag within this element
    */
   public void writeTag(String tag, String value) throws IOException
   {
      this.writeIndentedLine("<"+tag+">"+transformSpecials(value)+"</"+tag+">");
   }

   /**
    * Convenience routine to write a child comment tag within this element
    */
   public void writeComment(String text) throws IOException
   {
      this.writeIndentedLine("<!-- "+text+" -->");
   }

   /**
    * Convenience routine to write a child tag with attributes (given as
    * "name='Value' other='more' " etc) within this element
    */
   public void writeTag(String tag, String attr, String value) throws IOException
   {
      this.writeIndentedLine("<"+tag+" "+attr+">"+transformSpecials(value)+"</"+tag+">");
   }

   /**
    * Transforms special characters to safe ones (eg & to &amp, < to &lt)
    */
   public static String transformSpecials(String s)
   {
      //java v1.4
      /*
      s = s.replaceAll("&", "&amp;");  //do first so we don't catch specials
      s = s.replaceAll("<", "&lt;");
      s = s.replaceAll(">", "&gt;");
      return s;
       /**/

      /**/
      //pre java 1.4
      int pos =0;
      while ((pos = s.indexOf('&',pos+1)) != -1)
      {
         s = s.substring(0,pos)+"&amp;"+s.substring(pos+1);
      }
      while ((pos = s.indexOf('<')) != -1)
      {
         s = s.substring(0,pos)+"&lt;"+s.substring(pos+1);
      }
      while ((pos = s.indexOf('>')) != -1)
      {
         s = s.substring(0,pos)+"&gt;"+s.substring(pos+1);
      }
      
      return s;
       /**/
   }
   
   /**
    * Called to make a new child tag with the given tag name and a string of attribute/values.
    * @param attr a string such as " name='this' size='other' "
    */
   public XmlTagPrinter newTag(String tag, String attr) throws IOException
   {
      closeChild();
      return newTag(new XmlTagPrinter(tag, attr, this));
   }

   /**
    * Called to make a new child tag with the given tag name
    */
   public XmlTagPrinter newTag(String tag) throws IOException
   {
      closeChild();
      return newTag(new XmlTagPrinter(tag, null, this));
   }

   /**
    * Called to take the given tag and make it the child.  Useful for specialist
    * derived Tags.
    */
   public XmlTagPrinter newTag(XmlTagPrinter tag) throws IOException
   {
      closeChild();
      child = tag;
      return child;
   }

   /** remove the child tag */
   protected void clearChild()
   {
      child = null;
   }

   /** closes the child tag  */
   protected void closeChild() throws IOException
   {
      if (child != null)
      {
         child.close();
         child = null;
      }
   }

   /**
    * Returns true if the tag has a child tag - implying the child tag is open */
   public boolean hasChild()
   {
      return (child != null);
   }

   /**
    * Returns the child tag - only used for administration purposes by
    * subclasses
    */
   protected XmlTagPrinter getChild()
   {
      return child;
   }

   
   /**
    * Close tag
    */
   public void close() throws IOException
   {
      Log.affirm(parent.getChild() == this, "Closing XML tag but it's not the child of its parent");

      closeChild();

      writeLine("</"+name+">");
      parent.clearChild();
   }
   
   /** For debug and display purposes
    */
   public String toString()
   {
      return "<"+name+">";
   }
   
}

/*
 $Log: XmlTagPrinter.java,v $
 Revision 1.1  2004/07/02 16:52:20  mch
 More sensible names



   Date        Author      Changes
   8 Oct 2002  M Hill      Created

 */

   
