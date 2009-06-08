/*
 * $Id: DocServlet.java,v 1.1 2009/06/08 11:48:19 pah Exp $
 * 
 * Created on 5 Jun 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.docs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Servlet to reuse the maven xdocs by dynamically transforming them.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 5 Jun 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class DocServlet extends HttpServlet {

    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(DocServlet.class);
    
    private ServletContext context;

    private Transformer xformer;

    private File xdocRoot;
    
 
    @Override
    public void init(ServletConfig config) throws ServletException {
       super.init(config);
       context = config.getServletContext();
       
       try {
        String root = context.getRealPath("/doc/xdoc/");
        xdocRoot = new File(root);
        logger.info("initializing DocServlet docroot ="+xdocRoot);
        InputStream xslt = context.getResourceAsStream("/doc/xdocTohtml.xsl");
        assert xslt != null : "could not find the xdocTohtml.xsl";
           TransformerFactory factory = TransformerFactory.newInstance();
           Templates template = factory.newTemplates(new StreamSource(xslt));
           xformer = template.newTransformer();
    } catch (TransformerConfigurationException e) {
        logger.fatal("problem setting up document transformer", e);
    } catch (TransformerFactoryConfigurationError e) {
        logger.fatal("problem setting up document transformer", e);
    }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
      String uri = req.getRequestURI();
      int is;
      if ((is = uri.indexOf("/doc/")) != 0) {
          
          String filename = uri.substring(is+5, uri.length());
          int i;
          if(( i =filename.indexOf('.')) != 0)
          {
              filename = filename.substring(0, i) + ".xml";
          }
          else
          {
              filename = filename+ "xml";
          }
          File infile = new File(xdocRoot, filename);
          logger.debug("docfile="+filename);
          Source source = new StreamSource(new FileInputStream(infile));
          StringWriter output = new StringWriter();
          Result result = new StreamResult(output);
          try {
            xformer.transform(source, result);
            req.setAttribute("body", output.toString());
            RequestDispatcher dispatcher = req.getRequestDispatcher("/admin/docdisplay.jsp");
            if (dispatcher == null) {
              resp.sendError(HttpServletResponse.SC_NOT_FOUND, "/admin/docdisplay.jsp.jsp");
            }
            else {
              dispatcher.forward(req, resp);
            }
           
        } catch (TransformerException e) {
            throw new ServletException("problem with xslt transformation of documentation", e);
        }

        
    }
      else {
          logger.fatal("DocServlet not properly set up in web.xml");
      }
       
    }

}


/*
 * $Log: DocServlet.java,v $
 * Revision 1.1  2009/06/08 11:48:19  pah
 * NEW - bug 2854: improve documentation to make clear which links only work in the live app
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2854
 * add a servlet to automatically do doc transformations
 *
 */
