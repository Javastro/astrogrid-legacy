/*
 * Created on 25-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.myspace.filesystem;

import java.io.Serializable;

import org.astrogrid.portal.myspace.generation.StorageFilesGenerator;
import org.astrogrid.store.Ivorn;
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
public class Tree extends Directory implements Serializable {
    
    private static final boolean TRACE_ENABLED = true ;
    private static final boolean DEBUG_ENABLED = true ;
    private static Logger logger = Logger.getLogger( Tree.class );
    
    public static final String SEPARATOR = "/";
    public static final String MYSPACE_TREE = "myspace-tree";
    public static final String MYSPACE_ITEM = "myspace-item";
    public static final String MYSPACE_ENDPOINT_ATTR = "endpoint";
    public static final String MYSPACE_FILE_ATTR = "file";
    public static final String MYSPACE_FOLDER_ATTR = "folder";
        
    private FileManagerDelegate delegate ;
    private Ivorn accountSpace ;

      
    public static Tree newTree( Ivorn accountSpace ) {
        if( TRACE_ENABLED ) logger.debug( "entry: Tree.newTree(Ivorn)" ) ;
        Tree tree = null ;

        try {
            FileManagerDelegate delegate = (new FileManagerDelegateResolverImpl()).resolve( accountSpace ) ;
            FileManagerNode rootNode = delegate.getAccount( accountSpace ) ;
            tree = new Tree( accountSpace, delegate, rootNode ) ;            
        }
        catch( Exception e ) {
            ;
        } 
        finally {
            if( TRACE_ENABLED ) logger.debug( "exit: Tree.newTree(Ivorn)" ) ;
        }
        return tree ;
        
    } // end of newTree( Ivorn )
     
    
    private Tree( Ivorn accountSpace, FileManagerDelegate delegate, FileManagerNode myNode ) {
        super( myNode, (Directory)null ) ;
        if( TRACE_ENABLED ) logger.debug( "entry: Tree()" ) ;
        this.accountSpace = accountSpace ;
        this.delegate = delegate ;
        if( TRACE_ENABLED ) logger.debug( "exit: Tree()" ) ;   
        
    } // end of Tree( Ivorn )
    
    
    public Directory newDirectory( FileManagerNode myNode, Directory myParent ) {
        Directory directory = new Directory( myNode, myParent ) ;
        
        return directory ;
    }
    
    
    public File newFile( FileManagerNode myNode, Directory myParent ) {
        File file = new File( myNode, myParent ) ;
        
        return file ;
    }
    

    public String toXmlString() {
        if( TRACE_ENABLED ) logger.debug( "entry: Tree.toXmlString()") ;
        
        StringBuffer buffer ;
        
        try {
            // More intelligent would be a calculation of buffer size required...
            buffer = new StringBuffer( 4096*2 ) ;
            
            buffer
         		.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" )
         		.append( "<" )
         		.append( Tree.MYSPACE_TREE )
         		.append( ' ' )
         		.append( Tree.MYSPACE_ENDPOINT_ATTR )
         		.append( "=\"" )
         		.append( this.accountSpace.toString() ) 
         		.append( "\" >" ) ;
            
            // A tree instance is also the root directory, so ...
            super.toXmlString( buffer ) ;
         	
            buffer
         		.append( "</" )
         		.append( Tree.MYSPACE_TREE )
         		.append( ">" ) ;
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "exit: Tree.toXmlString()") ;
        }
        
        return buffer.toString() ;
        
    } // end of toXmlString()
    
 
} // end of class Tree
