package org.astrogrid.portal.myspace.acting.framework;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

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
    String dest = context.getParameter(MySpaceHandler.PARAM_DEST);
    String fileName = context.getParameter(MySpaceHandler.PARAM_FILE);
    
    if(dest != null && dest.length() > 0 &&
        fileName != null && fileName.length() > 0) {
      
      InputStream fileInputStream = null;
	    OutputStream outputStream = null;

	    try {
	      fileInputStream = context.getFileInputStream(MySpaceHandler.PARAM_FILE);
		    outputStream = context.getStoreClient().putStream(dest, false);
		    
		    // Copy the input stream to the output stream.
		    byte[] bytesRead = null;
		    int bytesAvailable = fileInputStream.available();
		    while(bytesAvailable > 0) {
		      bytesRead = new byte[bytesAvailable];
		      fileInputStream.read(bytesRead);
		      outputStream.write(bytesRead);
		      
		      bytesAvailable = fileInputStream.available();
		    }
	    }
	    catch(Exception e) {
	      throw new MySpaceHandlerException("error uploading file", e);
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
		      }
	      }
	      catch(Throwable t){
	        // assume closure.
	      }
	    }
    }
    else {
      throw new MySpaceHandlerException("invalid destination or file name");
    }
  }
}
