package org.astrogrid.portal.myspace.acting.framework;

/**
 * Create a MySpace handler based on the action value.
 * 
 * @author peter.shillan
 */
public class MySpaceHandlerFactory {
  // MySpace actions.
  private static final String ACTION_CHANGE = "myspace-change";
  private static final String ACTION_COPY = "myspace-copy";
  private static final String ACTION_DELETE = "myspace-delete";
  private static final String ACTION_FIND_URL = "myspace-find-url";
  private static final String ACTION_MOVE = "myspace-move";
  private static final String ACTION_NEW_CONTAINER = "myspace-new-container";
  private static final String ACTION_UPLOAD = "myspace-upload";
  private static final String ACTION_UPLOAD_URL = "myspace-upload-url";
  
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
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_COPY)) {
        result = new CopyHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_DELETE)) {
        result = new DeleteHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_FIND_URL)) {
        result = new FindURLHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_MOVE)) {
        result = new MoveHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_NEW_CONTAINER)) {
        result = new NewContainerHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_UPLOAD)) {
        result = new UploadHandler(context); 
      }
      else if(action.equalsIgnoreCase(MySpaceHandlerFactory.ACTION_UPLOAD_URL)) {
        result = new UploadURLHandler(context); 
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
