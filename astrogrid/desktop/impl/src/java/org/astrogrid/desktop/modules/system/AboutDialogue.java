/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Dimension;

import javax.swing.event.HyperlinkListener;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;

/** Show an about dialogue.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 5, 20072:14:37 PM
 */
public class AboutDialogue implements Runnable {
    private final String content;
    private final HyperlinkListener hyper;
    
    public void run() {
        final ResultDialog rd = new ResultDialog(content);
        rd.getResultDisplay().addHyperlinkListener(hyper);
        rd.setSize(new Dimension(500,600));
        rd.getBanner().setVisible(true);
        rd.getBanner().setTitle("About VODesktop");
        rd.getBanner().setSubtitleVisible(false);
        rd.getBanner().setIcon(IconHelper.loadIcon("logo48x48.png"));
        rd.centerOnScreen();
        rd.setVisible(true);        
        rd.toFront();
    }

    public AboutDialogue(final HyperlinkListener hyper, final String content) {
        super();
        this.hyper = hyper;
        this.content = content;
    }

}
