/*
   $Id: XmlOutput.java,v 1.1 2003/08/25 18:36:31 mch Exp $

   Date        Author      Changes
   8 Oct 2002  M Hill      Created

   (c) Copyright...
*/
package org.astrogrid.tools.xml;

import java.io.*;
import org.astrogrid.log.Log;
import org.astrogrid.tools.ascii.AsciiOutputStream;

/**
 * An abstract class defining output methods for XML output streams and tags. By
 * using this 'intermediary' class, we can define tags or streams as XmlOutput,
 * and pass them around as the same thing.  Therefore there is no effective
 * difference between writing a node in a bigger XML file, and writing the
 * root node directly to an XmlOutputStream
 */

public abstract class XmlOutput extends OutputStream
{
   private XmlTag childTag = null;

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
    * Abstract method that should implement writing out the given string at
    * the given indent.  The indent is an integer representing the number of
    * levels to indent.  The string should not include the new line character
    */
   protected abstract void writeIndentedLine(int indent, String string) throws IOException;

   /**
    * Abstract method that should implement writing a string directly to the
    * stream.  NB this won't do any indenting/etc - not sure if it should
    * therefore be protected...
    */
   public abstract void writeString(String s) throws IOException;

   /**
    * Writes out a tag on one line
    */
   public void writeTag(String tag, String value) throws IOException
   {
      this.writeIndentedLine("<"+tag+">"+transformSpecials(value)+"</"+tag+">");
   }

   /**
    * Writes out a tag with attributes on one line
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
    * Called to make a new simple tag with a name and an attribute.
    */
   public XmlTag newTag(String tag, String attr) throws IOException
   {
      closeChild();
      return newTag(new XmlTag(tag, attr, this));
   }

   /**
    * Called to make a new simple tag with a name
    */
   public XmlTag newTag(String tag) throws IOException
   {
      closeChild();
      return newTag(new XmlTag(tag, null, this));
   }

   /**
    * Called to take the given tag and make it the child.  Allows for specialist
    * derived Tags.
    */
   public XmlTag newTag(XmlTag tag) throws IOException
   {
      closeChild();
      childTag = tag;
      return childTag;
   }

   /** remove the child tag */
   protected void clearChild()
   {
      childTag = null;
   }

   /** closes the child tag  */
   protected void closeChild() throws IOException
   {
      if (childTag != null)
      {
         childTag.close();
         childTag = null;
      }
   }

   /**
    * Returns true if the tag has a child tag - implying the child tag is open */
   public boolean hasChild()
   {
      return (childTag != null);
   }

   /**
    * Returns the child tag - only used for administration purposes by
    * subclasses
    */
   protected XmlTag getChild()
   {
      return childTag;
   }

   /**
    * It's a bit naughty using OutputStream, as we don't implement the one
    * method that should be - write(int).  However we do use the other methods
    * - eg close - so we just chuck an exception here if anyone tries to
    * use it
    */
   public void write(int i)
   {
      throw new IllegalArgumentException("XmlOutput is not intended for writing ints - use writeString(String)");
   }
}
