package org.astrogrid.datacenter.cocoon.acting.utils;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
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
  public abstract String getRequestParameter(String sitemapParam, String defaultValue, Parameters params, Request request);
  /**
   * @see ActionUtils#getRequestParameter(String, String, Parameters, Request)
   */
  public abstract String getRequestParameter(String sitemapParam, Parameters params, Request request);
  /**  
   * @see ActionUtils#getNewObject(String, String, Parameters, Object[])
   */
  public abstract Object getNewObject(String sitemapParam, Parameters params, Request request, List args) throws CreatorException;
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
  public abstract Object getNewObject(String sitemapParam, String defaultClassName, Parameters params, Request request, List args) throws CreatorException;
  public abstract Element getDomElement(String adql) throws ParserConfigurationException, SAXException, IOException;
  public abstract ValidationHandler validate(String xml);
  public abstract boolean shouldValidate(String xml);
  public abstract boolean shouldBeNSAware(String xml);
}