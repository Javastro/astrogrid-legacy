/*$Id: ValueDescriptor.java,v 1.1 2005/08/11 10:15:00 nw Exp $
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

/** Descriptor for a value
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class ValueDescriptor extends Descriptor {

    /** Construct a new ValueDescriptor
     * 
     */
    public ValueDescriptor() {
        super();
    }
 
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ValueDescriptor:");
        buffer.append('\n');        
        buffer.append(super.toString());
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: ValueDescriptor.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/13 12:59:13  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/