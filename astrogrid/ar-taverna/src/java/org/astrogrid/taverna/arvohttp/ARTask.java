/**
 * 
 */
package org.astrogrid.taverna.arvohttp;

import java.util.HashMap;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;

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

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.*;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.InvalidArgumentException;

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
	
	private static Logger logger = Logger.getLogger(ARProcessor.class);

	// seems like we get a constructor which passes a reference to the processor.
	public ARTask(Processor p) {
		logger.warn("start constructor in ARTask");
		this.processor = (ARProcessor)p;
	}
	private final ARProcessor processor;
	
	private Resource[] getResources(String paramValue, String serviceType) throws ACRException, ServiceException, NotFoundException, URISyntaxException {
		logger.warn("finding Resource(s) serviceType = " + serviceType);
		Resource []resources = null;
		ACR acr = SingletonACR.getACR();
		Registry reg = (Registry)acr.getService(Registry.class);
		if(paramValue == null || paramValue.trim().length() == 0) {
			logger.warn("grabbing all resources");
			if(serviceType.equals("CONE")) {
				Cone cone = (Cone)acr.getService(Cone.class);				
				String xqueryCone = cone.getRegistryXQuery();
				resources = reg.xquerySearch(xqueryCone);
			}else if(serviceType.equals("SIAP")) {
				Siap siap = (Siap)acr.getService(Siap.class);				
				String xquerySiap = siap.getRegistryXQuery();
				resources = reg.xquerySearch(xquerySiap);				
			}
		}else if(paramValue.startsWith("ivo://")) {
			logger.warn("looking for single resoruce");
			Resource res = reg.getResource(new URI(paramValue));
			if(serviceType.equals("CONE") && res.getType().indexOf("Cone") == -1) {
				resources = new Resource[1];
				resources[0] = res;				
			}else if(serviceType.equals("SIAP") && res.getType().indexOf("SimpleImage") == -1) {
				resources = new Resource[1];
				resources[0] = res;				
			}
		}else {
			logger.warn("doing a keyword search");

			if(paramValue.indexOf(" and ") != -1) {
				resources = reg.keywordSearch(paramValue, false);
			}else {
				resources = reg.keywordSearch(paramValue, true);
			}
			if(resources != null) {
				for(int j = 0;j < resources.length;j++) {
					if(serviceType.equals("CONE") && resources[j].getType().indexOf("Cone") == -1) {
						resources[j] = null;
					}else if(serviceType.equals("SIAP") && resources[j].getType().indexOf("SimpleImage") == -1) {
						resources[j] = null;
					}//else
				}//for
			}//if
		}//else
		if(resources != null && resources.length > 0)
			logger.warn("done number of resources returned = " + resources.length);
		else
			logger.warn("done but no resources to be returned");
		
		return resources;
	}
	
	private String processSiap(Siap siap,Resource res, double ra, double dec, double size /*, String outputLoc*/) throws InvalidArgumentException, NotFoundException, ServiceException, TransformerException, TransformerConfigurationException {
		URI resURI = getResourceAccessURI(res);
		/*
		if(outputLoc != null && outputLoc.trim().length() > 0) {
			siap.executeAndSave(siap.constructQuery(resURI,ra,dec,size), new URI(outputLoc));
			return outputLoc;
			//resultMap.put("result0", outputLoc);
		}
		*/
		logger.warn("processingsiap for " + res.getId());
		Document resultDoc = siap.executeVotable(siap.constructQuery(resURI,ra,dec,size));
	    StringWriter output = new StringWriter();
	    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(resultDoc), new StreamResult(output));
		return output.toString();
	}
	
	private String processCone(Cone cone,Resource res, double ra, double dec, double size /*, String outputLoc*/) throws InvalidArgumentException, NotFoundException, ServiceException,  TransformerException, TransformerConfigurationException {
		URI resURI = getResourceAccessURI(res);
		/*
		if(outputLoc != null && outputLoc.trim().length() > 0) {
			cone.executeAndSave(cone.constructQuery(resURI,ra,dec,size), new URI(outputLoc));
			return outputLoc;
			//resultMap.put("result0", outputLoc);
		}
		*/
		logger.warn("processing cone for " + res.getId());
		Document resultDoc = cone.executeVotable(cone.constructQuery(resURI,ra,dec,size));
	    StringWriter output = new StringWriter();
	    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(resultDoc), new StreamResult(output));
		return output.toString();
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
		logger.info("start execute in ARTask");

		String name = processor.getName();
	    Map outputMap = new HashMap();
	    Map resultMap = new HashMap();
	    Double ra, dec, size;
	    String outputLoc;
	    URI resURI;
	    Cone cone = null;
	    Siap siap = null;		    
		
		try {
		    ACR acr = SingletonACR.getACR();
		    DataThing parameterThing = (DataThing)arg0.get("Ivorn or Registry Keyword");
		    String paramValue = (String)parameterThing.getDataObject();
		    Registry reg = (Registry)acr.getService(Registry.class);
		    Resource []res = getResources(paramValue,name);
		    boolean allNulls = true;
		    if(res == null || res.length == 0) {
		    	throw new TaskExecutionException("No Resources Found that corresponded to type = " + name);
		    }
		    for(int j = 0;j < res.length;j++) {
		    	if(res[j] != null) {
		    		allNulls = false;
		    		j = res.length;
		    	}
		    }
		    if(allNulls) {
		    	throw new TaskExecutionException("No Resources Found that corresponded to type = " + name);		    	
		    }
		    ra = (Double)((DataThing)arg0.get("RA")).getDataObject();
    		dec = (Double)((DataThing)arg0.get("DEC")).getDataObject();
    		size = (Double)((DataThing)arg0.get("SIZE")).getDataObject();		    
    		//outputLoc = (String)((DataThing)arg0.get("OutputLocation")).getDataObject();
    		String result;
		    for(int j = 0;j < res.length;j++) {
		    	if(res[j] != null) {
			    	if(name.equals("SIAP")) {
			    		if(siap == null) {
			    			siap = (Siap)acr.getService(Siap.class);
			    		}
			    		result =  processSiap(siap,res[j],ra.doubleValue(),dec.doubleValue(),size.doubleValue()/*,outputLoc*/);
			    		resultMap.put("SiapResult" + res[j].getId().toString(), result);			    		
			    	}else if(name.equals("CONE")) {
			    		if(cone == null) {
			    			cone = (Cone)acr.getService(Cone.class);
			    		}
			    		result = processCone(cone,res[j],ra.doubleValue(),dec.doubleValue(),size.doubleValue()/*,outputLoc*/);
			    		resultMap.put("ConeResult_" + res[j].getId().toString(), result);
			    	}//else
		    	}//if
		    }//for
		    logger.warn("finised AR task placing things in output map.  the result map size = " + resultMap.size());
		    outputMap.put("result",DataThingFactory.bake(resultMap));
		    logger.info("end execute in ARTask");
		    return outputMap;
		} catch (Throwable x) {
			TaskExecutionException tee =  new TaskExecutionException("Failed to execute: " + processor.getName(),x);
			tee.initCause(x);
			throw tee;
		} 
	}
	
	private URI getResourceAccessURI(Resource r) throws InvalidArgumentException {
        // hope for now we've only got one service capability.
        if (! (r instanceof Service)) {
        	throw new InvalidArgumentException("Resouce found is not a known type of 'Service' id = " + r.getId());
        }
        Service s = (Service)r;
        if (s.getCapabilities().length == 0 || s.getCapabilities()[0].getInterfaces().length == 0 || s.getCapabilities()[0].getInterfaces()[0].getAccessUrls().length == 0){
        	throw new InvalidArgumentException("Resource does not provide an access URL.  id = " + r.getId());
        }
        return s.getCapabilities()[0].getInterfaces()[0].getAccessUrls()[0].getValueURI();
	}
	//returns true if c is some kind of AR bean - various heuristics used.
	// pity we've not got a marker interface.
	private boolean isBean(Class c) {
		String n = c.getName();
		return  (! c.isInterface()) 
		&& (n.endsWith("Information") || n.endsWith("Descriptor") || n.endsWith("Bean"));
	}
	
}
