/*
 * $Id: CommandLineApplicationDescription.java,v 1.3 2005/03/13 07:13:39 clq2 Exp $
 *
 * Created on 25 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.defaults.DefaultPicoContainer;

public class CommandLineApplicationDescription extends AbstractApplicationDescription {
  
    
    /** Construct a new CommandLineApplicationDescription
     * @param arg0
     */
    public CommandLineApplicationDescription(ApplicationDescriptionEnvironment arg0,PicoContainer pico) {
        super(arg0);
        this.pico = pico;
    }

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommandLineApplicationDescription.class);
    private final PicoContainer pico;
    private String executionPath;
    /**
     * The class that is used to instantiate this particular application. 
     */
    private String instanceClass;
   
/**
 * @see org.astrogrid.applications.description.base.AbstractApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.applications.ParameterValues)
 */
public Application initializeApplication(String jobStepID, User user,Tool tool) throws CeaException {
    ApplicationInterface appInterface = this.getInterface(tool.getInterface());
    if (appInterface == null) { // go for default then..
        appInterface = this.getInterfaces()[0];
    }
    CommandLineApplication app = null;
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
    MutablePicoContainer child = new DefaultPicoContainer(pico);    
    child.registerComponentInstance(tool);
    child.registerComponentInstance(user);
    child.registerComponentInstance(appInterface);
    child.registerComponentInstance(jobStepID);
    try {
        return (CommandLineApplication)child.registerComponentImplementation(CommandLineApplication.class,clazz).getComponentInstance(child);
    } catch (PicoException e) {// somethings gone wrong - not the correct deps.
        logger.error("Could not instantiate the application class",e);
        throw new CeaException("Could not instantiate the application class",e);
    }
  }

/**
* @return
*/
public String getInstanceClass() {
      return instanceClass;
   }

/**
* @param string
*/
public void setInstanceClass(String string) {
      instanceClass = string;
   }

/**
* @return
*/
public String getExecutionPath() {
      return executionPath;
   }

/**
* @param string
*/
public void setExecutionPath(String string) {
      executionPath = string;
   }
  


}
