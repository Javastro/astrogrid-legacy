/**
 * 
 */
package org.astrogrid.desktop.modules.ag.transformers;

import org.apache.commons.collections.Transformer;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/**
 * @author Noel Winstanley
 * @since Apr 18, 20066:46:25 PM
 */
public class JobURNTransformer implements Transformer {

	public Object transform(Object arg0) {
        return ((JobURN)arg0).getContent();
	}

}
