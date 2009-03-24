/*$Id: TypeStructureTransformer.java,v 1.16 2009/03/24 13:08:21 nw Exp $
 * Created on 21-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.commons.collections.Transformer;

/** Transformer that will render almost any object tree down to types suitable for xmlrpc lib.
 * Output should just be primitives,  maps, vectors.
 * 
 * @modified added type information - to indicate which sunclass is being returned
 * @todo can now let List and Map pass through - xmlrpc lib accepts more than just vector and hashtable.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Feb-2005
 *
 */
public class TypeStructureTransformer implements Transformer {
	
	public TypeStructureTransformer(final Transformer trans) {
		this.trans = trans;
	}
	// transformer used to perform recursion.
	private final Transformer trans;
	

  

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        if (arg0 == null) {
            return "null"; //@todo check this is correct. nulls can;t be transported by xmlrpc.
        }
        // pass thtough these supported primitive types.
        if (arg0 instanceof String 
                || arg0 instanceof Integer 
                || arg0 instanceof Double 
                || arg0 instanceof Date 
                || arg0 instanceof Boolean 
                || arg0 instanceof Byte
                || arg0 instanceof byte[]) {
            return arg0;
        } 

        if (arg0 instanceof Void) {
        	return "OK";
        }
        if (arg0 instanceof Class) {
        	return ((Class)arg0).getName();
        }
        if (arg0 instanceof Calendar) {
            return ((Calendar)arg0).getTime();
        }
        
        if (arg0 instanceof Float) {
            // promote to double;
            return new Double(((Float)arg0).doubleValue());
        }
        
        if (arg0 instanceof Long) {
            // demote to int. yechh.
            return Integer.valueOf(((Long)arg0).intValue());
        }
        
        if (arg0 instanceof URL
                || arg0 instanceof URI) {
            return arg0.toString();
        }

       
       // special case for hashtable - as it's a type that's supported by our xmlrpc lib, we don't need to 
       // change it - - just transform the contents (which hopefully is more efficient than creating a whole new structure)
       
       if (arg0 instanceof Hashtable) {
    	   final Hashtable t = (Hashtable)arg0;
    	   for (final Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
    	       final Map.Entry e = (Map.Entry)i.next();
    	       final Object key = e.getKey();
    	       final Object tranKey = trans.transform(key);
    	       if (tranKey != key) { // intentional - not using equals(). want to see whether key has been passed through unchanged
    	           // transformed key is different - so better remove old binding.
    	           i.remove(); // will remove that key.
    	       }
    	       t.put(tranKey,trans.transform(e.getValue())); // depending on whether tranKey!=key, this will either overwrite the old binding, or create a new one.
    	       
    	   }    
    	   return t;
       }
        // case for all other map types..
        if (arg0 instanceof Map) {
            final Map m = (Map)arg0;
            final Hashtable h = new Hashtable(m.size());
            for (final Iterator i = m.entrySet().iterator(); i.hasNext(); ) {
                final Map.Entry e = (Map.Entry)i.next();
                h.put(trans.transform(e.getKey()),
                		e.getValue() == null ? "null" : trans.transform(e.getValue()));
                //this null check is an interim fix - but probably need to be handling nulls better throughout.
                // this check is to fix a barf caused by passing a null to recursive call of trans.transform() - 
                // which then uses the strategy to find which transformer to use.
                // seems that the strategy can't handle null - which is a pain.
            }
            return h;
        }
        // convert an array to a collection.
        if (arg0 instanceof Object[]) {           
            arg0 = Arrays.asList((Object[])arg0); // then processed by Collection clause.
        }      
        
        // special case for vector - as it's a supported type, transform it's contents in-place.
        if (arg0 instanceof Vector) {
        	final Vector v = (Vector)arg0;
        	for (int i = 0; i < v.size(); i++) {
        		final Object o = v.get(i);
        		v.set(i,trans.transform(o));
        	}
        	return v;
        }
        
