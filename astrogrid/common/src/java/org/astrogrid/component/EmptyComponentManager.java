/*$Id: EmptyComponentManager.java,v 1.2 2005/03/13 07:13:39 clq2 Exp $
 * Created on 04-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.component;

import org.astrogrid.component.descriptor.ComponentDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 04-May-2004
 *
 */
public class EmptyComponentManager implements ComponentManager{

    protected static final Log log = LogFactory.getLog(EmptyComponentManager.class);     
    /** Construct a new EmptyComponentManager
     * 
     */
    public EmptyComponentManager() {
        pico = new DefaultPicoContainer();
    }
    /** the picocontainer that manages the components */
    protected MutablePicoContainer pico;
    public String informationHTML() {
    
              StringWriter sw = new StringWriter();
              PrintWriter p = new PrintWriter(sw);
              p.println("<h1>Component Information</h1>");
              for (Iterator i = getContainer().getComponentInstances().iterator(); i.hasNext(); ) {
                  Object o = i.next();
                  if (o instanceof ComponentDescriptor) {
                      ComponentDescriptor descr = (ComponentDescriptor)o;
                      p.println("<b>" + descr.getName() + "</b> ");
                      p.println("<blockquote><pre>" + descr.getDescription() + "</pre></blockquote>");                
                  } else {
                      p.println(o.getClass().getName() + ":" + o.toString());
                  }
                  p.println("<hr/>");
              }
              return sw.toString();
      }
    public String information() {
    
              StringWriter sw = new StringWriter();
              PrintWriter p = new PrintWriter(sw);
              p.println("Component Information");
              p.println("-------------------------------");
              for (Iterator i = getContainer().getComponentInstances().iterator(); i.hasNext(); ) {
                  Object o = i.next();
                  if (o instanceof ComponentDescriptor) {
                      ComponentDescriptor descr = (ComponentDescriptor)o;
                      p.println(descr.getName());
                      p.println(descr.getDescription());              
                  } else { 
                      p.println(o.getClass().getName() + ":" + o.toString());
                  }
                  p.println();
              }
              return sw.toString();
      }
    public Test getSuite() {
    
               
        TestSuite result = new TestSuite("Installation Tests");
        for (Iterator i = getContainer().getComponentInstances().iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof ComponentDescriptor) {
                ComponentDescriptor descr  = (ComponentDescriptor)o;
                Test test = descr.getInstallationTest();
                if (test != null) {
                    result.addTest(test);
                }
            }
        }
        return result;
    }
    /**
         * @see org.astrogrid.jes.component.ComponentManager#getContainer()
         */
    public MutablePicoContainer getContainer() {
        return pico;
    }
    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        log.info("Starting component manager");
        pico.start();
    }
    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        log.info("Stopping component manager");
        pico.stop();
    } 
}


/* 
$Log: EmptyComponentManager.java,v $
Revision 1.2  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.1.136.1  2005/03/11 13:24:38  nw
changed to allow picocontainer to be set later.

Revision 1.1  2004/05/04 11:00:12  nw
moved pico-container component stuff from jes into common, so it can be used in cea too
 
*/