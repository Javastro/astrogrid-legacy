package org.astrogrid.portal.datacenter.acting;

import java.io.InputStream;
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
import org.astrogrid.portal.common.user.UserHelper;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.community.User;

/**
 * This class provides the DataCenter UI with the facility to
 * load a given MySpace named ADQL file into the Query Builder.
 * 
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class MySpaceLoadAction extends AbstractAction {
  /**
   * Load the required ADQL document from MySpace.
   *
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
    Session session = request.getSession(true);
    
		InputStream inStream = null;
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

      String mySpaceName = utils.getAnyParameter("myspace-name", params, request, session);
			logger.debug("[act] mySpaceName: " + mySpaceName);

			// Copy the input stream to the output buffer.
			inStream = storeClient.getStream(mySpaceName);
			StringBuffer outputBuffer = new StringBuffer();
			
	    int bytesAvailable = inStream.available();
	    while(bytesAvailable > 0) {
	      outputBuffer.append((char)inStream.read());
	      
	      bytesAvailable = inStream.available();
	    }

	    String adqlDocument = outputBuffer.toString(); 
      
      request.setAttribute("adql-document", adqlDocument);
      request.setAttribute("adql-document-loaded", "true");

      logger.debug("[act] adql-document: " + adqlDocument);
      logger.debug("[act] adql-document-loaded: true");

      sitemapParams.put("adql-document", adqlDocument);
      sitemapParams.put("adql-document-loaded", "true");
    }
    catch(Throwable t) {
      request.setAttribute("adql-document-loaded", "false");
			request.setAttribute("adql-document-error-message", t.getLocalizedMessage());
      sitemapParams = null;
      
      logger.debug("[act] throwable: " + t.getClass() + ", msg: " + t.getLocalizedMessage());
    }
    finally {
      try {
        if(inStream != null) {
          inStream.close();
	      }
      }
      catch(Throwable t){
        // assume closure.
      }
    }    

    logger.debug("[act] sitemapParams: " + sitemapParams);
    
    return sitemapParams;
  }
}
