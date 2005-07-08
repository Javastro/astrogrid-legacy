/*
 * Created on 25-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.myspace.filesystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
//import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.NodeIterator;
//import org.astrogrid.filemanager.common.NodeNotFoundFault;
//import org.astrogrid.filemanager.common.FileManagerFault;

// import org.astrogrid.filemanager.common.exception.*;
import org.astrogrid.store.Ivorn;


/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Tree extends Directory implements Serializable {
    
    private static final boolean TRACE_ENABLED = true ;
    private static final boolean DEBUG_ENABLED = true ;
    private static Logger logger = Logger.getLogger( Tree.class );
    
    public static final String SEPARATOR = "/";
    
    public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" ;
    
    public static final String 
    	TREE_ELEMENT = "myspace-tree",
    	ITEM_ELEMENT = "myspace-item",
    	PROPERTIES_ELEMENT = "file-properties",
    	DIRECTORY_ELEMENT = "directory-item",
    	MYSPACE_ENDPOINT_ATTR = "endpoint",
    	MYSPACE_FILE_ATTR = "file",
    	MYSPACE_FOLDER_ATTR = "folder",
    	CREATED_ATTR = "created",
    	NODE_CREATED_ATTR = "node-created",
    	FILE_CREATED_ATTR = "file-created",
    	MIME_TYPE_ATTR = "mime-type",
    	MODIFIED_ATTR = "modified",
    	NODE_MODIFIED_ATTR = "node-modified",
    	FILE_MODIFIED_ATTR = "file-modified",
    	NAME_ATTR = "name",
    	OWNER_ATTR = "owner",
    	PATH_ATTR = "path",
    	SIZE_ATTR = "size",
    	URL_ATTR = "url",
    	CONTENT_LOCATION = "content-location";
    
    private static final int 
    	INCREMENTAL_SIZE = 256,
    	MAX_LEVEL_LOADING_COUNT = 1 ;
        
    private FileManagerClient fileManagerClient ;
    private Ivorn accountSpace ;
    private Map itemCache ; 

      
    public static Tree constructTree( Ivorn accountSpace, FileManagerClient fmClient ) {
        if( TRACE_ENABLED ) logger.debug( "entry: Tree.constructTree(Ivorn,FileManagerClient)" ) ;
        Tree tree = null ;
        
        logger.debug( "accountSpace Ivorn: " + accountSpace.toString() ) ;

        try {
            logger.debug( "About to get home()..." ) ;
            FileManagerNode rootNode = fmClient.home() ;
            logger.debug( "Tree.constructTree found home" ) ;
            tree = new Tree( accountSpace, fmClient, rootNode ) ; 
            // Build from the root...
            tree.constructBranch( tree ) ;
        }
        catch( Throwable ex ) {
            logger.debug( "Tree.constructTree() failed.", ex ) ;
        } 
        finally {
            if( TRACE_ENABLED ) logger.debug( "exit: Tree.constructTree(Ivorn,FileManagerClient)" ) ;
        }
        return tree ;
        
    } // end of constructTree( Ivorn )
     
    
    protected Tree( Ivorn accountSpace, FileManagerClient client, FileManagerNode rootNode ) {
        super( rootNode, (Directory)null ) ;
        if( TRACE_ENABLED ) logger.debug( "entry: Tree()" ) ;
        this.accountSpace = accountSpace ;
        this.fileManagerClient = client ;
        this.itemCache = new HashMap( 256 ) ;
        this.itemCache.put( this.path(), this ) ;
        if( TRACE_ENABLED ) logger.debug( "exit: Tree()" ) ;   
        
    } // end of Tree( Ivorn )
    
    
    /**
     * This is a first attempt at eliminating recursive filling of a branch,
     * either via a method or via a constructor. It is not very OO, but it is 
     * proving difficult to manage this in an OO manner without resorting to 
     * some form of recursion. 
     * 
     * @param directory
     */
    public void constructBranch( Directory directory, String[] essentialPaths ) {
        if( TRACE_ENABLED ) logger.debug( "entry: Tree.constructBranch(Directory)" ) ;
        
        try {
        
        int levelCount = 0 ;
        
        ArrayList
        	parents = new ArrayList( INCREMENTAL_SIZE ),
        	children = new ArrayList( INCREMENTAL_SIZE ),
        	referenceHolder = null ;
        
        Iterator dirIterator ;
        NodeIterator nodeIterator ;
        FileManagerNode 
        	parentNode = null, 
        	childNode = null ;
        Directory parentDirectory ;
        
        parents.add( directory ) ;
        
        logger.debug( "About to load first level..." ) ;
        while( parents.size() > 0 ) {
            dirIterator = parents.iterator() ;
            while( dirIterator.hasNext() ) {
                parentDirectory = (Directory)dirIterator.next() ;
                parentNode = parentDirectory.getNode() ;
      
                try {
                    logger.debug( "About to access node iterator for directory..." ) ;
                    nodeIterator = parentNode.iterator() ;
                    parentDirectory.setFilled( true ) ;
                    logger.debug( "About to load directory..." ) ;
                    while( nodeIterator.hasNext() ) {
                        logger.debug( "About to get next child of directory..." ) ;
                        childNode = (FileManagerNode)nodeIterator.next() ;
                        if( childNode.isFolder() ) {
                            children.add( constructDirectory( childNode, parentDirectory ) ) ;
                        }
                        else if( childNode.isFile() ) {
                            constructFile( childNode, parentDirectory ) ;
                        }
                        else {
                            ; // ?
                        }
                            
                    } // end while
                }
                catch( Exception ex ) {
                    logger.debug( "Some node misbehaved.\n" 
                                + "Parent node [" + (parentNode != null ? parentNode.getName() : "" ) + "]\n"
                                + "Child node [" + (childNode != null ? childNode.getName() : "" ) + "]", ex ) ;
                    parentDirectory.setValid( false ) ;
                    parentDirectory.setException( ex ) ;
                    continue ;
                }
                
            } // end while
            
            if( ++levelCount >= MAX_LEVEL_LOADING_COUNT ) break ; // test of loading 
            
            // We've finished with the parents, so clear the parent array...
            parents.clear() ;
            
            // Swap the arrays...
            referenceHolder = parents ;
            parents = children ;
            children = referenceHolder ;
            
            // The fancy arithmetic walks the ArrayList size up in increments of whatever
            // INCREMENTAL_SIZE is set to. I  hope this ensures reasonable efficiency wherever we 
            // hit a large number of directories ...
            if( parents.size()%INCREMENTAL_SIZE > 0 ) {
                children.ensureCapacity( ( parents.size()/INCREMENTAL_SIZE + 1 ) * INCREMENTAL_SIZE ) ;
            }
            
        } // end while
        
        parents.clear() ;
        children.clear() ;
        
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "exit: Tree.constructBranch(Directory)" ) ;
        }
        
    } // end of constructBranch( Directory )
    
    
    public void constructBranch( Directory directory ) {
       String[] essentialPaths = null ;
       this.constructBranch( directory, essentialPaths ) ;     
    }
    
    
    
    
    public Directory constructDirectory( FileManagerNode node, Directory parent ) {
        Directory directory = new Directory( node, parent ) ;
        if( directory != null ) {
            itemCache.put( directory.path(), directory ) ;
            parent.add( directory ) ;
        }
        return directory ;
    }
    
    public Directory newDirectory( String name, String path ) {
        if( TRACE_ENABLED ) logger.debug( "entry: newDirectory( String name, String path )" ) ;
        
        logger.debug( "path: " + path ) ;
        logger.debug( "name: " + name ) ;
        Directory 
            parentDirectory = null,
            newDirectory = null ;
        FileManagerNode
            parentNode = null,
            newNode = null ;
        
        try {
            parentDirectory = this.getDirectory( path ) ;
            parentNode = parentDirectory.getNode() ;
                       
            try {
                newNode = parentNode.addFolder( name ) ;
            }
            catch( Exception ex ) {
                logger.error( "Failed to create new directory", ex ) ;
            }
            if( newNode != null ) { 
                newDirectory = this.constructDirectory( newNode, parentDirectory ) ;
                if( DEBUG_ENABLED ) assert( newDirectory != null ) ;
                newDirectory.setFilled( true ) ;
            }
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "exit: newDirectory( String name, String path )" ) ;
        }
        
        return newDirectory ;
        
    }
    
    
    /**
     * Attempts to accommodate an out-of-date directory. The tree is a lazey initializor. 
     * If the directory has never been filled by the tree, or if it has 
     * been but proved at the time to be empty, then an attempt is made to fill the branch.
     * If the directory has previously been filled by the tree and was not empty,
     * then we attempt a crude form of synchronization. At the moment (March 2005)
     * synchronization means refreshing it of files. Nothing is done for sub-directories.
     * 
     * @param directory
     */
    public void refresh( Directory directory ) {
        if( TRACE_ENABLED ) logger.debug( "entry: Tree.refresh( Directory directoy )" ) ;
        
        try {
            if( directory.isFilled() == false
                ||
                directory.isEmpty() == true ) {
                this.constructBranch( directory ) ;                   
            }
            else {
                directory.resynchWithSystem() ;
            }           
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "exit: Tree.refresh( Directory directoy )" ) ;
        }
        
    }
    
    
    public File newFile( String name, String path ) {
        if( TRACE_ENABLED ) logger.debug( "entry: newFile( String name, String path )" ) ;
        
        logger.debug( "path: " + path ) ;
        logger.debug( "name: " + name ) ;
        Directory 
            parentDirectory = null ;
        File
            newFile = null ;
        FileManagerNode
            parentNode = null,
            newNode = null ;
        
        try {
            parentDirectory = this.getDirectory( path ) ;
            parentNode = parentDirectory.getNode() ;
                       
            try {
                newNode = parentNode.addFile( name ) ;
            }
            catch( Exception ex ) {
                logger.error( "Failed to create new file", ex ) ;
            }
            if( newNode != null ) { 
                newFile = this.constructFile( newNode, parentDirectory ) ;
                if( DEBUG_ENABLED ) assert( newFile != null ) ;
            }
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "exit: newFile( String name, String path )" ) ;
        }
        
        return newFile ;
    }
    
    
    public File getFile( String pathRelativeToAccountSpace ) {
        File file = null ;
        Item item = (Item) itemCache.get( pathRelativeToAccountSpace ) ;
        if( item != null && item instanceof File ) {
            file = (File)item ;
        }
        return file ;
    }
    
    
    public Directory getDirectory( String pathRelativeToAccountSpace ) {
        Directory directory = null ;
        if( pathRelativeToAccountSpace != null ) {
            Item item = (Item) itemCache.get( pathRelativeToAccountSpace ) ;
            if( item != null && item instanceof Directory ) {
                directory = (Directory)item ;
            }
        }
        
        return directory ;
    }
    
    
    public Item getItem( String pathRelativeToAccountSpace ) {
        return (Item) itemCache.get( pathRelativeToAccountSpace ) ;
    }
    
    
    public File constructFile( FileManagerNode node, Directory parent ) {
        File file = new File( node, parent ) ;
        if( file != null ) {
            itemCache.put( file.path(), file ) ; 
            parent.add( file ) ;
        }
        return file ;
    }
    
    protected void destructFile( File file ) {
        itemCache.remove( file.path() ) ;
        file.setNode( null ) ; // safety first.
    }
     
    
    public String toXmlString() {
        String[] nullArray = null ;
        return this.toXmlString( nullArray ) ;
    }

    public String toXmlString( String[] openBranches ) {
        if( TRACE_ENABLED ) logger.debug( "entry: Tree.toXmlString(String[] openBranches)") ;
        
        StringBuffer buffer ;
        
        try {
            // More intelligent would be a calculation of buffer size required...
            buffer = new StringBuffer( 4096*2 ) ;
            
            buffer
         		.append( Tree.XML_HEADER )
         		.append( "<" )
         		.append( Tree.TREE_ELEMENT )
         		.append( ' ' )
         		.append( Tree.MYSPACE_ENDPOINT_ATTR )
         		.append( "=\"" )
         		.append( this.accountSpace.toString() ) 
         		.append( "\" >" ) ;
            
            // A tree instance is also the root directory, so ...
            super.toTreeViewXmlString( buffer, openBranches ) ;
         	
            buffer
         		.append( "</" )
         		.append( Tree.TREE_ELEMENT )
         		.append( ">" ) ;
            
            if( DEBUG_ENABLED ) {
                logger.debug( "Buffer size is: " + buffer.length() + 
                              "\nNumber of items in cache is: " + itemCache.size() ) ;
//                logger.debug( buffer.toString() ) ;
            }

        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "exit: Tree.toXmlString(String[] openBranches)") ;
        }
        
        return buffer.toString() ;
        
    } // end of toXmlString(String[] openBranches)
    
 
    /**
     * @return Returns the accountSpace.
     */
    public Ivorn getAccountSpace() {
        return accountSpace;
    }
    
    /**
     * @param accountSpace The accountSpace to set.
     */
    public void setAccountSpace(Ivorn accountSpace) {
        this.accountSpace = accountSpace;
    }
    
    
    public Ivorn formNodeIvorn( String path ) {
        Ivorn targetIvorn = null ;
        
        try {
            Ivorn rootIvorn = this.getNode().getIvorn() ;
            StringBuffer buffer = new StringBuffer( 256 ) ;
            buffer
         		.append( Ivorn.SCHEME )
         		.append( "://" )
         		.append( rootIvorn.getPath() )
         		.append( '#' )
         		.append( rootIvorn.getFragment() )
         		.append( path.substring( path.indexOf( '/' ) ) ) ;
            
            logger.debug( "buffer.length(): " + buffer.length() );         
            targetIvorn = new Ivorn( buffer.toString() ) ;
         
        }
        catch( Exception e ) {
            logger.error( "Could not generate ivorn given relative path.", e ) ;
            // Do nothing. We must handle nulls.
        } 
        
        return targetIvorn ;
        
    } // end of formNodeIvorn(String)
    
    
