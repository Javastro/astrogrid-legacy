/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.comp.ExternalViewerHyperlinkListener;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;

/** Show an about dialogue.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 5, 20072:14:37 PM
 */
public class AboutDialogue implements Runnable {
    private final BrowserControl browser;
    private final String content;
    
    public void run() {
        JEditorPane l = new JEditorPane();
        l.setContentType("text/html");
        l.setBorder(null);
        l.setEditable(false);
        l.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);      // this key is only defined on 1.5 - no effect on 1.4
        l.setFont(UIConstants.SANS_FONT);   
        l.addHyperlinkListener(new ExternalViewerHyperlinkListener(browser));
        l.setText(content);
        
        JScrollPane sp = new JScrollPane(l,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(new Dimension(400,400));
        JOptionPane.showMessageDialog(null,sp,"About VODesktop"
                ,JOptionPane.INFORMATION_MESSAGE
                ,IconHelper.loadIcon("logo48x48.png"));
    }

    public AboutDialogue(BrowserControl browser, String content) {
        super();
        this.browser = browser;
        this.content = content;
    }

}
