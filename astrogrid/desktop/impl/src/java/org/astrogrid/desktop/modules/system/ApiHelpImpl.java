/*$Id: ApiHelpImpl.java,v 1.9 2007/06/18 17:00:13 nw Exp $
 * Created on 23-Jun-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.acr.builtin.ValueDescriptor;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.framework.Module;
import org.astrogrid.desktop.framework.ReflectionHelper;

/** Implementation of the APIHelp component
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 23-Jun-2005
 * @todo strip html tags from responses - this should be text-only.
 * @todo transform results of invocations.
 */
public class ApiHelpImpl implements ApiHelp {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ApiHelpImpl.class);

    /** Construct a new ApiHelpImpl
     * 
     */
    public ApiHelpImpl(ACRInternal reg, Converter conv, Transformer rpc, Transformer string) {
        super();
        this.reg = reg;
        this.conv = conv;
        this.rpc = rpc;
        this.string = string;
    }
    protected final ACRInternal reg;
    protected final Converter conv;
    protected final Transformer rpc;
    protected final Transformer string;
    
    
    public ModuleDescriptor[] listModuleDescriptors() {
    	return (ModuleDescriptor[])reg.getDescriptors().values().toArray(new ModuleDescriptor[]{});
    }
    
    public String[] listMethods() {
        Vector result = new Vector();

        for (Iterator i = reg.getDescriptors().values().iterator(); i.hasNext(); ) {
            ModuleDescriptor m = (ModuleDescriptor) i.next();
            for(Iterator j = m.componentIterator(); j.hasNext(); ) {
                ComponentDescriptor cd = (ComponentDescriptor)j.next();
                for (Iterator k = cd.methodIterator(); k.hasNext(); ) {
                    MethodDescriptor md = (MethodDescriptor)k.next();
                    result.add(m.getName() + "." + cd.getName() + "." + md.getName());
                }
            }
        }
        return (String[])result.toArray(new String[result.size()]);
    }

    public String[] listModules() {
        Vector modules = new Vector();
        for (Iterator i =reg.getDescriptors().values().iterator(); i.hasNext(); ) {
            ModuleDescriptor md = (ModuleDescriptor)i.next();
            modules.add(md.getName());
        }
        return (String[])modules.toArray(new String[modules.size()]);
    }
    
    public String[] listComponentsOfModule(String moduleName) throws NotFoundException {
        Vector components = new Vector();
        ModuleDescriptor m = (ModuleDescriptor)reg.getDescriptors().get(moduleName);
        if (m == null) {
            throw new NotFoundException("Unknown module " + moduleName);
         }            
        for (Iterator i = m.componentIterator(); i.hasNext(); ) {
            ComponentDescriptor cd = (ComponentDescriptor)i.next();
            components.add(m.getName() + "." + cd.getName());
        }
        return (String[])components.toArray(new String[components.size()]);
    }
    
    public String interfaceClassName(String arg0) throws NotFoundException {
    	if (arg0 == null) {
    		// should throw some other kind of exception really - but not permitted by the signature
    		throw new NotFoundException("null parameter");
    	}
        String[] names = arg0.split("\\.");        
        ModuleDescriptor m = (ModuleDescriptor)reg.getDescriptors().get(names[0]);
        if (m == null) {
            throw new NotFoundException("Unknown module " + names[0]);
        }
        ComponentDescriptor cd = m.getComponent(names[1]);
        if (cd == null) {
            throw new NotFoundException("Unknown component " + names[1]);
        }
        return cd.getInterfaceClass().getName();
    }
    
    public String[] listMethodsOfComponent(String componentName) throws NotFoundException {
        Vector methods = new Vector();
        String[] names = componentName.split("\\.");
        ModuleDescriptor m = (ModuleDescriptor)reg.getDescriptors().get(names[0]);

        if (m == null) {
            throw new NotFoundException("Unknown module " + names[0]);
        }
        ComponentDescriptor cd = m.getComponent(names[1]);
        if (cd == null) {
            throw new NotFoundException("Unknown component " + names[1]);
        }                     
        for (Iterator i = cd.methodIterator(); i.hasNext(); ) {
            MethodDescriptor md = (MethodDescriptor)i.next();
            methods.add(m.getName() + "." + cd.getName() + "." + md.getName());
        }
        return (String[])methods.toArray(new String[methods.size()]);            
    }
    
    public String[][] methodSignature(String methodName) throws NotFoundException {
        Vector sigs = new Vector();
        if (methodName == null) {
        	throw new NotFoundException("null");
        }

        String[] names = methodName.split("\\.");
        if (names[0].equalsIgnoreCase("system")) {
            if (names[1].equalsIgnoreCase("listMethods")) {
                sigs.add(new String[]{"array","string"});
                return(String[][])sigs.toArray(new String[sigs.size()][2]);
            }
            if (names[1].equalsIgnoreCase("methodSignature")) {
                sigs.add(new String[]{"array","string"});
                return (String[][])sigs.toArray(new String[sigs.size()][2]);
            }
            if (names[1].equalsIgnoreCase("listModules")) {
                sigs.add(new String[]{"array","string"});
                return (String[][])sigs.toArray(new String[sigs.size()][2]);
            }                
            if (names[1].equalsIgnoreCase("methodHelp")) {
                sigs.add(new String[]{"string","string"});
                return (String[][])sigs.toArray(new String[sigs.size()][2]);
            }
            if (names[1].equalsIgnoreCase("moduleHelp")) {
                sigs.add(new String[]{"string","string"});
                return (String[][])sigs.toArray(new String[sigs.size()][2]);
            }
            if (names[1].equalsIgnoreCase("componentHelp")) {
                sigs.add(new String[]{"string","string"});
                return (String[][])sigs.toArray(new String[sigs.size()][2]);
            }                
            if (names[1].equalsIgnoreCase("listComponentsOfModule")) {
                sigs.add(new String[]{"array","string"});
                return(String[][])sigs.toArray(new String[sigs.size()][2]);
            }
            if (names[1].equalsIgnoreCase("listMethodsOfComponent")) {
                sigs.add(new String[]{"array","string"});
                return (String[][])sigs.toArray(new String[sigs.size()][2]);
            }                
        }
            ModuleDescriptor m = (ModuleDescriptor)reg.getDescriptors().get(names[0]);
            if (m == null) {
                throw new NotFoundException( "Unknown module " + names[0]);
            }
            ComponentDescriptor cd = m.getComponent(names[1]);
            if (cd == null) {
                throw new NotFoundException("Unknown component " + names[1]);
            }                
            MethodDescriptor md = cd.getMethod(names[2]);
            if (md == null) {
                throw new NotFoundException("Unknown method "+ names[2]);
            }     
            List sig= new ArrayList();
            sig.add(getXMLRPCType(md.getReturnValue()));
            for (Iterator i = md.parameterIterator(); i.hasNext();) {
                sig.add(getXMLRPCType( ((ValueDescriptor)i.next())));
            }    
            sigs.add(sig.toArray(new String[sig.size()]));
        
            
        return (String[][])sigs.toArray(new String[sigs.size()][]);
    }
    
    protected String getXMLRPCType(ValueDescriptor vd) {
        // strictly speaking, this doesn't return valid xmlrpc types - but these are only a suggestion, not a spec, anyhow.
        String type = vd.getUitype();
        return type == null ? "string" : type.trim();
    }

    public String moduleHelp(String moduleName) throws NotFoundException {
            ModuleDescriptor m = (ModuleDescriptor)reg.getDescriptors().get(moduleName);
            if (m == null) {
                throw new NotFoundException("Unknown module " + moduleName);
            }
            StringBuffer result = new StringBuffer();
            result.append("Module ")
            .append(m.getName())
            .append("\n\t")
            .append(m.getDescription())    ;
            return result.toString();        
                                  
    }
    
    public String componentHelp(String componentName) throws NotFoundException {
    	if (componentName == null) {
    		throw new NotFoundException("null");
    	}
        String[] names = componentName.split("\\.");
        String moduleName=names[0];
            ModuleDescriptor m = (ModuleDescriptor)reg.getDescriptors().get(moduleName);
            if (m == null) {
                throw new NotFoundException("Unknown module " + moduleName);
            }
            ComponentDescriptor cd = m.getComponent(names[1]);
            if (cd == null) {
                throw new NotFoundException("Unknown component " + names[1]);
            }                        
            StringBuffer result = new StringBuffer()
            .append("Component ")
            .append(cd.getName())
            .append(" in module ")
            .append(m.getName())
            .append("\n\t")
            .append(cd.getDescription());
            return result.toString();                   
    }
    

    //@todo improve format - remove cruft - from method & component help.
    public String methodHelp(String methodName) throws NotFoundException {
    	if (methodName == null) {
    		throw new NotFoundException("null");
    	}
        String[] names = methodName.split("\\.");
        ModuleDescriptor m = (ModuleDescriptor)reg.getDescriptors().get(names[0]);
        if (m == null) {
            throw new NotFoundException( "Unknown module " + names[0]);
        }
        // special case for builtin methods..
        if (m.getName().equals("system") && names.length == 2) {
            return methodHelp("system.apihelp." + names[1]);
        }
        
        ComponentDescriptor cd = m.getComponent(names[1]);
        if (cd == null) {
            throw new NotFoundException("Unknown component " + names[1]);
        }                
        MethodDescriptor md = cd.getMethod(names[2]);
        if (md == null) {
            throw new NotFoundException("Unknown method "+ names[2]);
        }
     
        StringBuffer result = new StringBuffer()
                .append("Method ")
                .append(md.getName())
                .append(" belonging to component ")
                .append(cd.getName())
                .append(" in module ")
                .append(m.getName())
                .append("\n\t")
                .append(md.getDescription());
        for (Iterator i = md.parameterIterator(); i.hasNext(); ) {
            ValueDescriptor vd = (ValueDescriptor)i.next();
            result.append("\n")
                    .append(vd.getName())
                    .append(" : ")
                    .append(getXMLRPCType(vd))
                    .append("\n")
                    .append(vd.getDescription());                                
        }           
        result.append("\n")
                .append(md.getReturnValue().getName())
                .append(" : ")
                .append(getXMLRPCType(md.getReturnValue()))
                .append("\n")
                .append(md.getReturnValue().getDescription());
        return result.toString();
        }

    //@todo merge ApiHelp.callFunction,  XMLRPCServlet.execute() and HtmlServlet.callMethod
	public Object callFunction(String methodName, int returnKind, Object[] args) throws ACRException, InvalidArgumentException, NotFoundException {
		if (methodName == null) {
			throw new InvalidArgumentException("null");
		}
		String[] names = methodName.split("\\.");
		if (names.length != 3) {
			throw new InvalidArgumentException("Expected a fully-qualified function name - of format module.component.function");
		}
		// this traversal isn't necessary, but is good for error-trapping.
	      ModuleDescriptor m = (ModuleDescriptor)reg.getDescriptors().get(names[0]);
	        if (m == null) {
	            throw new NotFoundException( "Unknown module " + names[0]);
	        }
	        
	        ComponentDescriptor cd = m.getComponent(names[1]);
	        if (cd == null) {
	            throw new NotFoundException("Unknown component " + names[1]);
	        }                
	        MethodDescriptor md = cd.getMethod(names[2]);
	        if (md == null) {
	            throw new NotFoundException("Unknown method "+ names[2]);
	        }		

	        // ok. all should be there. go on and call the method.
	        Module module = reg.getModule(m.getName());
	        Object service = module.getComponent(cd.getName());
	        try {
				Method method = ReflectionHelper.getMethodByName(service.getClass(),md.getName());
				Class[] parameterTypes = method.getParameterTypes();
		        int argumentCount =  args == null ? 0 : args.length;
				if (parameterTypes.length != argumentCount) {
		        	throw new InvalidArgumentException("Function " + methodName + " expects "
		        			+ parameterTypes.length + " parameters, but " 
		        			+ argumentCount + " arguments were provided"
		        			);
		        }				
                // convert parameters to correct types.
                logger.debug("Converting args...");
                Object[] realArgs = new Object[parameterTypes.length];
                Iterator it = md.parameterIterator();
                for (int i =0; i < parameterTypes.length && it.hasNext(); i++) {
                    //unused.ValueDescriptor vd = (ValueDescriptor)it.next();
                    realArgs[i] = conv.convert(parameterTypes[i],args[i]);
                }
                // call method

                logger.debug("Calling method...");
                Object result = MethodUtils.invokeMethod(service,md.getName(),realArgs);
                switch (returnKind) {
                case DATASTRUCTURE:
                	return rpc.transform(result);
                case STRING:
                	return string.transform(result);
                case ORIGINAL:
                default:
                	return result;
                }
            } catch (InvocationTargetException e) {
            	throw new ACRException(e.getCause());
           
			} catch (NoSuchMethodException x) {
				throw new NotFoundException(x.getMessage());
			} catch (IllegalAccessException x) {
				throw new ACRException(x);
			}
	        
	}


    }        



/* 
$Log: ApiHelpImpl.java,v $
Revision 1.9  2007/06/18 17:00:13  nw
javadoc fixes.

Revision 1.8  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.7  2006/06/27 10:38:34  nw
findbugs tweaks

Revision 1.6  2006/06/15 09:54:09  nw
improvements coming from unit testing

Revision 1.5  2006/06/02 00:16:15  nw
Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.

Revision 1.4  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.3.60.3  2006/04/14 02:45:02  nw
finished code.extruded plastic hub.

Revision 1.3.60.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.3.60.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.1  2005/06/23 09:08:26  nw
changes for 1.0.3 release
 
*/