/*$Id: ApplicationDescription.java,v 1.3 2004/03/12 14:53:09 nw Exp $
 * Created on 09-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.Parameters;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.common.bean.BaseBean;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import org.exolab.castor.types.AnyNode;
import org.exolab.castor.xml.ValidationException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** Description of an application 
 * <p>
 * This class provides access to the descriptor of an application - by wrapping a {@link org.astrogrid.applications.beans.v1.ApplicationBase} object.
 * <ul>
 * <li>{@link #getInterfaces}
 * <li>{@link #getName}
 * <li>{@link #getParameters}
 * </ul>
 * 
 * <p>
 * It also provides methods to create a new workflow {@link org.astrogrid.workflow.beans.v1.Tool} object that can be used to call the application this class describes
 * <ul>
 * <li>{@link #createToolFromDefaultInterface}
 * <li>{@link #createToolFromInterface}
 * </ul>
 * 
* <p>
* ParameterValues can be added to a <tt>Tool</tt> by hand, with the assistance of these methods:
* <ul>
* <li>{@link c#reateValueFromDefinition}
* <li>{@link #getDefinitionForValue}
* <li>{@link #getReferenceForValue}
* <li>{@link #getDefinitionForReference}
* </ul>
* 
* <p>
* Finally, a Tool object can be checked that it conforms to the application description by calling the {@link #validate} method
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Mar-2004
 *
 */
public class ApplicationDescription  {
    /** Construct a new ApplicationDescription
     * 
     */
    public ApplicationDescription(ApplicationBase app) {
        this.app = app;
        this.paramMap = populateParamMap(app);
    }
    private final ApplicationBase app;
    private final Map paramMap;
    
    private final Map populateParamMap(ApplicationBase app){
        Map m = new HashMap();
        Parameters params = app.getParameters();
        for (int i = 0; i < params.getParameterCount(); i++) {
            BaseParameterDefinition param = params.getParameter(i);
            m.put(param.getName(),param);
        }
        return m;
    }
    
    /**  interfaces contain a bunch of 'ParameterRef' objects, that reference 'BaseParameterDefinitions' that are defined elsewhere, and give the type, user interface
     * description, etc of the paramter. This method will get a definition for a parameter reference
     * @param parameterRef the reference to look up
     * @return the associated parameter definition.
     * @throws IllegalArgumentException when asked to find definition for a reference that is not in the application description.
     */ 
    public BaseParameterDefinition getDefinitionForReference(ParameterRef parameterRef) throws IllegalArgumentException {
        BaseParameterDefinition result = (BaseParameterDefinition)paramMap.get(parameterRef.getRef());
        if (result == null) {
            throw new IllegalArgumentException("ParameterRef " + parameterRef.getRef() + " not found in this application description");
        }
        return result;
    }
    
