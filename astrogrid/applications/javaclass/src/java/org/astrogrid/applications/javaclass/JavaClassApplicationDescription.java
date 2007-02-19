/*$Id: JavaClassApplicationDescription.java,v 1.2 2007/02/19 16:20:22 gtr Exp $
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

/** A description for an application that is implemented as a static java method.
 * <p>
 * For a method foo(), this class will create an {@link org.astrogrid.applications.description.ApplicationDescription} for <tt><i>authorityName</i>/foo</tt>,
 * where the authority name is specified in the constructor.
 * <p>
 * Constructs all the metadata for the application via reflection on the static method.
 * @todo add support for attributes, or something, so that other metadata can be specified (e.g. documentation, UCD).
 * @todo improve definition of types
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class JavaClassApplicationDescription extends AbstractApplicationDescription {
    /** Construct a new JavaClassApplicationDescription
     * @param method the method that is to be the implementation of this application
     * @param authorityName the name of the authority under which to add this application
     * @param env standard container for helper objects.
     * 
     */
    public JavaClassApplicationDescription(Method method,String authorityName,ApplicationDescriptionEnvironment env) {
        super(env);
        this.method = method;
        createMetadata(authorityName);
    }
    protected final Method method;
    
    /** populates this object with parameterDescriptions by reflecting on application method
     * @todo improve handling to parameter types
     *  */
    protected final void createMetadata(String communityName){
        setName(communityName + "/" + method.getName());
        Class[] inputs = method.getParameterTypes();
        BaseApplicationInterface singleInterface =new BaseApplicationInterface(method.getName(),this);        
        for (int i = 0; i < inputs.length; i++) {
            Class input = inputs[i];
            JavaClassParameterDescription param = new JavaClassParameterDescription();
            param.setName("parameter-" + i);
            ParameterTypes targetType = ParameterTypes.TEXT; // suitable default.
            // special cases.
            Class coreType = (input.isArray() ? input.getComponentType() : input); // get the base type, if this is an array.
            if (coreType.equals(Boolean.class) || coreType.equals(Boolean.TYPE)) {
                targetType = ParameterTypes.BOOLEAN;
            } else if (coreType.equals(Byte.TYPE) || coreType.equals(Byte.class)) {
                targetType = ParameterTypes.BINARY;
            } else if (coreType.equals(Integer.TYPE) || coreType.equals(Integer.class)) {
                targetType = ParameterTypes.INTEGER;
            } else if (coreType.equals(Float.TYPE) || coreType.equals(Float.class)) {
                targetType = ParameterTypes.REAL;
            } else if (coreType.equals(Double.TYPE) || coreType.equals(Double.class)) {
                targetType = ParameterTypes.DOUBLE;
            }
            // URI, Document, Node?
            
            param.setType(targetType); 
            param.setSubType(input.getName());
            param.setTargetClass(input);
            this.addParameterDescription(param);
            try {
                singleInterface.addInputParameter(param.getName());
            }
            catch (ParameterDescriptionNotFoundException e) {
                // impossible.
                e.printStackTrace();
                assert false; //are you sure?
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
                assert false; //never say never
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
Revision 1.2  2007/02/19 16:20:22  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:23:34  gtr
Javaclass-application stuff is moved to a separate project.

Revision 1.4  2004/07/30 14:54:47  jdt
merges in from case3 branch

Revision 1.3.4.1  2004/07/29 09:11:54  jdt
can't be too careful

Revision 1.3  2004/07/26 10:21:47  nw
javadoc

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