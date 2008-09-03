/*
 * $Id: SExtractor.java,v 1.9 2008/09/03 14:19:06 pah Exp $
 *
 * Created on 24 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline.sextractor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.commandline.CommandLineApplication;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.impl.CommandLineParameterDefinition;
import org.astrogrid.applications.commandline.DefaultCommandLineParameterAdapter;
import org.astrogrid.applications.commandline.MergingParameterAdapter;
import org.astrogrid.applications.commandline.MergingParameterAdapter.Concentrator;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;

import java.io.IOException;

public class SExtractor extends CommandLineApplication {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory.getLog(SExtractor.class);


   /*
    * The concentrators for the repeated parameters...
    */
   private final Concentrator imageConcentrator = new MergingParameterAdapter.Concentrator(
         ",");

 
 

   /**
 * @param jobStepId
 * @param t
 * @param interf
 * @param env
 * @param lib
 */
public SExtractor(String jobStepId, Tool t, ApplicationInterface interf,
	ApplicationEnvironment env, ProtocolLibrary lib) {
    super(jobStepId, t, interf, env, lib);
    
}


/* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#preRunHook()
    */
   protected void preRunHook() {
      argvals.add("-CATALOG_TYPE");
      argvals.add("ASCII_HEAD");
   }


    /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#preWritebackHook()
    */
   protected void preWritebackHook() {
      
      // need to convert the output to a VOTable
      
     try {
        //FIXME band is determined by the last character of the catalogue name file AVO Kludge
        String band;
        DefaultCommandLineParameterAdapter catname = (DefaultCommandLineParameterAdapter)findParameterAdapter("CATALOG_NAME");
       // band=catname.getRawValue().substring(catname.getRawValue().length()-1);
       band=((DefaultCommandLineParameterAdapter)findParameterAdapter("IMAGE_BAND")).getWrappedParameter().getValue(); //TODO this is a bit of a cheat will not allow indirect values....     
       ASCII2VOTableConverter conv = new ASCII2VOTableConverter(catname,(DefaultCommandLineParameterAdapter) findParameterAdapter("PARAMETERS_NAME"), applicationEnvironment, band);
      conv.writeVOTable();
   }
   catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
   }     
   }
   
   protected ParameterAdapter instantiateAdapter(ParameterValue pval,
         ParameterDescription desr, ExternalValue indirectVal) {

      if (desr.getId().equals("DetectionImage") || desr.getId().equals("PhotoImage")) {
         if (logger.isDebugEnabled()) {
            logger.debug("creating merging adapter for image parameters");
         }

         return new MergingParameterAdapter(getApplicationInterface(), pval,
               (CommandLineParameterDefinition)desr, indirectVal,
               applicationEnvironment, imageConcentrator);
      }
     else {
         return super.instantiateAdapter(pval, desr, indirectVal);
      }
   }
   

}
