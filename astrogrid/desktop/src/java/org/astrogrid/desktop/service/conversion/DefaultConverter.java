/*$Id: DefaultConverter.java,v 1.1 2005/02/21 11:25:07 nw Exp $
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

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

/** Default implementation of the converter interface - uses convertUtils.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public class DefaultConverter implements Converter {

    /** Construct a new ConvertUtilsTransformer
     * 
     */
    private DefaultConverter() {
        super();
    }

    
    public static Converter getInstance() {
        return theInstance;
    }
    
    private static final DefaultConverter theInstance = new DefaultConverter();

    /**
     * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
     */
    public Object convert(Class c, Object o) {
        return ConvertUtils.lookup(c).convert(c,o);
    }

}


/* 
$Log: DefaultConverter.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/