/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;

import org.apache.commons.lang.time.StopWatch;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceFormatter;
import org.astrogrid.desktop.modules.ui.comp.ExternalViewerHyperlinkListener;

/** displays resource as formatted html.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20072:32:38 PM
 */
public class FormattedResourceViewer extends JEditorPane implements ResourceViewer {

	public FormattedResourceViewer(final BrowserControl browser, final RegistryBrowser regBrowser) {
		super();
		setContentType("text/html");
		setBorder(BorderFactory.createEmptyBorder());
		setEditable(false);
		putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);		// this key is only defined on 1.5 - no effect on 1.4
        setFont(Font.decode("Helvetica"));		
		addHyperlinkListener(new ExternalViewerHyperlinkListener(browser, regBrowser));
		clear();
		setPreferredSize(new Dimension(200,100));		
	}
	
	public void clear() {
		setText("<html><body></body></html>");
	}
	
	public void display(Resource res) {
		StopWatch st = new StopWatch();
		st.start();
		final String html = ResourceFormatter.renderResourceAsHTML(res);
		st.split();
		String split = st.toSplitString();
		setText(html);
		st.stop();
		System.out.println(split);
		System.out.println(st.toString());
		setCaretPosition(0);		
	}

	public JComponent getComponent() {
		return this;
	}

}
