package org.astrogrid.portal.myspace.generation ;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.io.StringReader ;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.astrogrid.community.User;
// simport org.astrogrid.portal.login.common.SessionKeys;
import org.astrogrid.portal.myspace.acting.framework.ContextWrapper;
import org.astrogrid.portal.myspace.acting.framework.ContextWrapperFactory;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

/**
 * Generate <code>StoreClient</code> list of files as XML.
 * 
 * @author peter.shillan
 */
public class StorageFilesGenerator extends AbstractGenerator {
    
  private static final boolean TRACE_ENABLED = false ;
  private static final boolean DEBUG_ENABLED = false ;
  private static final String FILTER = "*";
  private static final String SEPARATOR = "/";
  private static final String MYSPACE_TREE = "myspace-tree";
  private static final String MYSPACE_ITEM = "myspace-item";
  private static final String MYSPACE_ENDPOINT_ATTR = "endpoint";
  private static final String MYSPACE_FILE_ATTR = "file";
  private static final String MYSPACE_FOLDER_ATTR = "folder";
  
  private ContextWrapper context;
  
  /**
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params)
  	  throws ProcessingException, SAXException, IOException {
    super.setup(resolver, objectModel, src, params);
    
    // Get environment.
    ActionUtils utils = ActionUtilsFactory.getActionUtils();
    Request request = ObjectModelHelper.getRequest(objectModel);
    Session session = request.getSession(true);

    // Create environmental context.
    context = ContextWrapperFactory.getContextWrapper(
        ContextWrapper.PARAM_PROTOCOL, utils, params, request, session);
  }

  /**
   * Generate <code>StoreClient</code> list of files into as XML.
   * 
   * @see org.apache.cocoon.generation.Generator#generate()
   */
  public void generate() throws IOException, SAXException, ProcessingException {
    if( TRACE_ENABLED) this.getLogger().debug( "enter: StorageFilesGenerator.generate()" );
        
    try {
        // Get the user's storage files.
        StoreFile userFile = getUserFile();
        
        if(userFile != null) {            
            StorageItemTree tree = new StorageItemTree( userFile ) ;
            XMLReader xmlReader = XMLReaderFactory.createXMLReader() ;
            xmlReader.setContentHandler( super.xmlConsumer ) ;
            InputSource source = new InputSource( new StringReader( tree.toXmlString() ) ) ;
            xmlReader.parse( source ) ;
        }
        else {
          this.getLogger().error("cannot find files for user");
          // throw new ProcessingException("cannot find files for user");
        }
        
    }
    catch( IOException iox ) {
        this.getLogger().error("cannot find files for user");
        // throw new ProcessingException("cannot find files for user");
    }
    finally {
        if( TRACE_ENABLED) this.getLogger().debug( "exit: StorageFilesGenerator.generate()" );
    }
    
  } // end of generate()
  
  
  /**
   * @return
   */
  private StoreFile getUserFile() throws IOException {
      if( TRACE_ENABLED) this.getLogger().debug( "enter: StorageFilesGenerator.getUserFile()" );
      
    StoreFile userFiles = null;
    String cacheRequest = null ;
    
    try{
        // Make sure we have a valid user.
        User user = context.getUser();
        if(user == null) {
          return null;
        }
        
        String userId = user.getUserId();
        if(userId == null || userId.length() == 0) {
          return null;
        }
        
        cacheRequest = context.getParameter( "myspace-refresh-cache" ) ;
        if( DEBUG_ENABLED) this.getLogger().debug( "cacheRequest: " + (cacheRequest==null ? "null" : "[" + cacheRequest.toString() + "]") );
        if( cacheRequest != null && cacheRequest.equalsIgnoreCase( "yes") ) {
            context.setMySpaceCache( null ) ;
        }
        
        userFiles = context.getMySpaceCache() ;
        if( DEBUG_ENABLED) this.getLogger().debug( "userFiles from cache: " + userFiles );
        if( userFiles != null ) {
            return userFiles ;
        }
        
        // Get list of user files.
        StoreClient storeClient = context.getStoreClient();
        if(storeClient == null) {
          return null;
        }
        
        StoreFile root = storeClient.getFiles(getUserFilter(userId));
        if(root == null) {
          return null;
        }
        
        StoreFile[] allUsers = root.listFiles();
        
        // Find files for the user we want.
        for(int userIndex = 0; userIndex < allUsers.length && userFiles == null; userIndex++) {
          if(userId.equals(allUsers[userIndex].getName())) {
            userFiles = allUsers[userIndex];
          } 
        }
        
        if( DEBUG_ENABLED) this.getLogger().debug( "userFiles into cache: " + userFiles );
        context.setMySpaceCache( userFiles ) ;
 
    }
    catch( Throwable t ) {
        t.printStackTrace() ;
    }
    finally {
        if( TRACE_ENABLED) this.getLogger().debug( "exit: StorageFilesGenerator.getUserFile()" );
    }
      
    return userFiles;
    
  }
  
  
  private String getUserFilter(String userId) {
    // return StorageFilesGenerator.SEPARATOR + userId + StorageFilesGenerator.SEPARATOR + StorageFilesGenerator.FILTER;
    return StorageFilesGenerator.SEPARATOR + userId + StorageFilesGenerator.FILTER;
  }

  
  private String getSafeName(String fileName) {
    StringBuffer result = new StringBuffer(32);
    
    char character = 0;
    for(int cIndex = 0; cIndex < fileName.length(); cIndex++) {
      character = fileName.charAt(cIndex);
      switch(character) {
        case '/':
        case '@':
        case ' ':
          result.append('_');
          break;

        default:
          result.append(character);
          break;
      }
    }
    
    return result.toString();
  }
  
  
  private String getFolderPath(String filePath, String fileName) {
    String result = "";
    
    if(filePath != null) {
      if(fileName != null) {
        result = filePath.substring(0, filePath.length() - fileName.length());
        if (result.endsWith("/")) {
          result = result.substring(0, result.length());
        }
      }
    }
    
    return result;
  }
  
  
  private abstract class StorageItem {
      
