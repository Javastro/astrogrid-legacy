/*
 * Created on Sep 9, 2004
 */
package org.astrogrid.applications.integration.http.webservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.config.SimpleConfig;

import junit.framework.TestCase;

/**
 * @author jdt
 */
public class CECClientTest extends TestCase {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(CECClientTest.class);
	private static final String ENDPOINT = "org.astrogrid.cea.http.service.url";

	public void testCanCallDelegate() throws CEADelegateException {
		String endPoint = SimpleConfig.getProperty(ENDPOINT);
		CommonExecutionConnectorClient delegate = DelegateFactory.createDelegate(endPoint);
		

		//about the simplest call we can do, just to see that we *can* contact the server
		String regEntry = delegate.returnRegistryEntry();

		logger.debug("testCallDelegate() - Registry entry:  : regEntry = "
				+ regEntry);
	}
	
}
