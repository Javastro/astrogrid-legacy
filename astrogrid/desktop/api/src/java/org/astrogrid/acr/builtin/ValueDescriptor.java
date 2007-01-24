/*$Id: ValueDescriptor.java,v 1.2 2007/01/24 14:04:46 nw Exp $
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

/** Descriptor for a value
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk
 * @since 2.2
 *
 */
public class ValueDescriptor extends Descriptor {

    /** Construct a new ValueDescriptor
     * 
     */
    public ValueDescriptor() {
        super();
    }
    private Class type;
    private String uitype;
 
    /** user-readable type information */
    public String getUitype() {
        return this.uitype;
    }

    /** used internally */
    public void setUitype(String uitype) {
        this.uitype = uitype;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ValueDescriptor:");
        buffer.append("\nuitype: ");
        buffer.append(uitype);
        buffer.append("\ntype: ");
        buffer.append(type != null ? type.getName() : "");
        buffer.append('\n');        
        buffer.append(super.toString());
        buffer.append("]");
        return buffer.toString();
    }

    /** the type of this value */
    public Class getType() {
        return this.type;
    }

    /** used internally */
    public void setType(Class type) {
        this.type = type;
    }
}


/* 
$Log: ValueDescriptor.java,v $
Revision 1.2  2007/01/24 14:04:46  nw
updated my email address

Revision 1.1  2006/06/02 00:17:10  nw
Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.68.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/13 12:59:13  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/