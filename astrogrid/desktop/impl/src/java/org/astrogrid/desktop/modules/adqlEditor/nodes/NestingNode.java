/* NestingNode.java
 * Created on 19-Apr-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor.nodes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ListIterator;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandInfo;

/**
 * Flattens a node branch that might otherwise be deeply nested.
 * Only two contexts relevant at the moment: AND and OR
 * Visually, a series of AND's is then shown as an array of siblings
 * 
 *      a
 *      |
 *      b
 * AND--|
 *      c
 *      |
 *      d
 * 
 * rather than as a logical hierarchy...
 * 
 *      a
 *      |
 * AND--|   b
 *      |   |
 *     AND--|   c
 *          |   |
 *         AND--|
 *              |
 *              d
 *              
 * @author Jeff Lusted jl99@star.le.ac.uk
 */
public class NestingNode extends AdqlNode {
    
    private static final Log log = LogFactory.getLog( NestingNode.class ) ;
    
    public static final Hashtable CHILDREN_NESTING ; 
    static {
        CHILDREN_NESTING = new Hashtable() ;
        CHILDREN_NESTING.put( AdqlData.UNION_SEARCH_TYPE, "" ) ;
        CHILDREN_NESTING.put( AdqlData.INTERSECTION_SEARCH_TYPE, "" ) ;
    }
    
    public static boolean isNestingRequired( XmlObject o ) {
        return isNestingRequired( o.schemaType() ) ;
    }
    
    public static boolean isNestingRequired( SchemaType type ) {
        if(  ( type.isBuiltinType() == false )
             &&
             ( type.isAnonymousType() == false )
             &&
             ( CHILDREN_NESTING.containsKey( AdqlUtils.getLocalName( type ) ) ) ) {
            return true ;
        }
        return false ;
    }

    private Boolean arrayContext ;
    private SchemaProperty elementProperty ;
    private SchemaType nestingType ;
    /**
     * @param o
     */
    NestingNode( NodeFactory nodeFactory, AdqlNode parent, XmlObject o ) throws UnsupportedObjectException {
        super( nodeFactory, parent, o, true );
        if( !CHILDREN_NESTING.containsKey( AdqlUtils.getLocalName( o.schemaType() ) ) ) {
            throw new UnsupportedObjectException( "Object " + o.schemaType().getName() + " does not support Child Nesting." ) ;
        }
        nestingType = o.schemaType() ;
    }
    
    
    NestingNode( NodeFactory nodeFactory, AdqlNode parent, XmlObject o, int childNodeIndex ) throws UnsupportedObjectException {
        super( nodeFactory, parent, o, childNodeIndex );
        if( !CHILDREN_NESTING.containsKey( AdqlUtils.getLocalName( o.schemaType() ) ) ) {
            throw new UnsupportedObjectException( "Object " + o.schemaType().getName() + " does not support Child Nesting." ) ;
        }
        nestingType = o.schemaType() ;
    }
    

    protected void maintainNodeIndex( AdqlNode childNode ) { 
        add( childNode ) ;
//        XmlObject childObject = childNode.getXmlObject() ;
//        XmlObject nested = AdqlUtils.getParent( childObject ) ;
//        int index = getList().indexOf( nested ) ;
//        if( nested == getList().get( getList().size() - 1 ) ) {
//            Object[] obs = AdqlUtils.getArray( nested, getElementName() ) ;
//            for( int i=0; i<obs.length; i++ ) {
//                if( childObject == obs[i] ) {
//                    index += i ;
//                    break ;
//                }
//            }
//        }
//        if( index < 1 )
//            add( childNode ) ; 
//        else
//            insert( childNode, index ) ;
    }
    
    @Override
    protected void build( XmlObject o ) {
        Object[] obs = AdqlUtils.getArray( o, getElementName() ) ;
        SchemaType type = getXmlObject().schemaType() ;
        if( obs != null ) {
            for( int i=0; i<obs.length; i++ ) {
                if( AdqlUtils.areTypesEqual( type, ((XmlObject)obs[i]).schemaType()  ) ) {
                    build( (XmlObject)obs[i] ) ;
                } 
                else {
                    nodeFactory.newInstance( this, (XmlObject)obs[i] ) ;
                }
            }
        }
    }
   
    @Override
    public AdqlNode insert( CommandInfo ci, XmlObject source, boolean before ) {
        AdqlNode newInstance = null ;
        if( getChildCount() <= 1 ) {            
            newInstance = super.insert( ci, source, before ) ;           
        }
        else {
            AdqlNode targetNode = ci.getChildEntry() ;
            XmlObject nestedObject = AdqlUtils.getParent( ci.getChildObject() ) ;
            int index = super.getIndex( ci.getChildEntry() ) ;
            if( before == false )
                index++ ;
            newInstance = nodeFactory.newInstance( this, add( ci, targetNode, nestedObject, before ).set( source ), index ) ;
        }
        return newInstance ;
    }
    
