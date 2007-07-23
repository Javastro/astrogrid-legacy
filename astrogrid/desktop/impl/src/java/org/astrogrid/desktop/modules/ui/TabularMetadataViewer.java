/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.commons.lang.text.StrBuilder;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.astrogrid.TableBeanComparator;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.comp.IconField;
import org.astrogrid.desktop.modules.ui.comp.TextAreaRenderer;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** generic table metadata viewer.
 * @author Noel.Winstanley@manchester.ac.uk
 *  * @FIXME fine-tune resizing - at the moment the table slides away, rather than shrinking.
 * 
 * @todo add datatips for columns.
 * @since Jul 18, 20072:22:45 PM
 */
public class TabularMetadataViewer extends JPanel implements ItemListener {
	/**
	 * 
	 */
	public TabularMetadataViewer() {
		catalogues = new BasicEventList();
		tables = new BasicEventList();
		columns = new BasicEventList();

		// build the panel. 
		FormLayout layout = new FormLayout("right:d, 3dlu,100dlu,3dlu:grow" // cols
				,"d,max(10dlu;d),d,max(10dlu;d),fill:100dlu:grow,d" // rows
		);
		PanelBuilder builder = new PanelBuilder(layout,this);
		CellConstraints cc = new CellConstraints();

		int row =1;

		builder.addLabel("Catalogue",cc.xy(1,row));

		cataCombo = new JComboBox(new EventComboBoxModel(catalogues));
		cataCombo.addItemListener(this);
		cataCombo.setRenderer(new BasicComboBoxRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (value != null) {
					value = ((Catalog)value).getName();
				}
				return super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
			}
		});
		cataCombo.setEditable(false);
		builder.add(cataCombo,cc.xy(3,row++));

		cataLabel = builder.addLabel("",cc.xyw(1,row++,4));

		builder.addLabel("Table",cc.xy(1,row));
		// make this one auto-complete.
		//@todo should I sort these tables?
		EventList sortedTables = new SortedList(tables,new TableBeanComparator());
		tableCombo = new JComboBox();
		tableCombo.addItemListener(this);
		AutoCompleteSupport acs = AutoCompleteSupport.install(tableCombo,sortedTables,new TextFilterator(){
			public void getFilterStrings(List baseList, Object element) {
				TableBean tb = (TableBean)element;
				baseList.add(tb.getName());
				baseList.add(tb.getDescription());
			}
		}
		, new Format() {
			public StringBuffer format(Object obj, StringBuffer toAppendTo,
					FieldPosition pos) {
				TableBean tb = (TableBean)obj;
				if (tb != null) {
					toAppendTo.append(tb.getName());
				} 
				return toAppendTo;
			}
			public Object parseObject(String source, ParsePosition pos) {
				for (Iterator i = tables.iterator(); i.hasNext();) {
					TableBean tb = (TableBean) i.next();
					if (source.equals(tb.getName())) {
						return tb;
					}
				}
				return null;
			}
		});
		tableCombo.setRenderer(new BasicComboBoxRenderer() {
			// I've tried to optimize this a bit,as it's a tight inner loop.
			StrBuilder sb = new StrBuilder(100);
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (value != null) {
				    final TableBean tb = (TableBean)value;
				    // re-use a string builder, rather than contatenating strings each time.
				    sb.clear();
				    sb.append(tb.getName())
				        .append(" : ")
				        .appendFixedWidthPadRight(tb.getDescription(),50,' ');
					value = sb.toString();
				}
				return super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
			}
		});
		acs.setStrict(false);
		acs.setSelectsTextOnFocusGain(true);
		acs.setFilterMode(TextMatcherEditor.CONTAINS);
		builder.add(tableCombo,cc.xy(3,row++));

		tableLabel = builder.addLabel("",cc.xyw(1,row++,4));

		// add a text-filter to the columns list
		JTextField filterField = new IconField(0);
		filterField.setToolTipText("Filter columns");
		FilterList filteredColumns = new FilterList(columns,
					new TextComponentMatcherEditor(filterField, new ColumnTextFilterator()));
		
		// necessary to have sorted columns before 
		SortedList sortedColumns = new SortedList(filteredColumns,new Comparator() {

			public int compare(Object arg0, Object arg1) {
				ColumnBean a = (ColumnBean)arg0;
				ColumnBean b = (ColumnBean)arg1;
				return a.getName().compareTo(b.getName());
			}
		});
		jtable = new MetadataTable(sortedColumns );
				
		jtableScrollpane = new JScrollPane(jtable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jtableScrollpane.setBorder(BorderFactory.createEmptyBorder());
		jtableScrollpane.setMinimumSize(new Dimension(50,50));
		jtableScrollpane.getViewport().setBackground(jtable.getBackground());
		builder.add(jtableScrollpane,cc.xyw(1,row++,4));
		
		// add the text-filter box at the bottom of the ui.
		builder.addLabel("Filter columns by",cc.xy(1,row));
		builder.add(filterField,cc.xy(3,row));
		
	}
	
	private static class ColumnTextFilterator implements TextFilterator {

		public void getFilterStrings(List baseList, Object element) {
			ColumnBean cb = (ColumnBean) element;
			if (cb.getDatatype() != null) {
				baseList.add(cb.getDatatype());
			}
			if (cb.getDescription() != null) {
				baseList.add(cb.getDescription());
			}
			if (cb.getName() != null) {
				
				baseList.add(cb.getName());
			}
			if (cb.getUCD() != null) {
				baseList.add(cb.getUCD());
			}
			if (cb.getUnit() != null) {
				baseList.add(cb.getUnit());
			}
			
		}
	}

	
	/** top level list - of catalogues */
	protected final EventList catalogues;
	/** list of tables in the selected catalogue */
	protected final EventList tables;
	/** list of colums in the selected table */
	protected final EventList columns;
	
	/** dropdown list of catalogues */
	protected final JComboBox cataCombo;
	/** label describing the selected catalog */
	protected final JLabel cataLabel;
	/** dropdown listr of tables */
	protected final JComboBox tableCombo;
	/** description of the selected table */
	protected final JLabel tableLabel;
	/** the table displaying column metadata */
	protected final MetadataTable jtable;

	/** scrollpane containing jtable */
	protected final  JScrollPane jtableScrollpane;

	public void clear() {
		
		cataCombo.setSelectedIndex(-1);
		tableCombo.setSelectedIndex(-1);
		
		cataCombo.setEnabled(false);
		tableCombo.setEnabled(false);
	}

	public void display(Resource res) {
		catalogues.clear();
		tables.clear();
		columns.clear();
		//@todo maybe fallback to resource title if catalogue lacks a description.
		cataLabel.setText("");
		tableLabel.setText("");
		
		if (res instanceof DataCollection) {
			DataCollection coll = (DataCollection)res;
			List colList = Arrays.asList(coll.getCatalogues());
			catalogues.addAll(colList);
			cataCombo.setEnabled(colList.size() > 1);
			cataCombo.setSelectedIndex(0);
		
		} else if (res instanceof CatalogService) {
			CatalogService serv = (CatalogService)res;
			Catalog cat =new Catalog();
			cat.setName("");
			cat.setTables(serv.getTables());
			catalogues.add(cat);
			cataCombo.setEnabled(false);
			cataCombo.setSelectedIndex(0);
			
		} else { // it's not of interest.
			cataCombo.setEnabled(false);
			cataCombo.setSelectedIndex(-1);			
			tableCombo.setEnabled(false);
			tableCombo.setSelectedIndex(-1);
		}
	}

	public void itemStateChanged(ItemEvent e) {

		if(e.getStateChange() != ItemEvent.SELECTED) {
			return;
		}
		if (e.getSource() == cataCombo) {
			Catalog c = (Catalog)e.getItem();
			cataLabel.setText(c == null || c.getDescription() == null 
					? "" : "<html>" + c.getDescription());
			tables.clear();
			tables.addAll(Arrays.asList(c.getTables()));
			if (c.getTables().length > 0) {
				tableCombo.setSelectedIndex(0);
			}
			tableCombo.setEnabled(c.getTables().length > 1);
		} else if (e.getSource() == tableCombo) {
			TableBean tb = (TableBean)e.getItem();
			tableLabel.setText(tb == null || tb.getDescription() == null 
					? "" : "<html>" + tb.getDescription());
			columns.clear();
			columns.addAll(Arrays.asList(tb.getColumns()));
		}
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

	/** display a list of columns */
	public static class MetadataTable extends JTable {
		/** model containing the current _selection_ in jtable */
		private final EventSelectionModel tableSelection;	
		
		public MetadataTable(SortedList columns) { // sorted list required by TableComparatorChooser
			SortedList sortedColumns = new SortedList(columns
					,GlazedLists.beanPropertyComparator(ColumnBean.class,"name"));
			setModel(new EventTableModel(sortedColumns,new MetadataTableFormat()));
			new TableComparatorChooser(this,sortedColumns,false);
			tableSelection = new EventSelectionModel(columns);
			setSelectionModel(tableSelection);
			tableSelection.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			getColumnModel().getColumn(0).setPreferredWidth(75);
			getColumnModel().getColumn(1).setPreferredWidth(150);
			getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
			getColumnModel().getColumn(2).setPreferredWidth(50);
			getColumnModel().getColumn(3).setPreferredWidth(75);
			getColumnModel().getColumn(4).setPreferredWidth(50);	
			setPreferredScrollableViewportSize(new Dimension(50,50));			
		}
		
		public ColumnBean[] getSelected() {
			List l = tableSelection.getSelected();
			return (ColumnBean[])l.toArray(new ColumnBean[l.size()]);
		}
	}
}
