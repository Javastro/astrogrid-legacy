/**
 * 
 */
package org.astrogrid.taverna.arcea_app;

import org.embl.ebi.escience.scuflworkers.ProcessorFactory;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

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
	
	private static Logger logger = Logger.getLogger(ARProcessorFactory.class);	
	
	/**
	 * 
	 */
	public ARProcessorFactory(String ivorn) {
		logger.warn("cea start constructor in ARProcessorFactory");
		setName(ivorn);
		logger.warn("done with arprocessorfactory");
	}
		
	public Class getProcessorClass() {
		return ARProcessor.class;
	}
 
	public String getProcessorDescription() {
		return "A processor that executes the CeaApplication " + getName();
	}
}
