/*$Id: CastorXmlTransformer.java,v 1.1 2005/02/21 11:25:07 nw Exp $
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
import org.exolab.castor.xml.Marshaller;

import java.io.StringWriter;

/** Implementation of transformation to XML for castor-generated objects.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public class CastorXmlTransformer implements Transformer {

    /** Construct a new CastorXmlTransformer
     * 
     */
    private CastorXmlTransformer() {
        super();
    }

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        try {
            StringWriter sw = new StringWriter();
            Marshaller.marshal(arg0,sw);
            return sw.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    public static final Transformer getInstance() {
        return theInstance;
    }
    private static final Transformer theInstance = new CastorXmlTransformer();
       

}


/* 
$Log: CastorXmlTransformer.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/