/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.util.Comparator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.folders.StaticList;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Allows editing of static lists.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 30, 20072:05:03 PM
 */
public class StaticListEditingPanel extends EditingPanel implements ActionListener, ListSelectionListener, ListEventListener {
//	 defines the format of the table.
	private static class StaticListEditorTableFormat implements AdvancedTableFormat, WritableTableFormat {

		public Class getColumnClass(int arg0) {
			return URI.class;
		}

		public Comparator getColumnComparator(int arg0) {
			return GlazedLists.comparableComparator();
		}

		public int getColumnCount() {
			return 1;
		}

		public String getColumnName(int arg0) {
			return "Identifier";
		}

		public Object getColumnValue(Object arg0, int arg1) {
			return arg0;
		}

		public boolean isEditable(Object arg0, int arg1) {
			return true;
		}

		public Object setColumnValue(Object arg0, Object arg1, int arg2) {
			// easy - edited value is already a URI at this point.
			return arg1;
		}
	}
	/**
	 * 
	 */
	public StaticListEditingPanel() {
		FormLayout layout = new FormLayout(
				"2dlu,right:d,1dlu,max(30dlu;d):grow,4dlu,d,1dlu,d,3dlu" // cols
				,"d,d,d,1dlu:grow,d" // rows
				);
		PanelBuilder builder = new PanelBuilder(layout,this);
		CellConstraints cc = new CellConstraints();
		int row = 1;
		builder.addLabel("The list named:",cc.xy(2,row));
		folderName.setText("new list");
		folderName.setColumns(20);
		builder.add(folderName,cc.xy(4,row));
		
		row++;
		builder.addLabel("Contains the resources:",cc.xyw(2,row,3));
		
		row++;
		ids = new BasicEventList();
		ids.addListEventListener(this);
		table = new JTable(new EventTableModel(ids,new StaticListEditorTableFormat()));
		selection = new EventSelectionModel(ids);
		selection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionModel(selection);
		selection.addListSelectionListener(this);
		table.setDragEnabled(true);
		
		final JScrollPane scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		builder.add(scrollPane	,cc.xyw(2,row,5));
	
		Box b = new Box(BoxLayout.Y_AXIS);
		add = new JButton(IconHelper.loadIcon("editadd16.png"));
		add.setToolTipText("Add a resource to this list");
		remove = new JButton(IconHelper.loadIcon("editremove16.png"));
		remove.setEnabled(false);
		remove.setToolTipText("Remove a resource from this list");
		add.addActionListener(this);
		remove.addActionListener(this);
		b.add(add);
		b.add(remove);
		builder.add(b,cc.xy(8,row));
		
		row++;
		// blank space left to grow.
		row++;
		builder.add(ok,cc.xy(6,row));
		builder.add(cancel,cc.xy(8,row));			
		
	}
	
	private final JTable table;
	private final JButton add;
	private final JButton remove;
	private final EventList ids;
	private final EventSelectionModel selection;
	
	public void setCurrentlyEditing(ResourceFolder currentlyEditing) {
		if (!(currentlyEditing instanceof StaticList)) {
			throw new IllegalArgumentException("Not an instanceof StaticList");
		}
		super.setCurrentlyEditing(currentlyEditing);
		StaticList sl = (StaticList)getCurrentlyEditing();
		ids.clear();
		ids.addAll(sl.getResourceSet());
	}
	
	public void loadEdits() {
		super.loadEdits();
		StaticList sl = (StaticList)getCurrentlyEditing();
		sl.setResourceSet(ids);
	}
// responds to button clicks.
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == add) {
			int insPos = ids.size();
			URI nu = URI.create("ivo://edit.me");
			ids.add(insPos,nu);
			Rectangle rect =  table.getCellRect(insPos, 0, true);
			table.scrollRectToVisible(rect);
			table.editCellAt(insPos,0);
		} else if (e.getSource() == remove && ! selection.isSelectionEmpty()) {
			ids.remove(selection.getSelected().get(0));
		}
	}

	// listens to selecito in table.
	public void valueChanged(ListSelectionEvent e) {
		remove.setEnabled( ! selection.isSelectionEmpty());
	}

// listens to changes of id list.
	public void listChanged(ListEvent arg0) {
		while (arg0.hasNext()) {
			arg0.next();
			ok.setEnabled(shouldOkBeEnabled());
		}
	}
	// extended to guard against empty resource lists.
	protected boolean shouldOkBeEnabled() {
		return super.shouldOkBeEnabled() && (ids != null ?  ids.size() > 0 : true);
	}
}