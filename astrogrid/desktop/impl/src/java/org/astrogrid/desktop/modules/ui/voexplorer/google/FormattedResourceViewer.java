/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.astrogrid.acr.ivoa.Vosi;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.resource.CapabilityTester;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;

/** displays resource as formatted html.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20072:32:38 PM
 */ 
public class FormattedResourceViewer extends ResourceDisplayPane implements ResourceViewer {

	public FormattedResourceViewer(final BrowserControl browser, final RegistryBrowser regBrowser, CapabilityTester tester,Vosi vosi) {
		super(browser, regBrowser, tester,vosi);
		CSH.setHelpIDString(this, "reg.details");
		setPreferredSize(new Dimension(200,100));		
	}
	
	public void addTo(JTabbedPane t) {
		final JScrollPane scrollPane = new JScrollPane(this,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		t.addTab("Details", IconHelper.loadIcon("info16.png")
				, scrollPane, "Details of chosen resource");			
	}

}
