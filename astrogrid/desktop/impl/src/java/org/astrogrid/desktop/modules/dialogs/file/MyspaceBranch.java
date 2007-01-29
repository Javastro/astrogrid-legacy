/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.filemanager.client.FileManagerNode;

import uk.ac.starlink.connect.Branch;
import uk.ac.starlink.connect.Node;

/**
 * @author Noel Winstanley
 * @since Nov 6, 20064:21:47 PM
 */
class MyspaceBranch extends MyspaceNode implements Branch {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(MyspaceBranch.class);

	public MyspaceBranch(FileManagerNode node, Branch parent) {
		super(node, parent); 
	}

	/** create a child node. if name is null, throws an illegal arg exception*/
	public Node createNode(String name) {
			if (name == null) {
				throw new IllegalArgumentException("createNode: node name was null");
			}
	        Node[] children = getChildren();
	        for ( int i = 0; i < children.length; i++ ) {
	            if ( children[ i ].getName().equals( name ) ) {
	                return children[ i ];
	            }
	        }
	        return new PotentialChildLeaf( name, this );
	    }
	

	public Node[] getChildren() {
		if (getNode() == null) {
			logger.warn("Attempt to get children of a null node");
			return new Node[0];
		}
		List l = new ArrayList();
		Enumeration i = getNode().children();
		if (i == null) {
			logger.warn("Attempt to get chldren of a null enumeration");
			return new Node[0];
		}
		while( i.hasMoreElements() ) {
			FileManagerNode c = (FileManagerNode)i.nextElement();
			if (c.isFile()) {
				l.add(new MyspaceLeaf(c,this));
			} else if (c.isFolder()) {
				l.add(new MyspaceBranch(c,this));
			} else {
				// sometjing else
			}
		}
		return (Node[])l.toArray(new Node[l.size()]);
	}
}
