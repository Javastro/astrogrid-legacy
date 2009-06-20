/*
 * $Id: AbstractMetadataController.java,v 1.5 2009/06/20 14:23:24 pah Exp $
 * 
 * Created on 12 May 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.uws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.ApplicationDefinition;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.registry.RegistryQueryLocator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;
import org.w3c.dom.Document;

/**
 * Common functionality of the metadata controller.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class AbstractMetadataController {

    protected final CEAComponents manager;
    protected Transformer identityTransformer;
    protected final RegistryQueryLocator regQuerier;
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
	    .getLog(AbstractMetadataController.class);
    static final String resultRegexp = "/app/+([^/\\?#]+)[/\\?#]?";
    protected static final Pattern resultPattern = Pattern.compile(resultRegexp);
    

    /**
     * Model for resource info in MVC.
     * @author Paul Harrison (paul.harrison@manchester.ac.uk) 17 Sep 2008
     * @version $Name:  $
     * @since VOTech Stage 7
     */
    public static class ResInfo{
        public boolean isRegistered() {
            return registered;
        }
        final private String shortref;
        final private String name;
        final private String id;
        final private String[] ifaces;
        final private boolean registered;
        public ResInfo(String id, String name, String[] ifaces, boolean isregistered) {
            this.id = id;
            this.name = name;
            this.shortref = name.replaceAll("\\s", "");
            this.ifaces = ifaces;
            this.registered = isregistered;
        }
        public String getShortref() {
            return shortref;
        }
        public String getName() {
            return name;
        }
        public String getId() {
            return id;
        }
	public String[] getIfaces() {
	    return ifaces;
	}
        
    }

    protected void createAppsModel(ModelAndView mav)
	    throws ApplicationDescriptionNotFoundException {
	        List<ResInfo> resources = new ArrayList<ResInfo>();
	        String[] apps = manager.getApplicationDescriptionLibrary().getApplicationNames();
	        for (int i = 0; i < apps.length; i++) {
	            ApplicationDefinition appDesc = manager.getApplicationDescriptionLibrary().getDescription(apps[i]);
	            ApplicationInterface[] intfs = appDesc.getInterfaces();
	            String[] ifaces = new String[intfs.length];
	            for (int j = 0; j < ifaces.length; j++) {
			ifaces[j]= intfs[j].getId();
		    }
	            resources.add(new ResInfo(appDesc.getId(),appDesc.getName(), ifaces, regQuerier.isRegistered(appDesc.getId())));
	        }
	        mav.addObject("apps", resources);
	    }

    public AbstractMetadataController(CEAComponents manager, RegistryQueryLocator regQuerier) {
	this.manager = manager;
	this.regQuerier = regQuerier;
	  TransformerFactory tFactory =
	      TransformerFactory.newInstance();
	    try {
		identityTransformer = tFactory.newTransformer();
	    } catch (TransformerConfigurationException e) {
		logger.error("problem setting up for writing metadata", e);
		
	    }

    }

    
    protected void sendXML(HttpServletResponse response, Document document) throws IOException, TransformerException
    {
	sendXML(response, document, identityTransformer);
    }
    protected void sendXML(HttpServletResponse response, Document document, Transformer theTransformer) throws IOException,
	    TransformerException {
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.setContentType("application/xml");
	        DOMSource source = new DOMSource(document);
	        StreamResult result = new StreamResult(response.getOutputStream());
	        theTransformer.transform(source, result);
	    }

    protected void sendAppXML(HttpServletRequest request, HttpServletResponse response, Transformer theTransformer) throws IOException,
	    TransformerException {
	        String name;
	        if((name= decodeAppId(request)) != null)
	        {
	            
	            try {
	        	ApplicationDefinition appd = manager.getApplicationDescriptionLibrary().getDescriptionByShortName(name);
	        	Document doc = manager.getMetadataService().getApplicationDescription(appd.getId());
	        	sendXML(response, doc, theTransformer);
	        	return;
	            } catch (ApplicationDescriptionNotFoundException e) {
	        	response.sendError(HttpServletResponse.SC_NOT_FOUND, "no application name found for name="+ name);
	        	return;
	            }
	        }
	        else
	        {
	            response.sendError(HttpServletResponse.SC_NOT_FOUND, "no application name found for url="+ request.getRequestURI());
	        }
	    }

    protected String decodeAppId(HttpServletRequest request){
        String reqURL = new UrlPathHelper().getPathWithinServletMapping(request);
        Matcher matcher = resultPattern.matcher(reqURL);
        if(matcher.find()){
            return matcher.group(1);
        }
        else
        {
            return null;
        }

    }
}

/*
 * $Log: AbstractMetadataController.java,v $
 * Revision 1.5  2009/06/20 14:23:24  pah
 * improvements in registration if talking to AG registry
 *
 * Revision 1.4  2009/02/26 12:22:54  pah
 * separate more out into cea-common for both client and server
 *
 * Revision 1.3  2008/09/18 08:46:45  pah
 * improved javadoc
 *
 * Revision 1.2  2008/09/03 14:18:34  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/05/13 16:02:47  pah
 * uws with full app running UI is working
 *
 */
