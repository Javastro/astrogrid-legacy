/*
 * $Id: AppDefinitionController.java,v 1.1 2008/10/09 11:40:10 pah Exp $
 * 
 * Created on 8 Oct 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.uws;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.CompositeApplicationDescriptionLibrary;
import org.astrogrid.applications.description.DynamicApplicationDescriptionLibrary;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;

/**
 * Controller for Dynamic application definition.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 8 Oct 2008
 * @version $Name:  $
 * @since VOTech Stage 8
 */
@Controller
@RequestMapping("/defineApp/**")
public class AppDefinitionController {
    
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(AppDefinitionController.class);
    private CEAComponents manager;
    private DynamicApplicationDescriptionLibrary dynLib;
    private boolean disabled = true;
    
    @Autowired
    public AppDefinitionController(CEAComponents manager, Configuration conf) {
        this.manager = manager;
        this.dynLib = new DynamicApplicationDescriptionLibrary(conf);
        //IMPL perhaps this is a slightly screwy way to add the dynamic app desc library - should be more explicit in the spring config - but then difficult to get the dynlib 
        ApplicationDescriptionLibrary complib = manager.getApplicationDescriptionLibrary();
        if(complib instanceof CompositeApplicationDescriptionLibrary){
            ((CompositeApplicationDescriptionLibrary)complib).addLibrary(dynLib);
            disabled = false;
        }
        else {
            logger.error("CompositeApplicationDescriptionLibrary not found - dynamic library not added to it");
        }
    }

    
    @RequestMapping("new")
    public void defineApp(HttpServletRequest request, HttpServletResponse response) throws IOException, TransformerException{

        TransformerFactory factory = TransformerFactory.newInstance();
        
        // Use the factory to create a template containing the xsl file
        Templates template = factory.newTemplates(new StreamSource(
            this.getClass().getResourceAsStream("chiba-unmassage.xsl")));

        // Use the template to create a transformer
        Transformer xformer = template.newTransformer();
        StreamSource source = new StreamSource(request.getInputStream());
        StringWriter sw = new StringWriter();

        Result result = new StreamResult(sw);
        xformer.transform(source, result);
        logger.debug(sw.toString());
        
        dynLib.addApplicationDescription(sw.toString());
        UWSUtils.redirectTo(request, response, "/defineApp/show");

        
    }
    
    @RequestMapping("show")
    public ModelAndView showApp(HttpServletRequest request, HttpServletResponse response) throws ApplicationDescriptionNotFoundException
    {
        ModelAndView retval = new ModelAndView("dynapp");
        ApplicationDescription desc = dynLib.getDescription(dynLib.getApplicationNames()[0]);
        retval.addObject("app", desc);
        return retval;
        
    }
    
    @RequestMapping("save")
    public void saveApp(HttpServletRequest request, HttpServletResponse response) throws IOException  {
        
        
        dynLib.saveDescription(request.getParameter("location"));
        UWSUtils.redirectTo(request, response, "/defineApp/show");
    }
    
}


/*
 * $Log: AppDefinitionController.java,v $
 * Revision 1.1  2008/10/09 11:40:10  pah
 * NEW - bug 2847: simple xform application definition
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2847
 *
 */