    /** find the parameter reference assocuated with a parameter value in a particular interface*/
    public ParameterRef getReferenceForValue(ParameterValue paramVal, Interface intf) throws IllegalArgumentException {
        ParameterRef[] refs = intf.getInput().getPref();
        String name = paramVal.getName();     
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Empty name in this parameter value");
        }
        for (int i = 0; i < refs.length; i++) {
            if (refs[i].getRef().equals(name)) {
                return refs[i];  
            }          
        }
        // hmm, wasn't an input. try outputs..
        refs = intf.getOutput().getPref();
        for (int i = 0; i < refs.length; i++) {
            if (refs[i].getRef().equals(name)) {
                return refs[i];
            }            
        }
        // not found. we throw.
        throw new IllegalArgumentException("ParameterValue " + paramVal.getName() + "not found in this interface");        
    }
    
    /** convenience method to get a parameter definition for a parameter value.
     * <p>
     * equivalent to <tt> getDefinitionForReference(getReferenceForValue(p,i))</tt>
     * @param paramVal
     * @param intf
     * @return
     * @throws IllegalArgumentException
     */
    public BaseParameterDefinition getDefinitionForValue(ParameterValue paramVal, Interface intf) throws IllegalArgumentException {
        return getDefinitionForReference(getReferenceForValue(paramVal,intf));
    }
    
    
    /** from a definition of a parameter, create a new, initialized parameter value.
     * 
     * @param defn definition of the parameter
     * @return a parameter value with name, type initialized. if <tt>defn</tt> provides a defaultValue, this is set as <tt>content</tt>
     */
    public ParameterValue createValueFromDefinition(BaseParameterDefinition defn) {
        ParameterValue paramVal = new ParameterValue();
        paramVal.setName(defn.getName());
        paramVal.setType(defn.getType());
        Object o= defn.getDefaultValue();
        if (o != null) { // work around here.. - extension schema, so there's a hole, where an anynode appears. orsomething.
            if (o instanceof AnyNode) {
                AnyNode any = (AnyNode)o;
                paramVal.setContent(any.getStringValue());
            } else {
               paramVal.setContent(o.toString());
            }
        }
        return paramVal;
    }
    
    
    /** construct a new, initialized tool from the default (first) interface in the application descriptor */
    public Tool createToolFromDefaultInterface() {
        return createToolFromInterface(app.getInterfaces().get_interface(0));
    }
    
    
    /** construct a new, initialized tool from an interface in this application descriptor
     * @param intf the interface to create a tool instance from 
     * @return a populated tool object
     * @throws IllegalArgumentException if the interface does not belong to this application description.*/
    public Tool createToolFromInterface(Interface intf) throws IllegalArgumentException {
        Tool t = new Tool();
        t.setInterface(intf.getName());
        t.setName(app.getName());
        Input input = new Input();
        Output output = new Output();
        t.setInput(input);
        t.setOutput(output);        
        // populate inputs - pity these can't be the same class.
        ParameterRef[] parameterRefs = intf.getInput().getPref();
        for (int i = 0; i < parameterRefs.length; i++) {
            ParameterRef paramRef = parameterRefs[i];
            BaseParameterDefinition paramDef = getDefinitionForReference(paramRef);
            ParameterValue paramVal = createValueFromDefinition(paramDef);
            input.addParameter(paramVal);
        }
        // do the same for outputs
        parameterRefs = intf.getOutput().getPref();
        for (int i = 0; i < parameterRefs.length; i++) {
            ParameterRef paramRef = parameterRefs[i];
            BaseParameterDefinition paramDef = getDefinitionForReference(paramRef);
            ParameterValue paramVal = createValueFromDefinition(paramDef);
            output.addParameter(paramVal);
        }
        
        return t;
    }
    
    /** verify that a tool object matches what is expected by the application (as defined by this descriptor)
     * <p>
     * at moment, checks against information in the application description. in future, could access web services to resolve ucds, or check url parameters, etc. 
     */
    public void validate(Tool t) throws ToolValidationException{
        // check tool structure 
        try {
            t.validate();
        } catch (ValidationException e) {
            throw new ToolValidationException(e);
        }
        // so now we know that all required fields are there, etc. 
        Interface[] intfs = getInterfaces().get_interface();
        Interface intf = null;
        for (int i = 0; i < intfs.length; i++) {
            if (intfs[i].getName().equals(t.getInterface())) {
                intf = intfs[i];
            }            
        }
        if (intf == null) {
            throw new ToolValidationException("Interface " + t.getInterface() + " not found"); 
        }
        // now need to check each parameter, against its interface, and the definition itself.
        ParameterRef[] refs = intf.getInput().getPref();
        BaseBean searchRoot = t.getInput();
        for (int i =0; i < refs.length; i++) {
            validateReference(refs[i],searchRoot);
        }
        // now the outputs
        refs = intf.getOutput().getPref();
        searchRoot = t.getOutput();
        for (int i =0; i < refs.length; i++) {
            validateReference(refs[i],searchRoot);
        }                      
    }
    
    private void validateReference(ParameterRef reference,BaseBean searchRoot) throws ToolValidationException {
        Iterator results = searchRoot.findXPathIterator("/parameter[name = '" + reference.getRef() + "']");
        try {
            BaseParameterDefinition paramDef = getDefinitionForReference(reference);
            int occurenceCount = 0;
            while( results.hasNext()) {
                occurenceCount ++;
                ParameterValue val = (ParameterValue)results.next();
                
            }
            if (occurenceCount < reference.getMinoccurs() || occurenceCount > reference.getMaxoccurs()) {
                throw new ToolValidationException("Parameter " + reference.getRef() + " occurs " + occurenceCount 
                    + ". Should be between " + reference.getMinoccurs() + " and " + reference.getMaxoccurs());
            }
        } catch (IllegalArgumentException e) {
            throw new ToolValidationException(e);
        }        
    }
    
 
    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#getInterfaces()
     */
    public InterfacesType getInterfaces() {
        return app.getInterfaces();
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#getName()
     */
    public String getName() {
        return app.getName();
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#getParameters()
     */
    public Parameters getParameters() {
        return app.getParameters();
    }

}


/* 
$Log: ApplicationDescription.java,v $
Revision 1.3  2004/03/12 14:53:09  nw
bugfix for default parameters

Revision 1.2  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.2  2004/03/11 13:36:46  nw
tidied up interfaces, documented

Revision 1.1.2.1  2004/03/09 17:42:50  nw
getting there..

Revision 1.1  2004/03/09 15:31:21  nw
represents the initerface to an application
 
*/