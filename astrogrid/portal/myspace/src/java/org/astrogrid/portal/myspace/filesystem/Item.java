/*
 * Created on 25-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.myspace.filesystem;

import java.io.Serializable;
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
public abstract class Item {
    
    private static final boolean TRACE_ENABLED = true ;
    private static final boolean DEBUG_ENABLED = true ;
    private static Logger logger = Logger.getLogger( Item.class ) ;
    
    private FileManagerNode myNode ;
    private Directory myParent ;
    
    public static String getSafeName(String fileName ) {
        
        StringBuffer result = new StringBuffer( fileName.length() ) ;
        
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
      
      
      public static String getFolderPath(String filePath, String fileName) {
        String result = "";
        
        if( filePath != null && fileName != null) {
            result = filePath.substring(0, filePath.length() - fileName.length());
            if (result.endsWith("/")) {
              result = result.substring(0, result.length());
            }
        }
        
        return result;
      }
    
    public Item() {}
    
    public Item( FileManagerNode node, Directory parent ) {
        this.myNode = node ;
        this.myParent = parent ;
    }

    /**
     * @return Returns the myNode.
     */
    protected FileManagerNode getMyNode() {
        return myNode;
    }
    
    public StringBuffer path( StringBuffer buffer ) {
        if( buffer == null ) {
            buffer = new StringBuffer(256) ;
        }
        
        if( isRoot() == false ) {
        	buffer.append( myParent.path( buffer ) ) ; 
        }
        	
        buffer.append( myNode.name() ) ;
        
        if( myNode.isContainer() )
            buffer.append( Tree.SEPARATOR ) ;
        
        return buffer ;
    }
    
    public StringBuffer path() {
        return this.path( null );
    }
    
    public abstract StringBuffer toXmlString( StringBuffer buffer );
    
    public boolean isRoot() {
        return ( myParent == null ? true : false ) ;
    }
    
    public Directory getRoot() {
        return (Directory) ( isRoot() ? this : getMyParent().getRoot() ) ;
    }
    
    public boolean isTree() {
        return isRoot() ;
    }
    
    public Tree getTree() {
        return (Tree) getRoot() ;
    }
    
    /**
     * @return Returns the myParent.
     */
    protected Directory getMyParent() {
        return myParent;
    }
 
} // end of class Item
