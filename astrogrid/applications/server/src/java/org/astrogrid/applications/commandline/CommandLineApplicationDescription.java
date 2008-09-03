/*
 * $Id: CommandLineApplicationDescription.java,v 1.2 2008/09/03 14:18:53 pah Exp $
 *
 * Created on 25 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.MetadataAdapter;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.impl.CeaCmdLineApplicationDefinition;
import org.astrogrid.community.User;

/**
 *  description of a commandline application. The main specialization in thsi class is how to initialize the runnable {@link Application} object from the standard description.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 3 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 * @TODO reduce the dependency propagation in this class.
 */
public class CommandLineApplicationDescription extends AbstractApplicationDescription {
  
    
    private final CeaCmdLineApplicationDefinition clapp;
    private final Configuration conf;

    /** Construct a new CommandLineApplicationDescription
     * @param env
     * @param lib 
     */
    public CommandLineApplicationDescription(Configuration conf, MetadataAdapter ma) {
        super(ma);
        this.clapp = (CeaCmdLineApplicationDefinition) ma.getApplicationBase();
        this.conf = conf;
    }

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommandLineApplicationDescription.class);
    /**
 * @see org.astrogrid.applications.description.base.AbstractApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.applications.ParameterValues)
 */
public Application initializeApplication(String jobStepID, User user,Tool tool) throws CeaException {
    ApplicationInterface appInterface = this.getInterface(tool.getInterface());
    if (appInterface == null) { // go for default then..
        appInterface = this.getInterfaces()[0];
    }
    AbstractApplication app = null;
     String instanceClass = this.getInstanceClass();
     Class clazz = CommandLineApplication.class;
     // if a specialized version of the class is required
     if (instanceClass != null && instanceClass.length() > 0) {
        try {
            clazz = Class.forName(instanceClass);            
        }
        catch (Exception e) { 
          logger.error("Could not find the application specialization class falling back on the default",e);
        } 
     }
    // create a child pico-container, that contains registrations for the correct class, etc.
    try {
	
	Constructor[] cons = clazz.getConstructors();
	assert cons.length == 1 : "there should only be one constructor for CommandLineApplication classes";
	Constructor con = cons[0];
	return (AbstractApplication)con.newInstance(new Object[]{jobStepID, tool, appInterface, new CommandLineApplicationEnvironment(jobStepID, user, 
		getInternalComponentFactory().getIdGenerator(), conf),  getInternalComponentFactory().getProtocolLibrary()});
    } catch (Exception e) {// somethings gone wrong - not the correct deps.
        logger.error("Could not instantiate the application class",e);
        throw new CeaException("Could not instantiate the application class "+clazz.getCanonicalName(),e);
    }
  }

/**
* @return
*/
public String getInstanceClass() {
      return clapp.getInstanceClass();
   }

/**
 * This simply returns the executables path.
* @return
*/
public String getExecutionPath() {
  //TODO - not NPE safe
      return clapp.getExecutionPath().getContent().get(0).toString();
   }

/**
 * Returns the commandline. i.e. executable path along with any fixed parameters.
 * @return
 */
public List<String> getExecutionCmdline()
{
    List<String> retval = new ArrayList<String>();
    for (Iterator iterator = clapp.getExecutionPath().getContent().iterator(); iterator.hasNext();) {
	Object thing = iterator.next();
	if (thing instanceof String) {
	    retval.add((String) thing);
	    
	} else {
	    logger.error("programming error - do not know what to do with " + thing.getClass().getCanonicalName());
	}
	
    }
    ;
    return retval;
}
public void setExecutionPath(String path) {
    clapp.getExecutionPath().getContent().clear();
    clapp.getExecutionPath().getContent().add(path);
}

public void setInstanceClass(String name) {
    clapp.setInstanceClass(name);
}

@Override
public String toString() {
    StringBuffer retval = new StringBuffer(super.toString());
    retval.append("instance class="+ getInstanceClass());
    retval.append('\n');
    retval.append("execution path="+getExecutionPath());
    return retval.toString();
}


}
