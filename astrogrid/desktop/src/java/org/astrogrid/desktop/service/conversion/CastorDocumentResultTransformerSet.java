/*$Id: CastorDocumentResultTransformerSet.java,v 1.1 2005/02/22 01:10:31 nw Exp $
 * Created on 21-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service.conversion;

import org.astrogrid.desktop.service.annotation.ResultTransformerSet;
import org.astrogrid.desktop.service.conversion.transformers.TypeStructureTransformer;

/** @todo add stylesheed to render xml inito html.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 *
 */
public class CastorDocumentResultTransformerSet extends StringResultTransformerSet {

    /** Construct a new CastorDocumentResultTransformerSet
     * 
     */
    protected CastorDocumentResultTransformerSet() {
        super();
        setXmlrpcTransformer(TypeStructureTransformer.getInstance());
        setPlainTransformer(TypeStructureTransformer.getInstance());
        setHtmlTransformer(TypeStructureTransformer.getInstance());
    }
    
    public static ResultTransformerSet getInstance() {
        return theInstance;
    }

    private static ResultTransformerSet theInstance = new CastorDocumentResultTransformerSet(); 

}


/* 
$Log: CastorDocumentResultTransformerSet.java,v $
Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/