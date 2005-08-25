/*$Id: TypeStructureTransformer.java,v 1.2 2005/08/25 16:59:58 nw Exp $
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

import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.commons.collections.Transformer;
import org.exolab.castor.xml.Marshaller;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
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

/** implem,entation of transformer that will render almost any object tree down to types suitable for xmlrpc lib - i.e. type structures - prims, maps, vectors.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 *
 */
public class TypeStructureTransformer implements Transformer {

    /** Construct a new DefaultXmlRpcTransformer
     * 
     */
    private TypeStructureTransformer() {
        this(true);
    }
    
    private TypeStructureTransformer(boolean b) {
        this.specialTreatmentForCastor = b;
    }
    
    private final boolean specialTreatmentForCastor;

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        if (arg0 == null) {
            return null;
        }
        if (arg0 instanceof JobURN) { // simple castor type - just get value.
            return ((JobURN)arg0).getContent();
        }
        if ( specialTreatmentForCastor && (arg0 instanceof Workflow || arg0 instanceof Tool)) { // special treatment for castor types..
            try {
                StringWriter sw = new StringWriter();
                Marshaller.marshal(arg0,sw);
                return sw.toString();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }            
        }
        if (arg0 instanceof String 
                || arg0 instanceof Integer 
                || arg0 instanceof Double 
                || arg0 instanceof Vector 
                || arg0 instanceof Hashtable 
                || arg0 instanceof Date 
                || arg0 instanceof Boolean 
                || arg0 instanceof byte[]) {
            return arg0;
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
            return new Integer(((Long)arg0).intValue());
        }
        
        if (arg0 instanceof URL
                || arg0 instanceof URI
                || arg0 instanceof Ivorn 
                || arg0 instanceof org.astrogrid.filemanager.common.Ivorn 
                || arg0 instanceof NodeIvorn) {
            return arg0.toString();
        }
        if (arg0 instanceof Map) {
            Map m = (Map)arg0;
            Hashtable h = new Hashtable(m.size());
            for (Iterator i = m.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry e = (Map.Entry)i.next();
                h.put(this.transform(e.getKey()),this.transform(e.getValue()));
            }
            return h;
        }
        if (arg0 instanceof Object[]) {
            arg0 = Arrays.asList((Object[])arg0); // processed by Collection clause.
        }        
        if (arg0 instanceof Collection) {
            Collection col = (Collection)arg0;
            Vector v = new Vector(col.size());
            for (Iterator i = col.iterator(); i.hasNext(); ) {
                v.add(this.transform(i.next()));
            }
            return v;
        }
                       
        // we got a bean of some kind. lets hope it doesn't contain cycles..
        Map result = new Hashtable();
        DynaBean db = new WrapDynaBean(arg0);
        DynaClass dc = db.getDynaClass();
        DynaProperty[] props = dc.getDynaProperties();
        for (int i =0; i < props.length; i++) {            
            String name = props[i].getName();
            if (name == null || "class".equals(name)) { // don't want to persist this one - causes an infinite loop.
                continue;
            } // don't want the following either.
          
            Class type = props[i].getType();
            if (type == null 
                    || InputStream.class.isAssignableFrom( type) 
                    || OutputStream.class.isAssignableFrom(type )
                    || Reader.class.isAssignableFrom(type)
                    || Writer.class.isAssignableFrom(type)){
                continue;
            }            
           //@todo handle indexed and mapped properties, if needed.
            try {
                Object value = db.get(name);
                if (value != null) { // hashtable can't handle nulls..
                    result.put(name,this.transform(value));
                }
            } catch (IllegalArgumentException e) {
                // thrown by dynabean for properties with only a setter - ignore.
            }
        }
        return result;
    }
    
    private static final Transformer theInstance = new TypeStructureTransformer();
    private static final Transformer castorIgnoringInstance = new TypeStructureTransformer(false);
    public static Transformer getInstance() {
        return theInstance;
    }
    public static Transformer getCastorIgnoringInstance() {
        return castorIgnoringInstance;
    }

}


/* 
$Log: TypeStructureTransformer.java,v $
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