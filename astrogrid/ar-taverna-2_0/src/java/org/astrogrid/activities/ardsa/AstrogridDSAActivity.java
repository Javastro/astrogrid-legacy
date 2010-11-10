package org.astrogrid.activities.ardsa;

import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.reference.ReferenceService;
/*
import net.sf.taverna.t2.reference.ReferenceServiceException;
import net.sf.taverna.t2.workflowmodel.EditException;
import net.sf.taverna.t2.workflowmodel.EditsRegistry;
import net.sf.taverna.t2.workflowmodel.OutputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractActivity;
*/

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.w3c.dom.Document;



import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.Marshaller;


import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;


public class AstrogridDSAActivity extends AbstractAsynchronousActivity<AstrogridDSAActivityConfigurationBean> {

	private AstrogridDSAActivityConfigurationBean configurationBean;
	
	@Override
	public void configure(AstrogridDSAActivityConfigurationBean configurationBean)
			throws ActivityConfigurationException {
		this.configurationBean = configurationBean;
		generatePorts();
	}
	
	@Override
	public AstrogridDSAActivityConfigurationBean getConfiguration() {
		return configurationBean;
	}
	
	//current samples from soaplabactivity
	public void executeAsynch(final Map<String, T2Reference> data,
			final AsynchronousActivityCallback callback) {
		
		callback.requestRun(new Runnable() {

			public void run() {
				final ReferenceService referenceService = callback.getContext().getReferenceService();

				final Map<String, T2Reference> outputData = new HashMap<String, T2Reference>();
				try {
					ACR acr = org.astrogrid.ar.SingletonACR.getACR();
					Applications apps = (Applications)acr.getService(Applications.class);
					//if(name.equals("DSA")) {	
						Object ivorn = referenceService.renderIdentifier(data
							.get("ivorn"), String.class, callback.getContext());
						Object serviceIvorn = referenceService.renderIdentifier(data
								.get("service ivorn"), String.class, callback.getContext());
						Object query = referenceService.renderIdentifier(data
							.get("query"), String.class, callback.getContext());
						Object saveLoc = referenceService.renderIdentifier(data
								.get("save location"), String.class, callback.getContext());
						
						//toolStruct = createCEAToolMap(arg0);
						URI ceaAppIvorn = new URI((String)ivorn);
						URI ceaServiceIvorn = null;
						Object temp;
						if(serviceIvorn != null && ((String)serviceIvorn).trim().length() > 0) {
							ceaServiceIvorn = new URI((String)serviceIvorn);
						}
						//String ceaInterfaceName = (String)((DataThing)input.get("Interface Name")).getDataObject();
						String ceaInterfaceName = "ADQL";
						Map toolStruct = apps.createTemplateStruct(ceaAppIvorn,ceaInterfaceName);
						
						Hashtable inputFromCEATemplate = (Hashtable)toolStruct.get("input");
						
					    Iterator keyIter = inputFromCEATemplate.keySet().iterator();
					    String paramName;
					    String paramValue;
					    Object parameterThing;
					    while(keyIter.hasNext()) {
					    	paramName = (String)keyIter.next();
					    	if(data.containsKey(paramName)) {
					    		//found great
					    		 
					    		 parameterThing = referenceService.renderIdentifier(data.get(paramName), String.class, callback.getContext());
					    		 //logger.warn("the parameterThing toString = " + parameterThing.toString());
					    		 Hashtable inputVals = (Hashtable)inputFromCEATemplate.get(paramName);
					    		 //change value and indirect now.
					    		 paramValue = (String)parameterThing;
					    		 if(paramValue.startsWith("http://") || paramValue.startsWith("ftp://") ||
					    		    paramValue.startsWith("ivo://")) {
					    			inputVals.put("indirect",new Boolean(true));
					    		 }else {
					    			inputVals.put("indirect",new Boolean(false));
					    		 }
					    		 inputVals.put("value",paramValue.trim());
					    	}else {
					    		//not found assume an optional param, but probably good to
					    		//check and throw an exceptin if minOccurs is not 0.
					    		inputFromCEATemplate.remove(paramName);
					    	}
					    }
					    if(saveLoc != null && ((String)saveLoc).trim().length() > 0) {
					    	paramValue = (String)saveLoc;
					    	if(paramValue != null && paramValue.trim().length() > 0) {
					    		 if(paramValue.startsWith("http://") || paramValue.startsWith("ftp://") ||
					 	    		    paramValue.startsWith("ivo://")) {
					    			 Hashtable outputFromCEATemplate = (Hashtable)toolStruct.get("output");
					    			 Hashtable outputVals = (Hashtable)outputFromCEATemplate.get("Result");
					    			 outputVals.put("indirect",new Boolean(true));
					    			 outputVals.put("value",paramValue.trim());
					    		 }
					    	}
					    }

						Document toolDoc = createToolDocument(toolStruct);
						StringWriter outputToolDoc = new StringWriter();
						//logger.warn("transform the tooldoc to a string for debugging");
						TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(outputToolDoc));
						//logger.warn("tooldoc to be written to file and submitted: = " + outputToolDoc);
						//logger.warn("creating file");
						java.io.File toolFile = java.io.File.createTempFile("taverna",".wkfl");
						toolFile.deleteOnExit();
						//comment out this one it has problems in windows use the Fileoutputstream instead.
						//TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(toolFile));
						TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(new java.io.FileOutputStream(toolFile)));
						//logger.warn("set file to delete on exit file name = " + toolFile.toURI());
						URI executionID;
						if(ceaServiceIvorn == null) {
							executionID = apps.submitStored(toolFile.toURI());
						}else {
							executionID = apps.submitStoredTo(toolFile.toURI(),ceaServiceIvorn);
						}	
						
						Map finalResultMap = new HashMap();						
						int tempCounter = 0;
						String finalExecutionInfo = null;
						boolean discovered = false;
						//logger.warn("about to try and execute id = " + executionID);
						while(!discovered && tempCounter < 5) {
							try {
								//logger.warn("firing off exectuion");
								//lookout = (Lookout)acr.getService(Lookout.class);
								ExecutionInformation execInfo = apps.getExecutionInformation(executionID);
								discovered = true;
								while(execInfo.getStatus().equals(ExecutionInformation.INITIALIZING) ||
										  execInfo.getStatus().equals(ExecutionInformation.RUNNING) ||
										  execInfo.getStatus().equals(ExecutionInformation.UNKNOWN) ||
										  execInfo.getStatus().equals(ExecutionInformation.PENDING)) {
									//logger.warn("need to sleep hasn't completed, status = " + execInfo.getStatus());
									Thread.sleep(1500);
									//lookout.refresh();
									execInfo = apps.getExecutionInformation(executionID);
								}//while
								finalExecutionInfo = execInfo.toString();					
								//logger.warn("finished with execution the final status = " + execInfo.getStatus());					
								finalResultMap = apps.getResults(executionID);
								//logger.warn("map size after finish  = " + finalResultMap.size());
								if(finalResultMap.size() == 0) {						
									finalResultMap.put("Error", "Problem with execution info = " + execInfo.toString());
								}

							}catch(org.astrogrid.acr.NotFoundException ne) {
								if(tempCounter == 5) {
									throw ne;
								}					
								//logger.warn("did not find executionID lets try several more times before quiting " + executionID);
								tempCounter++;
								Thread.sleep(1000);
							}
						}//while
						
						/*
						outputData.put("execution_id", referenceService.register(value,
								outputPort.getDepth(), true, callback
										.getContext()));
						*/
						outputData.put("results", referenceService.register(finalResultMap,
								0, true, callback.getContext()));						
						outputData.put("execution_id", referenceService.register(executionID.toString(),
								0, true, callback.getContext()));
						outputData.put("execution_info", referenceService.register(finalExecutionInfo,
								0, true, callback.getContext()));
				} catch(Exception e1) {
					e1.printStackTrace();
				}
				//data.
			}
		});
		
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
	

	
	private void generatePorts() throws ActivityConfigurationException {
		//this.
		addInput("ivorn",0,true,null,String.class);
		addInput("query",0,true,null,String.class);
		addInput("save location",0,true,null,String.class);

		addOutput("results",0);
		addOutput("execution_id",0);
		addOutput("execution_info",0);
		//check
		//addInput("")
	}
}
