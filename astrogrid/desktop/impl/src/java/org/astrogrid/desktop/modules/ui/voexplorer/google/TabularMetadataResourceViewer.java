/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import javax.swing.JTabbedPane;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.TabularMetadataViewer;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;

/** resource viewer that displays tabular metadata.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 200711:52:12 PM
 */
public class TabularMetadataResourceViewer extends TabularMetadataViewer implements ResourceViewer{


	public void addTo(UIComponentBodyguard parent, JTabbedPane t) {
		t.addTab("Tables",IconHelper.loadIcon("table16.png")
				,this,"Tabular schema for this resource");
				
	}

}
