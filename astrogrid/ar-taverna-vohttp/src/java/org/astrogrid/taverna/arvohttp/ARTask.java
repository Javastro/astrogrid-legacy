/**
 * 
 */
package org.astrogrid.taverna.arvohttp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.TimeZone;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.astrogrid.taverna.arvohttp.registry.ARRegistrySearch;

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
import org.astrogrid.acr.astrogrid.Stap;
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
	
	private Resource[] getResources(List ivorns, String serviceType) throws ACRException, ServiceException, NotFoundException, URISyntaxException {
		Iterator iter = ivorns.iterator();
		String ivorn;
		Resource []res = new Resource[ivorns.size()];
		int i = 0;
		while(iter.hasNext()) {
			ivorn = ((String)iter.next()).trim();
			if(ivorn.startsWith("ivo://")) {
				logger.warn("in getResources(list..) ivorn = " + ivorn);
				res[i] = getResources(ivorn,serviceType)[0];
				i++;
			}//if
		}//while
		return res;
	}
	
	private Resource[] getResources(String paramValue, String serviceType) throws ACRException, ServiceException, NotFoundException, URISyntaxException {
		logger.warn("finding Resource(s) serviceType = " + serviceType);
		Resource []resources = null;
		ACR acr = SingletonACR.getACR();
		Registry reg = (Registry)acr.getService(Registry.class);
		if(paramValue == null || paramValue.trim().length() == 0) {
			logger.warn("grabbing all resources");
			if(serviceType.startsWith("CONE")) {
				Cone cone = (Cone)acr.getService(Cone.class);				
				String xqueryCone = cone.getRegistryXQuery();
				resources = reg.xquerySearch(xqueryCone);
			}else if(serviceType.startsWith("SIAP")) {
				Siap siap = (Siap)acr.getService(Siap.class);				
				String xquerySiap = siap.getRegistryXQuery();
				resources = reg.xquerySearch(xquerySiap);				
			}else if(serviceType.startsWith("STAP")) {
				Stap stap = (Stap)acr.getService(Stap.class);				
				String xqueryStap = stap.getRegistryXQuery();
				resources = reg.xquerySearch(xqueryStap);				
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
				}else if(serviceType.equals("STAP") && res.getType().indexOf("SimmpleTimeAccess") != -1) {
					logger.warn("yes siap");
					resources[k] = res;				
				}else {
					resources[k] = null;
				}
			}
		}else {
			logger.warn("doing a keyword search");
			if(serviceType.startsWith("CONE")) {
				resources = ARRegistrySearch.search(reg, paramValue,"near(@xsi:type,'*ConeSearch')");
			}else if(serviceType.startsWith("SIAP")) {
				resources = ARRegistrySearch.search(reg, paramValue,"near(@xsi:type,'*SimpleImageAccess')");
			}else if(serviceType.startsWith("STAP")) {
				resources = ARRegistrySearch.search(reg, paramValue,"near(@xsi:type,'*SimpleTimeAccess')");
			}else if(serviceType.startsWith("SSAP")) {
				resources = ARRegistrySearch.search(reg, paramValue,"near(@xsi:type,'*Spectral')");
			}
			//resources = (Resource [])resultVector.toArray();					
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
	
	private URL getStapURL(Stap stap,Resource res, Calendar start, Calendar end) throws InvalidArgumentException, NotFoundException, ServiceException, TransformerException, java.net.MalformedURLException, TransformerConfigurationException {
		logger.warn("getSiapURL");
		URI resURI = getResourceAccessURI(res);

		logger.warn("processingstap for " + res.getId() + " and uri = " + resURI);
		URL stapURL = stap.constructQuery(resURI,start, end);
		logger.warn("url for stapURL to call  = " + stapURL);
		urlMap.put(res.getId().toString(), stapURL.toString());		
		return stapURL;
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
	
	private String processStap(Stap stap,Resource res, Calendar start, Calendar end) throws InvalidArgumentException, NotFoundException, ServiceException, TransformerException, java.net.MalformedURLException, TransformerConfigurationException, IOException {
		logger.warn("processSiap");
		
		//Document resultDoc = siap.executeVotable(getSiapURL(siap,res,ra,dec,size));
	    StringWriter output = new StringWriter();
	    logger.warn("done with executeVotable");
	    //TransformerFactory.newInstance().newTransformer().transform(new DOMSource(resultDoc), new StreamResult(output));
	    TransformerFactory.newInstance().newTransformer().transform(new StreamSource(getStapURL(stap,res,start, end).openStream()), new StreamResult(output));	    
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

		//String name = processor.getName();
		String name = processor.getCommonName();
	    Map outputMap = new HashMap();
	    Map resultMap = new HashMap();
	    ArrayList errorList = new ArrayList();
	    double ra = Double.NaN;
	    double dec = Double.NaN;
	    double size = Double.NaN;
	    
	    Calendar start = Calendar.getInstance();
	    Calendar end = Calendar.getInstance();
	    String outputLoc;
	    URI resURI;
	    Cone cone = null;
	    Siap siap = null;
	    Ssap ssap = null;
	    Stap stap = null;
	    urlMap = new HashMap();
		logger.warn("Name = " + name);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		try {
		    ACR acr = SingletonACR.getACR();
		    logger.warn("got acr in ARTask now get DataThing");
		    
		    Object []mapArr = arg0.keySet().toArray();
		    for(int l = 0;l < mapArr.length;l++) {
		    	logger.warn("mapArr index = " + l + " and val = " + mapArr[l]);
		    }

		    
		  
		    Registry reg = (Registry)acr.getService(Registry.class);
		    Resource []res = null;
		    if(name.indexOf("RegQuery") != -1) {
		    	DataThing parameterThing = (DataThing)arg0.get("Ivorn or Registry Keywords");
			    String paramValue = ((String)parameterThing.getDataObject()).trim();
			    logger.warn("paramValue = " + paramValue);
		    	res = getResources(paramValue,name);
		    	ArrayList ivornList = new ArrayList();
		    	for(int i = 0;i < res.length;i++) {
		    		ivornList.add(res[i].getId().toString());
		    	}
		    	outputMap.put("Ivorns",DataThingFactory.bake(ivornList));
		    	return outputMap;
		    }else {
		    	DataThing parameterThingList = (DataThing)arg0.get("Ivorns");
		    	//String whatisit = (String)parameterThingList.getDataObject();
		    	//logger.warn("string from what = " + whatisit);
			    List paramValueList = (List)parameterThingList.getDataObject();
			    logger.warn("paramValueList size = " + paramValueList.size());		    	
		    	res = getResources(paramValueList,name);
		    }
		    
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
		    if(arg0.containsKey("RA"))
		    	ra = Double.parseDouble(((String)((DataThing)arg0.get("RA")).getDataObject()).trim());
		    if(arg0.containsKey("DEC"))
		    	dec = Double.parseDouble(((String)((DataThing)arg0.get("DEC")).getDataObject()).trim());
		    if(arg0.containsKey("SIZE"))
		    	size = Double.parseDouble(((String)((DataThing)arg0.get("SIZE")).getDataObject()).trim());
		    if(arg0.containsKey("Start")) {
		    	start.setTime(dateFormat.parse( ((String)((DataThing)arg0.get("Start")).getDataObject()).trim()   ));
		    }
		    if(arg0.containsKey("End")) {
		    	end.setTime(dateFormat.parse( ((String)((DataThing)arg0.get("End")).getDataObject()).trim()));
		    }
		    
    		String saveURLS = ((String)((DataThing)arg0.get("Only URLS Needed")).getDataObject()).trim();
    		logger.warn("ok process things ra = " + ra + " dec = " + dec + " size = " + size + " start = " + start.toString() + " end = " + end.toString());
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
			    			try {
					    		result =  processSiap(siap,res[j], ra, dec, size/*,outputLoc*/);
					    		logger.warn("ok got a result getId = " + res[j].getId() + " and string result = " + result);
					    		resultMap.put("SiapResult_" + res[j].getId().toString().replaceAll("[^\\w*]","_"), result);
			    			}catch(TransformerException te) {
			    				errorList.add(res[j].getId().toString() + " - " + te.getMessage());
			    		    }catch(java.net.MalformedURLException me) {
			    				errorList.add(res[j].getId().toString() + " - " + me.getMessage());
			    		    }catch(IOException ioe) {
			    				errorList.add(res[j].getId().toString() + " - " + ioe.getMessage());			    		    	
			    		    }
			    		}
			    	}else if(name.equals("CONE")) {
			    		if(cone == null) {
			    			cone = (Cone)acr.getService(Cone.class);
			    		}
			    		if(saveURLS != null && saveURLS.equals("true") ) {
			    			result = getConeURL(cone,res[j], ra, dec, size).toString();
			    		}else {
			    			try {
			    				result = processCone(cone,res[j], ra, dec, size/*,outputLoc*/);
			    				resultMap.put("ConeResult_" + res[j].getId().toString().replaceAll("[^\\w*]","_"), result);
			    			}catch(TransformerException te) {
			    				errorList.add(res[j].getId().toString() + " - " + te.getMessage());
			    		    }catch(java.net.MalformedURLException me) {
			    				errorList.add(res[j].getId().toString() + " - " + me.getMessage());
			    		    }catch(IOException ioe) {
			    				errorList.add(res[j].getId().toString() + " - " + ioe.getMessage());			    		    	
			    		    }
			    		}
			    		
			    	}else if(name.equals("SSAP")) {
			    		if(ssap == null) {
			    			ssap = (Ssap)acr.getService(Ssap.class);
			    		}
			    		if(saveURLS != null && saveURLS.equals("true") ) {
			    			result = getSSAPURL(ssap,res[j], ra, dec, size).toString();
			    		}else {		
			    			try {
					    		result = processSSAP(ssap,res[j], ra, dec, size/*,outputLoc*/);
					    		resultMap.put("SsapResult_" + res[j].getId().toString().replaceAll("[^\\w*]","_"), result);
			    			}catch(TransformerException te) {
			    				errorList.add(res[j].getId().toString() + " - " + te.getMessage());
			    		    }catch(java.net.MalformedURLException me) {
			    				errorList.add(res[j].getId().toString() + " - " + me.getMessage());
			    		    }catch(IOException ioe) {
			    				errorList.add(res[j].getId().toString() + " - " + ioe.getMessage());			    		    	
			    		    }					    		
			    		}
			    	}else if(name.equals("STAP")) {
			    		if(ssap == null) {
			    			stap = (Stap)acr.getService(Stap.class);
			    		}
			    		if(saveURLS != null && saveURLS.equals("true") ) {
			    			result = getStapURL(stap,res[j], start, end).toString();
			    		}else {	
			    			try {
			    				result = processStap(stap,res[j], start, end/*,outputLoc*/);
			    				resultMap.put("StapResult_" + res[j].getId().toString().replaceAll("[^\\w*]","_"), result);
			    			}catch(TransformerException te) {
			    				errorList.add(res[j].getId().toString() + " - " + te.getMessage());
			    		    }catch(java.net.MalformedURLException me) {
			    				errorList.add(res[j].getId().toString() + " - " + me.getMessage());
			    		    }catch(IOException ioe) {
			    				errorList.add(res[j].getId().toString() + " - " + ioe.getMessage());			    		    	
			    		    }			    				
			    		}
			    	}//else			    	
		    	}//if
		    }//for
		    logger.warn("finised AR task placing things in output map.  the result map size = " + resultMap.size());
		    //outputMap.put("result",DataThingFactory.bake(resultMap));
		    logger.warn("lets make the Ivorns and URLs list the size = " + urlMap.size());
		    outputMap.put("Ivorns",DataThingFactory.bake(new ArrayList(urlMap.keySet())));
		    outputMap.put("URLs",DataThingFactory.bake(new ArrayList(urlMap.values())));
		    outputMap.put("ErrorList", DataThingFactory.bake(errorList));
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
