/*
 * $Id: MetaDataController.java,v 1.3 2009/02/26 12:22:54 pah Exp $
 * 
 * Created on 9 May 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.uws;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.ApplicationDefinition;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.execution.ListOfParameterValues;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

/**
 * Controller for returning various metadata.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 May 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
@Controller
@RequestMapping("/reg/**")
public class MetaDataController extends AbstractMetadataController {
    private static final Log logger = LogFactory.getLog(MetaDataController.class);

    @Autowired
    public MetaDataController(CEAComponents manager){
	  super(manager);

    }

    @RequestMapping("")
    public ModelAndView listResources(HttpServletRequest request, HttpServletResponse response) throws ApplicationDescriptionNotFoundException
    {
	
	ModelAndView mav = new ModelAndView("resourceList");
	createAppsModel(mav);
	return mav ;
    }

    @RequestMapping("all")
    public void allResources(HttpServletRequest request, HttpServletResponse response) throws IOException, MetadataException, TransformerException
    {
	Document document = manager.getMetadataService().returnRegistryEntry();
	sendXML(response, document); 
    }

    @RequestMapping("app")
    public ModelAndView listApps(HttpServletRequest request, HttpServletResponse response) throws ApplicationDescriptionNotFoundException
    {
	ModelAndView mav = new ModelAndView("applist");
	createAppsModel(mav);
	return mav;
    }

    @RequestMapping("app/*/res")
    public void appResource(HttpServletRequest request, HttpServletResponse response) throws IOException, TransformerException
    {
	sendAppXML(request, response, identityTransformer);
	
	
    }

    @RequestMapping("server")
    public void serverResource(HttpServletRequest request, HttpServletResponse response) throws MetadataException, IOException, TransformerException
    {
	Document dom = manager.getMetadataService().getServerDescription();
	sendXML(response, dom);
	
	
    }
    
    @RequestMapping("app/*/reschiba")
    public void appResourceMassaged(HttpServletRequest request, HttpServletResponse response) throws IOException, TransformerException
    {
        TransformerFactory factory = TransformerFactory.newInstance();
        
        // Use the factory to create a template containing the xsl file
        Templates template = factory.newTemplates(new StreamSource(
            this.getClass().getResourceAsStream("chiba-massage.xsl")));

        // Use the template to create a transformer
        Transformer xformer = template.newTransformer();
        sendAppXML(request, response, xformer);
        
        	
    }
    
    
    
    @RequestMapping("app/*/tool")
    public void createTool(HttpServletRequest request, HttpServletResponse response) throws InterfaceDescriptionNotFoundException, ParameterDescriptionNotFoundException, MetadataException, IOException, TransformerException
    {
	String name = decodeAppId(request);
	if(name != null)
	{
	    String intf = request.getParameter("intf");
	    if(intf == null) // if not specified just use the first
	    {
		ApplicationDefinition desc = manager.getApplicationDescriptionLibrary().getDescriptionByShortName(name);
		intf = desc.getInterfaces()[0].getId();
	    }
	    Tool tool = makeTool(name, intf);
	    Document document = CEAJAXBUtils.marshall(tool);
	    sendXML(response, document);
	}
	else
	{
	    response.sendError(HttpServletResponse.SC_NOT_FOUND, "could not decode application name in url");
	}
    }

    
   private Tool makeTool(String name, String intf) throws ApplicationDescriptionNotFoundException, InterfaceDescriptionNotFoundException, ParameterDescriptionNotFoundException
    {
	ApplicationDefinition descr = manager.getApplicationDescriptionLibrary().getDescriptionByShortName(name);
	ApplicationInterface iface = descr.getInterface(intf);
	Tool t = new Tool();
	t.setId(descr.getId());
	t.setInterface(iface.getId());
	ListOfParameterValues input = t.getInput();
	ListOfParameterValues output = t.getOutput();
	String[] irefs = iface.getArrayofInputs();
	for (int i = 0; i < irefs.length; i++) {
	    ParameterDescription pdescr = descr.getParameterDescription(irefs[i]);
	    ParameterValue pv = createParameterValue(pdescr);
	    input.addParameter(pv);
	    
	}
	String[] orefs = iface.getArrayofOutputs();
	for (int i = 0; i < orefs.length; i++) {
	    ParameterDescription pdescr = descr.getParameterDescription(orefs[i]);
	    ParameterValue pv = new ParameterValue(orefs[i],"");
	    output.addParameter(pv);
	}
	
	return t;
    }
    
    private ParameterValue createParameterValue(ParameterDescription pb) {
	      ParameterValue pv = new ParameterValue();
	      pv.setId(pb.getId());
	      if (pb.getDefaultValue() != null && pb.getDefaultValue().size() > 0) {
	          pv.setValue(pb.getDefaultValue().get(0));
	      } else {
	          pv.setValue("");
	      }
	      return pv;
	      
	  }

}


/*
 * $Log: MetaDataController.java,v $
 * Revision 1.3  2009/02/26 12:22:54  pah
 * separate more out into cea-common for both client and server
 *
 * Revision 1.2  2008/09/03 14:18:34  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/06/10 20:10:49  pah
 * moved ParameterValue and friends to CEATypes.xsd
 *
 * Revision 1.1.2.1  2008/05/13 16:02:47  pah
 * uws with full app running UI is working
 *
 */
