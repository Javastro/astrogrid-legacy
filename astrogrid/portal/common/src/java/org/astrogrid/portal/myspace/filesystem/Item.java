/*
 * Created on 25-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.myspace.filesystem;

//import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Collections; 



//import org.astrogrid.store.Ivorn;
// import org.astrogrid.filemanager.client.FileManagerDelegate ;
//import org.astrogrid.filemanager.resolver.FileManagerDelegateResolverImpl ;
import org.astrogrid.filemanager.client.FileManagerNode ;
//import org.astrogrid.filemanager.common.exception.*;
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
        
        if( (filePath != null && fileName != null) 
             &&
             filePath.endsWith( fileName) ) {
            
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
    public FileManagerNode getNode() {
        return myNode;
    }
    
    protected void setNode( FileManagerNode node ) {
        this.myNode = node ;
    }
 
    
    public String path() {
        
        StringBuffer buffer = null ;
        ArrayList list = new ArrayList() ; 
        Item item = this ;
        
        list.add( item.getNode() ) ;
        while( item.isRoot() == false ) {
            item = item.getParent() ;
            list.add( item.getNode() ) ;
        }  ;
        
        buffer = new StringBuffer( list.size() * 16 ) ;
        
        Collections.reverse( list ) ;
        
        ListIterator iterator = list.listIterator() ;
        FileManagerNode node = null ;
        
        while( iterator.hasNext() ) {
            node = (FileManagerNode) iterator.next() ;
            buffer.append( node.getName() ) ;
            if( node.isFolder() )
                buffer.append( Tree.SEPARATOR ) ;
        }
        
        return buffer.toString() ;
    }
    
    
    public abstract StringBuffer toTreeViewXmlString( StringBuffer buffer, String[] openBranches ) ;
    public abstract String toDirectoryViewXmlString() ;
    public abstract String toDetailsViewXmlString() ;
    public abstract StringBuffer toLineViewXmlString( StringBuffer buffer ) ;
    
    public boolean isRoot() {
        return ( myParent == null ? true : false ) ;
    }
    
    public Directory getRoot() {
        return (Directory) ( isRoot() ? this : getParent().getRoot() ) ;
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
    protected Directory getParent() {
        return myParent;
    }
 
} // end of class Item
