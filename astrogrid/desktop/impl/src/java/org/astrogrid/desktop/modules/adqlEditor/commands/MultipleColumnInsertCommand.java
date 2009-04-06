/* MultipleColumnInsertCommand.java
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

import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlCursor;
import org.astrogrid.adql.v1_0.beans.*;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
//import org.astrogrid.desktop.modules.dialogs.editors.ADQLToolEditorPanel.InsertColumnAction;
//import org.astrogrid.desktop.modules.dialogs.editors.ADQLToolEditorPanel.InsertTableAction;
//import org.astrogrid.desktop.modules.adqlEditor.commands.CommandFactory.UndoManager ;
/**
 * @author Jeff Lusted jl99@star.le.ac.uk
 *
 */
public class MultipleColumnInsertCommand extends AbstractCommand {
    
    private static final Log logger = LogFactory.getLog( MultipleColumnInsertCommand.class ) ;
    
    protected TableBean table ;
    protected ColumnBean[] columns ;
    protected String tableAlias ;
    protected MCIUndoManager internalUndoManager ;
    protected boolean joinRelated = false ;
    
   
    /**
     * @param target
     * @param 
     */
    public MultipleColumnInsertCommand( AdqlTree adqlTree
                                      , UndoManager undoManager
                                      , AdqlNode parentTarget
                                      , SchemaType childType ) {
        super( adqlTree, undoManager, parentTarget, childType, null ) ;
        this.internalUndoManager = new MCIUndoManager() ;
    }
    
    public TableBean getTable() {
        return table ;
    }
    
    public void setTable( TableBean table ) {
        this.table = table ;
    }
    
    public String getTableAlias() {
        return tableAlias ;
    }
    
    public void setTableAlias( String alias ) {
        this.tableAlias = alias ;
    }
    
    public ColumnBean[] getColumns() {
        return columns ;
    }
    
    public void setColumns( ColumnBean[] columns ) {
        this.columns = columns ;
        this.internalUndoManager.columnList = new ArrayList<AdqlNode>( columns.length ) ;
    }
    
    public boolean isColumnCutPresent() {
        return internalUndoManager.columnCutCommand != null ;
    }
    
    public CutCommand getColumnCutCommand() {
        return internalUndoManager.columnCutCommand ;
    }
    
    public boolean isTableCutPresent() {
        return internalUndoManager.tableCutCommand != null ;
    }
    
    public CutCommand getTableCutCommand() {
        return internalUndoManager.tableCutCommand ;
    }
    
    public boolean isTableInsertPresent() {
        return internalUndoManager.tableInsertCommand != null ;
    }
    
    public TableInsertCommand getTableInsertCommand() {
        return internalUndoManager.tableInsertCommand ;
    }
    
    public boolean isTableContainerInsertPresent() {
        return internalUndoManager.tableContainerInsertCommand != null ;
    }
    
    public StandardInsertCommand getTableContainerInsertCommand() {
        return internalUndoManager.tableContainerInsertCommand ;
    }
    
    public Object[] getChildObjectArray() {
        return this.internalUndoManager.columnList.toArray() ;
    }
    
