/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry.srql;

/** abstract class that captures commonality of an 'and' and an 'or' query.
 * @author Noel Winstanley
 * @since Aug 10, 20066:03:53 PM
 */
public abstract class BinaryOperatorSRQL extends SRQL {

	protected SRQL left;
	protected SRQL right;


	public SRQL getLeft() {
		return this.left;
	}

	public void setLeft(SRQL left) {
		this.left = left;
	}

	public SRQL getRight() {
		return this.right;
	}

	public void setRight(SRQL right) {
		this.right = right;
	}

}
