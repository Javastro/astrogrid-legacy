/**
 * 
 */
package org.astrogrid.taverna.arcea;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

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
//import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.acr.astrogrid.ExecutionInformation;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.votable.VOTableBuilder;

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
	
	  private StarTable getStarTable(String votableXML) {
	    	try {
		    	ByteArrayInputStream bai = new ByteArrayInputStream(votableXML.getBytes());
		    	StarTable table = (new StarTableFactory()).makeStarTable(bai, new VOTableBuilder());
		    	return table;
	    	}catch(IOException ioe) {
	    		ioe.printStackTrace();
	    	}
	    	return null;
	    }
	        
	    private StarTable getStarTable(URL url) {
	    	try {
	    		return ((new StarTableFactory()).makeStarTable(url));
	    	}catch(IOException ioe) {
	    		ioe.printStackTrace();
	    	}
	    	return null;
	    }
	    
	    private List getData(StarTable table, String nameCol) throws IOException {
	    	int rowCount = (int)table.getRowCount();
	    	int j = 0;
	    	int nameColSeq = 0;
		    	for(int i = 0;i < table.getColumnCount();i++) {
		    		if(table.getColumnInfo(i).getUCD().equals(nameCol) ||
		    		   table.getColumnInfo(i).getName().equals(nameCol)) {
		    			nameColSeq = i;
		    		    j++;
		    		    i = table.getColumnCount();
		    		}//if
		    	}//for
	    	j = 0;
	    	
	    	RowSequence rseq = table.getRowSequence();
	    	ArrayList resultArray = new ArrayList();
	    	Object val;
		     try {
		         while ( rseq.next() ) {
		        	 val = rseq.getCell(nameColSeq);
		        	 resultArray.add((String)val);
		         }//while
		     }finally {
		         rseq.close();
		     }//finally
		     return resultArray;
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

		//String name = processor.getName();
		String name = processor.getCommonName();
		Object temp;
	    Map outputMap = new HashMap();		    

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
			if(name.equals("VOTABLE_Fetch_Field")) {
				String votable = ((String)((DataThing)arg0.get("VOTABLE")).getDataObject()).trim();
				String column = ((String)((DataThing)arg0.get("Column or UCD")).getDataObject()).trim();
				StarTable st;
				if(votable.startsWith("http")) {
					try {
						URL testURL = new URL(votable);
						st = getStarTable(testURL);
						outputMap.put("ResultList",DataThingFactory.bake(getData(st,column)));						
					}catch(Exception e) {
						st = null;
					}
				}else {
					st = getStarTable(votable);
					outputMap.put("ResultList",DataThingFactory.bake(getData(st,column)));
				}
				return outputMap;				
			}

			
			
			ACR acr = SingletonACR.getACR();
			Applications apps = (Applications)acr.getService(Applications.class);
			//Lookout lookout;
			Map toolStruct;
			//if(name.equals("DSA")) {				
				toolStruct = createCEAToolMap(arg0);
				Document toolDoc = createToolDocument(toolStruct);
				StringWriter outputToolDoc = new StringWriter();
				logger.warn("transform the tooldoc to a string for debugging");
				TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(outputToolDoc));
				logger.warn("tooldoc to be written to file and submitted: = " + outputToolDoc);
				logger.warn("creating file");
				java.io.File toolFile = java.io.File.createTempFile("taverna",".wkfl");
				toolFile.deleteOnExit();
				//comment out this one it has problems in windows use the Fileoutputstream instead.
				//TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(toolFile));
				TransformerFactory.newInstance().newTransformer().transform(new DOMSource(toolDoc), new StreamResult(new java.io.FileOutputStream(toolFile)));
				logger.warn("set file to delete on exit file name = " + toolFile.toURI());
				//save the toolDoc file
				URI executionID;
				if(arg0.containsKey("Optional CeaService Ivorn")) {
					temp = ((DataThing)arg0.get("Optional CeaService Ivorn")).getDataObject();
					URI ceaServiceIvorn = new URI(new String((  ((String)temp).trim()   )));
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
			String finalExecutionInfo = null;
			boolean discovered = false;
			logger.warn("about to try and execute id = " + executionID);
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
						Thread.sleep(1500);
						//lookout.refresh();
						execInfo = apps.getExecutionInformation(executionID);
					}//while
					finalExecutionInfo = execInfo.toString();					
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
		    outputMap.put("ResultList",DataThingFactory.bake(new ArrayList(finalResultMap.values())));
		    outputMap.put("ExecutionID",DataThingFactory.bake(executionID.toString()));
		    outputMap.put("ExecutionInformation",DataThingFactory.bake(finalExecutionInfo));		    
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
		
	private Map createCEAToolMap(Map input) throws TaskExecutionException, ACRException, URISyntaxException {

		logger.warn("start inputObjects in ARTask");
		
		URI ceaAppIvorn = new URI((String)((DataThing)input.get("CeaApp Ivorn")).getDataObject());
		URI ceaServiceIvorn;
		Object temp;
		if(input.containsKey("Optional CeaService Ivorn")) {
			temp = ((DataThing)input.get("Optional CeaService Ivorn")).getDataObject();
			if(temp != null && ((String)temp).trim().length() > 0) {
				ceaServiceIvorn = new URI(new String((   ((String)temp).trim()   )));
			}
		}
		String ceaInterfaceName = (String)((DataThing)input.get("Interface Name")).getDataObject();

	    ACR acr = SingletonACR.getACR();
		Applications apps = (Applications)acr.getService(Applications.class);
		
		//CeaApplication
		Map toolStruct = apps.createTemplateStruct(ceaAppIvorn,ceaInterfaceName.toUpperCase());
		
		Hashtable inputFromCEATemplate = (Hashtable)toolStruct.get("input");
		
	    Iterator keyIter = inputFromCEATemplate.keySet().iterator();
	    String paramName;
	    String paramValue;
	    DataThing parameterThing;
	    while(keyIter.hasNext()) {
	    	paramName = (String)keyIter.next();
	    	if(input.containsKey(paramName)) {
	    		//found great
	    		 parameterThing = (DataThing)input.get(paramName);
	    		 logger.warn("the parameterThing toString = " + parameterThing.toString());
	    		 Hashtable inputVals = (Hashtable)inputFromCEATemplate.get(paramName);
	    		 //change value and indirect now.
	    		 paramValue = (String)parameterThing.getDataObject();
	    		 if(paramValue.startsWith("http://") || 
	    		    paramValue.startsWith("ftp://") ||
	    		    paramValue.startsWith("ivo://")  ||
	    		    paramValue.startsWith("vos://")) {
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
	    if(input.containsKey("Optional Result Saved")) {
	    	parameterThing = (DataThing)input.get("Optional Result Saved");
	    	paramValue = (String)parameterThing.getDataObject();
	    	if(paramValue != null && paramValue.trim().length() > 0) {
	    		 if(paramValue.startsWith("http://") || 
	    		    paramValue.startsWith("ftp://") ||
	    		    paramValue.startsWith("vos://") ||
	 	    		paramValue.startsWith("ivo://")) {
	    			 Hashtable outputFromCEATemplate = (Hashtable)toolStruct.get("output");
	    			 //Hashtable outputVals = (Hashtable)outputFromCEATemplate.get((String)outputFromCEATemplate.get("Result"));
	    			 Hashtable outputVals = (Hashtable)outputFromCEATemplate.get("Result");
	    			 outputVals.put("indirect",new Boolean(true));
	    			 outputVals.put("value",paramValue.trim());
	    		 }
	    	}
	    }
	    return toolStruct;
	}
	

}
