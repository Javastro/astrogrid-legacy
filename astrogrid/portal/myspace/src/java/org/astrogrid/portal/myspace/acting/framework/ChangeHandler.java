package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

/**
 * Change the MySpace end point used.
 * 
 * @author peter.shillan
 */
public class ChangeHandler extends AbstractMySpaceHandler {
  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public ChangeHandler(ContextWrapper context) {
    super(context);
  }

  /**
   * Set a new MySpace end point for global use.
   * 
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {
    // Set the new MySpace end point.
    String endPoint = context.getEndPoint();
    
    if(endPoint != null && endPoint.length() > 0) {
      addGlobalResult(ContextWrapper.PARAM_END_POINT, endPoint, results);
    }
    else {
      throw new MySpaceHandlerException("no valid end point");
    }
  }
}
