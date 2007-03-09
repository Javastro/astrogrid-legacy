/**
 * 
 */
package org.astrogrid.taverna.ar;

import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.embl.ebi.escience.scuflworkers.ProcessorFactory;

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
	/**
	 * 
	 */
	public ARProcessorFactory(ModuleDescriptor module,ComponentDescriptor component, MethodDescriptor method) {
		this.method = method;
		this.module = module;
		this.component= component;
		logger.info("start constructor in ARProcessorFactory");

		setName(method.getName());
	}
	private final MethodDescriptor method;
	private final ModuleDescriptor module;
	private final ComponentDescriptor component;
	
	public String getMethodName() {
		logger.info("start getMethodName in ARProcessorFactory");
		StringBuffer sb  = new StringBuffer(module.getName())
			.append('.')
			.append(component.getName())
			.append('.')
			.append(method.getName());
		logger.info("end getMethodName value = " + sb.toString());
		return sb.toString();
	}
	
 public Class getProcessorClass() {
	 return ARProcessor.class;
 };
 
 
 public String getProcessorDescription() {
	 return "A processor that executes the Astro Runtime function " + getMethodName();
 };
}
