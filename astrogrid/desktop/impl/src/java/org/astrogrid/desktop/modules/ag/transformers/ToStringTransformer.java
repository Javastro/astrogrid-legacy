/**
 * 
 */
package org.astrogrid.desktop.modules.ag.transformers;

import org.apache.commons.collections.Transformer;

/** Simply convert something to a string using {@code toString()}.
 * @author Noel Winstanley
 * @since Apr 18, 20066:49:25 PM
 */
public class ToStringTransformer implements Transformer {

	public Object transform(final Object arg0) {
		return arg0 == null ? "null" : arg0.toString();
	}

}
