/*
 * $Id: OutputParams.java,v 1.1 2004/01/18 12:28:00 pah Exp $
 * 
 * Created on 17-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.commandline.sextractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import voi.vowrite.VOTableField;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class OutputParams {

   List columnlist = new ArrayList();
   /**
    * 
    */
   public OutputParams(File paramfile) throws IOException {

      FileReader fr = new FileReader(paramfile);
      BufferedReader br = new BufferedReader(fr);

      String line;

      while ((line = br.readLine()) != null) {
         String inline=line.trim();
         if (!inline.startsWith("#") && inline.length() > 0) {

            columnlist.add(inline);
         }
      }
      br.close();

   }

   public int getNumber() {
      return columnlist.size();
   }

   public VOTableField getParamAsVOTableField(int i) {
      VOTableField retval = new VOTableField();

      String fname = (String)columnlist.get(i);
      retval.setName(fname);
      retval.setUcd(fname);
      retval.setDataType("float");

      return retval;

   }

}
