/*
 * $Id: HyperZVOTableWriter.java,v 1.3 2004/01/26 17:45:50 pah Exp $
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
      File outputfil = new File(outCat.getRealFile().getPath()+".z_phot");
      tmpfile.delete();
      boolean renamesuccess = outputfil.renameTo(tmpfile);
      
      internalAddToVOTable(tmpfile);
      internalWriteVOTable(outCat.getRealFile());
      
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
      resource.setDescription("This is the ouput of running the hyperz from within the ASTROGRID job system as part of the AVO Demo Jan 2004 - Paul Harrison (pah@jb.man.ac.uk)");
      TRSet tr = resource.getTRSet(0);
      FieldSet fieldSet = resource.getFieldSet(0);
      SavotField field = new SavotField();
      field.setDescription("photometric redshift primary solution estimated by hyperz");
      field.setName("photoz");
      field.setUcd("REDSHIFT_PHOT");
      field.setDataType("float");
      fieldSet.addItem(field);
      
      field = new SavotField();
      field.setDescription("chi**2");
      field.setName("chisquared");
      field.setDataType("float");
      fieldSet.addItem(field);
      
      field = new SavotField();
      field.setDescription("p(chi**2)");
      field.setName("pchi2");
      field.setDataType("float");
      field.setUcd("STAT_PROBABILITY");
      fieldSet.addItem(field);

      field = new SavotField();
      field.setDescription("the number of the spectral type");
      field.setName("nspectype");
      field.setDataType("float");
      fieldSet.addItem(field);

      field = new SavotField();
      field.setDescription("the age record");
      field.setName("agerec");
      field.setDataType("float");
      field.setUcd("TIME_AGE");
      fieldSet.addItem(field);

      field = new SavotField();
      field.setDescription("the age in Gyr");
      field.setName("ageGy");
      field.setDataType("float");
      field.setUcd("TIME_AGE");
      fieldSet.addItem(field);
      
      field = new SavotField();
      field.setDescription("the absorption in the V band to ba applied to the fbest fit SED");
      field.setName("absorptionV");
      field.setDataType("float");
      fieldSet.addItem(field);
      
      field = new SavotField();
      field.setDescription("the normalization factor b of equation 1 needed to minimise chi**");
      field.setName("normb");
      field.setDataType("float");
      fieldSet.addItem(field);

      field = new SavotField();
       field.setDescription("the weighted mean redshift zwm");
       field.setName("zwm");
       field.setDataType("float");
       fieldSet.addItem(field);

      field = new SavotField();
       field.setDescription("absolute magnitude");
       field.setName("mag_abs");
       field.setDataType("float");
       field.setUcd("PHOT_ABS-MAG");
       fieldSet.addItem(field);

      field = new SavotField();
       field.setDescription("the secondary peak of the probability function");
       field.setName("z_secondary");
       field.setDataType("float");
       fieldSet.addItem(field);
      field = new SavotField();
       field.setDescription("probability associated with secondary solution");
       field.setName("z_secondary_prob");
       field.setDataType("float");
       fieldSet.addItem(field);
       
       
       
      
     
      
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
               for (int i = 1; i < 9; i++) {
                  td.setContent(vals[i]);
                  theTDs.addItem(td);
                  
               }
               for (int i = 15; i < 20; i++) {
                  td.setContent(vals[i]);
                  theTDs.addItem(td);
                  
               }
             
               
            }
         }
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
     
      
      
   }
}
