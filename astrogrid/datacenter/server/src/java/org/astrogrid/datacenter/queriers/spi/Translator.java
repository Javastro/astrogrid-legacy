/*$Id: Translator.java,v 1.2 2004/01/15 11:58:36 nw Exp $
 * Created on 26-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.spi;

import org.w3c.dom.Element;

/** Interface describing a query language translator 
 * 
 * <p>
 * Maps from a query document to some intermediate form.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2003
 *
 */
public interface Translator {
    /** translate a query document into an intermediate form
     * 
     * @param e query document
     * @return a parsed / translated representation 
     */
    public Object translate(Element e) throws Exception ;
    
    /** used to assert the type returned by the translator */
    public Class getResultType();
    
    

}


/* 
$Log: Translator.java,v $
Revision 1.2  2004/01/15 11:58:36  nw
improved documentation

Revision 1.1  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.
 
*/