public File copyFile( String targetName
                    , String targetPath
                    , String sourcePath ) throws CopyException {
    if( TRACE_ENABLED ) logger.debug( "entry: Tree.copyFile()") ;
    File source = null ;
    File target = null ;
    Directory targetDirectory = null ;
    FileManagerNode newNode = null  ;
    
    try {
        source = this.getFile( sourcePath ) ;
        if( source == null ) {
            throw new CopyException( "File not found: \n" + sourcePath ) ;
        } 
        targetDirectory = this.getDirectory( targetPath ) ;
        if( targetDirectory == null ) {
            throw new CopyException( "Target directory not found: \n" + targetPath ) ;
        } 
        target = this.getFile( targetPath + targetName ) ;
        if( target != null ) {
            this.deleteFile( target, false ) ;
        }
        newNode = source.getNode().copy( targetName
                                       , targetDirectory.getNode()
                                       , null ) ;
        newNode.transferCompleted() ;
        logger.debug( "newNode: " + newNode ) ;
        target = this.constructFile( newNode, targetDirectory ) ;
        logger.debug( "target: " + target ) ;
        
    }
    catch( Exception ex ) {
            logger.error( ex ) ;
            throw new CopyException( "Could not create target file: \n" 
                                   + "name: " + targetName + "\n" 
                                   + "path: " + targetPath ) ;
    }
    finally {
        if( TRACE_ENABLED ) logger.debug( "exit: Tree.copyFile()") ;
    }
  
    return target ;
    
}


