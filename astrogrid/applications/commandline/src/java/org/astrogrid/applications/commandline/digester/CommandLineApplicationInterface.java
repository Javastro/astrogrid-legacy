/*
 * $Id: CommandLineApplicationInterface.java,v 1.2 2004/10/08 19:56:50 pah Exp $
 * 
 * Created on 27-Aug-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.digester;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;

/**
 * Utility class to make it easier to create interfaces from digester.
 * @author Paul Harrison (pah@jb.man.ac.uk) 27-Aug-2004
 * @version $Name:  $
 * @since iteration6
 */
class CommandLineApplicationInterface extends BaseApplicationInterface {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(CommandLineApplicationInterface.class);

    /**
     * @param name
     * @param description
     */
    public CommandLineApplicationInterface(String name,
            ApplicationDescription description) {
        super(name, description);

      if (logger.isDebugEnabled()) {
         logger
               .debug("CommandLineApplicationInterface(ApplicationDescription description = "
                     + description + ") - creating new interface description");
      }
 
    }
    
    public void addInputParameterAsPref(ParameterRef pref) throws ParameterDescriptionNotFoundException
    {
          getApplicationDescription().getParameterDescription(pref.getRef());
          addInputParameter(pref.getRef(), pref.getMinoccurs(), pref.getMaxoccurs());
    }
    public void addOutputParameterAsPref(ParameterRef pref) throws ParameterDescriptionNotFoundException
    {
        getApplicationDescription().getParameterDescription(pref.getRef());
        addOutputParameter(pref.getRef(), pref.getMinoccurs(), pref.getMaxoccurs());
        
    }
}
