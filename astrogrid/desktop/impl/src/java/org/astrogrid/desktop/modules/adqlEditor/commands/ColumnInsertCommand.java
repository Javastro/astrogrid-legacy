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
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ColumnInsertCommand extends StandardInsertCommand {
  
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
    
    @Override
    public void setSelectedValue( String value ) {
        setColumnName( value ) ;
    }
    
    public TableBean getTable() {
        return table ;
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
    
    @Override
    public Result execute() {
        removeAllColumnsOption() ;
        return super.execute() ;
    }
    
    
    @Override
    protected Result _execute() {
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
                    AdqlUtils.set( getChildObject()
                                 , "table"
                                 , XmlString.Factory.newValue( tableName ) ) ;
                } 
                String colName = columnName ;
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
    
    @Override
    protected Result _unexecute() {
        Result result = super._unexecute() ;
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
   
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer() ;
        buffer.append( "\nColumnInsertCommand" ) ;
        buffer.append( super.toString() ) ;
        buffer
            .append( "\ncolumnName: ").append( columnName )
            .append( "\ntable: " ).append( table.getName() )
            .append( "\ntableAlias: " ).append( tableAlias ) ;
        return buffer.toString() ; 
    }  
}