/*$Id: ToHashtableTransformer.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service.conversion;

import org.apache.commons.collections.Transformer;

import java.util.Hashtable;
import java.util.Map;

/** Transform map-type things into hash tables - for xmlrpc libe.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public class ToHashtableTransformer implements Transformer {

    /** Construct a new ToHashtableTransformer
     * 
     */
    public ToHashtableTransformer() {
        super();
    }

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        if (arg0 instanceof Map) {
            return new Hashtable((Map)arg0);
        }
        throw new IllegalArgumentException("Don't know how to convert objects of type " + arg0.getClass().getName());
           }
    
    private static final Transformer theInstance = new ToHashtableTransformer();
    public static final Transformer getInstance() {
        return theInstance;
    }

}


/* 
$Log: ToHashtableTransformer.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/