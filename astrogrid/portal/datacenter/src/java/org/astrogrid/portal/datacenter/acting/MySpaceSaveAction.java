package org.astrogrid.portal.datacenter.acting;

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
import org.astrogrid.community.User;
import org.astrogrid.portal.common.user.UserHelper;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;

import org.apache.avalon.framework.logger.ConsoleLogger;

/**
 * This class provides the DataCenter UI with the facility to
 * save a given MySpace named ADQL file from the Query Builder.
 * 
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class MySpaceSaveAction extends AbstractAction {
    
    /**
     * Switch for our debug statements. 
     *  
     */
    private static final boolean DEBUG_TO_SYSTEM_OUT = true;
     
  /**
   * Save the required ADQL document to MySpace.
   * 
   * <p>
   *   SiteMap Outputs:
   *     <ol>
   *       <li><code>adql-document-saved</code>: "true" if ADQL was successfully saved</li>
   *     </ol>
   * </p>
   * <p>
   *   Request Attribute Outputs:
   *     <ol>
   *       <li><code>adql-document-saved</code>: "true" if ADQL was successfully saved</li>
   *       <li><code>adql-document-error-message</code>: ADQL save error message</li>
   *     </ol>
   * </p>
   * 
   * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters params) {
    Logger logger = this.retrieveLogger();
    
    Map sitemapParams = new HashMap();
    ActionUtils utils = ActionUtilsFactory.getActionUtils();

    Request request = ObjectModelHelper.getRequest(objectModel);
    Session session = request.getSession(true);
    
    try {
      // Set the current user.
      User user = UserHelper.getCurrentUser(params, request, session);
      
      // Set MySpace end point.
      String endPoint = utils.getAnyParameter(
          "myspace-end-point",
          params, request, session);
      
      // Set base AstroGrid storage location.
      Agsl agsl = new Agsl(endPoint);
      
      // Get the storage client.
      StoreClient storeClient = StoreDelegateFactory.createDelegate(user, agsl);

      String adqlDocument = utils.getAnyParameter("adql-query", params, request, session);
      logger.debug("[act] adqlDocument: " + adqlDocument);
      System.out.println("[act] adqlDocument: " + adqlDocument);

      String mySpaceName = utils.getAnyParameter("myspace-name", params, request, session);
      logger.debug("[act] mySpaceName: " + mySpaceName);

      storeClient.putString(adqlDocument, mySpaceName, false);
//      request.setAttribute("adql-document-saved", "true");
//      sitemapParams.put("adql-document-saved", "true");
    }
		catch(Throwable t) {
			request.setAttribute("adql-document-saved", "false");
			request.setAttribute("adql-document-error-message", t.getLocalizedMessage());
			sitemapParams = null;
		}
      
    return sitemapParams;
  }
  
  
  /**
   * During unit tests the logger isn't setup properly, hence this method to
   * use a console logger instead.  Also will log to console
   * if debugToSystemOutOn - can be useful.
   *  
   */
  private Logger retrieveLogger() {
      Logger logger = super.getLogger();
      if (logger == null || DEBUG_TO_SYSTEM_OUT) {
          enableLogging(new ConsoleLogger());
          logger = super.getLogger();
      }
      return logger ;
  }  
  
  
}
