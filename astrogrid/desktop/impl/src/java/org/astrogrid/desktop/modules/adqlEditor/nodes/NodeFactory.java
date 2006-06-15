
package org.astrogrid.desktop.modules.adqlEditor.nodes ;


import java.util.Enumeration;
import java.util.Hashtable;
import java.lang.Integer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.SchemaProperty; 
import org.apache.xmlbeans.SchemaType; 
import org.apache.xmlbeans.SimpleValue; 
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlLong ;
import org.apache.xmlbeans.XmlDouble ;



import javax.swing.tree.DefaultMutableTreeNode ;
import org.astrogrid.desktop.modules.adqlEditor.*;

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
    
    
    public static AdqlNode newInstance( XmlObject rootObject ) {

        AdqlNode rootEntry = newInstance( null, rootObject ) ;
        return rootEntry ;
    }
    
    public static AdqlNode newInstance( AdqlNode parent, XmlObject xmlObject, int childNodeIndex ) {
        AdqlNode node = null ;
        try { 
            if( NestingNode.isNestingRequired( xmlObject ) ) {
                node = new NestingNode( parent, xmlObject, childNodeIndex ) ;
            }
            else if( HidingNode.isHidingRequired( xmlObject ) ) {
                node = new AtomNode( parent, xmlObject, childNodeIndex ) ;
            }
            else {
                node = new AdqlNode( parent, xmlObject, childNodeIndex ) ;
            }
        }
        catch( Exception ex ) {
            log.debug( "NodeFactory.newInstance(AdqlNode, XmlObject, int):", ex );
        }
        return node ;
    }
     
    public static AdqlNode newInstance( AdqlNode parent, XmlObject xmlObject ) {
        AdqlNode entry = null ;
        if( xmlObject == null )
            return entry ;
        try { 
            if( NestingNode.isNestingRequired( xmlObject ) ) {
                entry = new NestingNode( parent, xmlObject ) ;
            }
            else if( HidingNode.isHidingRequired( xmlObject ) ) {
                entry = new AtomNode( parent, xmlObject ) ;
            }
            else {
                entry = new AdqlNode( parent, xmlObject ) ;
            }
        }
        catch( Exception ex ) {
            log.debug( "NodeFactory.newInstance():", ex );
        }
        return entry ;
    }
    
    public static boolean removeInstance( AdqlNode parent, AdqlNode child ) {
        boolean retValue = false ;
        XmlObject co ;  // child object
        XmlObject po ; // parent object     
        XmlCursor cursor = null ;
        try {
            //
            // Simple bit first. Remove the child entry from the mutable tree...
            parent.remove( child ) ;
            //
            // XmlCursor is plain wonderful! 
            // I use it to remove the object from the parent.
            co = child.getXmlObject() ;  // child object
            po = parent.getXmlObject() ; // parent object
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
    
    
    public static boolean disconnectInstance( AdqlNode parent, AdqlNode child ) {
        parent.remove( child ) ;
        return true ;
    }

    public static boolean reconnectInstance( AdqlNode parent, AdqlNode child, int index ) {
        parent.insert( child, index ) ;
        return true ;
    } 

    public static boolean reconnectInstance( AdqlNode parent, AdqlNode child ) {
        parent.add( child ) ;
        return true ;
    } 
    
}