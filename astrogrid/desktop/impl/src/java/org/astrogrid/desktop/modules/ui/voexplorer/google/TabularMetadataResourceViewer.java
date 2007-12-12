/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import javax.swing.JTabbedPane;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.TabularMetadataViewer;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** resource viewer that displays tabular metadata.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 200711:52:12 PM
 */
public class TabularMetadataResourceViewer extends TabularMetadataViewer implements ResourceViewer{


	public void addTo( JTabbedPane t) {
		t.addTab("Table Metadata",IconHelper.loadIcon("table16.png")
				,this,"View the table metadata provided by this resource");
				
	}

}
