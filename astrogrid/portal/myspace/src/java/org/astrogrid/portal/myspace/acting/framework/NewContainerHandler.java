package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

/**
 * Create a new MySpace container.
 * 
 * @author peter.shillan
 */
public class NewContainerHandler extends AbstractMySpaceHandler {

  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public NewContainerHandler(ContextWrapper context) {
    super(context);
  }

  /**
   * Create a new MySpace container.
   * 
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {
    String dest = context.getParameter(MySpaceHandler.PARAM_DEST);

    if(dest != null && dest.length() > 0) {
      context.getStoreClient().newFolder(dest);
    }
    else {
      throw new MySpaceHandlerException("invalid destination");
    }
  }
}
