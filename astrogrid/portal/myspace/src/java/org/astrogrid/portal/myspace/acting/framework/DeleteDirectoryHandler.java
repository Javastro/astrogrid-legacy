package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;
import org.astrogrid.portal.myspace.filesystem.DeleteException ;

/**
 * Delete a MySpace entry.
 * 
 * @author jeff.lusted
 */
public class DeleteDirectoryHandler extends AbstractMySpaceHandler {
  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public DeleteDirectoryHandler(ContextWrapper context) {
    super(context);
  }

  /**
   * Delete a MySpace entry.
   * 
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {
    String path = context.getParameter(MySpaceHandler.PARAM_TARGET_PATH);
    
    System.out.println( "path: " + path ) ;
    
    if( path != null && path.length() > 0 ) {
        try {
            context.getMySpaceTree().deleteDirectory( path, false );
        }
        catch( DeleteException dx ) {
            throw new MySpaceHandlerException ( dx.getMessage() ) ;
        }
    }
    else {
      throw new MySpaceHandlerException("invalid source");
    }
  }
}
