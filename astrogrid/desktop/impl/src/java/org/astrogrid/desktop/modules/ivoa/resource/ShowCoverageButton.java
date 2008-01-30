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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;

/** static button class that displays coverage in some way.
 * 
 * necessarily static so that it can be inserted
 * via a 'object' html tag with minimal hassle.
 * 
 * @todo implement placeholder for now - work out how to implement later.
 *  * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 22, 20072:23:19 PM
 */
public class ShowCoverageButton extends JButton implements ActionListener {
    /**
     * 
     */
    public ShowCoverageButton() {
        setText("Show Coverage");
           addActionListener(this);
    }

    
    private String doc;
    
    public final String getDoc() {
        return this.doc;
    }

    public final void setDoc(String doc) {
        this.doc = doc;
    }

    public void actionPerformed(ActionEvent e) {
        ResultDialog dialog = ResultDialog.newResultDialog(this,"");
        dialog.getResultDisplay().setContentType("text/plain");
        dialog.getResultDisplay().setText(
                "Temporary - need a formatter here\n" 
                +  doc);
        dialog.getResultDisplay().setCaretPosition(0);
        dialog.setSize(550,400);
        dialog.setVisible(true);

    }
}
