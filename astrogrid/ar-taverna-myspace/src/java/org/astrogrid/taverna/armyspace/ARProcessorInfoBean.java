package org.astrogrid.taverna.armyspace;

import org.embl.ebi.escience.scuflworkers.ProcessorInfoBeanHelper;

import org.apache.log4j.Logger;

/**
 * Provides information about the Biomoby Processor plugin, using taverna.properties, identified by the
 * tag 'biomobywsdl'
 * @author Stuart Owen
 *
 */

public class ARProcessorInfoBean extends ProcessorInfoBeanHelper {
	
	private static Logger logger = Logger.getLogger(ARProcessorInfoBean.class);
	
	public ARProcessorInfoBean() {
		super("astroruntimemyspace");
		logger.warn("just done constructor of ARProcessorInfoBean");
	}

}