        // recursively transform a collection.
        if (arg0 instanceof Collection) {
            final Collection col = (Collection)arg0;
            final Vector v = new Vector(col.size());
            for (final Iterator i = col.iterator(); i.hasNext(); ) {
                v.add(trans.transform(i.next()));
            }
            return v;
        }
                       
        // we got a bean of some kind. lets hope it doesn't contain cycles..
        final Map result = new Hashtable();
        final DynaBean db = new WrapDynaBean(arg0);
        final DynaClass dc = db.getDynaClass();
        final DynaProperty[] props = dc.getDynaProperties();
        for (int i =0; i < props.length; i++) {            
            final String name = props[i].getName();
            if (name == null || "class".equals(name)) { // don't want to persist this one - causes an infinite loop.
                continue;
            } // don't want the following either.
          
            final Class type = props[i].getType();
            if (type == null 
                    || InputStream.class.isAssignableFrom( type) 
                    || OutputStream.class.isAssignableFrom(type )
                    || Reader.class.isAssignableFrom(type)
                    || Writer.class.isAssignableFrom(type)){
                continue;
            }            
           //in the future, we might need to also handle indexed and mapped properties
            // however, AR API doesn't use these at present.
            try {
                final Object value = db.get(name);
                if (value != null) { // hashtable can't handle nulls..
                    result.put(name,trans.transform(value));
                }
            } catch (final IllegalArgumentException e) {
                // thrown by dynabean for properties with only a setter - ignore.
            }
        }
        //modified - add originating class type information.
        final Vector iVector = getTypeInformation(arg0);
        result.put("__interfaces",iVector);
        return result;
    }




    /** Creates a vector of the class names of the implementation class,
     * plus any interfaces this class provides
     * @param arg0 the object to inspect.
     * @return a vector of strings.
     */
    private Vector getTypeInformation(final Object arg0) {
        Class beanClass = arg0.getClass();
        final Vector iVector = new Vector();
        iVector.add(beanClass.getName());
        do {
            final Class[] interfaces = beanClass.getInterfaces();
            for(int i = 0; i < interfaces.length; i++) {
                iVector.add(interfaces[i].getName());
            }
            beanClass = beanClass.getSuperclass();            
        } while (beanClass != Object.class);
        return iVector;
    }
   
}


/* 
$Log: TypeStructureTransformer.java,v $
Revision 1.16  2009/03/24 13:08:21  nw
reworking of messaging system to make it protocol neutral.

Revision 1.15  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.14  2008/08/19 12:47:10  nw
findbugs fixes and improvements.

Revision 1.13  2008/08/04 16:37:23  nw
Complete - task 441: Get plastic upgraded to latest XMLRPC

Complete - task 430: upgrade to latest xmlrpc lib

Revision 1.12  2007/10/07 10:40:05  nw
added serialization of classnames

Revision 1.11  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.10  2006/10/11 10:39:41  nw
made a start on handling nulls.

Revision 1.9  2006/08/31 21:31:37  nw
minor tweaks and doc fixes.

Revision 1.8  2006/08/02 13:12:49  nw
added more efficient transformation code for vector and hashtable.

Revision 1.7  2006/08/01 23:27:30  jdt
bugfix for 1762

Revision 1.6  2006/06/27 19:13:52  nw
adjusted todo tags.

Revision 1.5  2006/06/27 10:29:36  nw
findbugs tweaks

Revision 1.4  2006/06/15 09:58:18  nw
improvements coming from unit testing

Revision 1.3  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.2.66.2  2006/04/18 18:49:03  nw
version to merge back into head.

Revision 1.2.66.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/06/23 09:08:26  nw
changes for 1.0.3 release

Revision 1.4  2005/06/08 14:51:59  clq2
1111

Revision 1.2.20.2  2005/06/02 14:34:32  nw
first release of application launcher

Revision 1.2.20.1  2005/05/12 15:49:21  nw
litte lix

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 13:55:21  nw
got vospace ls working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/