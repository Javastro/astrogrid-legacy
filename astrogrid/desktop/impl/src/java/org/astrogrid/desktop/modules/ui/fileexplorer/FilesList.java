/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventListModel;

/** A static {@code JList} of files. - you can just view them, but not interact or navigate.
 *  
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 24, 200711:51:48 AM
 */
public class FilesList extends JList {
    /**
     * Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(FilesList.class);

    /**
     * @param icons 
     * @param currentSelection 
     * @param files 
     * 
     */
    public FilesList(final EventList<FileObjectView> files) {
        super();
        setModel(new EventListModel<FileObjectView>(files));
        
        CSH.setHelpIDString(this,"files.icons");
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        setVisibleRowCount(-1);
            

        
        setCellRenderer(new DefaultListCellRenderer() {
            Dimension dim = new Dimension(100,50); //@todo calculate this more nicely.
        @Override
        public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
            final JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);
            //@todo optimize this- I redo this every time.
            l.setVerticalTextPosition(JLabel.BOTTOM);
            l.setHorizontalTextPosition(JLabel.CENTER);
            l.setHorizontalAlignment(JLabel.CENTER);
            l.setPreferredSize(dim);
            final FileObjectView f = (FileObjectView)value;
            final String name = f.getBasename();
            l.setText(WordUtils.wrap(name,14));
            l.setIcon(f.getIcon()); 
            return l;
        }
        });        
    }
    
    @Override
    public String getToolTipText(final MouseEvent e) {
        final int index = locationToIndex(e.getPoint());
        if (index < 0) {
            return null;
        }
        final ListModel dlm = getModel();
        ensureIndexIsVisible(index);
        final FileObjectView item = (FileObjectView)dlm.getElementAt(index);
       return createToolTipFromFileObject(item);
   }

    public static String createToolTipFromFileObject(final FileObjectView fv) {

        if (fv == null) {
            return null;
        }
        final HtmlBuilder sb = new HtmlBuilder();        
        { // take the URI from the AFO, if present.
            final FileObject afo = fv.getAstroscopeFileObject();
            sb.append("URI: ");
            if (afo != null) {
                sb.append(afo.getName().getURI());
            } else {
                sb.append(fv.getUri());
            }
        }

        // all else comes from the view.           
        sb.br()
        .append("Last modified: ")
        .append(fv.getLastModified());
        if (fv.getType().hasContent()) {
            final long sz = fv.getSize();
            sb.br()
            .append("Size: ")
            .append(sz == AstroscopeFileObject.UNKNOWN_SIZE ? "unknown" : FileUtils.byteCountToDisplaySize(sz));
            sb.br()
            .append("Content type: ")
            .append(fv.getContentType());
        }
        return sb.toString();
    }

}