/**
 * 
 */
package org.astrogrid.taverna.armyspace;

import java.util.HashMap;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.NotFoundException;

import org.w3c.dom.Document;
import org.astrogrid.acr.astrogrid.Myspace;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.resource.*;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.InvalidArgumentException;

import java.util.Vector;
import java.net.URI;
import java.net.URISyntaxException;

import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.baclava.factory.DataThingFactory;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scuflworkers.ProcessorTaskWorker;
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
	    Map outputMap = new HashMap();
	    Map resultMap;
	    URI resURI;
		logger.warn("Name = " + name);
		Vector savedOutput;
		List uriVector;
		List votableURIVector;
		try {
		    ACR acr = SingletonACR.getACR();
		    Myspace myspace = (Myspace)acr.getService(Myspace.class);
		    String myspaceDirectory = (String)((DataThing)arg0.get("Myspace Directory or File")).getDataObject();
		    Object mainInputObj;	
		    String recurseDir, votableOnly;
		    if(name.equals("Save")) {
			    mainInputObj = ((DataThing)arg0.get("Object or List")).getDataObject();
				MyspaceWriter mw = new MyspaceWriter(myspace);
				savedOutput = mw.writeObject(mainInputObj,new URI(myspaceDirectory),"FromTaverna");
				uriVector = (Vector)savedOutput.get(0);
				outputMap.put("Myspace URI List",DataThingFactory.bake(uriVector));				
		    }else if(name.equals("Save_For_VOTables")) {
			    mainInputObj = ((DataThing)arg0.get("Object or List")).getDataObject();		    	
		    	Object urlList = ((DataThing)arg0.get("Get Column/UCD Values for URLS to Save in Myspace")).getDataObject();
		    	Object nameList = ((DataThing)arg0.get("Get Column/UCD Values for Saved Myspace Names")).getDataObject();
				MyspaceWriter mw = new MyspaceWriter(myspace,true,(String [])urlList,(String [])nameList);
				savedOutput = mw.writeObject(mainInputObj,new URI(myspaceDirectory),"FromTaverna");
				uriVector = (List)savedOutput.get(0);
				votableURIVector = (List)savedOutput.get(1);
				outputMap.put("Myspace URI List",DataThingFactory.bake(uriVector));
				outputMap.put("Myspace URI List of VOTables",DataThingFactory.bake(votableURIVector));
				
		    	//OutputPort name = "Myspace URI List" 
		    	//OutputPort name = "Myspace URI List of VOTables"
		    	
		    }else if(name.equals("Fetch")) {
		    	//myspaceDirectory
		    	recurseDir = (String)((DataThing)arg0.get("Recurse Directories")).getDataObject();
		    	votableOnly = (String)((DataThing)arg0.get("Only VOTables")).getDataObject();
		    	MyspaceFetcher mf = new MyspaceFetcher(myspace);
		    	resultMap = mf.fetchObjects(myspaceDirectory,Boolean.parseBoolean(recurseDir),false,Boolean.parseBoolean(votableOnly));
		    	//Outputport name = ""Map of Results"
		    	outputMap.put("Map of Results", DataThingFactory.bake(resultMap));
		    	outputMap.put("ResultListName", DataThingFactory.bake(new ArrayList(resultMap.keySet())));
		    	outputMap.put("ResultListValue",DataThingFactory.bake(new ArrayList(resultMap.values())));
		    	
		    }else if(name.equals("Fetch_String_Content")) {
		    	recurseDir = (String)((DataThing)arg0.get("Recurse Directories")).getDataObject();
		    	votableOnly = (String)((DataThing)arg0.get("Only VOTables")).getDataObject();
		    	MyspaceFetcher mf = new MyspaceFetcher(myspace);
		    	resultMap = mf.fetchObjects(myspaceDirectory,Boolean.parseBoolean(recurseDir),true,Boolean.parseBoolean(votableOnly));
		    	outputMap.put("Map of Results", resultMap);
		    	outputMap.put("ResultListName", new ArrayList(resultMap.keySet()));
		    	outputMap.put("ResultListValue", new ArrayList(resultMap.values()));
		    }
		    logger.warn("end execute in ARTask");
		    return outputMap;
		} catch (Throwable x) {
			logger.warn("error do the stacktrace");
			x.printStackTrace();
			logger.warn("throw the taskexecutionexception");
			throw new TaskExecutionException("Failed to execute: " + processor.getName(),x);
		} 
	}
	
	//returns true if c is some kind of AR bean - various heuristics used.
	// pity we've not got a marker interface.
	private boolean isBean(Class c) {
		String n = c.getName();
		return  (! c.isInterface()) 
		&& (n.endsWith("Information") || n.endsWith("Descriptor") || n.endsWith("Bean"));
	}
	
}
