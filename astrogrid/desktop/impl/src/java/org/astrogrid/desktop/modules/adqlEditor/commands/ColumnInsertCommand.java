/* ColumnInsertCommand.java
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

import javax.swing.undo.UndoManager;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
import org.astrogrid.desktop.modules.adqlEditor.nodes.NodeFactory;

/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ColumnInsertCommand extends StandardInsertCommand {
    
    protected Catalog archive ;
    protected TableBean table ;
    protected String columnName ;
    protected String tableAlias ;

   
    /**
     * @param target
     * @param 
     */
    public ColumnInsertCommand( AdqlTree adqlTree
                              , UndoManager undoManager
                              , AdqlNode parentTarget 
                              , SchemaType childType
                              , SchemaProperty childElement ) {
        super( adqlTree, undoManager, parentTarget, childType, childElement ) ;
    }
    
    public ColumnInsertCommand( ColumnInsertCommand cic ) {
        super( cic.adqlTree, cic.undoManager, cic.getParentEntry(), cic.childType, cic.childElement ) ;
        this.archive = cic.archive ;
        this.table = cic.table ;
        this.columnName = cic.columnName ;
        this.tableAlias = cic.tableAlias ;
    }
    
    public String getColumnName() {
        return this.columnName ;
    }
    
    public void setColumnName( String columnName ) {
        this.columnName = columnName ;
    }
    
    public void setSelectedValue( String value ) {
        setColumnName( value ) ;
    }
    
    public TableBean getTable() {
        return table ;
    }
    
    public Catalog getArchive() {
        return this.archive ;
    }
    
    public void setArchive( Catalog archive ) {
        this.archive = archive ;
    }
    
    public void setTable( TableBean tableBean ) {
        this.table = tableBean ;
    }
    
    public String getTableAlias() {
        return tableAlias ;
    }
    
    public void setTableAlias( String alias ) {
        this.tableAlias = alias ;
    }
    
    public Result execute() {
        removeAllColumnsOption() ;
        return super.execute() ;
    }
    
    
    protected Result _execute() {
//        removeAllColumnsOption() ;
        Result result = super._execute() ;
        if( result != CommandExec.FAILED ) {
            try {          
                if( tableAlias != null ) {
                    AdqlUtils.set( getChildObject()
                                 , "table"
                                 , XmlString.Factory.newValue( tableAlias ) ) ;
                }
                else {
                    String tableName = table.getName() ;
//                    if( !AdqlUtils.isRegularIdentifier( tableName.toUpperCase() ) ) {
//                        tableName = "\"" +tableName + "\""  ;
//                    }
                    AdqlUtils.set( getChildObject()
                                 , "table"
                                 , XmlString.Factory.newValue( tableName ) ) ;
                } 
                String colName = columnName ;
//                if( !AdqlUtils.isRegularIdentifier( colName.toUpperCase() ) ) {
//                    colName = "\"" +colName + "\""  ;
//                }
                AdqlUtils.set( getChildObject()
                             , "name"
                             , XmlString.Factory.newValue( colName ) ) ;
            }
            catch( Exception exception ) {
                result = CommandExec.FAILED ;
            }
        } 
        return result ;
    }
    
    protected Result _unexecute() {
        Result result = super._unexecute() ;
//        reinstateAllColumnsOption() ;
        return result ;
    }
    
    private void removeAllColumnsOption() { 
        XmlObject o = getParentObject() ;
        if( !AdqlUtils.areTypesEqual( getParentEntry().getSchemaType()
                                    , AdqlUtils.getType( o, AdqlData.SELECTION_LIST_TYPE ) ) ) 
           return ;
        int arraySize = AdqlUtils.sizeOfArray( o, "Item" ) ;
        if( arraySize == 1 ) {
            XmlObject item = (XmlObject)AdqlUtils.getArray( o, getChildElementName(), 0 ) ;
            String name = item.schemaType().getName().getLocalPart() ;
            if( name.equals( AdqlData.ALL_SELECTION_ITEM_TYPE ) ) {
                AdqlNode entry = getParentEntry().getChild( 0 ) ;           
                CutCommand cutCommand = new CutCommand( adqlTree, undoManager, entry ) ;
                cutCommand.execute() ;
            }
        }
    }
    
    private void _reinstateAllColumnsOption() {
        XmlObject o = getParentObject() ;
        if( !AdqlUtils.areTypesEqual( getParentEntry().getSchemaType()
                                    , AdqlUtils.getType( o, AdqlData.SELECTION_LIST_TYPE ) ) ) 
           return ;
        int arraySize = AdqlUtils.sizeOfArray( o, "Item" ) ;
        if( arraySize == 0 ) {
            XmlObject newObject = AdqlUtils.addNewToEndOfArray( o, getChildElementName() ) ;
            newObject = newObject.changeType( AdqlUtils.getType( o, AdqlData.ALL_SELECTION_ITEM_TYPE ) ) ;
            adqlTree.getNodeFactory().newInstance( getParentEntry(), newObject ) ;
        }
    }
   
        
}