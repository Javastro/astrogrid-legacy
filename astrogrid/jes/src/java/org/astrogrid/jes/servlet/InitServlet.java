/*$Id: InitServlet.java,v 1.2 2004/07/09 09:30:28 nw Exp $
 * Created on 14-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.servlet;

import org.astrogrid.jes.jobscheduler.impl.scripting.JarPathsFromConfig;
import org.astrogrid.jes.jobscheduler.impl.scripting.WorkflowInterpreterFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/** servlet that passes in configuration information from the servlet context.
 * @author Noel Winstanley nw@jb.man.ac.uk 14-May-2004
 *
 */
public class InitServlet extends GenericServlet {
    private static final Log log = LogFactory.getLog(InitServlet.class);
    /** Construct a new InitServlet
     * 
     */
    public InitServlet() {
        super();
    }

    /**
     * @see javax.servlet.GenericServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
        // we do nothing.
    }
    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig conf) throws ServletException {
        // find base dir...
        log.info("initializing properties required by jes jes");
        ServletContext cxt = conf.getServletContext();
        File root = new File(cxt.getRealPath("/"));
          File lib = new File(new File(root,"WEB-INF"),"lib");
          File[] jesJarArr = lib.listFiles(new FileFilter(){
              public boolean accept(File pathname) {
                  return pathname.getName().indexOf(WorkflowInterpreterFactory.JarPaths.JES_JAR) != -1;
              }
          });
          File[] libJarArr = lib.listFiles(new FileFilter(){
              public boolean accept(File pathname) {
                  return pathname.getName().indexOf(WorkflowInterpreterFactory.JarPaths.LIBRARY_JAR) != -1;
              }
          });                          
          if (jesJarArr.length > 0) {
              System.setProperty(JarPathsFromConfig.JES_JAR_KEY, jesJarArr[0].getAbsolutePath());
          } else {
              System.setProperty(JarPathsFromConfig.JES_JAR_KEY,(new File(new File(root,"WEB-INF"),"classes")).getAbsolutePath());
          }
          if (libJarArr.length > 0) {
              System.setProperty(JarPathsFromConfig.LIB_JAR_KEY, libJarArr[0].getAbsolutePath());
          }
          log.info("Jes Jar set to " + System.getProperty(JarPathsFromConfig.JES_JAR_KEY));
          log.info("Library jar set to " + System.getProperty(JarPathsFromConfig.LIB_JAR_KEY));   

    }

}


/* 
$Log: InitServlet.java,v $
Revision 1.2  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/