/*
 * $Id: ASCII2VOTableConverter.java,v 1.7 2008/09/18 08:46:46 pah Exp $
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

import org.astrogrid.applications.commandline.DefaultCommandLineParameterAdapter;
import org.astrogrid.applications.environment.ApplicationEnvironment;

import voi.vowrite.VOTable;
import voi.vowrite.VOTableCoosys;
import voi.vowrite.VOTableField;
import voi.vowrite.VOTableInfo;
import voi.vowrite.VOTableResource;
import voi.vowrite.VOTableStreamWriter;
import voi.vowrite.VOTableTable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class ASCII2VOTableConverter {


   private File tmpfile;
   private DefaultCommandLineParameterAdapter outFileparam; 
   private DefaultCommandLineParameterAdapter paramsFileparam; 
   private ApplicationEnvironment env;
   private OutputParams outputParams;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(ASCII2VOTableConverter.class);
  
   /**
    * @param outFile
    * @param paramsFile
    * @param env
    * @param band
    * @throws IOException
    */
   public ASCII2VOTableConverter(DefaultCommandLineParameterAdapter outFile, DefaultCommandLineParameterAdapter paramsFile, ApplicationEnvironment env, String band) throws IOException {
      this.outFileparam = outFile;
      this.paramsFileparam = paramsFile;
      this.env = env;
      
      tmpfile = env.getTempFile(paramsFileparam.getWrappedParameter().getId());
      outputParams = new OutputParams(paramsFileparam.getReferenceFile(), band);   
       
      
   }
   
   public void writeVOTable()
   {
      FileReader in = null;
      tmpfile.delete();
      boolean renamesuccess = outFileparam.getReferenceFile().renameTo(tmpfile);

         try {
            in = new FileReader(tmpfile);
            FileOutputStream out = new FileOutputStream(outFileparam.getReferenceFile());
            internalWriteVOTable(in, out);
         }
         catch (FileNotFoundException e1) {
            logger.error("sextractor output file not found",e1);
         }
         finally{
         try {
	    in.close();
	} catch (IOException e) {
	    logger.error("failed to close file", e);
	}
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
