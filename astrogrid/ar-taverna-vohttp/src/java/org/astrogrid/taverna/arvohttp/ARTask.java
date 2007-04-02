/**
 * 
 */
package org.astrogrid.taverna.arvohttp;

import java.util.HashMap;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ArrayList;
import java.net.URL;

import org.apache.log4j.Logger;

import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.IOException;
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
import org.astrogrid.acr.ivoa.Ssap;
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
	
	private static Logger logger = Logger.getLogger(ARTask.class);

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
			String []ivorns = paramValue.split(",");
			resources = new Resource[ivorns.length];
			for(int k = 0;k < ivorns.length;k++) {
				Resource res = reg.getResource(new URI(ivorns[k]));
				if(serviceType.equals("CONE") && res.getType().indexOf("Cone") != -1) {
					logger.warn("yes cone");
					resources[k] = res;				
				}else if(serviceType.equals("SIAP") && res.getType().indexOf("SimpleImage") != -1) {
					logger.warn("yes siap");
					resources[k] = res;				
				}else {
					resources[k] = null;
				}
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
	
	private URL getSiapURL(Siap siap,Resource res, double ra, double dec, double size /*, String outputLoc*/) throws InvalidArgumentException, NotFoundException, ServiceException, TransformerException, java.net.MalformedURLException, TransformerConfigurationException {
		logger.warn("getSiapURL");
		URI resURI = getResourceAccessURI(res);

		logger.warn("processingsiap for " + res.getId() + " and uri = " + resURI);
		URL siapURL = siap.constructQuery(resURI,ra,dec,size);
		logger.warn("url for siapURL to call  = " + siapURL);
		urlMap.put(res.getId().toString(), siapURL.toString());		
		return siapURL;
	}
			
	private String processSiap(Siap siap,Resource res, double ra, double dec, double size /*, String outputLoc*/) throws InvalidArgumentException, NotFoundException, ServiceException, TransformerException, java.net.MalformedURLException, TransformerConfigurationException, IOException {
		logger.warn("processSiap");
		
		//Document resultDoc = siap.executeVotable(getSiapURL(siap,res,ra,dec,size));
	    StringWriter output = new StringWriter();
	    logger.warn("done with executeVotable");
	    //TransformerFactory.newInstance().newTransformer().transform(new DOMSource(resultDoc), new StreamResult(output));
	    TransformerFactory.newInstance().newTransformer().transform(new StreamSource(getSiapURL(siap,res,ra,dec,size).openStream()), new StreamResult(output));	    
	    logger.warn("done return output");
		return output.toString();
	}
	
	private URL getConeURL(Cone cone,Resource res, double ra, double dec, double size /*, String outputLoc*/) throws InvalidArgumentException, NotFoundException, ServiceException,  TransformerException, TransformerConfigurationException {
		URI resURI = getResourceAccessURI(res);
		URL coneURL = cone.constructQuery(resURI,ra,dec,size);
		logger.warn("url for coneURL to call = " + coneURL);
		urlMap.put(res.getId().toString(), coneURL.toString());
		return coneURL;
	}
	
	
	private String processCone(Cone cone,Resource res, double ra, double dec, double size /*, String outputLoc*/) throws InvalidArgumentException, NotFoundException, ServiceException,  TransformerException, TransformerConfigurationException, IOException {
		logger.warn("processing cone for " + res.getId());
		//Document resultDoc = cone.executeVotable(getConeURL(cone,res,ra,dec,size));
	    StringWriter output = new StringWriter();
	    //TransformerFactory.newInstance().newTransformer().transform(new DOMSource(resultDoc), new StreamResult(output));
	    TransformerFactory.newInstance().newTransformer().transform(new StreamSource(getConeURL(cone,res,ra,dec,size).openStream()), new StreamResult(output));	    
		return output.toString();
	}	
	
	private URL getSSAPURL(Ssap ssap,Resource res, double ra, double dec, double size /*, String outputLoc*/) throws InvalidArgumentException, NotFoundException, ServiceException,  TransformerException, TransformerConfigurationException {
		URI resURI = getResourceAccessURI(res);
		URL ssapURL = ssap.constructQuery(resURI,ra,dec,size);
		logger.warn("url for coneURL to call = " + ssapURL);
		urlMap.put(res.getId().toString(), ssapURL.toString());
		return ssapURL;
	}
	
	private Map urlMap;
	
	
	private String processSSAP(Ssap ssap,Resource res, double ra, double dec, double size /*, String outputLoc*/) throws InvalidArgumentException, NotFoundException, ServiceException,  TransformerException, TransformerConfigurationException, IOException {
		logger.warn("processing cone for " + res.getId());
		//Document resultDoc = ssap.executeVotable(getSSAPURL(ssap,res,ra,dec,size));
	    StringWriter output = new StringWriter();
	    //TransformerFactory.newInstance().newTransformer().transform(new DOMSource(resultDoc), new StreamResult(output));
	    TransformerFactory.newInstance().newTransformer().transform(new StreamSource(getSSAPURL(ssap,res,ra,dec,size).openStream()), new StreamResult(output));	    
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
		logger.warn("start execute in ARTask");

		String name = processor.getName();
	    Map outputMap = new HashMap();
	    Map resultMap = new HashMap();
	    Double ra, dec, size;
	    String outputLoc;
	    URI resURI;
	    Cone cone = null;
	    Siap siap = null;
	    Ssap ssap = null;
	    urlMap = new HashMap();
		logger.warn("Name = " + name);
		try {
		    ACR acr = SingletonACR.getACR();
		    logger.warn("got acr in ARTask now get DataThing");
		    DataThing parameterThing = (DataThing)arg0.get("Ivorn or Registry Keywords");
		    Object []mapArr = arg0.keySet().toArray();
		    for(int l = 0;l < mapArr.length;l++) {
		    	logger.warn("mapArr index = " + l + " and val = " + mapArr[l]);
		    }
		    logger.warn("have datathing so get the paramValue of it3");
		    if(parameterThing == null) {
		    	logger.warn("parameterThing is null");
		    }
		    
		    Object test = parameterThing.getDataObject();
		    if(test == null) {
		    	logger.warn("test object was null :(");
		    }
		    logger.warn("object test tostring = " + test.toString());
		    if(test instanceof String){
		    	logger.warn("yes it is a string");
		    }else {
		    	logger.warn("no it is not a string");
		    }
		    String paramValue = (String)parameterThing.getDataObject();
		    logger.warn("paramValue = " + paramValue);
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
		    logger.warn("grab RA object and analyze");
		    test = ((DataThing)arg0.get("RA")).getDataObject();
		    if(test instanceof String) {
		    	logger.warn("RA dataobject is a string :(");
		    }else if(test instanceof Double) {
		    	logger.warn("RA is a instanceof Double");
		    }else {
		    	logger.warn("darn RA not sure what insanceof it is");
		    }
		    
		    ra = Double.parseDouble((String)((DataThing)arg0.get("RA")).getDataObject());
    		dec = Double.parseDouble((String)((DataThing)arg0.get("DEC")).getDataObject());
    		size = Double.parseDouble((String)((DataThing)arg0.get("SIZE")).getDataObject());
    		String saveURLS = (String)((DataThing)arg0.get("Only URLS Needed")).getDataObject();
    		
    		logger.warn("ok process things ra = " + ra + " dec = " + dec + " size = " + size);
    		//outputLoc = (String)((DataThing)arg0.get("OutputLocation")).getDataObject();
    		String result;
		    for(int j = 0;j < res.length;j++) {
		    	if(res[j] != null) {
			    	if(name.equals("SIAP")) {
			    		if(siap == null) {
			    			siap = (Siap)acr.getService(Siap.class);
			    		}
			    		logger.warn("rpcess the siap");
			    		if(saveURLS != null && saveURLS.equals("true") ) {
			    			result = getSiapURL(siap,res[j], ra, dec, size).toString();
			    		}else {
				    		result =  processSiap(siap,res[j], ra, dec, size/*,outputLoc*/);
				    		logger.warn("ok got a result getId = " + res[j].getId() + " and string result = " + result);
				    		resultMap.put("SiapResult_" + res[j].getId().toString().replaceAll("[^\\w*]","_"), result);
			    		}
			    	}else if(name.equals("CONE")) {
			    		if(cone == null) {
			    			cone = (Cone)acr.getService(Cone.class);
			    		}
			    		if(saveURLS != null && saveURLS.equals("true") ) {
			    			result = getConeURL(cone,res[j], ra, dec, size).toString();
			    		}else {
				    		result = processCone(cone,res[j], ra, dec, size/*,outputLoc*/);
				    		resultMap.put("ConeResult_" + res[j].getId().toString().replaceAll("[^\\w*]","_"), result);			    			
			    		}
			    		
			    	}//else
			    	else if(name.equals("SSAP")) {
			    		if(ssap == null) {
			    			ssap = (Ssap)acr.getService(Ssap.class);
			    		}
			    		if(saveURLS != null && saveURLS.equals("true") ) {
			    			result = getSSAPURL(ssap,res[j], ra, dec, size).toString();
			    		}else {			    		
				    		result = processSSAP(ssap,res[j], ra, dec, size/*,outputLoc*/);
				    		resultMap.put("SsapResult_" + res[j].getId().toString().replaceAll("[^\\w*]","_"), result);
			    		}
			    	}//else			    	
		    	}//if
		    }//for
		    logger.warn("finised AR task placing things in output map.  the result map size = " + resultMap.size());
		    outputMap.put("result",DataThingFactory.bake(resultMap));
		    logger.warn("lets make the Ivorns and URLs list the size = " + urlMap.size());
		    outputMap.put("Ivorns",DataThingFactory.bake(new ArrayList(urlMap.keySet())));
		    outputMap.put("URLs",DataThingFactory.bake(new ArrayList(urlMap.values())));
    		if(saveURLS == null || !saveURLS.equals("true") ) {
    		    outputMap.put("ResultList",DataThingFactory.bake(new ArrayList(resultMap.values())));    			
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
