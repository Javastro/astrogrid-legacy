package org.astrogrid.io.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * A special XmlTagPrinter used to represent the underlying output stream.
 * <p>
 *
 */

public class XmlAsciiWriter extends XmlPrinter
{
   private PrintWriter out = null;

   private String indentSpaces = "   ";
   private final static int INDENT_SIZE = 3;

   /** marker to say whether root tag has already been opened.  Can only have
    * one root tag.*/
   private boolean startedRootTag = false;

   /**
    * Constructor - pass in output stream to pipe to, and
    * the initial header will be written if the flag is true.
    */
   public XmlAsciiWriter(Writer aWriter, boolean writeProcInstHdr) throws IOException
   {
      super(null, null, null);
      out = new PrintWriter(aWriter);
      open(writeProcInstHdr);
   }

   /**
    * Constructor - pass in output stream to pipe to, and
    * the initial header will be written
    */
   public XmlAsciiWriter(OutputStream out, boolean writeProcInstHdr) throws IOException
   {
      this(new OutputStreamWriter(out), writeProcInstHdr);
   }


   protected void open(boolean writeProcInstHdr) throws IOException{
      if (writeProcInstHdr) {
         writeLine(0,"<?xml version=\"1.0\"?>");
      }
   }

   /**
    * Should only be able to write comment tags at the root level (ie, at
    * outputstream level) before any child tags are added
    *
   public void writeCommentTag(String tag) throws IOException
   {
      Log.affirm(state == STATE_PRETAG, "Cannot write comment "+tag+" as tag "+getChild()+" is set");
      //       Log.assert( (tag.charAt(0) != '!') && (tag.charAt(0) != '?'), "Can only write comments (! or ?) before root tag, not '"+tag+"'");
      writeLine("<"+tag+">");
   }
    /**/

   /**
    * newTag overridden to ensure only one tag can be set
    */
   public XmlPrinter newTag(XmlPrinter aTag) throws IOException
   {
      assert !startedRootTag : "Cannot have more than one tag at root, trying to add "+aTag;
      startedRootTag = true;
      return super.newTag(aTag);
   }

   /**
    * writeString implementation - writes to the 'output stream' given in
    * the constructor, using the AsciiOutputStream as a filter.
    * NB - all writing is eventually channelled through this.
    */
   protected void writeString(String s) throws IOException
   {
      out.write(s);
   }

   /**
    * Implements the XmlOutput.writeIndentedLine method that writes a string
    * plus a new line character, with a suitable number of spaces at the
    * beginning corresponding to the given indent.
    */
   protected void writeLine(int indentIndex, String string) throws IOException
   {
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

      writeString(indentSpaces.substring(0, indent) + string + "\n");
   }

   /**
    * Close tag - and output stream
    */
   public void close() throws IOException
   {
      super.close();
      out.close();
   }


   /** Test harness method - writes out a simple table
    */
   public static void main(String args[])
   {
      System.out.println("Hello World!");

      try
      {
         XmlAsciiWriter xOut = new XmlAsciiWriter(new OutputStreamWriter(System.out), true);

         XmlPrinter ftag = xOut.newTag("FRUIT");

         ftag.writeTag("DESCRIPTION","Sort of fruity things");

         XmlPrinter atag = ftag.newTag("APPLE");
         atag.writeTag("SKIN","Rosy");
         atag.writeTag("FLESH","White & Powdery");
         atag.writeTag("STALK","Yes unless it's fallen off");
         atag.close();

         XmlPrinter otag = ftag.newTag("ORANGE");
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



