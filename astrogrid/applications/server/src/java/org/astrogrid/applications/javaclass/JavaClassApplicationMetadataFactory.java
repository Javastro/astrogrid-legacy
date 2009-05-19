/*
 * $Id: JavaClassApplicationMetadataFactory.java,v 1.8 2009/05/19 15:11:31 pah Exp $
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.ivoa.resource.Content;

import org.astrogrid.applications.description.ServiceDefinitionFactory;
import org.astrogrid.applications.description.ServiceDescriptionException;
import org.astrogrid.applications.description.annotation.CEAApplication;
import org.astrogrid.applications.description.annotation.CEAInterface;
import org.astrogrid.applications.description.annotation.CEAParameter;
import org.astrogrid.applications.description.base.ApplicationKind;
import org.astrogrid.applications.description.base.BaseParameterDefinition;
import org.astrogrid.applications.description.base.InterfaceDefinition;
import org.astrogrid.applications.description.base.InterfaceDefinitionList;
import org.astrogrid.applications.description.base.ParameterDefinitionList;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.description.cea.CeaApplication;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.joda.time.DateTime;

/**
 * Simple factory to create metadata from a java method definition.
 * 
 * @TODO make much more sophisticated - 
 * @TODO this is still a little messy really.... should just be part of the {@link JavaClassMetadataAdapter}
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 18 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class JavaClassApplicationMetadataFactory {

    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(JavaClassApplicationMetadataFactory.class);
    private final CeaApplication ceaapp;
    private final Class<?> clazz;
    private Map<String, Method> methmap = new HashMap<String, Method>();
    private ServiceDefinitionFactory sf;
    
    public JavaClassApplicationMetadataFactory(CeaApplication app, Class<?> classs, ServiceDefinitionFactory sf){
        ceaapp = app;
        clazz = classs;
        this.sf = sf;
    }

     /**
     * populates this object with parameterDescriptions by reflecting on
     * application method
     * 
     * @throws ServiceDescriptionException
     * @todo improve handling to parameter types
     * @todo need unit test for annotation metadata generation
     * */
    protected final CeaApplication createMetadata(
            )
            throws ServiceDescriptionException {
        
        if (clazz.isAnnotationPresent(CEAApplication.class)) {
            CEAApplication ann = clazz.getAnnotation(CEAApplication.class);
            ceaapp.setIdentifier(ann.id());
            ceaapp.setTitle(clazz.getSimpleName());
            ceaapp.setShortName(clazz.getSimpleName().substring(0,15));
            DateTime value = new DateTime();
            ceaapp.setCreated(value); // TODO need better date for this...
            ceaapp.setUpdated(value);
            ceaapp.setStatus("active");
            Content content = new Content();
            content.addSubject("???");//TODO get subject from somewhere
            content.setDescription(ann.description());
            content.setReferenceURL(ann.referenceURL());
            ceaapp.setContent(content);
            // curation information assumed to be just the same as for the service.
            ceaapp.setCuration(sf.getCECDescription().getCuration());
         } else {
            logger.fatal("necessary Application annotation missing");
            assert false : "necessary Application annotation missing";
        }
        
        ParameterDefinitionList parameters = ceaapp.getApplicationDefinition().getParameters();
        parameters.getParameterDefinition(); // needed to ensure that the parameter definition is created
        InterfaceDefinitionList interfaces = ceaapp.getApplicationDefinition().getInterfaces();
        interfaces.getInterfaceDefinition(); // needed to ensure that the interface definition is created

        // each public static method becomes an interface.
        for (Method method : clazz.getDeclaredMethods()) {
            int code = method.getModifiers();
            if (Modifier.isStatic(code) && Modifier.isPublic(code) && method.isAnnotationPresent(CEAInterface.class)) {
                methmap.put(method.getName(), method);
                Class<?>[] inputs = method.getParameterTypes();
                Annotation[][] paramAnnotations = method.getParameterAnnotations(); // parameter annotations are a little less convienient...
                InterfaceDefinition singleInterface = new InterfaceDefinition();
                singleInterface.setId(method.getName());
                singleInterface.setDescription(method.getAnnotation(CEAInterface.class).description());
                for (int i = 0; i < inputs.length; i++) {
                    Class<?> input = inputs[i];
                    JavaClassParameterDescription param;
                    
                    String paramId, description, name;
                    if(paramAnnotations.length > 0 && paramAnnotations[i].length > 0) // allow for the case where there is no parameter annotation
                    {
                        CEAParameter parameterAnnotation = (CEAParameter) paramAnnotations[i][0]; //IMPL assume that there is only a CEAParameter annotation - dangerous for future.
                        paramId = parameterAnnotation.id();
                        description = parameterAnnotation.description();
                        if(( name=parameterAnnotation.name())==null){
                            name = paramId;
                        }
                    } else { 
                        paramId = method.getName()+i;
                        name = paramId;
                        description = "";
                    }
                    try {
                        param = (JavaClassParameterDescription) parameters.getParameterDefinitionbyId(paramId);
                    } catch (ParameterDescriptionNotFoundException e1) {
                       
                        param = new JavaClassParameterDescription();
                        param.setId(paramId);
                        ParameterTypes targetType = ParameterTypes.TEXT; // suitable
                        // default.
                        // special cases.
                        //FIXME - need to incorporate type information - however the full mapping has not been decided yet - perhaps remove the unknown type from ParameterTypes
                        Class coreType = (input.isArray() ? input
                                .getComponentType() : input); // get the base type, if this is an array.
                        if (coreType.equals(Boolean.class)
                                || coreType.equals(Boolean.TYPE)) {
                            targetType = ParameterTypes.BOOLEAN;
                        } else if (coreType.equals(Byte.TYPE)
                                || coreType.equals(Byte.class)) {
                            targetType = ParameterTypes.BINARY;
                        } else if (coreType.equals(Integer.TYPE)
                                || coreType.equals(Integer.class)) {
                            targetType = ParameterTypes.INTEGER;
                        } else if (coreType.equals(Float.TYPE)
                                || coreType.equals(Float.class)) {
                            targetType = ParameterTypes.REAL;
                        } else if (coreType.equals(Double.TYPE)
                                || coreType.equals(Double.class)) {
                            targetType = ParameterTypes.REAL; // IMPL real = double
                            // now...
                        }
                        // URI, Document, Node?
                        param.setType(targetType);
                        // TODO remove? param.setSubType(input.getName());
                        param.setTargetClass(input);
                        param.setName(name);
                        param.setDescription(description);
                    }
                    ceaapp.getApplicationDefinition().getParameters()
                            .getParameterDefinition().add(param);
                    try {
                        singleInterface.addInputParameter(param.getId());//FIXME - possible multiplicity if array...
                    } catch (ParameterDescriptionNotFoundException e) {
                        // impossible.
                        e.printStackTrace();
                        assert false; // are you sure?
                    }
                }
                Class output = method.getReturnType();
                if (!output.equals(Void.TYPE)) { // only add a parameter description
                    // if the method doesn't return
                    // void.
                    BaseParameterDefinition result = new BaseParameterDefinition();
                    result.setId(method.getName()+"-result");
                    result.setType(ParameterTypes.TEXT);
                    // TODO remove? result.setSubType(output.getName());
                    parameters.getParameterDefinition().add(result);
                    singleInterface.addOutputParameter(result.getId());
                    // add parameters to interface.
                    interfaces.getInterfaceDefinition().add(singleInterface);
                }
            }
        }
        ceaapp.getApplicationDefinition().getApplicationType().add(
                ApplicationKind.PROCESSING);
        return ceaapp;
    }
    
    /**
     * return the mapping between interface id and methods. 
     * must be called after {@link #createMetadata()}.
     * @return
     */
    protected final Map<String, Method>getMethodMap(){
        return new HashMap<String, Method>(methmap);
    }

}

