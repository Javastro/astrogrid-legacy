package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

/**
 * Create a new MySpace container.
 * 
 * @author peter.shillan
 */
public class NewContainerHandler extends AbstractMySpaceHandler {

  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public NewContainerHandler(ContextWrapper context) {
    super(context);
  }

  /**
   * Create a new MySpace container.
   * 
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {

      String 
      	path = context.getParameter(MySpaceHandler.PARAM_TARGET_PATH),
    	name = context.getParameter(MySpaceHandler.PARAM_TARGET_NAME) ;
      
      System.out.println( "path: " + path ) ;
      System.out.println( "name: " + name ) ;

      if( (path != null && path.length() > 0)
          &&
          (name != null && name.length() > 0) ) {
          context.getMySpaceTree().newDirectory( name, path );
          
          // Take the opportunity to add to the open branches?...
      }
      else {
          String message = "Failed to create new directory.\n" +
                           "Name: " + name + "\n" +
                           "Path: " + path ;
          throw new MySpaceHandlerException( message );
      }
  }
  
}
