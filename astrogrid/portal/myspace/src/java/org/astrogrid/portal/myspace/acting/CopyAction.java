package org.astrogrid.portal.myspace.acting;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;

public class CopyAction extends AbstractAction {

  /* (non-Javadoc)
   * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters params) {
    Logger logger = this.getLogger();
    
    Map sitemapParams = new HashMap();
    ActionUtils utils = ActionUtilsFactory.getActionUtils();

    Request request = ObjectModelHelper.getRequest(objectModel);
    Session session = request.getSession(true);
    
    logger.debug("[act] params:  " + params);
    logger.debug("[act] request: " + request);
    logger.debug("[act] session: " + session);
    
    String endPoint = utils.getAnyParameter("myspace-end-point", "http://localhost:8080/myspace", params, request, session);
    logger.debug("[act] endPoint: " + endPoint);

    String renameResult = null;
    try {
      MySpaceClient delegate = MySpaceDelegateFactory.createDelegate(endPoint);
    
      logger.debug("[act] myspace-delegate-class: " + delegate.getClass().getName());

      String userId = utils.getAnyParameter("username", params, request, session);
      logger.debug("[act] userId: " + userId);

      String communityId = utils.getAnyParameter("community-id", params, request, session);
      logger.debug("[act] communityId: " + communityId);

      String credential = utils.getAnyParameter("credential", params, request, session);
      logger.debug("[act] credential: " + credential);

      String oldMySpaceName = utils.getAnyParameter("myspace-old-name", params, request, session);
      logger.debug("[act] oldMySpaceName: " + oldMySpaceName);

      String newMySpaceName = utils.getAnyParameter("myspace-new-name", params, request, session);
      logger.debug("[act] newMySpaceName: " + newMySpaceName);
      
      renameResult = delegate.copyDataHolding(userId, communityId, credential, oldMySpaceName, newMySpaceName);
      
      request.setAttribute("myspace-copy", "true");
      request.setAttribute("myspace-copy-result", renameResult);
    }
    catch(Throwable t) {
      request.setAttribute("myspace-copy", "false");
      request.setAttribute("myspace-copy-error-message", t.getLocalizedMessage());
      sitemapParams = null;
      
      logger.debug("[act] throwable: " + t.getClass() + ", msg: " + t.getLocalizedMessage());
    }
    
    logger.debug("[act] sitemapParams: " + sitemapParams);
    
    return sitemapParams;
  }

}
