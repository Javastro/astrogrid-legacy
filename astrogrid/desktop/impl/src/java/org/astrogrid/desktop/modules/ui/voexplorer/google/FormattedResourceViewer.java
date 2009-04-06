/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.HyperlinkListener;

import org.astrogrid.acr.ivoa.Vosi;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.resource.CapabilityTester;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;

/** Displays resource as formatted HTML in VOExplorer.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20072:32:38 PM
 */ 
public class FormattedResourceViewer extends ResourceDisplayPane implements ResourceViewer {

	public FormattedResourceViewer(final HyperlinkListener hyper, final CapabilityTester tester,final Vosi vosi) {
		super(hyper, tester,vosi);
		CSH.setHelpIDString(this, "reg.details");
		setPreferredSize(new Dimension(200,100));		
	}
	
	public void addTo(final JTabbedPane t) {
		final JScrollPane scrollPane = new JScrollPane(this,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		t.addTab("Details", IconHelper.loadIcon("info16.png")
				, scrollPane, "Details of chosen resource");			
	}

}
