/*
 * Created on 25-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.myspace.filesystem;

//import java.io.IOException;
//import java.io.Serializable;
//import java.util.Date;
//import java.net.URL;

import java.util.Map; 

//import org.apache.cocoon.ProcessingException;
//import org.astrogrid.portal.myspace.generation.StorageFilesGenerator;
//import org.astrogrid.store.Agsl;
//import org.astrogrid.store.Ivorn;
//import org.astrogrid.store.delegate.StoreFile;
// import org.astrogrid.filemanager.client.FileManagerDelegate ;

import org.astrogrid.filemanager.client.FileManagerNode ;
import org.astrogrid.filemanager.client.NodeIterator;
import org.astrogrid.filemanager.client.NodeMetadata;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.apache.log4j.Logger;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class File extends Item {
     
    private static final boolean TRACE_ENABLED = true ;
    private static final boolean DEBUG_ENABLED = true ;
    private static Logger logger = Logger.getLogger( File.class ) ;
    
    public File ( FileManagerNode node, Directory parent ){
        super( node, parent ) ;
    }
  
    
    public StringBuffer toTreeViewXmlString( StringBuffer buffer, String[] openBranches ) {
        if( TRACE_ENABLED) logger.debug( "entry: File.toTreeViewXmlString(StringBuffer)" );
        String fileName = getNode().getName() ;
        String filePath = path().toString() ;        
        String safeName = Item.getSafeName(filePath);
        
        buffer
    		.append( "<" )
    		.append( Tree.ITEM_ELEMENT )
    		.append( " type=\"" )
    		.append( Tree.MYSPACE_FILE_ATTR )
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
	      
            buffer
            	.append( " ivorn=\"" )
            	.append( getNode().getIvorn() )
            	.append( "\" ")
            	.append( " folder-path=\"" )
            	.append( getFolderPath(filePath, fileName) )
            	.append( "\" ")
            	.append( ">" ) ;

//These are yet to come ...
//	      itemElement.setAttribute("created", storeFile.getCreated().toString());
//	      itemElement.setAttribute("mime-type", storeFile.getMimeType());
//	      itemElement.setAttribute("modified", storeFile.getModified().toString());
//	      itemElement.setAttribute("owner", storeFile.getOwner());
//	      itemElement.setAttribute("size", Long.toString(storeFile.getSize()));
        }
	    catch( Exception ex ) {
	        logger.error( "File.toTreeXmlString() encountered invalid Ivorn\n", ex ) ;
	    }
          
	    buffer
    		.append( "</" )
    		.append( Tree.ITEM_ELEMENT )
    		.append( ">" ) ;
    
        if( TRACE_ENABLED) logger.debug( "exit: File.toTreeViewXmlString(StringBuffer)" );
	    return buffer ;
	    
    } // end of toTreeViewXmlString( StringBuffer ) 
    
    
    public String toDirectoryViewXmlString() {
        return null ;
    }
    
    public StringBuffer toLineViewXmlString( StringBuffer buffer ) {
        if( TRACE_ENABLED) logger.debug( "entry: File.toLineViewXmlString()" );
        if( buffer == null ) {
            buffer = new StringBuffer() ;
        }
        
        FileManagerNode node = getNode() ;
        NodeMetadata metadata = node.getMetadata() ;
        Map attributesMap = metadata.getAttributes() ;
        String mimetype = (String)attributesMap.get( NodeMetadata.MIME_TYPE ) ;
        String name = node.getName() ;
        String filePath = path().toString() ;        
        String safeName = Item.getSafeName(filePath);
        
        buffer
        	.append( "<" )
        	.append( Tree.ITEM_ELEMENT )
        	.append( " type=\"" ).append( Tree.MYSPACE_FILE_ATTR ).append( "\" ")
        	.append( " safe-name=\"" ).append( safeName ).append( "\" ")
        	.append( " full-name=\"" ).append( filePath ).append( "\" ")
        	.append( " item-name=\"" ).append( name ).append( "\" ")
        	.append( " id=\"" ).append( safeName ).append( "\" ") 
        	.append( " folder-path=\"" ).append( filePath ).append( "\" ")
        	
        	
        	.append( Tree.NAME_ATTR ).append( "=\"" ).append( name ).append( "\" " )
        	.append( Tree.SIZE_ATTR ).append( "=\"" ).append( metadata.getSize() ).append( "\" " )
        	.append( Tree.CREATED_ATTR  ).append( "=\"" ).append( metadata.getCreateDate().getTime().toString() ).append( "\" " )
        	.append( Tree.MODIFIED_ATTR  ).append( "=\"" ).append( metadata.getModifyDate().getTime().toString() ).append( "\" " ) ;
        
        if( mimetype != null && mimetype.length() > 0 )	
            buffer.append( Tree.MIME_TYPE_ATTR  ).append( "=\"" ).append( mimetype ).append( "\" " ) ;
        	
        buffer
            .append( ">" ) 
        	.append( "</" )
        	.append( Tree.ITEM_ELEMENT )
        	.append( ">" ) ;
        
        if( TRACE_ENABLED) logger.debug( "exit: File.toLineViewXmlString()" );
        return buffer ;
    }
    
    
    public String toDetailsViewXmlString() {
        if( TRACE_ENABLED ) logger.debug( "entry: File.toDetailsViewXmlString()") ;
        
        StringBuffer buffer ;
        
        try {
            // More intelligent would be a calculation of buffer size required...
            buffer = new StringBuffer( 512 ) ;
            FileManagerNode node = getNode() ;
            NodeMetadata metadata = node.getMetadata() ;
            Map attributesMap = metadata.getAttributes() ;
            String mimetype = (String)attributesMap.get( NodeMetadata.MIME_TYPE ) ;
            String urlString = null ;
            String contentLocationString = null ;
            
            // This is an expensive call...
            try { urlString = node.contentURL().toString() ; } 
            catch( Exception e ) { urlString = ""; }
            
            // This may also be an expensive call...
            try {
                contentLocationString = metadata.getContentLocation().toString() ;
            }
            catch( Exception e ) {
                contentLocationString = "" ;
            }
            
            buffer
         		.append( Tree.XML_HEADER )
         		.append( "<" )
         		.append( Tree.PROPERTIES_ELEMENT )
         		.append( ' ' )
         		.append( Tree.NAME_ATTR ).append( "=\"" ).append( node.getName() ).append( "\" " )
         		.append( Tree.PATH_ATTR ).append( "=\"" ).append( this.path() ).append( "\" " )
         		.append( Tree.SIZE_ATTR ).append( "=\"" ).append( metadata.getSize() ).append( "\" " )
         		.append( Tree.OWNER_ATTR ).append( "=\"" ).append( this.getTree().getAccountSpace().getPath() ).append( "\" " )
         		.append( Tree.CREATED_ATTR ).append( "=\"" ).append( metadata.getCreateDate().getTime().toString() ).append( "\" " )
         		.append( Tree.MODIFIED_ATTR ).append( "=\"" ).append( metadata.getModifyDate().getTime().toString() ).append( "\" " ) ;
//         		.append( Tree.NODE_CREATED_ATTR ).append( "=\"" ).append( node.getNodeCreateDate() ).append( "\" " )
//         		.append( Tree.NODE_MODIFIED_ATTR ).append( "=\"" ).append( node.getNodeModifyDate() ).append( "\" " )  
//         		.append( Tree.FILE_CREATED_ATTR ).append( "=\"" ).append( node.getFileCreateDate() ).append( "\" " )
//         		.append( Tree.FILE_MODIFIED_ATTR ).append( "=\"" ).append( node.getFileModifyDate() ).append( "\" " )
            
            if( mimetype != null && mimetype.length() > 0 ) 	
                buffer.append( Tree.MIME_TYPE_ATTR  ).append( "=\"" ).append( mimetype ).append( "\" " ) ;
            
         	buffer
         		.append( Tree.URL_ATTR ).append( "=\"" ).append( urlString ).append( "\" " )
         		.append( Tree.CONTENT_LOCATION ).append( "=\"" ).append( contentLocationString ).append( "\" " )
         		.append( "/>" ) ;
            
            logger.debug( "Buffer size is: " + buffer.length() ) ;
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "exit: File.toDetailsViewXmlString()") ;
        }
        
        return buffer.toString() ;
    }
    
} // end of class File
