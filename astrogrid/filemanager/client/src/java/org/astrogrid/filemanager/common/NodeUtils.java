/*$Id: NodeUtils.java,v 1.2 2005/03/11 13:37:05 clq2 Exp $
 * Created on 17-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.common;

import org.astrogrid.filemanager.common.Attribute;
import org.astrogrid.filemanager.common.Child;
import org.astrogrid.filemanager.common.DataLocation;
import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeTypes;

import org.apache.axis.types.URI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** static class of helper methods for working with nodes, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2005
 *
 */
public class NodeUtils {

    /** Construct a new NodeUtils
     * 
     */
    private NodeUtils() {
        super();
    }

    /** Factory to create a new node.
     * @param parentIvorn parent for the new node. (parent is left unaltered).
     * @param name name of the new node
     * @param type folder or file
     * @param newIvorn the identifier to create the new node as
     * @param defaultStore default storepoint to use for this node.
     * @return a new node.
     */
    public static Node newNode(NodeIvorn parentIvorn, NodeName name, NodeTypes type, NodeIvorn newIvorn, URI defaultStore) {
        Node newNode = new Node();
        newNode.setName(name);
        newNode.setCreateDate(Calendar.getInstance());
        newNode.setAttributes(new Attribute[0]);
        newNode.setChild(new Child[0]);
        newNode.setIvorn(newIvorn);
        newNode.setModifyDate(newNode.getCreateDate());
        newNode.setParent(parentIvorn);
        newNode.setSize(new Long(0));
        newNode.setType(type);
        DataLocation location = new DataLocation();
        location.setUri(defaultStore);
        newNode.setLocation(location);
        return newNode;
    }

    /** create a carbon-copy of a node.
     * 
     * can't use clone(), as doesn't implement cloneable.
     * implements serializable though - so serialize, then deserialize to get a new node.
     * 
     * @param n
     * @return
     */
    public static Node cloneNode(Node n) {
        try {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(n);
        os.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream is = new ObjectInputStream(bis);
        Node result = (Node)is.readObject();
        return result;
        } catch (Exception e) {
            throw new RuntimeException("Fauled to clone node",e);
        }
    }

    /** find the ivorn associated with a named child.
     * @param bean node to look in 
     * @param childName name of child to hunt for
     * @return the id of the child if found, else null.
     */
    public static NodeIvorn findChild(Node bean,NodeName childName) {
        Child[] children = bean.getChild();
        if (children == null) {
            return null;
        }
        for (int i = 0; i < children.length; i++) {
            if (children[i].getName().equals(childName)) {
                return children[i].getIvorn();
            }            
        }
        return null;
    }
    
    /** add a child to the children of a node.
     * <p>
     * 
     * precondition: <code>findChild == null</code>
     * @param parent node to add to.
     * @param newNodeName name of new child
     * @param newNodeIvorn unique id of new child.
     */
    public static void addChild(Node parent, NodeName newNodeName, NodeIvorn newNodeIvorn) {
        Child child = new Child();
        child.setIvorn(newNodeIvorn);
        child.setName(newNodeName);
        Child[] children = parent.getChild();
        if (children == null) {
            children = new Child[0];
        }
        Child[] newChildren = new Child[children.length + 1];
        System.arraycopy(children,0,newChildren,0,children.length);
        newChildren[children.length] = child; 
        parent.setChild(newChildren);        
       
    }
    
    /** remove a child of a node.
     * <p>
     * 
     * precondition: <code>findChild != null</code>
     * @param parent node to remove from to.
     * @param killNodeName name of the child to remomve.
     */
    public static void removeChild(Node parent, NodeName killNodeName) {
        if (findChild(parent,killNodeName) == null) {
            return;
        }
        Child[] children = parent.getChild();
        Child[] newChildren = new Child[children.length -1];
        for (int i = 0,  j = 0; i < children.length; i++) {
            if (! children[i].getName().equals(killNodeName)) {
                newChildren[j++] = children[i];
            }
        }
        parent.setChild(newChildren);
    }

    /** Merges  a map of attributes into the node - keys set in the map take precedence over those already in the node.
     * @todo may need to add filtering to this later.
     * @param n
     * @param attribs
     */
    public static void mergeAttribs(Node n, Map attribs) {
        Attribute[] current = n.getAttributes();
        // convert current into a map..
        Map currentMap = new HashMap(current.length);
        for (int i = 0; i < current.length; i++) {
            currentMap.put(current[i].getKey(),current[i].getValue());
        }
        // load in new attributes - new ones take precendence.
        currentMap.putAll(attribs);
        // convert back into an array of attributes again.
        Attribute[] nu = new Attribute[currentMap.size()];
        Iterator it = currentMap.entrySet().iterator();        
        for (int i = 0; it.hasNext(); i++) {
            Map.Entry e = (Map.Entry)it.next();
            Attribute a = new Attribute();
            a.setKey(e.getKey().toString());
            a.setValue(e.getValue().toString());
            nu[i] = a;            
        }
        n.setAttributes(nu);
    }
    


}


/* 
$Log: NodeUtils.java,v $
Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:41:10  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:27  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store

Revision 1.1.2.1  2005/02/18 15:50:14  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)
 
*/