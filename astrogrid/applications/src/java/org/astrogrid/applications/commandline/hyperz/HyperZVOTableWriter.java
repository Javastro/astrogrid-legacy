/*
 * $Id: HyperZVOTableWriter.java,v 1.1 2004/01/22 12:41:36 pah Exp $
 * 
 * Created on 20-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.hyperz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;



import cds.savot.model.FieldSet;
import cds.savot.model.SavotField;
import cds.savot.model.SavotResource;
import cds.savot.model.SavotTD;
import cds.savot.model.SavotTable;
import cds.savot.model.SavotVOTable;
import cds.savot.model.TDSet;
import cds.savot.model.TRSet;
import cds.savot.samples.WriteDocument;

import org.astrogrid.applications.FileReferenceParameter;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.commandline.CmdLineApplicationEnvironment;

/**
 * Writes out a votable, by appending the results to the end of each row of the input VOTable.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class HyperZVOTableWriter {

 
   private SavotVOTable voTable;

   private CmdLineApplicationEnvironment env;

   private File tmpfile;

    /**
    * @param inputVOTable
    * @param parameter
    * @param applicationEnvironment
    */
   public HyperZVOTableWriter(SavotVOTable inputVOTable, Parameter parameter, CmdLineApplicationEnvironment applicationEnvironment) {
     outCat = (FileReferenceParameter)parameter;
    this.env = applicationEnvironment;
    tmpfile = env.getTempFile();
    voTable = inputVOTable;
    
   }
   private FileReferenceParameter outCat;

   public void write() {
      
      //FIXME the file name will be wrong when copying into myspace
      File outputfil = new File(env.getExecutionDirectory(), outCat.getRealFile().getPath()+".z_phot");
      tmpfile.delete();
      boolean renamesuccess = outputfil.renameTo(tmpfile);
      
      internalAddToVOTable(tmpfile);
      internalWriteVOTable(outputfil);
      
   }

   /**
    * @param outputfil
    */
   private void internalWriteVOTable(File outputfil) {
      WriteDocument.generateDocument(voTable, outputfil.getPath());
   }

   /**
    * @param tmpfile
    */
   private void internalAddToVOTable(File tmpfile) {
      SavotResource resource = (SavotResource)voTable.getResources().getItemAt(0);
      TRSet tr = resource.getTRSet(0);
      FieldSet fieldSet = resource.getFieldSet(0);
      SavotField photzfield = new SavotField();
      photzfield.setDescription("photometric redshift estimated by hyperz");
      photzfield.setName("photoz");
      photzfield.setUcd("REDSHIFT_PHOT");
      fieldSet.addItem(photzfield);
      
      try {
          FileReader in = new FileReader(tmpfile);
      int iline  = 0;
      BufferedReader rd = new BufferedReader(in);
      
      String line;
      
         while ((line = rd.readLine())!= null) {
            if(!line.startsWith("#")){
               String[] vals = line.trim().split(" +");
               int j = Integer.parseInt(vals[0]);
               TDSet theTDs = tr.getTDSet(j-1);
               SavotTD td = new SavotTD();
               td.setContent(vals[1]);
               theTDs.addItem(td);
               
            }
         }
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
     
      
      
   }
}
