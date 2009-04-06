/* EditSingletonTextCommand.java
 * Created on 17-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree.TableData;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditTupleTextCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( EditTupleTextCommand.class ) ;
    private HashMap<String, TableData> fromTables ;
    private String[] oldValues ;
    private String[] newValues ;
    private ArrayList<Object> updates ;
    private ArrayList<Object> references ;
    private ArrayList<AdqlNode> typeList ;
    
private class UpdateControls {
        
        public Integer token ;
        public String attributeName ;
        public String oldValue ;
        public String newValue ;
        
        UpdateControls( Integer token
                      , String attributeName
                      , String oldValue
                      , String newValue ) {
            this.token = token ;
            this.attributeName = attributeName ;
            this.oldValue = oldValue ;
            this.newValue = newValue ;
         }
    }
    
    private class ColumnReference {
        
        public Integer token ;
        public String attributeName ;
        public String oldValue ;
        public String newValue ;
        public String tableName ;
        
        ColumnReference( Integer token
                       , String attributeName
                       , String oldValue
                       , String newValue
                       , String tableName ) {
            this.token = token ;
            this.attributeName = attributeName ;
            this.oldValue = oldValue ;
            this.newValue = newValue ;
            this.tableName = tableName ;
         }
    }

    /**
     * @param target
     * @param source
     */
    public EditTupleTextCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlNode target, HashMap<String, TableData> fromTables ) {
        super( adqlTree, undoManager, target ) ;  
        this.fromTables = fromTables ;
        // NB: The way I have used target is acceptable ONLY in the constructor.
        // Elsewhere it must be accessed via the command methods!!!
        XmlObject xmlObject = target.getXmlObject() ;
        XmlObject attribute = null ;
        try {
            String[] attrNames = AdqlUtils.getEditableAttributes( xmlObject ) ;
            oldValues = new String[ attrNames.length ] ;
            newValues = new String[ attrNames.length ] ;
            for( int i=0; i<attrNames.length; i++ ) {
                attribute = AdqlUtils.get( xmlObject, attrNames[i] ) ;
                if( attribute == null ) {
                    oldValues[i] = "" ;
                }
                else {
                    oldValues[i] = ((SimpleValue)attribute).getStringValue() ;
                }                       
            }
        }
        catch( Exception ex ) {
            oldValues = new String[] { "", "" } ;
            newValues = new String[ 2 ] ;
            log.warn( ex ) ;
        }
        
    }
    
    public void setNewValues( String[] values ) {
        if( values == null ) {
            this.newValues = new String[] { "", "" } ;
            return ;
        }
        int len = ( values.length > this.newValues.length ?  this.newValues.length : values.length ) ;
        for( int i=0; i<len; i++ ) {
            this.newValues[i] = values[i] ;
            if( this.newValues[i] == null || this.newValues[i].trim().length()==0 ) {
                this.newValues[i] = "" ;
            }
        }
    }
    
    public void setSelectedValues( String[] values ) {
        setNewValues( values ) ;
    }
        
    @Override
    public Result execute() {   
        Result result = _execute() ;
        if( result != CommandExec.FAILED )
            undoManager.addEdit( this ) ;
        return result ;
    }
   
    private Result _execute() {
        Result result = CommandExec.OK ;
        try {     
            XmlObject element = getChildObject() ;
            String[] attributeNames = AdqlUtils.getEditableAttributes( AdqlUtils.getLocalName( element ) ) ;
            
            for( int i=0; i<attributeNames.length; i++ ) {                        
                boolean bOptional = AdqlUtils.isOptionalAttribute( element, attributeNames[i] ) ;
                if( newValues[i] == null || newValues[i].length()==0 ) {
                   if( bOptional
                       &&
                       AdqlUtils.isSet( element, attributeNames[i] ) 
                       && 
                       preValidateOK( element, attributeNames[i], bOptional, oldValues[i], newValues[i] ) ) {
                       AdqlUtils.unset( element, attributeNames[i] ) ;
                       getUpdates().add( new UpdateControls( getChildToken(), attributeNames[i], oldValues[i], newValues[i] ) ) ;
                       crossValidateDelete( element, attributeNames[i], oldValues[i], newValues[i] ) ;
                   }
                   else {
                       ;  // Cannot delete a non-optional attribute. We do nothing, ignoring any changes.
                   }
                }
                else if( ( oldValues[i] == null || oldValues[i].length()==0 ) 
                         && 
                         preValidateOK( element, attributeNames[i], bOptional, oldValues[i], newValues[i] ) ) {                          
                    AdqlUtils.set( element, attributeNames[i], XmlString.Factory.newValue( newValues[i] ) ) ;
                    getUpdates().add( new UpdateControls( getChildToken(), attributeNames[i], oldValues[i], newValues[i] ) ) ;
                    crossValidateCreate(element, attributeNames[i], oldValues[i], newValues[i] ) ;                                 
                }
                else if( !areEqual( oldValues[i], newValues[i] )
                         && 
                         preValidateOK( element, attributeNames[i], bOptional, oldValues[i], newValues[i] ) ) {              
                    AdqlUtils.set( element, attributeNames[i], XmlString.Factory.newValue( newValues[i] ) ) ;
                    getUpdates().add( new UpdateControls( getChildToken(), attributeNames[i], oldValues[i], newValues[i] ) ) ;
                    crossValidateUpdate(element, attributeNames[i], oldValues[i], newValues[i] ) ;                         
                }                  
            }
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.debug( "EditTupleTextCommand._execute() failed.", exception ) ;
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
        }
        finally {
            resetTypeList() ;
        }
        return result ;    
    }
    
    private ArrayList<Object> getReferences() {
        if( references == null ) {
            references = new ArrayList<Object>() ;
        }
        return references ;
    }
     
    private ArrayList<Object> getUpdates() {
        if( updates == null ) {
            updates = new ArrayList<Object>() ;
        }
        return updates ;
    }
    
    @Override
    public void die() {
        super.die();
    }
    
    
    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        try {
            UpdateControls uc = null ;
            ListIterator<Object> it = getUpdates().listIterator() ;
            while( it.hasNext() ) {
                uc = (UpdateControls)it.next() ;
                if( uc.newValue == null || uc.newValue.length()==0 ) {
                    AdqlUtils.unset( adqlTree.getCommandFactory().getEditStore().get( uc.token ).getXmlObject()
                                   , uc.attributeName ) ;
                }
                else {
                    AdqlUtils.set( adqlTree.getCommandFactory().getEditStore().get( uc.token ).getXmlObject()
                                 , uc.attributeName
                                 , XmlString.Factory.newValue( uc.newValue ) ) ;
                }
            }
            if( references != null ) {
                ColumnReference cr = null ;
                it = getReferences().listIterator() ;
                while( it.hasNext() ) {
                    cr = (ColumnReference)it.next() ;  
                    if( cr.newValue == null || cr.newValue.length()==0 ) {               
                        AdqlUtils.set( adqlTree.getCommandFactory().getEditStore().get( cr.token ).getXmlObject()
                                , cr.attributeName
                                , XmlString.Factory.newValue( cr.tableName ) ) ;    
                    }
                    else {
                        AdqlUtils.set( adqlTree.getCommandFactory().getEditStore().get( cr.token ).getXmlObject()
                                , cr.attributeName
                                , XmlString.Factory.newValue( cr.newValue ) ) ;   
                    }
                }
                if( cr.newValue == null || cr.newValue.length()==0 ) {
//                    ((TableData)fromTables.get( cr.tableName ) ).alias = null ;
                }
                else {
                    fromTables.get( cr.tableName ).addAlias( cr.newValue ) ;
                }   
            }           
        }
        catch( Exception ex ) {
            log.debug( "EditTupleTextCommand.redo() failed.", ex ) ;
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
            throw new CannotRedoException() ;
        }
    }
    
    public void _redo() throws CannotRedoException {
        super.redo();
        try {
            UpdateControls uc = null ;
            ListIterator<Object> it = getUpdates().listIterator() ;
            while( it.hasNext() ) {
                uc = (UpdateControls)it.next() ;
                if( uc.newValue == null || uc.newValue.length()==0 ) {
                    AdqlUtils.unset( adqlTree.getCommandFactory().getEditStore().get( uc.token ).getXmlObject()
                                   , uc.attributeName ) ;
                }
                else {
                    AdqlUtils.set( adqlTree.getCommandFactory().getEditStore().get( uc.token ).getXmlObject()
                                 , uc.attributeName
                                 , XmlString.Factory.newValue( uc.newValue ) ) ;
                }
            }
            if( references != null ) {
                ColumnReference cr = null ;
                it = getReferences().listIterator() ;
                while( it.hasNext() ) {
                    cr = (ColumnReference)it.next() ;         
                    AdqlUtils.set( adqlTree.getCommandFactory().getEditStore().get( cr.token ).getXmlObject()
                                 , cr.attributeName
                                 , XmlString.Factory.newValue( cr.newValue ) ) ;    
                }
                if( cr.newValue == null || cr.newValue.length()==0 ) {
//                    ((TableData)fromTables.get( cr.tableName ) ).alias = null ;
                }
                else {
                    fromTables.get( cr.tableName ).addAlias( cr.newValue ) ;
                }   
            }           
        }
        catch( Exception ex ) {
            log.debug( "EditTupleTextCommand.redo() failed.", ex ) ;
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
            throw new CannotRedoException() ;
        }
    }
    
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        try {
            UpdateControls uc = null ;
            ListIterator<Object> it = getUpdates().listIterator() ;
            while( it.hasNext() ) {
                uc = (UpdateControls)it.next() ;
                if( uc.oldValue == null || uc.oldValue.length()==0 ) {
                    AdqlUtils.unset( adqlTree.getCommandFactory().getEditStore().get( uc.token ).getXmlObject()
                                   , uc.attributeName ) ;
                }
                else {
                    AdqlUtils.set( adqlTree.getCommandFactory().getEditStore().get( uc.token ).getXmlObject()
                                 , uc.attributeName
                                 , XmlString.Factory.newValue( uc.oldValue ) ) ;
                }
            }
            if( references != null ) {
                ColumnReference cr = null ;
                it = getReferences().listIterator() ;
                while( it.hasNext() ) {
                    cr = (ColumnReference)it.next() ;     
                    if( cr.oldValue == null || cr.oldValue.length()==0 ) {
                        AdqlUtils.set( adqlTree.getCommandFactory().getEditStore().get( cr.token ).getXmlObject()
                                , cr.attributeName
                                , XmlString.Factory.newValue( cr.tableName ) ) ;  
                    }
                    else {
                        AdqlUtils.set( adqlTree.getCommandFactory().getEditStore().get( cr.token ).getXmlObject()
                                , cr.attributeName
                                , XmlString.Factory.newValue( cr.oldValue ) ) ;  
                    }
                }
                if( cr.oldValue == null || cr.oldValue.length()==0 ) {
//                    ((TableData)fromTables.get( cr.tableName ) ).alias = null ;
                }
                else {
                    fromTables.get( cr.tableName ).addAlias( cr.oldValue ) ;
                }               
            }            
        }
        catch( Exception ex ) {
            log.debug( "EditTupleTextCommand.undo() failed.", ex ) ;
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
            throw new CannotUndoException() ;
        }
    }
    
    @Override
    public String getPresentationName() {
        return "Edit" ;
    }

    /**
     * @return the oldValue
     */
    public String[] getOldValues() {
        return oldValues;
    }

    /**
     * @param oldValue the oldValue to set
     */
    public void setOldValue(String[] oldValues) {
        this.oldValues = oldValues;
    }

    /**
     * @return the newValue
     */
    public String[] getNewValues() {
        return newValues;
    }
    
    private boolean areEqual( String oldValue, String newValue ) {
        if( oldValue == null && newValue == null )
            return true ;
        return String.valueOf( oldValue ).equals( newValue ) ;
    }
    
    private boolean preValidateOK( XmlObject element
                                 , String attributeName
                                 , boolean attributeOptional
                                 , String oldValue
                                 , String newValue ) {
        if( isTableValidationRequired( element, attributeName ) )
            return preValidateTable( element, attributeName, attributeOptional, oldValue, newValue ) ;
        return true ;
    }
    
    private void crossValidateDelete( XmlObject element
                                    , String attributeName
                                    , String oldValue
                                    , String newValue ) {
        if( isTableValidationRequired( element, attributeName ) )
            crossValidateTableDelete( element, attributeName, oldValue, newValue ) ;
    }
    
    private void crossValidateUpdate( XmlObject element
                                    , String attributeName
                                    , String oldValue
                                    , String newValue ) {
        if( isTableValidationRequired( element, attributeName ) )
            crossValidateTableUpdate( element, attributeName, oldValue, newValue ) ;
    }
    
    private void crossValidateCreate( XmlObject element
                                    , String attributeName
                                    , String oldValue
                                    , String newValue ) {
        if( isTableValidationRequired( element, attributeName ) )
            crossValidateTableCreate( element, attributeName, oldValue, newValue ) ;
    }
    
    private boolean isTableValidationRequired( XmlObject element, String attributeName ) {
        String elementTypeName = AdqlUtils.getLocalName( element ) ;
        if( AdqlData.METADATA_LINK_TABLE.containsKey( elementTypeName ) ) {
            String[] attrNames = AdqlData.CROSS_VALIDATION.get( elementTypeName ) ;
            for( int i=0; i<attrNames.length; i++ ) {
                if( attrNames[i].equals( attributeName ) )
                    return true ;
            }
        }
        return false ;
    }
    
    // As currently defined this is the deletion of the alias for a table.
    // We go through the query, changing every reference to this table back
    // to its full name rather than the alias.
    // eg: from a.ra to hipass.ra
    private void crossValidateTableDelete( XmlObject tableElement
                                         , String attributeName
                                         , String oldValue
                                         , String newValue  ) {       
        String tableName = ((SimpleValue)AdqlUtils.get( tableElement, "name" )).getStringValue() ;
        ListIterator<AdqlNode> iterator = getTypeList( AdqlData.COLUMN_REFERENCE_TYPE ).listIterator() ;
        if( iterator.hasNext() ) {
            XmlObject xmlTableName = XmlString.Factory.newValue( tableName ) ;
            AdqlNode node ;
            XmlObject element ;
            CommandFactory.EditStore editStore = adqlTree.getCommandFactory().getEditStore() ;
            while( iterator.hasNext() ) {
                node = iterator.next() ;
                element = node.getXmlObject() ;
                if( ((SimpleValue)AdqlUtils.get( element, "table" )).getStringValue().equals( oldValue ) ) {
                    AdqlUtils.set( element, "table", xmlTableName ) ;
                    getReferences().add( new ColumnReference( editStore.add( node ) 
                                                            , "table"
                                                            , oldValue
                                                            , newValue
                                                            , tableName ) ) ;
                }
            }
        }       
        fromTables.get( tableName ).removeAlias( oldValue ) ;
    }
    
    
    private void crossValidateTableUpdate( XmlObject tableElement
                                         , String attributeName
                                         , String oldAliasValue
                                         , String newAliasValue  ) {
        String tableName = ((SimpleValue)AdqlUtils.get( tableElement, "name" )).getStringValue() ;
        ListIterator<AdqlNode> iterator = getTypeList( AdqlData.COLUMN_REFERENCE_TYPE ).listIterator() ;
        if( iterator.hasNext() ) {
            XmlObject xmlTableName = XmlString.Factory.newValue( newAliasValue ) ;
            AdqlNode node ;
            XmlObject element ;
            CommandFactory.EditStore editStore = adqlTree.getCommandFactory().getEditStore() ;
            while( iterator.hasNext() ) {
                node = iterator.next() ;
                element = node.getXmlObject() ;
                if( ((SimpleValue)AdqlUtils.get( element, "table" )).getStringValue().equals( oldAliasValue ) ) {
                    AdqlUtils.set( element, "table", xmlTableName ) ;
                    getReferences().add( new ColumnReference( editStore.add( node ) 
                                                            , "table"
                                                            , oldAliasValue
                                                            , newAliasValue
                                                            , tableName ) ) ;
                }
            }
        } 
        fromTables.get( tableName ).removeAlias( oldAliasValue ) ;
        fromTables.get( tableName ).addAlias( newAliasValue ) ;
    }
    
    
    private void crossValidateTableCreate( XmlObject tableElement
                                         , String attributeName
                                         , String oldAliasValue
                                         , String newAliasValue  ) {
        String tableName = ((SimpleValue)AdqlUtils.get( tableElement, "name" )).getStringValue() ;
        ListIterator<AdqlNode> iterator = getTypeList( AdqlData.COLUMN_REFERENCE_TYPE ).listIterator() ;
        if( iterator.hasNext() ) {
            XmlObject xmlTableName = XmlString.Factory.newValue( newAliasValue ) ;
            AdqlNode node ;
            XmlObject element ;
            CommandFactory.EditStore editStore = adqlTree.getCommandFactory().getEditStore() ;
            while( iterator.hasNext() ) {
                node = iterator.next() ;
                element = node.getXmlObject() ;
                if( ((SimpleValue)AdqlUtils.get( element, "table" )).getStringValue().equals( tableName ) ) {
                    AdqlUtils.set( element, "table", xmlTableName ) ;
                    getReferences().add( new ColumnReference( editStore.add( node ) 
                                                            , "table"
                                                            , oldAliasValue
                                                            , newAliasValue
                                                            , tableName ) ) ;
                }
            }
        }    
        fromTables.get( tableName ).addAlias( newAliasValue ) ;
    }
    
    
    // Changing a table alias to one already in use will corrupt the query!
    private boolean preValidateTable( XmlObject tableElement
                                    , String attributeName
                                    , boolean attributeOptional
                                    , String oldAliasValue
                                    , String newAliasValue  ) {
        if( newAliasValue == null )
            return true ;
        boolean retValue = true ;
        ArrayList<XmlObject> tableList = new ArrayList<XmlObject>(); 
        XmlObject xmlAliasName = null ;
        XmlObject element = null ;
        XmlCursor cursor = tableElement.newCursor() ;               
        cursor.toStartDoc() ;   // Reposition on the top of the document
        cursor.toFirstChild() ; // And then to the first child!
        do {
            // Only deal with elements...
            if( cursor.isStart() ) {
                element = cursor.getObject() ;
                // We make a collection of all the table elements in a query...
                if( AdqlUtils.getLocalName( element ).equals( AdqlData.TABLE_TYPE ) ) {
                    tableList.add( element ) ;
                }                       
            }                 
        } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
        cursor.dispose() ;
        ListIterator<XmlObject> iterator = tableList.listIterator() ;
        while( iterator.hasNext() ) {
            element = iterator.next() ;
            if( element == tableElement )
                continue ;
            xmlAliasName = AdqlUtils.get( element, attributeName ) ;
            if( xmlAliasName != null && ((SimpleValue)xmlAliasName).getStringValue().equals( newAliasValue ) ) {
                retValue = false ;
                break ;
            }
        }              
        return retValue ;
    }
    
    
