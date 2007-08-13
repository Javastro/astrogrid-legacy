/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.apache.html.dom.HTMLBuilder;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.system.CSH;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

/** A static list of files - you can just view them, but not interact or navigate.
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
    public FilesList(EventList files, final IconFinder icons) {
        super();
        setModel(new EventListModel(files));
        
        CSH.setHelpIDString(this,"files.list");
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        setVisibleRowCount(-1);
            

        
        setCellRenderer(new DefaultListCellRenderer() {
            Dimension dim = new Dimension(100,50); //@todo calculate this more nicely.
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);
            //@todo optimize this- I redo this every time.
            l.setVerticalTextPosition(JLabel.BOTTOM);
            l.setHorizontalTextPosition(JLabel.CENTER);
            l.setHorizontalAlignment(JLabel.CENTER);
            l.setPreferredSize(dim);
            FileObject f = (FileObject)value;
            final String name = f.getName().getBaseName();
            l.setText(WordUtils.wrap(name,14));
            l.setIcon(icons.find(f)); 
            return l;
        }
        });        
    }
    
    public String getToolTipText(MouseEvent e) {
        int index = locationToIndex(e.getPoint());
        ListModel dlm = getModel();
        ensureIndexIsVisible(index);
        FileObject item = (FileObject)dlm.getElementAt(index);
       if (item == null) {
           return "";
       }
       if (item instanceof DelegateFileObject) {
           item = ((DelegateFileObject)item).getDelegateFile();
       }
       HtmlBuilder sb = new HtmlBuilder();
       sb.append(item.getName().getFriendlyURI());
       try {
       FileContent content = item.getContent();
       sb.br().append(new Date(content.getLastModifiedTime()));
       if (item.getType().hasContent()) {
           long sz = content.getSize();
           sb.br().append(sz < 1024 && sz > 0 ? 1 : sz / 1024).append("KB");
           sb.br().append(content.getContentInfo().getContentType());
       }
       } catch (FileSystemException ex) {
           // don't care
       }
       return sb.toString();
   }

}