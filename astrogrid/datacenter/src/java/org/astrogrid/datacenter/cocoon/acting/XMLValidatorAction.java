package org.astrogrid.datacenter.cocoon.acting;

import java.util.Map;
import java.util.HashMap;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Category;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtils;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtilsFactory;
import org.astrogrid.datacenter.cocoon.acting.utils.ValidationHandler;

public class XMLValidatorAction extends AbstractAction {
  private Category logger = Category.getInstance(getClass());

  public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters params) {
    Map sitemapParams = new HashMap();
    ActionUtils utils = ActionUtilsFactory.getActionUtils();
    
    Request request = ObjectModelHelper.getRequest(objectModel);
    String xml = utils.getRequestParameter("xml-param", params, request);
    
    ValidationHandler handler = utils.validate(xml);
    if(handler.valid()) {
      request.setAttribute("xml-valid", "true");
      sitemapParams.put("xml-valid", "true");

      logger.debug("[act] sitemap params: " + sitemapParams);
    }
    else {
      request.setAttribute("xml-validation-errors", handler.getErrorMessages());
      request.setAttribute("xml-validation-warnings", handler.getWarningMessages());
      request.setAttribute("xml-validation-fatal-errors", handler.getFatalErrorsMessages());
      
      request.setAttribute("xml-valid", "false");

      request.setAttribute("xml-errors", Boolean.toString(handler.anyErrors()));
      request.setAttribute("xml-warnings", Boolean.toString(handler.anyWarnings()));
      request.setAttribute("xml-fatal-errors", Boolean.toString(handler.anyFatalErrors()));

      sitemapParams = null;
    }
    
    return sitemapParams;
  }
}