    public int[] getChildIndexArray() {
        int arraySize = internalUndoManager.lastColumnIndex - internalUndoManager.firstColumnIndex + 1 ;
        int[] indexArray = new int[ arraySize ] ;
        for( int i=0; i<arraySize; i++ ){
            indexArray[i] = internalUndoManager.firstColumnIndex + i ;
        }
        return indexArray ;
    }
   
    
    @Override
    public Result execute() {
        Result result = _execute() ;
        internalUndoManager.end() ;
        if( result != CommandExec.FAILED ) {
            undoManager.addEdit( internalUndoManager ) ;
        }
        else {
            internalUndoManager.undo() ;
        }   
        return result ;
    } 
    
    
    protected Result _execute() {
        Result result = null ;
        // The column reference must be within the remit of 
        // either a FROM clause within a normal SELECT statement,
        // or a tables' array within a JOIN TABLE clause...
        TreePath current = adqlTree.getSelectionPath() ;
        //
        // Are we inserting column refs within a join condition...
        if( joinRelated == true ) {
           AdqlNode joinNode = findJoinTableNodeFromBelow( current ) ;
           result = maintainArrayOfJoinTables( joinNode ) ;
        }
        else {
            //
            // It is insertion of a column outside of an explicit join
            // but could still be for a table within an explicit join.
            // We search for any suitable table with the same name.
            // If we find one we use it, otherwise we create one in a FROM...           
            if( !findSuitableExistingTable() ) {
                result = maintainFromTables( current ) ;
            }        
        }
        //
        // At last we are in a position to include the user's chosen columns for this table...
        if( result != CommandExec.FAILED || result != CommandExec.ERROR ) {
            result = addSelectedColumns() ;
        }
       
        return result ;
    }
    
  
    private Result addSelectedColumns() {
        Result result = CommandExec.FAILED ;
        if( internalUndoManager.getLimit() < columns.length + 10 ) {
            internalUndoManager.setLimit( columns.length + 10 ) ;
        }
        final ColumnInsertCommand 
        	command = new ColumnInsertCommand( adqlTree
        	                                 , internalUndoManager
        	                                 , getParentEntry()
        	                                 , childType
        	                                 , null ) ;
        command.setTable( table ) ;
        command.setTableAlias( tableAlias ) ;
        ColumnInsertCommand cic = null ;
        for( int i=0; i<columns.length; i++ ) {
            // We must shallow copy a command for each column.
            // Else the undo facility will not work.
            if( cic == null ) {
                cic = command ;
            }
            else {
                cic = new ColumnInsertCommand( command ) ;
            }                   
            cic.setColumnName( columns[i].getName() ) ;
            result = cic.execute() ;
            if( result == CommandExec.FAILED || result == CommandExec.ERROR ) {
                break ;
            }
            else if( i==0 ){
                this.childToken = cic.childToken ;
            }               
        }     
        return result ;  
    }
    
    
    private Result maintainArrayOfJoinTables( AdqlNode joinNode  ) {
        Result result = CommandExec.ERROR ;
        //
        // Locate the table array...
        AdqlNode[] children = joinNode.getChildren() ;
        AdqlNode fromTables = null ;
        for( int i=0; i<children.length; i++ ) {
            if( children[i].getSchemaType() == ArrayOfFromTableType.type ) {
                fromTables = children[i] ;
            }
        }
        //
        // If there is no table array, create one...
        if( fromTables == null ) {
            StandardInsertCommand
            sic = new StandardInsertCommand( adqlTree
                                           , internalUndoManager
                                           , joinNode
                                           , ArrayOfFromTableType.type
                                           , null ) ;
            result = sic.execute() ;  
            if( result == CommandExec.ERROR || result == CommandExec.FAILED ) {
                return result ;
            }
            fromTables = sic.getChildEntry() ;
        }
        //
        // 
        AdqlNode[] tables = fromTables.getChildren() ;
        TableType tab = null ;
        AdqlNode tableEntry = null ;
        for( int i=0; i<tables.length; i++ ) {

            tab = (TableType)(tables[i].getXmlObject()) ;
            // NB: This does not currently cater for a table being quoted twice
            // using a different alias. This is a viable situation...
            if( tab.getName().equalsIgnoreCase( this.table.getName() ) ) {
                tableEntry = children[i] ;
                if( tab.isSetAlias() ) {
                    tableAlias = tab.getAlias() ;
                }
                break ;
            }

        }
        //  
        // If we haven't found an entry in the table array that corresponds to
        // the table from which the user wishes to include column references,
        // Then we auto-include an appropriate entry...
        if( tableEntry == null ) {
            TableInsertCommand
                tic = new TableInsertCommand( adqlTree
                                            , internalUndoManager
                                            , fromTables
                                            , TableType.type
                                            , null ) ;
            tic.setTableName( table.getName() ) ;
            result = tic.execute() ;    
            if( result == CommandExec.OK || result == CommandExec.WARNING ) {
                tableAlias = tic.getAllocatedAlias() ;
            }
        }
        return result ;
    }
    
