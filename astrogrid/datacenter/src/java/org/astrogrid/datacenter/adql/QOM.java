/*$Id: QOM.java,v 1.3 2003/09/08 09:34:56 nw Exp $
 * Created on 28-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.adql;

import java.io.IOException;
import java.io.Writer;

import org.exolab.castor.xml.CastorException;
import org.xml.sax.ContentHandler;
import org.exolab.castor.mapping.*;
/** interface that describes commonality between all classes in the  Query Object Model.
 * <p>
 * Defines the marshalling / unmarshalling to XML routines. - 
 *  * <p>
 * Also defines a dynamic DynamicVisitor pattern for the QOM. See {@link DynamicVisitor} for details 
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Aug-2003
 *
 */
public interface QOM {
    /**
     * wrapper around {@link #validate}
     * @return true if <tt>validate()</tt> succeeds, false if it throws a <tt>CastorException</tt>
     * @see #validate
     */
    public boolean isValid() ;
    /**
     *  write xml representation out to a writer
     * @param w the writer to output to
     * @throws CastorException on error
     */
    public void marshal(Writer w) throws CastorException;
    /**
     * write xml representation out to a content handler
     * @param c handler to write out to
     * @throws CastorException on error
     */
    public void marshal(ContentHandler c) throws CastorException, IOException;
    /** 
     * verify this query object would generate an xml representation valid against the ADQL xml schema
     * @throws CastorException if validation fails.
     */
    public void validate() throws CastorException;
    
    /**
     * Entry point for a dynamic visitor to process the object tree in a top down fashion (i.e. visit the
     * parent before it's children)
     * @param v the visitor object
     * @throws Exception if an error occurs during traversal
     * @todo could split the exceptions into those that may be thrown in traversal, and user exceptions.
     * @see DynamicVisitor
     */
    public void acceptTopDown(DynamicVisitor v) throws TraversalException, ProcessingException;
    
    /**Entry point for a dynamic visitor to process the object tree in a bottom-up manner (i.e. visit the child
     * nodes before their parents).
     * 
     * @param v visitor object
     * @throws Exception if an error occurs during traversal.
     * @see DynamicVisitor
     */
    public void acceptBottomUp(DynamicVisitor v) throws TraversalException, ProcessingException;

/**
 * 
 * @param v
 * @throws Exception
 */
    public void callVisitor(DynamicVisitor v) throws TraversalException, ProcessingException;

   
    /** compute the children elements of this element 
     * @return array of child QOM objects (all non-null child elements of this element)
     * @todo - is it possible add an intermediate caching of results, so reflection only needs to be done one.
     *  
     */
    public QOM[] getChildren() throws TraversalException; 
}


/* 
$Log: QOM.java,v $
Revision 1.3  2003/09/08 09:34:56  nw
Improved exception handling

Revision 1.2  2003/09/02 14:44:55  nw
addedd interface to visitor pattern

Revision 1.1  2003/08/28 15:27:54  nw
added ADQL object model.
 
*/