package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

// import org.astrogrid.store.Agsl;
import org.astrogrid.portal.myspace.filesystem.MoveException ;

/**
 * Move a MySpace entry.
 * 
 * @author peter.shillan
 */
public class MoveHandler extends AbstractMySpaceHandler {
  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public MoveHandler(ContextWrapper context) {
    super(context);
  }

  /**
   * Move a MySpace entry.
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {
    String sourcePath = context.getParameter(MySpaceHandler.PARAM_SOURCE_PATH);
    String targetName = context.getParameter(MySpaceHandler.PARAM_TARGET_NAME);
    String targetPath = context.getParameter(MySpaceHandler.PARAM_TARGET_PATH);
    
    if( sourcePath != null && sourcePath.length() > 0 
        &&
        targetName != null && targetName.length() > 0 
        &&
        targetPath != null && targetPath.length() > 0 ) {
        
        try {
            context.getMySpaceTree().moveFile( targetName, targetPath, sourcePath ) ;
        }
        catch( MoveException mx ) {
            throw new MySpaceHandlerException( mx.getMessage() ) ;
        }
    }
    else {
        throw new MySpaceHandlerException("invalid source or destination");
    }
  }
}
