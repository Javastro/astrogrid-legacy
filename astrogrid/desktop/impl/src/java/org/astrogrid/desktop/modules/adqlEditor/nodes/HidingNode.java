package org.astrogrid.desktop.modules.adqlEditor.nodes;


import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandInfo;

/**
 * 
 */
public abstract class HidingNode extends AdqlNode {
    
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    private static final Log log = LogFactory.getLog( HidingNode.class ) ;
    
    public static final Hashtable HIDE_CHILDREN ; 
    static {
        HIDE_CHILDREN = new Hashtable() ;
        HIDE_CHILDREN.put( AdqlData.ATOM_TYPE, "" ) ;
    }
    
    public static boolean isHidingRequired( XmlObject xmlbean ) {
        boolean retValue = false ;
        SchemaType type = xmlbean.schemaType() ;
        if(  ( type.isBuiltinType() == false )
             &&
             ( type.isAnonymousType() == false )
             &&
             ( HIDE_CHILDREN.containsKey( type.getName().getLocalPart() ) ) ) {
            retValue = true ;
        }
        return retValue ;
    }

    
    HidingNode( NodeFactory nodeFactory, AdqlNode parent, XmlObject o ) throws UnsupportedObjectException {
        super( nodeFactory, parent, o, false ) ;
        if( !HIDE_CHILDREN.containsKey( AdqlUtils.getLocalName( o ) ) ) {
            throw new UnsupportedObjectException( "Object " + o.schemaType().getName() + " does not support Child Hiding." ) ;
        }
    }
    
    HidingNode( NodeFactory nodeFactory, AdqlNode parent, XmlObject o, int childNodeIndex ) throws UnsupportedObjectException {
        super( nodeFactory, parent, o, childNodeIndex, false ) ;
        if( !HIDE_CHILDREN.containsKey( AdqlUtils.getLocalName( o ) ) ) {
            throw new UnsupportedObjectException( "Object " + o.schemaType().getName() + " does not support Child Hiding." ) ;
        }
    }
    
    protected void maintainNodeIndex( AdqlNode child ) {}
    
    @Override
    public boolean isBottomLeafEditable() {
        return true ;
    }
    
    @Override
    public abstract String toHtml( boolean expanded, boolean leaf, AdqlTree tree ) ;
        
    @Override
    public AdqlNode insert(CommandInfo ci, XmlObject source, boolean before) {
        return null ;
    }
    @Override
    public AdqlNode insert(CommandInfo ci, XmlObject source) {
        return null;
    }
    @Override
    public AdqlNode insert(CommandInfo ci) {
       return null ;
    }
    public void remove(AdqlNode child) {
       return ;
    }
    public AdqlNode replace(AdqlNode target, XmlObject source) {
        return null ;
    }
    
}