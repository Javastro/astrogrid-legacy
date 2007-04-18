/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry;

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
import javax.swing.JTable;
import javax.swing.ListCellRenderer;

import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.comp.TextAreaRenderer;

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

/** viewer that displays tabular metadata.
 * @todo add datatips for columns.
 * @FIXME hook up the 2 combo boxes.
 * @FIXME - somehow mark this tab as disabled when not applicable.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 200711:52:12 PM
 */
public class TabularMetadataViewer extends JTable implements ResourceViewer{
	public TabularMetadataViewer() {
		catalogues = new BasicEventList();
		tables = new BasicEventList();
		columns = new BasicEventList();
		SortedList sortedColumns = new SortedList(columns
				,GlazedLists.beanPropertyComparator(ColumnBean.class,"name"));
		setModel(new EventTableModel(sortedColumns,new MetadataTableFormat()));
		new TableComparatorChooser(this,sortedColumns,false);
		
		getColumnModel().getColumn(0).setPreferredWidth(100);
		getColumnModel().getColumn(1).setPreferredWidth(250);
		getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
		getColumnModel().getColumn(2).setPreferredWidth(50);
		getColumnModel().getColumn(3).setPreferredWidth(75);
		getColumnModel().getColumn(4).setPreferredWidth(75);	
		setPreferredScrollableViewportSize(new Dimension(400,200));
		// build the panel. 
		FormLayout layout = new FormLayout("right:pref, 3dlu,max(100dlu;pref),3dlu:grow" // cols
				,"p,2dlu,max(10dlu;pref),3dlu, p,2dlu,max(10dlu;pref),3dlu,pref:grow" // rows
				);
		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
		builder.addLabel("Catalogue",cc.xy(1,1));
		cataCombo = new JComboBox(new EventComboBoxModel(catalogues));
		cataCombo.setRenderer(new ListCellRenderer() {
			ListCellRenderer orig = cataCombo.getRenderer();
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel l = (JLabel)orig.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
				if (value != null) {
					l.setText(((Catalog)value).getName());
				}
				return l;
			}
		});
		cataCombo.setEditable(false);
		builder.add(cataCombo,cc.xy(3,1));
		cataLabel = builder.addLabel("",cc.xyw(1,3,4));
		builder.addLabel("Table",cc.xy(1,5));
		final JComboBox tableCombo = new JComboBox(new EventComboBoxModel(tables));
		tableCombo.setRenderer(new ListCellRenderer() {
			ListCellRenderer orig = tableCombo.getRenderer();
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel l = (JLabel)orig.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
				if (value != null) {
					l.setText(((TableBean)value).getName());
				}
				return l;
			}
		});
		tableCombo.setEditable(false);
		builder.add(tableCombo,cc.xy(3,5));
		tableLabel = builder.addLabel("",cc.xyw(1,7,4));
		// controller code.
		cataCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
				Catalog c = (Catalog)e.getItem();
				cataLabel.setText(c == null || c.getDescription() == null 
						? "" : "<html>" + c.getDescription());
				tables.clear();
				tables.addAll(Arrays.asList(c.getTables()));
				if (c.getTables().length > 0) {
					tableCombo.setSelectedIndex(0);
				}
				tableCombo.setEnabled(c.getTables().length > 1);
				}
			}
		});
		tableCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					TableBean tb = (TableBean)e.getItem();
					tableLabel.setText(tb == null || tb.getDescription() == null 
							? "" : "<html>" + tb.getDescription());
					columns.clear();
					columns.addAll(Arrays.asList(tb.getColumns()));
				}
			}
		});
		
		
		final JScrollPane scrollPane1 = new JScrollPane(this,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane1.setBorder(BorderFactory.createEmptyBorder());
		builder.add(scrollPane1,cc.xyw(1,9,4));
		panel = builder.getPanel();
	

	}
	private final EventList catalogues;
	private final JPanel panel;
	private final JComboBox cataCombo;
	private final EventList tables;
	private final EventList columns;
	private final JLabel cataLabel;
	private final JLabel tableLabel;
	public void clear() {
		catalogues.clear();
		tables.clear();
		columns.clear();
		cataLabel.setText("");
		tableLabel.setText("");
	}

	public void display(Resource res) {
		clear();
		if (res instanceof DataCollection) {
			DataCollection coll = (DataCollection)res;
			catalogues.addAll(Arrays.asList(coll.getCatalogues()));
			cataCombo.setEnabled(true);
			if (coll.getCatalogues().length > 0) {
				cataCombo.setSelectedIndex(0);
			}
			cataCombo.setEnabled(coll.getCatalogues().length > 1);
		} else if (res instanceof CatalogService) {
			CatalogService serv = (CatalogService)res;
			Catalog cat =new Catalog();
			cat.setName("-");
			cat.setTables(serv.getTables());
			catalogues.add(cat);
			cataCombo.setEnabled(false);
			cataCombo.setSelectedIndex(0);
		} 
	}

	public JComponent getComponent() {
		return panel;
	}

	private static class MetadataTableFormat implements TableFormat {

		public int getColumnCount() {
			return 5;
		}

		public String getColumnName(int arg0) {
			switch (arg0) {
			case 0:
				return "Column Name";
			case 1:
				return "Description";
			case 2:
				return "Datatype";
			case 3:
				return "UCD";
			case 4:
				return "Units";
			default:
				return "Unknown";
			}
		}

		public Object getColumnValue(Object arg0, int arg1) {
			ColumnBean cb = (ColumnBean)arg0;
			switch(arg1) {
				case 0:
					return cb.getName();
				case 1:
					return cb.getDescription();
				case 2:
					return cb.getDatatype();
				case 3:
					return cb.getUCD();
				case 4:
					return cb.getUnit();
				default:
					return null;
			}
		}
	}

}