    private Result maintainFromTables( TreePath path )  {
        Result result = CommandExec.ERROR ;
        AdqlNode fromEntry = findFromClause( path ) ;
        AdqlNode[] children = fromEntry.getChildren() ;
        SchemaType tableType = getTableType() ;
        AdqlNode tableEntry = null ;
        XmlString tableName = null ;
        for( int i=0; i<children.length; i++ ) {
            if( children[i].getSchemaType().getName().equals( tableType.getName() ) ) {
                tableName = (XmlString)AdqlUtils.get( children[i].getXmlObject(), "name" ) ;
                // NB: This does not currently cater for a table being quoted twice
                // using a different alias. This is a viable situation...
                if( tableName.getStringValue().equals( table.getName() ) ) {
                    tableEntry = children[i] ;
                    if( AdqlUtils.isSet( tableEntry.getXmlObject(), "alias ") ) {
                        tableAlias = ((XmlString)AdqlUtils.get( tableEntry.getXmlObject() , "alias" )).getStringValue() ;           
                    }
                    break ;
                }
            }
        }
        //  
        // If we haven't found an entry in the FROM clause that corresponds to
        // the table from which the user wishes to include column references,
        // Then we auto-include an appropriate entry in the FROM clause...
        if( tableEntry == null ) {
            TableInsertCommand
            	tic = new TableInsertCommand( adqlTree
            	                            , internalUndoManager
                                            , fromEntry
                                            , tableType
                                            , null ) ;
            tic.setTableName( table.getName() ) ;
            result = tic.execute() ;    
            if( result == CommandExec.OK || result == CommandExec.WARNING ) {
                tableAlias = tic.getAllocatedAlias() ;
            }
        }
        return result ;
    }
    
   
    
    private TreePath findParentSelect( TreePath path ) {
        TreePath workPath = path ;
        AdqlNode entry = null ;
        SchemaType selectType = getSelectType() ;
        for( int i=0; i<path.getPathCount(); i++, workPath = workPath.getParentPath() ) {
            entry = (AdqlNode)workPath.getLastPathComponent() ;
            if( entry.getSchemaType().getName().equals( selectType.getName() ) ) {
                break ;
            }
        }   
        return workPath ;
    }
    
    public AdqlNode findFromClause( TreePath path ) {
        //
        // Find the enclosing SELECT clause...
        TreePath selectPath = findParentSelect( path ) ;
        //
        // Find all the child elements of the SELECT clause...
        AdqlNode selectEntry = ((AdqlNode)selectPath.getLastPathComponent()) ;
        AdqlNode childEntries[] = selectEntry.getChildren() ;
        //
        // Go through the children of the SELECT clause looking for the FROM clause...
        AdqlNode entry = null ;
        SchemaType fromType = getFromType() ;
        for( int i=0; i<childEntries.length; i++ ) {
            entry = childEntries[i] ;
            if( entry.getSchemaType().getName().equals( fromType.getName() ) ) {
                break ;
            }
        } 
        // If no FROM clause found in this part of the query.
        // We need to create a new one...
        if( entry == null ) {  
            StandardInsertCommand
	        fromInsertCommand = new StandardInsertCommand( adqlTree
	                                                     , internalUndoManager
	                                                     , selectEntry
	                                                     , fromType
	                                                     , null ) ;
	        Result result = fromInsertCommand.execute() ;
	        if( result == CommandExec.OK || result == CommandExec.WARNING )
	            entry = fromInsertCommand.getChildEntry() ;
        }       
        return entry ;
    }
    
    private AdqlNode findJoinTableNode( TreePath current ) {
        //
        // First look for a Join above the current...
        AdqlNode node = findJoinTableNodeFromBelow( current ) ;
        //
        // If find one, 
        // Look for a JOIN within a FROM clause below the enclosing SELECT...
        if( node == null ) {
            node = findJoinTableNodeFromAbove( current ) ;
        }
        return node ;
    }
    
    private AdqlNode findJoinTableNodeFromAbove( TreePath current ) {        
        //
        // Look for a JOIN within a FROM clause below the enclosing SELECT...
        AdqlNode node = (AdqlNode) findParentSelect( current ).getLastPathComponent() ;
        //
        // Find all the child elements of the SELECT clause...
        AdqlNode childEntries[] = node.getChildren() ;
        //
        // Go through the children of the SELECT clause looking for the FROM clause...
        AdqlNode fromNode = null ;
        SchemaType fromType = getFromType() ;
        for( int i=0; i<childEntries.length; i++ ) {
            if( childEntries[i].getSchemaType().getName().equals( fromType.getName() ) ) {
                fromNode = childEntries[i] ;
                break ;
            }
        }
        //
        // Found a FROM?
        // If yes, search for a JOIN...
        if( fromNode != null ) {
            AdqlNode[] nodes = fromNode.getChildren() ;           
            for( int i=0; i<nodes.length; i++ ) {
                if( nodes[i].getSchemaType() == JoinTableType.type) {                   
                    return nodes[i] ;
                }
            } 
        }
        //
        // OK. We have not found a JOIN...
        return null ;
    }
    
