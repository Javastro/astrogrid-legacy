/*
   $Id: SExtractorOutputMonitor.java,v 1.1.1.1 2003/08/25 18:36:16 mch Exp $

   Date       Author      Changes
   3 Dec 02   M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.ace.sextractor;

import java.io.OutputStream;

/**
 * Monitors the output of SExtractor for things like errors, etc.  Caches
 * a few lines, and any beginning with "Warning" or "Error"
 *
 * @version %I%
 * @author M Hill
 */
   
public class SExtractorOutputMonitor extends OutputStream
{
   StringBuffer working = new StringBuffer();

   String[] lastLines = new String[10];
   
   String lastError = null;
   String lastWarning = null;
   
   /**
    * Called by the FilteredOutputStream (or OutputStreamSplitter) when
    * a character is to be written
    */
   public void write(int b) throws java.io.IOException
   {
      if (b != '\n')
      {
         working.append((char) b);
      }
      else
      {
         //chops off before first ">", as this marks the beginnig of the text
         int i= working.toString().indexOf('>');
         if (i==-1)
         {
            newLine(working.toString());
         }
         else
         {
            newLine(working.substring(i+2,working.length()));
         }
         working.setLength(0);
      }
   }
   
   private void newLine(String s)
   {
      //shuffle down lines
      for (int i=0;i<lastLines.length-2;i++)
      {
         lastLines[i] = lastLines[i+1];
      }
      lastLines[lastLines.length-1] = s;
      
      if (s.toLowerCase().startsWith("*error*"))
      {
         lastError = s;
      }
      else if (s.toLowerCase().indexOf("warning")>-1)
      {
         lastWarning = s;
      }
   }
   
   public String getLastLine()
   {
      return lastLines[lastLines.length-1];
   }

   public String getLastError()
   {
      return lastError;
   }

}

