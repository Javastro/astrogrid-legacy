/*$Id: JoinInsertCommand.java,v 1.3 2009/03/26 18:04:10 nw Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor.commands;

import javax.swing.tree.TreePath;
import javax.swing.undo.UndoManager;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType;
import org.astrogrid.adql.v1_0.beans.ComparisonPredType;
import org.astrogrid.adql.v1_0.beans.ComparisonType;
import org.astrogrid.adql.v1_0.beans.SelectDocument;
import org.astrogrid.adql.v1_0.beans.SelectType;
import org.astrogrid.adql.v1_0.beans.FromType;
import org.astrogrid.adql.v1_0.beans.*;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

/**
 * JoinInsertCommand
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Apr 18, 2008
 */
public class JoinInsertCommand extends EnumeratedElementInsertCommand {

    protected UndoManager masterUndoManager ;
       
    /**
     * @param adqlTree
     * @param undoManager
     * @param parentTarget
     * @param childType
     * @param childElement
     */
    public JoinInsertCommand( AdqlTree adqlTree
                            , UndoManager undoManager
                            , AdqlNode parentTarget
                            , SchemaType childType
                            , SchemaProperty childElement ) {
        super(adqlTree, null, parentTarget, childType, childElement);
        this.masterUndoManager = undoManager ;
        this.undoManager = new InternalUndoManager() ;
    }
       
    /* (non-Javadoc)
     * @see org.astrogrid.desktop.modules.adqlEditor.commands.StandardInsertCommand#execute()
     */
    @Override
    public Result execute() {
        StandardInsertCommand tableArrayInsertCommand = null ;
        StandardInsertCommand comparisonInsertCommand = null ;
        Result result = super.execute() ;
        if( result != CommandExec.FAILED ) {
            tableArrayInsertCommand = new StandardInsertCommand( adqlTree
                                                               , this.undoManager 
                                                               , this.getChildEntry()
                                                               , ArrayOfFromTableType.type 
                                                               , null ) ;
            result = tableArrayInsertCommand.execute() ;
            if( result != CommandExec.FAILED ) {
                comparisonInsertCommand = new EnumeratedInsertCommand( adqlTree
                                                                     , this.undoManager
                                                                     , this.getChildEntry()
                                                                     , ComparisonPredType.type 
                                                                     , null 
                                                                     , ComparisonType.type
                                                                     , "Comparison" ) ;
                comparisonInsertCommand.setSelectedValue( "=" ) ;
                result = comparisonInsertCommand.execute() ;                
            }            
        }
        undoManager.end() ;
        if( result != CommandExec.FAILED ) {                    
            this.masterUndoManager.addEdit( undoManager ) ;
            removeDummyTable() ;
            adqlTree.expandPath( new TreePath( this.getChildEntry().getPath() ) ) ;
        }
        else if( undoManager.canUndo() ){
            undoManager.undo() ;
        }
        return result ;
    }
    
    private void removeDummyTable() {
        //
        // Find the SELECT clause...
        AdqlNode node = adqlTree.getRootNode() ;
        AdqlNode[] nArray ;
        XmlObject xo = node.getXmlObject() ;
        SelectType st = null ;
        if( xo instanceof SelectType ) {
            st = (SelectType)xo ;
        }
        else {
            SelectDocument sd = (SelectDocument)xo ;
            st = sd.getSelect() ;
            nArray = node.getChildren() ;
            for( int i=0; i<nArray.length; i++ ) {
                if( nArray[i].getXmlObject() == st ) {
                    node = nArray[i] ;
                    break ;
                }
            }
        }
        //
        // Find the FROM...
        FromType ft = st.getFrom() ;
        if( ft == null )
            return ;
        nArray = node.getChildren() ;
        for( int i=0; i<nArray.length; i++ ) {
            if( nArray[i].getXmlObject() == ft ) {
                node = nArray[i] ;
                break ;
            }
        }        
        FromTableType[] tArray = ft.getTableArray() ;
        if( tArray == null )
            return ;
        TableType dummyTable = null ;
        int index = 0 ;
        //
        // We have the table array...
        for( int i=0; i<tArray.length; i++ ) {
            if( tArray[i] instanceof TableType ) {
                if( ((TableType)tArray[i]).getName().equalsIgnoreCase( AdqlData.DUMMY_TABLE_NAME ) ) {
                    dummyTable = (TableType)tArray[i] ;
                    break ;
                }
            }
        }
        if( dummyTable == null )
            return ;
        node.remove( index ) ;
        ft.removeTable( index ) ;       
    }
    
    public class InternalUndoManager extends UndoManager {
        
        public InternalUndoManager() {}
        
        public JoinInsertCommand getOwner() {
            return JoinInsertCommand.this ;
        }
        
    }
    
}


/*
$Log: JoinInsertCommand.java,v $
Revision 1.3  2009/03/26 18:04:10  nw
source code improvements - cleaned imports, @override, etc.

Revision 1.2  2008/04/22 13:40:26  jl99
Merge of branch vodesktop-jl-2733.
Fixes for 2733, but also 2735 and 2736.

Revision 1.1.2.3  2008/04/22 11:34:51  jl99
Expand tree branches that have been inserted (only partially successful)

Revision 1.1.2.2  2008/04/22 10:59:45  jl99
Dummy table removed if present.

Revision 1.1.2.1  2008/04/22 09:47:38  jl99
Insertion of an explicit join now includes all the constructs automatically, defaulting the condition to an equals.

*/