package org.astrogrid.common.j2ee;

import java.io.InputStream;
import java.io.StringWriter;
import javax.servlet.ServletContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * A Java bean, for use in JSPs, that tabulates the env-entry
 * elements in the deployment descriptor of a web-application.
 * The bean parses web.xml in its parent web-application and
 * generates an HTML table showing the names, types and current
 * values of the environment entries. The values are marked up
 * as input elements. Hence, the table may be included in an
 * HTML form to make an editor for the values.
 *
 * To use this class, instantiate it as a bean from a JSP
 * using the jsp:useBean tag. Then set the context property to
 * the value of the JSP's "application" variable: this passes
 * the servlet context of the JSP into the bean. Finally, read
 * the table property to extract the HTML. If you use the
 * jsp:getProperty tag to do this then the table will be added
 * in-line to the HTML output of the JSP. If this method fails to
 * derive the table then it returns an HTML paragraph containing
 * an error report; it never throws exceptions.
 *
 * Constructing the bean and setting the context property do little
 * processing. The main work of transforming the XML to HTML is done
 * when the table property is read.
 *
 * XSLT is used to transform the XML into HTML. The transformation
 * script is read from an external file. @TODO: fix this?
 *
 * This class will only work inside a servlet container; it uses
 * classes from javax.servlet to access web.xml.
 *
 * @deprecated Use the classes in
 * {@link org.astrogrid.common.j2ee.environment} instead of this bean.
 *
 * @author Guy Rixon
 */
public class EnvEntriesTabulator {

  /**
   * Constructs an EnvEntriesTabulator.
   */
  public EnvEntriesTabulator() {}

  /**
   * The servlet context to be used for finding web.xml.
   */
  private ServletContext context;

  /**
   * Sets the context property.
   *
   * @param context The servlet context of the calling servlet or JSP.
   */
  public void setContext(ServletContext context) {
    this.context = context;
  }

  /**
   * Gets the HTML table derived from the deployment descriptor.
   * This accessor actually computes the table when it is asked for.
   *
   * @return The HTML table, or an error report if the table can't be derived.
   */
  public String getTable() {

    // This can't work without a servlet context.
    if (this.context == null) {
      return "<p>Error: the context property is not set " +
          "in the tabulator bean.</p>";
    }

    // Swallow all errors. Do not throw exceptions.
    try {

      // Get the current web.xml as a source transformable by XSLT.
      InputStream  webXmlStream =
          this.context.getResourceAsStream("/WEB-INF/web.xml");
      StreamSource webXmlSource = new StreamSource(webXmlStream);

      // Build a transformer that can produce a web form from web.xml.
      InputStream  xsltStream =
        this.context.getResourceAsStream("/webDotXmlToForm.xsl");
        StreamSource xsltSource = new StreamSource(xsltStream);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltSource);

      // Make the output stream for this page a possible target
      // for the transformation.
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);

      // Run the transformer on web.xml.
      transformer.transform(webXmlSource, result);

      // Return the result of the transformation.
      return writer.getBuffer().toString();

    }
    catch (Exception e) {
      return "<p>Error: failed to extract the table: " + e + "</p>";
    }
  }

}