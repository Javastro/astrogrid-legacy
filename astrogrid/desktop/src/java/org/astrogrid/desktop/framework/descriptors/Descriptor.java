/*$Id: Descriptor.java,v 1.4 2005/08/05 11:46:56 nw Exp $
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

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** Base class for all descriptors;
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class Descriptor implements Serializable {

    /** Construct a new Descriptor
     * 
     */
    public Descriptor() {
        super();
    }
    /** name of the entity being described */
    protected String name = "unknown";
    /** description of hte entity */
    protected String description = "none given";
    /** container for configuration properties for the entity */
    protected final Map properties = new HashMap();
    
    
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return this.name;
    }
    /** helper method - returns {@link #getName} capitalized. later will break with spaces too */
    public String getUIName() {
        return StringUtils.capitalize(name);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    /** retrive an arbitrary attribute from the descriptor */
    public String getProperty(String key) {
        Object o = this.properties.get(key);
        return o == null? null : o.toString();
    }
    
    /** retrieve an arbitrary attribute wrapped in &lt;value&gt; tags - sutable for further parsing */
    public String getPropertyDocument(String key) {
        String s = getProperty(key);
        return s == null ? null  : "<value>" + s + "</value>";
    }
    
    public void setProperty(String key,String value) {
        this.properties.put(key,value);
    }
    
    /** iterate over all properties of the descriptor */
    public Iterator propertyIterator() {
        return properties.values().iterator();
    }
        
        
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append('\n');
        buffer.append(" description: ");
        buffer.append(description);
        buffer.append('\n');        
        buffer.append(" properties: ");
        buffer.append(properties);
        buffer.append('\n');        
        return buffer.toString();
    }
    /**
     * Returns true if other object is a descriptor of the same class, with the same name.
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        Descriptor castedObj = (Descriptor) o;
        return this.name == null ? castedObj.name == null : this.name.equals(castedObj.name);
         
    }
}


/* 
$Log: Descriptor.java,v $
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