    private AdqlNode findJoinTableNodeFromBelow( TreePath current ) {
        //
        // Look for a Join above the current...
        TreePath path = current.getParentPath() ;
        Object[] objs = path.getPath() ;
        AdqlNode node = null ;
        for( int i=objs.length-1; i>=0; i-- ) {
            node = (AdqlNode)objs[i] ;
            if( node.getSchemaType() == JoinTableType.type) {
                return node ;
            }
            if( node.getSchemaType() == SelectType.type ) {
                break ;
            }
        }  
        //
        // OK. We have not found a JOIN...
        return null ;
    }
    
    private boolean findSuitableExistingTable() {
        boolean found = false ;
        XmlCursor cursor = adqlTree.getRootNode().getXmlObject().newCursor() ;
        XmlObject xo ;
        SchemaType type ;
        String tableName = this.table.getName() ;
        do {
            if( cursor.isStart() ) {
                xo = cursor.getObject() ; 
                type = xo.schemaType() ;
                if( type == TableType.type || type == FromTableType.type ) {
                    TableType t = (TableType)xo ;
                    if( t.getName().equalsIgnoreCase( tableName ) ) {
                        if( t.isSetAlias() ) {
                            this.tableAlias = t.getAlias() ;
                            found = true ;
                            break ;
                        }
                    }
                }
                
            }
        } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ; 
        
        return found ;
    }
    
  
    
    public AdqlNode findTablesHolder( TreePath path ) {
        //
        // Find the enclosing SELECT clause...
        TreePath selectPath = findParentSelect( path ) ;
        //
        // Find all the child elements of the SELECT clause...
        AdqlNode selectEntry = ((AdqlNode)selectPath.getLastPathComponent()) ;
        AdqlNode childEntries[] = selectEntry.getChildren() ;
        //
        // Go through the children of the SELECT clause looking for the FROM clause...
        AdqlNode entry = null ;
        SchemaType fromType = getFromType() ;
        for( int i=0; i<childEntries.length; i++ ) {
            entry = childEntries[i] ;
            if( entry.getSchemaType().getName().equals( fromType.getName() ) ) {
                break ;
            }
        } 
        // If no FROM clause found in this part of the query.
        // We need to create a new one...
        if( entry == null ) { 
            // ? Still to do 
        }       
        return entry ;
    }
    
    private SchemaType getFromType() {
        return AdqlUtils.getType( childType, AdqlData.FROM_TYPE ) ;
    }
      
    private SchemaType getSelectType() {
        return AdqlUtils.getType( childType, AdqlData.SELECT_TYPE ) ;
    }
     
    private SchemaType getColumnReferenceType() {
        return AdqlUtils.getType( childType, AdqlData.COLUMN_REFERENCE_TYPE ) ;
    }
    
    private SchemaType getArrayOfFromTableType() {
        return AdqlUtils.getType( childType, AdqlData.ARRAY_OF_FROM_TABLE_TYPE ) ;
    }
    
 
    private SchemaType getJoinTableType() {
        return AdqlUtils.getType( childType, AdqlData.JOIN_TABLE_TYPE ) ;
    }
    
    
    private SchemaType getTableType() {
        return AdqlUtils.getType( childType, AdqlData.TABLE_TYPE ) ;
    }
    
    public class MCIUndoManager extends UndoManager {
        
        CutCommand columnCutCommand ;
        CutCommand tableCutCommand ;
        TableInsertCommand tableInsertCommand ; 
        StandardInsertCommand tableContainerInsertCommand ;
        ArrayList<AdqlNode> columnList ;
        int firstColumnIndex = -1 ;
        int lastColumnIndex = -1 ;
        
        public MCIUndoManager() {}
        
        public MultipleColumnInsertCommand getOwner() {
            return MultipleColumnInsertCommand.this ;
        }
            
