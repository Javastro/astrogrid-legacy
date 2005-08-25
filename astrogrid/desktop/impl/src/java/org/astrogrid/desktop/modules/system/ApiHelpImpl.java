/*$Id: ApiHelpImpl.java,v 1.2 2005/08/25 16:59:58 nw Exp $
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

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.desktop.framework.DefaultModule;
import org.astrogrid.desktop.framework.MutableACR;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;
import org.astrogrid.desktop.framework.descriptors.ValueDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
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
    public ApiHelpImpl(MutableACR reg) {
        super();
        this.reg = reg;
    }
    protected final MutableACR reg;
    public String[] listMethods() {
        Vector result = new Vector();

        for (Iterator i = reg.moduleIterator(); i.hasNext(); ) {
            DefaultModule m = (DefaultModule)i.next();
            for(Iterator j = m.getDescriptor().componentIterator(); j.hasNext(); ) {
                ComponentDescriptor cd = (ComponentDescriptor)j.next();
                for (Iterator k = cd.methodIterator(); k.hasNext(); ) {
                    MethodDescriptor md = (MethodDescriptor)k.next();
                    result.add(m.getDescriptor().getName() + "." + cd.getName() + "." + md.getName());
                }
            }
        }
        return (String[])result.toArray(new String[]{});
    }

    public String[] listModules() {
        Vector modules = new Vector();
        for (Iterator i =reg.moduleIterator(); i.hasNext(); ) {
            ModuleDescriptor md = ((DefaultModule)i.next()).getDescriptor();
            modules.add(md.getName());
        }
        return (String[])modules.toArray(new String[]{});
    }
    
    public String[] listComponentsOfModule(String moduleName) throws NotFoundException {
        Vector components = new Vector();
        DefaultModule m = (DefaultModule)reg.getModule(moduleName);
        if (m == null) {
            throw new NotFoundException("Unknown module " + moduleName);
         }            
        for (Iterator i = m.getDescriptor().componentIterator(); i.hasNext(); ) {
            ComponentDescriptor cd = (ComponentDescriptor)i.next();
            components.add(m.getDescriptor().getName() + "." + cd.getName());
        }
        return (String[])components.toArray(new String[]{});
    }
    
    /**
     * @see org.astrogrid.acr.system.ApiHelp#interfaceClassName(java.lang.String, java.lang.String)
     */
    public String interfaceClassName(String arg0) throws NotFoundException {
        String[] names = arg0.split("\\.");        
        DefaultModule m = (DefaultModule)reg.getModule(names[0]);
        if (m == null) {
            throw new NotFoundException("Unknown module " + names[0]);
        }
        ComponentDescriptor cd = m.getDescriptor().getComponent(names[1]);
        if (cd == null) {
            throw new NotFoundException("Unknown component " + names[1]);
        }
        return cd.getInterfaceClass().getName();
    }
    
    public String[] listMethodsOfComponent(String componentName) throws NotFoundException {
        Vector methods = new Vector();
        String[] names = componentName.split("\\.");
        DefaultModule m = (DefaultModule)reg.getModule(names[0]);

        if (m == null) {
            throw new NotFoundException("Unknown module " + names[0]);
        }
        ComponentDescriptor cd = m.getDescriptor().getComponent(names[1]);
        if (cd == null) {
            throw new NotFoundException("Unknown component " + names[1]);
        }                     
        for (Iterator i = cd.methodIterator(); i.hasNext(); ) {
            MethodDescriptor md = (MethodDescriptor)i.next();
            methods.add(m.getDescriptor().getName() + "." + cd.getName() + "." + md.getName());
        }
        return (String[])methods.toArray(new String[]{});            
    }
    
    public String[][] methodSignature(String methodName) throws NotFoundException {
        Vector sigs = new Vector();

        String[] names = methodName.split("\\.");
        if (names[0].equalsIgnoreCase("system")) {
            if (names[1].equalsIgnoreCase("listMethods")) {
                sigs.add(new String[]{"array","string"});
                return(String[][])sigs.toArray(new String[][]{});
            }
            if (names[1].equalsIgnoreCase("methodSignature")) {
                sigs.add(new String[]{"array","string"});
                return (String[][])sigs.toArray(new String[][]{});
            }
            if (names[1].equalsIgnoreCase("listModules")) {
                sigs.add(new String[]{"array","string"});
                return (String[][])sigs.toArray(new String[][]{});
            }                
            if (names[1].equalsIgnoreCase("methodHelp")) {
                sigs.add(new String[]{"string","string"});
                return (String[][])sigs.toArray(new String[][]{});
            }
            if (names[1].equalsIgnoreCase("moduleHelp")) {
                sigs.add(new String[]{"string","string"});
                return (String[][])sigs.toArray(new String[][]{});
            }
            if (names[1].equalsIgnoreCase("componentHelp")) {
                sigs.add(new String[]{"string","string"});
                return (String[][])sigs.toArray(new String[][]{});
            }                
            if (names[1].equalsIgnoreCase("listComponentsOfModule")) {
                sigs.add(new String[]{"array","string"});
                return(String[][])sigs.toArray(new String[][]{});
            }
            if (names[1].equalsIgnoreCase("listMethodsOfComponent")) {
                sigs.add(new String[]{"array","string"});
                return (String[][])sigs.toArray(new String[][]{});
            }                
        }
            DefaultModule m = (DefaultModule)reg.getModule(names[0]);
            if (m == null) {
                throw new NotFoundException( "Unknown module " + names[0]);
            }
            ComponentDescriptor cd = m.getDescriptor().getComponent(names[1]);
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
            sigs.add(sig.toArray(new String[]{}));
        
            
        return (String[][])sigs.toArray(new String[][]{});
    }
    public static final String XMLRPC_TYPE_KEY = "system.xmlrpc.type";
    
    protected String getXMLRPCType(ValueDescriptor vd) {
        String type = vd.getProperty(XMLRPC_TYPE_KEY);
        return type == null ? "string" : type.trim();
    }

    public String moduleHelp(String moduleName) throws NotFoundException {
            DefaultModule m = (DefaultModule)reg.getModule(moduleName);
            if (m == null) {
                throw new NotFoundException("Unknown module " + moduleName);
            }
            StringBuffer result = new StringBuffer();
            result.append("Module ")
            .append(m.getDescriptor().getName())
            .append("\n\t")
            .append(m.getDescriptor().getDescription())    ;
            return result.toString();        
                                  
    }
    
    public String componentHelp(String componentName) throws NotFoundException {
        String[] names = componentName.split("\\.");
        String moduleName=names[0];
            DefaultModule m = (DefaultModule)reg.getModule(moduleName);
            if (m == null) {
                throw new NotFoundException("Unknown module " + moduleName);
            }
            ComponentDescriptor cd = m.getDescriptor().getComponent(names[1]);
            if (cd == null) {
                throw new NotFoundException("Unknown component " + names[1]);
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
    public String methodHelp(String methodName) throws NotFoundException {
        String[] names = methodName.split("\\.");
        DefaultModule m = (DefaultModule)reg.getModule(names[0]);
        if (m == null) {
            throw new NotFoundException( "Unknown module " + names[0]);
        }
        // special case for builtin methods..
        if (m.equals("system") && names.length == 2) {
            return methodHelp("system.apihelp." + names[1]);
        }
        
        ComponentDescriptor cd = m.getDescriptor().getComponent(names[1]);
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
Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.1  2005/06/23 09:08:26  nw
changes for 1.0.3 release
 
*/