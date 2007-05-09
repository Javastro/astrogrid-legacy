/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;


/** abstract class that captures commonality of an 'and' and an 'or' query.
 * @author Noel Winstanley
 * @since Aug 10, 20066:03:53 PM
 */
public abstract class BinaryOperatorQuery extends AbstractQuery {

	protected AbstractQuery left;
	protected AbstractQuery right;


	public AbstractQuery getLeft() {
		return this.left;
	}

	public void setLeft(AbstractQuery left) {
		this.left = left;
	}

	public AbstractQuery getRight() {
		return this.right;
	}

	public void setRight(AbstractQuery right) {
		this.right = right;
	}

}
