package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

// import org.astrogrid.store.Agsl;
import org.astrogrid.portal.myspace.filesystem.* ;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Load a directory entry with subdirectories and files.
 * 
 * @author jeff.lusted
 */
public class LoadBranchHandler extends AbstractMySpaceHandler {
  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public LoadBranchHandler(ContextWrapper context) {
    super(context);
  }
   
  /**
   * Load a directory.
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {
    String 
    	path = context.getParameter(MySpaceHandler.PARAM_TREE_VIEW_PATH),
    	sOpenBranches = context.getParameter( MySpaceHandler.PARAM_TREE_OPEN_BRANCHES ) ; 
    
    System.out.println( "sOpenBranches: " + sOpenBranches ) ;
    
    Tree tree = null ;
    Directory directory = null ;
    
    if( path != null && path.length() > 0 ) {
        
       // First of all, load the branch from MySpace...
        
       tree = context.getMySpaceTree() ;
       directory = tree.getDirectory( path ) ;
       if( directory == null ) {
           throw new MySpaceHandlerException("Directory not found:\n" + path );
       }
       
       // Jeff. This needs thinking about.
       // If the directory has already been filled, don't do it again.
       // This is possible considering multiple windows.
       // What I'm uncertain about is when a branch gets out of date and needs
       // rebuilding.
       if( directory.isFilled() == false ) {
           tree.constructBranch( directory ) ;
       }
       
       // Second, if we are passed an open branches string, we parse it
       // and save the results in the session object. This is then used
       // whenever we need to return to the Explorer from another page
       // or simply to refresh the branch to the state displayed in front 
       // of the user...
       //
       // NB. I think I can get away with an array of string paths to the open
       // directories. I mean as a session object. This will not be updated, 
       // simply inserted, deleted, replaced. So I believe I can get away
       // without ensuring that the array is thread safe. Experimental.
       // Otherwise it will have to be a synchronized ArrayList.
       
       // The test for null is neuroticism
       if( sOpenBranches != null ) {
           // This concatenates the path of the newly loaded directory.
           // Otherwise contained directories will not be shown.
           sOpenBranches += "*" + path ;
           String [] branches = sOpenBranches.split( "[*]" ) ;
           System.out.println( "sOpenBranches.split() returned: " + branches ) ;
           if( branches != null && branches.length > 0 ) {
               context.setMySpaceTreeOpenBranches( branches ) ;
               System.out.println( "context.getMySpaceTreeOpenBranches(): " + context.getMySpaceTreeOpenBranches() ) ;
           }  
           
       }
 
    }
    else {
        throw new MySpaceHandlerException("Invalid tree view path:\n" + path );
    }
  }
}
