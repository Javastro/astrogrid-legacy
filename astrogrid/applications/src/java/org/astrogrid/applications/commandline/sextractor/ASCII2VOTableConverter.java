/*
 * $Id: ASCII2VOTableConverter.java,v 1.3 2004/01/22 12:41:36 pah Exp $
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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.astrogrid.applications.FileReferenceParameter;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.commandline.CmdLineApplicationEnvironment;

import voi.vowrite.VOTable;
import voi.vowrite.VOTableCoosys;
import voi.vowrite.VOTableField;
import voi.vowrite.VOTableInfo;
import voi.vowrite.VOTableResource;
import voi.vowrite.VOTableStreamWriter;
import voi.vowrite.VOTableTable;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class ASCII2VOTableConverter {


   private File tmpfile;
   private FileReferenceParameter outFileparam; 
   private FileReferenceParameter paramsFileparam; 
   private CmdLineApplicationEnvironment env;
   private OutputParams outputParams;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(ASCII2VOTableConverter.class);
   /**
    * 
    */
   public ASCII2VOTableConverter(Parameter outFile, Parameter paramsFile, CmdLineApplicationEnvironment env, String band) throws IOException {
      this.outFileparam = (FileReferenceParameter)outFile;
      this.paramsFileparam = (FileReferenceParameter)paramsFile;
      this.env = env;
      
      tmpfile = env.getTempFile();
      outputParams = new OutputParams(paramsFileparam.getRealFile(), band);
      
      
   }
   
   public void writeVOTable()
   {
      FileReader in = null;
      File outputfile = new File(env.getExecutionDirectory(), outFileparam.getRealFile().getPath());
      tmpfile.delete();
      boolean renamesuccess = outputfile.renameTo(tmpfile);

         try {
            in = new FileReader(tmpfile);
            FileOutputStream out = new FileOutputStream(outputfile);
            internalWriteVOTable(in, out);
            //reset the output file parameter so that it now refers to the newly generated VOTable
         }
         catch (FileNotFoundException e1) {
            logger.error("sextractor output file not found",e1);
         }
   }

   /**
    * Write a votable from the sextractor output.
    * @return The File object representing the new VOTable
    */
   void internalWriteVOTable(FileReader in, FileOutputStream out)
   {
      PrintStream prnStream = new PrintStream(out) ;

      // Create an instance of VOTableStreamingWriter class.
      VOTableStreamWriter voWrite = new VOTableStreamWriter(prnStream) ;

      //Create a votable element
      VOTable vot = new VOTable() ;

      //Set description of VOTable.
      String descString = "SExtractor output from Astrogrid ApplicationController run" ;

      vot.setDescription(descString) ;

      // Write the VOTable element to outputStream.
      voWrite.writeVOTable(vot) ;

      //Create a new resource element.          
      VOTableResource voResource = new VOTableResource() ;
      // TODO this needs to be found properly instead of just guessed....
      VOTableCoosys cosys = new VOTableCoosys();
      cosys.setID("cs1");
      cosys.setSystem("eq_FK5");
      cosys.setEpoch("2000");
      cosys.setEquinox("2000");
      voResource.addCOOSYS(cosys);
      VOTableInfo tabinfo = new VOTableInfo();
      tabinfo.setID("AGSEx");
      voResource.addInfo(tabinfo);

      // Write the Resource element to outputStream.
      voWrite.writeResource(voResource) ;

      // Create a new Table element
      VOTableTable voTab = new VOTableTable() ;
      voTab.setName("Table1") ;

      // Add two fields in the table.
      
      for (int i = 0; i < outputParams.getNumber(); i++) {
         VOTableField vofield = outputParams.getParamAsVOTableField(i);
         voTab.addField(vofield);
      }
      // Write the Table element to outputStream.
      voWrite.writeTable(voTab) ;
      
      BufferedReader rd = new BufferedReader(in);
      
      String line;
      
      try {
         while ((line = rd.readLine())!= null) {
            if(!line.startsWith("#")){
               String[] vals = line.trim().split(" +");
               voWrite.addRow(vals, vals.length);
            }
         }
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      // End the TABLE element.
      voWrite.endTable() ;

      // End the RESOURCE element.
 voWrite.endResource() ;

      // End the VOTABLE element.
 voWrite.endVOTable() ;
      
   }

}
