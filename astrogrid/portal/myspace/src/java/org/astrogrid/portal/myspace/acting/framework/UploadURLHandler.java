package org.astrogrid.portal.myspace.acting.framework;

import java.net.URL;
import java.util.Map;

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
    String dest = context.getParameter(MySpaceHandler.PARAM_DEST);
    String urlSpec = context.getParameter(MySpaceHandler.PARAM_URL);
    
    if(dest != null && dest.length() > 0 &&
        urlSpec != null && urlSpec.length() > 0) {
	    try {
	      URL url = new URL(urlSpec);
		    context.getStoreClient().putUrl(url, dest, false);
	    }
	    catch(Exception e) {
	      throw new MySpaceHandlerException("error uploading url", e);
	    }
    }
    else {
      throw new MySpaceHandlerException("invalid destination or url");
    }
  }
}