//    private Integer findNodeTokenFor( XmlObject xmlObject ) {
//        Integer token = null ;
//        AdqlNode foundNode = null ;
//        foundNode = findNode( (AdqlNode)adqlTree.getModel().getRoot(), xmlObject ) ;
//        if( foundNode != null ) {
//            token = adqlTree.getCommandFactory().getEditStore().get( foundNode ) ;
//            if( token == null ) {
//                token = adqlTree.getCommandFactory().getEditStore().add( foundNode ) ;
//            }
//        }
//        return token ;
//    }
//    
//    private AdqlNode findNode( AdqlNode parent, XmlObject target ) {
//        AdqlNode found = null ;
//        ArrayList list = getTypeList( target ) ;
//        ListIterator iterator = list.listIterator() ;
//        while( iterator.hasNext() ) {
//            AdqlNode node = (AdqlNode)iterator.next() ;
//            if( node.getXmlObject() == target ) {
//                found = node ;
//                break ;
//            }
//        }
//        return found ;
//    }
    

    private ArrayList<AdqlNode> getTypeList( String targetLocalName ) {
        //
        // The first parameter in this call is just any valid type...
        SchemaType type = AdqlUtils.getType( getChildType(), targetLocalName ) ;
        if( typeList == null ) {
            typeList = new ArrayList<AdqlNode>() ;
        }
        if( typeList.isEmpty()
            || 
            !AdqlUtils.areTypesEqual( (XmlObject)typeList.get(0), type ) ) {
            typeList.clear() ;
            DefaultTreeModel defModel = (DefaultTreeModel)adqlTree.getModel() ;
            AdqlNode rootNode = (AdqlNode)defModel.getRoot() ;
            buildTypeList( rootNode, type ) ;
//            buildTypeList( (AdqlNode)((DefaultTreeModel)adqlTree.getModel()).getRoot(), type ) ;
        }
        return typeList ;
    }
    
    private void buildTypeList( AdqlNode parent, SchemaType type ) {
        if( AdqlUtils.areTypesEqual( parent.getXmlObject(), type ) ) {
            typeList.add( parent ) ;
        } 
        AdqlNode[] nodes = parent.getChildren() ;
        for( int i=0; i<nodes.length; i++ ) {
            buildTypeList( nodes[i], type ) ;           
        }       
    }
    
    private void resetTypeList() {
        if( typeList != null ) {
            typeList.clear() ;
            typeList = null ;
        }
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(512) ;
        buffer.append( "\nEditTupleTextCommand" ) ;
        buffer.append( super.toString() ) ;
        if( newValues == null ) {
            buffer.append( "\nnewValues: null" ) ;
        }
        else {
            buffer.append( "\nnewValues:" ) ;
            for( int i=0; i<newValues.length; i++ ) {
                buffer.append('\n').append( i ).append( ": " ).append( newValues[i] ) ;
            }
        }
        if( oldValues == null ) {
            buffer.append( "\noldValues: null" ) ;
        }
        else {
            buffer.append( "\noldValues:" ) ;
            for( int i=0; i<oldValues.length; i++ ) {
                buffer.append('\n').append( i ).append( ": " ).append( oldValues[i] ) ;
            }
        }
        return buffer.toString() ;
    }
   
}