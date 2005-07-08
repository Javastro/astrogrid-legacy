package org.astrogrid.portal.myspace.acting.framework;

/**
 * Create a MySpace handler based on the action value.
 * 
 * @author peter.shillan
 */
public class MySpaceHandlerFactory {
  // MySpace actions implemented
  private static final String 
  	ACTION_COPY_FILE = "myspace-copy-file",
  	ACTION_DELETE_FILE = "myspace-delete-file", 
	ACTION_MOVE_FILE = "myspace-move-file",
	ACTION_NEW_DIRECTORY = "myspace-new-directory",
  	ACTION_UPLOAD_FILE = "myspace-upload-file",
  	ACTION_UPLOAD_URL = "myspace-upload-url",
  	ACTION_LOAD_BRANCH = "myspace-load-branch",
  	ACTION_RELOCATE_FILE = "myspace-relocate-file",
  	ACTION_REFRESH_DIRECTORY = "myspace-refresh-directory" ;
 
  // MySpace actions yet to be implemented
  private static final String 
  	ACTION_RENAME_FILE = "myspace-rename-file",
  	ACTION_RENAME_DIRECTORY = "myspace-rename-directory",
  	ACTION_DELETE_DIRECTORY = "myspace-delete-directory" ;
	;
  
  // MySpace actions obsolete
  private static final String 
	ACTION_CHANGE = "myspace-change",
	ACTION_FIND_URL = "myspace-find-url" ;  
  
  
  /**
   * Create a MySpace handler based on the action value.
   * 
   * @param action MySpace action to perform
   * @param context environment context
   */
  
  public static MySpaceHandler getMySpaceHandler(String action, ContextWrapper context) {
    AbstractMySpaceHandler result = null;
    
    if(action != null && action.length() > 0) {
      if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_CHANGE)) {
        result = new ChangeHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_COPY_FILE)) {
        result = new CopyHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_DELETE_FILE)) {
        result = new DeleteHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_DELETE_DIRECTORY)) {
          result = new DeleteDirectoryHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_FIND_URL)) {
        result = new FindURLHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_MOVE_FILE)) {
        result = new MoveHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_NEW_DIRECTORY)) {
        result = new NewContainerHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_UPLOAD_FILE)) {
        result = new UploadHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_UPLOAD_URL)) {
        result = new UploadURLHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_LOAD_BRANCH)) {
          result = new LoadBranchHandler(context); 
      }
      else if( action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_REFRESH_DIRECTORY ) ) {
          result = new RefreshDirectoryHandler(context); 
      }
      else if( action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_RELOCATE_FILE ) ) {
          result = new RelocateFileHandler(context); 
      }
    }
    
    return result;
  }
  
  /**
   * No instances allowed. 
   */
  private MySpaceHandlerFactory() {
    super();
  }
}
