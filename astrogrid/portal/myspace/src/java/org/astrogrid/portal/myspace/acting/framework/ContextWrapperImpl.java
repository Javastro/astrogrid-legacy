package org.astrogrid.portal.myspace.acting.framework;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.* ;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.servlet.multipart.Part;
import org.astrogrid.community.User;
import org.astrogrid.portal.common.user.UserHelper;
import org.astrogrid.portal.login.common.SessionKeys;
import org.astrogrid.portal.utils.acting.ActionUtils;

// import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;

import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.portal.myspace.filesystem.Tree ;
import org.astrogrid.portal.common.session.AstrogridSessionFactory ;
import org.astrogrid.portal.common.session.AstrogridSession ;
import org.astrogrid.portal.common.session.AttributeKey ;


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
  private Ivorn ivorn;
//  private Agsl agsl;
  
  // private StoreClient storeClient;
  private FileManagerClient fileManagerClient ;
  
  
  public ContextWrapperImpl(ActionUtils utils, Parameters params, Request request, Session session) 
  		throws MalformedURLException, IOException {
      
    this.utils = utils;
    this.params= params;
    this.request = request;
    this.session = session;
    
    try {
        ;
	    // Set the current user.
	    // this.user = UserHelper.getCurrentUser(params, request, session);
	    
      // Get the store client from the VOSpaceResolver.
      //this.ivorn = (Ivorn) utils.getAnyParameterObject(
      //    SessionKeys.IVORN,
      //    params, request, session);
      
      // Set base AstroGrid storage location.
      //jl agsl = storeClient.getEndpoint();
      //this.agsl =  new Agsl (ivorn.getPath() ) ;
    
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
  
  //public Agsl getAgsl() throws IOException {
  //  return agsl;
  //}
  
  public Ivorn getIvorn() throws IOException {
      return ivorn ;
    }
  
  /**
   * Return the AstroGrid file manager delegate.
   * 
   * @return AstroGrid file manager delelgate
   */
  public FileManagerClient getFileManagerClient() {
    return (FileManagerClient)AstrogridSessionFactory.getSession(session).getAttribute(AttributeKey.FILE_MANAGER_CLIENT ) ;
  }
  
  public Tree getMySpaceTree() {
      return (Tree)AstrogridSessionFactory.getSession(session).getAttribute( AttributeKey.MYSPACE_TREE ) ;
  }
  
  
  /**
   * Return the MySpace tree array of open branches
   * 
   * @return the MySpace tree of open branches
   */
  public String[] getMySpaceTreeOpenBranches() {
      return (String[])AstrogridSessionFactory.getSession(session).getAttribute( AttributeKey.MYSPACE_TREE_OPEN_BRANCHES) ;
  }
  
  /**
   * Set the MySpace tree array of open branches
   * 
   * @return void
   */
  public void setMySpaceTreeOpenBranches( String[] openBranches ) {
      AstrogridSessionFactory.getSession(session).setAttribute( AttributeKey.MYSPACE_TREE_OPEN_BRANCHES, openBranches ) ;
  }
  
  public void setGlobalAttribute(String attribute, Object value) {
    session.setAttribute(attribute, value);
  }
  
  public void setLocalAttribute(String attribute, Object value) {
    request.setAttribute(attribute, value);
  }
  
  public InputStream getFileInputStream(String fileName) throws Exception {
    System.out.println( "entry:getFileInputStream()" ) ;  
    InputStream result = null;
    
    System.out.println( "Searching for Part parameters..." ) ;
    for( Enumeration e = request.getParameterNames(); e.hasMoreElements();  ) {
        String param = (String)e.nextElement() ;
        Object value = request.get( param ) ;
        System.out.println( "[" + param + "] [" + value.getClass().getName() + "] : " + value ) ;
        if( value instanceof Part ) {
            String fName = ((Part)value).getFileName() ;
            String uName = ((Part)value).getUploadName() ;
            System.out.println( "Found Part: fname: [" + fName + "] and uName: ["+ uName + "]" ) ;
        }
    }
    
    Part part = (Part) request.get(fileName);
    if (part != null) {
      result = part.getInputStream();
    } else {
      // TODO parameter not found
    }
    
    return result;
  }
}