        @Override
        public boolean addEdit( UndoableEdit anEdit ) {
            boolean added = super.addEdit( anEdit ) ;
            if( added == false )
                return added ;
            if( anEdit instanceof CutCommand ) {
                CutCommand cc = (CutCommand)anEdit ;
                if( AdqlUtils.areTypesEqual( cc.getChildType(), AdqlData.ALL_SELECTION_ITEM_TYPE ) ) {
                    columnCutCommand = cc ;
                }
                else if(AdqlUtils.areTypesEqual( cc.getChildType(), AdqlData.TABLE_TYPE ) ) {
                    tableCutCommand = cc ;
                }
                else {
                    logger.error( "Unknown cut command in MultipleInsertColumnCommand" ) ;
                }
            }
            else if( anEdit instanceof TableInsertCommand ) {
                tableInsertCommand = (TableInsertCommand)anEdit;
            }
            else if( anEdit instanceof ColumnInsertCommand ) {
                ColumnInsertCommand cic = (ColumnInsertCommand)anEdit ;
                columnList.add( cic.getChildEntry() ) ;
                if( firstColumnIndex == -1 ) {
                    firstColumnIndex = cic.getChildIndex() ;
                    lastColumnIndex = firstColumnIndex ;
                }
                else {
                    lastColumnIndex++ ;
                }                            
            }
            else if( anEdit instanceof StandardInsertCommand ) {
                StandardInsertCommand sic = (StandardInsertCommand)anEdit ;
                if( AdqlUtils.areTypesEqual( sic.getChildType(), AdqlData.FROM_TYPE ) ) {
                    tableContainerInsertCommand = sic ;
                }
                else {
                    logger.error( "Unknown insert command in MultipleInsertColumnCommand" ) ;
                }
            }
            return added;
        }
        
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer() ;
        buffer.append( "\nMultipleColumnInsertCommand" ) ;
        buffer.append( super.toString() ) ;
        buffer
            .append( "\ntableAlias: ").append( tableAlias )
            .append( "\ntable: " ).append( table.getName() ) ;
        if( columns != null ) {
            for( int i=0; i<columns.length; i++ ) {
                buffer.append( "\ncolumn " ).append( i ).append( ": " ).append( columns[i].getName() ) ;
            }
        }
        return buffer.toString() ; 
    }

    /* (non-Javadoc)
     * @see org.astrogrid.desktop.modules.adqlEditor.commands.AbstractCommand#isChildEnabled()
     */
    @Override
    public boolean isChildEnabled() {
        boolean enabled = false ;       
        //
        // First we test for normal range of cardinalities...
        AdqlNode target = getParentEntry() ;
        XmlObject o = target.getXmlObject() ;
        String e = getChildElementName() ;
        if( isChildOptionalSingleton() ) {
            enabled = !AdqlUtils.isSet( o, e ) ;
            if( enabled == true && columns != null && columns.length != 1 ) {
                enabled = false ;
            }
        }
        else if( isChildMandatorySingleton() ) {
            enabled = ( AdqlUtils.get( o, e ) == null ) ;
            if( enabled == true && columns != null && columns.length != 1 ) {
                enabled = false ;
            }
        }
        else if( isChildHeldInArray() ) {
            if( maxOccurs == -1 ) {
                enabled = true ;
            }
            else if( columns != null ) {
                if( maxOccurs - AdqlUtils.sizeOfArray( o, e ) >= columns.length )
                    enabled = true ;
            }
        } 
        
        if( enabled == true ) {
            //
            // Establish whether it is associated with an explicit join... 
            TreeNode[] tnodes = target.getPath() ;
            TreePath path = new TreePath( tnodes ) ;
            AdqlNode joinNode = findJoinTableNodeFromBelow( path ) ;
            if( joinNode !=  null ) {
                //
                // OK. We know it is a JOIN construct.
                // Find the array of tables in the join...
                AdqlNode[] nodes = joinNode.getChildren() ;
                AdqlNode arrayNode = null ;
                for( int i=0; i<nodes.length; i++ ) {
                     if( nodes[i].getSchemaType() == ArrayOfFromTableType.type ) {
                         arrayNode = nodes[i] ;
                     }
                }
                //
                // The criterion for being enabled is two fold:
                // (1) If the array is not full (or doesn't exist), there is space for the target table.
                // (2) If the array is full, then one of its tables must be the target table.
                if( arrayNode != null ) {
                    ArrayOfFromTableType tableArray = (ArrayOfFromTableType)arrayNode.getXmlObject() ;
                    if( tableArray.sizeOfFromTableTypeArray() == 2 ) {
                        //
                        // Assume this will fail...
                        enabled = false ;
                        FromTableType[] tables = tableArray.getFromTableTypeArray() ;
                        for( int i=0; i<tables.length; i++ ) {
                            if( ((TableType)tables[i]).getName().equalsIgnoreCase( this.table.getName() ) ) {
                                enabled = true ;
                                this.joinRelated = true ;
                                break ;
                            }
                        }
                    }
                    else {
                        this.joinRelated = true ;
                    }
                }
                else {
                    this.joinRelated = true ;
                }
            }            
        }
        return enabled ;
    }  
    
}