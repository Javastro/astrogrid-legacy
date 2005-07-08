package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;
import org.astrogrid.portal.myspace.filesystem.* ;
import org.astrogrid.store.Ivorn;

/**
 * Relocate a file from one filestore to another.
 * 
 * @author jeff.lusted
 */
public class RelocateFileHandler extends AbstractMySpaceHandler {
    
    private static final boolean TRACE_ENABLED = true ;
    private static final boolean DEBUG_ENABLED = true ;
    
    
  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public RelocateFileHandler(ContextWrapper context) {
    super(context);
  }

  /**
   * Physically relocate a file from one filestore to another
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {
    if( TRACE_ENABLED) trace( "entry: RelocateFileHandler.executeTemplateMethod()" );
    
    String 
    	path = context.getParameter(MySpaceHandler.PARAM_SOURCE_PATH),
    	fileStore = context.getParameter( MySpaceHandler.PARAM_FILESTORE ) ; 
    
    debug( "path: " + path ) ;
    debug( "fileStore: " + fileStore ) ;
    
    Tree tree = null ;
    File file = null ;
    
    try {
        if( path != null && path.length() > 0 ) {
            
           tree = context.getMySpaceTree() ;
           file = tree.getFile( path ) ;
           if( file != null ) {
               file.getNode().move( null, null, new Ivorn( Ivorn.SCHEME + "://" + fileStore) ) ;
           }
           
        }
    }
    catch( Exception ex ) {
        log( "Failure in relocating file to different filestore..." ) ;
        log( "file path: " + path ) ;
        log( "filestore: " + fileStore ) ;
        log( "user: " + context.getParameter( "user" ) ) ;
        log( ex.toString() ) ;
    }
    finally {
        if( TRACE_ENABLED) trace( "exit: RelocateFileHandler.executeTemplateMethod()" );
    }
 
  }
  
  private void trace( String traceString ) {
      //this.getLogger().debug( traceString );
      System.out.println( traceString );
  }
    
  private void debug( String logString ){
      //this.getLogger().debug( logString );
      System.out.println( logString );
  }  
  
  private void log( String logString ){
      //this.getLogger().debug( logString );
      System.out.println( logString );
  } 
  
}
