/*
 * $Id: Dft.java,v 1.7 2004/10/05 16:04:45 pah Exp $
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

package org.astrogrid.applications.commandline.dft;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.commandline.CommandLineApplication;
import org.astrogrid.applications.commandline.CommandLineApplicationEnvironment;
import org.astrogrid.applications.commandline.CommandLineParameterDescription;
import org.astrogrid.applications.commandline.DefaultCommandLineParameterAdapter;
import org.astrogrid.applications.commandline.MergingParameterAdapter;
import org.astrogrid.applications.commandline.MergingParameterAdapter.Concentrator;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.workflow.beans.v1.Tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 * @TODO should not really need this class - better to build adding standard
 *       parameters to the interface definition so that the standard commandline
 *       application could deal with it
 */
public class Dft extends CommandLineApplication {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory.getLog(Dft.class);

   /*
    * The concentrators for the repeated parameters...
    */
   private final Concentrator matchConcentrator = new MergingParameterAdapter.Concentrator(
         ",");

   private final Concentrator targetConcentrator = new MergingParameterAdapter.Concentrator(
         ",");

   /**
    * Construct a new Dft
    * 
    * @param id
    * @param jobStepId
    * @param user
    * @param description
    */
   public Dft(String id, String jobStepId, Tool tool,
         ApplicationInterface interf, CommandLineApplicationEnvironment env,
         ProtocolLibrary lib) {
      super(jobStepId, tool, interf, env, lib);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.astrogrid.applications.commandline.CmdLineApplication#preRunHook()
    */
   protected void preRunHook() {
      argvals.add("output=VOTable");
      argvals.add("maxMatches=1");

   }

   protected ParameterAdapter instantiateAdapter(ParameterValue pval,
         ParameterDescription desr, ExternalValue indirectVal) {

      if (desr.getName().equals("matches")) {
         if (logger.isDebugEnabled()) {
            logger.debug("creating merging adapter for parameter matches");
         }

         return new MergingParameterAdapter(getApplicationInterface(), pval,
               (CommandLineParameterDescription)desr, indirectVal,
               applicationEnvironment, matchConcentrator);
      }
      else if (desr.getName().equals("targets"))
      {
         if (logger.isDebugEnabled()) {
            logger.debug("creating merging adapter for parameter targets");
         }

         return new MergingParameterAdapter(getApplicationInterface(), pval,
               (CommandLineParameterDescription)desr, indirectVal,
               applicationEnvironment, targetConcentrator);
        
      }
      else {
         return super.instantiateAdapter(pval, desr, indirectVal);
      }
   }
}