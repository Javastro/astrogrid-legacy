package org.astrogrid.portal.myspace.acting.framework;

import java.net.URL;
import java.util.Map;

/**
 * Find the URL for a MySpace entry.
 * 
 * @author peter.shillan
 */
public class FindURLHandler extends AbstractMySpaceHandler {
  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public FindURLHandler(ContextWrapper context) {
    super(context);
  }

  /**
   * Find the URL for a MySpace entry.
   * 
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {
    String src = context.getParameter(MySpaceHandler.PARAM_SOURCE_PATH);
    
    if(src != null && src.length() > 0) {
      URL url = context.getMySpaceTree().getFile( src ).getNode().contentURL() ;
      
      addLocalResult(MySpaceHandler.PARAM_TARGET_PATH, url.toString(), results);
    }
    else {
      throw new MySpaceHandlerException("invalid source");
    }
  }
}
