package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

/**
 * Delete a MySpace entry.
 * 
 * @author peter.shillan
 */
public class DeleteHandler extends AbstractMySpaceHandler {
  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public DeleteHandler(ContextWrapper context) {
    super(context);
  }

  /**
   * Delete a MySpace entry.
   * 
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {
    String src = context.getParameter(MySpaceHandler.PARAM_SRC);
    
    if(src != null && src.length() > 0) {
      context.getStoreClient().delete(src);
    }
    else {
      throw new MySpaceHandlerException("invalid source");
    }
  }
}
