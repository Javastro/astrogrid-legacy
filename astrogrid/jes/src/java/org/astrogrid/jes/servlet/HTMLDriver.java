/*$Id: HTMLDriver.java,v 1.3 2004/04/08 14:43:26 nw Exp $
 * Created on 05-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.servlet;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobController;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/** Simple servlet that allows JES to be driven from a HTML interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Apr-2004
 *
 */
public class HTMLDriver extends HttpServlet {
    public final static String INSPECT = "inspect";
    public final static String SUBMIT = "submit";
    public final static String DELETE = "delete";
    public final static String ABORT = "abort";
    public final static String LIST = "list";
    public final static String WORKFLOW = "workflow";
    
    public final static String ACTION = "action";
    public final static String URN = "urn";
    
    public final static String USERNAME = "username";
    public final static String COMMUNITY = "community";
    /** Construct a new HTMLDriver
     * 
     */
    public HTMLDriver() {
        super();
    }
    
    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(ACTION);
        try {
        if (INSPECT.equalsIgnoreCase(action)) {
            doInspect(req,resp);
        } else if (DELETE.equalsIgnoreCase(action)) {
            deleteJob(req,resp);
        } else if (ABORT.equalsIgnoreCase(action)){
            abortJob(req,resp);
        } else {
            doList(req,resp);
        }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    /**
     * @param req
     * @param resp
     */
    private void abortJob(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        JobController man = JesDelegateFactory.createJobController(computeEndpoint(req).toString());
        JobURN urn = new JobURN();
        urn.setContent(req.getParameter(URN).trim());
        PrintWriter out = resp.getWriter();
          out.println("<html><body>");
          out.println("<h1>Aborting Job " + urn.getContent() + "</h1>");
          man.cancelJob(urn);
          out.println("<h1>Done</h1>");
          out.println("</body></table>");  
    }

    /**
     * @param req
     * @param resp
     */
    private void deleteJob(HttpServletRequest req, HttpServletResponse resp) throws Exception  {
        JobController man = JesDelegateFactory.createJobController(computeEndpoint(req).toString());
        JobURN urn = new JobURN();
        urn.setContent(req.getParameter(URN).trim());
        PrintWriter out = resp.getWriter();
          out.println("<html><body>");
          out.println("<h1>Deleting Job" + urn.getContent() + "</h1>");
          man.deleteJob(urn);
          out.println("<h1>Done</h1>");
          out.println("</body></table>");  
    }

    /**
     * @param req
     * @param resp
     */
    private void doList(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        JobController man = JesDelegateFactory.createJobController(computeEndpoint(req).toString());
        Account acc = new Account();
        acc.setCommunity(req.getParameter(COMMUNITY));
        acc.setName(req.getParameter(USERNAME));
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Jobs for " + acc.getName() + "@" + acc.getCommunity());
        JobSummary[] summary = man.readJobList(acc);
        out.println("<table>");        
        for (int i = 0; i < summary.length; i++) {           
            out.println("<tr>");
            out.println("<td>" + summary[i].getName() + "</td>");
            String urn = summary[i].getJobURN().getContent();
            out.println("<td><a href='html-driver?action=" + INSPECT + "&urn=" + URLEncoder.encode(urn) + "'>"
                + urn + "</a></td>");
            out.println("<td><a href='html-driver?action=" + DELETE + "&urn=" + URLEncoder.encode(urn)+ "'>delete</a></td>");
            out.println("<td><a href='html-driver?action=" + ABORT + "&urn=" + URLEncoder.encode(urn) + "'>abort</a></td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body></html>");
    }

    private URL computeEndpoint(HttpServletRequest req) throws MalformedURLException {
        
        URL endpoint = new URL("http", req.getServerName(),
          req.getServerPort(), req.getContextPath() +  "/services/JobControllerService"); 
        return endpoint;
    }
    /**
     * @param req
     * @param resp
     */
    private void doInspect(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //ComponentManager man = ComponentManagerFactory.getInstance();

        JobController man = JesDelegateFactory.createJobController(computeEndpoint(req).toString());        
        JobURN urn = new JobURN();
        urn.setContent(req.getParameter(URN).trim());
        Workflow wf = man.readJob(urn);
        TransformerFactory fac = TransformerFactory.newInstance();
        Transformer trans = fac.newTransformer(
            new StreamSource(this.getClass().getResourceAsStream("workflow.xsl"))
            );
        
        StringWriter sw = new StringWriter();
        wf.marshal(sw);
        sw.close();
        trans.setOutputProperty(OutputKeys.METHOD,"html");
        trans.transform(
            new StreamSource(new StringReader(sw.toString()))
            , new StreamResult(resp.getOutputStream())
            );
    }
    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
        //ComponentManager man = ComponentManagerFactory.getInstance();

        JobController man = JesDelegateFactory.createJobController(computeEndpoint(req).toString());
        Workflow wf = Workflow.unmarshalWorkflow(new StringReader(req.getParameter(WORKFLOW)));

        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Submitting Job</h1>");

        JobURN urn = man.submitWorkflow(wf);
        out.println("<h1>Done: <a href='html-driver?action=" + INSPECT + "&urn=" + URLEncoder.encode(urn.getContent())+ "'>" + urn.getContent() + "</a></h1>");
        out.println("</body></table>");                
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }



}


/* 
$Log: HTMLDriver.java,v $
Revision 1.3  2004/04/08 14:43:26  nw
added delete and abort job functionality

Revision 1.2  2004/04/07 23:06:05  nw
got html-front-end working

Revision 1.1  2004/04/06 08:29:21  nw
start of a html interface
 
*/