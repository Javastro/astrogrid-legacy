/*
 * $Id: IOUtil.java,v 1.2 2004/04/16 16:47:23 pah Exp $
 * 
 * Created on 14-Apr-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.common.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Some general utility methods. These are mainly static methods to do some useful IO related functionns.
 * @author Paul Harrison (pah@jb.man.ac.uk) 14-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public class IOUtil {

   /**
    * 
    */
   public IOUtil() {
      super();
      // TODO Auto-generated constructor stub
   }
   
   /**
    * @param is
    * @param file
    * @throws IOException
    */
   public static void copyStreamToFile(InputStream is, File file) throws IOException
   {
      OutputStream out = new FileOutputStream(file);
      BufferedInputStream bis = new BufferedInputStream(is);
      int i;
      while ((i=bis.read()) != -1) {
         out.write(i);
      }
      out.close();
      
   }

   /**
    * @param out
    * @param realFile
    */
   public static void writeFile(OutputStream out, File realFile) throws IOException {
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(realFile));
      int i;
      while ((i=in.read()) != -1) {
         out.write(i);
         
      }
      out.close();
   }

   /**
    * @param string
    * @param inf
    */
   public static void saveStringToFile(String string, File inf) throws FileNotFoundException {
      PrintWriter pw = new PrintWriter(new FileOutputStream(inf));
      pw.write(string);
      pw.close();
   }

}
