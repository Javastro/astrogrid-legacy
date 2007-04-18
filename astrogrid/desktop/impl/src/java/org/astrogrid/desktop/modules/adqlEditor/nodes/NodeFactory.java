
package org.astrogrid.desktop.modules.adqlEditor.nodes ;


import java.util.Hashtable;

import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;

/**
 * . 
 */
public final class NodeFactory {
    
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    private static final Log log = LogFactory.getLog( NodeFactory.class ) ;
    
    public static final String MISSING_VALUE = "{ missing }" ;
    
    public static final Hashtable VALUE_GETTERS ;
    static {
        VALUE_GETTERS = new Hashtable() ;
        VALUE_GETTERS.put( "REAL", "REAL" ) ;
        VALUE_GETTERS.put( "INTEGER", "INTEGER" ) ;
        VALUE_GETTERS.put( "STRING", "STRING" ) ; // Not sure about this one.
    }
    
    private DefaultTreeModel treeModel ;
    private AdqlTree adqlTree ;
    private boolean bForcedNotToUseModel = false ;
    
    public void setForcedNotToUseModel( boolean force ) {
        bForcedNotToUseModel = force ;
    }
    
    public boolean isForcedNotToUseModel() {
        return bForcedNotToUseModel ;
    }
    
    public NodeFactory( AdqlTree adqlTree ) {
        this.adqlTree = adqlTree ;
    }
    
    public AdqlNode newInstance( XmlObject rootObject ) {
        AdqlNode rootEntry = newInstance( null, rootObject ) ;
        setTreeModel( rootEntry ) ;
        return rootEntry ;
    }
    
    public AdqlNode newInstance( AdqlNode parent, XmlObject xmlObject, int childNodeIndex ) {
        AdqlNode node = null ;
        try { 
            if( NestingNode.isNestingRequired( xmlObject ) ) {
                node = new NestingNode( this, parent, xmlObject, childNodeIndex ) ;
            }
            else if( HidingNode.isHidingRequired( xmlObject ) ) {
                node = new AtomNode( this, parent, xmlObject, childNodeIndex ) ;
            }
            else {
                node = new AdqlNode( this, parent, xmlObject, childNodeIndex ) ;
            }
        }
        catch( Exception ex ) {
            log.debug( "NodeFactory.newInstance(AdqlNode, XmlObject, int):", ex );
        }
        return node ;
    }
     
    public AdqlNode newInstance( AdqlNode parent, XmlObject xmlObject ) {
        AdqlNode entry = null ;
        if( xmlObject == null )
            return entry ;
        try { 
            if( NestingNode.isNestingRequired( xmlObject ) ) {
                entry = new NestingNode( this, parent, xmlObject ) ;
            }
            else if( HidingNode.isHidingRequired( xmlObject ) ) {
                entry = new AtomNode( this, parent, xmlObject ) ;
            }
            else {
                entry = new AdqlNode( this, parent, xmlObject ) ;
            }
        }
        catch( Exception ex ) {
            log.debug( "NodeFactory.newInstance():", ex );
        }
        return entry ;
    }
    
    public boolean removeInstance( AdqlNode parent, AdqlNode child ) {
        boolean retValue = false ;
        XmlObject co = child.getXmlObject() ;  // child object
        XmlObject po = parent.getXmlObject() ; // parent object
        XmlCursor cursor = null ;
        try {
            //
            // Simple bit first. Remove the child entry from the mutable tree...
            // parent.remove( child ) ;
            remove( parent, child ) ;
        
            //
            // XmlCursor is plain wonderful! 
            // I use it to remove the object from the parent.            
            cursor = po.newCursor() ;
            cursor.toFirstChild() ; // There has to be a first child!
            do {
                if( co == cursor.getObject() ) {
                    retValue = cursor.removeXml() ;
                    break ;
                }
            } while( cursor.toNextSibling() ) ;
            retValue = true ;
        }
        catch( Exception ex ) {
            log.debug( "Failure to remove instance..." ) ;
            log.debug( "Parent:" ) ;
            parent.debug_LogChildren() ;
                        
            log.debug( "Child:" ) ;
            log.debug( child.getXmlObject().toString() ) ;
            log.debug( "hashCode: " + child.hashCode() ) ;
        } 
        finally {
            if( cursor != null )
                cursor.dispose() ;
        }
        return retValue ;
    }
    
    
    public boolean disconnectInstance( AdqlNode parent, AdqlNode child ) {
        return disconnectInstance( parent, child, checkOnModelUse() ) ;
    }
    
    public boolean disconnectInstance( AdqlNode parent, AdqlNode child, boolean bRefresh ) {
        remove( parent, child, bRefresh ) ;
        return true ;
    }

    public boolean reconnectInstance( AdqlNode parent, AdqlNode child, int index ) {
        return reconnectInstance( parent, child, index, checkOnModelUse() ) ;
    } 
    
    public boolean reconnectInstance( AdqlNode parent
                                    , AdqlNode child
                                    , int index
                                    , boolean bRefresh ) {
        insert( child, parent, index, bRefresh ) ;
        return true ;
    } 

    public boolean reconnectInstance( AdqlNode parent, AdqlNode child ) {
        return reconnectInstance( parent, child, checkOnModelUse() ) ;      
    } 
    
    public boolean reconnectInstance( AdqlNode parent, AdqlNode child, boolean bRefresh ) {
        add( parent, child, bRefresh ) ;
        return true ;
    } 
    
    public void add( AdqlNode parent, AdqlNode child ) {
        add( parent, child, checkOnModelUse() ) ;
    }
    
    public void add( AdqlNode parent, AdqlNode child, boolean bRefresh ) {
        if( bRefresh ) {
            int index = parent.getChildCount() ;
            getTreeModel().insertNodeInto( child, parent, index ) ;
        }
        else {
            parent.add( child ) ;
        }
    }
    
    public void insert( AdqlNode parent, AdqlNode child, int index ) {
        insert( parent, child, index, checkOnModelUse() ) ;
    }
    
    public void insert( AdqlNode parent, AdqlNode child, int index, boolean bRefresh ) {
        if( bRefresh ) {
            getTreeModel().insertNodeInto( child, parent, index ) ;
        }
        else {
            parent.insert( child, index ) ;
        }
    }
    
    public void remove( AdqlNode parent, AdqlNode child ) {
        remove( parent, child, checkOnModelUse() ) ;
    }
    
    public void remove( AdqlNode parent, AdqlNode child, boolean bRefresh ) {
        if( child == getRootEntry() )
            return ;
        if( bRefresh ) {
            getTreeModel().removeNodeFromParent( child ) ;
        }
        else {
            parent.remove( child ) ;
        }
    }
    
    public AdqlNode getRootEntry() {
        return (AdqlNode)getTreeModel().getRoot() ;
    }
    
    private void setTreeModel( AdqlNode rootEntry ) {
        treeModel = new DefaultTreeModel( rootEntry ) ;
        adqlTree.setModel( treeModel ) ;
    }
    
    public DefaultTreeModel getTreeModel() {
        return treeModel ;
    }
    
    private boolean checkOnModelUse() {
        boolean retValue = false ;
        if( treeModel != null && bForcedNotToUseModel == false ) 
            retValue = true ;
        return retValue ;
    }

    public AdqlTree getAdqlTree() {
        return adqlTree;
    }
}