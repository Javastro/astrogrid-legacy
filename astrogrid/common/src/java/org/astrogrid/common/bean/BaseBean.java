/*$Id: BaseBean.java,v 1.1 2004/02/10 17:30:57 nw Exp $
 * Created on 10-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.common.bean;

import org.apache.commons.jxpath.JXPathContext;

import java.util.Iterator;

/** Base class for generated object-model beans.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Feb-2004
 * @see http://jakarta.apache.org/commons/jxpath/apidocs/org/apache/commons/jxpath/JXPathContext.html
 * @see BaseBeanTest
 */
public abstract class BaseBean {
    /** Construct a new BaseBean
     * 
     */
    public BaseBean() {
        super();
    }
    
    private transient JXPathContext cxt;
    /** access the jxpath context object for this object (lazily initialized)*/
    public final JXPathContext getJXPathContext() {
        if (cxt == null) {
            synchronized(this) {
                if (cxt == null) {
                    this.cxt = JXPathContext.newContext(this);
                    this.cxt.setLenient(true);
                }
            }
        }
        return cxt;
    } 
    
    /** execute an xpath query, using thiis object as the root of an object graph
     * 
     * @param xpath query to execute
     * @return object found, or null
     */
    public final Object findXPathValue(String xpath) {
        return this.getJXPathContext().getValue(xpath);
    }
    /** execute an expath query, using this object as the root of an object graph
     * 
     * @param xpath query to execute
     * @return iterator containing series of objects that match this query
     */
    public final Iterator findXPathIterator(String xpath) {
        return this.getJXPathContext().iterate(xpath);
    }
    
    
}


/* 
$Log: BaseBean.java,v $
Revision 1.1  2004/02/10 17:30:57  nw
added base class for castor-generated object models that provides xpath querying
 
*/