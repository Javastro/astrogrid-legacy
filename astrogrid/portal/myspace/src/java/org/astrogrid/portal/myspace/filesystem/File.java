/*
 * Created on 25-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.myspace.filesystem;

import java.io.IOException;
import java.io.Serializable;

import org.apache.cocoon.ProcessingException;
import org.astrogrid.portal.myspace.generation.StorageFilesGenerator;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.filemanager.client.FileManagerDelegate ;
import org.astrogrid.filemanager.resolver.FileManagerDelegateResolverImpl ;
import org.astrogrid.filemanager.client.FileManagerNode ;
import org.astrogrid.filemanager.common.exception.*;
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
    
    
    public File ( FileManagerNode directoryNode, Directory parent ){
        super( directoryNode, parent ) ;
    }
    
    
    public StringBuffer toXmlString( StringBuffer buffer ) {
        if( TRACE_ENABLED) logger.debug( "entry: File.toXmlString(StringBuffer)" );
        String fileName = getMyNode().name() ;
        String filePath = path().toString() ;        
        String safeName = Item.getSafeName(filePath);
        
        buffer
    		.append( "<" )
    		.append( Tree.MYSPACE_ITEM )
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
            	.append( getMyNode().ivorn() )
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
	    catch( FileManagerIdentifierException e) {
	        logger.error( "File.toXmlString() encountered invalid Ivorn\n", e) ;
	    }
          
	    buffer
    		.append( "</" )
    		.append( Tree.MYSPACE_ITEM )
    		.append( ">" ) ;
    
        if( TRACE_ENABLED) logger.debug( "exit: File.toXmlString(StringBuffer)" );
	    return buffer ;
	    
    } // end of toXmlString( StringBuffer ) 
    
} // end of class File
