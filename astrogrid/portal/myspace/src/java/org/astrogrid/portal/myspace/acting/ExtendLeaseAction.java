package org.astrogrid.portal.myspace.acting;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;

public class ExtendLeaseAction extends AbstractAction {

  /* (non-Javadoc)
   * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters params) {
    Logger logger = this.getLogger();
    
    Map sitemapParams = new HashMap();
    ActionUtils utils = ActionUtilsFactory.getActionUtils();

    Request request = ObjectModelHelper.getRequest(objectModel);
    
    logger.debug("[act] params:  " + params);
    logger.debug("[act] request: " + request);
    
    String endPoint = utils.getAnyParameter("myspace-end-point", "http://localhost:8080/myspace", params, request);
    logger.debug("[act] endPoint: " + endPoint);

    String renameResult = null;
    try {
      MySpaceClient delegate = MySpaceDelegateFactory.createDelegate(endPoint);
    
      logger.debug("[act] myspace-delegate-class: " + delegate.getClass().getName());

      String userId = utils.getAnyParameter("username", params, request);
      logger.debug("[act] userId: " + userId);

      String communityId = utils.getAnyParameter("community-id", params, request);
      logger.debug("[act] communityId: " + communityId);

      String credential = utils.getAnyParameter("credential", params, request);
      logger.debug("[act] credential: " + credential);

      String oldMySpaceName = utils.getAnyParameter("myspace-old-name", params, request);
      logger.debug("[act] oldMySpaceName: " + oldMySpaceName);

      String noOfDaysParam = utils.getAnyParameter("myspace-extension-days", params, request); 
      int noOfDays = Integer.parseInt(noOfDaysParam);
      logger.debug("[act] noOfDays: " + noOfDaysParam);

      renameResult = delegate.extendLease(userId, communityId, credential, oldMySpaceName, noOfDays);
      
      request.setAttribute("myspace-extend-lease", "true");
      request.setAttribute("myspace-extend-lease-result", renameResult);
    }
    catch(Throwable t) {
      request.setAttribute("myspace-extend-lease", "false");
      request.setAttribute("myspace-extend-lease-error-message", t.getLocalizedMessage());
      sitemapParams = null;
      
      logger.debug("[act] throwable: " + t.getClass() + ", msg: " + t.getLocalizedMessage());
    }
    
    logger.debug("[act] sitemapParams: " + sitemapParams);
    
    return sitemapParams;
  }

}
