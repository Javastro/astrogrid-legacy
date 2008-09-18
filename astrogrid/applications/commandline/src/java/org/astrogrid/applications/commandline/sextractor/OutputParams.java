/*
 * $Id: OutputParams.java,v 1.3 2008/09/18 08:46:46 pah Exp $
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

import voi.vowrite.VOTableField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class OutputParams {

   private String band;
   private List columnlist = new ArrayList();
   /**
    * 
    */
   public OutputParams(File paramfile, String band) throws IOException {

      FileReader fr = new FileReader(paramfile);
      BufferedReader br = new BufferedReader(fr);
      this.band = band;
      
      String line;

      while ((line = br.readLine()) != null) {
         String inline=line.trim();
         if (!inline.startsWith("#") && inline.length() > 0) {

            columnlist.add(inline);
         }
      }
      br.close();
      fr.close();

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
      //TODO - UCD handling needs to be done properly this is a quick fix
      if (fname.equals("NUMBER")) {
         
         
      }
      if(fname.equals("X_WORLD"))
      {
         retval.setName("RA");
         retval.setUcd("POS_EQ_RA_MAIN"); 
         retval.setRef("cs1");
         retval.setUnit("deg");
      }
      if(fname.equals("Y_WORLD"))
      {
         retval.setName("DEC");
         retval.setUcd("POS_EQ_DEC_MAIN"); 
         retval.setRef("cs1");
         retval.setUnit("deg");
      }
      // use this t get our photometry...
      if(fname.equals("MAG_ISO"))
      {
         retval.setUcd("PHOT_MAG_"+band);
      }
      if(fname.equals("MAGERR_ISO"))
      {
         retval.setUcd("PHOT_MAG_"+band+"_ERR");
      }

      return retval;

   }

}
