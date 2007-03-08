/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry.srql;

import java.io.Serializable;

/** base class for a SRQL expression 
 * @author Noel Winstanley
 * @since Aug 9, 20062:41:17 PM
 */
public abstract class SRQL implements Serializable{

	/** accept a visitor to this query */
		public abstract Object accept(SRQLVisitor visitor);
}