/*
 * $Log: JavaClassApplicationMetadataFactory.java,v $
 * Revision 1.8  2009/05/19 15:11:31  pah
 * make sure that the referenceURL gets set
 *
 * Revision 1.7  2009/05/19 15:10:24  pah
 * make sure that the referenceURL gets set
 *
 * Revision 1.6  2009/04/04 20:41:54  pah
 * ASSIGNED - bug 2113: better configuration for java CEC
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2113
 * Introduced annotations
 * Revision 1.5 2008/10/06
 * 12:16:16 pah factor out classes common to server and client
 * 
 * Revision 1.4 2008/09/24 13:40:49 pah package naming changes
 * 
 * Revision 1.3 2008/09/13 09:51:03 pah code cleanup
 * 
 * Revision 1.2 2008/09/03 14:18:44 pah result of merge of pah_cea_1611 branch
 * 
 * Revision 1.1.2.4 2008/06/16 21:58:58 pah altered how the description
 * libraries fit together - introduced the SimpleApplicationDescriptionLibrary
 * to just plonk app descriptions into.
 * 
 * Revision 1.1.2.3 2008/05/17 16:44:31 pah make sure that the short name is set
 * 
 * Revision 1.1.2.2 2008/03/26 17:15:40 pah Unit tests pass
 * 
 * Revision 1.1.2.1 2008/03/19 23:10:55 pah First stage of refactoring done -
 * code compiles again - not all unit tests passed
 * 
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 */
