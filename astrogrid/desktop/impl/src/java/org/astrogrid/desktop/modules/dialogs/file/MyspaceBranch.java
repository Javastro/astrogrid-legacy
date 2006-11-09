/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.astrogrid.filemanager.client.FileManagerNode;

import uk.ac.starlink.connect.Branch;
import uk.ac.starlink.connect.Node;

/**
 * @author Noel Winstanley
 * @since Nov 6, 20064:21:47 PM
 */
class MyspaceBranch extends MyspaceNode implements Branch {

	public MyspaceBranch(FileManagerNode node, Branch parent) {
		super(node, parent);
	}

	public Node createNode(String name) {
	        Node[] children = getChildren();
	        for ( int i = 0; i < children.length; i++ ) {
	            if ( children[ i ].getName().equals( name ) ) {
	                return children[ i ];
	            }
	        }
	        return new PotentialChildLeaf( name, this );
	    }
	

	public Node[] getChildren() {
		List l = new ArrayList();
		for (Enumeration i = getNode().children(); i.hasMoreElements(); ) {
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
