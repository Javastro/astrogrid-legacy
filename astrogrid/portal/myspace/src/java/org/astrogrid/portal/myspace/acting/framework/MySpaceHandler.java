package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

public interface MySpaceHandler {
  public static final String 
  	PARAM_ACTION = "myspace-action",
  	PARAM_ACTION_HANDLER = "myspace-action-handler",
  	PARAM_ACTION_ERR_MSG = "myspace-action-error-message",
  	PARAM_TARGET_NAME = "myspace-target-name",
  	PARAM_TARGET_PATH = "myspace-target-path",
  	PARAM_SOURCE_NAME = "myspace-source-name",
  	PARAM_SOURCE_PATH = "myspace-source-path",
  	PARAM_DIRECTORY_VIEW_PATH = "myspace-directory-view-path",
  	PARAM_TREE_VIEW_PATH = "myspace-tree-view-path",
  	PARAM_TREE_OPEN_BRANCHES = "myspace-tree-open-branches",
  	PARAM_REQUESTED_MODE = "requested-mode",
  	PARAM_FILESTORE = "myspace-filestore" ;
  
  // These require reworking. Used in/adjacent to uploading a file from local file system.
  public static final String 
	PARAM_SELECTED = "selected",
  	PARAM_CLIPBOARD = "clipboard",
  	PARAM_LOCAL_FILE = "localFile",
  	PARAM_URL = "URL",
  	PARAM_CURRENT_LOCATION = "currentLocation" ;
  /**
   * Does the handler's work and returns a map of name/value pairs.
   * 
   * @return map of attributes set during work
   */
  public Map execute();
}