/*$Id: MethodDescriptor.java,v 1.4 2009/03/30 15:02:54 nw Exp $
 * Created on 10-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.builtin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.acr.system.ApiHelp;

/** Describes a method (function) belonging to an AR Service
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 

 * @see ApiHelp
 *
 */
public class MethodDescriptor extends Descriptor {

    /** Construct a new MethodDescriptor
     * @exclude
     */
    public MethodDescriptor() {
        super();
    }
    
    protected ValueDescriptor returnValue = new ValueDescriptor();

    
    protected final List parameterValues = new ArrayList();
        
    /** description of the return value of this method */
    public ValueDescriptor getReturnValue() {
        return this.returnValue;
    }
    
    
    /** @exclude*/
    public void setReturnValue(final ValueDescriptor returnValue) {
        this.returnValue = returnValue;
    }
    
    /** @exclude */
    public void addParameter(final ValueDescriptor desc) {
        parameterValues.add(desc);
    }
    
    /** iterate over descriptions of the parameters of this method 
     * 
     * @return an iterator over {@link ValueDescriptor} objects
     */
    public Iterator parameterIterator() {
        return parameterValues.iterator();
    }

    /** list the parameters to this method */
    public ValueDescriptor[] getParameters() {
    	return (ValueDescriptor[])parameterValues.toArray(new ValueDescriptor[]{});
    }
    	
    /** @exclude */
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
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
Revision 1.4  2009/03/30 15:02:54  nw
Added override annotations.

Revision 1.3  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.2  2007/01/24 14:04:46  nw
updated my email address

Revision 1.1  2006/06/02 00:17:10  nw
Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.

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