    @Override
    public AdqlNode insert( CommandInfo ci, XmlObject source, int index ) {
        AdqlNode newInstance = null ;
        if( getChildCount() <= 1 ) {            
            newInstance = super.insert( ci, source, index ) ;           
        }
        else {          
            AdqlNode targetNode = null ;
            boolean before = true ;
            if( index == getChildCount() ) {
                before = false ;
                targetNode = getChild( index-1 ) ;
            }
            else {
                targetNode = getChild( index ) ;
            }          
            XmlObject nestedObject = AdqlUtils.getParent( targetNode.getXmlObject() ) ;        
            newInstance = nodeFactory.newInstance( this, add( ci, targetNode, nestedObject, before ).set( source ), index ) ;
        }
        return newInstance ;
    }
    
    @Override
    public AdqlNode insert(CommandInfo ci, XmlObject source) {
        AdqlNode newInstance = null ;
        XmlObject lastNested = getLastNested() ;
        if( AdqlUtils.sizeOfArray( lastNested, ci.getChildElementName() ) < 2 ) {
            newInstance = super.insert( ci, source ) ;
        }
        else {
            newInstance = nodeFactory.newInstance( this, add( ci, lastNested ).set( source ) ) ;
        }
        return newInstance ;
    }
    
    @Override
    public AdqlNode insert( CommandInfo ci ) {
        AdqlNode newInstance = null ;
        XmlObject lastNested = getLastNested() ;
        if( AdqlUtils.sizeOfArray( lastNested, ci.getChildElementName() ) < 2 ) {
            newInstance = super.insert( ci ) ;
        }
        else {
            newInstance = nodeFactory.newInstance( this, add( ci, lastNested ) ) ;
        }
        return newInstance ;
    }
    

    @Override
    public void remove( CommandInfo ci ) {
        if( getLastNested() == getXmlObject() ) {
            super.remove( ci ) ;
            return ;
        }
        boolean check ;
        AdqlNode parentNode = ci.getParentEntry();
        XmlObject preservedSibling = null ;
        Object[] array = AdqlUtils.getArray( AdqlUtils.getParent(ci.getChildObject()), getElementName() ) ;
        for( int i=0; i<array.length; i++ ) {
            if( ci.getChildObject() != array[i] ) {
                preservedSibling = ((XmlObject)array[i]) ;
                break ;
            }
        }
        XmlCursor pCursor = preservedSibling.newCursor() ;
        XmlCursor nCursor = ci.getChildObject().newCursor() ;
        check = nCursor.toParent() ; 
        XmlObject oldNest = nCursor.getObject() ;
        if( oldNest == userObject )
            userObject = null ;
        nCursor.push() ;
        XmlObject pObject = pCursor.getObject() ;
        XmlObject nObject = nCursor.getObject() ;
        check = pCursor.copyXml( nCursor ) ;
        pCursor.dispose() ;
        
        XmlObject rebuildPoint = null ;
        if( userObject == null ){
            if( nCursor.getObject() == oldNest ) {
                check = nCursor.toPrevSibling() ;
            }
            userObject = nCursor.getObject() ;
            rebuildPoint = (XmlObject)userObject ;
            nCursor.pop() ;
            check = nCursor.toParent() ;
        }
        else {
            nCursor.pop() ;
            check = nCursor.toParent() ;
            rebuildPoint = nCursor.getObject() ; 
        }
               
        nCursor.toFirstChild() ; // There has to be a first child!
        XmlObject x ; // x marks the spot!
        do {
            x = nCursor.getObject() ;
            if( x == oldNest ) {
                check = nCursor.removeXml() ; // remove old nest
                break ;
            }
        } while( nCursor.toNextSibling() ) ;
        
        nCursor.dispose() ;
        
        if( log.isDebugEnabled() ) {
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);
            log.debug( "NestingNode.remove()...\n" + getXmlObject().toString() ) ;
        }       
        
        // Free all moved nodes...
        ArrayList nodeList = freeNodesAfterRemove( ci ) ;
        
