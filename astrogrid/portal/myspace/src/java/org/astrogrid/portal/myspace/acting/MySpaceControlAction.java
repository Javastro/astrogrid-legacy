package org.astrogrid.portal.myspace.acting;

import java.util.Map;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;

import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
import org.astrogrid.portal.myspace.acting.framework.*;

/**
 * Find and execute the correct handler for the desired MySpace action.
 * 
 * @author peter.shillan
 */
public class MySpaceControlAction extends AbstractAction {
  // MySpace action parameters.
  private static final String PARAM_ACTION = "myspace-action";
  
  /**
   * Find and execute the correct handler for the desired MySpace action.
   * 
   * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public Map act(
      Redirector redirector, 
      SourceResolver resolver, 
      Map objectModel, 
      String source, 
      Parameters params) {
    Logger logger = getLogger();
    
    // Create the sitemap parameters to be returned.
    Map sitemapParams = null;
    
    // Get the utilities object.
    ActionUtils utils = ActionUtilsFactory.getActionUtils();
    
    // Get the request and session context objects.
    Request request = ObjectModelHelper.getRequest(objectModel);
    Session session = request.getSession(true);
    
    // Get the correct handler and execute.
    ContextWrapper context = null;
    String contextProtocol = utils.getAnyParameter(
        ContextWrapper.PARAM_PROTOCOL, params, request, session);

    try {
      context = ContextWrapperFactory.getContextWrapper(
          contextProtocol, utils, params, request, session);
      sitemapParams = findAndExecute(context);
    }
    catch(Throwable t) {
      // Log error.  Sitemap results will be null, indicating failure.
      logger.error("error processing MySpace action", t);
    }
    
    return sitemapParams;
  }
  
  /**
   * Get the correct MySpace handler and execute it.
   * 
   * @param context environment context
   * @return sitemap results
   */
  private Map findAndExecute(ContextWrapper context) {
    Logger logger = getLogger();

    Map results = null;
    
    // Get MySpace action.
    String action = context.getParameter(MySpaceControlAction.PARAM_ACTION);
    
    // Choose a MySpace handler.
    MySpaceHandler handler = null;
    if(action != null && action.length() > 0) {
      handler = MySpaceHandlerFactory.getMySpaceHandler(action, context);
    }

    // Execute a valid handler.
    if(handler != null) {
      results = handler.execute();
    }
    else {
      logger.error("no handler found for MySpace action [" + action + "]");
    }
    
    return results;
  }
}
