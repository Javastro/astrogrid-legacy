/*
 * $Id: SExtractor.java,v 1.4 2004/08/28 07:17:34 pah Exp $
 *
 * Created on 24 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline.sextractor;

import org.astrogrid.applications.commandline.CommandLineApplication;
import org.astrogrid.applications.commandline.CommandLineApplicationEnvironment;
import org.astrogrid.applications.commandline.CommandLineParameterAdapter;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.IOException;

public class SExtractor extends CommandLineApplication {


 
   /** Construct a new SExtractor
     * @param id
     * @param jobStepId
     * @param user
     * @param description
     */
    public SExtractor(String id, String jobStepId, User user, Tool tool, ApplicationInterface description, CommandLineApplicationEnvironment env,ProtocolLibrary lib) {
        super(jobStepId,  tool,description, env,lib);
    }
 

   /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#preRunHook()
    */
   protected void preRunHook() {
      argvals.add("-CATALOG_TYPE");
      argvals.add("ASCII_HEAD");
      // FIXME temp kludge to work for AVO demo
      argvals.add("-FILTER_NAME");
      argvals.add("/home/applications/demo/h_goods_r1.0z_detect_conv.txt");
      argvals.add("-CHECKIMAGE_TYPE");
      argvals.add("NONE");
      argvals.add("-WEIGHT_TYPE");
      argvals.add("NONE");
      argvals.add("-DETECT_THRESH");
      argvals.add("4.0");
      argvals.add("-ANALYSIS_THRESH");
      argvals.add("4.0");
   }


    /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#preWritebackHook()
    */
   protected void preWritebackHook() {
      
      // need to convert the output to a VOTable
      
     try {
        //FIXME band is determined by the last character of the catalogue name file AVO Kludge
        String band;
        CommandLineParameterAdapter catname = (CommandLineParameterAdapter)findParameterAdapter("CATALOG_NAME");
       // band=catname.getRawValue().substring(catname.getRawValue().length()-1);
       band=catname.getWrappedParameter().getValue().substring(catname.getWrappedParameter().getValue().length()-1);       
       ASCII2VOTableConverter conv = new ASCII2VOTableConverter(catname,(CommandLineParameterAdapter) findParameterAdapter("PARAMETERS_NAME"), applicationEnvironment, band);
      conv.writeVOTable();
   }
   catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
   }     
   }
   
    

}
