/*$Id: CommandLineParameterAdapterFactory.java,v 1.2 2004/07/01 11:07:59 nw Exp $
 * Created on 18-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.commandline;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.DefaultParameterAdapterFactory;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.indirect.IndirectParameterValue;
import org.astrogrid.applications.parameter.indirect.IndirectionProtocolLibrary;
 /** factory for creating parameter adapters.
  * depending on the calling convention for that parameter, will either return a InlineCommandLineParameterAdapter, or a ReferenceCommandLineParameterAdapter.
  * @author Noel Winstanley nw@jb.man.ac.uk 18-Jun-2004
  *
  */

public class CommandLineParameterAdapterFactory extends DefaultParameterAdapterFactory {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommandLineParameterAdapterFactory.class);

    public CommandLineParameterAdapterFactory(IndirectionProtocolLibrary arg0,CommandLineApplicationEnvironment env,ApplicationInterface appInterface) {
        super(arg0);
        this.env = env;
        this.appInterface = appInterface;
    }
    protected final ApplicationInterface appInterface;
    protected final CommandLineApplicationEnvironment env;
    protected ParameterAdapter instantiateAdapter( ParameterValue pval, ParameterDescription desr, IndirectParameterValue indirectVal) {                
        CommandLineParameterDescription clpd = (CommandLineParameterDescription)desr;
         if (!clpd.isFile()) {
             logger.debug("treating " + pval.getName() + " as inline parameter");
          return new InlineCommandLineParameterAdapter(pval, (CommandLineParameterDescription)desr, indirectVal);
         } else {
             logger.debug("treating " + pval.getName() +" as reference parameter");
             return new ReferenceCommandLineParameterAdapter(appInterface,pval, (CommandLineParameterDescription)desr,indirectVal,env);
         }
      }
}

/* 
$Log: CommandLineParameterAdapterFactory.java,v $
Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:43:39  nw
final version, before merge
 
*/