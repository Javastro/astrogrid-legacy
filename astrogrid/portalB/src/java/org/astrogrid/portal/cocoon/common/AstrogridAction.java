
package org.astrogrid.portal.cocoon.common;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import java.util.Map;
import java.util.HashMap;

/**
 *
 *
 */
public class AstrogridAction extends AbstractAction
{
   /**
    * Switch for our debug statements.
    *
    */
   public static boolean DEBUG_FLAG = true;
   
   /**
    * Cocoon param for the user param in the session.
    *
    */
   private static final String PARAM_CREDENTIAL = "credential";
   

   /**
    * Our action method.
    *
    */
   public Map act(
      Redirector redirector, 
      SourceResolver resolver, 
      Map objectModel, 
      String source, 
      Parameters params)
      {
      
      //
      // Get our current request and session.
      Request request = ObjectModelHelper.getRequest(objectModel);
      Session session = request.getSession();
      
      String credential = (String)session.getAttribute(PARAM_CREDENTIAL);
      if(DEBUG_FLAG) {
         System.out.println("the credential is = " + credential);      
      }
      
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap() ;
      results.put(PARAM_CREDENTIAL,credential);
      return results;
   }  
}