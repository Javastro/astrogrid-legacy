package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

import org.astrogrid.store.Agsl;

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
    String src = context.getParameter(MySpaceHandler.PARAM_SRC);
    String dest = context.getParameter(MySpaceHandler.PARAM_DEST);
    
    // Validate the parameters.
    if(src != null && src.length() > 0 &&
        dest != null && dest.length() > 0) {
      // Create a new storage location within the same MySpace store.
      Agsl destination = new Agsl(context.getEndPoint(), dest);
      
      // Copy the MySpace entries.
      context.getStoreClient().copy(src, destination);
    }
    else {
      throw new MySpaceHandlerException("invalid source or destination");
    }
  }
}
