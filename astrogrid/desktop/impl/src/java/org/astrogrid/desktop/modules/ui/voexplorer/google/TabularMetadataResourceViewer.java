/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.resource.ShowMetadataButton;
import org.astrogrid.desktop.modules.ui.TabularMetadataViewer;

/** A {@link ResourceViewer} that displays tabular metadata for a resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 200711:52:12 PM
 */
public class TabularMetadataResourceViewer extends TabularMetadataViewer implements ResourceViewer{
    
    /**
     * 
     */
    public static final ImageIcon TABLE_ICON = IconHelper.loadIcon("table16.png");
    public static final ImageIcon STARRED_TABLE_ICON = IconHelper.loadIcon("starredtable16.png");
    /** constant - used to to locate this tab from {@link ShowMetadataButton} */
    public static final String TAB_TITLE = "Table Metadata";
	public void addTo( final JTabbedPane t) {
		t.addTab(TAB_TITLE,TABLE_ICON
				,this,"View the table metadata provided by this resource");
				
	}

}
