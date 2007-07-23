/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;

import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.TabularMetadataViewer;
import org.astrogrid.desktop.modules.ui.comp.TextAreaRenderer;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

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
