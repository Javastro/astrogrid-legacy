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

/**
 * This class provides the DataCenter UI with the facility to
 * validate an XML document provided by the Query Builder.
 * 
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class XMLValidatorAction extends AbstractAction {
  private Category logger = Category.getInstance(getClass());

  /**
   * <p>
   *   Validate the XML document provided.
   * </p>
   * <p>
   *   SiteMap Requirements:
   *     <ol>
   *       <li><code>xml-param</code>: name of the <code>Request</code> parameter containing the XML document</li>
   *     </ol>
   * </p>
   * <p>
   *   SiteMap Outputs:
   *     <ol>
   *       <li><code>xml-valid</code>: "true" if XML was valid</li>
   *     </ol>
   * </p>
   * <p>
   *   Request Attribute Outputs:
   *     <ol>
   *       <li><code>xml-valid</code>: "true" if XML was valid</li>
   *       <li><code>xml-validation-errors</code>: "true" if XML produced errors</li>
   *       <li><code>xml-validation-warnings</code>: "true" if XML produced warnings</li>
   *       <li><code>xml-validation-fatal-errors</code>: "true" if XML produced fatal errors</li>
   *       <li><code>xml-errors</code>: XML produced errors</li>
   *       <li><code>xml-warnings</code>: XML produced warnings</li>
   *       <li><code>xml-fatal-errors</code>: XML produced fatal errors</li>
   *     </ol>
   * </p>
   * 
   * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
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