package org.astrogrid.datacenter.cocoon.acting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Category;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtils;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtilsFactory;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;

/**
 * This class provides the DataCenter UI with the facility to
 * load a given MySpace named ADQL file into the Query Builder.
 * 
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class LoadAction extends AbstractAction {
  private Category logger = Category.getInstance(getClass());

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
    Map sitemapParams = new HashMap();
    ActionUtils utils = ActionUtilsFactory.getActionUtils();

    Request request = ObjectModelHelper.getRequest(objectModel);
    
    logger.debug("[act] params:  " + params);
    logger.debug("[act] request: " + request);
    
    String endPoint = params.getParameter("myspace-end-point", "http://localhost:8080/myspace");
    logger.debug("[act] endPoint: " + endPoint);
    // TODO: get user id from web page
    // TODO: get community id (community delegate) 
    try {
      logger.debug("[act] myspace-delegate-class: 1");
      List delegateArgs = new ArrayList();
      logger.debug("[act] myspace-delegate-class: 2");
      delegateArgs.add(endPoint);
      logger.debug("[act] myspace-delegate-class: 3");
      MySpaceManagerDelegate delegate =
          (MySpaceManagerDelegate) utils.getNewObject(
              "myspace-delegate-class", params, request, delegateArgs);
      logger.debug("[act] myspace-delegate-class: 4");
    
      logger.debug("[act] myspace-delegate-class: " + delegate.getClass().getName());

			String userId = utils.getRequestParameter("username", params, request);
			logger.debug("[act] userId: " + userId);

			String communityId = utils.getRequestParameter("community-id", params, request);
			logger.debug("[act] communityId: " + communityId);

			String credential = utils.getRequestParameter("credential", params, request);
			logger.debug("[act] credential: " + credential);

      String mySpaceName = utils.getRequestParameter("myspace-name", params, request);
			logger.debug("[act] mySpaceName: " + mySpaceName);

      String adqlDocument = delegate.getDataHolding(userId, communityId, credential, mySpaceName);
      delegate.listDataHoldings();
      
      request.setAttribute("adql-document", adqlDocument);
      request.setAttribute("adql-document-loaded", "true");

      logger.debug("[act] adql-document: " + adqlDocument);
      logger.debug("[act] adql-document-loaded: true");

      sitemapParams.put("adql-document-loaded", "true");
    }
    catch(Exception e) {
      request.setAttribute("adql-document-loaded", "false");
      request.setAttribute("adql-document-error-message", e.getLocalizedMessage());
      sitemapParams = null;
      
      logger.debug("[act] exception: " + e.getClass() + ", msg: " + e.getLocalizedMessage());
    }
    catch(Throwable t) {
      request.setAttribute("adql-document-loaded", "false");
			request.setAttribute("adql-document-error-message", t.getLocalizedMessage());
      sitemapParams = null;
      
      logger.debug("[act] throwable: " + t.getClass() + ", msg: " + t.getLocalizedMessage());
    }
    
    logger.debug("[act] sitemapParams: " + sitemapParams);
    
    return sitemapParams;
  }
}
