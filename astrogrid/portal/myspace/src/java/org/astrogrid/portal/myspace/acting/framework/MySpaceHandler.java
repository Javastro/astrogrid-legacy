package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

public interface MySpaceHandler {
  public static final String PARAM_ACTION = "myspace-action";
  public static final String PARAM_ACTION_HANDLER = "myspace-action-handler";
  public static final String PARAM_ACTION_ERR_MSG = "myspace-action-error-message";
  
  public static String PARAM_SRC = "myspace-src";
  public static String PARAM_DEST = "myspace-dest";
  public static String PARAM_FILE = "myspace-file";
  public static String PARAM_URL = "myspace-url";

  /**
   * Does the handler's work and returns a map of name/value pairs.
   * 
   * @return map of attributes set during work
   */
  public Map execute();
}