/*$Id: ApiHelpImpl.java,v 1.13 2008/12/22 18:18:00 nw Exp $
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

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
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
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/** Implementation of the APIHelp component.
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
    public ApiHelpImpl(final ACRInternal reg, final Converter conv, final Transformer rpc, final Transformer string) {
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
    	return reg.getDescriptors().values().toArray(new ModuleDescriptor[]{});
    }
    
    public String[] listMethods() {
        final List result = new ArrayList();

        for (final Iterator i = reg.getDescriptors().values().iterator(); i.hasNext(); ) {
            final ModuleDescriptor m = (ModuleDescriptor) i.next();
            for(final Iterator j = m.componentIterator(); j.hasNext(); ) {
                final ComponentDescriptor cd = (ComponentDescriptor)j.next();
                for (final Iterator k = cd.methodIterator(); k.hasNext(); ) {
                    final MethodDescriptor md = (MethodDescriptor)k.next();
                    result.add(m.getName() + "." + cd.getName() + "." + md.getName());
                }
            }
        }
        return (String[])result.toArray(new String[result.size()]);
    }

    public String[] listModules() {
        final List modules = new ArrayList();
        for (final Iterator i =reg.getDescriptors().values().iterator(); i.hasNext(); ) {
            final ModuleDescriptor md = (ModuleDescriptor)i.next();
            modules.add(md.getName());
        }
        return (String[])modules.toArray(new String[modules.size()]);
    }
    
    public String[] listComponentsOfModule(final String moduleName) throws NotFoundException {
        final List components = new ArrayList();
        final ModuleDescriptor m = reg.getDescriptors().get(moduleName);
        if (m == null) {
            throw new NotFoundException("Unknown module " + moduleName);
         }            
        for (final Iterator i = m.componentIterator(); i.hasNext(); ) {
            final ComponentDescriptor cd = (ComponentDescriptor)i.next();
            components.add(m.getName() + "." + cd.getName());
        }
        return (String[])components.toArray(new String[components.size()]);
    }
    
    public String interfaceClassName(final String arg0) throws NotFoundException {
    	if (arg0 == null) {
    		// should throw some other kind of exception really - but not permitted by the signature
    		throw new NotFoundException("null parameter");
    	}
        final String[] names = arg0.split("\\.");        
        final ModuleDescriptor m = reg.getDescriptors().get(names[0]);
        if (m == null) {
            throw new NotFoundException("Unknown module " + names[0]);
        }
        final ComponentDescriptor cd = m.getComponent(names[1]);
        if (cd == null) {
            throw new NotFoundException("Unknown component " + names[1]);
        }
        return cd.getInterfaceClass().getName();
    }
    
    public String[] listMethodsOfComponent(final String componentName) throws NotFoundException {
        final List methods = new ArrayList();
        final String[] names = componentName.split("\\.");
        final ModuleDescriptor m = reg.getDescriptors().get(names[0]);

        if (m == null) {
            throw new NotFoundException("Unknown module " + names[0]);
        }
        final ComponentDescriptor cd = m.getComponent(names[1]);
        if (cd == null) {
            throw new NotFoundException("Unknown component " + names[1]);
        }                     
        for (final Iterator i = cd.methodIterator(); i.hasNext(); ) {
            final MethodDescriptor md = (MethodDescriptor)i.next();
            methods.add(m.getName() + "." + cd.getName() + "." + md.getName());
        }
        return (String[])methods.toArray(new String[methods.size()]);            
    }
    
    public String[][] methodSignature(final String methodName) throws NotFoundException {
        final List sigs = new ArrayList();
        if (methodName == null) {
        	throw new NotFoundException("null");
        }

        final String[] names = methodName.split("\\.");
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
            final ModuleDescriptor m = reg.getDescriptors().get(names[0]);
            if (m == null) {
                throw new NotFoundException( "Unknown module " + names[0]);
            }
            final ComponentDescriptor cd = m.getComponent(names[1]);
            if (cd == null) {
                throw new NotFoundException("Unknown component " + names[1]);
            }                
            final MethodDescriptor md = cd.getMethod(names[2]);
            if (md == null) {
                throw new NotFoundException("Unknown method "+ names[2]);
            }     
            final List sig= new ArrayList();
            sig.add(getXMLRPCType(md.getReturnValue()));
            for (final Iterator i = md.parameterIterator(); i.hasNext();) {
                sig.add(getXMLRPCType( ((ValueDescriptor)i.next())));
            }    
            sigs.add(sig.toArray(new String[sig.size()]));
        
            
        return (String[][])sigs.toArray(new String[sigs.size()][]);
    }
    
    protected String getXMLRPCType(final ValueDescriptor vd) {
        // strictly speaking, this doesn't return valid xmlrpc types - but these are only a suggestion, not a spec, anyhow.
        final String type = vd.getUitype();
        return type == null ? "string" : type.trim();
    }

    public String moduleHelp(final String moduleName) throws NotFoundException {
            final ModuleDescriptor m = reg.getDescriptors().get(moduleName);
            if (m == null) {
                throw new NotFoundException("Unknown module " + moduleName);
            }
            final StringBuffer result = new StringBuffer();
            result
            .append(m.getName())
            .append(" - ")
            .append(m.getDescription())    ;
            return result.toString();        
                                  
    }
    
    public String componentHelp(final String componentName) throws NotFoundException {
    	if (componentName == null) {
    		throw new NotFoundException("null");
    	}
        final String[] names = componentName.split("\\.");
        final String moduleName=names[0];
            final ModuleDescriptor m = reg.getDescriptors().get(moduleName);
            if (m == null) {
                throw new NotFoundException("Unknown module " + moduleName);
            }
            final ComponentDescriptor cd = m.getComponent(names[1]);
            if (cd == null) {
                throw new NotFoundException("Unknown component " + names[1]);
            }                        
            final StringBuffer result = new StringBuffer()
            .append(componentName)
            .append("\n")
            .append(stripHTML(cd.getDescription()));
            return result.toString();                   
    }
    

    public String methodHelp(final String methodName) throws NotFoundException {
        if (methodName == null) {
            throw new NotFoundException("null");
        }
        final String[] names = methodName.split("\\.");
        final ModuleDescriptor m = reg.getDescriptors().get(names[0]);
        if (m == null) {
            throw new NotFoundException( "Unknown module " + names[0]);
        }
        // special case for builtin methods..
        if (m.getName().equals("system") && names.length == 2) {
            return methodHelp("system.apihelp." + names[1]);
        }

        final ComponentDescriptor cd = m.getComponent(names[1]);
        if (cd == null) {
            throw new NotFoundException("Unknown component " + names[1]);
        }                
        final MethodDescriptor md = cd.getMethod(names[2]);
        if (md == null) {
            throw new NotFoundException("Unknown method "+ names[2]);
        }

        final StringBuilder result = new StringBuilder()  
            .append(getXMLRPCType(md.getReturnValue()))
            .append(' ')
            .append(methodName)
            .append('(');
        
        final ValueDescriptor[] parameters = md.getParameters();
        boolean parameterDescriptionSeen = false;
        for (int ix = 0; ix < parameters.length; ix++) {
            if (ix > 0) {
                result.append(", ");
            }
            final ValueDescriptor vd = parameters[ix];
            result.append(getXMLRPCType(vd))
                .append(" ")
                .append(vd.getName());
            
            parameterDescriptionSeen = parameterDescriptionSeen 
                        ||  StringUtils.isNotBlank(vd.getDescription()) ;
        }   
        result.append(")\n").append(stripHTML(md.getDescription()));

        if (parameters.length > 0 && parameterDescriptionSeen) {
            result.append("\n\nParameters:\n");
            for (int ix = 0; ix < parameters.length; ix++) {
                final ValueDescriptor vd = parameters[ix];
                result.append("   ")
                .append(vd.getName())
                .append(" - ")
                .append(stripHTML(vd.getDescription()))
                .append("\n");
            }           
        }
        if (StringUtils.isNotBlank(md.getReturnValue().getDescription())) {
            result.append("\nReturns:\n   ")
            .append(stripHTML(md.getReturnValue().getDescription()));
        }
        return result.toString();
    }

    //@todo merge ApiHelp.callFunction,  XMLRPCServlet.execute() and HtmlServlet.callMethod
	public Object callFunction(final String methodName, final int returnKind, final Object[] args) throws ACRException, InvalidArgumentException, NotFoundException {
		if (methodName == null) {
			throw new InvalidArgumentException("null");
		}
		final String[] names = methodName.split("\\.");
		if (names.length != 3) {
			throw new InvalidArgumentException("Expected a fully-qualified function name - of format module.component.function");
		}
		// this traversal isn't necessary, but is good for error-trapping.
	      final ModuleDescriptor m = reg.getDescriptors().get(names[0]);
	        if (m == null) {
	            throw new NotFoundException( "Unknown module " + names[0]);
	        }
	        
	        final ComponentDescriptor cd = m.getComponent(names[1]);
	        if (cd == null) {
	            throw new NotFoundException("Unknown component " + names[1]);
	        }                
	        final MethodDescriptor md = cd.getMethod(names[2]);
	        if (md == null) {
	            throw new NotFoundException("Unknown method "+ names[2]);
	        }		

	        // ok. all should be there. go on and call the method.
	        final Module module = reg.getModule(m.getName());
	        final Object service = module.getComponent(cd.getName());
	        try {
				final Method method = ReflectionHelper.getMethodByName(service.getClass(),md.getName());
				final Class[] parameterTypes = method.getParameterTypes();
		        final int argumentCount =  args == null ? 0 : args.length;
				if (parameterTypes.length != argumentCount) {
		        	throw new InvalidArgumentException("Function " + methodName + " expects "
		        			+ parameterTypes.length + " parameters, but " 
		        			+ argumentCount + " arguments were provided"
		        			);
		        }				
                // convert parameters to correct types.
                logger.debug("Converting args...");
                final Object[] realArgs = new Object[parameterTypes.length];
                final Iterator it = md.parameterIterator();
                for (int i =0; i < parameterTypes.length && it.hasNext(); i++) {
                    //unused.ValueDescriptor vd = (ValueDescriptor)it.next();
                    realArgs[i] = conv.convert(parameterTypes[i],args[i]);
                }
                // call method

                logger.debug("Calling method...");
                final Object result = MethodUtils.invokeMethod(service,md.getName(),realArgs);
                switch (returnKind) {
                case DATASTRUCTURE:
                	return rpc.transform(result);
                case STRING:
                	return string.transform(result);
                case ORIGINAL:
                default:
                	return result;
                }
            } catch (final InvocationTargetException e) {
            	throw new ACRException(e.getCause());
           
			} catch (final NoSuchMethodException x) {
				throw new NotFoundException(new ExceptionFormatter().format(x));
			} catch (final IllegalAccessException x) {
				throw new ACRException(x);
			}
	        
	}

	/** strips specific HTML tags known to occur in doc.
	 * doesn't blindluy strip all tags, else xml examples (e.g. registry resource documents)
	 * will get trashed too.
	 * @param in
	 * @return
	 */
	public static String stripHTML(final String in) {
	    return in.replaceAll("</?(A|a|B|b|BR|br|DD|dd|DL|dl|DT|dt|H2|h2|I|i|LI|li|P|p|PRE|pre|TT|tt|UL|ul|BLOCKQUOTE|blockquote)\\s*[^>]*>","");
//        return in.replaceAll("<\\w+\\s*[^>]>","");
        //return in.replaceAll("<(B|BR)\\s*[^>]*>","");
	}
    }        



/* 
$Log: ApiHelpImpl.java,v $
Revision 1.13  2008/12/22 18:18:00  nw
improved in-program API help.

Revision 1.12  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.11  2008/03/10 17:01:28  nw
removed unneeded synchronization

Revision 1.10  2007/10/08 08:36:28  nw
improved exception formatting

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