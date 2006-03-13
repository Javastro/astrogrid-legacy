/* TableMetadataPanel.java
 * Created on 24-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.* ;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.JLabel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.* ;

import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.astrogrid.TableBean ;
import org.astrogrid.acr.astrogrid.ColumnBean ;
import org.astrogrid.desktop.modules.dialogs.editors.ADQLToolEditorPanel;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TableMetadataPanel extends JPanel {
    
    private static final Log log = LogFactory.getLog( TableMetadataPanel.class ) ;
    private static final boolean DEBUG_ENABLED = false ;
    private static final boolean TRACE_ENABLED = false ;
    
    private final static String 
    	NAME 		= "Name",
    	UCD  		= "UCD",
    	UNITS 		= "Units",
    	TYPE 		= "Type",
    	DESCRIPTION = "Description" ;
    
    private TableBean astroTable ;
    private DatabaseBean dbBean ;
    private int tableIndex ;
    private JTable displayTable ;
    private JScrollPane displayTablerScrollPane ;
    private AdqlTree adqlTree ;
    private ADQLToolEditorPanel adqlToolEditorPanel ;
    private SchemaType columnReferenceType ;
    private SchemaType selectType ;
    private SchemaType fromType ;
    private SchemaType joinTableType ;
    private SchemaType arrayOfFromTableType ;
    private SchemaType tableType ;

    public TableMetadataPanel( ADQLToolEditorPanel adqlToolEditorPanel
                             , AdqlTree adqlTree
                             , DatabaseBean dbBean
                             , int tableIndex ) {
        this( adqlToolEditorPanel, adqlTree, dbBean.getTables()[tableIndex] ) ;
        this.dbBean = dbBean ;
        this.tableIndex = tableIndex ;
    }
    
    
    /**
     * 
     */
    public TableMetadataPanel( ADQLToolEditorPanel adqlToolEditorPanel, AdqlTree adqlTree, TableBean tableBean ) {
        super();
        this.adqlToolEditorPanel = adqlToolEditorPanel ;
        this.adqlTree = adqlTree ;
        this.astroTable = tableBean ;
        setLayout( new GridBagLayout() ) ;       
        GridBagConstraints gbc ;
        JSplitPane splitMetadata = new JSplitPane( JSplitPane.VERTICAL_SPLIT );

        // Place the table description in the top half...
        splitMetadata.setTopComponent( initDescription() ) ;

        // Place the column metadate in the bottom half...
        splitMetadata.setBottomComponent( initColumnView() ) ;

        // Set the rest of the split pane's properties,
        splitMetadata.setDividerLocation(100);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        add( splitMetadata, gbc ) ;
    }

    private Component initDescription() {
        // Table description...
        JScrollPane scrollPane = new JScrollPane() ; 
        JTextPane tableDescription = new JTextPane() ;
        scrollPane.setViewportView( tableDescription ) ;
        String description = astroTable.getDescription() ;
        if( description != null )
            description = description.trim() ;
        if( description == null || description.length() == 0 ) {
            description = "No description available" ;
        }
        tableDescription.setText( description ) ; 
        return scrollPane ;
    }
    
    private Component initColumnView() {
        AstroTableModel astroTableModel = new AstroTableModel();
        displayTable = new JTable() ;
        displayTable.setAutoCreateColumnsFromModel( false ) ;
        displayTable.setModel( astroTableModel ) ;
        
        for( int i=0 ; i<astroTableModel.getColumnCount(); i++ ) {
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() ;
            renderer.setHorizontalAlignment( astroTableModel.displayColumnData[i].alignment ) ;
            TableColumn 
            	column = new TableColumn( i
            	                        , astroTableModel.displayColumnData[i].width
            	                        , renderer
            	                        , null );
            displayTable.addColumn( column ) ;
        }
        
        JTableHeader header = displayTable.getTableHeader() ;
        header.setUpdateTableInRealTime( false ) ;
        
        displayTablerScrollPane = new JScrollPane() ; 
        displayTablerScrollPane.getViewport().setBackground( displayTable.getBackground() ) ;
        displayTablerScrollPane.getViewport().add( displayTable ) ;
        displayTable.addMouseListener( new ContextPopup() ) ;
        return displayTablerScrollPane ;
    }
    
    class DisplayColumnData {
        public String title ;
        public int width ;
        public int alignment ;
       
        public DisplayColumnData( String title, int width, int alignment ) {
            this.title = title ;
            this.width = width ;
            this.alignment = alignment ;          
        }
    } // end of class DisplayColumnData
    
    class AstroTableModel extends AbstractTableModel {
        
        final public DisplayColumnData displayColumnData[] = {
                new DisplayColumnData( TableMetadataPanel.NAME, 100, JLabel.LEFT ) ,
                new DisplayColumnData( TableMetadataPanel.UCD, 100, JLabel.LEFT ) ,
                new DisplayColumnData( TableMetadataPanel.UNITS, 100, JLabel.LEFT ) ,
                new DisplayColumnData( TableMetadataPanel.TYPE, 100, JLabel.LEFT ) ,
                new DisplayColumnData( TableMetadataPanel.DESCRIPTION, 200, JLabel.LEFT ) ,
        } ;
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false ;
        }
        public int getColumnCount() {
            return displayColumnData.length ;
        }
        public int getRowCount() {
            if( astroTable == null )
                return 0 ;
            return astroTable.getColumns().length ;
        }
        
        public String getColumnName( int column ) {
            return displayColumnData[ column ].title ;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            if( rowIndex < 0 || rowIndex >= getRowCount() )
                return "" ;
            ColumnBean astroColumn = astroTable.getColumns()[ rowIndex ] ;
            switch( columnIndex ) {
            	case 0: return astroColumn.getName() ;
            	case 1: return astroColumn.getUCD() ;
            	case 2: return astroColumn.getUnit() ;
            	case 3: return astroColumn.getDatatype() ;
            	case 4: return astroColumn.getDescription() ;
            }
            return "";
        }
        
        public ColumnBean getColumnBeanGivenRowIndex( int rowIndex ) {
            return astroTable.getColumns()[ rowIndex ] ;
        }
        
        AstroTableModel() {}
        
    } // end of class AstroTableModel
    
    private class ContextPopup extends MouseAdapter implements PopupMenuListener {
        
        private JPopupMenu popup ;
        private InsertAction insertAction ;
        
        public ContextPopup() {
            popup = new JPopupMenu( "ColumnMetadataContextMenu" ) ;
            popup.add( "Column References" ) ;
            popup.addSeparator() ; 
            insertAction = new InsertAction( "" ) ;
            popup.add( insertAction ) ;
            popup.addPopupMenuListener( this ) ;
        }
        
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
        
        public void popupMenuCanceled(PopupMenuEvent e) {
            adqlToolEditorPanel.unsetElastic() ;
            adqlToolEditorPanel.updateDisplay() ;
            
        }
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            adqlToolEditorPanel.unsetElastic() ;
            adqlToolEditorPanel.updateDisplay() ;
        }
        
        public void mouseReleased( MouseEvent event ) {
            
            if( 
                ( event.isPopupTrigger() || event.getButton() == MouseEvent.BUTTON3 )
                &&
                adqlTree.isShowing()
            ) {
    	        TreePath path = adqlTree.getSelectionPath() ;
    	        // If the path is null or there is no relevant parent
    	        // Then we cannot do anything with this entry...
    	        if( path == null || path.getPathCount() < 2 )
    	            return ;  
                int count = displayTable.getSelectedRowCount() ;
                StringBuffer buffer = new StringBuffer( 36 ) ;
                buffer
                	.append( "Insert " )
                	.append( count )
                	.append( " reference" )
                	.append( count>1 || count==0 ? "s" : "" )
                	.append( " into \"" )
                	.append( ((AdqlEntry)path.getLastPathComponent()).getDisplayName() )
                	.append( "\"" ) ;
    	        insertAction.putValue( AbstractAction.NAME, buffer.toString() ) ;
    	        //
    	        // The mouse position is shown relative to the JTable.
    	        // As this is in a JScrollPane, it requires adjustment
    	        // according to what position the JViewport has on the 
    	        // JTable...
	            JViewport viewPort = displayTablerScrollPane.getViewport() ;
    	        Point popupPosition =
    	            SwingUtilities.convertPoint( displayTable, event.getPoint(), viewPort ) ;

    	        Rectangle rect = adqlTree.getPathBounds( path ) ;   	  
    	        Rectangle vr = adqlTree.getVisibleRect() ;
    	        rect = SwingUtilities.computeIntersection( vr.x, vr.y, vr.width, vr.height, rect ) ;
    	        if( rect.width == 0 ) {
    	            adqlTree.scrollPathToVisible( path ) ;
    	            adqlTree.setSelectionPath( path ) ;
    	            rect = adqlTree.getPathBounds( path ) ;   	  
        	        vr = adqlTree.getVisibleRect() ;
        	        rect = SwingUtilities.computeIntersection( vr.x, vr.y, vr.width, vr.height, rect ) ;
    	        }
    	        Point rightSideOfPath = rect.getLocation() ;
    	        rightSideOfPath.x += (rect.width + 1) ;
    	        rightSideOfPath.y += rect.height / 2 ;
	            Point[] elastic = new Point[2] ;
	            elastic[0] = SwingUtilities.convertPoint( displayTable
	                                                    , event.getPoint()
	                                                    , adqlToolEditorPanel ) ;
	            elastic[1] = SwingUtilities.convertPoint( adqlTree
	                                                    , rightSideOfPath
	                                                    , adqlToolEditorPanel ) ;
	            adqlToolEditorPanel.setElastic( elastic ) ;
	            adqlToolEditorPanel.updateDisplay() ;
		        insertAction.setEnabled( insertAction.testAndBuildSuitability() ) ;
	            popup.show( viewPort, popupPosition.x, popupPosition.y ) ;
            }
                     
        } // end of ContextPopup.mouseReleased( MouseEvent event )
        
    } // end of class ContextPopup
    
    private class InsertAction extends AbstractAction {
        ArrayList commands ; 
        AdqlCommand suitableCommand ;
        int concreteType ;
	   
	    public InsertAction( String contextInstruction ) {
	        super( contextInstruction ) ;
	    }
	    
	    private boolean testAndBuildSuitability() {
	        TreePath path = adqlTree.getSelectionPath() ;
	        // If the path is null or there is no parent
	        // Then we cannot paste into this entry...
	        if( path == null || path.getPathCount() < 2 )
	            return false ;
	        // If the target allows for no children, then we dont paste into it.
	        AdqlEntry entry = (AdqlEntry)path.getLastPathComponent() ;
	        SchemaProperty[] elements = entry.getElementProperties() ;
	        if( elements == null || elements.length == 0 ) 
	        	return false ;  
	        //  Check that it is OK to create a column reference into the target...
	        if( AdqlCommand.isSuitablePasteTarget( entry, getColumnReferenceType() ) == false ) 
	            return false ;
	        //  Build array of possible insert commands for target...
	        commands = AdqlCommand.buildCommands( entry ) ; 
	        
	        //  Find a suitable match of types...
	        int result[] = AdqlCommand.findSuitableMatch( commands, getColumnReferenceType() ) ;
	        if( result[0] == -1 )
	            return false ;
	        suitableCommand = (AdqlCommand)commands.get( result[0] ) ;
	        concreteType = result[1] ;
	        
	        // Now for the specific testing of arrays ...
	        int count = displayTable.getSelectedRowCount() ;
	        if( suitableCommand.isArray() ) {
	            if( suitableCommand.isInsertableIntoArray( count ) )
	                return true ;
	            else
	                return false ;
	        }
	       
	        return true ;
	        
	    }
	    
	    
	    public void actionPerformed( ActionEvent e ) {
	        //
	        // If nothing has been selected in the tree (or in the equivalent edit panes)
	        // or the user has managed to select elements above the primary 
	        // select clause (impossible but still we cater for it!)
	        // Then we just ignore the attempt...
	        TreePath path = adqlTree.getSelectionPath() ;
	        if( path == null || path.getPathCount() < 2 )
	            return ;	      
	        // If the target allows for no children, then we cannot create into it.
	        // (This is weak. We should allow creating over bottom leaves)
	        AdqlEntry entry = (AdqlEntry)path.getLastPathComponent() ;
	        SchemaProperty[] elements = entry.getElementProperties() ;
	        if( elements == null || elements.length == 0 ) 
	        	return ;        
	        // Check that it is OK to create a column reference into the target...
	        if( AdqlCommand.isSuitablePasteTarget( entry, getColumnReferenceType() ) == false ) {
	            if( DEBUG_ENABLED ) log.debug( "columnReferenceType unsuitable" ) ;
	            return ;
	        }
	  
	        // Build array of possible insert commands for target...
	        ArrayList commands = AdqlCommand.buildCommands( entry ) ; 
	        
	        // Attempt to find a suitable match of types...
	        int result[] = AdqlCommand.findSuitableMatch( commands, getColumnReferenceType() ) ;	        
	        if( result[0] == -1 ) {
	            if( DEBUG_ENABLED ) log.debug( "could not find a suitable match." ) ;
	            return ;
	        }
	        if( DEBUG_ENABLED ) log.debug( "columnReferenceType suitable..." ) ;
	        if( DEBUG_ENABLED ) log.debug( "found: " 
	                 + ((AdqlCommand)commands.get( result[0] )).getConcreteTypes()[ result[1] ].getName() ) ;	
	        
	        //
	        // The column reference must be within the remit of 
	        // either a FROM clause within a normal SELECT statement,
	        // or a tables' array within a JOIN TABLE clause...
	        if( isJoinTableBound() ) {
	            maintainArrayOfJoinTables() ;
	        }
	        else {
	            maintainFromTables( path ) ;
	        }
	        //
	        // At last we are in a position to include the user's chosen columns for this table...
	        addSelectedColumns( (AdqlCommand)commands.get( result[0] ), result[1] ) ;
	        
	        return ;
	    }
	        
    }
    
 
    
    
    
