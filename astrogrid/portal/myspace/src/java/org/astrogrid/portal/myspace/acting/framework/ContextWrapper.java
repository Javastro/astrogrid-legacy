package org.astrogrid.portal.myspace.acting.framework;

import java.io.IOException;
import java.io.InputStream;

import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreFile;

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
  public static final String DEFAULT_END_POINT = "file://astrogrid-myspace-file";

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
   * Return the AstroGrid storage location.
   * 
   * @return AstroGrid storage location
   * @throws IOException
   */
  public Agsl getAgsl() throws IOException;

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
  
  /**
   * Return the cache of MySpace files for a user.
   * 
   * 
   * @return <code>StoreFile</code> for a user
   *
   */
  public StoreFile getMySpaceCache() ;
  
  
  /**
   * Set the cache of MySpace files for a user.
   * 
   * 
   */
  public void setMySpaceCache( StoreFile cache ) ;
  
  
  
  
  
  
  
}