/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.astrogrid.desktop.icons.IconHelper;

/**
 *  static button class that attempts to flip tabs
 * 
 * necessarily static so that it can be inserted
 * via a 'object' html tag with minimal hassle.
 * 
 * 
 * implementation relies on the 'table metata' view being the one following the formatted resource view.

 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 22, 20072:23:19 PM
 */
public class ShowMetadataButton extends ResourceDisplayPaneEmbeddedButton implements ActionListener {
    /**
     * 
     */
    public ShowMetadataButton() {
           setText("Show Table Metadata");
           setIcon(IconHelper.loadIcon("table16.png"));           
           addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        JTabbedPane tabs = (JTabbedPane)SwingUtilities.getAncestorOfClass(JTabbedPane.class,(Component)e.getSource());
        if (tabs != null) {
            tabs.setSelectedIndex(tabs.getSelectedIndex()+1);
        }
    }
}
