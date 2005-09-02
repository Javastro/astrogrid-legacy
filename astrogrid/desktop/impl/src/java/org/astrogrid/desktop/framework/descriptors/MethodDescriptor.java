/*$Id: MethodDescriptor.java,v 1.2 2005/09/02 14:03:34 nw Exp $
 * Created on 10-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework.descriptors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** descriptor for a method.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class MethodDescriptor extends Descriptor {

    /** Construct a new MethodDescriptor
     * 
     */
    public MethodDescriptor() {
        super();
    }
    
    protected ValueDescriptor returnValue = new ValueDescriptor();

    
    protected final List parameterValues = new ArrayList();
        
    /** get descriptor for the return value of this method */
    public ValueDescriptor getReturnValue() {
        return this.returnValue;
    }
    public void setReturnValue(ValueDescriptor returnValue) {
        this.returnValue = returnValue;
    }
    
    public void addParameter(ValueDescriptor desc) {
        parameterValues.add(desc);
    }
    
    /** iterate over parameter descriptors  
     * 
     * @return an iterator over {@link ValueDescriptor} objects
     */
    public Iterator parameterIterator() {
        return parameterValues.iterator();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[MethodDescriptor:");
        buffer.append(" returnValue: ");
        buffer.append(returnValue);
        buffer.append('\n');        
        buffer.append(" parameterValues: ");
        buffer.append(parameterValues);
        buffer.append('\n');        
        buffer.append(super.toString());
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: MethodDescriptor.java,v $
Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:13  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/