/*$Id: Translator.java,v 1.3 2004/01/15 14:49:47 nw Exp $
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
 * <p>
 * Maps from a query document to some intermediate form.
 * <p>
 * This interface defines two methods - {@link #translate} which performs the conversion between query document and intermediate form -
 *  and {@link #getResultType}. which is used to sanity-check the plugin. This method
return the {@link java.lang.Class} of the intermediate representation objects that are produced by the <tt>translate</tt> method.
 i.e. the following must be true
<code><pre><tt>
  Translator trans;
  Element e;
  trans.getResultType().equals(trans.translate(e));
</tt></pre></code>
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
    
    /** used to assert the type returned by the {@link #translate} method */
    public Class getResultType();
    
    

}


/* 
$Log: Translator.java,v $
Revision 1.3  2004/01/15 14:49:47  nw
improved documentation

Revision 1.2  2004/01/15 11:58:36  nw
improved documentation

Revision 1.1  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.
 
*/