//	private class PasteIntoAction extends AbstractAction {
//	    private AdqlEntry entry ;
//	    private XmlObject pasteObject;
//	    private AdqlCommand suitableCommand ;
//	    private int validType ;
//	       
//	    public PasteIntoAction( AdqlEntry entry ) {
//	        super( "Paste into" ) ;
//	        this.entry = entry ;
//	        setEnabled( testAndBuildSuitability() ) ;
//	    }
//	    
//	    private boolean testAndBuildSuitability() {
//	        TreePath path = adqlTree.getSelectionPath() ;
//	        // If the path is null or there is no parent
//	        // Then we cannot paste into this entry...
//	        if( path == null || path.getPathCount() < 2 )
//	            return false ;
//	        // If the clipboard is empty then there is nothing to paste...
//	        if( clipBoard.isEmpty() )
//	            return false ;
//	        // If the target allows for no children, then we dont paste into it.
//	        SchemaProperty[] elements = entry.getElementProperties() ;
//	        if( elements == null || elements.length == 0 ) 
//	        	return false ;        
//	        // Check that the last clipboard object is suitable to paste into the target...
//	        pasteObject = (XmlObject)clipBoard.get( clipBoard.size() - 1 ) ;
//	        if( AdqlCommand.isSuitablePasteTarget( entry, pasteObject ) == false )
//	            return false ;
//	        
//	        // Build array of possible insert commands for target...
//	        ArrayList commands = AdqlCommand.buildCommands( entry ) ; 
//	        
//	        // Attempt to find a suitable match of types...
//	        int result[] = AdqlCommand.findSuitableMatch( commands, pasteObject ) ;	        
//	        if( result[0] == -1 )
//	            return false ;
//	        this.suitableCommand = (AdqlCommand)commands.get( result[0] ) ;
//	        this.validType = result[1] ;
//	        return true ;
//	    }
//	    
//	    public void actionPerformed( ActionEvent e ) {
//	        // Now do the business.
//	        // Insert a basic object...
//	        XmlObject newObject1 = insertObject( suitableCommand, validType ) ;
//	        // Copy contents of paste object into it...
//	        XmlObject newObject2 = newObject1.set( pasteObject ) ;
//
//            // Create the new tree node instance (and pray it works)...
//        	AdqlEntry.newInstance( entry, newObject2 ) ;  
//        	
//        	// Refresh tree...
//	        ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( entry ) ;
//	        adqlTree.repaint() ;
//	        
//	    }
//	  
//	        
//	    private XmlObject insertObject( AdqlCommand commandBean, int subType ) {
//            AdqlEntry parent = commandBean.getEntry() ;
//            XmlObject parentObject = parent.getXmlObject() ;
//            SchemaType schemaType = commandBean.getConcreteTypes()[subType] ;
//            XmlObject newObject = null ; 
//            if( commandBean.isArray() ) {                
//                newObject = AdqlUtils.addNewToEndOfArray( parentObject, commandBean.getElementName() ) ;
//                newObject = newObject.changeType( schemaType ) ;
//            }
//            else {
//                if( schemaType.isBuiltinType() ) {
//                    newObject = XmlObject.Factory.newInstance().changeType( schemaType ) ;
//                    newObject = setDefaultValue( newObject ) ;
//                }
//                else {            
//                    newObject = AdqlUtils.addNew( parentObject, commandBean.getElementName() ) ;
//                    if( newObject != null ) {
//                        newObject = newObject.changeType( schemaType ) ;
//                    }
//                    else {
//                        newObject = XmlObject.Factory.newInstance().changeType( commandBean.getElement().javaBasedOnType() ) ;
//                        newObject = newObject.changeType( schemaType ) ;
//                        newObject = setDefaultValue( newObject ) ;
//                        AdqlUtils.set( parentObject, commandBean.getElementName(), newObject ) ; 
//                    }
//                }
//            } 
//            return newObject ;
//        }
//	    
//	}    
    
    
    
    
    
    
    private void addSelectedColumns( AdqlCommand command, int concreteSubtype ) {
        // This is admittedly confusing terminology...
        // Since the JTable is holding information about relational table columns
        // in a display table with each row displaying metadata about a column!!!
        int[] selectedColumnBeans = displayTable.getSelectedRows() ;
        AstroTableModel model = (AstroTableModel)displayTable.getModel() ;
        ColumnBean columnBean = null ;
        ADQLToolEditorPanel.InsertColumnAction insert = null ;
        AdqlTree.TableData tableData = adqlTree.getTableData( astroTable.getName() ) ;
        // Following getout is a temporary kludge
        if( tableData == null )
            return ;
        for( int i=0 ; i<selectedColumnBeans.length; i++ ) {
            columnBean = model.getColumnBeanGivenRowIndex( selectedColumnBeans[i] ) ;
            // Using the InsertColumnAction is also a kludge.
            // Need to factor out the appropriate code.
            insert = adqlToolEditorPanel.new InsertColumnAction( "dummy name"
                                                               , command
                                                               , concreteSubtype
                                                               , tableData
                                                               , columnBean ) ;
            ActionEvent e = null ;
            if( i == selectedColumnBeans.length-1 )
                e = new ActionEvent( this, 1, "Repaint" ) ;
            insert.actionPerformed( null ) ;
        }
        
         
    }
    
    
    private void maintainArrayOfJoinTables() {
        
    }
    
    private void maintainFromTables( TreePath path )  {
        AdqlEntry fromEntry = findFromClause( path ) ;
        AdqlEntry[] children = fromEntry.getChildren() ;
        SchemaType tableType = getTableType() ;
        AdqlEntry tableEntry = null ;
        XmlString tableName = null ;
        for( int i=0; i<children.length; i++ ) {
            if( children[i].getSchemaType().getName().equals( tableType.getName() ) ) {
                tableName = (XmlString)AdqlUtils.get( children[i].getXmlObject(), "name" ) ;
                // NB: This does not currently cater for a table being quoted twice
                // using a different alias. This is a viable situation...
                if( tableName.getStringValue().equals( astroTable.getName() ) ) {
                    tableEntry = children[i] ;
                    break ;
                }
            }
        }
        //  
        // If we haven't found an entry in the FROM clause that corresponds to
        // the table from which the user wishes to include column references,
        // Then we auto-include an appropriate entry in the FROM clause...
        if( tableEntry == null ) {
            // Build array of possible insert commands for target...
	        ArrayList commands = AdqlCommand.buildCommands( fromEntry ) ; 
	        
	        // Attempt to find a suitable match of types...
	        int result[] = AdqlCommand.findSuitableMatch( commands, getTableType() ) ;	        
	        if( result[0] == -1 ) {
	            if( DEBUG_ENABLED ) log.debug( "could not find a suitable match." ) ;
	            return ;
	        }
	        if( DEBUG_ENABLED ) log.debug( "tableType suitable..." ) ;
	        if( DEBUG_ENABLED ) log.debug( "found: " 
	                 + ((AdqlCommand)commands.get( result[0] )).getConcreteTypes()[ result[1] ].getName() ) ;	
	        	        
            ADQLToolEditorPanel.InsertTableAction insert = adqlToolEditorPanel.new
               InsertTableAction( "dummy name"
                                , (AdqlCommand)commands.get( result[0] )
                                , result[1]
                                , dbBean
                                , tableIndex ) ;          
            insert.actionPerformed( null ) ;
        }
    }
    
   
    
    private TreePath findParentSelect( TreePath path ) {
        TreePath workPath = path ;
        AdqlEntry entry = null ;
        SchemaType selectType = getSelectType() ;
        for( int i=0; i<path.getPathCount(); i++, workPath = workPath.getParentPath() ) {
            entry = (AdqlEntry)workPath.getLastPathComponent() ;
            if( entry.getSchemaType().getName().equals( selectType.getName() ) ) {
                break ;
            }
        }   
        return workPath ;
    }
    
  
    
    
    private AdqlEntry findFromClause( TreePath path ) {
        //
        // Find the enclosing SELECT clause...
        TreePath selectPath = findParentSelect( path ) ;
        //
        // Find all the child elements of the SELECT clause...
        AdqlEntry selectEntry = ((AdqlEntry)selectPath.getLastPathComponent()) ;
        AdqlEntry childEntries[] = selectEntry.getChildren() ;
        //
        // Go through the children of the SELECT clause looking for the FROM clause...
        AdqlEntry entry = null ;
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
            ArrayList commands = AdqlCommand.buildCommands( selectEntry ) ;
            // Attempt to find a suitable match of types...
	        int match[] = AdqlCommand.findSuitableMatch( commands, fromType ) ;	        
	        if( match[0] == -1 ) {
	            log.error( "could not find a suitable match." ) ;
	        }
	        else {
	            // Now create the empty FROM clause...
	            XmlObject newObject = AdqlUtils.addNew( selectEntry.getXmlObject()
	                                                  , ((AdqlCommand)commands.get(match[0])).getElementName() ) ;
	            entry = AdqlEntry.newInstance( selectEntry, newObject ) ;
	        }
        }       
        return entry ;
    }
    
    
    private AdqlEntry findTablesHolder( TreePath path ) {
        //
        // Find the enclosing SELECT clause...
        TreePath selectPath = findParentSelect( path ) ;
        //
        // Find all the child elements of the SELECT clause...
        AdqlEntry selectEntry = ((AdqlEntry)selectPath.getLastPathComponent()) ;
        AdqlEntry childEntries[] = selectEntry.getChildren() ;
        //
        // Go through the children of the SELECT clause looking for the FROM clause...
        AdqlEntry entry = null ;
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
            ArrayList commands = AdqlCommand.buildCommands( selectEntry ) ;
            // Attempt to find a suitable match of types...
	        int match[] = AdqlCommand.findSuitableMatch( commands, fromType ) ;	        
	        if( match[0] == -1 ) {
	            log.error( "could not find a suitable match." ) ;
	        }
	        else {
	            // Now create the empty FROM clause...
	            XmlObject newObject = AdqlUtils.addNew( selectEntry.getXmlObject()
	                                                  , ((AdqlCommand)commands.get(match[0])).getElementName() ) ;
	            entry = AdqlEntry.newInstance( selectEntry, newObject ) ;
	        }
        }       
        return entry ;
    }
    
    private boolean isJoinTableBound() {
        return false ;
    }
    
    private SchemaType getFromType() {
        if( fromType == null ) {
            fromType = getType( AdqlData.FROM_TYPE ) ;
        }
        return fromType ;
    }
    
    
    private SchemaType getSelectType() {
        if( selectType == null ) {
            selectType = getType( AdqlData.SELECT_TYPE ) ;
        }
        return selectType ;
    }
    
    
    private SchemaType getColumnReferenceType() {
        if( columnReferenceType == null ) {
            columnReferenceType = getType( AdqlData.COLUMN_REFERENCE_TYPE ) ;
        }
        return columnReferenceType ;
    }
    
    
    private SchemaType getArrayOfFromTableType() {
        if( arrayOfFromTableType == null ) {
            arrayOfFromTableType = getType( AdqlData.ARRAY_OF_FROM_TABLE_TYPE ) ;
        }
        return arrayOfFromTableType ;
    }
    
 
    private SchemaType getJoinTableType() {
        if( joinTableType == null ) {
            joinTableType = getType( AdqlData.JOIN_TABLE_TYPE ) ;
        }
        return joinTableType ;
    }
    
    
    private SchemaType getTableType() {
        if( tableType == null ) {
            tableType = getType( AdqlData.TABLE_TYPE ) ;
        }
        return tableType ;
    }
    

    private SchemaType getType( String localName ) {
        SchemaType target = null ;
        XmlObject root = ((AdqlEntry)(adqlTree.getModel().getRoot())).getXmlObject() ;
        SchemaType[] globalTypes = root.schemaType().getTypeSystem().globalTypes() ;
        for( int i=0; i<globalTypes.length; i++ ) {
            if( globalTypes[i].getName().getLocalPart().equals( localName ) ) {
                target = globalTypes[i] ;
                break ;
            }
        }
        return target ;
    }
    
} // end of class TableMetadataPanel
