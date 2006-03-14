package org.astrogrid.desktop.modules.dialogs.registry;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RegistryTree extends JTree {

	private DefaultMutableTreeNode  treeNode;

	/**
	* This single constructor builds an RegistryTree object using the XML document
	* passed in through the constructor.
	*
	* @param document XML Document
	*
	* @exception ParserConfigurationException  This exception is potentially thrown if
	* the constructor configures the parser improperly.  It won't.
	*/
	public RegistryTree(Document document) throws ParserConfigurationException
	{
		super();
		// Set basic properties for the Tree rendering
		getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
		setShowsRootHandles( true );
		setEditable( false );

	    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = builderFactory.newDocumentBuilder();
	    document.getDocumentElement().normalize();

		// Take the DOM root node and convert it to a Tree model for the JTree
		treeNode = createTreeNode( (Node)document.getDocumentElement() );
		setModel( new DefaultTreeModel( treeNode ) );		
	} //end RegistryTree()

	/**
	* This takes a DOM Node and recurses through the children until each one is added
	* to a DefaultMutableTreeNode. The JTree then uses this object as a tree model.
	*
	* @param root org.w3c.Node.Node
	* @return Returns a DefaultMutableTreeNode object based on the root Node passed in
	*/
	private DefaultMutableTreeNode createTreeNode( Node root )
	{
		DefaultMutableTreeNode treeNode = null;

		if (root.getNodeType() == Node.TEXT_NODE) {		
		    treeNode = new DefaultMutableTreeNode(root.getNodeValue());
		} else {
			treeNode = new DefaultMutableTreeNode(treeNodeLabel(root));
		}

		if( root.hasChildNodes() )
		{
		    NodeList children = root.getChildNodes();
		    // Only recurse if Child Nodes are non-null
		    if( children != null )
		    {
		        int numChildren = children.getLength();
		        for (int i=0; i < numChildren; i++)
		        {
		            Node node = children.item(i);
		            if( node != null )
		            {
		                // A special case could be made for each Node type.
		                if( node.getNodeType() == Node.ELEMENT_NODE )
		                {
		                   treeNode.add( createTreeNode(node) );
		                } //end if( node.getNodeType() == Node.ELEMENT_NODE )
		                String data = node.getNodeValue();
		                if( data != null )
		                {
		                    data = data.trim();
		                    if ( !data.equals("\n") && !data.equals("\r\n") && data.length() > 0 )
		                    {
		                        treeNode.add(createTreeNode(node));
		                    } //end if ( !data.equals("\n") && !data.equals("\r\n") && data.length() > 0 )
		                } //end if( data != null )
		            } //end if( node != null )
		        } //end for (int i=0; i < numChildren; i++)
		     } //end if( children != null )
		  } //end if( root.hasChildNodes() )
		  return treeNode;
	} //end createTreeNode( Node root )
	

	private static String treeNodeLabel(Node childElement) {
		NamedNodeMap elementAttributes = childElement.getAttributes();
		String treeNodeLabel = childElement.getNodeName();
		if (elementAttributes != null && elementAttributes.getLength() > 0) {
		    treeNodeLabel = treeNodeLabel + " (";
		    int numAttributes = elementAttributes.getLength();
		    for(int i=0; i<numAttributes; i++) {
		        Node attribute = elementAttributes.item(i);
		        if (i > 0 && !attribute.getNodeName().startsWith("xmlns") && !attribute.getNodeName().startsWith("xsi")) {
		            treeNodeLabel = treeNodeLabel + ", ";
		        }
		        if (!(attribute.getNodeName().startsWith("xmlns") || attribute.getNodeName().startsWith("xsi"))) {
		        treeNodeLabel = treeNodeLabel + attribute.getNodeName() +
		          "=" + attribute.getNodeValue();
		        }
		    }
		    treeNodeLabel = treeNodeLabel + ")";
		    // Tidy label
		    if (treeNodeLabel.trim().endsWith("()")) 
		    	treeNodeLabel = treeNodeLabel.substring(0, treeNodeLabel.length() - 2);
		}
		return(treeNodeLabel);
	}
}
