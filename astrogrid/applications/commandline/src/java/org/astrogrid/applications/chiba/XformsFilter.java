/*
 * $Id: XformsFilter.java,v 1.1 2008/10/08 19:49:17 pah Exp $
 * 
 * Created on 7 Oct 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.chiba;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.agent.web.WebFactory;
import org.chiba.agent.web.filter.BufferedHttpServletResponseWrapper;
import org.chiba.agent.web.filter.XFormsFilter;
import org.chiba.agent.web.servlet.WebUtil;
import org.chiba.agent.web.session.XFormsSession;
import org.chiba.agent.web.session.XFormsSessionManager;
import org.chiba.agent.web.session.impl.DefaultXFormsSessionManagerImpl;
import org.chiba.xml.xforms.exception.XFormsException;

/**
 * Customized XformsFilter  - re-implements the {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} method with changes as indicated from orginal.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 7 Oct 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class XformsFilter extends XFormsFilter {
    private static final Log LOG = LogFactory.getLog(XFormsFilter.class);
    /**
     * The actual filtering method
     *
     * @see http://java.sun.com/j2ee/sdk_1.3/techdocs/api/javax/servlet/Filter.html#doFilter(javax.servlet.ServletRequest,%20javax.servlet.ServletResponse,%20javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest srvRequest, ServletResponse srvResponse, FilterChain filterChain) throws IOException, ServletException {

        //ensure correct Request encoding
        if (srvRequest.getCharacterEncoding() == null) {
            srvRequest.setCharacterEncoding(defaultRequestEncoding);
        }

        HttpServletRequest request = (HttpServletRequest) srvRequest;

        HttpServletResponse response = (HttpServletResponse) srvResponse;
        HttpSession session = request.getSession(true);

        if ("GET".equalsIgnoreCase(request.getMethod()) && request.getParameter("submissionResponse") != null) {
            doSubmissionResponse(request, response);
        } else {

            /* before servlet request */
            if (isXFormUpdateRequest(request)) {
                LOG.info("Start Update XForm");

                try {
                    XFormsSession xFormsSession = WebUtil.getXFormsSession(request, session);
                    xFormsSession.setRequest(request);
                    xFormsSession.setResponse(response);
                    xFormsSession.handleRequest();
                } catch (XFormsException e) {
                    throw new ServletException(e);
                }
                LOG.info("End Update XForm");
            } else {

                /* do servlet request */
                LOG.info("Passing to Chain");
                BufferedHttpServletResponseWrapper bufResponse = new BufferedHttpServletResponseWrapper((HttpServletResponse) srvResponse);
                filterChain.doFilter(srvRequest, bufResponse);
                LOG.info("Returned from Chain");

                // response is already committed to the client, so nothing is to
                // be done
                if (bufResponse.isCommitted())
                    return;

                //set mode of operation (scripted/nonscripted) by config
                if (this.mode == null)
                    this.mode = "true";

                if (this.mode.equalsIgnoreCase("true")) {
                    request.setAttribute(WebFactory.SCRIPTED, "true");
                } else if (this.mode.equalsIgnoreCase("false")) {
                    request.setAttribute(WebFactory.SCRIPTED, "false");
                }

                /* dealing with response from chain */
                if (handleResponseBody(request, bufResponse)) {
                    byte[] data = prepareData(bufResponse);
                    if (data.length > 0) {
                        request.setAttribute(WebFactory.XFORMS_INPUTSTREAM, new ByteArrayInputStream(data));
                    }
                }

                if (handleRequestAttributes(request)) {
//unsafe close - might have been a writer....                    bufResponse.getOutputStream().close();
                    LOG.info("Start Filter XForm");

                    XFormsSession xFormsSession = null;
                    try {
                        XFormsSessionManager sessionManager = DefaultXFormsSessionManagerImpl.getXFormsSessionManager();
                        xFormsSession = sessionManager.createXFormsSession(request, response, session);
                        xFormsSession.setBaseURI(request.getRequestURL().toString());
                        xFormsSession.setXForms();
                        xFormsSession.init();
                        xFormsSession.handleRequest();
                    }
                    catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                        if (xFormsSession != null) {
                            xFormsSession.close(e);
                        }
                        throw new ServletException(e.getMessage());
                    }

                    LOG.info("End Render XForm");
                } else {
                    srvResponse.getOutputStream().write(bufResponse.getData());
                    srvResponse.getOutputStream().close();
                }
            }
        }
    }
}


/*
 * $Log: XformsFilter.java,v $
 * Revision 1.1  2008/10/08 19:49:17  pah
 * update to chiba 1.5 cibaweb 2.3
 *
 */
