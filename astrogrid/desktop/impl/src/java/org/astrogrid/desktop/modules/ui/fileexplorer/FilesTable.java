/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

/** table that will display a static table of files - no selection, no navigation.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 24, 20071:39:21 PM
 */
public class FilesTable extends JTable {
    private static final Log logger = LogFactory.getLog(FilesTable.class);

    
    public FilesTable(SortedList files, final IconFinder icons) {

        // setup the model.
        setModel(new EventTableModel(files,new StorageTableFormat(icons)));     


        // setup the behaviour
        setPreferredScrollableViewportSize(new Dimension(300,300));
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);     
        setAutoResizeMode(AUTO_RESIZE_NEXT_COLUMN);
        setBorder(BorderFactory.createEmptyBorder());
        setShowGrid(false);
        

        
        //allow sorting by columns.
        new TableComparatorChooser(this,files,true);
        
        //configure column widths
        final TableColumnModel cm = getColumnModel();
        cm.getColumn(0).setPreferredWidth(40);
        cm.getColumn(0).setMaxWidth(40);
        cm.getColumn(0).setMaxWidth(40);
        cm.getColumn(1).setMinWidth(200);
        cm.getColumn(2).setPreferredWidth(200);
        cm.getColumn(2).setMaxWidth(200);
        cm.getColumn(3).setPreferredWidth(70);
        cm.getColumn(3).setMaxWidth(70);
        cm.getColumn(4).setPreferredWidth(200);
        cm.getColumn(4).setMaxWidth(200);
                

        // alter how some columns are rendered.
        // show date-time for date objects
        setDefaultRenderer(Date.class,new DefaultTableCellRenderer.UIResource() {
            // doesn't give column alignment.DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
            DateFormat formatter = new SimpleDateFormat(" dd MMM yyyy, HH:mm");
            protected void setValue(Object value) {
                setText((value == null) ? "" : formatter.format(value));
            }
        });
        // add kb in some cases, don't display some cases.
        setDefaultRenderer(Long.class,new DefaultTableCellRenderer.UIResource() {
            {
                setHorizontalAlignment(JLabel.RIGHT);
            }
            protected void setValue(Object value) {
                long l =  value == null ? -1: ((Long)value).longValue();
                if (l == -1) {
                    setText(""); 
                } else if (l > 1024) {
                    setText(l/1024 + " MB");
                } else {
                    setText(l + " KB");
                }       
            }
        });     
        

                
    }

}