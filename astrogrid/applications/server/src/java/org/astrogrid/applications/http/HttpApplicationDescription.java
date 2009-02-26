/*$Id: HttpApplicationDescription.java,v 1.2 2009/02/26 12:45:56 pah Exp $
 * Created on Jul 24, 2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.applications.http;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.AbstractApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.MetadataAdapter;
import org.astrogrid.applications.description.base.InterfaceDefinition;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.security.SecurityGuard;

/**
 * application description for an HttpApplication
 * 
 * @author jdt
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 31 Mar 2008
 *  
 */
public class HttpApplicationDescription extends AbstractApplicationDescription {

    Log log = LogFactory.getLog(HttpApplicationDescription.class);

    /**
     * Constructor
     */
    public HttpApplicationDescription(final Configuration conf, final MetadataAdapter ma) {
        super(ma, conf);
  
       this.application = (CeaHttpApplicationDefinition) ma.getApplicationBase();//IMPL this might not be appropriate....
       if (log.isTraceEnabled()) {
            log.trace("HttpApplicationDescription(WebHttpApplication, String, ApplicationDescriptionEnvironment) - end");
        }
    }

    
    
    /** derived version of the application
     */
    protected final CeaHttpApplicationDefinition application;
    private List<InterfaceDefinition> interfaces;

    /**
     * @see org.astrogrid.applications.description.ApplicationDefinition#initializeApplication(java.lang.String,
     *      SecurityGuard, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String jobStepID, SecurityGuard secGuard, Tool tool) throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("initializeApplication(String jobStepID = " + jobStepID + ", User user = " + secGuard
                    + ", Tool tool = " + tool + ") - start");
        }

        
        ApplicationInterface appInterface = this.getInterface(tool.getInterface());
        if (appInterface == null) { // go for default then..
            appInterface = this.getInterfaces()[0];
            log.debug("Interface not found - using default");
        }
        log.debug("Using interface "+appInterface);
        
        ApplicationEnvironment appenv = new ApplicationEnvironment(jobStepID, secGuard, getInternalComponentFactory().getIdGenerator(), conf );
	Application returnApplication = new HttpApplication(jobStepID, tool, appInterface, appenv , getInternalComponentFactory().getProtocolLibrary(), application.getCeaHttpAdapterSetup()  );
        
        
        if (log.isTraceEnabled()) {
            log.trace("initializeApplication(String, User, Tool) - end - return value = " + returnApplication);
        }
        return returnApplication;
    }	

    /**
     * Return the URL of this Webapp.
     * 
     * @return
     */
    public String getUrl() {
        return application.getCeaHttpAdapterSetup().getURL().getValue();
    }
}

/*
 * $Log: HttpApplicationDescription.java,v $
 * Revision 1.2  2009/02/26 12:45:56  pah
 * separate more out into cea-common for both client and server
 *
 * Revision 1.1  2008/09/10 23:27:17  pah
 * moved all of http CEC and most of javaclass CEC code here into common library
 *
 * Revision 1.8  2008/09/03 14:19:02  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.7.86.3  2008/09/03 12:00:49  pah
 * still not really working
 *
 * Revision 1.7.86.2  2008/08/02 13:32:32  pah
 * safety checkin - on vacation
 *
 * Revision 1.7.86.1  2008/04/01 13:50:07  pah
 * http service also passes unit tests with new jaxb metadata config
 *
 * Revision 1.7  2005/07/05 08:27:01  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.6.66.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.6.52.2  2005/06/08 22:10:46  pah
 * make http applications v10 compliant
 *
 * Revision 1.6.52.1  2005/05/31 12:51:43  pah
 * moved to v10 schema interpretation - this means that the authorityID is read directly with the applicaion "name"
 *
 * Revision 1.6  2004/12/18 15:43:57  jdt
 * merge from  cea_pah_561b
 *
 * Revision 1.5.48.1  2004/12/07 07:32:27  pah
 * update to pass band information properly
 *
 * Revision 1.5  2004/09/01 15:42:26  jdt
 * Merged in Case 3
 *
 * Revision 1.1.4.11  2004/08/11 22:55:35  jdt
 * Refactoring, and a lot of new unit tests.
 *
 * Revision 1.1.4.10  2004/08/10 13:44:23  jdt
 * Following new workflow-object types...and a safety checkin.
 *
 * Revision 1.1.4.9  2004/08/09 16:37:13  jdt
 * Brought into line following pah's suggested schema changes
 * Revision 1.1.4.8 2004/08/06
 * 13:30:22 jdt safety checkin
 * 
 * Revision 1.1.4.7 2004/08/02 18:05:28 jdt Added more tests and refactored the
 * test apps to be set up from xml.
 * 
 * Revision 1.1.4.6 2004/07/30 16:59:50 jdt limping along.
 * 
 * Revision 1.1.4.5 2004/07/30 11:02:30 jdt Added unit tests, refactored the
 * RegistryQuerier anf finished off HttpApplicationCEAComponentManager.
 * 
 * Revision 1.1.4.4 2004/07/29 21:30:47 jdt ** empty log message ***
 * 
 * Revision 1.1.4.3 2004/07/29 17:08:22 jdt Think about how I'm going to get
 * stuff out of the registry
 * 
 * Revision 1.1.4.2 2004/07/29 16:35:21 jdt Safety checkin, while I think about
 * what happens next.
 * 
 * Revision 1.1.4.1 2004/07/27 17:20:25 jdt merged from applications_JDT_case3
 * 
 * Revision 1.1.2.1 2004/07/24 17:16:16 jdt Borrowed from javaclass
 * application....stealing is always quicker.
 * 
 * 
 *  
 */