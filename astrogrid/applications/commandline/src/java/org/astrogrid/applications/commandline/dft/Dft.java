/*
 * $Id: Dft.java,v 1.10 2011/09/02 21:55:52 pah Exp $
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

import org.astrogrid.applications.commandline.CommandLineApplication;
import org.astrogrid.applications.commandline.DefaultCommandLineParameterAdapter;
import org.astrogrid.applications.commandline.MergingParameterAdapter;
import org.astrogrid.applications.commandline.MergingParameterAdapter.Concentrator;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.impl.CommandLineParameterDefinition;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterDirection;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

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
         " ");

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
   public Dft(String jobStepId, Tool tool,
         ApplicationInterface interf, ApplicationEnvironment env,
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
         ParameterDescription desr, ParameterDirection dir, ExternalValue indirectVal) {

      if (desr.getId().equals("matches")) {
         if (logger.isDebugEnabled()) {
            logger.debug("creating merging adapter for parameter matches");
         }

         return new MergingParameterAdapter(getApplicationInterface(), pval,
               (CommandLineParameterDefinition)desr, dir, applicationEnvironment, matchConcentrator);
      }
      else if (desr.getId().equals("targets")) //FIXME - I dont think that this is supposed to be a multiple
      {
         if (logger.isDebugEnabled()) {
            logger.debug("creating merging adapter for parameter targets");
         }

         return new MergingParameterAdapter(getApplicationInterface(), pval,
               (CommandLineParameterDefinition)desr, dir, applicationEnvironment, targetConcentrator);
        
      }
      else {
         return super.instantiateAdapter(pval, desr, dir, indirectVal);
      }
   }
}