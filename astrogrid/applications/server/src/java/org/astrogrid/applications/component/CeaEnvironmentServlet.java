package org.astrogrid.applications.component;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.common.j2ee.environment.EnvEntry;
import org.astrogrid.common.j2ee.environment.Environment;
import org.astrogrid.common.j2ee.environment.EnvironmentServlet;
import org.astrogrid.config.SimpleConfig;

/**
 * A servlet to manage environment entries in JNDI and web.xml.
 *
 * This is a sub-class of the generic AstroGrid EnvironmentServlet.
 * The change from the parent class is to initialize one specific
 * env-entry when the servlet starts: cea.base.dir which
 * locates the CEC's writeable directory-tree and hence the
 * configuration files. 
 *
 * If the env-entry is set, then this servlet does not try
 * to change it. However, if no value for the env-entry is
 * found in JNDI, then this servlet sets a value based on
 * its knowledge of the environment.
 *
 * If the temporary-files directory (specified by the Java
 * system-property java.io.tmpdir) is, say, /tmp, and if the
 * web-application context is, say, CL-CEC-1, then cea.base.dir
 * gets set to /tmp/CL-CEC-1.
 *
 * @author Guy Rixon
 */
public class CeaEnvironmentServlet extends EnvironmentServlet {
  
  static Log log = LogFactory.getLog(CeaEnvironmentServlet.class);
  
  /**
   * Constructs a new CeaEnvironmentServlet.
   */
  public CeaEnvironmentServlet() {
    super();
  }
  
  /**
   * Initializes the servlet.
   * This adds actions to the initialization in the super-class.
   */
  public void init() throws ServletException {
    super.init();
    
    log.debug("EnvironmentServlet is initialized.");
    log.debug("Now initializing the CeaEnvironmentServlet...");
    
    // Get the environment object from the J2EE context; the
    // parent class put it there during its init() method.
    ServletContext context = this.getServletContext();
    log.debug("Getting the environment bean from the context...");
    Environment environment = (Environment)context.getAttribute("environment");
    log.debug("Got the environment bean.");
    
    // Find the env-entry of interest.
    log.debug("Getting the env-entries...");
    EnvEntry[] entries = environment.getEnvEntry();
    log.debug("Got " + entries.length + " EnvEntry beans.");
    for (int i = 0; i < entries.length; i++) {
      if ("cea.base.dir".equals(entries[i].getName())) {
        log.debug("Initializing cea.base.dir...");
        
        // If no JNDI value, construct one. and add it as the
        // default value. This doesn't alter anything in JNDI
        // but does mean that a configuration constructed from the
        // environment will get the default value.
        if (entries[i].getOperationalValue() == null) {
          File baseDir = new File(System.getProperty("java.io.tmpdir"),
                                  environment.getContextPath());
          String path = baseDir.getAbsolutePath();
          log.debug("cea.base.dir will be set to " + path);
          
          // Add the new value to the environment as a replacement value.
          // This doesn't change JNDI and isn't directly available
          // to the consumers of the configuration (but see below). 
          // However, it does mean that the value shows up in the 
          // configuration editor. 
          entries[i].setReplacementValue(path);
          
        }
      }
    }
    log.info("CeaEnvironmentServlet is initialized.");
  }
}
