/*$Id: AxisObjectPureJavaReflectionProvider.java,v 1.2 2005/03/11 13:37:05 clq2 Exp $
 * Created on 24-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.nodestore.file;

import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;

import java.lang.reflect.Field;
import java.util.Iterator;

/** Adaptaion of the standard reflection provider in XStream - to ignore the ___prefixed fields that are used internally by axis.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2005
 *
 */
class AxisObjectPureJavaReflectionProvider extends PureJavaReflectionProvider {

    /** Construct a new AxisObjectPureJavaReflectionProvider
     * 
     */
    public AxisObjectPureJavaReflectionProvider() {
        super();
    }
    /** method is copied from XStream sources, and is standard, apart from part marked *NWW* */
    public void visitSerializableFields(Object object, ReflectionProvider.Visitor visitor) {
        for (Iterator iterator = fieldDictionary.serializableFieldsFor(object.getClass()); iterator.hasNext();) {
            Field field = (Field) iterator.next();
            if (!fieldModifiersSupported(field)) {
                continue;
            }
            /*NWW*/
            if (field.getName().startsWith("__")) {
                continue;
            }
            validateFieldAccess(field);
            Object value = null;
            try {
                value = field.get(object);
            } catch (IllegalArgumentException e) {
                throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e);
            } catch (IllegalAccessException e) {
                throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), e);
            }
            visitor.visit(field.getName(), field.getType(), field.getDeclaringClass(), value);
        }
    }
       
    
    

}


/* 
$Log: AxisObjectPureJavaReflectionProvider.java,v $
Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:38  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:27  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store
 
*/