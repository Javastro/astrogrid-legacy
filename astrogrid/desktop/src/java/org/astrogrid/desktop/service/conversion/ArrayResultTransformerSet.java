/*$Id: ArrayResultTransformerSet.java,v 1.1 2005/02/22 01:10:31 nw Exp $
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

/** Result transformer set that will return an array of strings in suitable forms.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 * @todo probably more here..
 */
public class ArrayResultTransformerSet extends StringResultTransformerSet {

    /** Construct a new StringArrayResultTransformerSet
     * 
     */
    protected ArrayResultTransformerSet() {
        super();
        super.setXmlrpcType(ARRAY);
    }
    
    public static ResultTransformerSet getInstance() {
        return theInstance;
    }
    
    private static ResultTransformerSet theInstance = new ArrayResultTransformerSet(); 


}


/* 
$Log: ArrayResultTransformerSet.java,v $
Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/