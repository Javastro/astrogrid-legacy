package org.astrogrid.portal.myspace.acting.framework;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.astrogrid.portal.myspace.filesystem.*;


/**
 * Upload a file into MySpace.
 * 
 * @author peter.shillan
 */
public class UploadHandler extends AbstractMySpaceHandler {

  /**
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#AbstractMySpaceHandler(org.astrogrid.portal.myspace.acting.framework.ContextWrapper)
   * @param context
   */
  public UploadHandler(ContextWrapper context) {
    super(context);
  }

  /**
   * Upload a file into MySpace.
   * 
   * @see org.astrogrid.portal.myspace.acting.framework.AbstractMySpaceHandler#executeTemplateMethod(java.util.Map)
   */
  protected void executeTemplateMethod(Map results) throws Throwable {
    System.out.println( "enter: UploadHandler.executeTemplateMethod()" ) ;
    String 
    	oldFileName = context.getParameter(MySpaceHandler.PARAM_SOURCE_NAME),
    	newFileName = context.getParameter(MySpaceHandler.PARAM_TARGET_NAME),
    	path = context.getParameter(MySpaceHandler.PARAM_TARGET_PATH),
        localFile = context.getParameter(MySpaceHandler.PARAM_LOCAL_FILE) ;
    File file = null ;
    
    System.out.println( "oldFileName: " + oldFileName ) ;
    System.out.println( "newFileName: " + newFileName ) ;
    System.out.println( "path: " + path ) ;
    System.out.println( "localFile points to: " + localFile ) ;
       
    if( ( oldFileName != null && oldFileName.length() > 0 )
         &&
        ( newFileName != null && newFileName.length() > 0 ) 
         &&
        ( path != null && path.length() > 0 ) 
        &&
        ( localFile != null && localFile.length() > 0 ) ) {
      
        InputStream fileInputStream = null;
	    OutputStream outputStream = null;
	    Tree tree = null ;

	    try {
	        fileInputStream = context.getFileInputStream( MySpaceHandler.PARAM_LOCAL_FILE );
	        if( fileInputStream == null )
	            System.out.println( "fileInputStream: is null" ) ;
	        tree = context.getMySpaceTree() ;
	        file = tree.getFile( path + newFileName );
	        if( file == null ) {
	            file = tree.newFile( newFileName, path ) ;
	        }
		    outputStream = file.getNode().writeContent() ;
		    
		    // Copy the input stream to the output stream.
		    byte[] bytesRead = null;
		    int bytesAvailable = fileInputStream.available();
		    while( bytesAvailable > 0 ) {
		      bytesRead = new byte[bytesAvailable];
		      fileInputStream.read(bytesRead);
		      outputStream.write(bytesRead);
		      
		      bytesAvailable = fileInputStream.available();
		    }
	    }
	    catch(Exception e) {
	      throw new MySpaceHandlerException("Error uploading file:\n" +
	                                        "oldFileName: " + oldFileName + "\n" +
	                                        "newFileName: " + newFileName + "\n" +
	                                        "Path: " + path , e) ;
	    }
	    finally {
	      try {
	        if(fileInputStream != null) {
		        fileInputStream.close();
		      }
	      }
	      catch(Throwable t){
	        // assume closure.
	      }

	      try {
	        if(outputStream != null) {
	          outputStream.close();
	          file.getNode().transferCompleted() ;
		      }
	      }
	      catch(Throwable t){
	        // assume closure.
	      }
	    }
    }
    else {
      throw new MySpaceHandlerException( "Error uploading file:\n" +
                                         "oldFileName: " + oldFileName + "\n" +
                                         "newFileName: " + newFileName + "\n" +
                                         "Path: " + path );
    }
  }
}
