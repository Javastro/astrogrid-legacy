/*$Id: AbstractQOM.java,v 1.2 2003/09/02 14:43:54 nw Exp $
 * Created on 28-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.adql;
import org.exolab.castor.mapping.*;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.util.ClassDescriptorResolverImpl;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import java.lang.reflect.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
/** abstract implementation of the query object model classes.
 *  - necessary work around for castor, so we can get all generated classes implementing the QOM interface.

 * @author Noel Winstanley nw@jb.man.ac.uk 28-Aug-2003
 *
 */
public abstract class AbstractQOM implements QOM {
    
    /** @see QOM#acceptBottomUp */
     public void acceptBottomUp(DynamicVisitor v) throws Exception{
        QOM[] children = this.getChildren();
        for (int i =0;i < children.length; i++) {
            children[i].acceptBottomUp(v);
        }
        callVisitor(v);
    }
    /** @see QOM#acceptTopDown */
    public void acceptTopDown(DynamicVisitor v) throws Exception {
        callVisitor(v);
        QOM[] children = this.getChildren();
        for (int i = 0; i < children.length; i++) {
            children[i].acceptTopDown(v);
        }
    }
    
    
    
    /** compute the children elements of this element 
     * @return array of child QOM objects (all non-null child elements of this element)
     * @todo - is it possible add an intermediate caching of results, so reflection only needs to be done one.
     *  
     */
    public QOM[] getChildren() throws MappingException {
        ClassDescriptorResolver resolver = new ClassDescriptorResolverImpl();        
        XMLClassDescriptor cd = resolver.resolve(this.getClass());
        XMLFieldDescriptor[] fields = cd.getElementDescriptors();
        List qomFields = new Collector();
        for (int i = 0; i < fields.length; i++) {
            FieldDescriptor field = fields[i];
            if ( QOM.class.isAssignableFrom( field.getFieldType() ) ){
                // rules - if not null, add to list.
                // if multivalued, add all elements to the list.
                if (field.isMultivalued()) {
                    Object vals = field.getHandler().getValue(this);
                    if (vals != null) {
                        CollectionHandler h = CollectionHandlers.getHandler(vals.getClass());                        
                        for (Enumeration e = h.elements(vals); e.hasMoreElements(); ) {
                            qomFields.add( e.nextElement() );
                        }
                    }
                } else {
                    qomFields.add( field.getHandler().getValue(this) );
                }
            }
        }
        return (QOM[]) qomFields.toArray(new QOM[]{});
    }
 
    /** convenience collection class */
    private class Collector extends ArrayList {
        
            /** only adds if the item is not null         */
        public boolean add(Object o) {
            if (o != null) {
               return super.add(o);
            } else {
                return false;
            }
        }
   } //end inner class

  /** call the appropriate method on the visitor object.
   * searches for a matching <tt>visit</tt> method in the visting object, starting with
   * most specific type, and then traversing up the QOM class hierarchy.
   * If no other more type-specific mathc is found, will call {@link DynamicVisitor#visit}
   * @param v the visiting object.
   * @throws InvocationTargetException if the method cannot be called
   * @throws IllegalAccessException if the method cannot be called.
   */
  public void callVisitor(DynamicVisitor v)  throws Exception{
      Method m = null;
      Class c = this.getClass();
    while (m == null && c != null && c != AbstractQOM.class ) {
      try {
        m = v.getClass().getMethod("visit",new Class[] {c});
      } catch (NoSuchMethodException e) { // didn't find a method. repeat, looking for parent class type.
          c = c.getSuperclass();
      }
    }
    if (m != null) {
        // found the method, now call it.
         m.invoke(v,new Object[]{this});
    } else {
        // else use the default.
        v.visit(this);
    }
  }

 
} 


/* 
$Log: AbstractQOM.java,v $
Revision 1.2  2003/09/02 14:43:54  nw
added dynamic visitor pattern.

Revision 1.1  2003/08/28 15:27:54  nw
added ADQL object model.
 
*/