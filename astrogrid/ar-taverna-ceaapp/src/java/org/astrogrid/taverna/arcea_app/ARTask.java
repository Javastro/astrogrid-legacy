/**
 * 
 */
package org.astrogrid.taverna.arcea_app;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

import java.net.URISyntaxException;

import org.apache.log4j.Logger;

import org.w3c.dom.Document;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.astrogrid.acr.InvalidArgumentException;
import javax.xml.parsers.ParserConfigurationException;
import org.exolab.castor.xml.Marshaller;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.ui.Lookout;
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
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

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
		logger.warn("cea start constructor in ARTask");
		this.processor = (ARProcessor)p;
	}
	private final ARProcessor processor;
	
    private ParameterValue[] convertParams(Map inputHash) {
        ParameterValue[] arr = new ParameterValue[inputHash.size()];
        Iterator i = inputHash.entrySet().iterator();
        for (int ix = 0; ix < arr.length; ix++) {
            Map.Entry e =(Map.Entry) i.next();
            arr[ix] =convertHashToParameter(e.getKey().toString(),(Map)e.getValue());                        
        }
        return arr;
    }
    
    private ParameterValue convertHashToParameter(String name,Map h) {
        ParameterValue v= new ParameterValue();
        v.setName(name);
        v.setIndirect(((Boolean)h.get("indirect")).booleanValue());
        v.setValue(h.get("value").toString());
        return v;
    }    
	
	private Document createToolDocument(Map toolStruct) throws InvalidArgumentException {
	    Tool t = new Tool();
        t.setName(toolStruct.get("name").toString());
        t.setInterface(toolStruct.get("interface").toString());
        Input input = new org.astrogrid.workflow.beans.v1.Input();
        
        Map paramHash= (Map)toolStruct.get("input");
        input.setParameter(convertParams(paramHash));
        t.setInput(input);
        
        Output output = new Output();
        paramHash = (Map)toolStruct.get("output");               
        output.setParameter(convertParams(paramHash));
        t.setOutput(output);

        try {
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        	DocumentBuilder db = dbf.newDocumentBuilder();        	
            Document doc = db.newDocument();
            Marshaller.marshal(t,doc);
            return doc;
        } catch (ParserConfigurationException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) { 
            throw new InvalidArgumentException(e);
        }		
		
	}
	
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

		String name = processor.getName();
		Object temp;		
	
		try {
				String ivorn = processor.getName();
				String ceaInterfaceName = processor.getInterfaceName();
				URI ceaAppIvorn = new URI(ivorn);
				URI ceaServiceIvorn;
				Object temp;
				if(input.containsKey("Optional CeaService Ivorn")) {
					temp = ((DataThing)input.get("Optional CeaService Ivorn")).getDataObject();
					if(temp != null && ((String)temp).trim().length() > 0) {
						ceaServiceIvorn = new URI(new String(((String)temp)));
					}
				}
	
			    ACR acr = SingletonACR.getACR();
				Applications apps = (Applications)acr.getService(Applications.class);
				
				Map toolStruct = apps.createTemplateStruct(ceaAppIvorn,ceaInterfaceName);			Lookout lookout;
			//if(name.equals("DSA")) {				
				Document toolDoc =  = createCEAToolDocument(arg0, toolStruct);
				//Document toolDoc = createToolDocument(toolStruct);
				StringWriter outputToolDoc = new StringWriter();
				logger.warn("transform the tooldoc to a string for debugging");
				TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(outputToolDoc));
				logger.warn("tooldoc to be written to file and submitted: = " + outputToolDoc);
				logger.warn("creating file");
				java.io.File toolFile = java.io.File.createTempFile((new java.rmi.server.UID()).toString(),null);
				toolFile.deleteOnExit();
				TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(toolFile));
				logger.warn("set file to delete on exit file name = " + toolFile.toURI());
				//save the toolDoc file
				URI executionID;
				if(arg0.containsKey("Optional CeaService Ivorn")) {
					temp = ((DataThing)arg0.get("Optional CeaService Ivorn")).getDataObject();
					URI ceaServiceIvorn = new URI(new String(((String)temp)));
					logger.warn("try to do a submit");
					if(ceaServiceIvorn == null) {
						executionID = apps.submitStored(toolFile.toURI());
					}else {
						executionID = apps.submitStoredTo(toolFile.toURI(),ceaServiceIvorn);
					}				
				}else {
					executionID = apps.submitStored(toolFile.toURI());
				}
			//}
			
			
			
			//Document toolDoc = apps.convertStructToDocument(toolStruct);
			
			
			//DataThing parameterThing = (DataThing)arg0.get("processUntilFinished");
			Map finalResultMap = new HashMap();
			//if(parameterThing.getDataObject().equals("false")) {
			int tempCounter = 0;
			boolean discovered = false;
			logger.warn("about to try and execute id = " + executionID);
			while(!discovered && tempCounter < 5) {
				try {
					logger.warn("firing off exectuion");
					lookout = (Lookout)acr.getService(Lookout.class);
					ExecutionInformation execInfo = apps.getExecutionInformation(executionID);
					discovered = true;
					while(execInfo.getStatus().equals(ExecutionInformation.INITIALIZING) ||
						  execInfo.getStatus().equals(ExecutionInformation.RUNNING) ||
						  execInfo.getStatus().equals(ExecutionInformation.PENDING)) {
						logger.warn("need to sleep hasn't completed, status = " + execInfo.getStatus());
						Thread.sleep(1500);
						lookout.refresh();
						execInfo = apps.getExecutionInformation(executionID);
					}//while
					logger.warn("finished with execution the final status = " + execInfo.getStatus());
					
					finalResultMap = apps.getResults(executionID);
					logger.warn("map size after finish  = " + finalResultMap.size());
					if(finalResultMap.size() == 0) {						
						finalResultMap.put("Error", "Problem with execution info = " + execInfo.toString());
					}

				}catch(org.astrogrid.acr.NotFoundException ne) {
					if(tempCounter == 5) {
						throw ne;
					}					
					logger.warn("did not find executionID lets try several more times before quiting " + executionID);
					tempCounter++;
					Thread.sleep(1000);
				}
			}//while
				
			logger.warn("setting outputmap finalresultmap size again = " + finalResultMap.size() + "and executionID = " + executionID.toString());
			logger.warn("here is result of 0 in the final map = " + finalResultMap.values().toArray()[0].toString());
		    Map outputMap = new HashMap();	
		    /*
		    Hashtable outputFromCEATemplate = (Hashtable)toolStruct.get("output");
			
		    Iterator keyIter = outputFromCEATemplate.keySet().iterator();
		    while(keyIter.hasNext()) {
		    	outputMap.put("ExecutionID",DataThingFactory.bake(executionID.toString()));
		    }
		    */
		    outputMap.put("ResultList",DataThingFactory.bake(new ArrayList(finalResultMap.values())));
		    outputMap.put("ExecutionID",DataThingFactory.bake(executionID.toString()));
		    logger.warn("end cea execute in ARTask");
		    return outputMap;
		} catch (Throwable x) {
			x.printStackTrace();
			throw  new TaskExecutionException("Failed to execute: " + processor.getName(),x);
		} 
	}
	//returns true if c is some kind of AR bean - various heuristics used.
	// pity we've not got a marker interface.
	private boolean isBean(Class c) {
		String n = c.getName();
		return  (! c.isInterface()) 
		&& (n.endsWith("Information") || n.endsWith("Descriptor") || n.endsWith("Bean"));
	}
		
	private Document createCEAToolDocument(Map input, Map toolStruct) throws TaskExecutionException, ACRException, URISyntaxException {

		logger.warn("start inputObjects in ARTask");
		//use processor to get app name and interface
		
		
		Hashtable inputFromCEATemplate = (Hashtable)toolStruct.get("input");
		
	    Iterator keyIter = inputFromCEATemplate.keySet().iterator();
	    String paramName;
	    Object paramValue;
	    DataThing parameterThing;
	    Hashtable inputListTable = new Hashtable();
	    Hashtable outputListTable = new Hashtable();
	    while(keyIter.hasNext()) {
	    	paramName = (String)keyIter.next();
	    	if(input.containsKey(paramName)) {
	    		//found great
	    		 parameterThing = (DataThing)input.get(paramName);
	    		 logger.warn("the parameterThing toString = " + parameterThing.toString());
	    		 Hashtable inputVals = (Hashtable)inputFromCEATemplate.get(paramName);
	    		 //change value and indirect now.
	    		 paramValue = parameterThing.getDataObject();
	    		 if(paramValue instanceof String) {
		    		 if((String)paramValue.startsWith("http://") || (String)paramValue.startsWith("ftp://") ||
		    		    (String)paramValue.startsWith("ivo://")) {
		    			inputVals.put("indirect",new Boolean(true));
		    		 }else {
		    			inputVals.put("indirect",new Boolean(false));
		    		 }
		    		 inputVals.put("value",(String)paramValue);
	    		 }else if(paramValue instanceof List) {
	    			 int listSize = ((List)paramValue).size();
	    			
	    			 if(listSize > 0) {
			    		 if((String)((List)paramValue).get(0).startsWith("http://") || (String)((List)paramValue).get(0).startsWith("ftp://") ||
					    		    (String)((List)paramValue).get(0).startsWith("ivo://")) {
					    		inputVals.put("indirect",new Boolean(true));
			    		 }else {
					    			inputVals.put("indirect",new Boolean(false));
					     }	    				 
	    				 inputVals.put("value",  ((List)paramValue).get(0));
	    				 inputListTable.put(paramName, ((List)paramValue));
		    			 //will need to do the rest later.
	    				 
	    			 }
	    			 
	    		 }else {
	    			 logger.warn("Not a String or List value will try to use toString(). paramValue = " + paramValue);
	    			 if(paramValue.toString().startsWith("http://") || paramValue.toString().startsWith("ftp://") ||
	 		    		    paramValue.toString().startsWith("ivo://")) {
	 		    			inputVals.put("indirect",new Boolean(true));
	 		    		 }else {
	 		    			inputVals.put("indirect",new Boolean(false));
	 		    		 }
	 		    		 inputVals.put("value",paramValue.toString());	    			 
	    		 }
	    	}else {
	    		//not found assume an optional param, but probably good to
	    		//check and throw an exceptin if minOccurs is not 0.
	    		inputFromCEATemplate.remove(paramName);
	    	}
	    }
	    
	    Hashtable outputFromCEATemplate = (Hashtable)toolStruct.get("output");
		
	    keyIter = outputFromCEATemplate.keySet().iterator();
	    Hashtable outputVals;
	    while(keyIter.hasNext()) {
	    	if(input.containsKey("Optional Output Ref - " + paramName)) {
		    	parameterThing = (DataThing)input.get("Optional Output Ref - " + paramName);
		    	paramValue = (String)parameterThing.getDataObject();
		    	if(paramValue != null && paramValue.trim().length() > 0) {
		    		outputVals = (Hashtable)outputFromCEATemplate.get(paramName);
		    		outputVals.put("indirect",new Boolean(true));
	   			 	outputVals.put("value",paramValue);
		    	}
	    	}
	    }
	    Document doc = createToolDocument(toolStruct);
	    keyIter = inputListTable.keySet().iterator();
	    Iterator listIter;
	    while(keyIter.hasNext()) {
	    	paramName = (String)keyIter.next();
	    	List paramList = (List)inputListTable.get(paramName);
	    	listIter = paramList.iterator()
	    	listIter.next();//skip the first one
	    	while(listIter.hasNext()) {
	    		org.w3c.dom.Element paramElem = doc.getElementsByTagNameNS("*","parameter");
	    		if(paramName.equals(paramElem.getAttribute("name")) {
	    			org.w3c.dom.Node cloneParam = paramElem.cloneNode(true);
	    			org.w3c.dom.Node childNode = cloneParam.getFirstChild();
	    			childNode.getFirstChild().setNodeValue((String)listIter.next());
	    			paramElem.getParentNode().appendChild(cloneParam);
	    		}
	    		//doc.cloneNode(true)
	    	}//while
	    }//while
	    return doc;
	}
	

}
