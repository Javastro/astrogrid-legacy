/*
 * Created on 25-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.myspace.filesystem;

import java.io.Serializable;

//import org.astrogrid.portal.myspace.generation.StorageFilesGenerator;
//import org.astrogrid.portal.myspace.generation.StorageFilesGenerator.FileItem;
//import org.astrogrid.portal.myspace.generation.StorageFilesGenerator.FolderItem;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.filemanager.client.FileManagerDelegate ;
import org.astrogrid.filemanager.resolver.FileManagerDelegateResolverImpl ;
import org.astrogrid.filemanager.client.FileManagerNode ;
import org.astrogrid.filemanager.common.exception.*;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Directory extends Item {
    
    private static final boolean TRACE_ENABLED = true ;
    private static final boolean DEBUG_ENABLED = true ;
    private static Logger logger = Logger.getLogger( Directory.class ) ;
    
    private boolean valid ;
    private Exception exception ;
    private ArrayList myChildren ;
    
    
    public Directory( FileManagerNode directoryNode, Directory parent ){
        super( directoryNode, parent ) ;
        if( TRACE_ENABLED) logger.debug( "entry: Directory(FileManagerNode,Directory)" );
        try {
            Tree tree = this.getTree() ;
            long size = directoryNode.size() ;
            FileManagerNode.NodeIterator iterator ;
            FileManagerNode node ;
            if( size > 0 ) {
                myChildren = new ArrayList( (int)size ) ;
                iterator = directoryNode.iterator() ;
                while( iterator.hasNext() ) {
                    node = iterator.next() ;
                    if( node.isContainer() ) {
                        this.myChildren.add( tree.newDirectory( node, this ) ) ;
                    }
                    else if( node.isFile() ) {
                        this.myChildren.add( tree.newFile( node, this ) ) ;
                    }
                    else {
                        ;
                    }
                    
                } // end while
                
            } // end if
            
            this.valid = true ;
        }
        catch( Exception e ) {
            this.valid = false ;
            this.exception = e ;
            logger.error( "Directory marked as invalid.\n" ,e) ;
        }
        finally {
            if( TRACE_ENABLED) logger.debug( "exit: Directory(FileManagerNode,Directory)" ); 
        }
     
    } // end of Directory( FileManagerNode )
    
    
    public StringBuffer toXmlString( StringBuffer buffer ) {
        if( TRACE_ENABLED) logger.debug( "entry: Directory.toXmlString()" );
        if( buffer == null ) {
            buffer = new StringBuffer() ;
        }
        
        String fileName = getMyNode().name() ;
        String filePath = path().toString() ;        
        String safeName = Item.getSafeName(filePath);
        
        buffer
        	.append( "<" )
        	.append( Tree.MYSPACE_ITEM )
        	.append( " type=\"" )
        	.append( Tree.MYSPACE_FOLDER_ATTR )
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
        
        if( this.isEmpty() == false ) {
            
            Iterator iterator = myChildren.iterator() ;
            Item item ;        
            while( iterator.hasNext() ) {
                item = (Item)iterator.next() ;
                item.toXmlString( buffer ) ;
            }
            
        } // end if
              
        buffer
        	.append( "</" )
        	.append( Tree.MYSPACE_ITEM )
        	.append( ">" ) ;
        
        if( TRACE_ENABLED) logger.debug( "exit: Directory.toXmlString()" );
        return buffer ;
            
    } // end of toXmlString( StringBuffer )
    
    
    public boolean isEmpty() {
        return ( this.myChildren == null ? true : this.myChildren.isEmpty() ) ;
    }

} // end of class Directory
