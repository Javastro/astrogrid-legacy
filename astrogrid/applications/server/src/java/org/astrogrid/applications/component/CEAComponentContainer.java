/*
 * $Id: CEAComponentContainer.java,v 1.6 2011/09/02 21:55:52 pah Exp $
 * 
 * Created on 2 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.registry.RegistryUploader;
import org.astrogrid.applications.manager.ControlService;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * A class that acts a a container for all of the components that might be needed for functionality of the CEC - eg. used in jsp pages etc.
 * 
 * It is implemented by using {@link http://www.springframework.org} and assume that this component will always be produced in at least an ListableBeanFactory - it is a programming error if it is not.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 4 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public final class CEAComponentContainer extends AbstractCEAComponentContainer implements CEAComponents, BeanFactoryAware {

   // would be nice if this were autowireable - using the BeanFactoryAware interface for now
    private static ListableBeanFactory appCtx;
    
    public CEAComponentContainer(ControlService controlService, ExecutionController executionController,
	    MetadataService metadataService, QueryService queryService,
	    RegistryUploader registryUploaderService, ExecutionHistory executionHistoryService,
	    ApplicationDescriptionLibrary applicationDescriptionLibrary) {
	this.controlService = controlService;
	this.executionController = executionController;
	this.metadataService = metadataService;
	this.queryService = queryService;
	this.registryUploaderService = registryUploaderService;
	this.executionHistoryService = executionHistoryService;
	this.applicationDescriptionLibrary = applicationDescriptionLibrary;
    }
 
    /**
     * Retrieve an instance of itself from the underlying container technology.
     * @return
     */
    public static CEAComponentContainer getInstance()
    {
	CEAComponentContainer retval = null;
	if(appCtx != null)
	{
	   retval = (CEAComponentContainer) appCtx.getBean("ComponentManager");
	   
	}
	return retval;
    }
    /**
     * @param controlService the controlService to set
     */
    public void setControlService(ControlService controlService) {
        this.controlService = controlService;
    }

    /**
     * @param executionController the executionController to set
     */
    public void setExecutionController(ExecutionController executionController) {
        this.executionController = executionController;
    }

    /**
     * @param metadataService the metadataService to set
     */
    public void setMetadataService(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    /**
     * @param queryService the queryService to set
     */
    public void setQueryService(QueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * @param registryUploaderService the registryUploaderService to set
     */
    public void setRegistryUploaderService(RegistryUploader registryUploaderService) {
        this.registryUploaderService = registryUploaderService;
    }

    //TODO the following might not be behaving themselves completely - not sure if Spring will pick up the types that implement an interface properly - the pico version of this requested all objects then used instanceof
   //TODO check on nested beans too see warnings on Spring app context methods.
    public String informationHTML() {
	    
        StringWriter sw = new StringWriter();
        PrintWriter p = new PrintWriter(sw);
        p.println("<h1>Component Information</h1>");
         String[] beans = appCtx.getBeanDefinitionNames();
         for (int i = 0; i < beans.length; i++) {
           Object o = appCtx.getBean(beans[i]);
            if (o instanceof ComponentDescriptor) {
                ComponentDescriptor descr = (ComponentDescriptor)o;
                p.println("<b>" + descr.getName() + "</b> ");
                p.println("<blockquote><pre>" + descr.getDescription() + "</pre></blockquote>");                
            } else {
                p.println(beans[i] + ":" + o.getClass().getName() + ":" + o.toString());
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
        String[] beans = appCtx.getBeanDefinitionNames();
        for (int i = 0; i < beans.length; i++) {
          Object o = appCtx.getBean(beans[i]);
            if (o instanceof ComponentDescriptor) {
                ComponentDescriptor descr = (ComponentDescriptor)o;
                p.println(descr.getName());
                p.println(descr.getDescription());              
            } else { 
                p.println(beans[i] + ":" + o.getClass().getName() + ":" + o.toString());
            }
            p.println();
        }
        return sw.toString();
}
public TestSuite getSuite() {         
  TestSuite result = new TestSuite("Installation Tests");
  Map beans = appCtx.getBeansOfType(ComponentDescriptor.class, true, false);
  for (Iterator i = beans.values().iterator(); i.hasNext(); ) {
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

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
	// assume that this component will always be produced in at least an ApplicationContext - it is a programming error if it is not
	CEAComponentContainer.appCtx =  (ListableBeanFactory) beanFactory;
    }

    public void setExecutionHistoryService(ExecutionHistory executionHistoryService) {
        this.executionHistoryService = executionHistoryService;
    }

    public void setApplicationDescriptionLibrary(
    	ApplicationDescriptionLibrary applicationDescriptionLibrary) {
        this.applicationDescriptionLibrary = applicationDescriptionLibrary;
    }

}


/*
 * $Log: CEAComponentContainer.java,v $
 * Revision 1.6  2011/09/02 21:55:52  pah
 * result of merging the 2931 branch
 *
 * Revision 1.5.6.1  2009/07/15 09:47:11  pah
 * redesign of parameterAdapters
 *
 * Revision 1.5  2008/09/18 09:13:39  pah
 * improved javadoc
 *
 * Revision 1.4  2008/09/05 08:02:09  pah
 * correct refactoring
 *
 * Revision 1.3  2008/09/05 07:55:45  pah
 * extract out a component container base class
 *
 * Revision 1.2  2008/09/03 14:18:57  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.3  2008/04/17 16:08:32  pah
 * removed all castor marshalling - even in the web service layer - unit tests passing
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 * ASSIGNED - bug 2739: remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 *
 * Revision 1.1.2.2  2008/04/08 14:45:10  pah
 * Completed move to using spring as container for webapp - replaced picocontainer
 *
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 *
 * Revision 1.1.2.1  2008/04/04 15:46:07  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
