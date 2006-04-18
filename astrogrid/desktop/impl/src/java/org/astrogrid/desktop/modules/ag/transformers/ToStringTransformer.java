/**
 * 
 */
package org.astrogrid.desktop.modules.ag.transformers;

import org.apache.commons.collections.Transformer;

/** convert's it's arg to a string.
 * @author Noel Winstanley
 * @since Apr 18, 20066:49:25 PM
 */
public class ToStringTransformer implements Transformer {

	public Object transform(Object arg0) {
		return arg0 == null ? "null" : arg0.toString();
	}

}
