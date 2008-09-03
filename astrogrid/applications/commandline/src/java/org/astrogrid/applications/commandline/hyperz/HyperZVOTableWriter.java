/*
 * $Id: HyperZVOTableWriter.java,v 1.9 2008/09/03 14:18:54 pah Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.CeaException;

import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.impl.CommandLineParameterDefinition;
import org.astrogrid.applications.commandline.DefaultCommandLineParameterAdapter;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.protocol.ExternalValue;

import cds.savot.model.FieldSet;
import cds.savot.model.SavotField;
import cds.savot.model.SavotResource;
import cds.savot.model.SavotTD;
import cds.savot.model.SavotTable;
import cds.savot.model.SavotVOTable;
import cds.savot.model.TDSet;
import cds.savot.model.TRSet;
import cds.savot.model.TableSet;
import cds.savot.writer.SavotWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Writes out a votable by appending the results to the end of each row of the input VOTable.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class HyperZVOTableWriter extends DefaultCommandLineParameterAdapter {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(HyperZVOTableWriter.class);

    // define an interface here, rather than reference another component - as we need to allow late bindng
    public interface VOTableSource {
  
        public SavotVOTable getVOTable() ;
    }
 
   /** Construct a new HyperZVOTableWriter
     * @param val
     * @param descr
     */
    public HyperZVOTableWriter(ApplicationInterface interf, ParameterValue val, CommandLineParameterDefinition descr,ApplicationEnvironment env, ExternalValue ival, VOTableSource votableSource) {
        super(interf,val, descr,ival,env);
        logger.debug("creating hyperz table writer");
        this.votableSource = votableSource;
        tmpfile = env.getTempFile(descr.getId());
    }
    private final File tmpfile;
   private final VOTableSource votableSource;


   /**
    * @see org.astrogrid.applications.ParameterAdapter#writeBack()
    */
   public void writeBack(Object o) throws CeaException {
    if (logger.isDebugEnabled()) {
        logger.debug("hyperz writeBack() - start");
    }
      File outputfil = new File(getReferenceFile().getPath()+".z_phot");
      SavotVOTable votable = votableSource.getVOTable();      
      internalAddToVOTable(votable,outputfil); //add the copies of the original file
      internalWriteVOTable(votable,getReferenceFile());
      super.writeBack(o);
    if (logger.isDebugEnabled()) {
        logger.debug("writeBack() - end");
    }
   }

   /**
    * @param outputfil
    */
   private void internalWriteVOTable(SavotVOTable votable,File outputfil) {
    if (logger.isDebugEnabled()) {
        logger.debug("internalWriteVOTable(SavotVOTable, File) - start");
    }

     new SavotWriter().generateDocument(votable, outputfil.getPath());

    if (logger.isDebugEnabled()) {
        logger.debug("internalWriteVOTable(SavotVOTable, File) - end");
    }
   }

   /**
    * @param tmpfile
    */
   private void internalAddToVOTable(SavotVOTable voTable,File tmpfile) {
    if (logger.isDebugEnabled()) {
        logger.debug("internalAddToVOTable(SavotVOTable, File-"+tmpfile+") - start");
    }

      SavotResource resource = (SavotResource)voTable.getResources().getItemAt(0);
      //set some names and descriptions
      resource.setDescription("This is the ouput of running the hyperz from within the ASTROGRID job system as part of the AVO Demo Jan 2004 - Paul Harrison (pah@jb.man.ac.uk)");
      TableSet tableSet = resource.getTables();
      SavotTable savotTable = (SavotTable)tableSet.getItemAt(0);
      savotTable.setName(getWrappedParameter().getValue());
      
      TRSet tr = resource.getTRSet(0);
      
      //add the new fields
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
      field.setDescription("the normalization factor b of equation 1 needed to minimise chi**2");
      field.setName("normb");
      field.setDataType("float");
      fieldSet.addItem(field);

      field = new SavotField();
       field.setDescription("the weighted mean redshift zwm");
       field.setName("zwm");
       field.setDataType("float");
       fieldSet.addItem(field);

      field = new SavotField();
       field.setDescription("probability associated with the weighted mean redshift zwm");
       field.setName("zwm_prob");
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
               for (int i = 1; i < 9; i++) {
                  SavotTD td = new SavotTD();
                  td.setContent(vals[i]);
                  theTDs.addItem(td);
                  
               }
               for (int i = 15; i < 20; i++) {
                  SavotTD td = new SavotTD();
                  td.setContent(vals[i]);
                  theTDs.addItem(td);
                  
               }
             
               
            }
         }
      }
      catch (IOException e) {
        logger.error("internalAddToVOTable(SavotVOTable, File)", e);

         // TODO Auto-generated catch block
         e.printStackTrace();
      }
     
      
      
    if (logger.isDebugEnabled()) {
        logger.debug("internalAddToVOTable(SavotVOTable, File) - end");
    }
   }


}
