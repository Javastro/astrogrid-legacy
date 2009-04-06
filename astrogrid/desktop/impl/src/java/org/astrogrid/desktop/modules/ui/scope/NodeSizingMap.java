/*$Id: NodeSizingMap.java,v 1.3 2009/04/06 11:43:19 nw Exp $
 * Created on 27-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import java.util.Collections;
import java.util.Map;

/**
 * Maintains associations between node labels and sizing information.
 * 
 * an encapuslation of the map type previously used.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 *
 */
public class NodeSizingMap {

    /** Construct a new NodeSizingMap
     * 
     */
    public NodeSizingMap() {
        super();
        m =  Collections.synchronizedMap(new java.util.TreeMap<Object, NodeSizing>());
    }
    
    private final Map<Object, NodeSizing> m;

    
    /** adds a new offset value into the map
     *  - it won't have any node sizing associated with it until 
     *  {@link #setNodeSizing} is called.
     * @param offset
     */
    public void addOffset(String offset) {
        m.put(offset,null);
    }
    
    public void setNodeSizing() {
        int size = m.size();
        int breakdown = size/3;
        int increment = 0;
        Object []keys = m.keySet().toArray();
        for(int i = 0;i < 3;i++) {
            for(int j = increment;j < breakdown + increment;j++) {
                m.put(keys[j],NodeSizing.NODE_SIZING_ARRAY[i]);                  
            }
            increment += breakdown;                
        }
        // don't know what this does.
        if(keys.length > 0)
            m.put(keys[keys.length - 1], NodeSizing.LARGE_NODE);
    }      
    
    /** gets node sizing object associated with an offset.
     * <p>
     * will always return null unless {@link #setNodeSizing} has been called once first
     * @param offset string label
     * @return the associated node sizing, or null if not found, or if label is null
     */
    public NodeSizing getNodeSizing(String offset) {
        if (offset == null) {
            return null;
        }
        return m.get(offset);
    }

    
    
    /** remove all offsets and sizings from the map. */
    public void clear() {
        m.clear();
    }
}


/* 
$Log: NodeSizingMap.java,v $
Revision 1.3  2009/04/06 11:43:19  nw
Complete - taskConvert all to generics.

Incomplete - taskVOSpace VFS integration

Revision 1.2  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/