/*$Id: CompoundNodeList.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
 * Created on 11-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.client.registry;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Vector;


/**
 * A component to aggregate the contents of more than one NodeList.
 * @modified nww broke out of RegistryHelper. 
 */
class CompoundNodeList implements NodeList {
    /**
     * Our internal vector of Nodes.
     *  
     */
    private Vector vector = new Vector();

    /**
     * Public constructor.
     *  
     */
    public CompoundNodeList() {
    }

    /**
     * Add the contents of a NodeList.
     *  
     */
    public void add(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            vector.add(node);
        }
    }

    /**
     * Get the total numner of nodes.
     *  
     */
    public int getLength() {
        return vector.size();
    }

    /**
     * Get a specific node.
     *  
     */
    public Node item(int index) {
        return (Node) vector.get(index);
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CompoundNodeList:");
        buffer.append(" vector: ");
        buffer.append(vector);
        buffer.append("]");
        return buffer.toString();
    }
}

/* 
$Log: CompoundNodeList.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/02/11 14:28:28  nw
refactored, split out candidate classes.
 
*/