      protected StoreFile myStoreFile ;
 
      public StorageItem ( StoreFile storeFile ) {
          this.myStoreFile = storeFile ;
      }
      
      abstract public String toXmlString() throws ProcessingException;
    
    /**
     * @return Returns the storeFile.
     */
    public StoreFile getStoreFile() {
        return myStoreFile;
    }
    
    /**
     * @return Returns the type.
     */
    public abstract String getType() ;
    
  } // end of abstract class StorageItem
  
  
  private class FileItem extends StorageItem {
      
    public FileItem( StoreFile storeFile ) {
        super( storeFile ) ;
    }
    
    public String getType() {
        return StorageFilesGenerator.MYSPACE_FILE_ATTR ;
    }
      
    public String toXmlString() throws ProcessingException {
        if( TRACE_ENABLED) getLogger().debug( "entry: FileItem.toXmlString()" );
        StringBuffer buffer = new StringBuffer(128) ;

        String fileName = myStoreFile.getName();
        String filePath = myStoreFile.getPath();
        String safeName = getSafeName(filePath);
        
        buffer
        	.append( "<" )
        	.append( StorageFilesGenerator.MYSPACE_ITEM )
        	.append( " type=\"" )
        	.append( StorageFilesGenerator.MYSPACE_FILE_ATTR )
        	.append( "\" ")
        	.append( " safe-name=\"" )
        	.append( safeName )
        	.append( "\" ")
        	.append( " full-name=\"" )
        	.append( filePath )
        	.append( "\" ")
        	.append( " item-name=\"" )
        	.append( fileName )
        	.append( "\" ")
        	.append( " id=\"" )
        	.append( safeName )
        	.append( "\" ") ;
                     
        try {
          if( DEBUG_ENABLED) getLogger().debug("context.getStoreClient().getAgsl(filePath) start");
          Agsl agsl = context.getStoreClient().getAgsl(filePath);
          if( DEBUG_ENABLED) getLogger().debug("context.getStoreClient().getAgsl(filePath) end");
          // URL url = agsl.resolveURL();
          
//          String protocol = url.getProtocol(); 
//          String host = url.getHost();
//          int port = url.getPort();
//          String path = url.getPath();
//          String file = url.getFile();
  	      
          buffer
      		.append( " url=\"" )
//      		.append( agsl.resolveURL().toString() )
      		.append( "duff url" )
      		.append( "\" ")
      		.append( " ivorn=\"" )
      		.append( agsl.toIvorn(context.getUser()).toString() )
      		.append( "\" ")
      		.append( " folder-path=\"" )
      		.append( getFolderPath(filePath, fileName) )
      		.append( "\" ")
      		.append( ">" ) ;
  
// These are yet to come ...
//  	      itemElement.setAttribute("created", storeFile.getCreated().toString());
//  	      itemElement.setAttribute("mime-type", storeFile.getMimeType());
//  	      itemElement.setAttribute("modified", storeFile.getModified().toString());
//  	      itemElement.setAttribute("owner", storeFile.getOwner());
//  	      itemElement.setAttribute("size", Long.toString(storeFile.getSize()));
        }
  	    catch(IOException e) {
  	       throw new ProcessingException("could not resolve AstroGrid URL for [" + fileName + "]", e);
  	    }
              
        buffer
        	.append( "</" )
        	.append( StorageFilesGenerator.MYSPACE_ITEM )
        	.append( ">" ) ;
        
        if( TRACE_ENABLED) getLogger().debug( "exit: FileItem.toXmlString()" );
        return buffer.toString() ;
    }
      
  } // end of class FileItem
  
  
  private class FolderItem extends StorageItem {
      
