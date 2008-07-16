/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;

import org.astrogrid.acr.ivoa.resource.HasCoverage;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.StcResourceProfile;
import org.astrogrid.desktop.modules.dialogs.ConfirmDialog;
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
        
        final Resource r = getResourceDisplayPane(e).getCurrentResource();
        if (! (r instanceof HasCoverage)) {
            // very unlikley.
            throw new org.astrogrid.desktop.modules.system.ProgrammerError("Current resource has no coverage");
        }
        ConfirmDialog.newConfirmDialog(this,"Not yet implemented"
                ,"We can't format the coverage information yet. Show the XML source?"
                ,new Runnable() {

                    public void run() {
                        StcResourceProfile stc = ((HasCoverage)r).getCoverage().getStcResourceProfile();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        DomHelper.PrettyDocumentToStream(stc.getStcDocument(),bos);
                        
                        ResultDialog dialog = ResultDialog.newResultDialog(ShowCoverageButton.this,"");
                        dialog.getResultDisplay().setContentType("text/plain");
                        dialog.getResultDisplay().setText(bos.toString());
                        dialog.getResultDisplay().setCaretPosition(0);
                        dialog.setSize(550,400);
                        dialog.setVisible(true);
                    }
        }).show();

    }
}