        // Now rebuild nodes for those xmlobjects that were forced
        // into new positions by the remove...
        rebuildMovedNodesAfterRemove( ci, rebuildPoint, nodeList ) ;
            
    }  
    
    private ArrayList freeNodesAfterRemove( CommandInfo ci) {
        AdqlNode removedNode = ci.getChildEntry() ;
        ArrayList nodeList = new ArrayList() ;
        int childrenSize = children.size() ;
        int indexRemovedNode = children.indexOf( removedNode ) ;
        // This first check is to see whether the removed child
        // was one of the last two in the nest...
        // (If so, then we need to free the last two, or three if
        // there are three or more child nodes,
        // otherwise we need to free the targetted child
        // node and everything to its right )
        if( indexRemovedNode >= childrenSize-2 ) {
            if( childrenSize >= 3 ) {
                nodeList.add( children.elementAt( childrenSize-3 ) ) ;
            }          
            if( indexRemovedNode == childrenSize-1 ){
                nodeList.add( children.elementAt( childrenSize-2 ) ) ;
            }
            else {
                nodeList.add( children.elementAt( childrenSize-1 ) ) ;
            }      
        }
        else {       
            AdqlNode node = null ;
            Enumeration e = children() ;
            boolean found = false ;
            int index = 0 ;
            while( e.hasMoreElements() ) {
                node = (AdqlNode)e.nextElement() ;
                if( node == removedNode ){
                    found = true ;
                    if( index > 0 ){
                        nodeList.add( children.elementAt( index-1) ) ;
                    }
                }
                else if( found ) {
                    nodeList.add( node ) ;
                }
                index++ ;
            }          
        }
        if( nodeList.size() > 0 )
            children.removeAll( nodeList ) ;
        children.remove( removedNode ) ;
        return nodeList ;
    }
    

    private XmlObject add( CommandInfo ci, XmlObject lastNested ) {
        AdqlNode parentNode = ci.getParentEntry() ;
        // Make a space for the new nested object...
        AdqlNode disconnectedChildNode = ((AdqlNode)this.getChildAt( this.getChildCount()-1 ) ) ;
        XmlObject disconnectedObject = disconnectedChildNode.getXmlObject().copy() ;
        AdqlUtils.removeFromArray( lastNested, ci.getChildElementName(), 1 ) ;
        disconnectedChildNode.removeFromParent() ;

        // Create a new nested object...
        XmlObject nextNested = AdqlUtils.addNewToEndOfArray( lastNested, ci.getChildElementName() ).changeType( lastNested.schemaType() ) ;
        // Reknit the old and new to the new nested object...
        disconnectedObject = AdqlUtils.addNewToEndOfArray( nextNested, ci.getChildElementName() ).set( disconnectedObject ) ;
        nodeFactory.newInstance( parentNode, disconnectedObject ) ;
        XmlObject newObject = AdqlUtils.addNewToEndOfArray( nextNested, ci.getChildElementName() ) ;
        newObject = newObject.changeType( ci.getChildType() ) ;
        newObject = AdqlUtils.setDefaultValue( newObject ) ;        
        return newObject ;
    }
    
    
    
    private XmlObject add( CommandInfo ci, AdqlNode targetNode, XmlObject nested, boolean before ) {
        boolean check ;
        XmlObject newObject = null ;
        // Insertion requested before the first child...
        if( this.getFirstChild() == targetNode && before == true ) {
            XmlCursor cursor = targetNode.getXmlObject().newCursor() ;
            check = cursor.toParent() ;
            
            //
            // Remember the nested element qname and the nested that is target to be moved...
            QName qName = cursor.getName() ;
            XmlObject targetNest = cursor.getObject() ;
            
            // Create new nest...
            check = cursor.toParent() ;
            XmlCursor.TokenType token = cursor.toEndToken() ;
            cursor.beginElement( qName ) ;  
            cursor.push() ; // remember inside of new nest
            check = cursor.toParent() ;
            XmlObject newNest = cursor.getObject().changeType( targetNest.schemaType() ) ;
            
            // Create the inserted object...
            newObject = AdqlUtils.addNewToEndOfArray( newNest, ci.getChildElementName() ).changeType( ci.getChildType() ) ;
            
            // Recover position on inside of new nest
            check = cursor.pop() ;
            // Then move the targetted nest to its new spot...
            XmlCursor sourcePoint = targetNest.newCursor() ;
            check = sourcePoint.moveXml( cursor ) ;
            sourcePoint.dispose() ;
            cursor.dispose() ;
            
            this.userObject = newNest ;
        }
        
        else if( this.getLastChild() == targetNode ) {
            
            // Insertion requested before the last child...
            if( before == true ) {
                XmlCursor cursor = targetNode.getXmlObject().newCursor() ;
                check = cursor.toParent() ;
                // Remember element name for nest...
                QName qName = cursor.getName() ;
                // Create new nest within targetNodes current parent nest...
                XmlCursor.TokenType token = cursor.toEndToken() ;
                cursor.beginElement( qName ) ;  
                cursor.push() ; // remember inside of new nest
                check = cursor.toParent() ;
                XmlObject newNest = cursor.getObject().changeType( nestingType ) ;
                
                // Create the inserted object...
                newObject = AdqlUtils.addNewToEndOfArray( newNest, ci.getChildElementName() ).changeType( ci.getChildType() ) ;
                
                // Recover position on inside of new nest
                check = cursor.pop() ;
                // Then move the targetted nest to its new spot...
                XmlCursor sourcePoint = targetNode.getXmlObject().newCursor() ;
                check = sourcePoint.moveXml( cursor ) ;
                sourcePoint.dispose() ;
                cursor.dispose() ;
            }
            // Insertion requested after the last child...
            else {
                XmlCursor cursor = targetNode.getXmlObject().newCursor() ;
                check = cursor.toParent() ;
                QName qName = cursor.getName() ;
                // Create new nest within targetNodes current parent...
                XmlCursor.TokenType token = cursor.toEndToken() ;
                cursor.beginElement( qName ) ;  
                cursor.push() ; // remember inside of new nest
                check = cursor.toParent() ;
                XmlObject newNest = cursor.getObject().changeType( nestingType ) ;
                   
                // Recover position on inside of new nest
                check = cursor.pop() ;
                // Then move the targetted nest to its new spot...
                XmlCursor sourcePoint = targetNode.getXmlObject().newCursor() ;
                check = sourcePoint.moveXml( cursor ) ;
                sourcePoint.dispose() ;
                cursor.dispose() ;
                // Create the inserted object...
                newObject = AdqlUtils.addNewToEndOfArray( newNest, ci.getChildElementName() ).changeType( ci.getChildType() ) ;
             
            }
            
        }
        else {
            //Turn all afters into befores ...
            if( before == false ) {
                targetNode = (AdqlNode)children.elementAt( children.indexOf( targetNode ) + 1 ) ;
            }
                XmlCursor cursor = targetNode.getXmlObject().newCursor() ;
                check = cursor.toParent() ;
                
                //
                // Remember the nested element qname and the nested that is target to be moved...
                QName qName = cursor.getName() ;
                XmlObject targetNest = cursor.getObject() ;
                
                // Create new nest...
                check = cursor.toParent() ;
                XmlCursor.TokenType token = cursor.toEndToken() ;
                cursor.beginElement( qName ) ;  
                cursor.push() ; // remember inside of new nest
                check = cursor.toParent() ;
                XmlObject newNest = cursor.getObject().changeType( targetNest.schemaType() ) ;
                
                // Create the inserted object...
                newObject = AdqlUtils.addNewToEndOfArray( newNest, ci.getChildElementName() ).changeType( ci.getChildType() ) ;
                
                // Recover position on inside of new nest
                check = cursor.pop() ;
                // Then move the targetted nest to its new spot...
                XmlCursor sourcePoint = targetNest.newCursor() ;
                check = sourcePoint.moveXml( cursor ) ;
                sourcePoint.dispose() ;
                cursor.dispose() ;
        }
        
        // Free all moved nodes...
        Enumeration e = children() ;
        AdqlNode node ;
        ArrayList nodeList = new ArrayList() ;
        boolean found = false ;
        while( e.hasMoreElements() ) {
            node = (AdqlNode)e.nextElement() ;
            if( node == targetNode ) {
                found = true ;
                nodeList.add( node ) ; // JL: experiment
            }
            else if( found ) {
                nodeList.add( node ) ;
            }
        }
        children.removeAll( nodeList ) ;
        //children.remove( targetNode ) ;
        
        // Then rebuild the moved nodes...
        rebuildMovedNodesAfterInsert( ci, newObject, nodeList ) ;
        
        return newObject ;
    }
    
    
    private void rebuildMovedNodesAfterInsert(  CommandInfo ci, XmlObject newObject, ArrayList oldNodes ) {
        ArrayList newNodes = new ArrayList() ;
        XmlCursor cursor = newObject.newCursor() ;
        XmlObject xmlObject = null ;
        QName qualifiedName ;
        int depth = 0;
        boolean check = cursor.toNextSibling() ;
        if( check ) {
            do {
                if( cursor.isStart() ) {
                    depth++ ;
                    qualifiedName = cursor.getObject().schemaType().getName() ;
                    if( qualifiedName != null && !qualifiedName.equals( nestingType.getName() ) ) {
                        xmlObject = cursor.getObject() ;
                        newNodes.add( nodeFactory.newInstance( this, xmlObject ) );
                        check = cursor.toNextSibling() ;
                        continue ;
                    } 
                } 
                else if( cursor.isEnd() ){
                    depth-- ;
                }
                check = ( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
            } while( check && depth>-1) ;
        } 
        else {
            // The one special situation where we are adding a new
            // object at the end, and so need to rebuild the previous
            // end object (which has been moved to pair it with its new sibling)...
            check = cursor.toPrevSibling() ; // Point to previous end object
            xmlObject = cursor.getObject() ; // Get the previous end object
            newNodes.add( nodeFactory.newInstance( this, xmlObject ) ) ; // Rebuild its node
        }
        cursor.dispose() ;
        updateEditStore( ci, newNodes, oldNodes ) ;
    }
    
    
    private void rebuildMovedNodesAfterRemove( CommandInfo ci, XmlObject rebuildPoint, ArrayList oldNodes ) {
        ArrayList newNodes = new ArrayList() ;
        XmlCursor cursor = rebuildPoint.newCursor() ;
        XmlObject xmlObject = null ;
        QName qualifiedName ;
        int depth = 0;
        boolean check = cursor.toFirstChild() ;
        if( check ) {
            do {
                if( cursor.isStart() ) {
                    depth++ ;
                    qualifiedName = cursor.getObject().schemaType().getName() ;
                    if( qualifiedName != null ) {
                        if( !qualifiedName.equals( nestingType.getName() ) ) {
                            xmlObject = cursor.getObject() ;
                            newNodes.add( nodeFactory.newInstance( this, xmlObject ) ) ;
                            check = cursor.toNextSibling() ;
                            depth-- ;
                            continue ;
                        }
                    } 
                } 
                else if( cursor.isEnd() ){
                    depth-- ;
                }
                check = ( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
            } while( check && depth>-1) ;
        } 
        cursor.dispose() ;
        updateEditStore( ci, newNodes, oldNodes ) ;
    }
    
    private void updateEditStore( CommandInfo ci, ArrayList newNodes, ArrayList oldNodes ) {
        if( oldNodes.size() > 0 ) {
            ListIterator newIterator = newNodes.listIterator() ;
            ListIterator oldIterator = oldNodes.listIterator() ;
            while( oldIterator.hasNext() ) {
                AdqlNode oldNode = (AdqlNode)oldIterator.next() ;
                AdqlNode newNode = (AdqlNode)newIterator.next() ;
                ci.exchangeInEditStore( newNode, oldNode ) ;
            }
        }
    }
    
    private XmlObject getLastNested() {
        XmlObject retObject = getXmlObject() ;
        QName qualifiedName ;
        XmlCursor cursor = retObject.newCursor() ;
        int depth = 0 ;
        boolean check = cursor.toLastChild() ;  
        if( check ) {
            do {
                if( cursor.isStart() ) {
                    depth++ ;
                    qualifiedName = cursor.getObject().schemaType().getName() ;
                    if( qualifiedName != null && qualifiedName.equals( nestingType.getName() ) ) {
                        retObject = cursor.getObject() ;
                        check = cursor.toLastChild() ;
                        continue ;
                    } 
                }  
                else if( cursor.isEnd() ){
                    depth-- ;
                }
                check = ( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
            } while( check && depth>-1 ) ;
        } 
        cursor.dispose() ;
        return retObject ;
    }
    
    
    private boolean getArrayContext() {
        if( arrayContext == null ){
            int minOccurs = getElementProperty().getMinOccurs().intValue() ;
            BigInteger biMaxOccurs = getElementProperty().getMaxOccurs() ;
            int maxOccurs = ( biMaxOccurs == null ? -1 : biMaxOccurs.intValue() ) ;
            if( maxOccurs == -1  ||  maxOccurs > 1 )
                arrayContext = Boolean.TRUE;
            else
                arrayContext =Boolean.FALSE;
        }
        return arrayContext.booleanValue() ;
    }
    
    private SchemaProperty getElementProperty() {
        if( elementProperty == null ) {
            SchemaProperty[] elements = AdqlUtils.getParent( getXmlObject() ).schemaType().getElementProperties() ; ;
            for( int i=0; i<elements.length; i++ ) {
                if( elements[i].getType().isAssignableFrom( getXmlObject().schemaType() ) ) {
                    elementProperty = elements[i] ;
                    break ;
                }
            }  
        }
        return elementProperty;
    }
    
    private String getElementName(){
        return getElementProperty().getName().getLocalPart() ;
    }
    
    @Override
    public String toHtml( boolean expanded, boolean leaf, AdqlTree tree ) { 
        String retValue = super.toHtml( expanded, leaf, tree ) ;
        return retValue ;
    }
    
}
