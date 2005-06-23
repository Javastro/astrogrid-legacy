/*$Id: ApiHelpImpl.java,v 1.1 2005/06/23 09:08:26 nw Exp $
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

import org.astrogrid.acr.builtin.Module;
import org.astrogrid.acr.builtin.ModuleRegistry;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;
import org.astrogrid.desktop.framework.descriptors.ValueDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Jun-2005
 *
 */
public class ApiHelpImpl implements ApiHelp {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ApiHelpImpl.class);

    /** Construct a new ApiHelpImpl
     * 
     */
    public ApiHelpImpl(ModuleRegistry reg) {
        super();
        this.reg = reg;
    }
    protected final ModuleRegistry reg;
    public List listMethods() {
        Vector result = new Vector();

        for (Iterator i = reg.moduleIterator(); i.hasNext(); ) {
            Module m = (Module)i.next();
            for(Iterator j = m.getDescriptor().componentIterator(); j.hasNext(); ) {
                ComponentDescriptor cd = (ComponentDescriptor)j.next();
                for (Iterator k = cd.methodIterator(); k.hasNext(); ) {
                    MethodDescriptor md = (MethodDescriptor)k.next();
                    result.add(m.getDescriptor().getName() + "." + cd.getName() + "." + md.getName());
                }
            }
        }
        return result;
    }

    public List listModules() {
        Vector modules = new Vector();
        for (Iterator i =reg.moduleIterator(); i.hasNext(); ) {
            ModuleDescriptor md = ((Module)i.next()).getDescriptor();
            modules.add(md.getName());
        }
        return modules;
    }
    
    public List listComponentsOfModule(String moduleName) throws XmlRpcException {
        Vector components = new Vector();
        Module m = reg.getModule(moduleName);
        if (m == null) {
            throw new XmlRpcException(100, "Unknown module " + moduleName);
         }            
        for (Iterator i = m.getDescriptor().componentIterator(); i.hasNext(); ) {
            ComponentDescriptor cd = (ComponentDescriptor)i.next();
            components.add(m.getDescriptor().getName() + "." + cd.getName());
        }
        return components;
    }
    
    public List listMethodsOfComponent(String componentName) throws XmlRpcException {
        Vector methods = new Vector();
        String[] names = componentName.split("\\.");
        Module m = reg.getModule(names[0]);
        m.getComponent(names[1]);
        if (m == null) {
            throw new XmlRpcException(100, "Unknown module " + names[0]);
        }
        ComponentDescriptor cd = m.getDescriptor().getComponent(names[1]);
        if (cd == null) {
            throw new XmlRpcException(101,"Unknown component " + names[1]);
        }                     
        for (Iterator i = cd.methodIterator(); i.hasNext(); ) {
            MethodDescriptor md = (MethodDescriptor)i.next();
            methods.add(m.getDescriptor().getName() + "." + cd.getName() + "." + md.getName());
        }
        return methods;            
    }
    
    public List methodSignature(String methodName) throws XmlRpcException {
        Vector sigs = new Vector();
        Vector sig = new Vector();
        sigs.add(sig);
        String[] names = methodName.split("\\.");
        if (names[0].equalsIgnoreCase("system")) {
            if (names[1].equalsIgnoreCase("listMethods")) {
                sig.add("array");
                sig.add("string");
                return sig;
            }
            if (names[1].equalsIgnoreCase("methodSignature")) {
                sig.add("array");
                sig.add("string");
                return sig;
            }
            if (names[1].equalsIgnoreCase("listModules")) {
                sig.add("array");
                sig.add("string");
                return sig;
            }                
            if (names[1].equalsIgnoreCase("methodHelp")) {
                sig.add("string");
                sig.add("string");
                return sig;
            }
            if (names[1].equalsIgnoreCase("moduleHelp")) {
                sig.add("string");
                sig.add("string");
                return sig;
            }
            if (names[1].equalsIgnoreCase("componentHelp")) {
                sig.add("string");
                sig.add("string");
                return sig;
            }                
            if (names[1].equalsIgnoreCase("listComponentsOfModule")) {
                sig.add("array");
                sig.add("string");
                return sig;
            }
            if (names[1].equalsIgnoreCase("listMethodsOfComponent")) {
                sig.add("array");
                sig.add("string");
                return sig;
            }                
        }
            Module m = reg.getModule(names[0]);
            if (m == null) {
                throw new XmlRpcException(100, "Unknown module " + names[0]);
            }
            ComponentDescriptor cd = m.getDescriptor().getComponent(names[1]);
            if (cd == null) {
                throw new XmlRpcException(101,"Unknown component " + names[1]);
            }                
            MethodDescriptor md = cd.getMethod(names[2]);
            if (md == null) {
                throw new XmlRpcException(102,"Unknown method "+ names[2]);
            }                
            sig.add(getXMLRPCType(md.getReturnValue()));
            for (Iterator i = md.parameterIterator(); i.hasNext();) {
                sig.add(getXMLRPCType( ((ValueDescriptor)i.next())));
            }       
        
        return sigs;
    }
    public static final String XMLRPC_TYPE_KEY = "system.xmlrpc.type";
    
    protected String getXMLRPCType(ValueDescriptor vd) {
        String type = vd.getProperty(XMLRPC_TYPE_KEY);
        return type == null ? "string" : type.trim();
    }

    public String moduleHelp(String moduleName) throws XmlRpcException {
            Module m = reg.getModule(moduleName);
            if (m == null) {
                throw new XmlRpcException(100, "Unknown module " + moduleName);
            }
            StringBuffer result = new StringBuffer();
            result.append("Module ")
            .append(m.getDescriptor().getName())
            .append("\n\t")
            .append(m.getDescriptor().getDescription())    ;
            return result.toString();        
                                  
    }
    
    public String componentHelp(String componentName) throws XmlRpcException {
        String[] names = componentName.split("\\.");
        String moduleName=names[0];
            Module m = reg.getModule(moduleName);
            if (m == null) {
                throw new XmlRpcException(100, "Unknown module " + moduleName);
            }
            ComponentDescriptor cd = m.getDescriptor().getComponent(names[1]);
            if (cd == null) {
                throw new XmlRpcException(101,"Unknown component " + names[1]);
            }                        
            StringBuffer result = new StringBuffer()
            .append("Component ")
            .append(cd.getName())
            .append(" in module ")
            .append(m.getDescriptor().getName())
            .append("\n\t")
            .append(cd.getDescription());
            return result.toString();                   
    }
    public String methodHelp(String methodName) throws XmlRpcException {
        String[] names = methodName.split("\\.");
        Module m = reg.getModule(names[0]);
        if (m == null) {
            throw new XmlRpcException(100, "Unknown module " + names[0]);
        }
        ComponentDescriptor cd = m.getDescriptor().getComponent(names[1]);
        if (cd == null) {
            throw new XmlRpcException(101,"Unknown component " + names[1]);
        }                
        MethodDescriptor md = cd.getMethod(names[2]);
        if (md == null) {
            throw new XmlRpcException(102,"Unknown method "+ names[2]);
        }
     
        StringBuffer result = new StringBuffer()
                .append("Method ")
                .append(md.getName())
                .append(" belonging to component ")
                .append(cd.getName())
                .append(" in module ")
                .append(m.getDescriptor().getName())
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
    }        



/* 
$Log: ApiHelpImpl.java,v $
Revision 1.1  2005/06/23 09:08:26  nw
changes for 1.0.3 release
 
*/