package org.astrogrid.io.ascii;

/**
 * Description: ASCII Output stream.  Includes various methods for
 * writing out numbers, text, etc to an ASCII text file
 *
 * @author           : M Hill
 *
 **/
import java.io.*;

public class AsciiOutputStream extends FilterOutputStream implements AsciiCodes
{
   
   /**
    * Standard FilterStream-type constructor. .
    * @param out java.io.OutputStream
    */
   public AsciiOutputStream(java.io.OutputStream out)
   {
      super(out);
   }

   /**
    * prefixes any of the given occurences of character 'findChar' with "\\" and replaces
    * with the second character so that they get interpreted as literals rather than
    * special codes.
    *
    **/
   private void escape(StringBuffer b, char findChar, char replaceChar)
   {
      for(int i = 0; i < b.length(); i++) {
         if (b.charAt(i) == findChar) {
            b.insert(i++,'\\');
            b.setCharAt(i++, replaceChar);
         }
      }
   }
   /**
    * Wraps string representation of given object x in quotes, and ensures that any
    * 'odd' characters within it - such as other quotes - are 'escaped' so they are
    * interpreted correctly when re-read.
    */
   public String quoteWrap(Object x)
   {
      StringBuffer sb = new StringBuffer(x.toString());
      escape(sb, '"', '"');
      escape(sb, '\n', 'n');
      escape(sb, '\r', 'r');
      return "" + QUOTE + sb + QUOTE;
   }

   /**
    * Writes the given string
    */
   public void write(String s) throws IOException
   {
      if (s == null) return;
      
      write(s.getBytes());
   }

   /**
    * Writes out a 'real' number in ASCII form.
    */
   public void write(double r) throws IOException
   {
      write(Double.toString(r));
   }

   /**
    * Writes out an integer number in ASCII form.
    */
   public void write(long i) throws IOException
   {
      write(Long.toString(i));
   }
    
    /**
     * Writes a line
     */
    public void writeLine(String s) throws IOException
    {
        write(s);
        write(NL);
    }
}


