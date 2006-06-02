/**
 * 
 */
package org.astrogrid.taverna.ar;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ValueDescriptor;
import org.astrogrid.acr.system.ApiHelp;
import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.baclava.factory.DataThingFactory;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scuflworkers.ProcessorTaskWorker;
import org.embl.ebi.escience.scuflworkers.apiconsumer.APIConsumerDefinition;

import uk.ac.soton.itinnovation.taverna.enactor.entities.ProcessorTask;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * **
 * An implementation of this class provides the concrete invocation
 * functionality matching the possibly more abstract definition from the
 * Processor subclass. For example, the processor definition for a web service
 * has the responsibility of parsing WSDL, generating ports and fetching any
 * descriptive information whereas the ProcessorTaskWorker implementation for
 * this type has the task of actually creating and invoking the network call to
 * perform a single instance of the operation defined by the Processor.
 * 
 * 
 * @author Noel Winstanley
 * @since May 24, 20065:41:59 PM
 */
public class ARTask implements ProcessorTaskWorker {

	// seems like we get a constructor which passes a reference to the processor.
	public ARTask(Processor p) {
		this.processor = (ARProcessor)p;
	}
	private final ARProcessor processor;
	
	/**
	 * Given a map of name->DataThing value, invoke the underlying task and
	 * return a map of result name -> DataThing value.
	 * 
	 * @exception TaskExecutionException
	 *                thrown if an error occurs during task invocation
	 */
	public Map execute(Map arg0, ProcessorTask arg1) throws TaskExecutionException {
		// unpackage inputs.
		String methodName = processor.getMethodName();
		Class returnClass = processor.getMethodDescriptor().getReturnValue().getType();
		try {
			ACR acr = SingletonACR.getACR();
			ApiHelp api = (ApiHelp)acr.getService(ApiHelp.class);
			// call method
			while (returnClass.isArray()) {
				returnClass = returnClass.getComponentType();
			}
			int returnKind = isBean(returnClass) ? ApiHelp.ORIGINAL : ApiHelp.DATASTRUCTURE;
			Object result = api.callFunction(methodName,returnKind,inputObjects(arg0));
			// package outputs
		    Map outputMap = new HashMap();
		    outputMap.put("result",DataThingFactory.bake(result));
		    return outputMap;
		} catch (Throwable x) {
			TaskExecutionException tee =  new TaskExecutionException("Failed to execute: " + processor.getMethodName(),x);
			tee.initCause(x);
			throw tee;
		} 
	}
	//returns true if c is some kind of AR bean - various heuristics used.
	// pity we've not got a marker interface.
	private boolean isBean(Class c) {
		String n = c.getName();
		return  (! c.isInterface()) 
		&& (n.endsWith("Information") || n.endsWith("Descriptor") || n.endsWith("Bean"));
	}
		
	    private Object[] inputObjects(Map input) 
		throws TaskExecutionException {
			MethodDescriptor md = processor.getMethodDescriptor();
			ValueDescriptor[] parameters = md.getParameters();	    	
		Object[] inputObjects = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
		    DataThing parameterThing = (DataThing)input.get(parameters[i].getName());
		    if (parameterThing == null) {
			throw new TaskExecutionException("Requires input name "+parameters[i].getName());
		    }
		    
			inputObjects[i] = parameterThing.getDataObject();
		    
		    
		}
		return inputObjects;	
	    }
	

}
