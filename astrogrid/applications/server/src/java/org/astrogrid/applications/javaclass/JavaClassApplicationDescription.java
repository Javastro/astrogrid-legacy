/*$Id: JavaClassApplicationDescription.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.javaclass;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.base.BaseParameterDescription;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;

import java.lang.reflect.Method;

/** application description for a javaclass application
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class JavaClassApplicationDescription extends AbstractApplicationDescription {
    /** Construct a new JavaClassApplicationDescription
     * 
     */
    public JavaClassApplicationDescription(Method method,String communityName,ApplicationDescriptionEnvironment env) {
        super(env);
        this.method = method;
        createMetadata(communityName);
    }
    protected final Method method;
    
    /** creates parameter descriptions, etc.
*/
    /** populates this object with parameterDescriptions by reflecting on application method */
    protected final void createMetadata(String communityName){
        setName(communityName + "/" + method.getName());
        Class[] inputs = method.getParameterTypes();
        BaseApplicationInterface singleInterface =new BaseApplicationInterface(method.getName(),this);        
        for (int i = 0; i < inputs.length; i++) {
            Class input = inputs[i];
            JavaClassParameterDescription param = new JavaClassParameterDescription();
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
        }
    }
    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String jobStepID, User user, Tool tool) throws Exception {
        String newID = env.getIdGen().getNewID();
        final DefaultIDs ids = new DefaultIDs(jobStepID,newID,user);
        // we know there's only one interface supported for each application
        ApplicationInterface interf = this.getInterfaces()[0];
        return new JavaClassApplication(ids,tool,interf,env.getProtocolLib());
    }
}


/* 
$Log: JavaClassApplicationDescription.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/