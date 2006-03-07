package org.astrogrid.applications.component;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
    
    // Get the environment object from the J2EE context; the
    // parent class put it there during its init() method.
    ServletContext context = this.getServletContext();
    Environment environment = (Environment)context.getAttribute("environment");
    
    // Find the env-entry of interest.
    EnvEntry[] entries = environment.getEnvEntry();
    for (int i = 0; i < entries.length; i++) {
      if ("cea.base.dir".equals(entries[i].getName())) {
        
        // If no JNDI value, construct one. and add it as the
        // default value. This doesn't alter anything in JNDI
        // but does mean that a configuration constructed from the
        // environment will get the default value.
        if (entries[i].getOperationalValue() == null) {
          File baseDir = new File(System.getProperty("java.io.tmpdir"),
                                  environment.getContextPath());
          String path = baseDir.getAbsolutePath();
          
          // Add the new value to the environment as a replacement value.
          // This doesn't change JNDI and isn't directly available
          // to the consumers of the configuration (but see below). 
          // However, it does mean that the value shows up in the 
          // configuration editor. 
          entries[i].setReplacementValue(path);
          
          // Add the value to the AstroGrid generic configuration,
          // thus overriding the null value obtained from JNDI
          // (the AG configuration is read from JNDI). This makes the
          // value operational in the rest of the web-application.
          SimpleConfig.getSingleton().setProperty("cea.base.dir", path);
        }
      }
    }
  }
}
