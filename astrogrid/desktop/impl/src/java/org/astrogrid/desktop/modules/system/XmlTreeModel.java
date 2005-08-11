/* XmlTreeModel.java
 * Created on 09-Jun-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.system;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
/**
 * @todo move this out of the system module.
 *
 */
public class XmlTreeModel implements TreeModel
{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(XmlTreeModel.class);
    
	private XmlTreeNodeFilter treeNodeFilter;
    private Vector treeModelListeners;
    private Vector treeSelectionListeners;
    private Node rootNode;

/**
 * Creates a new <code>XmlTreeModel</code>.
 *
 * @param rootNode as the root-node of the XML structure.
 */
  public XmlTreeModel(Node rootNode)
  { 
  	this(rootNode, null);
  }


/**
 * Creates a new <code>XMLDocumentTree</code>.
 *
 * @param rootNode as the root-node of the XML structure.
 * @param filter that defines what tree nodes should be visible or not.
 */
  public XmlTreeModel(Node rootNode, XmlTreeNodeFilter  filter)
  { 
  	this.rootNode = rootNode;
    treeModelListeners = new Vector();
    treeSelectionListeners = new Vector();
    setTreeNodeFilter(filter);
  }

/**
 * Sets the tree node filter for this tree model.
 *
 * @param filter that defines what tree nodes should be visible or not.
 */
  public void setTreeNodeFilter(XmlTreeNodeFilter  filter)
  { 
  	this.treeNodeFilter = filter;
  }

//=============================================================================
// Implementing the TreeModel-Interface
//-----------------------------------------------------------------------------

/**
 * Adds a listener for the TreeModelEvent posted after the tree changes.
 *
 * @param listener as the TreeModelListener.
 */
  public void addTreeModelListener(TreeModelListener listener)
  { 
  	if (listener != null && !treeModelListeners.contains(listener))
    { 
  		treeModelListeners.addElement(listener);
    }
  }

/**
 * Removes a listener from the listeners list.
 *
 * @param listener as the TreeModelListener.
 */
  public void removeTreeModelListener(TreeModelListener listener)
  { 
  	if (listener != null) treeModelListeners.removeElement(listener);
  }
  
  /**
   * Adds a listener for the TreeModelEvent.
   *
   * @param listener as the TreeModelListener.
   */
    public void addTreeSelectionListener(TreeSelectionListener listener)
    { 
      if (listener != null && !treeModelListeners.contains(listener))
      { 
    	treeSelectionListeners.addElement(listener);
      }
    }  

  /**
   * Removes a listener from the listeners list.
   *
   * @param listener as the TreeModelListener.
   */
    public void removeTreeSelectionListener(TreeSelectionListener listener)
    { 
        if (listener != null) treeSelectionListeners.removeElement(listener);
    }
/**
 * Returns the root node of the tree.
 */
  public Object getRoot()
  { 
  	return rootNode;
  }

/**
 * Returns false if node has visible childs, else true.
 */
  public boolean isLeaf(Object node)
  {
  	return getVisibleChildCount((Node)node) == 0;
  }

/**
 * Returns the child XMLElement of parent XMLElement at index index.
 */
  public Object getChild(Object parent, int index)
  { 
  	return getVisibleChildAt((Node)parent, index);
  }

/**
 * Returns the number of children of parent.
 */
  public int getChildCount(Object parent)
  { 
  	return getVisibleChildCount((Node)parent);
  }

/**
 * Returns the number of children of parent.
 */
  public int getIndexOfChild(Object parent, Object child)
  { 
  	return getIndexOfVisibleChild((Node)parent, (Node)child);
  }

/**
 * Not implemented in this TreeModel.
 */
  public void valueForPathChanged(TreePath path, Object newValue)
  { /* not implemented */
  }

//=============================================================================
// Internal filter support ...
//-----------------------------------------------------------------------------

  private boolean isVisibleXmlTreeNode(Node node)
  { return (treeNodeFilter == null) ? true : treeNodeFilter.isVisibleXmlTreeNode(node);
  }

  private Node getVisibleChildAt(Node parentNode, int index)
  { NodeList childNodes = parentNode.getChildNodes();
    for (int i = 0, visibleIndex = 0, n = childNodes.getLength(); i < n; i++)
    { Node childNode = childNodes.item(i);
      if (!isVisibleXmlTreeNode(childNode)) continue;
      if (index == visibleIndex) return childNode;
      visibleIndex++;
    }
    return null;
  }

  private int getVisibleChildCount(Node parentNode)
  { NodeList childNodes = parentNode.getChildNodes();
    int visibleChildCount = 0;
    for (int i = 0, n = childNodes.getLength(); i < n; i++)
    { if (isVisibleXmlTreeNode(childNodes.item(i))) visibleChildCount++;
    }
    return visibleChildCount;
  }

  private int getIndexOfVisibleChild(Node parentNode, Node childNode)
  { NodeList childNodes = parentNode.getChildNodes();
    for (int i = 0, visibleChildCount = 0, n = childNodes.getLength(); i < n; i++)
    { Node checkNode = childNodes.item(i);
      if (isVisibleXmlTreeNode(checkNode))
      { if (checkNode.equals(childNode)) return visibleChildCount;
        visibleChildCount++;
      }
    }
    return -1;
  }

//=============================================================================
// Private Tree-Model Event support ...
//-----------------------------------------------------------------------------

