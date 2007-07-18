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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandFactory;
import org.astrogrid.desktop.modules.adqlEditor.commands.MultipleColumnInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

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
    	POSITION	= "Col",
    	NAME 		= "Name",
    	UCD  		= "UCD",
    	UNITS 		= "Units",
    	TYPE 		= "Type",
    	DESCRIPTION = "Description" ;
    
    private TableBean astroTable ;
    private Catalog databaseBean ;
    // private int tableIndex ;
    private JTable displayTable ;
    private JScrollPane displayTableScrollPane ;
    private AdqlTree adqlTree ;
    private ADQLEditorPanel adqlToolEditorPanel ;
    private SchemaType columnReferenceType ;
    private SchemaType selectType ;
    private SchemaType fromType ;
    private SchemaType joinTableType ;
    private SchemaType arrayOfFromTableType ;
    private SchemaType tableType ;

//    public TableMetadataPanel( ADQLToolEditorPanel adqlToolEditorPanel
//                             , AdqlTree adqlTree
//                             , DatabaseBean dbBean
//                             , int tableIndex ) {
//        this( adqlToolEditorPanel, adqlTree, dbBean.getTables()[tableIndex] ) ;
//        this.dbBean = dbBean ;
//        this.tableIndex = tableIndex ;
//    }
    
    
    /**
     * 
     */
    public TableMetadataPanel( ADQLEditorPanel adqlToolEditorPanel
                             , AdqlTree adqlTree
                             , Catalog databaseBean 
                             , TableBean tableBean ) {
        super();
        this.adqlToolEditorPanel = adqlToolEditorPanel ;
        this.adqlTree = adqlTree ;
        this.databaseBean = databaseBean ;
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
        TableSorter  sorter = new TableSorter( astroTableModel );
        displayTable = new JTable( sorter ) ;
        displayTable.setAutoCreateColumnsFromModel( false ) ;
        sorter.addMouseListenerToHeaderInTable( displayTable );
        // displayTable.setModel( astroTableModel ) ;
        
//        for( int i=0 ; i<astroTableModel.getColumnCount(); i++ ) {
//            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() ;
//            renderer.setHorizontalAlignment( astroTableModel.displayColumnData[i].alignment ) ;
//            TableColumn 
//            	column = new TableColumn( i
//            	                        , astroTableModel.displayColumnData[i].width
//            	                        , renderer
//            	                        , null );
//            displayTable.addColumn( column ) ;
//        }
//        
        JTableHeader header = displayTable.getTableHeader() ;
        header.setUpdateTableInRealTime( false ) ;
        
        displayTableScrollPane = new JScrollPane() ; 
        displayTableScrollPane.getViewport().setBackground( displayTable.getBackground() ) ;
        displayTableScrollPane.getViewport().add( displayTable ) ;
        displayTable.addMouseListener( new ContextPopup() ) ;
        return displayTableScrollPane ;
    }
    
    class DisplayColumnData {
        public String title ;
        public int width ;
        public int alignment ;
        public Class clazz ;
       
        public DisplayColumnData( String title, int width, int alignment ) {
            this.title = title ;
            this.width = width ;
            this.alignment = alignment ;          
        }
    } // end of class DisplayColumnData
    
    class AstroTableModel extends AbstractTableModel {
        
        final public DisplayColumnData displayColumnData[] = {
        	    new DisplayColumnData( TableMetadataPanel.POSITION, 50, JLabel.LEFT ) ,
                new DisplayColumnData( TableMetadataPanel.NAME, 200, JLabel.LEFT ) ,
                new DisplayColumnData( TableMetadataPanel.UCD, 75, JLabel.LEFT ) ,
                new DisplayColumnData( TableMetadataPanel.UNITS, 75, JLabel.LEFT ) ,
                new DisplayColumnData( TableMetadataPanel.TYPE, 75, JLabel.LEFT ) ,
                new DisplayColumnData( TableMetadataPanel.DESCRIPTION, 200, JLabel.LEFT ) ,
        } ;

        public Class getColumnClass(int col) {
        	Class clss = null ;
        	try {
        		clss = getValueAt(0,col).getClass();
        	}
        	catch( Exception ex ) {
        		clss = Object.class ;
        	}
        	return clss ;
        }
        
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
            	case 0: return new Integer( rowIndex+1 ) ;
            	case 1: return astroColumn.getName() ;
            	case 2: return astroColumn.getUCD() ;
            	case 3: return astroColumn.getUnit() ;
            	case 4: return astroColumn.getDatatype() ;
            	case 5: return astroColumn.getDescription() ;
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
            
            if( event.isPopupTrigger()  && adqlTree.isShowing()
            ) {
    	        processPopupClick(event);
            }
                     
        } // end of ContextPopup.mouseReleased( MouseEvent event )
        
        //NWW - fix for mac. for portability, need to check on mousePressed 
        // and mouseReleased whether it's the 'popupTrigger' event.
        // onlny way to do it - as a mac CTRL-Cick gives a different event type to a Button-3 click.
        // complicated, eh?
        //http://developer.apple.com/documentation/Java/Conceptual/Java14Development/07-NativePlatformIntegration/NativePlatformIntegration.html
        public void mousePressed(MouseEvent event) {
        	if (event.isPopupTrigger() && adqlTree.isShowing()) {
        		processPopupClick(event);
        	}
        }
		/**
		 * @param event
		 */
		private void processPopupClick(MouseEvent event) {
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
				.append( ((AdqlNode)path.getLastPathComponent()).getDisplayName() )
				.append( "\"" ) ;
			insertAction.putValue( AbstractAction.NAME, buffer.toString() ) ;
			//
			// The mouse position is shown relative to the JTable.
			// As this is in a JScrollPane, it requires adjustment
			// according to what position the JViewport has on the 
			// JTable...
			JViewport viewPort = displayTableScrollPane.getViewport() ;
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
        
    } // end of class ContextPopup
    
    private class InsertAction extends AbstractAction {
        
        MultipleColumnInsertCommand command ;
	   
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
	        AdqlNode entry = (AdqlNode)path.getLastPathComponent() ;
	        SchemaProperty[] elements = entry.getElementProperties() ;
	        if( elements == null || elements.length == 0 ) 
	        	return false ;  
	                
	        // This is admittedly confusing terminology...
	        // Since the JTable is holding information about relational table columns
	        // in a display table with each row displaying metadata about a column!!!
	        int[] selectedColumnBeans = displayTable.getSelectedRows() ;
//	        AstroTableModel model = (AstroTableModel)displayTable.getModel() ;
	        TableSorter sorter = (TableSorter)displayTable.getModel() ;
	        AstroTableModel model = (AstroTableModel)sorter.getModel() ;
	        ColumnBean[] columnBeans = new ColumnBean[ selectedColumnBeans.length ] ;
	        
	        for( int i=0 ; i<selectedColumnBeans.length; i++ ) {
	            columnBeans[i] = model.getColumnBeanGivenRowIndex( sorter.indexes[selectedColumnBeans[i]] ) ;            
	        }
	        CommandFactory factory = adqlTree.getCommandFactory() ;
	        command = factory.newMultipleColumnInsertCommand( entry 
	                                                        , AdqlUtils.getType( entry.getXmlObject(), AdqlData.COLUMN_REFERENCE_TYPE ) ) ;
	        if( command == null ) {
	            return false ;
	        }
	        command.setArchive( databaseBean ) ;
	        command.setTable( astroTable ) ;
	        command.setColumns( columnBeans ) ;
	        return true ;
	        
	    }
	       
	    public void actionPerformed( ActionEvent e ) {
	        TreePath path = adqlTree.getSelectionPath() ;
            if( path == null )
                return ;
            
	        CommandExec.Result result = command.execute()  ;   
	       	        
	        if( result != CommandExec.FAILED ) {
                DefaultTreeModel model =  (DefaultTreeModel)adqlTree.getModel() ;
    	        model.nodeStructureChanged( command.getParentEntry() ) ;
    	        model.nodeStructureChanged( command.findFromClause( path ) ) ;
                adqlTree.repaint() ;
            }
	        
	        
	        
	        
	    }
	        
    }
   
} // end of class TableMetadataPanel
