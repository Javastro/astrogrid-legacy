/*$Id: DefaultXmlTransformer.java,v 1.1 2005/02/22 01:10:31 nw Exp $
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

import com.thoughtworks.xstream.XStream;

/** Default implementation of transformer to xml. uses xstream.
 * @todo unsure whether to do away with this altogether, and just use xmlrpc serialization.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public class DefaultXmlTransformer implements Transformer {

    /** Construct a new DefaultXmlTransformer
     * 
     */
    private DefaultXmlTransformer() {
        super();
    }

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        Object arg1 = TypeStructureTransformer.getInstance().transform(arg0);
        return xstream.toXML(arg1);
    }

    protected final XStream xstream = new XStream();
    private static final Transformer theInstance = new DefaultXmlTransformer();
    public static final Transformer getInstance() {
        return theInstance;
    }
}


/* 
$Log: DefaultXmlTransformer.java,v $
Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/