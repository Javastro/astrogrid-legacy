/*
   $Id: XmlOutputStream.java,v 1.1 2002/11/27 16:58:47 mch Exp $

   Date        Author      Changes
   8 Oct 2002  M Hill      Created

   (c) Copyright...
*/
package org.astrogrid.tools.xml;

import java.io.*;
import org.astrogrid.log.Log;
import org.astrogrid.tools.ascii.AsciiOutputStream;

/**
 * Output stream for writing XML files.  Probably mostly used as a superclass for
 * various sepcific XML formats.  Provides helper classes, such as the XmlTagBlock,
 * to help write blocks correctly, so that closing tags are ensured.
 * An output stream is slightly pecuiliar in that it can only have one root tag,
 * preceeded possibly by comments
 *
 */

public class XmlOutputStream extends XmlOutput
{
   private AsciiOutputStream out = null;
   
   private String indentSpaces = "   ";
   private final static int INDENT_SIZE = 3;

   private int state = STATE_PRETAG;
   private final static int STATE_PRETAG = 0;   //tag has not yet been set
   private final static int STATE_TAG = 1;      //tag is set
   private final static int STATE_POSTTAG = 2;  //tag has been closed
   
   /**
    * Constructor - pass in output stream to pipe to, and
    * the initial header will be written
    */
   public XmlOutputStream(OutputStream anOutStream) throws IOException
   {
      super();
      out = new AsciiOutputStream(anOutStream);
      writeLine("<?xml version=\"1.0\"?>");
   }
   
   /**
    * Should only be able to write comment tags at the root level (ie, at
    * outputstream level) before any child tags are added
    */
   public void writeCommentTag(String tag) throws IOException
   {
      Log.affirm(state == STATE_PRETAG, "Cannot write comment "+tag+" as tag "+getChild()+" is set");
      //       Log.assert( (tag.charAt(0) != '!') && (tag.charAt(0) != '?'), "Can only write comments (! or ?) before root tag, not '"+tag+"'");
      writeLine("<"+tag+">");
   }
   
   /**
    * newTag overridden to ensure only one tag can be set
    */
   public XmlTag newTag(XmlTag aTag) throws IOException
   {
      Log.affirm(state == STATE_PRETAG, "Cannot set new tag - one "+getChild()+" already set");
      state = STATE_TAG;
      return super.newTag(aTag);
   }
   
   /**
    * writeString implementation - writes to the 'output stream' given in
    * the constructor, using the AsciiOutputStream as a filter.
    */
   public void writeString(String s) throws IOException
   {
      Log.affirm(state != STATE_POSTTAG, "Cannot write - tag has closed");
      out.write(s);
   }

   /**
    * Implements the XmlOutput.writeIndentedLine method that writes a string
    * plus a new line character, with a suitable number of spaces at the
    * beginning corresponding to the given indent.
    */
   protected void writeIndentedLine(int indentIndex, String string) throws IOException
   {
      Log.affirm(state != STATE_POSTTAG, "Cannot write - tag has closed");
      
      //convert indent index to index spaces
      //there's a bit of a botch here to allow root nodes to be on the same level
      //(ie 0 spaces) as the comment tags.
      int indent = (indentIndex-1)*INDENT_SIZE;
      if (indent<0)
      {
         indent = 0;
      }
      
      //by reusing the same string when possible we save on garbage collection.
      while (indentSpaces.length() < indent)
      {
         indentSpaces = indentSpaces + indentSpaces;
      }
        
      out.write(indentSpaces.substring(0, indent) + string + "\n");
   }

   /**
    * Close stream - ensure all tag blocks are closed too
    */
   public void close() throws IOException
   {
      closeChild();
      super.close();
      state = STATE_POSTTAG;
   }

   
   /** Test harness method - writes out a simple table
    */
   public static void main(String args[])
   {
      System.out.println("Hello World!");
      
      try
      {
         XmlOutputStream xOut = new XmlOutputStream(System.out);
         
         XmlTag ftag = xOut.newTag("FRUIT","");

         ftag.writeTag("DESCRIPTION","Sort of fruity things");
         
         XmlTag atag = ftag.newTag("APPLE","");
         atag.writeTag("SKIN","Rosy");
         atag.writeTag("FLESH","White & Powdery");
         atag.writeTag("STALK","Yes unless it's fallen off");
         atag.close();
         
         XmlTag otag = ftag.newTag("ORANGE","");
         otag.writeTag("SKIN","Orange");
         otag.writeTag("FLESH","Really Orange. Or Red.");
         otag.writeTag("STALK","Would be orange but I've eaten it");
//         otag.close();
         
         xOut.close();
         
      } catch (IOException ioe)
      {
         System.out.println("IOE: "+ioe);
      }
      
      System.out.println("Goodbye Cruel World!");
      
   }
}
