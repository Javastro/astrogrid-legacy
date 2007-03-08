/**
 * 
 */
package org.astrogrid.taverna.arcea;

import org.embl.ebi.escience.scuflworkers.ProcessorFactory;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * * Implementing classes are capable of creating a new processor and attaching it
 * to a model when supplied with the new processor name and a reference to the
 * model. The intention is that service scavengers should create an
 * implementation of this for each service they find and that these should then
 * be used as the user objects inside a default tree model to allow simple
 * service selection and addition to a ScuflModel
 * 
 * @author Noel Winstanley
 * @since May 24, 20065:30:56 PM
 */
public class ARProcessorFactory extends ProcessorFactory {
	
	private static final Log logger = LogFactory.getLog(ARProcessorFactory.class);
	
	private final String methodName;
	private  URI ceaServiceIvorn;
	private  URI ceaAppIvorn;
	private final String ceaInterface;
	
	
	/**
	 * 
	 */
	public ARProcessorFactory(String ceaAppIvorn, String ceaServiceIvorn, String ceaInterface) {
		logger.info("start constructor in ARProcessorFactory");
		this.methodName = (ceaAppIvorn.substring(6) + "-" + ceaInterface);
		//this.ceaAppIvorn = ceaAppIvornStr != null ? new URI(ceaAppIvornStr) : null;
		try {
			this.ceaAppIvorn = new URI(ceaAppIvorn);
		}catch(URISyntaxException use) {
			use.printStackTrace();
			logger.error(use.getMessage());
			this.ceaAppIvorn = null;
		}
		try {
			this.ceaServiceIvorn = new URI(ceaServiceIvorn);
		}catch(URISyntaxException use) {
			use.printStackTrace();
			logger.error(use.getMessage());
			this.ceaServiceIvorn = null;
		}		
		this.ceaInterface = ceaInterface;
		setName(methodName);
	}
	
	public String getMethodName() {
		return this.methodName;
	}

	public String getCeaInterface() {
		return this.ceaInterface;
	}
	
	
	public URI getCeaAppIvorn() {
		return this.ceaAppIvorn;
	}

	public URI getCeaServiceIvorn() {
		return this.ceaServiceIvorn;
	}

	
	public Class getProcessorClass() {
		return ARProcessor.class;
	}
 
	public String getProcessorDescription() {
		return "A processor that executes the CeaApplication " + getMethodName();
	}
}
