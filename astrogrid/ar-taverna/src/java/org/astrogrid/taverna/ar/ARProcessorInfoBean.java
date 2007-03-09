package org.astrogrid.taverna.ar;

import org.embl.ebi.escience.scuflworkers.ProcessorInfoBeanHelper;

/**
 * Provides information about the Biomoby Processor plugin, using taverna.properties, identified by the
 * tag 'biomobywsdl'
 * @author Stuart Owen
 *
 */

public class ARProcessorInfoBean extends ProcessorInfoBeanHelper {

	public ARProcessorInfoBean() {
		super("astroruntime");
	}

}
