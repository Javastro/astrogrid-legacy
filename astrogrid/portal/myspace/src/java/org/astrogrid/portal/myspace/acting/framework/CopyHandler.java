package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

//import org.astrogrid.store.Agsl;
import org.astrogrid.portal.myspace.filesystem.CopyException;

/**
 * Copy a MySpace entry.
 * 
 * @author peter.shillan
 */
public class CopyHandler extends AbstractMySpaceHandler {
  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public CopyHandler(ContextWrapper context) {
    super(context);
  }

  /**
   * Copy a MySpace entity from one location to another.
   * 
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {
    String targetName = context.getParameter(MySpaceHandler.PARAM_TARGET_NAME);
    String targetPath = context.getParameter(MySpaceHandler.PARAM_TARGET_PATH);
    String sourcePath = context.getParameter(MySpaceHandler.PARAM_SOURCE_PATH);
    
    // Validate the parameters.
    if( targetName != null && targetName.length() > 0 &&
        targetPath != null && targetPath.length() > 0 &&
        sourcePath != null && sourcePath.length() > 0
        ) {
      // Create a new storage location within the same MySpace store.
      // Agsl destination = new Agsl(context.getAgsl(), dest);
      
      // Copy the MySpace entries.
      try {
          context.getMySpaceTree().copyFile( targetName, targetPath, sourcePath ) ;
      }
      catch( CopyException cx ) {
          throw new MySpaceHandlerException( cx.getMessage() ) ;
      }
    }
    else {
      throw new MySpaceHandlerException("invalid source or destination");
    }
  }
}
