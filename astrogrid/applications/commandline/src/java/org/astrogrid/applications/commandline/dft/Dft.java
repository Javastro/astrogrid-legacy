/*
 * $Id: Dft.java,v 1.4 2004/07/26 12:03:33 nw Exp $
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

import org.astrogrid.applications.commandline.CommandLineApplication;
import org.astrogrid.applications.commandline.CommandLineApplicationEnvironment;
import org.astrogrid.applications.commandline.ReferenceCommandLineParameterAdapter;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.workflow.beans.v1.Tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 * @TODO should not really need this class - better to build adding standard parameters to the interface definition so that the standard commandline application could deal with it
 * @todo doubt this works in its current form. needs to be retested.
 * @todo can't do it this way anymore - have tightened up the adapter interface. rewrite needed.
 */
public class Dft extends CommandLineApplication {



   /** Construct a new Dft
     * @param id
     * @param jobStepId
     * @param user
     * @param description
     */
    public Dft(String id, String jobStepId, Tool tool,ApplicationInterface interf, CommandLineApplicationEnvironment env, ProtocolLibrary lib) {
        super(jobStepId, tool,interf,env,lib);
    }

    /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#preRunHook()
    */
   protected void preRunHook() {
      argvals.add("output=VOTable");
      argvals.add("maxMatches=1");

   }

   /**
    * @TODO This is a quick and dirty manipulation of the parameters to get things running for the AVO demo - should think about the best general way to do this.
    * @todo rewrite using custom adapters - this method isn't possible any more.
    * @see org.astrogrid.applications.commandline.CmdLineApplication#postParamSetupHook()
    */
   protected void postParamSetupHook() {
      //get the output file to the front...
      List newparams = new ArrayList();

    ParameterAdapter outputParam = findParameterAdapter("merged_output");
    // not possible.
    //outputAdapters.remove(outputParam);
    newparams.add(outputParam);
 //
      ReferenceCommandLineParameterAdapter mergeparam = null;
      StringBuffer sb = new StringBuffer();
 
      for (Iterator iter = inputParameterAdapters(); iter.hasNext();) {
         ParameterAdapter element = (ParameterAdapter)iter.next();
         if (element.getWrappedParameter().getName().equals("matches")) {
            if (mergeparam == null) {
               mergeparam = (ReferenceCommandLineParameterAdapter)element;
               sb.append(mergeparam.getReferenceFile().getPath());               
            }
            else
            {
               sb.append(" ");
               sb.append(((ReferenceCommandLineParameterAdapter)element).getReferenceFile().getPath());
            }

         }
         else {
            newparams.add(element);

         }
      }
      mergeparam.getWrappedParameter().setValue("matches="+sb.toString());
      newparams.add(mergeparam);
      // not possible.
      //inputAdapters.clear();
      //inputAdapters.addAll(newparams);

   }


}
