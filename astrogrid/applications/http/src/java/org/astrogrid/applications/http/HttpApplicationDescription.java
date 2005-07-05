/*$Id: HttpApplicationDescription.java,v 1.7 2005/07/05 08:27:01 clq2 Exp $
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

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.base.BaseParameterDescription;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.registry.IvornUtil;
import org.astrogrid.community.User;
import org.astrogrid.registry.beans.v10.cea.ApplicationDefinition;
import org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * application description for an HttpApplication
 * 
 * @author jdt
 *  
 */
public class HttpApplicationDescription extends AbstractApplicationDescription {

    Log log = LogFactory.getLog(HttpApplicationDescription.class);

    /**
     * Constructor
     */
    public HttpApplicationDescription(CeaHttpApplicationType application, 
            final ApplicationDescriptionEnvironment env) {
        super(env);
        if (log.isTraceEnabled()) {
            log.trace("HttpApplicationDescription(CeaHttpApplicationType application = " + application
                    +  ", ApplicationDescriptionEnvironment env = " + env
                    + ") - start");
        }

        this.application = application;
        createMetadata();

        if (log.isTraceEnabled()) {
            log.trace("HttpApplicationDescription(WebHttpApplication, String, ApplicationDescriptionEnvironment) - end");
        }
    }

    protected final CeaHttpApplicationType application;

    /**
     * populates this object with parameterDescriptions by reflecting on
     * application method
     * 
     * @TODO - currently assumes a single interface....change this
     */
    protected final void createMetadata() {
       //remove the ivo:// for now...
       //TODO ivo:// should be the prefix on all of the names - has implications on the way that jes etc call this though...
        setName(IvornUtil.removeProtocol(application.getIdentifier()));
        log.debug("Name: " + getName());

        final ApplicationDefinition applicationDefinition = application.getApplicationDefinition();

        InterfacesType interfaces = applicationDefinition.getInterfaces();
        log.debug("Found " + interfaces.get_interfaceCount() + " interfaces for this application");

        Enumeration enumerator = interfaces.enumerate_interface();
        while (enumerator.hasMoreElements()) {
            Interface oneInterface = (Interface) enumerator.nextElement();

            BaseApplicationInterface singleInterface = new BaseApplicationInterface(oneInterface.getName(), this);
            
            //For each interface set up inputs
            ParameterRef[] inputs = oneInterface.getInput().getPref();
            class ConstructableBaseParameterDescription extends BaseParameterDescription {
                 public ConstructableBaseParameterDescription(ParameterRef param) {
                  setName(param.getRef());
                  //  They'll all be Strings for http apps, but this should really be done properly
                  setType(ParameterTypes.TEXT);
              }
            };
            for (int i = 0; i < inputs.length; i++) {
                ParameterRef input = inputs[i];
                BaseParameterDescription param = new ConstructableBaseParameterDescription(input);
                this.addParameterDescription(param);
                try {
                    singleInterface.addInputParameter(param.getName());
                } catch (ParameterDescriptionNotFoundException e) {
                    // impossible.
                    log.error("createMetadata(String)", e);
                    assert false : "we'll see about that";
                }
            }
            
            //and the same for outputs
            ParameterRef[] outputs = oneInterface.getOutput().getPref();

            log.debug("createMetadata() - Found  " + outputs.length+" output parameters");
            assert outputs.length == 1 :"http apps can only really have one output"; //possibly not true if we allow post processing

            ParameterRef output = outputs[0];
            BaseParameterDescription result = new ConstructableBaseParameterDescription(output);
            this.addParameterDescription(result);
            try {
                singleInterface.addOutputParameter(result.getName());
            } catch (ParameterDescriptionNotFoundException e) {
                // can't happen.
                log.error("createMetadata(String)", e);
                assert false : "never say never";
            }
            // add parameters to interface.
            this.addInterface(singleInterface);
        }

        if (log.isTraceEnabled()) {
            log.trace("createMetadata(String) - end");
        }
    }

    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String,
     *      org.astrogrid.community.User, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String jobStepID, User user, Tool tool) throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("initializeApplication(String jobStepID = " + jobStepID + ", User user = " + user
                    + ", Tool tool = " + tool + ") - start");
        }

        String newID = env.getIdGen().getNewID();
        final DefaultIDs ids = new DefaultIDs(jobStepID, newID, user);
        
        ApplicationInterface appInterface = this.getInterface(tool.getInterface());
        if (appInterface == null) { // go for default then..
            appInterface = this.getInterfaces()[0];
            log.debug("Interface not found - using default");
        }
        log.debug("Using interface "+appInterface);
        
        Application returnApplication = new HttpApplication(ids, tool, appInterface, env.getProtocolLib());
        
        
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
        return application.getCeaHttpAdapterSetup().getURL().getContent();
    }

    /**
     * Get the underlying description as returned by the registry
     * 
     * @TODO think about the overlap between HttpApplicationDescription and
     *       WebHttpApplication
     * @return Returns the application.
     */
    public CeaHttpApplicationType getApplication() {
        return application;
    }
}

/*
 * $Log: HttpApplicationDescription.java,v $
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