public Directory copyDirectory( String sourcePath, String destinationPath ) throws CopyException {
    Directory source = this.getDirectory( sourcePath ) ;
    if( source == null ) {
        throw new CopyException( "Directory not found" ) ;
    }
    // Place holder for future implementation
    return null ;
}

public Item copyItem( String sourcePath, String destinationPath ) {
    return null ;
}


public void moveFile( String targetName, String targetPath, String sourcePath ) throws MoveException {
    if( TRACE_ENABLED ) logger.debug( "entry: Tree.moveFile()") ;
    try {
        this.copyFile( targetName, targetPath, sourcePath ) ;
        this.deleteFile( sourcePath, false ) ;
    }
    catch( Exception ex ) {
        throw new MoveException( ex.getLocalizedMessage() ) ;
    }
    finally {
        if( TRACE_ENABLED ) logger.debug( "exit: Tree.moveFile()") ;
    }   
}


public void deleteFile( File file, boolean repressRefresh ) throws DeleteException {
    this.deleteItem( file, repressRefresh ) ;
}

public void deleteDirectory( Directory directory, boolean repressRefresh ) throws DeleteException {
    this.deleteItem( directory, repressRefresh ) ;
}


public void deleteDirectory( String path, boolean repressRefresh ) throws DeleteException {
    this.deleteItem( path, repressRefresh ) ;
}


