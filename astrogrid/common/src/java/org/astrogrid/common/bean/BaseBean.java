/*$Id: BaseBean.java,v 1.3 2004/03/05 11:05:08 nw Exp $
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

import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.Functions;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;

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
    /** access the jxpath context object for this object (lazily initialized)
     * @modified NWW - renamed, to avoid using 'get*' naming convention - otherwise
     * is accessed when traversing the tree, which leads to recursion and out of memory errors.*/
    public final synchronized JXPathContext accessJXPathContext() {
        if (cxt == null) {
                    this.cxt = JXPathContext.newContext(this);
                    this.cxt.setLenient(true);
                    FunctionLibrary lib = new FunctionLibrary();
                    this.cxt.setFunctions(lib);                    
                    lib.addFunctions(new ClassFunctions(BaseBean.Fns.class,"fn"));         
        }
        return cxt;
    } 
    /** add a new funciton library to the xpath interpreter */
    public final void addFunctions(Functions fn) {
        ((FunctionLibrary)accessJXPathContext().getFunctions()).addFunctions(fn);
    }
    /** execute an xpath query, using thiis object as the root of an object graph
     * 
     * @param xpath query to execute
     * @return object found, or null
     */
    public final Object findXPathValue(String xpath) {
        return this.accessJXPathContext().getValue(xpath);
    }
    /** execute an expath query, using this object as the root of an object graph
     * 
     * @param xpath query to execute
     * @return iterator containing series of objects that match this query
     */
    public final Iterator findXPathIterator(String xpath) {
        return this.accessJXPathContext().iterate(xpath);
    }
    
    /** return the xpath for a particular object.
     * 
     * @param target object to return xpath for. must be in object tree (or a similar object equivalent under equals())
     * @return xpath for that object, or null
     *
     */
    public final String getXPathFor(Object target) {
        // add variable we're looking for.
        JXPathContext cxt = this.accessJXPathContext();
        cxt.getVariables().declareVariable("target",target);
        Pointer p  = cxt.getPointer("//*[fn:matchTarget()]");
        // tidyup  
        cxt.getVariables().undeclareVariable("target");
        // sanity check..
        return p != null && p.getNode() != null ? p.asPath() : null;        
    }

    /** class of functions added to jxpath context
     * <p>has to be public, else lib is not callable from jxpath */
    public static class Fns {
        /** returns true if context node is equal to variable target.
         * i.e. $target.equals(.)
         * @param ctxt
         * @return
         */ 
        public static boolean matchTarget(ExpressionContext ctxt) {
            Object candidate = ctxt.getContextNodePointer().getValue();
            Object target = ctxt.getJXPathContext().getVariables().getVariable("target");
            return target.equals(candidate);
        }
        
        /** access the name of the class of the context node - useful for finding nodes by class 
         * e.g. //*[functions:type() = 'org.astrogrid.Blob'] 
         *  */
        public static String type(ExpressionContext ctxt) {
            Pointer ptr = ctxt.getContextNodePointer();
            if (ptr == null) {
                return "null";
            }
            Object candidate = ctxt.getContextNodePointer().getValue();
            if (candidate == null) {
                return "null";
            }
            return candidate.getClass().getName();
        }

    }    

    
}


/* 
$Log: BaseBean.java,v $
Revision 1.3  2004/03/05 11:05:08  nw
added ability to plug in new xpath function sets

Revision 1.2  2004/03/01 01:26:12  nw
added method to find xpath for a given object in the tree
- can be used to implement activity keys

Revision 1.1  2004/02/10 17:30:57  nw
added base class for castor-generated object models that provides xpath querying
 
*/