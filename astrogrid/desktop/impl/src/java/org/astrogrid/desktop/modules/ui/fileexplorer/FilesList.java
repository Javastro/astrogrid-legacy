/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
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
       //@todo add more info in here later.
       return item.getName().getFriendlyURI();
   }

}