  private void fireTreeNodesChanged(TreeModelEvent e)
  { for (int i = 0, n = treeModelListeners.size(); i < n; i++)
    { ((TreeModelListener)treeModelListeners.elementAt(i)).treeNodesChanged(e);
    }
  }

  private void fireTreeNodesInserted(TreeModelEvent e)
  { for (int i = 0, n = treeModelListeners.size(); i < n; i++)
    { ((TreeModelListener)treeModelListeners.elementAt(i)).treeNodesInserted(e);
    }
  }

  private void fireTreeNodesRemoved(TreeModelEvent e)
  { for (int i = 0, n = treeModelListeners.size(); i < n; i++)
    { ((TreeModelListener)treeModelListeners.elementAt(i)).treeNodesRemoved(e);
    }
  }

  private void fireTreeStructureChanged(TreeModelEvent e)
  { for (int i = 0, n = treeModelListeners.size(); i < n; i++)
    { ((TreeModelListener)treeModelListeners.elementAt(i)).treeStructureChanged(e);
    }
  }

  private void fireTreeNodeInserted(Node parentNode, Node childNode)
  { TreeModelEvent e = makeTreeModelChangeEvent(parentNode, childNode);
    fireTreeNodesInserted(e);
  }

  private TreeModelEvent makeTreeModelChangeEvent(Node parentNode, Node childNode)
  { return new TreeModelEvent(
      this,
      getPathToRoot( parentNode ),
      new int[]    { getIndexOfVisibleChild(parentNode, childNode) },
      new Object[] { childNode }
    );
  }

//=============================================================================
// Tree-Path and -Node support ...
//-----------------------------------------------------------------------------

/**
 * Provides an array of Nodes as path from root to node.
 *
 * @param node as the path delimited Node
 * @return Object[] as path where Object[0] is the root node and Object[size - 1] is the given node.
 */
  public Object[] getPathToRoot(Node node)
  { Vector pathNodes = new Vector();
    Node parentNode = node;
    while (parentNode != null)
    { pathNodes.add(0, parentNode);
      if (parentNode.equals(rootNode)) break;
      parentNode = parentNode.getParentNode();
    }
    return (parentNode.equals(rootNode)) ? pathNodes.toArray() : new Object[0];
  }

/**
 * Returns the number of siblings of child (inlc. child). This is the number of childs of childs parent.
 *
 * @return number of siblings or -1 if child is the root element.
 */
  public int getSiblingsCount(Node child)
  { Node parent = child.getParentNode();
    if (parent != null) return getChildCount(parent);
    return -1;
  }

//=============================================================================
// Tree-Event support ...
//-----------------------------------------------------------------------------

/**
 * Notifies all listeners that have registered interest for notification on changes.
 *
 * @param changeNode as Node whose underlying structure has changed.
 */
  public void fireTreeStructureChanged(Node changedNode)
  { fireTreeStructureChanged(new TreeModelEvent(this, getPathToRoot(changedNode)));
  }

/**
 * Notifies all listeners that have registered interest for notification on changes.
 *
 * @param treeNode as Node whose content has changed.
 */
  public void fireTreeNodeChanged(Node treeNode)
  { fireTreeNodesChanged(new TreeModelEvent(this, getPathToRoot(treeNode)));
  }
  
  public void valueChanged(TreeSelectionEvent e) {
  	logger.warn("xxxxxxxx: " + e.getSource());
  }
  

//=============================================================================
//Tree-Manipulation-Support
//-----------------------------------------------------------------------------

/**
 * Adds a childNode to a parentNode.<br>
 * Notifies all listeners that have registered interest.
 *
 * @param parentNode as parent of the child Node.
 * @param childNode as Node to be added.
 */
  public void addXmlTreeNode(Node parentNode, Node childNode)
  { if (parentNode != null && childNode != null)
    { parentNode.appendChild(childNode);
      fireTreeNodeInserted (parentNode, childNode);
    }
  }

/**
 * Adds a node before a given successor.<br>
 * Notifies all listeners that have registered interest.
 *
 * @param node as Node to be added.
 * @param successor as Node after the to be added node
 */
  public void addXmlTreeNodeBefore(Node node, Node successor)
  { if (node != null)
    { Node parentNode = node.getParentNode();
      if (parentNode != null)
      { if (successor == null) // means add to the end
        { addXmlTreeNode(parentNode, node);
        }
        else // add before successor
        { parentNode.insertBefore(node, successor);
          fireTreeNodeInserted(parentNode, node);
        }
      }
    }
  }

/**
 * Removes a node from the tree.
 *
 * @param node as Node to be removed.
 */
  public void removeXmlTreeNode(Node node)
  { if (node != null)
    { Node parentNode = node.getParentNode();
      if (parentNode != null)
      { TreeModelEvent removeEvent = makeTreeModelChangeEvent(parentNode, node);
        parentNode.removeChild(node);
        fireTreeNodesRemoved(removeEvent);
      }
    }
  }
  
  /**
   * Filters the node elements of an XmlTreeModel.
   */
  public interface XmlTreeNodeFilter
  {
  /**
   * Defines if a given Element should be visible within an XmlTreeModel.
   *
   * @param nodeElement as the Element to be investigated.
   *
   * @return boolean <code>true</code> if the given Element should be visible otherwise <code>false</code>.
   */
    public boolean isVisibleXmlTreeNode(Node nodeElement);
  }  
  
}


