/*$Id: MetadataHelper.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service;

import org.apache.commons.attributes.Attributes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/** Helpler methods for working with metadata.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class MetadataHelper {

    /** Construct a new MetadataHelper
     * 
     */
    private MetadataHelper() {
        super();
    }
    /** get service document associated with an servce object */
    public static ServiceDoc getServiceDoc(Object o) {
        return (ServiceDoc)Attributes.getAttribute(o.getClass(),ServiceDoc.class);
    }

    /** get service document associated with a serivce class */
    public static ServiceDoc getServiceDocForClass(Class clazz) {
        return (ServiceDoc)Attributes.getAttribute(clazz,ServiceDoc.class);
    }
    
    /** get a method by name from a class */
    public static Method getMethodByName(Class clazz, String name) {
        Method[] ms = clazz.getMethods();
        for (int i = 0; i < ms.length; i++) {
            if (ms[i].getName().equalsIgnoreCase(name.trim())) {
                return ms[i];
            }
        }
        throw new IllegalArgumentException("method " + name + " not found");
    }
    /** get metadata for a method */
    public static MethodDoc getMethodDoc(Method m) {
        return (MethodDoc) Attributes.getAttribute(m,MethodDoc.class);
    }
    
    /** check whether a method has metadata */
    public static boolean hasMethodDoc(Method m) {
        return Attributes.hasAttributeType(m,MethodDoc.class);
    }
    /** get all method metadata in a class */
    public static List getMethodDocsForClass(Class clazz) {
        List result = new ArrayList();
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (hasMethodDoc(methods[i])) {
                result.add(getMethodDoc(methods[i]));
            }
        }
        return result;
    }
    /** get return value documentation for a method */
    public static ReturnDoc getReturnDoc(Method m) {
        return (ReturnDoc) Attributes.getReturnAttribute(m,ReturnDoc.class);
    }
    /** get parameter documentation for a method
     * 
     * @param m
     * @return a list of {@link ParamDoc}
     */
    public static List getParamDocs(Method m) {
        List l = new ArrayList();
        for (int i = 0; i < m.getParameterTypes().length; i++) {
            l.add(Attributes.getParameterAttribute(m,i,ParamDoc.class));
        }
        return l;
    }
    
    

}


/* 
$Log: MetadataHelper.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/