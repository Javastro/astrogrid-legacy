package org.astrogrid.portal.datacenter.acting;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Category;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
import org.astrogrid.portal.utils.acting.ValidationHandler;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.AdqlQuerier;
import org.astrogrid.datacenter.delegate.Certification;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.w3c.dom.Element;

/**
 * This class provides the DataCenter UI with the facility to
 * test a given ADQL string from the Query Builder against a
 * DataCenter URL.
 * 
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class TestAction extends AbstractAction {
  private Category logger = Category.getInstance(getClass());

  /**
   * <p>
   *   Test the required ADQL document against a DataCenter URL.
   * </p>
   * <p>
   *   SiteMap Requirements:
   *     <ol>
   *       <li><code>username</code>: name of the user</li>
   *       <li><code>community-id</code>: community identity</li>
   *       <li><code>credential</code>: user credentials</li>
   *       <li><code>datacenter-end-point</code>: URL for the DataCenter delegate</li>
   *       <li><code>datacenter-delegate-service</code>: name of the DataCenter delegate service to use</li>
   *       <li><code>adql-query</code>: name of the <code>Request</code> parameter containing the ADQL query string</li>
   *     </ol>
   * </p>
   * <p>
   *   SiteMap Outputs:
   *     <ol>
   *       <li><code>adql-query-errors</code>: "true" if ADQL produced an error</li>
   *     </ol>
   * </p>
   * <p>
   *   Request Attribute Outputs:
   *     <ol>
   *       <li><code>adql-query-result</code>: <code>DOM</code> element containing query result (VOTable)</li>
   *       <li><code>adql-query-errors</code>: "true" if ADQL produced an error</li>
   *       <li><code>adql-query-error-message</code>: ADQL query error message</li>
   *     </ol>
   * </p>
   * 
   * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters params) {
    Map sitemapParams = new HashMap();
    ActionUtils utils = ActionUtilsFactory.getActionUtils();
    
    Request request = ObjectModelHelper.getRequest(objectModel);
    Session session = request.getSession(true);

    ValidationHandler handler = new ValidationHandler();
    try {
      String delegateService = params.getParameter("datacenter-delegate-service", DatacenterDelegateFactory.DUMMY_SERVICE);
      logger.debug("[act] delegate service type: " + delegateService);

      String datacenterEndPoint = utils.getAnyParameter("datacenter-end-point", params, request, session);
      logger.debug("[act] datacenter end point: " + datacenterEndPoint);
      
      String userId = utils.getAnyParameter("username", params, request, session);
      logger.debug("[act] userId: " + userId);

      String communityId = utils.getAnyParameter("community-id", params, request, session);
      logger.debug("[act] communityId: " + communityId);

      String credential = utils.getAnyParameter("credential", params, request, session);
      logger.debug("[act] credential: " + credential);

      AdqlQuerier delegate =
          DatacenterDelegateFactory.makeAdqlQuerier(
              new Certification(userId, credential), datacenterEndPoint, delegateService);
      logger.debug("[act] datacenter delegate class: " + delegate.getClass().getName());
      
      String adql = utils.getAnyParameter("adql-query", params, request, session);
      logger.debug("[act] adql: " + adql);

      DatacenterResults datacenterResult = null;
      datacenterResult = delegate.doQuery(
          AdqlQuerier.VOTABLE,
          Select.unmarshalSelect(new StringReader(adql)));
      
      Element queryResult = datacenterResult.getVotable();
      logger.debug("[act] query result: " + queryResult);
      
      request.setAttribute("adql-query-result", queryResult);
      request.setAttribute("adql-query-errors", "false");
      sitemapParams.put("adql-query-errors", "false");
    }
		catch(Throwable t) {
			logger.debug("[act] exception: " + t.getLocalizedMessage());
			request.setAttribute("adql-query-errors", "true");
			request.setAttribute("adql-query-error-message", t.getLocalizedMessage());

      StringWriter stackTrace = new StringWriter();
      t.printStackTrace(new PrintWriter(stackTrace));
      request.setAttribute("adql-query-stack-trace", stackTrace.toString());

      try {
        stackTrace.close();
      }
      catch(IOException ioe)
      {
        // Do nothing.
      }

			sitemapParams = null;
		}
  
    return sitemapParams;
  }
}
