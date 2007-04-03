/**
 * 
 */
package org.astrogrid.taverna.arcea;

import java.util.HashMap;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;

import org.w3c.dom.Document;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.ExecutionInformation;

import java.net.URI;

import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.baclava.factory.DataThingFactory;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scuflworkers.ProcessorTaskWorker;
//import org.embl.ebi.escience.scuflworkers.apiconsumer.APIConsumerDefinition;

//import uk.ac.soton.itinnovation.taverna.enactor.entities.ProcessorTask;
import org.embl.ebi.escience.scufl.IProcessorTask;
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
	
	private static Logger logger = Logger.getLogger(ARTask.class);

	// seems like we get a constructor which passes a reference to the processor.
	public ARTask(Processor p) {
		logger.warn("start constructor in ARTask");
		this.processor = (ARProcessor)p;
	}
	private final ARProcessor processor;
	
	private String ceaInterfaceName;
	private URI ceaServiceIvorn;
	private URI ceaAppIvorn;
	
	/**
	 * Given a map of name->DataThing value, invoke the underlying task and
	 * return a map of result name -> DataThing value.
	 * 
	 * @exception TaskExecutionException
	 *                thrown if an error occurs during task invocation
	 */
	public Map execute(Map arg0, IProcessorTask arg1) throws TaskExecutionException {
		// unpackage inputs.
		logger.warn("start execute in ARTask");

		this.ceaServiceIvorn = processor.getCeaServiceIvorn();
		this.ceaAppIvorn = processor.getCeaAppIvorn();
		this.ceaInterfaceName = processor.getCeaInterface();
		
		//Class returnClass = processor.getMethodDescriptor().getReturnValue().getType();
		/*
		ACR acr = getACR();
		//Registry reg = (Registry)acr.getService(Registry.class);
		Applications apps = (Applications)acr.getService(Applications.class);
		CeaApplication cea = apps.getCeaApplication();
		InterfaceBean []ib = cea.getInterfaces();
		InterfaceBean ceaInterfaceBean;
		*/
		try {
			ACR acr = SingletonACR.getACR();
			Applications apps = (Applications)acr.getService(Applications.class);
			Map toolStruct = createCEAToolMap(arg0);
			Document toolDoc = apps.convertStructToDocument(toolStruct);
			URI executionID;
			if(ceaServiceIvorn == null) {
				executionID = apps.submit(toolDoc);
			}else {
				executionID = apps.submitTo(toolDoc,ceaServiceIvorn);
			}
			DataThing parameterThing = (DataThing)arg0.get("processUntilFinished");
			Map finalResultMap;
			if(parameterThing.getDataObject().equals("false")) {
				ExecutionInformation execInfo = apps.getExecutionInformation(executionID);
				while(execInfo.getStatus().equals(ExecutionInformation.INITIALIZING) ||
					  execInfo.getStatus().equals(ExecutionInformation.RUNNING)) {
					Thread.sleep(500);
					execInfo = apps.getExecutionInformation(executionID);
				}//while
				finalResultMap = apps.getResults(executionID);				
			}else {
				finalResultMap = new java.util.Hashtable();
			}
			
			
			// package outputs
		    Map outputMap = new HashMap();
		    outputMap.put("resultMap",DataThingFactory.bake(finalResultMap));
		    outputMap.put("executionID",DataThingFactory.bake(executionID.toString()));
		    logger.warn("end execute in ARTask");
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
		
	private Map createCEAToolMap(Map input) throws TaskExecutionException, ACRException {

		logger.warn("start inputObjects in ARTask");

	    ACR acr = SingletonACR.getACR();
		Applications apps = (Applications)acr.getService(Applications.class);
		Map toolStruct = apps.createTemplateStruct(ceaAppIvorn,ceaInterfaceName);
		Hashtable inputFromCEATemplate = (Hashtable)toolStruct.get("input");
	    Iterator keyIter = inputFromCEATemplate.keySet().iterator();
	    String paramName;
	    String paramValue;
	    while(keyIter.hasNext()) {
	    	paramName = (String)keyIter.next();
	    	if(input.containsKey(paramName)) {
	    		//found great
	    		 DataThing parameterThing = (DataThing)input.get(paramName);
	    		 logger.warn("the parameterThing toString = " + parameterThing.toString());
	    		 Hashtable inputVals = (Hashtable)inputFromCEATemplate.get(paramName);
	    		 //change value and indirect now.
	    		 paramValue = (String)parameterThing.getDataObject();
	    		 if(paramValue.startsWith("http://") || paramValue.startsWith("ftp://") ||
	    		    paramValue.startsWith("ivo://")) {
	    			inputVals.put("indirect",new Boolean(true));
	    		 }else {
	    			inputVals.put("indirect",new Boolean(false));
	    		 }
	    		 inputVals.put("value",paramValue);
	    	}else {
	    		//not found assume an optional param, but probably good to
	    		//check and throw an exceptin if minOccurs is not 0.
	    		inputFromCEATemplate.remove(paramName);
	    	}
	    }	    
	    return toolStruct;
	}
	

}
