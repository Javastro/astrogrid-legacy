/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ivoa.resource.HasCoverage;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.StcResourceProfile;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.util.DomHelper;

/** static button class that displays coverage in some way.
 * 
 * @todo implement placeholder for now - work out how to implement later.
 *  * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 22, 20072:23:19 PM
 */
public class ShowCoverageButton extends ResourceDisplayPaneEmbeddedButton implements ActionListener {
    /**
     * 
     */
    public ShowCoverageButton() {
        setText("Show Coverage");
           addActionListener(this);
           CSH.setHelpIDString(this,"reg.display.coverage");
    }


    public void actionPerformed(ActionEvent e) {
        
        Resource r = getResourceDisplayPane(e).getCurrentResource();
        if (! (r instanceof HasCoverage)) {
            // very unlikley.
            throw new RuntimeException("Programming error - current resource has no coverage");
        }
        StcResourceProfile stc = ((HasCoverage)r).getCoverage().getStcResourceProfile();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DomHelper.PrettyDocumentToStream(stc.getStcDocument(),bos);

        ResultDialog dialog = ResultDialog.newResultDialog(this,"");
        dialog.getResultDisplay().setContentType("text/plain");
        dialog.getResultDisplay().setText(
                "Temporary - need a formatter here\n" 
                +  bos);
        dialog.getResultDisplay().setCaretPosition(0);
        dialog.setSize(550,400);
        dialog.setVisible(true);

    }
}
