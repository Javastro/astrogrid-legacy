/**
 * 
 */
package org.astrogrid.desktop.modules.adqlEditor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.xmlbeans.SchemaProperty;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandFactory;
import org.astrogrid.desktop.modules.adqlEditor.commands.MultipleColumnInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
import org.astrogrid.desktop.modules.ui.TabularMetadataViewer;

/** Extended tabular metadata viewer that integrates with the adql query builder.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 18, 200711:53:49 PM
 */
public class AdqlEditorTabularMetadataViewer extends TabularMetadataViewer {

	public AdqlEditorTabularMetadataViewer(final ADQLEditorPanel adqlToolEditorPanel, final AdqlTree adqlTree) {
		this.adqlTree = adqlTree;
		this.adqlToolEditorPanel = adqlToolEditorPanel;
		
		jtable.addMouseListener(new ContextPopup());
	}

    private final AdqlTree adqlTree ;
    private final ADQLEditorPanel adqlToolEditorPanel ;
    
    // copied from tableMetadataPanel
  private class ContextPopup extends MouseAdapter implements PopupMenuListener {
        
        private final JPopupMenu popup ;
        private final InsertAction insertAction ;
        
        public ContextPopup() {
            popup = new JPopupMenu( "ColumnMetadataContextMenu" ) ;
            popup.add( "Column References" ) ;
            popup.addSeparator() ; 
            insertAction = new InsertAction( "" ) ;
            popup.add( insertAction ) ;
            popup.addPopupMenuListener( this ) ;
        }
        
        public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {}
        
        public void popupMenuCanceled(final PopupMenuEvent e) {
            adqlToolEditorPanel.unsetElastic() ;
            adqlToolEditorPanel.updateDisplay() ;
            
        }
        public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
            adqlToolEditorPanel.unsetElastic() ;
            adqlToolEditorPanel.updateDisplay() ;
        }
        
        @Override
        public void mouseReleased( final MouseEvent event ) {
            
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
        @Override
        public void mousePressed(final MouseEvent event) {
        	if (event.isPopupTrigger() && adqlTree.isShowing()) {
        		processPopupClick(event);
        	}
        }
		/**
		 * @param event
		 */
		private void processPopupClick(final MouseEvent event) {
			final TreePath path = adqlTree.getSelectionPath() ;
			// If the path is null or there is no relevant parent
			// Then we cannot do anything with this entry...
			if( path == null || path.getPathCount() < 2 ) {
                return ;
            }  
			final int count = jtable.getSelectedRowCount() ;
			final StringBuffer buffer = new StringBuffer( 36 ) ;
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
			final JViewport viewPort = jtableScrollpane.getViewport() ;
			final Point popupPosition =
			    SwingUtilities.convertPoint( jtable, event.getPoint(), viewPort ) ;

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
			final Point rightSideOfPath = rect.getLocation() ;
			rightSideOfPath.x += (rect.width + 1) ;
			rightSideOfPath.y += rect.height / 2 ;
			final Point[] elastic = new Point[2] ;
			elastic[0] = SwingUtilities.convertPoint( jtable
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
	   
	    public InsertAction( final String contextInstruction ) {
	        super( contextInstruction ) ;
	    }
	    
	    private boolean testAndBuildSuitability() {
	        final TreePath path = adqlTree.getSelectionPath() ;
	        // If the path is null or there is no parent
	        // Then we cannot paste into this entry...
	        if( path == null || path.getPathCount() < 2 ) {
                return false ;
            }
	        // If the target allows for no children, then we dont paste into it.
	        AdqlNode entry = (AdqlNode)path.getLastPathComponent() ;
	        final SchemaProperty[] elements = entry.getElementProperties() ;
	        if( elements == null || elements.length == 0 ) {
                return false ;
            }  

	        final ColumnBean[] columnBeans = jtable.getSelected();
	        final CommandFactory factory = adqlTree.getCommandFactory() ;
            if( entry.getShortTypeName().equalsIgnoreCase( AdqlData.SELECT_TYPE ) ) {
                final AdqlNode[] cns = entry.getChildren() ;
                for( int i=0; i<cns.length; i++ ) {
                    if( cns[i].getShortTypeName().equalsIgnoreCase( AdqlData.SELECTION_LIST_TYPE ) ) {
                        entry = cns[i] ;
                    }
                        break ;
                }
            }
	        command = factory.newMultipleColumnInsertCommand( entry 
	                                                        , AdqlUtils.getType( entry.getXmlObject(), AdqlData.COLUMN_REFERENCE_TYPE ) ) ;
	        if( command == null ) {
	            return false ;
	        }
            command.setTable( (TableBean)tableCombo.getSelectedItem() ) ;
            command.setColumns( columnBeans ) ;
            return command.isChildEnabled() ;
	        
	    }
	       
	    public void actionPerformed( final ActionEvent e ) {
	        final TreePath path = adqlTree.getSelectionPath() ;
          if( path == null ) {
            return ;
        }
          
	        final CommandExec.Result result = command.execute()  ;   
	       	        
	        if( result != CommandExec.FAILED ) {
	            final DefaultTreeModel model =  (DefaultTreeModel)adqlTree.getModel() ;
	            model.nodeStructureChanged( command.getParentEntry() ) ;
	            model.nodeStructureChanged( command.findFromClause( path ) ) ;
                adqlTree.ensureSomeNodeSelected( command ) ;
	            adqlTree.repaint() ;
          }	        
	    }
	        
  }  
}
