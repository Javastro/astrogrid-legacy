/*$Id: DynamicVisitor.java,v 1.1 2003/11/14 00:36:40 mch Exp $
 * Created on 29-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.adql;
/** Dynamic DynamicVisitor Pattern interface.
 * <p>
 * To process / traverse a ADQL query object tree, implement this interface and pass to the
 * {@link QOM#acceptBottomUp} or {@link QOM#acceptTopDown} method of the root object in the object tree.
 * <p>
 * <h3>Use</h3>
 * Implementations of this interface will be passed up and down query object trees. At each element in turn, a <tt>visit</tt>
 * method of the class is called. Implementations may provide more type-specific overloadings of the <tt>visit</tt> method.
 * If a type-specific method exists for an element type, it will be called. Otherwise, the closest matching method will be selected - 
 * so a visit method for any superclass, and finally the default {@link #visit(QOM)} method.
 * 
 * <h3>Background</h3>
 * This class provides a variant of the Visitor pattern. 
 * In the traditional visitor pattern, all classes in the object tree implement an interface containing <tt>accept</tt> 
 * methods. However, each class provides a different implementation that double-dispatches to the corresponding
 * <tt>visit</tt> method of the Visitor object.
 * <p>
 * This is not possible for the QOM - it is machine-generated code, of a schema that is likely to change in the future. So
 * we don't want to be editing / addng stuff the the generated code, as it will be lost at the next regeneration.
 * <p>
 * It might be possible to implement the traditional visitor pattern by extending each generated class, and placing
 * additional methods in the derived classes. But the serialization / deserialization to XML routines expect and return
 * instances of the base class, not these derived classes.
 * <p>
 * So we've come up with a dynamic implementation of the visitor pattern, where the traversal code occurs once 
 * in the base class of all the generated classes. It uses the Castor meta-data Descriptor classes for each generated class
 * to compute the child elements of it, and then calls the appropriate method on the visitor object via reflection.
 * <p>
 * In this way, the implementation is safe from accidental deletion by regenerating the code, and in addition will automagigically
 * work for any future schema changes. 
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Aug-2003
 * @todo replace dynamic mechanism with a static traversal; once the schema design is finalized. Will require
 * changing the visit method type from <tt>visit(Type t)</tt> to <tt>visitType(Type t)</tt> to prevent overloading problems. 
 *
 */
public interface DynamicVisitor {

    public void visit(QOM q) throws Exception;
}


/* 
$Log: DynamicVisitor.java,v $
Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.1  2003/10/14 12:43:42  nw
moved from parent datacenter project.

Revision 1.2  2003/09/03 13:42:31  nw
documentation fixes

Revision 1.1  2003/09/02 14:45:08  nw
interface for visitor pattern
 
*/