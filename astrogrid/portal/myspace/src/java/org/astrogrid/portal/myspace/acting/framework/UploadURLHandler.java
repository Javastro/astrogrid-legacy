package org.astrogrid.portal.myspace.acting.framework;

import java.net.URL;
import java.util.Map;

import org.astrogrid.portal.myspace.filesystem.File;
import org.astrogrid.portal.myspace.filesystem.Tree;

/**
 * Upload the contents of a URL into MySpace.
 * 
 * @author peter.shillan
 */
public class UploadURLHandler extends AbstractMySpaceHandler {

  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public UploadURLHandler(ContextWrapper context) {
    super(context);
  }

  /**
   * Upload the contents of a URL into MySpace.
   * 
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {
    String 
    	fileName = context.getParameter( MySpaceHandler.PARAM_TARGET_NAME ),
    	path = context.getParameter( MySpaceHandler.PARAM_TARGET_PATH ),
    	urlSpec = context.getParameter(MySpaceHandler.PARAM_SOURCE_PATH );
    
    if( fileName != null && fileName.length() > 0 
        &&
        path != null && path.length() > 0
        &&
        urlSpec != null && urlSpec.length() > 0 ) {
        
	    try {
	        URL url = new URL(urlSpec);
	      
	        Tree tree = context.getMySpaceTree() ;
	        File file = tree.getFile( path + fileName );
	        if( file == null ) {
	            file = tree.newFile( fileName, path ) ;
	        }
	        file.getNode().copyURLToContent( url ) ;
	        file.getNode().transferCompleted() ;

	    }
	    catch(Exception e) {
	      throw new MySpaceHandlerException( "Error uploading url:\n" +
                                             "Source: " + urlSpec + "\n" +
                                             "Name: " + fileName + "\n" +
                                             "Path: " + path, e);
	    }
    }
    else {
      throw new MySpaceHandlerException( "Error uploading url:\n" +
                                         "Source: " + urlSpec + "\n" +
                                         "Name: " + fileName + "\n" +
                                         "Path: " + path );
    }
  }
}
