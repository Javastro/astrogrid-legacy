/*$Id: IDTransformer.java,v 1.1 2005/02/22 01:10:31 nw Exp $
 * Created on 21-Feb-2005
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

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 *
 */
public class IDTransformer implements Transformer {

    /** Construct a new IDTransformer
     * 
     */
    public IDTransformer() {
        super();
    }

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        return arg0;
    }
    
    private static final Transformer theInstance = new IDTransformer();
    public static final Transformer getInstance() {
        return theInstance;
    }

}


/* 
$Log: IDTransformer.java,v $
Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/