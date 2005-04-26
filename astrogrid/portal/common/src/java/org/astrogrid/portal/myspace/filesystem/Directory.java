/*
 * Created on 25-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.myspace.filesystem;

//import java.io.Serializable;

//import org.astrogrid.portal.myspace.generation.StorageFilesGenerator;
//import org.astrogrid.portal.myspace.generation.StorageFilesGenerator.FileItem;
//import org.astrogrid.portal.myspace.generation.StorageFilesGenerator.FolderItem;
//import org.astrogrid.store.Ivorn;
// import org.astrogrid.store.delegate.StoreFile;
// import org.astrogrid.filemanager.client.FileManagerDelegate ;
// import org.astrogrid.filemanager.resolver.FileManagerDelegateResolverImpl ;

import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.NodeIterator;
import org.astrogrid.filemanager.client.NodeMetadata;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.FileManagerFault;


import java.util.ArrayList;
import java.util.List;
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
    private boolean filled ;
    private Exception exception ;
    private List children ;
    
    protected Directory( FileManagerNode node, Directory parent ){
      super( node, parent ) ;
      
      this.filled = false ;
      this.valid = true ;
      this.exception = null ;
      this.children = new ArrayList( 32 ) ;   
    }
    
    protected void resynchWithSystem () {
        if( TRACE_ENABLED) logger.debug( "entry: Directory.resynchWithSystem()" );
        
        // Safety first. If it has never been filled, this is the wrong call...
        if( this.isFilled() == false )
            return ;
        
        Tree tree = this.getTree() ;
        
        try {          
            this.getNode().refresh() ;
            Iterator iterator = this.children.iterator() ;
            Object obj = null ;
            File file = null ;
            
            // empty directory wrapper of files...
            while( iterator.hasNext() ) {
                obj = iterator.next() ;
                if( obj instanceof File ) {
                    tree.destructFile( (File)obj ) ;
                    iterator.remove() ;
                }
            }
            iterator = null ;
            
            NodeIterator nodeIterator = this.getNode().iterator() ;
            FileManagerNode childNode ;
            while( nodeIterator.hasNext() ) {
                childNode = (FileManagerNode)nodeIterator.next() ;
                if( childNode.isFile() ) {
                    tree.constructFile( childNode, this ) ;
                }
                else {
                    ; // ?
                }                   
            } // end while
            
        }
        catch( Exception ex ) {
            logger.error( "Resynchronize with system failed.", ex ) ;
        }       
        finally {
            if( TRACE_ENABLED) logger.debug( "exit: Directory.resynchWithSystem()" );
        }
        
    }
    
//    protected Directory( FileManagerNode directoryNode, Directory parent ){
//        super( directoryNode, parent ) ;
//        if( TRACE_ENABLED) logger.debug( "entry: Directory(FileManagerNode,Directory)" );
//        try {
//            Tree tree = this.getTree() ;
//            long size = directoryNode.size() ;
//            FileManagerNode.NodeIterator iterator ;
//            FileManagerNode node ;
//            if( size > 0 ) {
//                children = new ArrayList( (int)size ) ;
//                iterator = directoryNode.iterator() ;
//                while( iterator.hasNext() ) {
//                    node = iterator.next() ;
//                    if( node.isContainer() ) {
//                        this.children.add( tree.newDirectory( node, this ) ) ;
//                    }
//                    else if( node.isFile() ) {
//                        this.children.add( tree.newFile( node, this ) ) ;
//                    }
//                    else {
//                        ;
//                    }
//                    
//                } // end while
//                
//            } // end if
//            
//            this.valid = true ;
//        }
//        catch( Exception e ) {
//            this.valid = false ;
//            this.exception = e ;
//            logger.error( "Directory marked as invalid.\n" ,e) ;
//        }
//        finally {
//            if( TRACE_ENABLED) logger.debug( "exit: Directory(FileManagerNode,Directory)" ); 
//        }
//     
//    } // end of Directory( FileManagerNode )
    
    
    public StringBuffer toTreeViewXmlString( StringBuffer buffer, String[] openBranches ) {
        if( TRACE_ENABLED) logger.debug( "entry: Directory.toTreeXmlString()" );
        if( buffer == null ) {
            buffer = new StringBuffer() ;
        }
        
        String fileName = getNode().getName() ;
        String filePath = path().toString() ;        
        String safeName = Item.getSafeName(filePath);
        boolean openStatus = false ;
        
        // Check to see whether this directory is one of the ones
        // that is currently shown as open ...
        if( openBranches != null ) {
            for( int i=0; i<openBranches.length; i++ ) {
                if( openBranches[i] != null && filePath.equals( openBranches[i] ) ) {
                    openStatus = true ;
                    break ;
                }
            }
        }
        
        buffer
        	.append( "<" )
        	.append( Tree.ITEM_ELEMENT )
        	.append( " type=\"" ).append( Tree.MYSPACE_FOLDER_ATTR ).append( "\" " )
        	.append( " safe-name=\"" ).append( safeName ).append( "\" " )
        	.append( " full-name=\"" ).append( filePath ).append( "\" " )
        	.append( " item-name=\"" ).append( fileName ).append( "\" " )
        	.append( " id=\"" ).append( safeName ).append( "\" " ) 
        	.append( " folder-path=\"" ).append( filePath ).append( "\" " )
        	.append( " filled=\"" ).append( this.isFilled() ).append( "\" " )
        	.append( " valid=\"" ).append( this.isValid() ).append( "\" " )
        	.append( " empty=\"" ).append( this.isEmpty() ).append( "\" " )
        	.append( " open=\"" ).append( openStatus ).append( "\" " )
        	.append( ">" ) ;
        
        if( this.isEmpty() == false ) {
            
            Iterator iterator = children.iterator() ;
            Item item ;        
            while( iterator.hasNext() ) {
                item = (Item)iterator.next() ;
                if( item instanceof Directory ) {
                    item.toTreeViewXmlString( buffer, openBranches ) ;
                }
            }
            
        } // end if
              
        buffer
        	.append( "</" )
        	.append( Tree.ITEM_ELEMENT )
        	.append( ">" ) ;
        
        if( TRACE_ENABLED) logger.debug( "exit: Directory.toTreeXmlString()" );
        return buffer ;
            
    } // end of toTreeViewXmlString( StringBuffer )
    
    
    public String toDirectoryViewXmlString() {
        return this.toDirectoryViewXmlString(null).toString() ;
    }
    
    
    public StringBuffer toDirectoryViewXmlString( StringBuffer buffer ) {
        if( TRACE_ENABLED) logger.debug( "entry: Directory.toDirectoryViewXmlString()" );
        if( buffer == null ) {
            buffer = new StringBuffer(512) ;
        }
        
        String fileName = getNode().getName() ;
        String filePath = path().toString() ;        
        String safeName = Item.getSafeName(filePath);
        
        buffer
        	.append( Tree.XML_HEADER )
        	.append( "<" )
        	.append( Tree.DIRECTORY_ELEMENT )
        	.append( " type=\"" ).append( Tree.MYSPACE_FOLDER_ATTR ).append( "\" ")
        	.append( " safe-name=\"" ).append( safeName ).append( "\" ")
        	.append( " full-name=\"" ).append( filePath ).append( "\" ")
        	.append( " item-name=\"" ).append( fileName ).append( "\" ")
        	.append( " id=\"" ).append( safeName ).append( "\" ") 
        	.append( " folder-path=\"" ).append( filePath ).append( "\" ")
        	.append( ">" ) ;
        
        if( this.isEmpty() == false ) {
            
            Iterator iterator = children.iterator() ;
            Item item ;        
            while( iterator.hasNext() ) {
                item = (Item)iterator.next() ;
                item.toLineViewXmlString( buffer ) ;
            }
            
        } // end if
              
        buffer
        	.append( "</" )
        	.append( Tree.DIRECTORY_ELEMENT )
        	.append( ">" ) ;
        
        if( DEBUG_ENABLED ) {
            logger.debug( "Buffer size is: " + buffer.length() + 
                          "\nNumber of children in directory is: " + children.size() ) ;
            logger.debug( buffer.toString() ) ;
        }
        
        if( TRACE_ENABLED) logger.debug( "exit: Directory.toDirectoryViewXmlString()" );
        return buffer ;
        
    } // end of toDirectoryViewXmlString()
    
    
    public StringBuffer toLineViewXmlString( StringBuffer buffer ) {
        if( TRACE_ENABLED) logger.debug( "entry: Directory.toLineViewXmlString()" );
        if( buffer == null ) {
            buffer = new StringBuffer() ;
        }
        
        FileManagerNode node = getNode() ;
        NodeMetadata metadata = node.getMetadata() ;
        String name = node.getName() ;
        String filePath = path().toString() ;        
        String safeName = Item.getSafeName(filePath);
        
        
        buffer
        	.append( "<" )
        	.append( Tree.ITEM_ELEMENT )
        	.append( " type=\"" ).append( Tree.MYSPACE_FOLDER_ATTR ).append( "\" ")
        	.append( " safe-name=\"" ).append( safeName ).append( "\" ")
        	.append( " full-name=\"" ).append( filePath ).append( "\" ")
        	.append( " item-name=\"" ).append( name ).append( "\" ")
        	.append( " id=\"" ).append( safeName ).append( "\" ") 
        	.append( " folder-path=\"" ).append( filePath ).append( "\" ")
        	
        	.append( " filled=\"" ).append( this.isFilled() ).append( "\" " )
        	.append( " valid=\"" ).append( this.isValid() ).append( "\" " )
        	.append( " empty=\"" ).append( this.isEmpty() ).append( "\" " )        	
        	   	
        	.append( Tree.NAME_ATTR ).append( "=\"" ).append( name ).append( "\" " )
        	.append( Tree.CREATED_ATTR  ).append( "=\"" ).append( metadata.getCreateDate().getTime().toString() ).append( "\" " )
        	.append( Tree.MODIFIED_ATTR  ).append( "=\"" ).append( metadata.getModifyDate().getTime().toString() ).append( "\" " )
        	.append( Tree.MIME_TYPE_ATTR  ).append( "=\"" ).append( "Folder" ).append( "\" " )
        	
        	.append( ">" ) 
        
        	.append( "</" )
        	.append( Tree.ITEM_ELEMENT )
        	.append( ">" ) ;
        
        if( TRACE_ENABLED) logger.debug( "exit: Directory.toLineViewXmlString()" );
        return buffer ;
    }
    
    
    public String toDetailsViewXmlString() {
        return null ;
    }
    
    
    public boolean isEmpty() {
        return ( this.children == null ? true : this.children.isEmpty() ) ;
    }
    
    protected Directory add( Item item ) {
        this.children.add( item ) ;
        return this ;
    }
    
    protected Directory remove( Item item ) {
        this.children.remove( item ) ;
        return this ;
    }

    /**
     * @return Returns the children.
     */
//    protected List getChildren() {
//        return children;
//    }
    
    /**
     * @param children The children to set.
     */
//    protected void setChildren(List children) {
//        this.children = children;
//    }
    
    /**
     * @return Returns the exception.
     */
    public Exception getException() {
        return exception;
    }
    /**
     * @param exception The exception to set.
     */
    protected void setException(Exception exception) {
        this.exception = exception;
    }
    /**
     * @return Returns the filled.
     */
    public boolean isFilled() {
        return filled;
    }
    /**
     * @param filled The filled to set.
     */
    protected void setFilled(boolean filled) {
        this.filled = filled;
    }
    /**
     * @return Returns the valid.
     */
    public boolean isValid() {
        return valid;
    }
    /**
     * @param valid The valid to set.
     */
    protected void setValid(boolean valid) {
        this.valid = valid;
    }
} // end of class Directory
