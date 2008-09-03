/*
 * $Id: JavaClassApplicationMetadataFactory.java,v 1.2 2008/09/03 14:18:44 pah Exp $
 * 
 * Created on 18 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.javaclass;

import java.lang.reflect.Method;

import org.astrogrid.applications.description.ServiceDefinitionFactory;
import org.astrogrid.applications.description.ServiceDescriptionException;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.ApplicationKind;
import org.astrogrid.applications.description.base.BaseParameterDefinition;
import org.astrogrid.applications.description.base.InterfaceDefinition;
import org.astrogrid.applications.description.base.InternallyConfiguredApplicationDescription;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.joda.time.DateTime;

import net.ivoa.resource.cea.CeaApplication;

/**
 * Simple factory to create metadata from a java method definition.
 * @TODO make much more sophisticated - use java 5 annotations...
 * @TODO refactor the way that JavaClass is done to be more in line with the {@link InternallyConfiguredApplicationDescription}
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 18 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class JavaClassApplicationMetadataFactory {

    private final String communityName;
    private ServiceDefinitionFactory servdef;
    
    public JavaClassApplicationMetadataFactory(String authority, ServiceDefinitionFactory sdef)
    {
	this.communityName = authority;
	this.servdef = sdef;
    }

    /** populates this object with parameterDescriptions by reflecting on application method
     * @throws ServiceDescriptionException 
         * @todo improve handling to parameter types
         *  */
        protected final CeaApplication createMetadata(Method method) throws ServiceDescriptionException{
            CeaApplication retval = new CeaApplication();
            retval.setContent(servdef.getCECDescription().getContent());
            retval.setCuration(servdef.getCECDescription().getCuration());
            retval.setIdentifier("ivo://"+communityName + "/" + method.getName());
            String shortname=method.getName();
            if (shortname.length()>16) {
		shortname = shortname.substring(0, 16);
	    }
            retval.setShortName(shortname);
            retval.setTitle(method.getName());
            DateTime value = new DateTime();
	    retval.setCreated(value);
	    retval.setUpdated(value);
	    retval.setStatus("active");
            Class[] inputs = method.getParameterTypes();
            InterfaceDefinition singleInterface = new InterfaceDefinition();
            singleInterface.setId(method.getName());        
            for (int i = 0; i < inputs.length; i++) {
                Class input = inputs[i];
                JavaClassParameterDescription param = new JavaClassParameterDescription();
                param.setId("parameter-" + i);
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
                    targetType = ParameterTypes.REAL; //IMPL real = double now...
                }
                // URI, Document, Node?
                
                param.setType(targetType); 
    //TODO remove?            param.setSubType(input.getName());
                param.setTargetClass(input);
                retval.getApplicationDefinition().getParameters().getParameterDefinition().add(param);
                try {
                    singleInterface.addInputParameter(param.getId());
                }
                catch (ParameterDescriptionNotFoundException e) {
                    // impossible.
                    e.printStackTrace();
                    assert false; //are you sure?
                }
            }
            Class output = method.getReturnType();
            if (!output.equals(Void.TYPE)) { // only add a parameter description if the method doesn't return void.            
                BaseParameterDefinition result = new BaseParameterDefinition();
                result.setId("result");
                result.setType(ParameterTypes.TEXT);
              //TODO remove?            result.setSubType(output.getName());            
                retval.getApplicationDefinition().getParameters().getParameterDefinition().add(result);
                singleInterface.addOutputParameter(result.getId());        
                // add parameters to interface.
                retval.getApplicationDefinition().getInterfaces().getInterfaceDefinition().add(singleInterface);
            }
            retval.getApplicationDefinition().getApplicationType().add(ApplicationKind.PROCESSING);
	    return retval;
        }

    
    
    
}


/*
 * $Log: JavaClassApplicationMetadataFactory.java,v $
 * Revision 1.2  2008/09/03 14:18:44  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.4  2008/06/16 21:58:58  pah
 * altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.
 *
 * Revision 1.1.2.3  2008/05/17 16:44:31  pah
 * make sure that the short name is set
 *
 * Revision 1.1.2.2  2008/03/26 17:15:40  pah
 * Unit tests pass
 *
 * Revision 1.1.2.1  2008/03/19 23:10:55  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
