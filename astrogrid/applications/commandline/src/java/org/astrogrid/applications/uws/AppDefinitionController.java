/*
 * $Id: AppDefinitionController.java,v 1.3 2011/09/02 21:55:53 pah Exp $
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
import java.io.StringWriter;
import java.util.Set;

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
import org.astrogrid.applications.description.ApplicationDefinition;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.CompositeApplicationDescriptionLibrary;
import org.astrogrid.applications.description.DynamicApplicationDescriptionLibrary;
import org.astrogrid.applications.description.StandardApplicationDescriptionFactory;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        ApplicationDescriptionLibrary complib = manager.getApplicationDescriptionLibrary();
        if(complib instanceof CompositeApplicationDescriptionLibrary){
            Set<ApplicationDescriptionLibrary> libs = ((CompositeApplicationDescriptionLibrary)complib).getLibs();
            for (ApplicationDescriptionLibrary applicationDescriptionLibrary : libs) {
                if(applicationDescriptionLibrary instanceof DynamicApplicationDescriptionLibrary){
                    this.dynLib = (DynamicApplicationDescriptionLibrary) applicationDescriptionLibrary;
                    disabled = false;
                }
            }
        }
        if(disabled) {
            logger.info("dynamic library not found - applications cannot be dynamically defined");
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
        ApplicationDefinition desc = dynLib.getDescription(dynLib.getApplicationNames()[0]);
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
 * Revision 1.3  2011/09/02 21:55:53  pah
 * result of merging the 2931 branch
 *
 * Revision 1.2.2.3  2011/09/02 19:42:55  pah
 * change setup of dynamic description library
 *
 * Revision 1.2.2.2  2009/11/26 10:21:47  pah
 * RESOLVED - bug 2974: create application page does not save
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2974
 *
 * Revision 1.2.2.1  2009/07/15 10:16:25  pah
 * redesign of parameterAdapters
 *
 * Revision 1.2  2009/02/26 12:22:54  pah
 * separate more out into cea-common for both client and server
 *
 * Revision 1.1  2008/10/09 11:40:10  pah
 * NEW - bug 2847: simple xform application definition
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2847
 *
 */
