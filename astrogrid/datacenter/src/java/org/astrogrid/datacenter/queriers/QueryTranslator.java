/*$Id: QueryTranslator.java,v 1.6 2003/09/26 14:28:24 nw Exp $
 * Created on 02-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers;

import java.util.HashSet;
import java.util.Set;

import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.DynamicVisitor;
import org.astrogrid.datacenter.adql.QOM;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Element;

/** Abstract class that provides mechanism for traversing a ADQL tree, and maintaining a stack of translations.
 * Extend this class to implement DynamicVisitor to provide a concrete translator.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2003
 * @todo  optimize translation frame creation.
 * @see org.astrogrid.datacenter.queriers.sql.SqlQueryTranslator 
 * @see TranslationFrame
 */
public abstract  class QueryTranslator implements DynamicVisitor {
    /** stack of translation frames
     * declare methods that require a translation frame via {@link #requiresFrame} */
    protected TranslationFrame.Stack stack = new TranslationFrame.Stack();



    /** entry point to the translator - pass in an element containing the
    * XML query
    * and it returns the query as an SQL an SQL SELECT statment
    */
   public String translate(Element query) throws ADQLException, MarshalException, ValidationException
   {
      QOM qom = ADQLUtils.unmarshalSelect(query);
      return translate(qom);
   }

   /**
     * pass in any Query object to get its string equivalent.
     *  i.e. to translate to a SQL SELECT query, pass in a {@link org.astrogrid.datacenter.adql.generated.Select}
     * @param q query object to translate
     * @return string translation of this object
     * @throws ADQLException if error occurs (unlikely, indicates a falure of the traversal machinery)
     */
   public String translate(QOM q) throws ADQLException
   {
        stack.pushNew();
        traverse(q);
        // result will be a single result in the top frame on the stack.
        TranslationFrame f =  stack.pop();
        return f.toString();
    }
/**
 * traverse one node in the QOM tree
 * @param q the node to process
 * @throws ADQLException if error occurs.
 */
    protected void traverse(QOM q) throws ADQLException {
        /* similar to the acceptBottomUp, etc methods in AbstractQOM
         access the children of this node (i.e. the QOM elements it links to)
         if required, push a new frame onto the stack - this is to be
         used by the node's children - will add results to this frame, which can then be accessed by the parent.
         then recurse to all children of the node
         finally, call the visitor method for the current node.
         */
        QOM[] children = q.getChildren();
        if (isInternalProcessingNode(q.getClass())) {
            stack.pushNew();
        }
        for (int i =0;i < children.length; i++) {
            traverse(children[i]);
        }
        q.callVisitor(this);

    }

    private Set internalNodeSet = new HashSet();
    /**
     * check wheter this node type requires a new frame
     * @param q the class of node
     * @return true if this node type, or a supertype of this node type, has previously been declared to require a
     * stack frame via {@link #requiresFrame)
     */
    protected boolean isInternalProcessingNode(Class q) {
        do {
            if (internalNodeSet.contains(q)) {
                return true;
            }
            q = q.getSuperclass();
        } while(q != null);
        return false;
    }
    /** declare that this node type requires a new translation frame
     *
     * @param c the node type to provide frames for,
     */
    protected void requiresFrame(Class c) {
        internalNodeSet.add(c);
    }

}


/*
$Log: QueryTranslator.java,v $
Revision 1.6  2003/09/26 14:28:24  nw
documentation tweak

Revision 1.5  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.4  2003/09/08 09:35:34  nw
now returns more descriptive exception types

Revision 1.3  2003/09/07 18:56:42  mch
Moved ADQL package dependency to QueryTranslator only

Revision 1.2  2003/09/03 13:47:30  nw
improved documentaiton.
split existing MySQLQueryTranslator into a vanilla-SQL
version, and MySQL specific part.

Revision 1.1  2003/09/02 14:46:13  nw
base class of a sql translation

*/
