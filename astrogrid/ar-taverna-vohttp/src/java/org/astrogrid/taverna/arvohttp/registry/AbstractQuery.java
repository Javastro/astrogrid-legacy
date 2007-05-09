/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;

import java.io.Serializable;

/** base class for a keyword query.
 * @author Noel Winstanley
 * @since Aug 9, 20062:41:17 PM
 */
abstract class AbstractQuery implements Serializable{

		public abstract Object accept(QueryVisitor visitor);
}
