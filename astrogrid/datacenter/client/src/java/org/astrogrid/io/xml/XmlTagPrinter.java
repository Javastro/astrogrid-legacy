/*
   $Id: XmlTagPrinter.java,v 1.2 2004/07/06 14:43:19 mch Exp $

   (c) Copyright...
*/
package org.astrogrid.io.xml;

import java.io.*;

/**
 * Class used to write to an Xml Element.  The element is always created within
 * the context of another element (the root element being the file).
 * <p>
 * Not very happy with this at the moment; XmlPrinter both subclasses this and
 * acts as the root node, which is a bit dodgy.  It means for example that there
 * are problems writing things in the XmlTagPrinter constructor, as the XmlPrinter
 * hasn't initialised things properly
 */

public class XmlTagPrinter
{
   private XmlTagPrinter parent = null;
   private XmlTagPrinter child = null;  //can only have one child at a time
   private String name = null;
   private String attrs = null;
   private boolean closed = false;

   public XmlTagPrinter(String givenName, String givenAttrs, XmlTagPrinter parentWriter) throws IOException
   {
      super();
      
      this.parent = parentWriter;
      this.name = givenName;
      this.attrs = givenAttrs;
   }
   
   /** Opens the tag - ie writes out the name & atts */
   protected void open() throws IOException {
      //write out tag here WITHOUT indent at this level.
      if (name != null) {
         if ((attrs == null) || (attrs.length() == 0))
         {
            writeLine(0,"<"+name+">");
         }
         else
         {
            writeLine(0,"<"+name+" "+attrs+">");
         }
      }
   }
   
   /**
    * Writes out the given string indented relative to this tag.
    */
   public void writeLine(String string) throws IOException
   {
      assert !closed : "Cannot write to a closed tag ["+this+"]";
      assert !hasChild() : "Cannot write to this tag ["+this+"], it has an open child "+getChild();
      writeLine(1, string);
   }

   /**
    * Writes a string with the given indentation, relative to this tag.
    * By default calls the parentWriteLine implementation - calls parent with incremented
    * indentation, so that the indent increases for each level of tag
    */
   protected void writeLine(int indent, String string) throws IOException
   {
      parent.writeLine(indent+1, string);
   }
   
   /**
    * Convenience routine to write a child tag within this element
    */
   public void writeTag(String tag, String value) throws IOException
   {
      closeChild();
      this.writeLine("<"+tag+">"+transformSpecials(value)+"</"+tag+">");
   }

   /**
    * Convenience routine to write a child comment tag within this element
    */
   public void writeComment(String text) throws IOException
   {
      closeChild();
      this.writeLine("<!-- "+text+" -->");
   }

   /**
    * Convenience routine to write a child tag with attributes (given as
    * "name='Value' other='more' " etc) within this element
    */
   public void writeTag(String tag, String attr, String value) throws IOException
   {
      closeChild();
      this.writeLine("<"+tag+" "+attr+">"+transformSpecials(value)+"</"+tag+">");
   }

   /**
    * Transforms special characters to safe ones (eg & to &amp, < to &lt)
    */
   public static String transformSpecials(String s)
   {
      //java v1.4
      s = s.replaceAll("&", "&amp;");  //do first so we don't catch specials
      s = s.replaceAll("<", "&lt;");
      s = s.replaceAll(">", "&gt;");
      return s;
       /**/

      /*
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
    * Called to take the given tag and make it the child.  Closes existing child.
    * Public for use by specialist derived Tags.
    */
   public XmlTagPrinter newTag(XmlTagPrinter tag) throws IOException
   {
      closeChild();
      child = tag;
      child.open();
      return child;
   }

   /**
    * Convenience routine to make a new child tag with the given tag name and a string of attribute/values.
    * Automatically closes existing child
    * @param attr a string such as " name='this' size='other' "
    */
   public XmlTagPrinter newTag(String tag, String attr) throws IOException
   {
      return newTag(new XmlTagPrinter(tag, attr, this));
   }

   /**
    * Convenience routine to make a new child tag with the given tag name
    * Automatically closes existing child
    */
   public XmlTagPrinter newTag(String tag) throws IOException
   {
      return newTag(new XmlTagPrinter(tag, null, this));
   }


   /**
    * Returns true if the tag has a child tag - implying the child tag is open */
   public boolean hasChild()
   {
      return (getChild() != null);
   }

   /**
    * Returns the child tag - only used for administration purposes by
    * subclasses
    */
   protected XmlTagPrinter getChild()
   {
      //if it's been closed, remove it
      if ( (child != null) && (child.closed)) {
         child = null;
      }
      
      return child;
   }

   
   /** closes the child tag  */
   protected void closeChild() throws IOException
   {
      if (getChild() != null)
      {
         child.close();
         child = null;
      }
   }

   /**
    * Close tag and its child tags
    */
   public void close() throws IOException
   {
      assert !closed : "Reclosing tag "+name;
      
      closeChild();

      if (name != null) {
         writeLine(0,"</"+name+">");
      }
   
      closed = true;
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
 Revision 1.2  2004/07/06 14:43:19  mch
 Fixed a series of bugs

 Revision 1.1  2004/07/02 16:52:20  mch
 More sensible names



   Date        Author      Changes
   8 Oct 2002  M Hill      Created

 */

   
