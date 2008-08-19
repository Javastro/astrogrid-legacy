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
import org.astrogrid.acr.ivoa.resource.TableDataType;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.comp.SearchField;
import org.astrogrid.desktop.modules.ui.comp.TextAreaRenderer;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
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
 * 
 * @todo add datatips for columns.
 * @since Jul 18, 20072:22:45 PM
 */
public class TabularMetadataViewer extends JPanel implements ItemListener {
	/**
	 * 
	 */
	public TabularMetadataViewer() {
	    CSH.setHelpIDString(this,"table.metadata");
		catalogues = new BasicEventList();
		tables = new BasicEventList();
		columns = new BasicEventList();

		// build the panel. 
		final FormLayout layout = new FormLayout("right:d, 3dlu,100dlu:grow,1dlu,d" // cols
				,"d,max(10dlu;d),d,max(10dlu;d),fill:m:grow" // rows
		);
		final PanelBuilder builder = new PanelBuilder(layout,this);
		final CellConstraints cc = new CellConstraints();

		int row =1;

		builder.addLabel("Catalogue",cc.xy(1,row));

		cataCombo = new JComboBox(new EventComboBoxModel(catalogues));
		cataCombo.addItemListener(this);
		cataCombo.setRenderer(new BasicComboBoxRenderer() {
			@Override
            public Component getListCellRendererComponent(final JList list, Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
				if (value != null) {
					value = ((Catalog)value).getName();
				}
				return super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
			}
		});
		cataCombo.setEditable(false);
		builder.add(cataCombo,cc.xy(3,row++));

		cataLabel = builder.addLabel("",cc.xyw(1,row++,5));
		cataLabel.setFont(UIConstants.SMALL_DIALOG_FONT);

		builder.addLabel("Table",cc.xy(1,row));
		// make this one auto-complete.
		//@todo should I sort these tables?
		final EventList sortedTables = new SortedList(tables,new TableBeanComparator());
		tableCombo = new JComboBox();
		tableCombo.addItemListener(this);
		final AutoCompleteSupport acs = AutoCompleteSupport.install(tableCombo,sortedTables,new TextFilterator(){
			public void getFilterStrings(final List baseList, final Object element) {
				final TableBean tb = (TableBean)element;
				baseList.add(tb.getName());
				baseList.add(tb.getDescription());
				if (tb.getRole() != null) {
				    baseList.add(tb.getRole());
				}
				                           
			}
		}
		, new Format() {
			@Override
            public StringBuffer format(final Object obj, final StringBuffer toAppendTo,
					final FieldPosition pos) {
				final TableBean tb = (TableBean)obj;
				if (tb != null) {
					toAppendTo.append(tb.getName() == null ? "" : tb.getName() );
					if (tb.getRole() != null) {
					    
					toAppendTo.append(" (")
					    .append(tb.getRole())
					    .append(")");
					}
				} 
				return toAppendTo;
			}
			@Override
            public Object parseObject(final String source, final ParsePosition pos) {
				for (final Iterator i = tables.iterator(); i.hasNext();) {
					final TableBean tb = (TableBean) i.next();
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
			@Override
            public Component getListCellRendererComponent(final JList list, Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
				if (value != null) {
				    final TableBean tb = (TableBean)value;
				    // re-use a string builder, rather than contatenating strings each time.
				    sb.clear();
				    sb.append(tb.getName())
				        .append(" : ");
				    if (tb.getRole() != null) {
				        sb.append(tb.getRole());
				        sb.append(" : ");
				    }
				    if (tb.getDescription() != null) {
				        sb.appendFixedWidthPadRight(tb.getDescription(),50,' ');
				    }
					value = sb.toString();
				}
				return super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
			}
		});
		acs.setStrict(false);
		acs.setSelectsTextOnFocusGain(true);
		acs.setFilterMode(TextMatcherEditor.CONTAINS);
		builder.add(tableCombo,cc.xy(3,row));

		// add a text-filter to the columns list
		final SearchField filterField = new SearchField("Filter columns");
		final FilterList filteredColumns = new FilterList(columns,
					new TextComponentMatcherEditor(filterField.getWrappedDocument(), new ColumnTextFilterator()));

        // add the text-filter box to the same row as the table combo.
        builder.add(filterField,cc.xy(5,row++));		
        
        tableLabel = builder.addLabel("",cc.xyw(1,row++,5));
        tableLabel.setFont(UIConstants.SMALL_DIALOG_FONT);		
		

		jtable = new MetadataTable(filteredColumns );
				
		jtableScrollpane = new JScrollPane(jtable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jtableScrollpane.setBorder(BorderFactory.createEmptyBorder());
		jtableScrollpane.setMinimumSize(new Dimension(50,50));
		jtableScrollpane.getViewport().setBackground(jtable.getBackground());
		builder.add(jtableScrollpane,cc.xyw(1,row++,5));		
	}
	
	private static class ColumnTextFilterator implements TextFilterator {

		public void getFilterStrings(final List baseList, final Object element) {
			final NumberedColumnBean n = (NumberedColumnBean) element;
			final ColumnBean cb = n.cb;			
			if (cb.getColumnDataType() != null) {
			    final String t = cb.getColumnDataType().getType();
			    if (t != null) {
			        baseList.add(t);
			    }
			}
			if (cb.getDescription() != null) {
				baseList.add(cb.getDescription());
			}
			if (cb.getName() != null) {
				
				baseList.add(cb.getName());
			}
			if (cb.getUcd() != null) {
				baseList.add(cb.getUcd());
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
        catalogues.clear();               
        tables.clear();
        columns.clear();
        //@todo maybe fallback to resource title if catalogue lacks a description.
        cataLabel.setText("");
        tableLabel.setText("");
        
	    cataCombo.setSelectedIndex(-1);
		tableCombo.setSelectedIndex(-1);
		
		cataCombo.setEnabled(false);
		tableCombo.setEnabled(false);
	}

	public void display(final Resource res) {
	    clear(); // reset everything first.
		if (res instanceof DataCollection && ((DataCollection)res).getCatalogues().length > 0) {
			final DataCollection coll = (DataCollection)res;
			final List colList = Arrays.asList(coll.getCatalogues());
			    catalogues.addAll(colList);
			    cataCombo.setEnabled(true);
			    cataCombo.setSelectedIndex(0);
		
		} else if (res instanceof CatalogService && ((CatalogService)res).getTables().length > 0) {
			final CatalogService serv = (CatalogService)res;
			final Catalog cat =new Catalog();
			cat.setName("");
			cat.setTables(serv.getTables());
			catalogues.add(cat);
			cataCombo.setSelectedIndex(0);
			
		} else { // it's not of interest.		    
			cataLabel.setText("This resource provides no table metadata");
		}
		
	}

	public void itemStateChanged(final ItemEvent e) {

		if(e.getStateChange() != ItemEvent.SELECTED) {
			return;
		}
		if (e.getSource() == cataCombo) {
			final Catalog c = (Catalog)e.getItem();
			cataLabel.setText(c == null || c.getDescription() == null 
					? "" : "<html>" + c.getDescription());
			tables.clear();
			if (c != null && c.getTables().length > 0) {
			    tables.addAll(Arrays.asList(c.getTables()));
				tableCombo.setSelectedIndex(0);
			}
			tableCombo.setEnabled(c != null && c.getTables().length > 1);
		} else if (e.getSource() == tableCombo) {
			final TableBean tb = (TableBean)e.getItem();
			tableLabel.setText(tb == null || tb.getDescription() == null 
					? "" : "<html>" + tb.getDescription());
			columns.clear();
//			columns.addAll(Arrays.asList(tb.getColumns()));
			if (tb != null) {
			final ColumnBean[] cbs = tb.getColumns();
            for (int i = 0; i < cbs.length; i++) {
                columns.add(new NumberedColumnBean(cbs[i],i));
            }
			}
		}
	}
	    // datastructure used to add an 'index' column to the column beans.
	   private static class NumberedColumnBean {
	        public final ColumnBean cb;
	        public final Integer ix;
	        public NumberedColumnBean(final ColumnBean cb, final int n) {
	            super();
	            this.cb = cb;
	            this.ix = Integer.valueOf(n+1);
	        }
	    }
	
/** hacked around a little bit to return row numbers */
	private static class MetadataTableFormat implements AdvancedTableFormat {

		public int getColumnCount() {
			return 6;
		}

		public String getColumnName(final int arg0) {
		    switch (arg0) {
		        case 0:
		            return "#";
		        case 1:
		            return "Column Name";
		        case 2:
		            return "Description";
		        case 3:
		            return "Datatype";
		        case 4:
		            return "UCD";
		        case 5:
		            return "Units";
		        default:
		            return "Unknown";
		    }
		}
		
		public Object getColumnValue(final Object arg0, final int arg1) {
		    final NumberedColumnBean n = (NumberedColumnBean)arg0;
            //
            //JL: Changed to always return something.
            //    Otherwise the comparator throws a NullPointerException.
		    switch(arg1) {
		        case 0:
		           return n.ix;
		        case 1:
		            return provideEmptyDefault( n.cb.getName() );
		        case 2:
		            return provideEmptyDefault( n.cb.getDescription() );
		        case 3:
		            final TableDataType type = n.cb.getColumnDataType();
		            if (type == null) {
		                return "";
		            } 
		            if (type.getArraysize() != null && ! type.getArraysize().equals("1")) {
                        return  type.getType() + " " + type.getArraysize();		                
		            } else {
		                return provideEmptyDefault( type.getType());
		            }
		        case 4:
		            return provideEmptyDefault( n.cb.getUcd() );
		        case 5:
		            return provideEmptyDefault( n.cb.getUnit() );
		        default:
		            return "" ;
		    }
		}
        
        private String provideEmptyDefault( final String value ) {
            if( value != null ) {
                return value ;
            }
            return "" ;
        }

        public Class getColumnClass(final int column) {
            switch (column) {
                case 0:
                    return Integer.class;
                default:
                    return String.class;
            }
        }

        public Comparator getColumnComparator(final int column) {
            switch (column) {
                case 0:
                    return GlazedLists.comparableComparator();
                default:   
                    return GlazedLists.caseInsensitiveComparator();
            }
        }
	}


	
	/** display a list of columns */
	public static class MetadataTable extends JTable {
		/** model containing the current _selection_ in jtable */
		private final EventSelectionModel tableSelection;	
		
		public MetadataTable(final EventList columns) { // sorted list required by TableComparatorChooser
            
		    final SortedList sortedColumns = new SortedList(columns,
		            new Comparator() {

                        public int compare(final Object arg0, final Object arg1) {
                            final NumberedColumnBean a = (NumberedColumnBean)arg0;
                            final NumberedColumnBean b = (NumberedColumnBean)arg1;
                            return a.ix.compareTo(b.ix);
                        }
		    });
                       
			setModel(new EventTableModel(sortedColumns,new MetadataTableFormat()));
			new TableComparatorChooser(this,sortedColumns,false);
//JL bug# 2419 tableSelection = new EventSelectionModel( columns ) ;
//JL EventSelectionModel requires the sorted list...
            tableSelection = new EventSelectionModel( sortedColumns ) ;
			setSelectionModel(tableSelection);
			tableSelection.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			getColumnModel().getColumn(0).setPreferredWidth(10);
			getColumnModel().getColumn(1).setPreferredWidth(75);
			getColumnModel().getColumn(2).setPreferredWidth(150);
			getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer());
			getColumnModel().getColumn(3).setPreferredWidth(50);
			getColumnModel().getColumn(4).setPreferredWidth(75);
			getColumnModel().getColumn(5).setPreferredWidth(50);	
			setPreferredScrollableViewportSize(new Dimension(50,50));			
		}
		
		public ColumnBean[] getSelected() {
			final List l = tableSelection.getSelected();
			final ColumnBean[] result = new ColumnBean[l.size()];
			final Iterator i = l.iterator();
			for (int ix = 0; i.hasNext(); ix++) {
                result[ix] = ((NumberedColumnBean) i.next()).cb;                
            }
			return result;
		}
	}
}
