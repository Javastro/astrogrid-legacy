/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Dimension;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.ui.comp.ExternalViewerHyperlinkListener;

/** Show an about dialogue.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 5, 20072:14:37 PM
 */
public class AboutDialogue implements Runnable {
    private final BrowserControl browser;
    private final String content;
    
    public void run() {
        ResultDialog rd = new ResultDialog(content);
        rd.getResultDisplay().addHyperlinkListener(new ExternalViewerHyperlinkListener(browser));
        rd.setSize(new Dimension(500,600));
        rd.getBanner().setVisible(true);
        rd.getBanner().setTitle("About VODesktop");
        rd.getBanner().setSubtitleVisible(false);
        rd.getBanner().setIcon(IconHelper.loadIcon("logo48x48.png"));
        rd.centerOnScreen();
        rd.setVisible(true);        
        rd.toFront();
    }

    public AboutDialogue(BrowserControl browser, String content) {
        super();
        this.browser = browser;
        this.content = content;
    }

}
