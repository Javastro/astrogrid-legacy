package org.astrogrid.portal.acting;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;

/**
 * Given a list of parameter names, return the values to the sitemap from either in priority order:
 *   <ul>
 *     <li>request</li>
 *     <li>session</li>
 *     <li>sitemap</li>
 *   </ul>
 * If any of the parameters does not exist, action fails.
 * <p/>
 * SiteMap parameters:
 *   <ol>
 *     <dt>parameter-names</dt>
 *     <dd>space-separated list of parameter names to return</dd>
 *   </ol>
 */
public class ReturnAnyParameterAction extends AbstractAction {

  public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters params)
      throws Exception {
    Map sitemapParams = new HashMap();
    
    ActionUtils utils = ActionUtilsFactory.getActionUtils();
    Request request = ObjectModelHelper.getRequest(objectModel);
    Session session = request.getSession(true);
    
    String parameterNames = params.getParameter("parameter-names", "");
    if(parameterNames != null && parameterNames.length() > 0) {
      String parameterName = null;
      String parameterValue = null;
      StringTokenizer tokens = new StringTokenizer(parameterNames);
      while(tokens.hasMoreTokens()) {
        parameterName = tokens.nextToken();
        parameterValue = utils.getAnyParameter(parameterName, params, request, session);
        
        if(parameterValue != null && parameterValue.length() > 0) {
          sitemapParams.put(parameterName, parameterValue); 
        }
        else {
          return null;
        }
      }
    }
    else {
      return null;
    }
    
    return sitemapParams;
  }

}
