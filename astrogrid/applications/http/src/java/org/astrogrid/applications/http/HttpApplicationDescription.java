/*$Id: HttpApplicationDescription.java,v 1.3 2004/07/30 14:54:47 jdt Exp $
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

import org.astrogrid.applications.Application;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.beans.v1.WebHttpApplication;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;
import org.apache.commons.logging.*;
/** 
 * application description for an HttpApplication
 * @author jdt
 *
 */
public class HttpApplicationDescription extends AbstractApplicationDescription {
    
    Log log = LogFactory.getLog(HttpApplicationDescription.class);
    
    /** 
     * Constructor
     */
    public HttpApplicationDescription(WebHttpApplication application, final String communityName, final ApplicationDescriptionEnvironment env) {
        super(env);
        this.application = application;
        createMetadata(communityName);
    }
    protected final WebHttpApplication application;
    
    /** creates parameter descriptions, etc.
*/
    /** populates this object with parameterDescriptions by reflecting on application method */
    protected final void createMetadata(String communityName){
        setName(communityName + "/" + application.getName());
        log.debug("Name: "+getName());
        /*//@TODO add interfaces and parameters...
        Class[] inputs = method.getParameterTypes();
        BaseApplicationInterface singleInterface =new BaseApplicationInterface(method.getName(),this);        
        for (int i = 0; i < inputs.length; i++) {
            Class input = inputs[i];
            HttpParameterDescription param = new HttpParameterDescription();
            param.setName("parameter-" + i);
            param.setType(ParameterTypes.TEXT); // set all to string for now. @todo refine this later.
            param.setSubType(input.getName());
            param.setTargetClass(input);
            this.addParameterDescription(param);
            try {
                singleInterface.addInputParameter(param.getName());
            }
            catch (ParameterDescriptionNotFoundException e) {
                // impossible.
                e.printStackTrace();
            }
        }
        Class output = method.getReturnType();
        if (!output.equals(Void.TYPE)) { // only add a parameter description if the method doesn't return void.            
            BaseParameterDescription result = new BaseParameterDescription();
            result.setName("result");
            result.setType(ParameterTypes.TEXT);
            result.setSubType(output.getName());            
            this.addParameterDescription(result);
            try {
                singleInterface.addOutputParameter(result.getName());
            } catch (ParameterDescriptionNotFoundException e) {
                // can't happen.
                e.printStackTrace();
            }        
            // add parameters to interface.
            this.addInterface(singleInterface);
        }*/
    }
    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String jobStepID, User user, Tool tool) throws Exception {
        String newID = env.getIdGen().getNewID();
        final DefaultIDs ids = new DefaultIDs(jobStepID,newID,user);
        // we know there's only one interface supported for each application
        // @TODO not true for HttpApplications - but assume for now
        ApplicationInterface interf = this.getInterfaces()[0];
        return new HttpApplication(ids,tool,interf,env.getProtocolLib());
    }
    /**
     * @return
     */
    public String getUrl() {
        return application.getURL();
    }
}


/* 
$Log: HttpApplicationDescription.java,v $
Revision 1.3  2004/07/30 14:54:47  jdt
merges in from case3 branch

Revision 1.1.4.5  2004/07/30 11:02:30  jdt
Added unit tests, refactored the RegistryQuerier anf finished off
HttpApplicationCEAComponentManager.

Revision 1.1.4.4  2004/07/29 21:30:47  jdt
*** empty log message ***

Revision 1.1.4.3  2004/07/29 17:08:22  jdt
Think about how I'm going to get stuff out of the registry

Revision 1.1.4.2  2004/07/29 16:35:21  jdt
Safety checkin, while I think about what happens next.

Revision 1.1.4.1  2004/07/27 17:20:25  jdt
merged from applications_JDT_case3

Revision 1.1.2.1  2004/07/24 17:16:16  jdt
Borrowed from javaclass application....stealing is always quicker.


 
*/