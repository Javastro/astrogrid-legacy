/*
   $Id: XmlTagWriter.java,v 1.1 2004/07/01 22:37:15 mch Exp $

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

public class XmlTagWriter
{
   private XmlTagWriter parent = null;
   private XmlTagWriter child = null;  //can only have one child at a time
   private String name = null;

   
   public XmlTagWriter(String givenName, String attrs, XmlTagWriter parentWriter) throws IOException
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
    * writeString implementation - writes a string out to the parent output
    */
   public void writeString(String s) throws IOException
   {
      parent.writeString(s);
   }

   /**
    * Convenience routine to write a tag on one line
    */
   public void writeTag(String tag, String value) throws IOException
   {
      this.writeIndentedLine("<"+tag+">"+transformSpecials(value)+"</"+tag+">");
   }

   /**
    * Convenience routine to write a comment
    */
   public void writeComment(String text) throws IOException
   {
      this.writeIndentedLine("<!-- "+text+" -->");
   }

   /**
    * Writes out a tag with attributes (given as "name='Value'" etc) on one line
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
    * Called to make a new simple tag with a name and a string of attributes.
    */
   public XmlTagWriter newTag(String tag, String attr) throws IOException
   {
      closeChild();
      return newTag(new XmlTagWriter(tag, attr, this));
   }

   /**
    * Called to make a new simple tag with a name
    */
   public XmlTagWriter newTag(String tag) throws IOException
   {
      closeChild();
      return newTag(new XmlTagWriter(tag, null, this));
   }

   /**
    * Called to take the given tag and make it the child.  Allows for specialist
    * derived Tags.
    */
   public XmlTagWriter newTag(XmlTagWriter tag) throws IOException
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
   protected XmlTagWriter getChild()
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


