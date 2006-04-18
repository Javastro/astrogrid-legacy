package org.astrogrid.desktop.modules.ag.converters;

import org.apache.commons.beanutils.Converter;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/**
 * @author Noel Winstanley
 * @since Apr 14, 20062:41:23 AM
 */
public class JobURNConverter implements Converter {
	public Object convert(Class arg0, Object arg1) {
	    if (arg0 != JobURN.class) {
	        throw new RuntimeException("Can only convert to JobURNs " + arg0.getName());
	    }
	    JobURN urn = new JobURN();
	    urn.setContent(arg1.toString());
	    return urn;
	}
}