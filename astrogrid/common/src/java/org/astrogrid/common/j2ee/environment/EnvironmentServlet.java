package org.astrogrid.common.j2ee.environment;

import java.net.URL;
import java.util.Enumeration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet to support configuration of a web application's environment.
 * The servlet constructs and maintains a Java bean of type
 * {@link org.astrogrid.common.j2ee.environment.Environment} which is
 * exposed to other web components via the servlet context.
 * <p>
 * On initialization, the servlet loads web.xml, the web
 * application's deployment-descriptor and creates from it the
 * Environment bean. The bean is set as an attribute in the
 * servlet context, which makes it available to JSPs via this
 * construct:
 * <pre>
 *   <jsp:useBean class="org.astrogrid.common.j2ee.environment"
 *       id="environment" scope="application"/>
 * </pre>
 * <p>
 * An HTTP request to this servlet is treated as an instruction to update
 * the values of environment entries in the Environment bean. Where the name
 * of a parameter matches the name of an EnvEntry in the Environment, the
 * parameter's value (it is treated as a scalar value) is set as the
 * replacementValue property for the EnvEntry bean.
 *
 * @author Guy Rixon
 */
public class EnvironmentServlet extends HttpServlet {

  /**
   * The bean representing the overall environment.
   * The purpose of the servlet is to maintain this bean.
   */
  private Environment environment;

  /** Creates a new instance of EnvironmentServlet */
  public EnvironmentServlet() {
    this.environment = new Environment();
  }

  public void init() throws ServletException {
    ServletContext context = this.getServletContext();
    try {
      URL webDotXmlUrl = context.getResource("/WEB-INF/web.xml");
      this.environment.setDeploymentDescriptor(webDotXmlUrl.toString());
      context.setAttribute("environment", this.environment);

      String rootUri = context.getResource("/").toString();
      String rootUri2 = rootUri.substring(0, rootUri.length()-1); // trim trailing slash
      int slashIndex = rootUri2.lastIndexOf("/");
      this.environment.setContextPath(rootUri2.substring(slashIndex));
    }
    catch (Exception e) {
      throw new ServletException(e);
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    this.doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    try {
      // Get all the parameters out of the request. Expect all of these to be new values for
      // environment entries. The names of the parameters should match the names of the
      // environment entries.
      Enumeration paramNames = request.getParameterNames();
      while (paramNames.hasMoreElements()) {
        String name = (String)paramNames.nextElement();
        String value = request.getParameter(name);
        this.updateEnvEntry(name, value);
      }

      // Hand off to a JSP to render the next page.
      RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/environment-main.jsp");
      dispatcher.forward(request, response);
    }
    catch (Exception e) {
      throw new ServletException(e);
    }
  }

  private void updateEnvEntry(String name, String value) {
    EnvEntry[] entries = this.environment.getEnvEntry();
    for (int i = 0; i < entries.length; i++) {
      if (name.equals(entries[i].getName())) {
        entries[i].setReplacementValue(value);
      }
    }
  }

}
