/* TableInsertCommand.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class TableInsertCommand extends StandardInsertCommand {
    
    private static final Log log = LogFactory.getLog( TableInsertCommand.class ) ;
   
//    protected CatalogService catalogService ;
//    protected Catalog database ;
    
    protected String tableName ;
    protected String allocatedAlias ;
   
    /**
     * @param target
     * @param 
     */
    public TableInsertCommand( AdqlTree adqlTree
                             , UndoManager undoManager
                             , AdqlNode parentTarget
                             , SchemaType childType
                             , SchemaProperty childElement ) {
        super( adqlTree, undoManager, parentTarget, childType, childElement ) ;
    }
    
    public TableInsertCommand( TableInsertCommand tic ) {
        super( tic.adqlTree, tic.undoManager, tic.getParentEntry(), tic.childType, tic.childElement ) ;
//        this.database = tic.database ;
//        this.catalogService = tic.catalogService ;
        this.tableName = tic.tableName ;
        this.allocatedAlias = tic.allocatedAlias ;
    }
    
    public String getTableName() {
        return tableName ;
    }
    
    public void setTableName( String name ) {
        this.tableName = name ;
    }
    
    @Override
    public void setSelectedValue( String value ) {
        setTableName( value ) ;
    }
    
//    public String[] getTableNames() {
//        TableBean[] tables = database.getTables() ;
//        String[] tableNames = new String[ tables.length ] ;
//        for( int i=0; i<tables.length; i++ ) {
//            tableNames[i] = tables[i].getName() ;
//        }
//        return tableNames ;
//    }
    
//    public void setDatabase( Catalog archive ) {
//        this.database = archive ;
//    }
    
    public String getAllocatedAlias() {
        return allocatedAlias ;
    }
    
    public void setAllocatedAlias( String alias ) {
        this.allocatedAlias = alias ;
    }
    
    @Override
    public Result execute() {
        removeDummyTable() ;
        return super.execute() ;
    }
    
    @Override
    protected Result _execute() {
//        removeDummyTable() ;
        Result result = super._execute() ;
        if( result != CommandExec.FAILED ) {
            try {          
                allocatedAlias = adqlTree.popAliasStack() ;
                AdqlUtils.set( getChildObject()
                             , "alias"
                             , XmlString.Factory.newValue( allocatedAlias ) ) ;
                String tabName = tableName ;
//                if( !AdqlUtils.isRegularIdentifier( tabName ) ) {
//                    tabName = "\"" + tabName + "\"" ;
//                }
                AdqlUtils.set( getChildObject()
                             , "name"
                             , XmlString.Factory.newValue( tabName ) ) ;
                String name = childType.getName().getLocalPart() ;
//                if( name.equals( "archiveTableType" ) ) {
//                    AdqlUtils.set( getChildObject()
//                                 , "archive"
//                                 , XmlString.Factory.newValue( adqlTree.getCatalogName() ) ) ;
//                }
                TableBean[] tables = adqlTree.getCatalogService().getTables() ;
                for( int i=0; i<tables.length; i++ ) {
                    if( tableName.equals( tables[i].getName() ) ) {
                        adqlTree.getFromTables().put( tableName, adqlTree.new TableData( tables[i], allocatedAlias ) ) ;
                        break ;
                    }
                } 
            }
            catch( Exception exception ) {
                result = CommandExec.FAILED ;
                log.error( "TableInsertCommand._execute() failed.", exception ) ;
                if( log.isDebugEnabled() ) {
                    log.debug(  this.toString() ) ;
                }
            }
        }       
        return result ;
    }
    
    @Override
    protected Result _unexecute() {
        Result result = super._unexecute() ;
//        reinstateDummyTable() ;
        adqlTree.pushAliasStack( allocatedAlias ) ;
        adqlTree.getFromTables().remove( tableName ) ;
        return result ;
    }
    
    private void removeDummyTable() {
        // This processing attempts the automatic removal of the dummy table 
        // which is included in the initial template for a new query...
        // (Bit of a bind I'm afraid)
        try {
            XmlObject o = getParentObject() ;
            int arraySize = AdqlUtils.sizeOfArray( o, getChildElementName() ) ;
            if( arraySize == 1 ) {
                Object table = AdqlUtils.getArray( o, getChildElementName(), 0 ) ;
                String name = ((XmlString)AdqlUtils.get( (XmlObject)table, "name" )).getStringValue() ;
                if( name.equals( AdqlData.DUMMY_TABLE_NAME ) ) {
                    AdqlNode entry = getParentEntry().getChild( 0 ) ;           
//                  AdqlEntry.removeInstance( parent, entry ) ;
                    CutCommand cutCommand = new CutCommand( adqlTree, undoManager, entry ) ;
                    cutCommand.execute() ;
                    adqlTree.getFromTables().remove( AdqlData.DUMMY_TABLE_NAME ) ;
                }
            }
        }
        catch( Exception ex ) {
            log.warn( "TableInsertCommand.removeDummyTable() failed.\n" +
                      "  Probable cause: inappropriate context." ) ;
        }
       
    }
    
    private void _reinstateDummyTable() {
        try {
            XmlObject o = getParentObject() ;
            if( !AdqlUtils.areTypesEqual( getParentEntry().getSchemaType()
                                        , AdqlUtils.getType( o, AdqlData.FROM_TYPE ) ) ) 
               return ;
            int arraySize = AdqlUtils.sizeOfArray( o, getChildElementName() ) ;
            if( arraySize == 0 ) {
                XmlObject newObject = AdqlUtils.addNewToEndOfArray( o, getChildElementName() ) ;
                newObject = newObject.changeType( childType ) ;
                adqlTree.getNodeFactory().newInstance( getParentEntry(), newObject ) ;
                AdqlUtils.set( newObject
                             , "name"
                             , XmlString.Factory.newValue( AdqlData.DUMMY_TABLE_NAME ) ) ;
            }
        }
        catch( Exception ex ) {
            log.warn( "TableInsertCommand._reinstateDummyTable() failed.\n" +
            "  Probable cause: inappropriate context." ) ;
        }      
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer() ;
        buffer.append( "\nTableInsertCommand" ) ;
        buffer.append( super.toString() ) ;
        buffer
            .append( "\nallocatedAlias: ").append( allocatedAlias )
            .append( "\ntableName: " ).append( tableName ) ;
//            .append( "\ndatabase: " ).append( database ) ;
        return buffer.toString() ; 
    }  
        
}