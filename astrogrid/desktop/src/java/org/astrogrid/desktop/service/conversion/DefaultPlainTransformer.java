/*$Id: DefaultPlainTransformer.java,v 1.1 2005/02/21 11:25:07 nw Exp $
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
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;

/** Default implementation of transformer to plaintext.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public class DefaultPlainTransformer implements Transformer {

    /** Construct a new DefaultPlainTransformer
     * 
     */
    private    DefaultPlainTransformer() {
        super();
    }

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        return ReflectionToStringBuilder.toString(arg0,plainStyle);
    }
    
    private static final Transformer theInstance = new DefaultPlainTransformer();
    public static final Transformer getInstance() {
        return theInstance;
    }
    
    protected final static StandardToStringStyle plainStyle = new StandardToStringStyle();
    static {
        plainStyle.setArraySeparator("\n");
        plainStyle.setArrayEnd("");
        plainStyle.setArrayStart("");
        plainStyle.setContentEnd("\n");
        plainStyle.setContentStart("");
        plainStyle.setFieldSeparator("\t");
        plainStyle.setUseClassName(false);
        plainStyle.setUseFieldNames(false);
        plainStyle.setUseIdentityHashCode(false);
        plainStyle.setArrayContentDetail(true);
        plainStyle.setDefaultFullDetail(true);
    }

}


/* 
$Log: DefaultPlainTransformer.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/