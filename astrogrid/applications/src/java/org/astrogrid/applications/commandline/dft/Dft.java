/*
 * $Id: Dft.java,v 1.3 2004/01/25 12:26:52 pah Exp $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.FileReferenceParameter;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.community.User;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 * @TODO should not really need this class - better to build adding standard parameters to the interface definition so that the standard commandline application could deal with it
 */
public class Dft extends CmdLineApplication {

   /**
    * 
    */
   public Dft() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param controller
    * @param user
    */
   public Dft(AbstractApplicationController controller, User user) {
      super(controller, user);
      // TODO Auto-generated constructor stub
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
    * @see org.astrogrid.applications.commandline.CmdLineApplication#postParamSetupHook()
    */
   protected void postParamSetupHook() {
      //get the output file to the front...
      List newparams = new ArrayList();
      Parameter outputparam = findParameter("merged_output");
      parameters.remove(outputparam);
      newparams.add(outputparam);

      FileReferenceParameter mergeparam = null;
      StringBuffer sb = new StringBuffer();
 
      for (Iterator iter = parameters.iterator(); iter.hasNext();) {
         Parameter element = (Parameter)iter.next();
         if (element.getName().equals("matches")) {

            if (mergeparam == null) {
               mergeparam = (FileReferenceParameter)element;
               sb.append(mergeparam.getRealFile().getPath());
               

            }
            else
            {
               sb.append(" ");
               sb.append(((FileReferenceParameter)element).getRealFile().getPath());
            }

         }
         else {
            newparams.add(element);

         }
      }
      List lval = new ArrayList();
      lval.add("matches="+sb.toString());
      mergeparam.setArgValue(lval);
      newparams.add(mergeparam);
      parameters = newparams;
      //merge the

   
   }

}
