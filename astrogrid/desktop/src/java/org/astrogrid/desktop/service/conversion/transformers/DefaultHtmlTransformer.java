/*$Id: DefaultHtmlTransformer.java,v 1.1 2005/02/22 01:10:31 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service.conversion.transformers;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/** Default implementaiton of transformer to html -  works ok-ish - not good for nested object trees.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *@todo try using tables instead of definitions lists..
 */
public class DefaultHtmlTransformer implements Transformer {

    /** Construct a new DefaultHtmlTransformer
     *
     */
    private DefaultHtmlTransformer() {
        super();
    }

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        Object arg1 = TypeStructureTransformer.getInstance().transform(arg0);
        StringBuffer sb = new StringBuffer();
        sb.append("<html><body><pre>");
        format(arg1,sb);
        sb.append("</pre</body></html>");
        return sb.toString();
    }
    
    private void format(Object arg1,StringBuffer buff) {
        if (arg1 == null) {
            buff.append(arg1); // let buff work out how to represent nulls.
            return;
        }
        if (arg1 instanceof Vector) {
            List v = (Vector)arg1;
            buff.append("<ul>");
            for (Iterator i = v.iterator(); i.hasNext(); ) {
                buff.append("<li>");
                format(i.next(),buff);
            }
            buff.append("</ul>");
            return;
        }
        if (arg1 instanceof Hashtable) {
            Map m = (Hashtable)arg1;
            buff.append("<dl>");
            for (Iterator i = m.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry e = (Map.Entry)i.next();
                buff.append("<dt>");
                buff.append(e.getKey());
                buff.append("<dd>");
                format(e.getValue(),buff);
            }
            buff.append("</dl>");
            return;
        }
        
        buff.append(arg1);
        return;
        
    }
    

    public static final Transformer getInstance() {
        return theInstance;
    }
    
    private static final Transformer theInstance = new DefaultHtmlTransformer();
    
}


/* 
$Log: DefaultHtmlTransformer.java,v $
Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/