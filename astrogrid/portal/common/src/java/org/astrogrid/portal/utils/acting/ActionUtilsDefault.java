package org.astrogrid.portal.utils.acting;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.astrogrid.common.creator.Creator;
import org.astrogrid.common.creator.CreatorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class ActionUtilsDefault implements ActionUtils {
  private Category logger = Category.getInstance(getClass());
  
  static {
    BasicConfigurator.configure();
  }
  
  /**
   * Do not allow creation of <code>ActionUtilsDefault</code>.
   * @see org.astrogrid.datacenter.cocoon.action.ActionUtilsFactory#getActionUtils()
   */
  ActionUtilsDefault() {
  }

  /**
   * Get a request parameter given a parameter passed into the action via the sitemap.
   * 
   * @param param request parameter name
   * @param defaultValue default value of request parameter name
   * @param params sitemap parameters
   * @param request request context object
   * 
   * @return value of request parameter
   */
  public String getRequestParameter(String param, String defaultValue, Parameters params, Request request) {
    // Make sure XML string exists for validation purposes.
    String value = request.getParameter(param);
    if(value == null) {
      value = defaultValue;
    }
    logger.debug("[getRequestParameter] value: " + value);

    return value;
  }

  /**
   * @see ActionUtils#getRequestParameter(String, String, Parameters, Request)
   */
  public String getRequestParameter(String param, Parameters params, Request request) {
    return getRequestParameter(param, "", params, request);
  }
  
  /**
   * Get a session attribute given a attribute passed into the action via the sitemap.
   * 
   * @param param session attribute name
   * @param defaultValue default value of session attribute name
   * @param params sitemap parameters
   * @param session session context object
   * 
   * @return value of session attribute
   */
  public String getSessionAttribute(String param, String defaultValue, Parameters params, Session session) {
    // Make sure XML string exists for validation purposes.
    String value = (String) session.getAttribute(param);
    if(value == null) {
      value = defaultValue;
    }
    logger.debug("[getSessionAttribute] value: " + value);

    return value;
  }

  /**
   * @see ActionUtils#getSessionAttribute(String, String, Parameters, Session)
   */
  public String getSessionAttribute(String param, Parameters params, Session session) {
    return getSessionAttribute(param, "", params, session);
  }
  
  public Object getSessionObject(String sitemapParam, String defaultValue, Parameters params, Session session) {
    Object result = defaultValue;
    
    if(session != null) {
      Object attribute = session.getAttribute(sitemapParam);
      if(attribute != null) {
        result = attribute;
      }
    }
    
    return result;
  }
  
  public Object getSessionObject(String sitemapParam, Parameters params, Session session) {
    return getSessionObject(sitemapParam, null, params, session);
  }

  public String getAnyParameter(String param, Parameters params, Request request, Session session) {
    return getAnyParameter(param, "", params, request, session);
  }
  
  public String getAnyParameter(String param, String defaultValue, Parameters params, Request request, Session session) {
    String result = "";
    
    // Try a request parameter first (if valid request).
    if(request != null) {
      result = getRequestParameter(param, params, request);
      if(result != null && result.length() > 0) {
        logger.info("[getAnyParameter] REQUEST: [" + param + "," + result + "]");
        return result;
      }
    }
    
    // Try a session attribute next (if valid session).
    if(session != null) {
      result = getSessionAttribute(param, params, session);
      if(result != null && result.length() > 0) {
        logger.info("[getAnyParameter] SESSION: [" + param + "," + result + "]");
        return result;
      }
    }
    
    // Get either sitemap parameter or the default value.
    result = params.getParameter(param, defaultValue);
    logger.info("[getAnyParameter] SITEMAP: [" + param + "," + result + "]");
    return result;
  }

  public Object getAnyParameterObject(String sitemapParam, String defaultValue, Parameters params, Request request, Session session) {
    Object result = defaultValue;
    
    Object sessionObject = getSessionObject(sitemapParam, params, session);
    if(sessionObject == null) {
      result = getAnyParameterObject(sitemapParam, params, request, session);
    }
    else {
      result = sessionObject;
    }
    
    return result;
  }

  public Object getAnyParameterObject(String sitemapParam, Parameters params, Request request, Session session) {
    return getAnyParameterObject(sitemapParam, null, params, request, session);
  }
  
  /**  
   * @see ActionUtils#getNewObject(String, String, Parameters, Object[])
   */
  public Object getNewObject(String sitemapParam, Parameters params, Request request, List args) throws CreatorException {
    return getNewObject(sitemapParam, "", params, request, args);
  }
  
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
  public Object getNewObject(String sitemapParam, String defaultClassName, Parameters params, Request request, List args) throws CreatorException {
    Object result = null;
    
    Object[] realArgs = null;
    if(args != null && args.size() > 0) {
      realArgs = args.toArray();
    }
    
    String className = params.getParameter(sitemapParam, defaultClassName);
    
    Creator creator = new Creator();
    result = creator.newInstance(className, realArgs);
    
    return result;
  }
  
  public Element getDomElement(String adql) throws ParserConfigurationException, SAXException, IOException {
    Element result = null;
    
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(shouldBeNSAware(adql));
    factory.setValidating(shouldValidate(adql));
    
    StringReader reader = new StringReader(adql);
    try {
      InputSource is = new InputSource(reader);
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(is);
      
      result = document.getDocumentElement();
    }
    catch(ParserConfigurationException e) {
      logger.debug("[getDomElement] exception: " + e.getMessage());
      throw e;
    }
    catch(SAXException e) {
      logger.debug("[getDomElement] exception: " + e.getMessage());
      throw e;
    }
    catch(IOException e) {
      logger.debug("[getDomElement] exception: " + e.getMessage());
      throw e;
    }
    finally {
      reader.close();
    }
    
    return result;
  }

  public ValidationHandler validate(String xml) {
    ValidationHandler handler = new ValidationHandler();

    logger.debug("[validate] xml:      " + xml);
    logger.debug("[validate] validate: " + shouldValidate(xml));
    logger.debug("[validate] ns aware: " + shouldBeNSAware(xml));

    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    saxParserFactory.setValidating(shouldValidate(xml));
    saxParserFactory.setNamespaceAware(shouldBeNSAware(xml));

    StringReader reader = new StringReader(xml);
    try {
      InputSource is = new InputSource(reader);
      
      SAXParser saxParser = saxParserFactory.newSAXParser();
      saxParser.getXMLReader().setErrorHandler(handler);
      saxParser.parse(is, handler);
    }
    catch(ParserConfigurationException e) {
      logger.debug("[validate] exception: " + e.getMessage());
      handler.exception(e);
    }
    catch(SAXException e) {
      logger.debug("[validate] exception: " + e.getMessage());
      handler.exception(e);
    }
    catch(IOException e) {
      logger.debug("[validate] exception: " + e.getMessage());
      handler.exception(e);
    }
    finally {
      reader.close();
    }
    
    return handler;
  }
  
  public boolean shouldValidate(String xml) {
    return ( xml.indexOf("<!DOCTYPE") > -1 ||
             xml.indexOf(":schemaLocation") > -1 );
  }
  
  public boolean shouldBeNSAware(String xml) {
    return xml.indexOf("xmlns:") > -1;
  }
}