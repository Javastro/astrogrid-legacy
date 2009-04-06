/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Component;
import java.text.NumberFormat;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.fileexplorer.NavigableFilesTable;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** Table for displaying results of astroscope queries.
 *  - based on files table, but provides additional columns of metadata
 *      offset, and absolute position (switchable between decimal and sexagesimal)
 *  - needs to handle cases when this meatadata isn't presnt (cone responses, etc)
 *  
 *  could also do with providing tooltips from astroscoope too.
 *  implemented as an adapter / delegate in front of the original table format - seems more
 *  resilient to future change this way.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 6, 20084:33:43 PM
 */
public class ResultsFilesTable extends NavigableFilesTable implements AdvancedTableFormat<FileObjectView> {

    private final AdvancedTableFormat<FileObjectView> originalFormat;

    /**
     * @param navigator
     */
    public ResultsFilesTable(final FileNavigator navigator) {
        
        super(navigator);
        //@todo new help string.

        // change table format - so we can display additional colummns.
        final EventTableModel<FileObjectView> model = (EventTableModel<FileObjectView>)getModel();
        this.originalFormat = (AdvancedTableFormat<FileObjectView>)model.getTableFormat();
        model.setTableFormat(this);
        
        final TableColumnModel cm = getColumnModel();
        // copied from superclass - yuck. - dunno why we've got to do this again.
        
        cm.getColumn(0).setPreferredWidth(40);
        cm.getColumn(0).setMaxWidth(40);
        cm.getColumn(0).setMaxWidth(40);
        cm.getColumn(1).setMinWidth(200);
        cm.getColumn(2).setPreferredWidth(200);
        cm.getColumn(2).setMaxWidth(200);
        cm.getColumn(3).setPreferredWidth(90);
        cm.getColumn(3).setMaxWidth(90);
        cm.getColumn(4).setPreferredWidth(200);
        cm.getColumn(4).setMaxWidth(200);
        setDefaultRenderer(Double.class,new DefaultTableCellRenderer() {
            private final  NumberFormat format;
            {
                this.format = NumberFormat.getInstance();
                format.setGroupingUsed(false);
                format.setMaximumFractionDigits(6);
                format.setMinimumFractionDigits(6);
                //((DecimalFormat)format).set
            }
            @Override
            public Component getTableCellRendererComponent(final JTable table,
                    final Object value, final boolean isSelected, final boolean hasFocus,
                    final int row, final int column) {
                final JLabel l = (JLabel)super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                l.setText(value != null ? format.format(value) : "");
                return l;
            }
        });
        // customization
        setBorder(null);
        // don't allow pasting into this view.
        getActionMap().remove(UIComponentMenuBar.EditMenuBuilder.PASTE);
        // should probably disable dropping into this view too? maybe it's already gone.
    }

    // table format interface.
    @Override
    public int getColumnCount() {
        return this.originalFormat.getColumnCount();
    }

    @Override
    public String getColumnName(final int arg0) {
        switch(arg0) {
            case 2: return "Position";
            case 3: return "Offset";
            default:
                return this.originalFormat.getColumnName(arg0);             
        }
    }

    @Override
    public Class getColumnClass(final int arg0) {
        switch(arg0) {
            case 2: return String.class;
            case 3: return Double.class;
            default:
                return this.originalFormat.getColumnClass(arg0);             
        }        
     
    }

    public Comparator getColumnComparator(final int arg0) {
        switch(arg0) {
            case 2: 
            case 3: return GlazedLists.comparableComparator();
            default:          
            return this.originalFormat.getColumnComparator(arg0);
        } 
            
    }

    public Object getColumnValue(final FileObjectView o, final int arg1) {               
       final AstroscopeFileObject afo = o.getAstroscopeFileObject();
       final TreeNode node = afo.getNode();
       
        switch(arg1) {

            case 2: 
                final String pos = node.getAttribute(AbstractRetriever.POS_ATTRIBUTE);
                return pos == null ? "" : pos;
            case 3: 
                final String offset = node.getAttribute(AbstractRetriever.OFFSET_DISPLAY_ATTRIBUTE);
                return offset == null ? null : new Double(offset);           
            default:          
                return this.originalFormat.getColumnValue(o, arg1);
        }
    }
    
    @Override
    protected String createFileTableToolTip(final FileObjectView item) {
        final AstroscopeFileObject afo = item.getAstroscopeFileObject();
        final HtmlBuilder b = new HtmlBuilder();
        b.append(afo.getNode().getAttribute(AbstractRetriever.TOOLTIP_ATTRIBUTE));
        b.br();
        
        b.append("URI: ").append(afo.getName().getURI());
        try {
            final FileContent content = afo.getContent();
            b.br()
            .append("Content type: ")
            .append(content.getContentInfo().getContentType());

        } catch (final FileSystemException ex) {
            // don't care
        }
        return b.toString();
    }

}
