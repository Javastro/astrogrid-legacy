package org.astrogrid.portal.datacenter.acting;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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

/**
 * This class provides the DataCenter UI with the facility to
 * load a given MySpace named ADQL file into the Query Builder.
 * 
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class MySpaceBrowseAction extends AbstractAction {
  /**
   * <p>
   *   Load the required ADQL document from MySpace.
   * </p>
   * <p>
   *   SiteMap Requirements:
   *     <ol>
   *       <li><code>myspace-end-point</code>: URL for the MySpace delegate</li>
   *       <li><code>myspace-delegate-class</code>: class name of the MySpace delegate</li>
   *       <li><code>myspace-name</code>: name of the <code>Request</code> parameter containing the MySpace name</li>
   *     </ol>
   * </p>
   * <p>
   *   SiteMap Outputs:
   *     <ol>
   *       <li><code>adql-document-loaded</code>: "true" if ADQL was successfully loaded</li>
   *     </ol>
   * </p>
   * <p>
   *   Request Attribute Outputs:
   *     <ol>
   *       <li><code>adql-document-loaded</code>: "true" if ADQL was successfully loaded</li>
   *       <li><code>adql-document</code>: ADQL document</li>
   *     </ol>
   * </p>
   * 
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

    try {
      MySpaceClient delegate = MySpaceDelegateFactory.createDelegate(endPoint);
    
      logger.debug("[act] myspace-delegate-class: " + delegate.getClass().getName());

			String userId = utils.getAnyParameter("username", params, request);
			logger.debug("[act] userId: " + userId);

			String communityId = utils.getAnyParameter("community-id", params, request);
			logger.debug("[act] communityId: " + communityId);

			String credential = utils.getAnyParameter("credential", params, request);
			logger.debug("[act] credential: " + credential);

      String mySpaceName = utils.getAnyParameter("myspace-name", params, request);
			logger.debug("[act] mySpaceName: " + mySpaceName);

      String mySpaceQuery = utils.getAnyParameter("myspace-query", params, request);
      logger.debug("[act] mySpaceQuery: " + mySpaceQuery);
      Vector mySpaceItems = delegate.listDataHoldings(userId, communityId, credential, buildQuery(userId, communityId, credential, mySpaceQuery));
      
      request.setAttribute("myspace-items", mySpaceItems);
      request.setAttribute("myspace-items-loaded", "true");

      logger.debug("[act] myspace-items: " + mySpaceItems);
      logger.debug("[act] myspace-items-loaded: true");

      sitemapParams.put("myspace-items-loaded", "true");
    }
    catch(Throwable t) {
      request.setAttribute("myspace-items-loaded", "false");
			request.setAttribute("myspace-items-error-message", t.getLocalizedMessage());
      sitemapParams = null;
      
      logger.debug("[act] throwable: " + t.getClass() + ", msg: " + t.getLocalizedMessage());
    }
    
    logger.debug("[act] sitemapParams: " + sitemapParams);
    
    return sitemapParams;
  }
  
  private String buildQuery(String userId, String communityId, String credential, String mySpaceQuery) {
    return "/" + userId + "@" + communityId + "/server/QUERY/" + mySpaceQuery;
  }
}
