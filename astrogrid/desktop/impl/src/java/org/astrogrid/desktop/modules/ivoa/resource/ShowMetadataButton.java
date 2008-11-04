/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.voexplorer.google.TabularMetadataResourceViewer;

/** HTML-Embedded button to show metadata.
 * <p/>
 *  Attempts to flip tabs to metadata viewer
 *  also changes tab heading, to indicate when table metadata is present.
 * 
 * necessarily static so that it can be inserted
 * via a 'object' html tag with minimal hassle.
 * 
 * 
 * implementation relies on the 'table metata' view being the one following the formatted resource view.

 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 22, 20072:23:19 PM
 */
public final class ShowMetadataButton extends ResourceDisplayPaneEmbeddedButton implements ActionListener {
    /**
     * 
     */
    public ShowMetadataButton() {
           setText("Show Table Metadata");
           setIcon(TabularMetadataResourceViewer.STARRED_TABLE_ICON);           
           addActionListener(this);
           CSH.setHelpIDString(this,"reg.display.table");     
           
           // change the icon on the tabular metadata viewer tab.
           // sadly, the added event for a new item gets fired _before_ the removed
           // event for the current item - which means that if two table-bearing resources are
           // flipped between in the resource table, the icon being displayed is incorrect
           // (as the first resource changes the icon back _after_ the second resource has set it).
           // to circumvent this, this class stuffs itself into the client properties of the tabbed pane,
           // to indicate whether it has rights to re-set the icon.
           addAncestorListener(new AncestorListener() {

            public void ancestorAdded(final AncestorEvent event) {
                final JTabbedPane tabPane = findTabPane();
                final int ix = findIndexOfTabularTab(tabPane);
                tabPane.setIconAt(ix,TabularMetadataResourceViewer.STARRED_TABLE_ICON);
                tabPane.putClientProperty(RESET_ICON_KEY,ShowMetadataButton.this);
            }

            public void ancestorMoved(final AncestorEvent event) {
                // ignored
            }

            public void ancestorRemoved(final AncestorEvent event) {
                final JTabbedPane tabPane = findTabPane();
                if (ShowMetadataButton.this == tabPane.getClientProperty(RESET_ICON_KEY)) {
                    // another button hasn't already reset the icon
                    final int ix = findIndexOfTabularTab(tabPane);
                    tabPane.setIconAt(ix,TabularMetadataResourceViewer.TABLE_ICON);
                    tabPane.putClientProperty(RESET_ICON_KEY,null);
                }
            }
           });
           
    }
           public static String RESET_ICON_KEY = "showMetadata.reset.icon";
           


    public void actionPerformed(final ActionEvent e) {
        final JTabbedPane tabs = findTabPane();
        final int ix = findIndexOfTabularTab(tabs);        
        if (ix > -1) {
            tabs.setSelectedIndex(tabs.getSelectedIndex()+1);
        }

    }

    // lazily initialized
    private JTabbedPane _tPane;
    
    /**
     * @return
     */
    private JTabbedPane findTabPane() {
        if (_tPane == null) {
            _tPane =  (JTabbedPane)SwingUtilities.getAncestorOfClass(JTabbedPane.class,getParent());
        } 
        return _tPane;
    }
    
    // private, lazily initialized.
    private int _ix = -1;
    private int findIndexOfTabularTab(final JTabbedPane tabs) {        
        if (_ix ==-1 && tabs != null) {
            // uninitialized
            _ix = tabs.indexOfTab(TabularMetadataResourceViewer.TAB_TITLE);
        }
        return _ix;
    }
    
}
