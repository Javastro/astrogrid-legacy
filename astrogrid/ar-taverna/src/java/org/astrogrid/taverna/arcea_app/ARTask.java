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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


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
//import org.astrogrid.acr.ui.Lookout;
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
        	dbf.setNamespaceAware(true);
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

		//String name = processor.getName();
		try {
				String ivorn = processor.getIvorn();
				String ceaInterfaceName = processor.getInterfaceName();
				URI ceaAppIvorn = new URI(ivorn);
				URI ceaServiceIvorn;
				Object temp;
				if(arg0.containsKey("Optional CeaService Ivorn")) {
					temp = ((DataThing)arg0.get("Optional CeaService Ivorn")).getDataObject();
					if(temp != null && ((String)temp).trim().length() > 0) {
						ceaServiceIvorn = new URI(new String((((String)temp).trim())));
					}
				}
	
			    ACR acr = SingletonACR.getACR();
				Applications apps = (Applications)acr.getService(Applications.class);
				logger.warn("try to create the template struct");
				Map toolStruct = apps.createTemplateStruct(ceaAppIvorn,ceaInterfaceName);
				logger.warn("created and toolstruct size = " + toolStruct.size());
				//Lookout lookout;
			//if(name.equals("DSA")) {				
				Document toolDoc  = createCEAToolDocument(arg0, toolStruct);
				//Document toolDoc = createToolDocument(toolStruct);
				StringWriter outputToolDoc = new StringWriter();
				logger.warn("transform the tooldoc to a string for debugging");
				TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(outputToolDoc));
				logger.warn("tooldoc to be written to file and submitted: = " + outputToolDoc);
				logger.warn("creating file");
				java.io.File toolFile = java.io.File.createTempFile("taverna",".wkfl");
				toolFile.deleteOnExit();
				logger.warn("created Temp File = " + toolFile.toString());
				//TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(toolFile));
				TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(new java.io.FileOutputStream(toolFile)));
				logger.warn("set file to delete on exit file name = " + toolFile.toURI());
				//save the toolDoc file
				URI executionID;
				if(arg0.containsKey("Optional CeaService Ivorn")) {
					temp = ((DataThing)arg0.get("Optional CeaService Ivorn")).getDataObject();
					ceaServiceIvorn = new URI(new String((  ((String)temp).trim()  )));
					logger.warn("try to do a submit");
					if(ceaServiceIvorn == null) {
						executionID = apps.submitStored(toolFile.toURI());
					}else {
						executionID = apps.submitStoredTo(toolFile.toURI(),ceaServiceIvorn);
					}				
				}else {
					logger.warn("submitting file uri = " + toolFile.toURI().toString());
					executionID = apps.submitStored(toolFile.toURI());
				}
			//}
				logger.warn("executed and execute id = " + executionID);
			
			
			//Document toolDoc = apps.convertStructToDocument(toolStruct);
			
			
			//DataThing parameterThing = (DataThing)arg0.get("processUntilFinished");
			Map finalResultMap = new HashMap();
			//if(parameterThing.getDataObject().equals("false")) {
			int tempCounter = 0;
			boolean discovered = false;
			String finalExecutionInfo = null;
			
			while(!discovered && tempCounter < 5) {
				try {
					logger.warn("firing off exectuion");
					//lookout = (Lookout)acr.getService(Lookout.class);
					ExecutionInformation execInfo = apps.getExecutionInformation(executionID);
					discovered = true;
					while(execInfo.getStatus().equals(ExecutionInformation.INITIALIZING) ||
						  execInfo.getStatus().equals(ExecutionInformation.RUNNING) ||
						  execInfo.getStatus().equals(ExecutionInformation.UNKNOWN) ||
						  execInfo.getStatus().equals(ExecutionInformation.PENDING)) {
						logger.warn("need to sleep hasn't completed, status = " + execInfo.getStatus());
						Thread.sleep(5000);
						//lookout.refresh();
						execInfo = apps.getExecutionInformation(executionID);
					}//while
					logger.warn("finished with execution the final status = " + execInfo.getStatus());
					finalExecutionInfo = execInfo.toString();
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
					logger.warn("finished sleeping for 1 second");
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
		    outputMap.put("ExecutionInformation",DataThingFactory.bake(finalExecutionInfo));
		    logger.warn("end cea execute in ARTask");
		    return outputMap;
		} catch (Throwable x) {
			x.printStackTrace();
			throw  new TaskExecutionException("Failed to execute: " + processor.getIvorn(),x);
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
	    //logger.warn("the inputFromCeaTemplate size = " + inputFromCEATemplate.size());
	    while(keyIter.hasNext()) {
	    	//logger.warn("ok there is more keyIter");
	    	paramName = (String)keyIter.next();
	    	//logger.warn("the paramName = " + paramName);
	    	if(input.containsKey(paramName)) {
	    		//found great
	    		 parameterThing = (DataThing)input.get(paramName);
	    		 logger.warn("the parameterThing toString = " + parameterThing.toString());
	    		 Hashtable inputVals = (Hashtable)inputFromCEATemplate.get(paramName);
	    		 //change value and indirect now.
	    		 paramValue = parameterThing.getDataObject();
	    		 if(paramValue instanceof String) {
	    			 logger.warn("parameter instance a string");
		    		 if(((String)paramValue).startsWith("http://") || 
		    			  ((String)paramValue).startsWith("ftp://")  ||
		    		    ((String)paramValue).startsWith("ivo://")  ||
                ((String)paramValue).startsWith("vos://")) {
		    			inputVals.put("indirect",new Boolean(true));
		    		 }else {
		    			inputVals.put("indirect",new Boolean(false));
		    		 }
		    		 inputVals.put("value",((String)paramValue).trim());
	    		 }else if(paramValue instanceof List) {
	    			 logger.warn("parameter instance a List");
	    			 int listSize = ((List)paramValue).size();
	    			
	    			 if(listSize > 0) {
			    		 if(((String)(((List)paramValue).get(0))).startsWith("http://") || 
			    		    ((String)(((List)paramValue).get(0))).startsWith("ftp://")  ||
					        ((String)(((List)paramValue).get(0))).startsWith("ivo://")  ||
                  ((String)(((List)paramValue).get(0))).startsWith("vos://")) {
					    		inputVals.put("indirect",new Boolean(true));
			    		 }else {
					    		inputVals.put("indirect",new Boolean(false));
					     }	    				 
	    				 inputVals.put("value",  ((String)((List)paramValue).get(0)).trim()   );
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
	 		    		 inputVals.put("value",paramValue.toString().trim());	    			 
	    		 }
	    	}else {
	    		logger.warn("need to remove from template paramName = " + paramName);
	    		//not found assume an optional param, but probably good to
	    		//check and throw an exceptin if minOccurs is not 0.
	    		//inputFromCEATemplate.remove(paramName);
	    		keyIter.remove();
	    	}
	    }
	    
	    logger.warn("lets do output");
	    Hashtable outputFromCEATemplate = (Hashtable)toolStruct.get("output");
		
	    keyIter = outputFromCEATemplate.keySet().iterator();
	    Hashtable outputVals;
	    while(keyIter.hasNext()) {
	    	paramName = (String)keyIter.next();
	    	if(input.containsKey(paramName + " - Ref")) {
		    	parameterThing = (DataThing)input.get(paramName + " - Ref");
		    	paramValue = (String)parameterThing.getDataObject();
		    	if(paramValue != null && ((String)paramValue).trim().length() > 0) {
		    		outputVals = (Hashtable)outputFromCEATemplate.get(paramName);
		    		outputVals.put("indirect",new Boolean(true));
	   			 	outputVals.put("value",  ((String)paramValue).trim()  );
		    	}
	    	}
	    }
	    Document doc = createToolDocument(toolStruct);

	    keyIter = inputListTable.keySet().iterator();
	    Iterator listIter;
	    logger.warn("ok we have to do some digging in the dom to fix lists");
	    while(keyIter.hasNext()) {
	    	paramName = (String)keyIter.next();
	    	List paramList = (List)inputListTable.get(paramName);
	    	listIter = paramList.iterator();
	    	listIter.next();//skip the first one
	    	logger.warn("paramName for list fix = " + paramName + " the List size = " + paramList.size());
	    	while(listIter.hasNext()) {
	    		//NodeList paramElem = doc.getElementsByTagNameNS("*","parameter");
	    		NodeList paramElem = doc.getElementsByTagName("parameter");
	    		logger.warn("paramElem nodelize size = " + paramElem.getLength());
	    		if(paramElem.getLength() == 0) {
	    			logger.warn("darn it is not finding a parameter element lets do a break");
	    			break;
	    		}
	    		for(int k = 0;k < paramElem.getLength();k++) {
		    		if(paramName.equals( ((Element)paramElem.item(k)).getAttribute("name"))) {
		    			logger.warn("ok great the paramElem name attribute matched paramName");
		    			Node cloneParam = ((Element)paramElem.item(k)).cloneNode(true);
		    			Node childNode = cloneParam.getFirstChild();
		    			childNode.getFirstChild().setNodeValue(  ((String)listIter.next()).trim()  );
		    			((Element)paramElem.item(k)).getParentNode().appendChild(cloneParam);
		    			k = paramElem.getLength()+2;
		    		}else if(k == (paramElem.getLength() - 1)) {
		    			//hmmm went through all the parameter name attribues and not a match
		    			//:(  need to do somethin so do a listIter.next for now and log it
		    			logger.warn("DARN NO MATCH ON PARAMETER NAME ATTRIBUTE");
		    			listIter.next();
		    		}
	    		}//for
	    		//doc.cloneNode(true)
	    	}//while
	    }//while

	    try {
		StringWriter outputToolDoc = new StringWriter();
		logger.warn("transform the tooldoc to a string for debugging in the createCeaToolDoc");
		TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(outputToolDoc));
		logger.warn("tooldoc to be written to file and submitted: = " + outputToolDoc);
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }

	    logger.warn("ok were done lets return the doc");
	    
	    return doc;
	}
}
