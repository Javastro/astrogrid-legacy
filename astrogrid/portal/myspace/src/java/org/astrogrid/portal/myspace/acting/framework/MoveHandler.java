package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

import org.astrogrid.store.Agsl;

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
    String src = context.getParameter(MySpaceHandler.PARAM_SRC);
    String dest = context.getParameter(MySpaceHandler.PARAM_DEST);
    
    if(src != null && src.length() > 0 &&
        dest != null && dest.length() > 0) {
      context.getStoreClient().move(src, new Agsl(context.getAgsl(), dest));
    }
    else {
      throw new MySpaceHandlerException("invalid source or destination");
    }
  }
}
