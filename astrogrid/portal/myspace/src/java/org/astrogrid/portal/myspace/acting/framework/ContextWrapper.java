package org.astrogrid.portal.myspace.acting.framework;

import java.io.InputStream;

import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;

/**
 * Wraps the environment context for use by <code>MySpaceHandler</code>
 * implementations.
 * 
 * @author peter.shillan
 * @see org.astrogrid.portal.myspace.acting.framework.MySpaceHandler
 */
public interface ContextWrapper {
  public static final String PARAM_PROTOCOL = "myspace-context-protocol";
  public static final String PARAM_END_POINT = "myspace-end-point";

  public static final String DEFAULT_PARAM_PROTOCOL = "none";
  public static final String DEFAULT_END_POINT = "file://astrogrid-portal-myspace";

  /**
   * Return a named parameter.
   * 
   * @param param parameter name
   * @return value of named parameter
   */
  public String getParameter(String param);

  /**
   * Return a named parameter or the default value.
   * 
   * @param param parameter name
   * @return value of named parameter or default value
   */
  public String getParameter(String param, String defaultValue);

  /**
   * Return the current user.
   * 
   * @return current user
   */
  public User getUser();

  /**
   * Return the MySpace end point.
   * 
   * @return MySpace end point
   */
  public String getEndPoint();

  
  /**
   * Return the AstroGrid storage location.
   * 
   * @return AstroGrid storage location
   */
  public Agsl getAgsl();

  /**
   * Return the AstroGrid storage client.
   * 
   * @return AstroGrid storage client
   */
  public StoreClient getStoreClient();

  /**
   * Set an attribute for global use.
   * 
   * @param attribute attribute name
   * @param value attribute value
   */
  public void setGlobalAttribute(String attribute, Object value);

  /**
   * Set an attribute for local use.
   * 
   * @param attribute attribute name
   * @param value attribute value
   */
  public void setLocalAttribute(String attribute, Object value);

  /**
   * Return an <code>InputStream</code> for a given file name.
   * 
   * @param fileName file name
   * @return <code>InputStream</code> for a given file name
   * @throws Exception
   */
  public InputStream getFileInputStream(String fileName) throws Exception;
}