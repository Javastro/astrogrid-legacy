package org.astrogrid.portal.myspace.acting.framework;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.servlet.multipart.MultipartHttpServletRequest;
import org.apache.cocoon.servlet.multipart.Part;
import org.astrogrid.community.User;
import org.astrogrid.portal.common.user.UserHelper;
import org.astrogrid.portal.login.common.SessionKeys;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.VoSpaceResolver;

/**
 * Default implementation of <code>org.astrogrid.portal.myspace.acting.framework.ContextWrapper</code>.
 * 
 * @author peter.shillan
 * @see org.astrogrid.portal.myspace.acting.framework.ContextWrapper
 */
public class ContextWrapperImpl implements ContextWrapper {
  private ActionUtils utils;
  private Parameters params;
  private Request request;
  private Session session;
  private User user;
  private String endPoint;
  private Ivorn ivorn;
  private Agsl agsl;
  private StoreClient storeClient;
  
  public ContextWrapperImpl(ActionUtils utils, Parameters params, Request request, Session session) 
  		throws MalformedURLException, IOException {
    this.utils = utils;
    this.params= params;
    this.request = request;
    this.session = session;
    
    try {
	    // Set the current user.
	    user = UserHelper.getCurrentUser(params, request, session);
	    
	    // Set MySpace end point.
	    endPoint = utils.getAnyParameter(
	        ContextWrapper.PARAM_END_POINT,
	        ContextWrapper.DEFAULT_END_POINT,
	        params, request, session);
	    
	    // Set base AstroGrid storage location.
	    agsl = new Agsl(endPoint);
	    
	    // Get the storage client.
	    storeClient = StoreDelegateFactory.createDelegate(user, agsl);
      
      // Get the store client from the VOSpaceResolver.
      ivorn = (Ivorn) utils.getAnyParameterObject(
          SessionKeys.IVORN,
          params, request, session);

      storeClient = VoSpaceResolver.resolveStore(user, ivorn);
    }
	  catch(Throwable t) {
      throw new IOException("Could not create a valid context: " + t.getLocalizedMessage());
	  }
  }
  
  public String getParameter(String param) {
    return utils.getAnyParameter(param, params, request, session);
  }
  
  public String getParameter(String param, String defaultValue) {
    return utils.getAnyParameter(param, defaultValue, params, request, session);
  }
  
  public User getUser() {
    return user;
  }
  
  public Agsl getAgsl() throws IOException {
    return VoSpaceResolver.resolveAgsl(ivorn);
  }
  
  public StoreClient getStoreClient() {
    return storeClient;
  }
  
  public void setGlobalAttribute(String attribute, Object value) {
    session.setAttribute(attribute, value);
  }
  
  public void setLocalAttribute(String attribute, Object value) {
    request.setAttribute(attribute, value);
  }
  
  public InputStream getFileInputStream(String fileName) throws Exception {
    InputStream result = null;
    
    Part part = (Part) request.get(fileName);
    if (part != null) {
      result = part.getInputStream();
    } else {
      // TODO parameter not found
    }
    
    return result;
  }
}