public void deleteFile( String path, boolean repressRefresh ) throws DeleteException {
    this.deleteItem( path, repressRefresh ) ;
}


//public void deleteFile( String path ) throws DeleteException {
//    if( TRACE_ENABLED ) logger.debug( "entry: Tree.deleteFile()") ;
//    
//    try{ 
//        File file = this.getFile( path ) ;
//        if( file == null ) {
//            logger.debug( "File not found:\n" + path ) ;
//            throw new DeleteException( "File not found.\n" + path ) ;
//        }
//        try{
//            FileManagerNode parentNode = file.getParent().getNode() ;
//            itemCache.remove( path ) ;
//            file.getParent().remove( file ) ;
//            file.getNode().delete() ;
//            parentNode.refresh() ;
//        }
//        catch( Exception ex ) {
//            logger.debug( "Deletion failed on file:\n" + path + "\n" + ex.getMessage(), ex ) ;
//            throw new DeleteException( ex.getMessage() ) ;
//        }
//
//    }
//    finally {
//        if( TRACE_ENABLED ) logger.debug( "exit: Tree.deleteFile()") ;
//    }
//   
//}


private void deleteItem( String path, boolean repressRefresh ) throws DeleteException {
    if( path == null ) {
        logger.debug( "File item not found at path:\n" + path ) ;
        throw new DeleteException( "File item not found at path:\n" + path ) ;
    }
    this.deleteItem( this.getItem(path), repressRefresh ) ;
}

private void deleteItem( Item item, boolean repressRefresh ) throws DeleteException {
    if( TRACE_ENABLED ) logger.debug( "entry: Tree.deleteItem()") ;

    String path = null ;
  
    try{ 
        FileManagerNode parentNode = item.getParent().getNode() ;
        path = item.path() ;
        itemCache.remove( path ) ;
        item.getParent().remove( item ) ;
        item.getNode().delete() ;
        if( repressRefresh == false ) {
            parentNode.refresh() ;
        }
    }
    catch( Exception ex ) {
        throw new DeleteException( ex ) ;
    }
    finally {
        if( TRACE_ENABLED ) logger.debug( "exit: Tree.deleteItem()") ;
    }
   
}


public class Policy {
    
    private String[] essentialPaths ;
    private int relativeLoadLevel ;
    private int absoluteLoadLevel ; 
    

}
    
} // end of class Tree