    public FolderItem( StoreFile storeFile ) {
        super( storeFile ) ;
    }
    
    public String getType() {
        return StorageFilesGenerator.MYSPACE_FOLDER_ATTR ;
    }
      
    public String toXmlString() throws ProcessingException {
        if( TRACE_ENABLED) getLogger().debug( "entry: FolderItem.toXmlString()" );
        StringBuffer buffer = new StringBuffer(128) ;

        String fileName = myStoreFile.getName();
        String filePath = myStoreFile.getPath();
        String safeName = getSafeName(filePath);
        
        buffer
        	.append( "<" )
        	.append( StorageFilesGenerator.MYSPACE_ITEM )
        	.append( " type=\"" )
        	.append( StorageFilesGenerator.MYSPACE_FOLDER_ATTR )
        	.append( "\" ")
        	.append( " safe-name=\"" )
        	.append( safeName )
        	.append( "\" ")
        	.append( " full-name=\"" )
        	.append( filePath )
        	.append( "\" ")
        	.append( " item-name=\"" )
        	.append( fileName )
        	.append( "\" ")
        	.append( " id=\"" )
        	.append( safeName )
        	.append( "\" ") 
        	.append( " folder-path=\"" )
        	.append( filePath )
        	.append( "\" ")
        	.append( ">" ) ;
        
    	StoreFile[] storeFiles = myStoreFile.listFiles();
        
    	for(int fileIndex = 0; fileIndex < storeFiles.length; fileIndex++) {
      	    if( storeFiles[fileIndex].isFile() ){
      	       buffer.append( new FileItem( storeFiles[fileIndex] ).toXmlString() ) ;
      	    }
      	    else if( storeFiles[fileIndex].isFolder() ){
      	       buffer.append( new FolderItem( storeFiles[fileIndex] ).toXmlString() ) ;
      	    }
      	}
              
        buffer
        	.append( "</" )
        	.append( StorageFilesGenerator.MYSPACE_ITEM )
        	.append( ">" ) ;
        
        if( TRACE_ENABLED) getLogger().debug( "exit: FolderItem.toXmlString()" );
        return buffer.toString() ;
    }
      
  } // end of class FolderItem
  
  private class StorageItemTree {
      
     private StoreFile root ;
     private StorageItem[] topLevel ;
      
     public StorageItemTree( StoreFile storeFile ) {
         this.root = storeFile ;
         this.topLevel = new StorageItem[1] ;
         this.topLevel[0] = new FolderItem( storeFile ) ;
         
     	 // StoreFile[] storeFiles = storeFile.listFiles();
     	 // topLevel = new StorageItem[ storeFiles.length ] ;
     	 
//     	 for(int fileIndex = 0; fileIndex < storeFiles.length; fileIndex++) {
//     	    if( storeFiles[fileIndex].isFile() ){
//     	       topLevel[fileIndex] = new FileItem( storeFiles[fileIndex] ) ;
//     	    }
//     	    else if( storeFiles[fileIndex].isFolder() ){
//     	       topLevel[fileIndex] = new FolderItem( storeFiles[fileIndex] ) ;
//     	    }
//     	 }
         
     } // end StorageItemTree()
     
     
     public String toXmlString() throws IOException, ProcessingException {
         
         StringBuffer buffer = new StringBuffer( 4096 ) ;
         String returnValue ;
         
         buffer
         	.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" )
         	.append( "<" )
         	.append( StorageFilesGenerator.MYSPACE_TREE )
         	.append( ' ' )
         	.append( StorageFilesGenerator.MYSPACE_ENDPOINT_ATTR )
         	.append( "=\"" )
         	.append( context.getAgsl().toIvorn(context.getUser()).toString() ) 
         	.append( "\" >" ) ;
         
         for(int fileIndex = 0; fileIndex < topLevel.length; fileIndex++) {
             if( topLevel[fileIndex] != null )
                 buffer.append( topLevel[fileIndex].toXmlString() ) ;
      	 }
         
         buffer
         	.append( "</" )
         	.append( StorageFilesGenerator.MYSPACE_TREE )
         	.append( ">" ) ;
         
         returnValue = buffer.toString() ;
         if( DEBUG_ENABLED) getLogger().debug( "MySpaceTree...\n" + returnValue );
         
         return buffer.toString() ;
         
     } // end toXmlString()
     
  } // end class StorageItemTree
  
} // end of class StorageFilesGenerator
