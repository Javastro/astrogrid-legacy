package org.astrogrid.datacenter.cocoon.acting;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Category;
import org.astrogrid.common.creator.CreatorException;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtils;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtilsFactory;
import org.astrogrid.datacenter.cocoon.acting.utils.ValidationHandler;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
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
   *       <li><code>datacenter-end-point</code>: URL for the DataCenter delegate</li>
   *       <li><code>datacenter-delegate-class</code>: class name of the DataCenter delegate</li>
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

    ValidationHandler handler = new ValidationHandler();
    try {
      String delegateClassName = utils.getRequestParameter("datacenter-delegate-class", params, request);
      logger.debug("[act] delegate class name: " + delegateClassName);

      String datacenterEndPoint = utils.getRequestParameter("datacenter-end-point", params, request);
      logger.debug("[act] datacenter end point: " + datacenterEndPoint);
      
      DatacenterDelegate delegate =
        getDelegate(params, utils, request, delegateClassName, datacenterEndPoint);
      logger.debug("[act] datacenter delegate class: " + delegate.getClass().getName());
      
      String adql = utils.getRequestParameter("adql-query", params, request);
      logger.debug("[act] adql: " + adql);

      Element queryInput = utils.getDomElement(adql);
      logger.debug("[act] query input: " + queryInput); 

      Element queryResult = null;
      queryResult = delegate.query(queryInput);
      
//      Element queryResult = queryInput;
      logger.debug("[act] query result: " + queryResult);
      
      request.setAttribute("adql-query-result", queryResult);
      request.setAttribute("adql-query-errors", "false");
      sitemapParams.put("adql-query-errors", "false");
    }
    catch(Exception e) {
      logger.debug("[act] exception: " + e.getLocalizedMessage());
      request.setAttribute("adql-query-errors", "true");
      request.setAttribute("adql-query-error-message", e.getLocalizedMessage());
      sitemapParams = null;
    }
		catch(Throwable t) {
			logger.debug("[act] exception: " + t.getLocalizedMessage());
			request.setAttribute("adql-query-errors", "true");
			request.setAttribute("adql-query-error-message", t.getLocalizedMessage());
			sitemapParams = null;
		}
  
    return sitemapParams;
  }

  private DatacenterDelegate getDelegate(Parameters params, ActionUtils utils, Request request, String delegateClassName, String datacenterEndPoint) throws CreatorException, MalformedURLException, ServiceException, IOException {
    DatacenterDelegate delegate = null;
    
    if(delegateClassName.length() > 0) {
      delegate =
          (DatacenterDelegate) utils.getNewObject(
              "datacenter-delegate-class", params, request, null);
    }
    else if(datacenterEndPoint.length() > 0){
      delegate = DatacenterDelegate.makeDelegate(datacenterEndPoint);
    }
    else {
      delegate = DatacenterDelegate.makeDelegate(null);
    }
  
    return delegate;
  }
}
