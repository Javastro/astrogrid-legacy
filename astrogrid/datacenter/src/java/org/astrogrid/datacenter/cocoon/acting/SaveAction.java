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
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtils;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtilsFactory;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class SaveAction extends AbstractAction {

  /**
   * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters params) {
    Map sitemapParams = new HashMap();
    ActionUtils utils = ActionUtilsFactory.getActionUtils();

    Request request = ObjectModelHelper.getRequest(objectModel);
    
    String endPoint = params.getParameter("myspace-end-point", "http://localhost:8080/myspace");
    // TODO: get user id from web page
    // TODO: get community id (community delegate) 
    try {
      List delegateArgs = new ArrayList();
      delegateArgs.add(endPoint);
      MySpaceManagerDelegate delegate =
          (MySpaceManagerDelegate) utils.getNewObject(
              "myspace-delegate-class", params, request, delegateArgs);
    
      // do something
      String userId = "gps";
      String communityId = "tag";
      String mySpaceName = utils.getRequestParameter("myspace-name", params, request);
      String adqlDocument = utils.getRequestParameter("adql-query", params, request);
      
      boolean saved =
          delegate.saveDataHolding(
              userId,
              communityId,
              mySpaceName,
              adqlDocument,
              "QUERY",
              "Overwrite");
      
      if(saved) {
        request.setAttribute("adql-document-saved", "true");
        sitemapParams.put("adql-document-saved", "true");
      }
      else {
        request.setAttribute("adql-document-saved", "false");
        request.setAttribute("adql-document-error-message", "MySpace failed to save document");
        sitemapParams = null;
      }
    }
    catch(Exception e) {
      request.setAttribute("adql-document-saved", "false");
      request.setAttribute("adql-document-error-message", e.getLocalizedMessage());
      sitemapParams = null;
    }
    
    return sitemapParams;
  }
}
