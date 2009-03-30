/*$Id: Descriptor.java,v 1.5 2009/03/30 15:02:54 nw Exp $
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

import java.io.Serializable;

/** Abstract datastructure: Describes some portion of the AstroRuntime
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk

 *
 */
public abstract class Descriptor implements Serializable {

    /** Construct a new Descriptor
     * @exclude
     */
    public Descriptor() {
        super();
    }
    /** name of the entity being described */
    protected String name = "unknown";
    /** description of hte entity */
    protected String description = "none given";
    
    /** the description */
    public String getDescription() {
        return this.description;
    }
    /** @exclude */
    public void setDescription(final String description) {
        this.description = description;
    }
    /** the name */
    public String getName() {
        return this.name;
    }

    /** @exclude */
    public void setName(final String name) {
        this.name = name;
    }
    
    private boolean excluded;
    

        
    /** Whether the entity should be excluded from documentation
     * @return the excluded
     */
    public boolean isExcluded() {
        return this.excluded;
    }
    /** @exclude
     * @param excluded the excluded to set
     */
    public void setExcluded(final boolean excluded) {
        this.excluded = excluded;
    }
    /** @exclude */
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append('\n');
        if (excluded) {
            buffer.append("excluded");
            buffer.append('\n');
        }
        buffer.append(" description: ");
        buffer.append(description);
        buffer.append('\n');        
        return buffer.toString();
    }
    /**
     * @exclude
     * Returns true if other object is a descriptor of the same class, with the same name.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        final Descriptor castedObj = (Descriptor) o;
        return this.name == null ? castedObj.name == null : this.name.equals(castedObj.name);
         
    }
}


/* 
$Log: Descriptor.java,v $
Revision 1.5  2009/03/30 15:02:54  nw
Added override annotations.

Revision 1.4  2008/12/22 18:13:26  nw
added field to model exclusion

Revision 1.3  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.2  2007/01/24 14:04:45  nw
updated my email address

Revision 1.1  2006/06/02 00:17:10  nw
Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.

Revision 1.3  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.2.60.3  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.2.60.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.2.60.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.4  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:13  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/06 15:04:10  nw
added new front end - more modern, with lots if icons.

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/