package org.astrogrid.portal.utils.acting;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.astrogrid.common.creator.CreatorException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public interface ActionUtils {
  /**
   * Get a request parameter given a parameter passed into the action via the sitemap.
   * 
   * @param sitemapParam sitemap given parameter whose value is the request parameter name
   * @param defaultValue default value of request parameter name
   * @param params sitemap parameters
   * @param request request context object
   * 
   * @return value of request parameter
   */
  public String getRequestParameter(String sitemapParam, String defaultValue, Parameters params, Request request);

  /**
   * @see ActionUtils#getRequestParameter(String, String, Parameters, Request)
   */
  public String getRequestParameter(String sitemapParam, Parameters params, Request request);
  
  /**
   * Get a session attribute given a attribute passed into the action via the sitemap.
   * 
   * @param sitemapParam sitemap given attribute whose value is the session attribute name
   * @param defaultValue default value of session attribute name
   * @param params sitemap parameters
   * @param session session context object
   * 
   * @return value of session attribute
   */
  public String getSessionAttribute(String sitemapParam, String defaultValue, Parameters params, Session session);

  /**
   * @see ActionUtils#getSessionAttribute(String, String, Parameters, Session)
   */
  public String getSessionAttribute(String sitemapParam, Parameters params, Session session);
  
  public String getAnyParameter(String sitemapParam, Parameters params, Request request, Session session);
  public String getAnyParameter(String sitemapParam, String defaultValue, Parameters params, Request request, Session session);
  /**  
   * @see ActionUtils#getNewObject(String, String, Parameters, Object[])
   */
  public Object getNewObject(String sitemapParam, Parameters params, Request request, List args) throws CreatorException;
  /**
   * Get a new instance of a class name given in the sitemap parameters.
   * 
   * @see org.astrogrid.common.creator.Creator
   * 
   * @param sitemapParam sitemap parameter containing the class name
   * @param defaultClassName default class name to use
   * @param params sitemap parameters
   * @param request request context object
   * @param args constructor arguments
   * 
   * @return new instance
   */
  public Object getNewObject(String sitemapParam, String defaultClassName, Parameters params, Request request, List args) throws CreatorException;
  public Element getDomElement(String adql) throws ParserConfigurationException, SAXException, IOException;
  public ValidationHandler validate(String xml);
  public boolean shouldValidate(String xml);
  public boolean shouldBeNSAware(String xml);
}