/* $Id: FileUploadExtractor.java,v 1.1 2004/04/15 14:07:56 mch Exp $
 * Taken from the akadia package
 */

package org.astrogrid.mySpace.webapp;

import java.io.*;

import java.util.Hashtable;

/**
 * Used to extract mime attachments from http posted messages, eg for uploading
 * files from the user's HD
 */

public class FileUploadExtractor {
   
   /** reads the input stream (the servlet request input stream), when it finds
    * the mime bit with a filename in it, pipes that to 'out'
    */
   public void upload(InputStream in, OutputStream out) throws IOException {
      
      byte[] line = new byte[128];
      int i = in.read(line, 0, 128);
      if (i < 3)
         return;
      int boundaryLength = i - 2;
      
      String boundary = new String(line, 0, boundaryLength); // -2 discards the newline character
      Hashtable fields = new Hashtable();
      
      while (i != -1) {
         String newLine = new String(line, 0, i);

         if (newLine.startsWith("Content-Disposition: form-data; name=\"")) {
            if (newLine.indexOf("filename=\"") != -1) {
               String filename = new String(line, 0, i-2);
               if (filename==null)
                  return;
               //this is the file content
               i = in.read(line, 0, 128);
               String contentType = new String(line, 0, i-2);
               i = in.read(line, 0, 128);
               // blank line
               i = in.read(line, 0, 128);
               newLine = new String(line, 0, i);
               PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
               while (i != -1 && !newLine.startsWith(boundary)) {
                  // the problem is the last line of the file content
                  // contains the new line character.
                  // So, we need to check if the current line is
                  // the last line.
                  i = in.read(line, 0, 128);
                  if ((i==boundaryLength+2 || i==boundaryLength+4) // + 4 is eof
                      && (new String(line, 0, i).startsWith(boundary)))
                     pw.print(newLine.substring(0, newLine.length()-2));
                  else
                     pw.print(newLine);
                  newLine = new String(line, 0, i);
                  
               }
               pw.close();
               
            }
            else {
               //this is a field
               // get the field name
               int pos = newLine.indexOf("name=\"");
               String fieldName = newLine.substring(pos+6, newLine.length()-3);
               // System.out.println("fieldName:" + fieldName);
               // blank line
               i = in.read(line, 0, 128);
               i = in.read(line, 0, 128);
               newLine = new String(line, 0, i);
               StringBuffer fieldValue = new StringBuffer(128);
               while (i != -1 && !newLine.startsWith(boundary)) {
                  // The last line of the field
                  // contains the new line character.
                  // So, we need to check if the current line is
                  // the last line.
                  i = in.read(line, 0, 128);
                  if ((i==boundaryLength+2 || i==boundaryLength+4) // + 4 is eof
                      && (new String(line, 0, i).startsWith(boundary)))
                     fieldValue.append(newLine.substring(0, newLine.length()-2));
                  else
                     fieldValue.append(newLine);
                  newLine = new String(line, 0, i);
               }
               //System.out.println("fieldValue:" + fieldValue.toString());
               fields.put(fieldName, fieldValue.toString());
            }
         }
         i = in.read(line, 0, 128);
      } // end while
   }
}

/*
 $Log: FileUploadExtractor.java,v $
 Revision 1.1  2004/04/15 14:07:56  mch
 Added file uploader helper

 */

