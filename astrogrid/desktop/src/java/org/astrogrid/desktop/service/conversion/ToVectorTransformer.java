/*$Id: ToVectorTransformer.java,v 1.1 2005/02/21 11:25:07 nw Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.collections.Transformer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

/** Convert any kind of collection or array to a vector - for use with the xmlrpclib.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public class ToVectorTransformer implements Transformer {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ToVectorTransformer.class);

    /** Construct a new ToVectorTransformer
     * 
     */
    public ToVectorTransformer() {
        super();
    }

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        if (arg0 instanceof Collection) {
            return new Vector((Collection)arg0);
        } 
        if (arg0 instanceof Object[]) {
            return new Vector(Arrays.asList((Object[])arg0));
        }
        throw new IllegalArgumentException("Don't know how to convert objects of type " + arg0.getClass().getName());
          }
    
    private static final Transformer theInstance = new ToVectorTransformer();
    
    public static final Transformer getInstance() {
        return theInstance;
    }

}


/* 
$Log: ToVectorTransformer.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/